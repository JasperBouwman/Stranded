package com.Stranded.gamble.inv;

import com.Stranded.nexus.InventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvStartSlots {

    public static ItemStack size33 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 3, height: 3");
    public static ItemStack size34 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 3, height: 4");
    public static ItemStack size35 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 3, height: 5");
    public static ItemStack size36 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 3, height: 6");

    public static ItemStack size53 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 5, height: 3");
    public static ItemStack size54 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 5, height: 4");
    public static ItemStack size55 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 5, height: 5");
    public static ItemStack size56 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 5, height: 6");

    public static ItemStack size73 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 7, height: 3");
    public static ItemStack size74 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 7, height: 4");
    public static ItemStack size75 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 7, height: 5");
    public static ItemStack size76 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 7, height: 6");


    public static ItemStack size93 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 9, height: 3");
    public static ItemStack size94 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 9, height: 4");
    public static ItemStack size95 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 9, height: 5");
    public static ItemStack size96 = InventoryEvent.newItemStack(Material.DIAMOND, 0, "width: 9, height: 6");
    public static String title = "Choose you size";

    public static void openInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 18, title);

        inv.setItem(0, size33);
        inv.setItem(1, size34);
        inv.setItem(2, size35);
        inv.setItem(3, size36);

        inv.setItem(9, size53);
        inv.setItem(10, size54);
        inv.setItem(11, size55);
        inv.setItem(12, size56);

        inv.setItem(5, size73);
        inv.setItem(6, size74);
        inv.setItem(7, size75);
        inv.setItem(8, size76);

        inv.setItem(14, size93);
        inv.setItem(15, size94);
        inv.setItem(16, size95);
        inv.setItem(17, size96);

        player.openInventory(inv);

    }

}
