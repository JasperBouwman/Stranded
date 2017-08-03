package com.Stranded.towers.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class HungerTower {

    @SuppressWarnings("deprecation")
    public void Tower(Location l) {

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY(), l.getBlockZ()).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY(), l.getBlockZ()).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() + 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ() - 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + 4, l.getBlockZ()).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + 4, l.getBlockZ()).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() + 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ() - 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 4, l.getBlockZ()).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 2, true);

        for (int i = 0; i <= 4; i++) {
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 1, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 1, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 1, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.PRISMARINE.getId(), (byte) 1, true);
        }

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ()).setType(Material.PACKED_ICE);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 2, l.getBlockZ()).setType(Material.PACKED_ICE);
        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 3, l.getBlockZ()).setType(Material.PACKED_ICE);

        for (int i = 1; i <= 3; i++) {
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() + 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 13, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + i, l.getBlockZ() - 1).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 13, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() + 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 13, true);
            Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX() - 1, l.getBlockY() + i, l.getBlockZ()).setTypeIdAndData(Material.STAINED_GLASS.getId(), (byte) 13, true);
        }

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2).setType(Material.WALL_SIGN);
        Block b = Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2);
        Sign s = (Sign) b.getState();
        s.setLine(0, "§4Enemy Tower");
        s.setLine(1, "Hunger lvl: 1");
        s.setLine(2, "Upgrade cost:");
        s.setLine(3, "10");
        s.update();

        Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 3, true);
        Block b1 = Bukkit.getWorld(l.getWorld().getName()).getBlockAt(l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2);
        Sign s1 = (Sign) b1.getState();
        s1.setLine(0, "§4Enemy Tower");
        s1.setLine(1, "Hunger lvl: 1");
        s1.setLine(2, "Upgrade cost:");
        s1.setLine(3, "10");
        s1.update();
    }
}
