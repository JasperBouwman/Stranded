package com.Stranded.islandBorder;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class BorderUtils {

    public static boolean border(Location location, Main p, BlockFace blockFace, boolean offset) {

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

            l.setX(l.getX() - (islandSize * 2));
            l.setZ(l.getZ() - (islandSize * 2));

            Location L2 = new Location(l.getWorld(), l.getX(), 1, l.getZ());

            int minX = Math.min(L1.getBlockX(), L2.getBlockX());
            int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
            int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
            int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());
            if (offset) {
                if (blockFace.toString().equals("NORTH")) {
                    return location.getZ() > maxZ;
                }
                if (blockFace.toString().equals("EAST")) {
                    return location.getX() < minX;
                }
                if (blockFace.toString().equals("SOUTH")) {
                    return location.getZ() < minZ;
                }
                if (blockFace.toString().equals("WEST")) {
                    return location.getX() > maxX;
                }
                if (blockFace.toString().equals("UP")) {
                    return location.getZ() < minZ ||
                            location.getX() > maxX ||
                            location.getZ() > maxZ ||
                            location.getX() < minX;
                }
                if (blockFace.toString().equals("DOWN")) {
                    return location.getZ() < minZ ||
                            location.getX() > maxX ||
                            location.getZ() > maxZ ||
                            location.getX() < minX;
                }
            } else {
                if (blockFace.toString().equals("NORTH")) {
                    return location.getZ() <= minZ;
                }
                if (blockFace.toString().equals("EAST")) {
                    return location.getX() >= maxX;
                }
                if (blockFace.toString().equals("SOUTH")) {
                    return location.getZ() >= maxZ;
                }
                if (blockFace.toString().equals("WEST")) {
                    return location.getX() <= minX;
                }
                if (blockFace.toString().equals("UP")) {
                    return location.getZ() < minZ ||
                            location.getX() > maxX ||
                            location.getZ() > maxZ ||
                            location.getX() < minX;
                }
                if (blockFace.toString().equals("DOWN")) {
                    return location.getZ() < minZ ||
                            location.getX() > maxX ||
                            location.getZ() > maxZ ||
                            location.getX() < minX;
                }
            }
        }
        return true;
    }

    public static boolean border(Location location, Main p, Player player) {
        try {
            Files f = new Files(p, "islands.yml");

            //todo
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
            String uuid = player.getUniqueId().toString();

            if (!p.getConfig().contains("island." + uuid)) {
                return true;
            }

            String island = p.getConfig().getString("island." + uuid);

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

            if (!p.getConfig().contains("island." + player.getUniqueId().toString())) {
                return true;
            }

            String island = p.getConfig().getString("island." + player.getUniqueId().toString());

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
