package com.Stranded.nexus;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InventoryEvent implements Listener {

    private Main p;

    public InventoryEvent(Main instance) {
        p = instance;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }
        if (meta.getDisplayName() == null) {
            return;
        }

        Files f = new Files(p, "pluginData.yml");

        String mainName = "";
        if (f.getConfig().contains("plugin.nexusShop.main.name")) {
            mainName = f.getConfig().getString("plugin.nexusShop.main.name");
        }
        String friendlyName = f.getConfig().getString("plugin.nexusShop.friendly.name");
        String enemyName = f.getConfig().getString("plugin.nexusShop.enemy.name");

        if (inv.getTitle().equals(mainName)) {

            for (String s : f.getConfig().getConfigurationSection("plugin.nexusShop.main.items").getKeys(false)) {
                ItemStack is = f.getConfig().getItemStack("plugin.nexusShop.main.items." + s + ".item");
                if (is.getItemMeta().getDisplayName().equals(meta.getDisplayName())) {

                    if (f.getConfig().getString("plugin.nexusShop.main.items." + s + ".target").equals("upgrade")) {

                        player.sendMessage(upgradeIsland(player, p));

                        e.setCancelled(true);

                        ItemMeta im = is.getItemMeta();

                        Files islands = new Files(p, "islands.yml");
                        double x = islands.getConfig().getDouble("island." + p.getConfig().getString("island." + player.getName()) + ".lvl");

                        ArrayList<String> lore = new ArrayList<>();

                        int spc = (19 + 6 * ((int) x + 1)) * (19 + 6 * ((int) x + 1));

                        double y = Math.sinh((x + 30)/20);
                        if (x < 100) {
                            lore.add("to lvl " + (int) (x + 1));
                            lore.add("cost: " + (int) y);
                            lore.add("total space: " + spc + " (" + (19 + 6 * ((int) x + 1)) + "*" + (19 + 6 * ((int) x + 1)) + ")");
                        } else {
                            lore.add("max upgraded");
                        }

                        im.setLore(lore);

                        e.getCurrentItem().setItemMeta(im);

                        break;
                    }

                    player.openInventory(new VillagerInteract(p).inv(f.getConfig().getString("plugin.nexusShop.main.items." + s + ".target"), player));
                    break;
                }
            }

        } else if (inv.getTitle().equals(friendlyName)) {
            int size = f.getConfig().getInt("plugin.nexusShop.friendly.size");
            if (e.getRawSlot() < size) {

                for (String s : f.getConfig().getConfigurationSection("plugin.nexusShop.friendly.items").getKeys(false)) {
                    ItemStack is = f.getConfig().getItemStack("plugin.nexusShop.friendly.items." + s + ".item");
                    if (is.getItemMeta().getDisplayName().equals(meta.getDisplayName())) {
                        int i = player.getLevel() - Integer.parseInt(meta.getLore().get(0).replace("cost: ", ""));
                        if (i > -1) {

                            confirm(player, item, inv.getTitle());

                        } else {
                            e.setCancelled(true);
                            player.sendMessage("not enough xp levels");
                        }
                        break;
                    }
                }
            }
        } else if (inv.getTitle().equals(enemyName)) {
            int size = f.getConfig().getInt("plugin.nexusShop.enemy.size");
            if (e.getRawSlot() < size) {

                for (String s : f.getConfig().getConfigurationSection("plugin.nexusShop.enemy.items").getKeys(false)) {
                    ItemStack is = f.getConfig().getItemStack("plugin.nexusShop.enemy.items." + s + ".item");
                    if (is.getItemMeta().getDisplayName().equals(meta.getDisplayName())) {
                        int i = player.getLevel() - Integer.parseInt(meta.getLore().get(0).replace("cost: ", ""));
                        if (i > -1) {

                            confirm(player, item, inv.getTitle());

                        } else {
                            e.setCancelled(true);
                            player.sendMessage("not enough xp levels");
                        }
                        break;
                    }
                }
            }
        } else if (inv.getTitle().equals("§9Confirm §3Friendly Tower")
                || inv.getTitle().equals("§9Confirm §4Enemy Tower")) {


            if (e.getRawSlot() < 9) {
                if (meta.getDisplayName().equals("§3Confirm")) {




                    ItemStack buy = new ItemStack(inv.getItem(4));
                    ItemMeta buyMeta = buy.getItemMeta();
                    buyMeta.setDisplayName(inv.getTitle().replace("§9Confirm ", ""));
                    ArrayList<String> list = new ArrayList<>();
                    list.add(inv.getItem(4).getItemMeta().getDisplayName());
                    buyMeta.setLore(list);
                    buy.setItemMeta(buyMeta);

                    e.setCancelled(true);

                    boolean isSpaceLeft = false;
                    for (ItemStack is : player.getInventory().getStorageContents()) {
                        if (is == null) {
                            isSpaceLeft = true;
                            break;
                        }
                    }
                    if (!isSpaceLeft) {
                        player.sendMessage("there is no more space left in your inventory");
                        return;
                    }
                    openMainInv(player);
                    player.sendMessage("place the tower somewhere on the ground when in war or in your island");
                    player.setLevel(player.getLevel() - Integer.parseInt(inv.getItem(4).getItemMeta().getLore().get(0).replace("cost: ", "")));
                    player.getInventory().addItem(buy);

                } else if (meta.getDisplayName().equals("§cCancel")) {
                    e.setCancelled(true);

                    openMainInv(player);
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }

    private void confirm(Player player, ItemStack is, String s) {

        Inventory inv = Bukkit.createInventory(null, 9, "§9Confirm " + s.replace("s", ""));
        inv.setItem(0, newItemStack(Material.WOOL, 13, "§3Confirm"));
        inv.setItem(1, newItemStack(Material.WOOL, 13, "§3Confirm"));
        inv.setItem(2, newItemStack(Material.WOOL, 13, "§3Confirm"));

        inv.setItem(4, is);

        inv.setItem(6, newItemStack(Material.WOOL, 14, "§cCancel"));
        inv.setItem(7, newItemStack(Material.WOOL, 14, "§cCancel"));
        inv.setItem(8, newItemStack(Material.WOOL, 14, "§cCancel"));

        player.openInventory(inv);

    }

    private void openMainInv(Player player) {
        player.openInventory(new VillagerInteract(p).inv("main", player));
    }

    public static ItemStack newItemStack(Material m, int damage, String displayName) {

        ItemStack is = new ItemStack(m, (byte) 1, (short) damage);
        ItemMeta im = is.getItemMeta();
        if (displayName != null) {
            im.setDisplayName(displayName);
        }
        is.setItemMeta(im);
        return is;
    }

    private static String upgradeIsland(Player player, Main p) {

        Files islands = new Files(p, "islands.yml");

        String island = p.getConfig().getString("island." + player.getName());

        double x = islands.getConfig().getDouble("island." + island + ".lvl");

        double y = Math.sinh((x + 35) / 20);

        if (x == 100) {
            return "max upgraded";
        }

        if (player.getLevel() >= (int) y) {

            player.setLevel(player.getLevel() - (int) y);

            islands.getConfig().set("island." + island + ".lvl", (int) x + 1);
            islands.saveConfig();

            return "upgraded";
        }

        return "not enough XP";
    }
}
