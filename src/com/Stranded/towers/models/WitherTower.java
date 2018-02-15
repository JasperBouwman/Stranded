package com.Stranded.towers.models;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class WitherTower {

    @SuppressWarnings("deprecation")
    public static void Tower(Location l, String lvl) {

        World w = l.getWorld();
        
        w.getBlockAt(l).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1).setType(Material.OBSIDIAN);

        w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + 4, l.getBlockZ()).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + 4, l.getBlockZ()).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() + 1).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() - 1).setType(Material.OBSIDIAN);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ()).setType(Material.OBSIDIAN);

        for (int i = 0; i <= 4; i++) {
            w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() + 1).setType(Material.NETHER_BRICK);
            w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() - 1).setType(Material.NETHER_BRICK);
            w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() + 1).setType(Material.NETHER_BRICK);
            w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() - 1).setType(Material.NETHER_BRICK);
        }

        w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()).setType(Material.STATIONARY_LAVA);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 2, l.getBlockZ()).setType(Material.STATIONARY_LAVA);
        w.getBlockAt(l.getBlockX(), l.getBlockY() + 3, l.getBlockZ()).setType(Material.STATIONARY_LAVA);
        for (int i = 1; i <= 3; i++) {
            w.getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 15, true);
            w.getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 15, true);
            w.getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 15, true);
            w.getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 15, true);
        }

        w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2).setType(Material.WALL_SIGN);
        Block b = w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2);
        Sign s = (Sign) b.getState();
        s.setLine(0, "§4Enemy Tower");
        s.setLine(1, "Wither lvl: " + lvl);
        s.setLine(2, "Upgrade cost:");
        s.setLine(3, "15");
        s.update();

        w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 3, true);
        Block b1 = w.getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2);
        Sign s1 = (Sign) b1.getState();
        s1.setLine(0, "§4Enemy Tower");
        s1.setLine(1, "Wither lvl: " + lvl);
        s1.setLine(2, "Upgrade cost:");
        s1.setLine(3, "15");
        s1.update();
    }
}
