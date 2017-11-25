package com.Stranded.commands.war.util;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class StartWar {

    private static void startRandomThemeWar(Main p, Files warData, Files warIslands, Files islands, ArrayList<String> island1, ArrayList<String> island2, String islandName1, String islandName2) {

        ArrayList<String> islandIDList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.island1." + islandName1 + ".islandList");

        for (String players : island1) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("everybody has accepted this war");
        }
        for (String players : island2) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("everybody has accepted this war");
        }

        int island1size = 0;
        int island2size = 0;

        for (String players : island1) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) island1size++;
        }
        for (String players : island2) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) island2size++;
        }

        for (String islandNew : islandIDList) {
            if (warIslands.getConfig().getBoolean("warIslands." + islandNew + ".inUse"))
                islandIDList.remove(islandNew);
        }
        if (islandIDList.size() == 0) {
            for (String players : island1) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, all available islands are in use");
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, all available islands are in use");
            }
            Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

            warData.getConfig().set("war.pending.island1." + islandName1, null);
            warData.getConfig().set("war.pending.island2." + islandName2, null);
            warData.saveConfig();
            return;
        }

        for (String islandNew : islandIDList) {
            int min = warIslands.getConfig().getInt("warIslands.island." + islandNew + ".minPlayers");
            int max = warIslands.getConfig().getInt("warIslands.island." + islandNew + ".maxPlayers");

            if (island1size > max || island2size > max || island1size < min || island2size < min) {
                islandIDList.remove(islandNew);
            }
            String blueUUID = warIslands.getConfig().getString("warIslands.island." + islandNew + ".armorStand.blue");
            String redUUID = warIslands.getConfig().getString("warIslands.island." + islandNew + ".armorStand.red");
            if (Bukkit.getEntity(UUID.fromString(blueUUID)) == null || Bukkit.getEntity(UUID.fromString(redUUID)) == null) {
                islandIDList.remove(islandNew);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("Stranded.WarIsland")) {
                        player.sendMessage("WARNING: the war theme " + islandNew.split("\\.")[0] + " island id " + islandNew.split("\\.")[1] +
                                " has been corrupted, missing: armorStand.\ndelete this island and create it again");
                    }
                }
            }
            String blueVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandNew + ".nexus.blue");
            String redVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandNew + ".nexus.red");
            if (Bukkit.getEntity(UUID.fromString(blueVillagerUUID)) == null || Bukkit.getEntity(UUID.fromString(redVillagerUUID)) == null) {
                islandIDList.remove(islandNew);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("Stranded.WarIsland")) {
                        player.sendMessage("WARNING: the war theme " + islandNew.split("\\.")[0] + " island id " + islandNew.split("\\.")[1] +
                                " has been corrupted, missing: villager.\ndelete this island and create it again");
                    }
                }
            }
        }
        boolean island1cancel = false;
        if (islandIDList.size() == 0) {
            for (String players : island1) {
                if (island1.size() == island1size) {
                    if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                        Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, to many people logged off");
                        Bukkit.getPlayer(UUID.fromString(players)).sendMessage("other islands are still able to accept your war request");
                        island1cancel = true;
                    }
                } else {
                    if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                        Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, to many people logged off");
                    }
                }
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, to many people logged off");
            }

            if (island1cancel) {
                warData.getConfig().set("war.pending.island2." + islandName2, null);
                warData.saveConfig();
            } else {
                //canceled islandPendingList
                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

                warData.getConfig().set("war.pending.island1." + islandName1, null);
                warData.getConfig().set("war.pending.island2." + islandName2, null);
                warData.saveConfig();
            }
            return;
        }

        //canceled islandPendingList
        Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

        warData.getConfig().set("war.pending.island1." + islandName1, null);
        warData.getConfig().set("war.pending.island2." + islandName2, null);
        warData.saveConfig();

        String finalIslandID = islandIDList.get(new Random().nextInt(islandIDList.size()));

        warIslands.getConfig().set("warIslands.island." + finalIslandID + ".inUse", true);

        Location blueLocation = (Location) warIslands.getConfig().get("warIslands.island." + finalIslandID + ".spawn.blue");
        Location redLocation = (Location) warIslands.getConfig().get("warIslands.island." + finalIslandID + ".spawn.red");

        int r = new Random().nextInt(2);
        if (r > 1) {
            for (String players : island1) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(blueLocation);
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(redLocation);
            }
        } else {
            for (String players : island1) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(redLocation);
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(blueLocation);
            }
        }

        int id = 0;
        while (true) {

            if (!warData.getConfig().contains("war.war." + id)) {

                if (r > 1) {
                    warData.getConfig().set("war.war." + id + ".blue.players", island1);
                    warData.getConfig().set("war.war." + id + ".red.players", island2);

                    warData.getConfig().set("war.war." + id + ".blue.islandName", islandName1);
                    warData.getConfig().set("war.war." + id + ".red.islandName", islandName2);

                } else {
                    warData.getConfig().set("war.war." + id + ".red", island1);
                    warData.getConfig().set("war.war." + id + ".blue", island2);

                    warData.getConfig().set("war.war." + id + ".red.islandName", islandName1);
                    warData.getConfig().set("war.war." + id + ".blue.islandName", islandName2);
                }

                warData.getConfig().set("war.war." + id + ".blue.islandName", islandName1);
                warData.getConfig().set("war.war." + id + ".red.islandName", islandName2);

                warData.getConfig().set("war.war." + id + ".blueSpawn", blueLocation);
                warData.getConfig().set("war.war." + id + ".redSpawn", redLocation);


                String blueVillagerUUID = warIslands.getConfig().getString("warIslands.island." + id + ".nexus.blue");
                String redVillagerUUID = warIslands.getConfig().getString("warIslands.island." + id + ".nexus.red");

                warData.getConfig().set("war.war." + id + ".blue.VillagerUUID", blueVillagerUUID);
                warData.getConfig().set("war.war." + id + ".red.VillagerUUID", redVillagerUUID);
                warData.getConfig().set("war.war." + id + ".warIsland." + finalIslandID.split("\\.")[0], finalIslandID.split("\\.")[1]);

                ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("playersInWar");

                String blueUUID = warIslands.getConfig().getString("warIslands.island." + finalIslandID + ".armorStand.blue");
                String redUUID = warIslands.getConfig().getString("warIslands.island." + finalIslandID + ".armorStand.red");

                warData.getConfig().set("war.war." + id + ".blue.ArmorStandUUID", blueUUID);
                warData.getConfig().set("war.war." + id + ".red.ArmorStandUUID", redUUID);

                ArmorStand blueAS = (ArmorStand) Bukkit.getEntity(UUID.fromString(blueUUID));
                ArmorStand redAS = (ArmorStand) Bukkit.getEntity(UUID.fromString(redUUID));

                int healthIsland1 = islands.getConfig().getInt("islands." + islandName1 + ".nexusHealth");
                int healthIsland2 = islands.getConfig().getInt("islands." + islandName2 + ".nexusHealth");

                list.addAll(island1);
                list.addAll(island2);
                p.getConfig().set("playersInWar", list);
                p.saveConfig();

                if (r > 1) {
                    blueAS.setCustomName("§9health: " + healthIsland1);
                    redAS.setCustomName("§chealth: " + healthIsland2);
                } else {
                    blueAS.setCustomName("§9health: " + healthIsland2);
                    redAS.setCustomName("§chealth: " + healthIsland1);
                }

                break;
            }

            id++;
        }

        warData.saveConfig();
    }

    private static void startThemeWar(Main p, Files warData, Files warIslands, Files islands, ArrayList<String> island1, ArrayList<String> island2, String islandTheme, String islandName1, String islandName2) {
        ArrayList<String> islandIDList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.island1." + islandName1 + ".islandList");

        for (String players : island1) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("everybody has accepted this war");
        }
        for (String players : island2) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("everybody has accepted this war");
        }

        int island1size = 0;
        int island2size = 0;

        for (String players : island1) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) island1size++;
        }
        for (String players : island2) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) island2size++;
        }

        for (String islandNew : islandIDList) {
            if (warIslands.getConfig().getBoolean("warIslands." + islandTheme + "." + islandNew + ".inUse"))
                islandIDList.remove(islandNew);
        }
        if (islandIDList.size() == 0) {
            for (String players : island1) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, all available islands are in use");
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, all available islands are in use");
            }
            Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

            warData.getConfig().set("war.pending.island1." + islandName1, null);
            warData.getConfig().set("war.pending.island2." + islandName2, null);
            warData.saveConfig();
            return;
        }

        for (String islandNew : islandIDList) {

            int min = warIslands.getConfig().getInt("warIslands.island." + islandTheme + "." + islandNew + ".minPlayers");
            int max = warIslands.getConfig().getInt("warIslands.island." + islandTheme + "." + islandNew + ".maxPlayers");

            if (island1size > max || island2size > max || island1size < min || island2size < min) {
                islandIDList.remove(islandNew);
            }
            String blueUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + islandNew + ".armorStand.blue");
            String redUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + islandNew + ".armorStand.red");
            if (Bukkit.getEntity(UUID.fromString(blueUUID)) == null || Bukkit.getEntity(UUID.fromString(redUUID)) == null) {
                islandIDList.remove(islandNew);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("Stranded.WarIsland")) {
                        player.sendMessage("WARNING: the war theme " + islandTheme + " island id " + islandNew + " has been corrupted, missing: armorStand.\ndelete this island and create it again");
                    }
                }

            }
            String blueVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + islandNew + ".nexus.blue");
            String redVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + islandNew + ".nexus.red");
            if (Bukkit.getEntity(UUID.fromString(blueVillagerUUID)) == null || Bukkit.getEntity(UUID.fromString(redVillagerUUID)) == null) {
                islandIDList.remove(islandNew);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("Stranded.WarIsland")) {
                        player.sendMessage("WARNING: the war theme " + islandTheme + " island id " + islandNew + " has been corrupted, missing: nexus.\ndelete this island and create it again");
                    }
                }
            }
        }
        boolean island1cancel = false;
        if (islandIDList.size() == 0) {
            for (String players : island1) {
                if (island1.size() == island1size) {
                    if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                        Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, to many people logged off");
                        Bukkit.getPlayer(UUID.fromString(players)).sendMessage("other islands are still able to accept your war request");
                        island1cancel = true;
                    }
                } else {
                    if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                        Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, to many people logged off");
                    }
                }
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null)
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("war is canceled, to many people logged off");
            }

            if (island1cancel) {
                warData.getConfig().set("war.pending.island2." + islandName2, null);
                warData.saveConfig();
            } else {
                //canceled islandPendingList
                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));
                warData.getConfig().set("war.pending.island1." + islandName1, null);
                warData.getConfig().set("war.pending.island2." + islandName2, null);
                warData.saveConfig();
            }
            return;
        }

        //canceled islandPendingList
        Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

        warData.getConfig().set("war.pending.island1." + islandName1, null);
        warData.getConfig().set("war.pending.island2." + islandName2, null);
        warData.saveConfig();

        String finalIslandID = islandIDList.get(new Random().nextInt(islandIDList.size()));

        warIslands.getConfig().set("warIslands.island." + islandTheme + "." + finalIslandID + ".inUse", true);

        Location blueLocation = (Location) warIslands.getConfig().get("warIslands.island." + islandTheme + "." + finalIslandID + ".spawn.blue");
        Location redLocation = (Location) warIslands.getConfig().get("warIslands.island." + islandTheme + "." + finalIslandID + ".spawn.red");

        int r = new Random().nextInt(2);
        if (r > 1) {
            for (String players : island1) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(blueLocation);
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(redLocation);
            }
        } else {
            for (String players : island1) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(redLocation);
            }
            for (String players : island2) {
                if (Bukkit.getPlayer(UUID.fromString(players)) == null) {
                    continue;
                }
                Bukkit.getPlayer(UUID.fromString(players)).teleport(blueLocation);
            }
        }

        int id = 0;
        while (true) {

            if (!warData.getConfig().contains("war.war." + id)) {

                if (r > 1) {
                    warData.getConfig().set("war.war." + id + ".blue.players", island1);
                    warData.getConfig().set("war.war." + id + ".red.players", island2);

                    warData.getConfig().set("war.war." + id + ".blue.islandName", islandName1);
                    warData.getConfig().set("war.war." + id + ".red.islandName", islandName2);
                } else {
                    warData.getConfig().set("war.war." + id + ".red", island1);
                    warData.getConfig().set("war.war." + id + ".blue", island2);

                    warData.getConfig().set("war.war." + id + ".red.islandName", islandName1);
                    warData.getConfig().set("war.war." + id + ".blue.islandName", islandName2);
                }

                warData.getConfig().set("war.war." + id + ".blue.islandName", islandName1);
                warData.getConfig().set("war.war." + id + ".red.islandName", islandName2);

                warData.getConfig().set("war.war." + id + ".blueSpawn", blueLocation);
                warData.getConfig().set("war.war." + id + ".redSpawn", redLocation);

                String blueUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + finalIslandID + ".armorStand.blue");
                String redUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + finalIslandID + ".armorStand.red");

                warData.getConfig().set("war.war." + id + ".blue.ArmorStandUUID", blueUUID);
                warData.getConfig().set("war.war." + id + ".red.ArmorStandUUID", redUUID);

                String blueVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + id + ".nexus.blue");
                String redVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + id + ".nexus.red");

                warData.getConfig().set("war.war." + id + ".blue.VillagerUUID", blueVillagerUUID);
                warData.getConfig().set("war.war." + id + ".red.VillagerUUID", redVillagerUUID);
                warData.getConfig().set("war.war." + id + ".warIsland." + islandTheme, finalIslandID);

                ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("playersInWar");

                list.addAll(island1);
                list.addAll(island2);
                p.getConfig().set("playersInWar", list);
                p.saveConfig();

                ArmorStand blueAS = (ArmorStand) Bukkit.getEntity(UUID.fromString(blueUUID));
                ArmorStand redAS = (ArmorStand) Bukkit.getEntity(UUID.fromString(redUUID));

                int healthIsland1 = islands.getConfig().getInt("islands." + islandName1 + ".nexusHealth");
                int healthIsland2 = islands.getConfig().getInt("islands." + islandName2 + ".nexusHealth");


                if (r > 1) {
                    blueAS.setCustomName("§9health: " + healthIsland1);
                    redAS.setCustomName("§chealth: " + healthIsland2);
                } else {
                    blueAS.setCustomName("§9health: " + healthIsland2);
                    redAS.setCustomName("§chealth: " + healthIsland1);
                }

                break;
            }

            id++;
        }

        warData.saveConfig();
    }

    public static void startWar(String islandName2, Main p) {

        Files warIslands = new Files(p, "warIslands.yml");
        Files warData = new Files(p, "warData.yml");
        Files islands = new Files(p, "islands.yml");

        String islandName1 = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

        ArrayList<String> island1 = new ArrayList<>();
        ArrayList<String> island2 = new ArrayList<>();
        island1.addAll(warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false));
        island2.addAll(warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false));

        String islandTheme = warData.getConfig().getString("war.pending.island1." + islandName1 + ".theme");

        if (islandTheme.equalsIgnoreCase("random")) {
            startRandomThemeWar(p, warData, warIslands, islands, island1, island2, islandName1, islandName2);
        } else {
            startThemeWar(p, warData, warIslands, islands, island1, island2, islandTheme, islandName1, islandName2);
        }
    }
}
