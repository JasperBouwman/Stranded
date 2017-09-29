package com.Stranded.commands.war.util;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class WarUtil {

    public static boolean testIfIsInWarWithComments(Main p, Player player) {

        Files warData = new Files(p, "warData.yml");
        String ownIslandName = p.getConfig().getString("island." + player.getName());

        if (warData.getConfig().contains("war.pending.island1." + ownIslandName)) {
            player.sendMessage("your island is already trying to start a war");
            return true;
        }
        if (warData.getConfig().contains("war.pending.island2." + ownIslandName)) {
            player.sendMessage("your island is already trying to start a war");
            return true;
        }

        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

            if (warData.getConfig().getString("war.war." + warID + ".blue.islandName").equals(ownIslandName)) {
                player.sendMessage("your island is already in a war");
                return true;
            }
            if (warData.getConfig().getString("war.war." + warID + ".red.islandName").equals(ownIslandName)) {
                player.sendMessage("your island is already in a war");
                return true;
            }
        }
        return false;
    }

    public static int testIfIsInWar(Main p, Player player) {

        Files warData = new Files(p, "warData.yml");
        String ownIslandName = p.getConfig().getString("island." + player.getName());

        if (warData.getConfig().contains("war.pending.island1." + ownIslandName)) {
            return 1;
        }
        if (warData.getConfig().contains("war.pending.island2." + ownIslandName)) {
            return 1;
        }

        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

            if (warData.getConfig().getString("war.war." + warID + ".blue.islandName").equals(ownIslandName)) {
                return 2;
            }
            if (warData.getConfig().getString("war.war." + warID + ".red.islandName").equals(ownIslandName)) {
                return 2;
            }
        }
        return 0;
    }

    public static int testIfPlayerIsInWar(Main p, Player player) {

        Files warData = new Files(p, "warData.yml");

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("playersInWar");
        if (list.contains(player.getName())) {
            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(player.getName())) {
                   return  1;
                }
                if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(player.getName())) {
                 return 1;
                }
            }
        }
        return 0;
    }

    public static void startWar(String islandName2, Main p) {

        Files warIslands = new Files(p, "warIslands.yml");
        Files warData = new Files(p, "warData.yml");

        String islandName1 = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

        ArrayList<String> island1 = new ArrayList<>();
        ArrayList<String> island2 = new ArrayList<>();
        island1.addAll(warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false));
        island2.addAll(warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false));

        ArrayList<String> islandIDList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandInvite." + islandName1 + ".islandList");
        String islandTheme = warData.getConfig().getString("war.pending.islandInvite." + islandName1 + ".theme");

        for (String players : island1) {
            if (Bukkit.getPlayerExact(players) != null)
                Bukkit.getPlayerExact(players).sendMessage("everybody has accepted this war");
        }
        for (String players : island2) {
            if (Bukkit.getPlayerExact(players) != null)
                Bukkit.getPlayerExact(players).sendMessage("everybody has accepted this war");
        }

        int island1size = 0;
        int island2size = 0;

        for (String players : island1) {
            if (Bukkit.getPlayerExact(players) != null) island1size++;
        }
        for (String players : island2) {
            if (Bukkit.getPlayerExact(players) != null) island2size++;
        }

        for (String islandNew : islandIDList) {
            if (warIslands.getConfig().getBoolean("warIslands." + islandTheme + "." + islandNew + ".inUse"))
                islandIDList.remove(islandNew);
        }
        if (islandIDList.size() == 0) {
            for (String players : island1) {
                if (Bukkit.getPlayerExact(players) != null)
                    Bukkit.getPlayerExact(players).sendMessage("war is canceled, all available islands are in use");
            }
            for (String players : island2) {
                if (Bukkit.getPlayerExact(players) != null)
                    Bukkit.getPlayerExact(players).sendMessage("war is canceled, all available islands are in use");
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
                //todo OPs error message
            }
            String blueVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + islandNew + ".nexus.blue");
            String redVillagerUUID = warIslands.getConfig().getString("warIslands.island." + islandTheme + "." + islandNew + ".nexus.red");
            if (Bukkit.getEntity(UUID.fromString(blueVillagerUUID)) == null || Bukkit.getEntity(UUID.fromString(redVillagerUUID)) == null) {
                islandIDList.remove(islandNew);
                //todo OPs error message
            }
        }
        boolean island1cancel = false;
        if (islandIDList.size() == 0) {
            for (String players : island1) {
                if (island1.size() == island1size) {
                    if (Bukkit.getPlayerExact(players) != null) {
                        Bukkit.getPlayerExact(players).sendMessage("war is canceled, to many people logged off");
                        Bukkit.getPlayerExact(players).sendMessage("other islands are still able to accept your war request");
                        island1cancel = true;
                    }
                } else {
                    if (Bukkit.getPlayerExact(players) != null) {
                        Bukkit.getPlayerExact(players).sendMessage("war is canceled, to many people logged off");
                    }
                }
            }
            for (String players : island2) {
                if (Bukkit.getPlayerExact(players) != null)
                    Bukkit.getPlayerExact(players).sendMessage("war is canceled, to many people logged off");
            }

            if (island1cancel) {
                warData.getConfig().set("war.pending.island2." + islandName2, null);
                warData.saveConfig();
            } else {
                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

                warData.getConfig().set("war.pending.island1." + islandName1, null);
                warData.getConfig().set("war.pending.island2." + islandName2, null);
                warData.saveConfig();
            }
            return;
        }

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
                if (Bukkit.getPlayerExact(players) == null) {
                    continue;
                }
                Bukkit.getPlayerExact(players).teleport(blueLocation);
                Bukkit.getPlayerExact(players).setBedSpawnLocation(blueLocation);
            }
            for (String players : island2) {
                if (Bukkit.getPlayerExact(players) == null) {
                    continue;
                }
                Bukkit.getPlayerExact(players).teleport(redLocation);
                Bukkit.getPlayerExact(players).setBedSpawnLocation(redLocation);
            }
        } else {
            for (String players : island1) {
                if (Bukkit.getPlayerExact(players) == null) {
                    continue;
                }
                Bukkit.getPlayerExact(players).teleport(redLocation);
                Bukkit.getPlayerExact(players).setBedSpawnLocation(redLocation);
            }
            for (String players : island2) {
                if (Bukkit.getPlayerExact(players) == null) {
                    continue;
                }
                Bukkit.getPlayerExact(players).teleport(blueLocation);
                Bukkit.getPlayerExact(players).setBedSpawnLocation(blueLocation);
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
                warData.getConfig().set("war.war." + id + ".warIsland." + islandTheme + "." + finalIslandID, redVillagerUUID);

                ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("playersInWar");

                list.addAll(island1);
                list.addAll(island2);
                p.getConfig().set("playersInWar", list);
                p.saveConfig();

                ArmorStand blueAS = (ArmorStand) Bukkit.getEntity(UUID.fromString(blueUUID));
                ArmorStand redAS = (ArmorStand) Bukkit.getEntity(UUID.fromString(redUUID));
                //todo calculate health
                blueAS.setCustomName("§9health: 20");
                redAS.setCustomName("§chealth: 20");


                break;
            }

            id++;
        }

        warData.saveConfig();

    }
}
