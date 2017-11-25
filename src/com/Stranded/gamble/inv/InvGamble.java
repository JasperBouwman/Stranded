package com.Stranded.gamble.inv;

import com.Stranded.nexus.InventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvGamble {

    public static ItemStack slots = InventoryEvent.newItemStack(Material.DIAMOND, 0, "Slots");
    public static ItemStack random = InventoryEvent.newItemStack(Material.DIAMOND, 0, "Random Item");
    public static String title = "Gamble";

    public static void openInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, title);

        inv.setItem(0, slots);
        inv.setItem(1, random);
        player.openInventory(inv);
    }
}
