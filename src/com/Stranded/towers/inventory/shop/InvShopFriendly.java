package com.Stranded.towers.inventory.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class InvShopFriendly {

    //towersData
    public static ItemStack regeneration = toItemStack(Material.WOOL, 1, "Regeneration", "cost: 15");
    public static ItemStack haste = toItemStack(Material.WOOL, 4, "Haste", "cost: 10");
    public static ItemStack speed = toItemStack(Material.WOOL, 0, "Speed", "cost: 7");
    public static ItemStack teleport = toItemStack(Material.WOOL, 3, "Teleport", "cost: 15");

    public static void openInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§3Friendly Towers");
        inv.addItem(regeneration, haste, speed, teleport);
        player.openInventory(inv);
    }
}
