package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.worldGeneration.warIsland.WarIslandUtil;
import com.google.common.base.Joiner;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Arrays;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.commands.warIsland.Create.getVector;
import static com.Stranded.commands.warIsland.Create.spawnArmorstand;
import static com.Stranded.commands.warIsland.Create.spawnNexus;
import static com.Stranded.worldGeneration.warIsland.ImportWarIsland.importWarIsland;

public class Import extends CmdManager {
    @Override
    public String getName() {
        return "import";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland import
        //warIsland import <warIsland>

        if (args.length == 1) {

            player.sendMessage("these are the islands you can import: ");
            File[] listOfFiles = new File(p.getDataFolder().toString() + "/warIslands").listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String[] s = file.getName().split("\\.");
                        if (file.getName().endsWith(".yml")) {
                            player.sendMessage(Joiner.on('.').join(Arrays.asList(s).subList(0, s.length - 1)));
                        }
                    }
                }
            }
        } else if (args.length == 2) {

            File[] listOfFiles = new File(p.getDataFolder().toString() + "/warIslands").listFiles();

            if (listOfFiles != null) {

                for (File file : listOfFiles) {

                    String name = args[1];
                    name += ".yml";

                    if (file.getName().equalsIgnoreCase(name)) {

                            WarIslandUtil warIslandUtil = new WarIslandUtil();

                            player.sendMessage("getting file, the larger the file, the longer this will take");

                            if (warIslandUtil.testWarIslandValidation(p, name)) {
                                player.sendMessage("importing...");
                                try {
                                    new Thread(() -> {

                                        importWarIsland(warIslandUtil.getF(), player);

                                        Files warIslands = getFiles("warIslands.yml");
                                        String theme = warIslandUtil.getF().getConfig().getString("warIsland.theme");

                                        int id = 0;
                                        while (true) {
                                            if (!warIslands.getConfig().contains("warIslands.island." + theme + "." + id)) {
                                                break;
                                            } else {
                                                id++;
                                            }
                                        }
                                        Location blueSpawn = (Location) warIslandUtil.getF().getConfig().get("warIsland.blueSpawn");
                                        Location redSpawn = (Location) warIslandUtil.getF().getConfig().get("warIsland.redSpawn");
                                        Location first = (Location) warIslandUtil.getF().getConfig().get("warIsland.islandSize");
                                        Location second = (Location) warIslandUtil.getF().getConfig().get("warIsland.islandSize");

                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".spawn.blue", blueSpawn);
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".spawn.red", redSpawn);
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".islandSize.first", first);
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".islandSize.second", second);
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".maxPlayers", warIslandUtil.getF().getConfig().getInt("warIsland.maxPlayers"));
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".minPlayers", warIslandUtil.getF().getConfig().getInt("warIsland.minPlayers"));
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".inUse", false);
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".armorStand.blue", spawnArmorstand(blueSpawn, "§9"));
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".armorStand.red", spawnArmorstand(redSpawn, "§c"));

                                        Vector v = getVector(blueSpawn, redSpawn);

                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".nexus.blue", spawnNexus(blueSpawn, v));
                                        v = v.multiply(-1);
                                        warIslands.getConfig().set("warIslands.island." + theme + "." + id + ".nexus.red", spawnNexus(redSpawn, v));

                                        warIslands.saveConfig();

                                        player.sendMessage("done importing");
                                        System.out.println("done importing");


                                    }).start();

                                } catch (Exception e) {
                                    player.sendMessage("there went something wrong");
                                }

                            } else {
                                player.sendMessage("this file isn't a war island");
                            }

                        return;
                    }
                }
                player.sendMessage("This island isn't found, please make sure that you have typed it right");

            } else {
                player.sendMessage("no islands found");
            }

        } else {
            player.sendMessage("Usage: /warisland import \n/warisland import <warisland name>");
        }

    }
}
