package com.Stranded.towers.inventory;

import com.Stranded.Main;
import com.Stranded.towers.Tower;
import com.Stranded.towers.inventory.ownTowers.InvFilteredTowers;
import com.Stranded.towers.inventory.ownTowers.InvMyTowers;
import com.Stranded.towers.inventory.ownTowers.InvTowers;
import com.Stranded.towers.inventory.shop.InvShop;
import com.Stranded.towers.inventory.shop.InvShopEnemy;
import com.Stranded.towers.inventory.shop.InvShopFriendly;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class InventoryEvent implements Listener {

    private static ItemStack confirm = toItemStack(Material.WOOL, 13, "§3Confirm");
    private static ItemStack cancel = toItemStack(Material.WOOL, 14, "§cCancel");
    private Main p;

    public InventoryEvent(Main main) {
        p = main;
    }

    public static ItemStack toItemStack(Material m, int damage, String displayName, String... lore) {
        ItemStack is = new ItemStack(m, 1, (short) damage);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName(displayName);

        if (lore != null) {
            im.setLore(Arrays.asList(lore));
        }

        is.setItemMeta(im);

        return is;
    }

    @EventHandler
    @SuppressWarnings({"unused", "deprecation"})
    public void onInventoryClickEvent(InventoryClickEvent e) throws IllegalAccessException {

        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();

        ItemStack item = e.getCurrentItem();
        if (item == null) {
            return;
        }


        //form the command /tower
        if (inv.getTitle().equals(InvTower.inv(player).getTitle())) {
            if (e.getRawSlot() >= inv.getSize()) {
                return;
            }
            if (!item.hasItemMeta()) {
                return;
            }
            if (item.equals(InvTower.ownTowers(player))) {
                e.setCancelled(true);
                InvTowers.openInv(player);
            } else if (item.equals(InvTower.shop)) {
                e.setCancelled(true);
                InvShop.openInv(player);
            }
        }

        //my towers
        if (inv.getTitle().equals(InvTowers.inv().getTitle())) {
            if (e.getRawSlot() >= inv.getSize()) {
                return;
            }
            if (!item.hasItemMeta()) {
                return;
            }
            if (item.equals(InvTowers.allTowers)) {
                InvMyTowers.openInv(p, player);
                p.getConfig().set("tower." + player.getUniqueId().toString() + ".page", 0);
            } else {
                InvFilteredTowers.openInv(p, player, item);
                p.getConfig().set("tower." + player.getUniqueId().toString() + ".page", 0);
            }
        }

        if (inv.getTitle().equals("All Towers") || inv.getTitle().startsWith("Towers, Filter: ")) {

            boolean inInv = !(e.getRawSlot() >= inv.getSize());
            int rawSlot = e.getRawSlot();
            InventoryAction a = e.getAction();

            switch (a) {
                case PLACE_ALL:
                    if (inInv) {
                        if (rawSlot == inv.getSize() - 5) {
                            if (e.getCursor().getAmount() == 1) {
                                if (isTower(e.getCursor())) {
                                    player.sendMessage("tower is added");
                                    Tower t = new Tower(p, player.getName());
                                    t.saveTower(e.getCursor());
                                    e.setCursor(new ItemStack(Material.AIR));
                                    inv.setItem(rawSlot, new ItemStack(Material.AIR));
                                    if (!inv.getTitle().equals("All Towers")) {
                                        String title = inv.getTitle().replace("Towers, Filter: ", "");

                                        InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 0, title));
                                    } else {
                                        InvMyTowers.openInv(p, player);
                                    }
                                } else {
                                    player.sendMessage("you only can place towers here to add");
                                    e.setCancelled(true);
                                }
                            } else {
                                player.sendMessage("you can only place 1 tower here to add");
                                e.setCancelled(true);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    break;
                case PLACE_ONE:
                    if (inInv) {
                        e.setCancelled(true);
                    }
                    break;
                case MOVE_TO_OTHER_INVENTORY:
                    if (isTower(item) && inInv) {
                        new Tower(p, player.getName()).removeTower(item);
                    } else if (!isTower(item) || inInv) {
                        e.setCancelled(true);
                    } else {
                        if (e.getCurrentItem().getAmount() == 1) {
                            new Tower(p, player.getName()).saveTower(item);

                            if (!inv.getTitle().equals("All Towers")) {
                                String title = inv.getTitle().replace("Towers, Filter: ", "");

                                player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));

                                InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 0, title));
                            } else {

                                player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));

                                InvMyTowers.openInv(p, player);
                            }
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    break;
                case PICKUP_ALL:
                    if (inInv && isTower(item)) {
                        new Tower(p, player.getName()).removeTower(item);
                    } else if (inInv) {
                        if (rawSlot == inv.getSize() - 1) {

                            int page = 0;
                            if (p.getConfig().contains("tower." + player.getUniqueId().toString() + ".page")) {
                                page = p.getConfig().getInt("tower." + player.getUniqueId().toString() + ".page");
                            }

                            player.sendMessage("page " + (page + 1));

                            p.getConfig().set("tower." + player.getUniqueId().toString() + ".page", page + 1);
                            p.saveConfig();

                            if (!inv.getTitle().equals("All Towers")) {
                                String title = inv.getTitle().replace("Towers, Filter: ", "");

                                InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 0, title));
                            } else {
                                InvMyTowers.openInv(p, player);
                            }


                        } else if (rawSlot == inv.getSize() - 9) {

                            int page = 0;
                            if (p.getConfig().contains("tower." + player.getUniqueId().toString() + ".page")) {
                                page = p.getConfig().getInt("tower." + player.getUniqueId().toString() + ".page");
                            }
                            if (page == 0) {
                                e.setCancelled(true);
                                return;
                            }
                            player.sendMessage("page " + (page - 1));

                            p.getConfig().set("tower." + player.getUniqueId().toString() + ".page", page - 1);
                            p.saveConfig();

                            if (!inv.getTitle().equals("All Towers")) {
                                String title = inv.getTitle().replace("Towers, Filter: ", "");

                                InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 0, title));
                            } else {
                                InvMyTowers.openInv(p, player);
                            }

                        } else if (rawSlot == inv.getSize() - 6) {
                            InvTowers.openInv(player);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                    break;
                case PICKUP_ONE:
                    if (inv.getSize() - 9 > rawSlot && inInv) {
                        e.setCancelled(true);
                    }
                    break;
                case PLACE_SOME:
                case CLONE_STACK:
                case HOTBAR_SWAP:
                case PICKUP_HALF:
                case PICKUP_SOME:
                case DROP_ALL_SLOT:
                case DROP_ONE_SLOT:
                case DROP_ALL_CURSOR:
                case DROP_ONE_CURSOR:
                case SWAP_WITH_CURSOR:
                case COLLECT_TO_CURSOR:
                case HOTBAR_MOVE_AND_READD:
                    if (inInv) {
                        e.setCancelled(true);
                    }
                    break;
                case NOTHING:
                case UNKNOWN:
                    break;
            }

        }

        //shop
        if (inv.getTitle().equals("Tower Shop")) {
            if (e.getRawSlot() >= inv.getSize()) {
                return;
            }
            if (!item.hasItemMeta()) {
                return;
            }
            if (item.equals(InvShop.friendly)) {
                InvShopFriendly.openInv(player);
            } else if (item.equals(InvShop.enemy)) {
                InvShopEnemy.openInv(player);
            }
        }

        if (inv.getTitle().equals("§4Enemy Towers")) {
            if (e.getRawSlot() >= inv.getSize()) {
                return;
            }

            for (Field o : InvShopEnemy.class.getDeclaredFields()) {
                ItemStack is = (ItemStack) o.get(o);

                if (item.equals(is)) {
                    int i = player.getLevel() - Integer.parseInt(is.getItemMeta().getLore().get(0).replace("cost: ", ""));
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

        if (inv.getTitle().equals("§3Friendly Towers")) {
            if (e.getRawSlot() >= inv.getSize()) {
                return;
            }

            for (Field o : InvShopFriendly.class.getDeclaredFields()) {
                ItemStack is = (ItemStack) o.get(o);

                if (item.equals(is)) {
                    int i = player.getLevel() - Integer.parseInt(is.getItemMeta().getLore().get(0).replace("cost: ", ""));
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

        if (inv.getTitle().equals("§9Confirm §3Friendly Tower")
                || inv.getTitle().equals("§9Confirm §4Enemy Tower")) {
            if (e.getRawSlot() >= inv.getSize()) {
                return;
            }
            if (item.equals(confirm)) {
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
                InvShop.openInv(player);
                player.sendMessage("place the tower somewhere on the ground when in war or in your island");
                player.setLevel(player.getLevel() - Integer.parseInt(inv.getItem(4).getItemMeta().getLore().get(0).replace("cost: ", "")));
                player.getInventory().addItem(buy);

            } else if (item.equals(cancel)) {
                e.setCancelled(true);
                InvShop.openInv(player);
            } else {
                e.setCancelled(true);
            }
        }

    }

    public static void confirm(Player player, ItemStack is, String s) {

        Inventory inv = Bukkit.createInventory(null, 9, "§9Confirm " + s.replace("s", ""));
        inv.setItem(0, confirm);
        inv.setItem(1, confirm);
        inv.setItem(2, confirm);

        inv.setItem(4, is);

        inv.setItem(6, cancel);
        inv.setItem(7, cancel);
        inv.setItem(8, cancel);

        player.openInventory(inv);

    }

    private boolean isTower(ItemStack is) {
        if (is.getType().equals(Material.WOOL)) {
            if (is.hasItemMeta()) {
                if (is.getItemMeta().hasDisplayName()) {
                    if (is.getItemMeta().getDisplayName().equals("§3Friendly Tower") || is.getItemMeta().getDisplayName().equals("§4Enemy Tower")) {
                        if (is.getItemMeta().hasLore()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
