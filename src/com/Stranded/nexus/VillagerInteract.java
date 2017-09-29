package com.Stranded.nexus;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class VillagerInteract implements Listener {

    private Main p;

    public VillagerInteract(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void EntityInteract(PlayerInteractEntityEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();

        if (e.getRightClicked() instanceof Villager) {
            ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");
            if (list.contains(e.getRightClicked().getUniqueId().toString())) {

                player.openInventory(inv("main", e.getPlayer()));

                e.setCancelled(true);
            }
        }
    }

    Inventory inv(String invName, Player player) {

        Files f = new Files(p, "pluginData.yml");
        Files islands = new Files(p, "islands.yml");

        String name = "";
        int size = 9;

        if (f.getConfig().contains("plugin.nexusShop." + invName + ".name")) {
            name = f.getConfig().getString("plugin.nexusShop." + invName + ".name");
        }
        if (f.getConfig().contains("plugin.nexusShop." + invName + ".size")) {
            size = f.getConfig().getInt("plugin.nexusShop." + invName + ".size");
        }

        Inventory inv = Bukkit.createInventory(null, size, name);

        for (String items : f.getConfig().getConfigurationSection("plugin.nexusShop." + invName + ".items").getKeys(false)) {
            ItemStack is = f.getConfig().getItemStack("plugin.nexusShop." + invName + ".items." + items + ".item");

            if (f.getConfig().getString("plugin.nexusShop." + invName + ".items." + items + ".target").equals("upgrade")) {
                ItemMeta im = is.getItemMeta();

                String island = p.getConfig().getString("island." + player.getName());
                double x = islands.getConfig().getDouble("island." + island + ".lvl");

                int spc = (19 + 6 * ((int) x + 1)) * (19 + 6 * ((int) x + 1));

                ArrayList<String> lore = new ArrayList<>();

                double y = Math.sinh((x + 35) / 20);
                if (x < 100) {
                    lore.add("to lvl " + (int) (x + 1));
                    lore.add("cost: " + (int) y);
                    lore.add("total space: " + spc + " (" + (19 + 6 * ((int) x + 1)) + "*" + (19 + 6 * ((int) x + 1)) + ")");
                } else {
                    lore.add("max upgraded");
                }

                im.setLore(lore);
                is.setItemMeta(im);
            }

            for (String i : f.getConfig().getStringList("plugin.nexusShop." + invName + ".items." + items + ".slots")) {
                int ii = Integer.parseInt(i);

                inv.setItem(ii, is);
            }
        }

        return inv;
    }
}
