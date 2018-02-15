package com.Stranded.towers;

import com.Stranded.Files;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.towers.Tower.MAX_UPGRADE;

public class TowerUtil {

    private static void saveTowerData(String effect, int level, Files islands, Files warData, String towerPath) {

        boolean updated = false;
        ArrayList<Integer> timer = new ArrayList<>();

        //towersData
        switch (effect.toLowerCase()) {
            case "speed":
            case "slow":
            case "regen":
            case "haste":
            case "wither":
            case "hunger":
            case "arrow":
                switch (level + 1) {
                    case 2:
                        timer.add(28);
                    case 4:
                        timer.add(26);
                    case 6:
                        timer.add(24);
                    case 8:
                        timer.add(22);
                    case 10:
                        timer.add(20);
                    case 12:
                        timer.add(18);
                    case 14:
                        timer.add(16);
                    case 16:
                        timer.add(14);
                    case 18:
                        timer.add(12);
                    case 20:
                        timer.add(10);
                        updated = true;
                        break;
                }
                break;
            case "teleport"://todo
                switch (level + 1) {
                    case 2:
                        timer.add(280);
                    case 3:
                        timer.add(260);
                    case 4:
                        timer.add(240);
                    case 5:
                        timer.add(250);
                    case 6:
                        timer.add(240);
                    case 7:
                        timer.add(220);
                    case 8:
                        timer.add(210);
                    case 9:
                        timer.add(220);
                    case 10:
                        timer.add(200);
                    case 11:
                        timer.add(180);
                    case 12:
                        timer.add(160);
                    case 13:
                        timer.add(140);
                    case 14:
                        timer.add(120);
                    case 15:
                        timer.add(100);
                    case 17:
                        timer.add(80);
                    case 19:
                        timer.add(60);
                        updated = true;
                        break;
                }//todo test timing
                break;
            case "tnt":
                switch (level + 1) {
                    case 2:
                        timer.add(43);
                    case 3:
                        timer.add(41);
                    case 4:
                        timer.add(39);
                    case 5:
                        timer.add(37);
                    case 6:
                        timer.add(35);
                    case 7:
                        timer.add(33);
                    case 8:
                        timer.add(30);
                    case 9:
                        timer.add(28);
                    case 10:
                        timer.add(26);
                    case 11:
                        timer.add(24);
                    case 12:
                        timer.add(22);
                    case 13:
                        timer.add(20);
                    case 14:
                        timer.add(18);
                    case 15:
                        timer.add(16);
                    case 17:
                        timer.add(14);
                    case 19:
                        timer.add(10);
                        updated = true;
                        break;
                }
                break;
        }

        if (updated) {
            if (timer.size() > 0) {
                int finalTimer = timer.get(0);
                if (towerPath.startsWith("war")) {
                    //warTower
                    warData.getConfig().set(towerPath + ".timer", finalTimer);
                    warData.saveConfig();
                } else {
                    //islandTower
                    islands.getConfig().set(towerPath + ".timer", finalTimer);
                    islands.saveConfig();
                }
            }
        }
    }

    public static boolean upgradeTower(int lvlInt, String lvl, Sign s, Sign opposite, Player player, int upgrade, Location towerLoc, boolean islandTower) {

        Files islands = getFiles("islands.yml");
        Files warData = getFiles("warData.yml");

        String towerPath = "";
        boolean br = true;

        String effectTmp = "";

        if (islandTower) {
            for (String island : islands.getConfig().getConfigurationSection("island").getKeys(false)) {
                if (islands.getConfig().contains("island." + island + ".towers")) {
                    // get all towers
                    for (String towerID : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                        Location l = (Location) islands.getConfig()
                                .get("island." + island + ".towers." + towerID + ".location");
                        if (l.equals(towerLoc)) {
                            towerPath = "island." + island + ".towers." + towerID;
                            effectTmp = islands.getConfig().getString("island." + island + ".towers." + towerID + ".type");
                            br = false;
                            break;
                        }
                    }
                }
            }
        } else if (towerLoc.getWorld().equals(Bukkit.getWorld("War"))) {
            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                for (String side : Arrays.asList("red", "blue")) {
                    if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
                        for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
                            Location l = (Location) warData.getConfig()
                                    .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
                            if (l.equals(towerLoc)) {
                                towerPath = "war.war." + warID + ".towers." + side + "." + towerID;
                                effectTmp = warData.getConfig().getString("war.war." + warID + ".towers." + side + "." + towerID + ".type");
                                br = false;
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
        if (br) {
            return false;
        }

        saveTowerData(effectTmp, lvlInt, islands, warData, towerPath);

        if (lvlInt + 1 == MAX_UPGRADE) {
            s.setLine(1, s.getLine(1).replace(lvl, "MAX"));
            s.setLine(3, "-");
            s.update();

            opposite.setLine(1, s.getLine(1).replace(lvl, "MAX"));
            opposite.setLine(3, "-");
            opposite.update();

            player.setLevel(player.getLevel() - upgrade);
            player.sendMessage("This tower is now max upgraded");
            return true;
        }

        StringBuilder str = new StringBuilder();
        str.append(lvlInt + 1);

        s.setLine(1, s.getLine(1).replace(lvl, str.toString()));
        int newCost = Integer.parseInt(s.getLine(3)) + 2;
        s.setLine(3, newCost + "");
        s.update();

        opposite.setLine(1, s.getLine(1).replace(lvl, str.toString()));
        opposite.setLine(3, newCost + "");
        opposite.update();

        player.setLevel(player.getLevel() - upgrade);
        player.sendMessage("This tower is upgraded");
        return true;
    }

    public static boolean testTowerLocationFromTower(Location l) {

        //true == to close
        int maxDistance = 10;

        Files islands = getFiles("islands.yml");
        Files warData = getFiles("warData.yml");

        for (String island : islands.getConfig().getConfigurationSection("island").getKeys(false)) {
            if (islands.getConfig().contains("island." + island + ".towers")) {
                // get all towers
                for (String towerID : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    Location loc = (Location) islands.getConfig()
                            .get("island." + island + ".towers." + towerID + ".location");

                    if (loc.getWorld().equals(l.getWorld())) {

                        if (l.distance(loc) < maxDistance) {
                            return true;
                        }
                    }
                }
            }
        }

        for (String side : Arrays.asList("blue", "red")) {

            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
                    // get all towers
                    for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
                        // get location of tower
                        Location loc = (Location) warData.getConfig()
                                .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");

                        if (loc.getWorld().equals(l.getWorld())) {
                            if (l.distance(loc) < maxDistance) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

//        String side = "blue";
//        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
//            if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
//                // get all towers
//                for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
//                    // get location of tower
//                    Location loc = (Location) warData.getConfig()
//                            .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
//
//                    if (loc.getWorld().equals(l.getWorld())) {
//                        if (l.distance(loc) < maxDistance) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//
//        side = "red";
//        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
//            if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
//                // get all towers
//                for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
//                    // get location of tower
//                    Location loc = (Location) warData.getConfig()
//                            .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
//
//                    if (loc.getWorld().equals(l.getWorld())) {
//                        if (l.distance(loc) < maxDistance) {
//                            return true;
//                        }
//                    }
//                }
//            }
//        }

        return false;

    }

    public static boolean testTowerLocationFromNexus(Location l) {

        //true == to close
        int maxDistance = 10;
        Files config = getFiles("config.yml");

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");

        for (String uuid : list) {

            Entity e = Bukkit.getEntity(UUID.fromString(uuid));
            if (e != null) {
                if (e.getWorld() == l.getWorld()) {
                    if (e instanceof Villager) {
                        Villager v = (Villager) e;
                        if (l.distance(v.getLocation()) < maxDistance) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
