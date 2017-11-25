package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.UUID;

public class RemoveTower {

    private static void giveTower(Main p, Location main, String owner, String towerLvl) {
        Block blockNorth = new Location(main.getWorld(), main.getBlockX(), main.getBlockY() + 1, main.getBlockZ() - 2)
                .getBlock();
        Block blockSouth = new Location(main.getWorld(), main.getBlockX(), main.getBlockY() + 1, main.getBlockZ() + 2)
                .getBlock();

        if (!blockNorth.getType().equals(Material.WALL_SIGN) && !blockSouth.getType().equals(Material.WALL_SIGN)) {
            return;
        }

        Sign signNorth;
        Sign signSouth;

        try {
            signNorth = (Sign) blockNorth.getState();
            if (signNorth.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                String effect = (signNorth.getLine(1).replace(" lvl: ", "").replace("MAX", ""));
                for (int i = 1; i <= 9; i++) {
                    StringBuilder str = new StringBuilder();

                    effect = effect.replace(str.append(i).toString(), "");
                }
                // get tower lvl
                String lvl = (signNorth.getLine(1).replace("Speed lvl: ", "")
                        .replace("Slow lvl: ", "").replace("Regen lvl: ", "")
                        .replace("Haste lvl: ", "").replace("Wither lvl: ", "")
                        .replace("Hunger lvl: ", "").replace("Tnt lvl: ", "")
                        .replace("Arrow lvl: ", ""));

                int lvlInt = Tower.MAX_UPGRADE;
                try {
                    lvlInt = Integer.parseInt(lvl);
                } catch (NumberFormatException ignore) {
                }


                if (towerLvl.equals("half")) {
                    lvlInt /= 2;
                } else if (towerLvl.equals("null")) {
                    lvlInt = 0;
                }
                new Tower(p, UUID.fromString(owner), effect, lvlInt).saveTower();
                return;
            }
        } catch (ClassCastException cce) {
            //north is not a sign
        }


        try {
            signSouth = (Sign) blockSouth.getState();
            if (signSouth.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                String effect = (signSouth.getLine(1).replace(" lvl: ", "").replace("MAX", ""));
                for (int i = 1; i <= 9; i++) {
                    StringBuilder str = new StringBuilder();

                    effect = effect.replace(str.append(i).toString(), "");
                }
                // get tower lvl
                String lvl = (signSouth.getLine(1).replace("Speed lvl: ", "")
                        .replace("Slow lvl: ", "").replace("Regen lvl: ", "")
                        .replace("Haste lvl: ", "").replace("Wither lvl: ", "")
                        .replace("Hunger lvl: ", "").replace("Tnt lvl: ", "")
                        .replace("Arrow lvl: ", ""));
                int lvlInt = Tower.MAX_UPGRADE;
                try {
                    lvlInt = Integer.parseInt(lvl);
                } catch (NumberFormatException ignore) {
                }
                if (towerLvl.equals("half")) {
                    lvlInt /= 2;
                } else if (towerLvl.equals("null")) {
                    lvlInt = 0;
                }
                new Tower(p, UUID.fromString(owner), effect, lvlInt).saveTower();

            }
        } catch (ClassCastException cce) {
            //south is not a sign
        }
    }

    @SuppressWarnings("deprecation")
    public static void removeWarTower(Main p, Files warData, String warID, String towerID, String side, String towerLvl) {
        Location main = (Location) warData.getConfig().get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
        Location L1 = new Location(main.getWorld(), main.getX() + 1, main.getY(), main.getZ() + 2);
        Location L2 = new Location(main.getWorld(), main.getX() - 1, main.getY() + 4, main.getZ() - 2);

        String owner = warData.getConfig().getString("war.war." + warID + ".towers." + side + "." + towerID + ".owner");

        giveTower(p, main, owner, towerLvl);

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int blockCount = 0;

        // set all blocks in this region to air
        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {
                    Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(xx, yy, zz);
                    byte by = (byte) warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".terrain." + blockCount + ".data");
                    block.setTypeIdAndData(warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".terrain." + blockCount + ".type"), by, true);
                    blockCount++;
                }
            }
        }

        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID, null);
        warData.saveConfig();
    }

    @SuppressWarnings("deprecation")
    public static void removeIslandTower(Main p, Files islands, String island, String id) {
        Location main = (Location) islands.getConfig().get("island." + island + ".towers." + id + ".location");
        Location L1 = new Location(main.getWorld(), main.getX() + 1, main.getY(), main.getZ() + 2);
        Location L2 = new Location(main.getWorld(), main.getX() - 1, main.getY() + 4, main.getZ() - 2);

        String owner = islands.getConfig().getString("island." + island + ".towers." + id + ".owner"); //UUID

        giveTower(p, main, owner, "full");

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int blockCount = 0;

        // set all blocks in this region to air
        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {
                    Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(xx, yy, zz);
                    byte by = (byte) islands.getConfig().getInt("island." + island + ".towers." + id + ".terrain." + blockCount + ".data");
                    block.setTypeIdAndData(islands.getConfig().getInt("island." + island + ".towers." + id + ".terrain." + blockCount + ".type"), by, true);
                    blockCount++;
                }
            }
        }

        islands.getConfig().set("island." + island + ".towers." + id, null);
        islands.saveConfig();
    }

}
