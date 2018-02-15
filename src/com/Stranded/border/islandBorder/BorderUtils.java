package com.Stranded.border.islandBorder;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class BorderUtils {

    public static boolean testPiston(Location location, BlockFace blockFace, boolean offset) {

        if (!location.getWorld().getName().equals("Islands")) {
            return false;
        }

        if (location.getBlockX() <= -200987 && location.getBlockX() >= -201013 &&
                location.getBlockZ() >= -200013 && location.getBlockZ() <= -199726) {
            return false;
        }

        Files f = getFiles( "islands.yml");

        if (!f.getConfig().contains("island")) {
            return true; //todo test
        }

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

    //todo check dis out
    public static boolean testIfIsInIsland() {
        return true;
    }
    public static boolean testIfIsInWarIsland() {
        return true;
    }

    public static boolean testBorder(Location location, Player player) {
        try {
            Files islands = getFiles( "islands.yml");
            Files config = getFiles("config.yml");
            String uuid = player.getUniqueId().toString();

            if (uuid.equals("3a5b4fed-97ef-4599-bf21-19ff1215faff") || uuid.equals("4c386ae3-8e21-4ad9-a2b5-c480688c8668")) {
                return false;
            }

            if (!player.getWorld().getName().equals("Islands")) {
                return false;
            }

            if (location.getBlockX() <= -200987 && location.getBlockX() >= -201013 &&
                    location.getBlockZ() >= -200013 && location.getBlockZ() <= -199726) {
                return false;
            }


            if (!config.getConfig().contains("island." + uuid)) {
                return true;
            }

            String island = config.getConfig().getString("island." + uuid);

            int islandLvl = islands.getConfig().getInt("island." + island + ".lvl");
            int islandSize = (19 + 6 * islandLvl) / 2;

            Location l = (Location) islands.getConfig().get("island." + island + ".location");

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

    public static boolean border2(Location location, Player player) {
        try {
            Files islands = getFiles( "islands.yml");
            Files config = getFiles("config.yml");

            if (location.getBlockX() <= -200987 && location.getBlockX() >= -201013 &&
                    location.getBlockZ() >= -200013 && location.getBlockZ() <= -199726) {
                return true;
            }

            if (!config.getConfig().contains("island." + player.getUniqueId().toString())) {
                return true;
            }

            String island = config.getConfig().getString("island." + player.getUniqueId().toString());

            int islandLvl = islands.getConfig().getInt("island." + island + ".lvl");
            int islandSize = (19 + 6 * islandLvl) / 2;

            Location l = (Location) islands.getConfig().get("island." + island + ".location");

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
