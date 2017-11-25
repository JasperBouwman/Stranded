package com.Stranded.nexus.inv;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

import java.util.ArrayList;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class InvMain {

    private static ItemStack gamble = toItemStack(Material.DIAMOND, 0, "gamble");
    private static ItemStack tower = toItemStack(Material.WOOL, 11, "towers");

    public static ItemStack nexusUpgrade(Main p, Files islands, Player player) {
        ItemStack is = new ItemStack(Material.MONSTER_EGG);
        SpawnEggMeta im = (SpawnEggMeta) is.getItemMeta();
        im.setSpawnedType(EntityType.VILLAGER);
        im.setDisplayName("nexus upgrade");

        String island = p.getConfig().getString("island." + player.getUniqueId().toString());
        int x = islands.getConfig().getInt("island." + island + ".nexusLvl");

        ArrayList<String> lore = new ArrayList<>();

        if (x < 50) {
            lore.add("to lvl " + (x + 1));
            double y = Math.sinh(((double) x + 35) / 20);
            lore.add("cost: " + (int) y);
        } else {
            lore.add("max upgraded");
        }

        im.setLore(lore);
        is.setItemMeta(im);

        return is;
    }

    public static ItemStack islandUpgrade(Main p, Files islands, Player player) {
        ItemStack is = new ItemStack(Material.GRASS);
        ItemMeta im = is.getItemMeta();
        String island = p.getConfig().getString("island." + player.getUniqueId().toString());
        int x = islands.getConfig().getInt("island." + island + ".lvl");

        int spc = (19 + 6 * (x + 1)) * (19 + 6 * (x + 1));

        ArrayList<String> lore = new ArrayList<>();

        if (x < 100) {
            lore.add("to lvl " + (x + 1));
            double y = Math.sinh(((double) x + 35) / 20);
            lore.add("cost: " + (int) y);
            lore.add("total space: " + spc + " (" + (19 + 6 * (x + 1)) + "*" + (19 + 6 * (x + 1)) + ")");
        } else {
            lore.add("max upgraded");
        }

        im.setLore(lore);
        im.setDisplayName("island upgrade");

        is.setItemMeta(im);
        return is;
    }


    public static void openInv(Main p, Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§2Nexus");

        Files islands = new Files(p, "islands.yml");

        inv.setItem(0, islandUpgrade(p, islands, player));
        inv.setItem(1, nexusUpgrade(p, islands, player));
        inv.setItem(2, gamble);
        inv.setItem(3, tower);

        player.openInventory(inv);
    }

}
