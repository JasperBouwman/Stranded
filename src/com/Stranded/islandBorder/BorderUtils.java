package com.Stranded.islandBorder;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BorderUtils {

    public static boolean border(Location location, Main p) {

        if (!location.getWorld().getName().equals("Islands")) {
            return false;
        }

        if (location.getBlockX() <= -200987 && location.getBlockX() >= -201013 &&
                location.getBlockZ() >= -200013 && location.getBlockZ() <= -199726) {
            return false;
        }

        Files f = new Files(p, "islands.yml");

        for (String island : f.getConfig().getConfigurationSection("island").getKeys(false)) {

            int islandLvl = f.getConfig().getInt("island." + island + ".lvl");
            int islandSize = (19 + 6 * islandLvl) / 2;

            Location l = (Location) f.getConfig().get("island." + island + ".location");

            l.setX(l.getX() + islandSize);
            l.setZ(l.getZ() + islandSize);

            Location L1 = new Location(l.getWorld(), l.getX(), 255, l.getZ());

            l.setX(l.getX() - islandSize * 2);
            l.setZ(l.getZ() - islandSize * 2);

            Location L2 = new Location(l.getWorld(), l.getX(), 1, l.getZ());

            int minX = Math.min(L1.getBlockX(), L2.getBlockX());
            int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
            int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
            int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

            for (int xx = minX; xx <= maxX; xx++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    if (location.getBlockX() == xx && location.getBlockZ() == zz) {
                        return false;

                    }
                }
            }
        }

        return true;
    }

    public static boolean border(Location location, Main p, Player player) {
        try {
            Files f = new Files(p, "islands.yml");

            //todo remove this when finished
            if (true) {
                return false;
            }

            if (!player.getWorld().getName().equals("Islands")) {
                return false;
            }

            if (location.getBlockX() <= -200987 && location.getBlockX() >= -201013 &&
                    location.getBlockZ() >= -200013 && location.getBlockZ() <= -199726) {
                return false;
            }

            if (!p.getConfig().contains("island." + player.getName())) {
                return true;
            }

            String island = p.getConfig().getString("island." + player.getName());

            int islandLvl = f.getConfig().getInt("island." + island + ".lvl");
            int islandSize = (19 + 6 * islandLvl) / 2;

            Location l = (Location) f.getConfig().get("island." + island + ".location");

            l.setX(l.getX() + islandSize);
            l.setZ(l.getZ() + islandSize);

            Location L1 = new Location(l.getWorld(), l.getX(), 255, l.getZ());

            l.setX(l.getX() - islandSize * 2);
            l.setZ(l.getZ() - islandSize * 2);

            Location L2 = new Location(l.getWorld(), l.getX(), 1, l.getZ());

            int minX = Math.min(L1.getBlockX(), L2.getBlockX());
            int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
            int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
            int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

            for (int xx = minX; xx <= maxX; xx++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    if (location.getBlockX() == xx && location.getBlockZ() == zz) {
                        return false;

                    }
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean border2(Location location, Main p, Player player) {
        try {
            Files f = new Files(p, "islands.yml");

            if (location.getBlockX() <= -200987 && location.getBlockX() >= -201013 &&
                    location.getBlockZ() >= -200013 && location.getBlockZ() <= -199726) {
                return true;
            }

            if (!p.getConfig().contains("island." + player.getName())) {
                return true;
            }

            String island = p.getConfig().getString("island." + player.getName());

            int islandLvl = f.getConfig().getInt("island." + island + ".lvl");
            int islandSize = (19 + 6 * islandLvl) / 2;

            Location l = (Location) f.getConfig().get("island." + island + ".location");

            l.setX(l.getX() + islandSize);
            l.setZ(l.getZ() + islandSize);

            Location L1 = new Location(l.getWorld(), l.getX(), 255, l.getZ());

            l.setX(l.getX() - islandSize * 2);
            l.setZ(l.getZ() - islandSize * 2);

            Location L2 = new Location(l.getWorld(), l.getX(), 1, l.getZ());

            int minX = Math.min(L1.getBlockX(), L2.getBlockX());
            int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
            int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
            int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

            for (int xx = minX; xx <= maxX; xx++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    if (location.getBlockX() == xx && location.getBlockZ() == zz) {
                        return false;

                    }
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
