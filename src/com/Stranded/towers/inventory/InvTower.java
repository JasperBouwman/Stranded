package com.Stranded.towers.inventory;

import com.Stranded.nexus.InventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class InvTower {

    static ItemStack ownTowers(Player player) {

        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
        SkullMeta im = (SkullMeta) is.getItemMeta();

        im.setDisplayName("your own towers");
        im.setOwningPlayer(player);

        is.setItemMeta(im);

        return is;
    }
    static ItemStack shop = InventoryEvent.newItemStack(Material.DOUBLE_PLANT, 0, "tower shop");

    public static Inventory inv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "Towers");

        inv.setItem(0, ownTowers(player));
        inv.setItem(1, shop);
        return inv;
    }

    public static void openInv(Player player) {
        player.openInventory(inv(player));
    }
}
