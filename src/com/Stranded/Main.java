package com.Stranded;

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
import com.Stranded.islandBorder.events.BorderEvents;
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

public class Main extends JavaPlugin {

    public static int reloadHolds = 0;
    public static boolean reloadPending = false;

    private static String className;

    public static void println(String text) {
        int line = ___8drrd3148796d_Xaf();
        System.out.println(text + " (" + className + ":" + line + ")");
    }

    private static int ___8drrd3148796d_Xaf() {
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
            if (methodName.equals("___8drrd3148796d_Xaf")) {
                thisOne = true;
            }
        }
        return -1;
    }

    //todo check out permissions
    //todo update defaultIslands to new method
    //todo war islands cant contain blocks with a filled container
    //todo in island set lootBox with lootTable
    //todo finish /lootTable, /stranded
    //todo test inv type

    public static boolean containsSpecialCharacter(String s) {
        if (s == null || s.trim().isEmpty()) {
            return true;
        }
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);

        return m.find();
    }

    public static void resetPlayerData(String player, Main main) {

        Player p = Bukkit.getPlayer(UUID.fromString(player));

        if (p == null) {

            main.getConfig().set("online.removeAll." + player, true);

            if (main.getConfig().contains("island." + player)) {
                String island = main.getConfig().getString("island." + player);
                Files islands = new Files(main, "islands.yml");
                for (String id : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    if (islands.getConfig().getString("island." + island + ".towers." + id + ".owner").equals(player)) {
                        RemoveTower.removeIslandTower(main, islands, island, id);
                    }
                }
                Files towers = new Files(main, "towers.yml");
                towers.getConfig().set("towers." + player, null);
                towers.saveConfig();
            }
        } else {
            p.setLevel(0);
            p.getInventory().clear();
            if (main.getConfig().contains("island." + player)) {
                String island = main.getConfig().getString("island." + player);
                Files islands = new Files(main, "islands.yml");
                for (String id : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    if (islands.getConfig().getString("island." + island + ".towers." + id + ".owner").equals(player)) {
                        RemoveTower.removeIslandTower(main, islands, island, id);
                    }
                }

                Files towers = new Files(main, "towers.yml");

                towers.getConfig().set("towers." + player, null);
                towers.saveConfig();

                Files playerData = new Files(main, "playerData.yml");
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

        registerCommands();
        registerEvents();
        enableRunnables();

        WorldGenerate.generate();
        saveDefaults();

        setDefaultIslands();

        new ServerPingEvent(this).setList();
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

    @Override
    public void onDisable() {

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


    }

    private void registerCommands() {
        getCommand("stranded").setExecutor(new Stranded(this));
        getCommand("nexus").setExecutor(new Nexus(this));
        getCommand("edit").setExecutor(new Edit(this));
        getCommand("war").setExecutor(new War(this));
        getCommand("island").setExecutor(new Island(this));
        getCommand("warisland").setExecutor(new WarIsland(this));
        getCommand("tower").setExecutor(new Towers(this));
        getCommand("gamble").setExecutor(new Gamble(this));
        getCommand("chat").setExecutor(new Chat(this));
        getCommand("trade").setExecutor(new Trade());
        getCommand("lootTable".toLowerCase()).setExecutor(new LootTable(this));
        getCommand("chatFilter".toLowerCase()).setExecutor(new ChatFilter(this));
        getCommand("reply").setExecutor(new Reply(this));

        getCommand("island").setTabCompleter(new IslandTabComplete(this));
        getCommand("war").setTabCompleter(new WarTabComplete(this));
        getCommand("warisland").setTabCompleter(new WarIslandTabComplete(this));
        getCommand("tower").setTabCompleter(new TowerTabComplete(this));
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        //border
        pm.registerEvents(new BorderEvents(this), this);
        //effects
        pm.registerEvents(new BlockBreak(this), this);
        pm.registerEvents(new WalkRun(this), this);
        pm.registerEvents(new HitKill(this), this);
        //nexus
        pm.registerEvents(new InventoryEvent(this), this);
        pm.registerEvents(new VillagerInteract(this), this);
        pm.registerEvents(new DamageEvent(this), this);
        //towers
        pm.registerEvents(new PlayerInteract(), this);
        pm.registerEvents(new BlockPlace(this), this);
        pm.registerEvents(new Tnt(), this);
        pm.registerEvents(new com.Stranded.towers.inventory.InventoryEvent(this), this);
        pm.registerEvents(new ChatEvent(this), this);
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
        PlayerEffects pe = new PlayerEffects(this);
        //this is also the runnable for the offset shower
        pe.Effects();

        TowerManager tm = new TowerManager(this);
        tm.Tower();
    }

    private void saveDefaults() {
        new Files(this, "pluginData.yml").saveDefaultConfig();
        new Files(this, "islands.yml").saveDefaultConfig();
        new Files(this, "warData.yml").saveDefaultConfig();
        new Files(this, "warIslands.yml").saveDefaultConfig();
        new Files(this, "chatFilter.yml").saveDefaultConfig();
    }
}
