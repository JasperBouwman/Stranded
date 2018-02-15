package com.Stranded.towers.inventory.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class InvShopEnemy {

    //towersData
    public static ItemStack tnt = toItemStack(Material.WOOL, 14, "TNT", "cost: 15");
    public static ItemStack slowness = toItemStack(Material.WOOL, 10, "Slowness", "cost: 15");
    public static ItemStack hunger = toItemStack(Material.WOOL, 13, "Hunger", "cost: 15");
    public static ItemStack wither = toItemStack(Material.WOOL, 15, "Wither", "cost: 15");
    public static ItemStack arrow = toItemStack(Material.WOOL, 8, "Arrow", "cost: 15");

    public static void openInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§4Enemy Towers");
        inv.addItem(tnt, slowness, hunger, wither, arrow);
        player.openInventory(inv);
    }

}
