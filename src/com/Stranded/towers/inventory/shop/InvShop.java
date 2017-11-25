package com.Stranded.towers.inventory.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class InvShop {

    public static ItemStack friendly = toItemStack(Material.DIAMOND_BLOCK,0, "§3Friendly Towers", "Regeneration", "Haste", "Speed");
    public static ItemStack enemy = toItemStack(Material.REDSTONE_BLOCK,0, "§4Enemy Towers", "TNT", "Slowness", "Hunger", "Wither", "Arrow");


    public static void openInv(Player player) {

        Inventory inv = Bukkit.createInventory(null, 9, "Tower Shop");

        inv.addItem(friendly, enemy);

        player.openInventory(inv);

    }

}
