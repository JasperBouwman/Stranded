package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.Stranded.worldGeneration.warIsland.ExportWarIsland;
import com.google.common.base.Joiner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;

import static com.Stranded.GettingFiles.getFiles;

public class Export extends CmdManager {
    @Override
    public String getName() {
        return "export";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {


        //warIsland export <name> <theme> <min> <max> [force] /*when you wan't to override the island you add the 'force' argument*/

        if (args.length == 5) {

            if (!args[1].endsWith(".yml")) {
                args[1] = args[1] + ".yml";
            }

            File file = new File(p.getDataFolder() + "/warIslands/", args[1]);

            if (file.exists()) {
                FancyMessage fm = new FancyMessage();

                fm.addText("This warIsland already exist, if you want to override this island add 'force' behind your command. Or click ", Colors.BLUE);
                fm.addText("here", Colors.DARK_AQUA);
                fm.addCommand("/warIsland " + Joiner.on(" ").join(Arrays.asList(args)) + " force");
                fm.addHover("/warIsland " + Joiner.on(" ").join(Arrays.asList(args)) + " force", Colors.DARK_AQUA);
                fm.addText(" to complete the export of your island", Colors.BLUE);

                fm.sendMessage(player);
            } else {
                testExportCommand(p, player, args, false);
            }
        } else if (args.length == 6) {

            if (!args[1].endsWith(".yml")) {
                args[1] = args[1] + ".yml";
            }

            if (args[5].equalsIgnoreCase("force")) {
                testExportCommand(p, player, args, true);
            } else {
                player.sendMessage("Usage: /warisland export <name> <theme> <min> <max> [force]");
            }
        } else {
            player.sendMessage("Usage: /warisland export <name> <theme> <min> <max> [force]");
        }
    }

    private static void testExportCommand(Main p, Player player, String[] args, boolean override) {
        int min = 1;
        int max = 2;

        boolean r = false;
        try {
            min = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage("your min value isn't a valid number");
            r = true;
        }
        try {
            max = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            player.sendMessage("your max value isn't a valid number");
            r = true;
        }
        if (r) return;

        if (min > max) {
            player.sendMessage("your min value is larger than your max value");
            return;
        }
        if (min <= 0) {
            player.sendMessage("your min value can't be smaller than 0");
            return;
        }

        Files warIslands = getFiles("warIslands.yml");

        String uuid = player.getUniqueId().toString();

        if (!warIslands.getConfig().contains("warIslands.offset." + uuid)) {
            player.sendMessage("you don't have an valid offset for a war Island, missing: all, use /warIsland wand to get a regen selector");
            return;
        }
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".blueSpawn")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: blue Spawn");
            r = true;
        }
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".redSpawn")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: red Spawn");
            r = true;
        }
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".first")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: first pos");
            r = true;
        }
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".second")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: second pos");
            r = true;
        }
        if (r) return;

        Location blueSpawn = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".blueSpawn");
        Location redSpawn = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".redSpawn");
        Location first = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".first");
        Location second = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".second");

        int maxX = Math.max(first.getBlockX(), second.getBlockX());
        int maxY = Math.max(first.getBlockY(), second.getBlockY());
        int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
        int minX = Math.min(first.getBlockX(), second.getBlockX());
        int minY = Math.min(first.getBlockY(), second.getBlockY());
        int minZ = Math.min(first.getBlockZ(), second.getBlockZ());

        int blueX = blueSpawn.getBlockX();
        int blueY = blueSpawn.getBlockY();
        int blueZ = blueSpawn.getBlockZ();
        int redX = redSpawn.getBlockX();
        int redY = redSpawn.getBlockY();
        int redZ = redSpawn.getBlockZ();

        if (blueX > maxX || blueX < minX || blueY > maxY || blueY < minY || blueZ > maxZ || blueZ < minZ) {
            player.sendMessage("your blue spawn is not in your selected war island");
            r = true;
        }
        if (redX > maxX || redX < minX || redY > maxY || redY < minY || redZ > maxZ || redZ < minZ) {
            player.sendMessage("your red spawn is not in your selected war island");
            r = true;
        }
        if (r) return;

        Location blueTmp = blueSpawn.clone();
        for (int i = 0; i < 2; i++) {
            blueTmp.setY(blueTmp.getBlockY() + 1);
            if (!blueTmp.getBlock().getType().equals(Material.AIR)) {
                player.sendMessage("the 2 blocks above your blue spawn must be air so there could be spawn a Nexus");
                r = true;
            }
        }
        Location redTmp = redSpawn.clone();
        for (int i = 0; i < 2; i++) {
            redTmp.setY(redTmp.getBlockY() + 1);
            if (!redTmp.getBlock().getType().equals(Material.AIR)) {
                player.sendMessage("the 2 blocks above your red spawn must be air so there could be spawn a Nexus");
                r = true;
            }
        }
        if (r) return;

        if (blueTmp.getBlockY() > maxY) {
            player.sendMessage("the place for the blue nexus is not in your selected war island");
            r = true;
        }
        if (redTmp.getBlockY() > maxY) {
            player.sendMessage("the place for the red nexus is not in your selected war island");
            r = true;
        }
        if (r) return;

        World blueWorld = blueSpawn.getWorld();
        World redWorld = redSpawn.getWorld();
        World firstWorld = first.getWorld();
        World secondWorld = second.getWorld();

        if (!blueWorld.equals(redWorld) || !blueWorld.equals(firstWorld) || !blueWorld.equals(secondWorld)) {
            player.sendMessage("some of your selections aren't in the same world");
            return;
        }

        if (args[2].equalsIgnoreCase("random")) {
            player.sendMessage("please do not use this theme name, this is used for players to choose a random theme");
            return;
        }

        if (override) {
            File file = new File(p.getDataFolder() + "/warIslands/" + args[1]);
            boolean delete = file.delete();
            if (!delete && file.exists()) {
                player.sendMessage("the old war island couldn't be deleted");
                return;
            }
        }

        Files island = new Files(p, args[1]);
        island.setWarIsland();

        player.sendMessage("starting with exporting");

        warIslands.getConfig().set("warIslands.offset." + uuid, null);
        warIslands.saveConfig();

        new ExportWarIsland().exportWarIsland(island, args[2], min, max, blueSpawn, redSpawn, first, second, player);

    }

}
