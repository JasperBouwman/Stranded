package com.Stranded;

import com.Stranded.border.warLimitations.WarLimitations;
import com.Stranded.commands.*;
import com.Stranded.commands.tabComplete.IslandTabComplete;
import com.Stranded.commands.tabComplete.TowerTabComplete;
import com.Stranded.commands.tabComplete.WarIslandTabComplete;
import com.Stranded.commands.tabComplete.WarTabComplete;
import com.Stranded.effects.PlayerEffects;
import com.Stranded.effects.events.BlockBreak;
import com.Stranded.effects.events.HitKill;
import com.Stranded.effects.events.WalkRun;
import com.Stranded.events.*;
import com.Stranded.gamble.InvClickEvent;
import com.Stranded.border.islandBorder.IslandBorder;
import com.Stranded.mapsUtil.MapTool;
import com.Stranded.nexus.InventoryEvent;
import com.Stranded.nexus.VillagerInteract;
import com.Stranded.towers.RemoveTower;
import com.Stranded.towers.TowerManager;
import com.Stranded.towers.events.BlockPlace;
import com.Stranded.towers.events.PlayerInteract;
import com.Stranded.towers.events.Tnt;
import com.Stranded.worldGeneration.DefaultIslandGenerator;
import com.Stranded.worldGeneration.WorldGenerate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Stranded.GettingFiles.getFiles;

public class Main extends JavaPlugin {

    public static int reloadHolds = 0;
    public static boolean reloadPending = false;

    private static String className;
    public static void println(String text) {
        int line = ___8d4rd3148796d_Xaf();
        System.out.println(text + " (" + className + ":" + line + ")");
    }
    private static int ___8d4rd3148796d_Xaf() {
        boolean thisOne = false;
        int thisOneCountDown = 1;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            String methodName = element.getMethodName();
            int lineNum = element.getLineNumber();
            if (thisOne && (thisOneCountDown == 0)) {
                className = element.getClassName();
                return lineNum;
            } else if (thisOne) {
                thisOneCountDown--;
            }
            if (methodName.equals("___8d4rd3148796d_Xaf")) {
                thisOne = true;
            }
        }
        return -1;
    }

    //todo add permissions
    //todo finish /lootTable
    //todo finish /warIsland box
    //todo test inv type
    //todo set colors to: {edit, lootTable, trade, war, warIsland, other}
    //todo create towerBlock warIslands ???
    //todo add .contains to all {till /island done}
    //todo update towers to new TerrainSave
    //todo set lootBox in island/warIsland
    //todo add lootBox on island from sign '<lootBox> \n %lootTable%'

    //todo towerTeleport.java

    //todo redo /stranded reload

    //todo all in runnable update files ?done?

    //find filter tower: "//towersData"

    public static boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);

        return m.find();
    }

    public static void resetPlayerData(String player) {

        Player p = Bukkit.getPlayer(UUID.fromString(player));
        Files config = getFiles("config.yml");

        if (p == null) {


            config.getConfig().set("online.removeAll." + player, true);

            if (config.getConfig().contains("island." + player)) {
                String island = config.getConfig().getString("island." + player);
                Files islands = getFiles("islands.yml");
                for (String id : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    if (islands.getConfig().getString("island." + island + ".towers." + id + ".owner").equals(player)) {
                        RemoveTower.removeIslandTower(islands, island, id);
                    }
                }
                Files towers = getFiles("towers.yml");
                towers.getConfig().set("towers." + player, null);
                towers.saveConfig();
            }
        } else {
            p.setLevel(0);
            p.getInventory().clear();
            if (config.getConfig().contains("island." + player)) {
                String island = config.getConfig().getString("island." + player);
                Files islands = getFiles("islands.yml");
                for (String id : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    if (islands.getConfig().getString("island." + island + ".towers." + id + ".owner").equals(player)) {
                        RemoveTower.removeIslandTower(islands, island, id);
                    }
                }

                Files towers = getFiles("towers.yml");

                towers.getConfig().set("towers." + player, null);
                towers.saveConfig();

                Files playerData = getFiles( "playerData.yml");
                playerData.getConfig().set("walk." + player, null);
                playerData.getConfig().set("BlockBreak." + player, null);
                playerData.getConfig().set("HitKill." + player, null);
                playerData.getConfig().set("fly." + player, null);
                playerData.getConfig().set("flying.players." + player, null);
                playerData.saveConfig();
            }
        }
    }

    @Override
    public void onEnable() {

        new GettingFiles(this);

        registerCommands();
        registerEvents();
        enableRunnables();

        WorldGenerate.generate();
        saveDefaults();

        setDefaultIslands();

        new ServerPingEvent(this).setList();

        MapTool.loadMaps(this);

    }

    @Override
    public void onDisable() {

        MapTool.saveMapTools();

        Files warData = new Files(this, "warData.yml");

        for (String side : Arrays.asList("island1", "island2")) {
            if (!warData.getConfig().contains("war.pending." + side)) {
                continue;
            }
            for (String island : warData.getConfig().getConfigurationSection("war.pending." + side).getKeys(false)) {

                for (String player : warData.getConfig().getStringList("war.pending." + side + "." + island + ".players")) {
                    Player pl = Bukkit.getPlayer(UUID.fromString(player));

                    if (pl != null) {
                        pl.sendMessage("war has been canceled due to a reloading of the server");
                    }
                }
            }
        }


        warData.getConfig().set("war.pending", null);
        warData.saveConfig();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getOpenInventory().getTitle().startsWith("Gamble: ")) {
                p.closeInventory();
            }
        }

        new ServerPingEvent(this).saveList();

        for (Files file : getFiles()) {
            file.saveConfig(true);
        }
        this.saveConfig();

    }

    private void setDefaultIslands() {
        if (Bukkit.getWorld("Islands") != null) {
            Files islands = new Files(this, "islands.yml");
            if (islands.getConfig().contains("islandData.islandTypesCopied")) {
                if (!islands.getConfig().getBoolean("islandData.islandTypesCopied")) {
                    Bukkit.broadcastMessage("the default islands are now loading in the server");
                    new DefaultIslandGenerator(this);
                }
            } else {
                Bukkit.broadcastMessage("the default islands are now loading in the server");
                new DefaultIslandGenerator(this);
            }
        }
    }

    private void registerCommands() {
        getCommand("stranded").setExecutor(new Stranded(this));
        getCommand("nexus").setExecutor(new Nexus(this));
        getCommand("edit").setExecutor(new Edit());
        getCommand("war").setExecutor(new War(this));
        getCommand("island").setExecutor(new Island(this));
        getCommand("warIsland".toLowerCase()).setExecutor(new WarIsland(this));
        getCommand("tower").setExecutor(new Towers(this));
        getCommand("gamble").setExecutor(new Gamble(this));
        getCommand("chat").setExecutor(new Chat());
        getCommand("trade").setExecutor(new Trade());
        getCommand("lootTable".toLowerCase()).setExecutor(new LootTable(this));
        getCommand("chatFilter".toLowerCase()).setExecutor(new ChatFilter());
        getCommand("reply").setExecutor(new Reply());
        getCommand("towerTeleport".toLowerCase()).setExecutor(new towerTeleport(this));
        getCommand("map").setExecutor(new Map(this));

        getCommand("island").setTabCompleter(new IslandTabComplete(this));
        getCommand("war").setTabCompleter(new WarTabComplete(this));
        getCommand("warIsland".toLowerCase()).setTabCompleter(new WarIslandTabComplete(this));
        getCommand("tower").setTabCompleter(new TowerTabComplete(this));
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        //testPiston
        pm.registerEvents(new IslandBorder(this), this);
        pm.registerEvents(new WarLimitations(this), this);
        //effects
        pm.registerEvents(new BlockBreak(), this);
        pm.registerEvents(new WalkRun(), this);
        pm.registerEvents(new HitKill(), this);
        //nexus
        pm.registerEvents(new InventoryEvent(this), this);
        pm.registerEvents(new VillagerInteract(this), this);
        pm.registerEvents(new DamageEvent(this), this);
        //towers
        pm.registerEvents(new PlayerInteract(this), this);
        pm.registerEvents(new BlockPlace(), this);
        pm.registerEvents(new Tnt(), this);
        pm.registerEvents(new com.Stranded.towers.inventory.InventoryEvent(this), this);
        pm.registerEvents(new ChatEvent(), this);
        //other
        pm.registerEvents(new CobbleGenEvent(this), this);
        pm.registerEvents(new LoginEvent(this), this);
        pm.registerEvents(new TeleportEvent(this), this);
        //war
        pm.registerEvents(new DeathEvent(this), this);
        pm.registerEvents(new LogOffEvent(this), this);
        pm.registerEvents(new RespawnEvent(this), this);
        //gamble
        pm.registerEvents(new InvClickEvent(this), this);
        //serverPing
        pm.registerEvents(new ServerPingEvent(this), this);
        //trade
        pm.registerEvents(new com.Stranded.trade.events.InventoryEvent(this), this);
    }

    private void enableRunnables() {
        new PlayerEffects(this).effects();
        //this is also the runnable for the offset shower

        new TowerManager(this).Tower();
    }

    private void saveDefaults() {
        new Files(this, "pluginData.yml").saveDefaultConfig();
        new Files(this, "islands.yml").saveDefaultConfig();
        new Files(this, "warData.yml").saveDefaultConfig();
        new Files(this, "warIslands.yml").saveDefaultConfig();
        new Files(this, "chatFilter.yml").saveDefaultConfig();
    }
}
