package com.Stranded.towers.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class TntTower {

    @SuppressWarnings("deprecation")
    public static void Tower(Location l, String lvl) {

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1).setType(Material.NETHER_BRICK);

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + 4, l.getBlockZ()).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + 4, l.getBlockZ()).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() + 1).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() - 1).setType(Material.NETHER_BRICK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ()).setType(Material.NETHER_BRICK);

        for (int i = 0; i <= 4; i++) {
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() + 1).setType(Material.RED_NETHER_BRICK);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() - 1).setType(Material.RED_NETHER_BRICK);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() + 1).setType(Material.RED_NETHER_BRICK);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() - 1).setType(Material.RED_NETHER_BRICK);
        }

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()).setType(Material.NETHER_WART_BLOCK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 2, l.getBlockZ()).setType(Material.NETHER_WART_BLOCK);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 3, l.getBlockZ()).setType(Material.NETHER_WART_BLOCK);
        for (int i = 1; i <= 3; i++) {
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 14, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 14, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 14, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 14, true);
        }

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2).setType(Material.WALL_SIGN);
        Block b = Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2);
        Sign s = (Sign) b.getState();
        s.setLine(0, "§4Enemy Tower");
        s.setLine(1, "Tnt lvl: " + lvl);
        s.setLine(2, "Upgrade cost:");
        s.setLine(3, "15");
        s.update();

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 3, true);
        Block b1 = Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2);
        Sign s1 = (Sign) b1.getState();
        s1.setLine(0, "§4Enemy Tower");
        s1.setLine(1, "Tnt lvl: " + lvl);
        s1.setLine(2, "Upgrade cost:");
        s1.setLine(3, "15");
        s1.update();
    }

}
