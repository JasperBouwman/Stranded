package com.Stranded.towers.inventory.ownTowers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class InvTowers {

    public static ItemStack allTowers = toItemStack(Material.WOOL, 5, "All towers");

    private static ItemStack tnt = toItemStack(Material.WOOL, 14, "TNT");
    private static ItemStack slowness = toItemStack(Material.WOOL, 10, "Slowness");
    private static ItemStack hunger = toItemStack(Material.WOOL, 13, "Hunger");
    private static ItemStack wither = toItemStack(Material.WOOL, 15, "Wither");
    private static ItemStack arrow = toItemStack(Material.WOOL, 8, "Arrow");
    private static ItemStack haste = toItemStack(Material.WOOL, 4, "Haste");
    private static ItemStack regeneration = toItemStack(Material.WOOL, 1, "Regeneration");
    private static ItemStack speed = toItemStack(Material.WOOL, 0, "Speed");

    public static Inventory inv() {
        Inventory inv = Bukkit.createInventory(null, 9, "My towers");

        inv.setItem(0, tnt);
        inv.setItem(1, slowness);
        inv.setItem(2, hunger);
        inv.setItem(3, wither);
        inv.setItem(4, arrow);
        inv.setItem(5, haste);
        inv.setItem(6, regeneration);
        inv.setItem(7, speed);

        inv.setItem(8, allTowers);

        return inv;
    }

    public static void openInv(Player player) {
        player.openInventory(inv());
    }
}
