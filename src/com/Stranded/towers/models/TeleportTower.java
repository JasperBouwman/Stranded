package com.Stranded.towers.models;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class TeleportTower {

    @SuppressWarnings("deprecation")
    public static void Tower(Location l, String lvl) {

        World w = l.getWorld();

        w.getBlockAt(l).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);

        w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + 4, l.getBlockZ()).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + 4, l.getBlockZ()).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ()).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 3, true);

        w.getBlockAt(l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);
        w.getBlockAt(l.getBlockX() + 1, l.getBlockY(), l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY(), l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);

        w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + 4, l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);
        w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + 4, l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + 4, l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + 4, l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE.getId(), (byte) 11, true);

        for (int i = 1; i < 4; i++) {
            w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE_POWDER.getId(), (byte) 11, true);
            w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE_POWDER.getId(), (byte) 11, true);
            w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.CONCRETE_POWDER.getId(), (byte) 11, true);
            w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.CONCRETE_POWDER.getId(), (byte) 11, true);
        }

        w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()).setType(Material.CYAN_SHULKER_BOX);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 2, l.getBlockZ()).setType(Material.DIAMOND_BLOCK);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 3, l.getBlockZ()).setTypeIdAndData(Material.CYAN_SHULKER_BOX.getId(), (byte) 6, true);
        for (int i = 1; i <= 3; i++) {
            w.getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 9, true);
            w.getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 9, true);
            w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 9, true);
            w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 9, true);
        }

        w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2).setType(Material.WALL_SIGN);
        Block b = w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2);
        Sign s = (Sign) b.getState();
        s.setLine(0, "§3Friendly Tower");
        s.setLine(1, "Tp lvl: " + lvl);
        s.setLine(2, "Upgrade cost:");
        s.setLine(3, "15");
        s.update();

        w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 3, true);
        Block b1 = w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2);
        Sign s1 = (Sign) b1.getState();
        s1.setLine(0, "§3Friendly Tower");
        s1.setLine(1, "Tp lvl: " + lvl);
        s1.setLine(2, "Upgrade cost:");
        s1.setLine(3, "15");
        s1.update();
    }

}
