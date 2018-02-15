package com.Stranded.towers.inventory.ownTowers;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.towers.Tower;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class InvFilteredTowers {

    public static ItemStack addTower = toItemStack(Material.TORCH, 0, "<-- place tower here to add");
    public static ItemStack up = toItemStack(Material.LONG_GRASS, 0, "up");
    public static ItemStack down = toItemStack(Material.HOPPER, 0, "down");
    public static ItemStack back = toItemStack(Material.DIAMOND, 0, "back");

    public static void openInv(Player player, ItemStack item) {

        Tower tower = new Tower(player.getName());
        ArrayList<ItemStack> filtered = tower.filterTowers(item.getItemMeta().getDisplayName());

        int page = 0;

        Files config = getFiles("config.yml");
        if (config.getConfig().contains("tower." + player.getUniqueId().toString() + ".page")) {
            page = config.getConfig().getInt("tower." + player.getUniqueId().toString() + ".page");
        }

        page *= 9;

        int size = 45;
        Inventory inv = Bukkit.createInventory(null, size, "Towers, Filter: " + item.getItemMeta().getDisplayName());

        int i = 0;
        for (ItemStack is : filtered) {
            if (i >= page) {
                if (i < size - 9) {
                    inv.setItem(i, is);
                    i++;
                }
            } else {
                page--;
            }
        }

        inv.setItem(size - 9, up);
        inv.setItem(size - 1, down);
        inv.setItem(size - 4, addTower);
        inv.setItem(size - 6, back);

        player.openInventory(inv);
    }

}
