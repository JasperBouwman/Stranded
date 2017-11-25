package com.Stranded.trade.events;

import com.Stranded.Main;
import com.Stranded.commands.Trade;
import com.Stranded.fancyMassage.FancyMessage;
import com.Stranded.fancyMassage.Test;
import com.Stranded.trade.TradeStatus;
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

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

@SuppressWarnings("unused")
public class InventoryEvent implements Listener {

    static ItemStack confirm = toItemStack(Material.STAINED_GLASS_PANE, 5, "Confirm");
    static ItemStack edit = toItemStack(Material.STAINED_GLASS_PANE, 4, "Edit");
    static ItemStack cancel = toItemStack(Material.STAINED_GLASS_PANE, 14, "Cancel");
    static ItemStack addXP = toItemStack(Material.EXP_BOTTLE, 0, "Plus 1 XP level");
    static ItemStack removeXP = toItemStack(Material.EXP_BOTTLE, 0, "Minus 1 XP level");
    static ItemStack refresh = toItemStack(Material.GOLDEN_APPLE, 1, "Refresh");

    private Main p;

    public InventoryEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void inventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getTitle().startsWith("Trade")) {
            Player player = (Player) e.getPlayer();
            Bukkit.getScheduler().runTaskLater(p, () -> player.openInventory(inv), 1);
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getTitle().startsWith("Trade")) {

            Player player = (Player) e.getWhoClicked();
            int slot = e.getRawSlot();
            InventoryAction a = e.getAction();

            if (!Trade.tradeStatus.containsKey(player)) {
                return;
            }


            TradeStatus tradeStatus = new TradeStatus(player);

            if (slot == 0) {//confirm
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ONE)) {
                    return;
                }

                tradeStatus.setConfirm(true);
                tradeStatus.updateTrade(inv);

            } else if (slot == 9) {//cancel
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ONE)) {
                    return;
                }

                tradeStatus.cancel();

            } else if (slot == 18) {//add xp
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ONE)) {
                    return;
                }

                if (player.getLevel() > tradeStatus.getXp()) {
                    tradeStatus.addXp();
                } else {
                    player.sendMessage("not enough xp");
                }

            } else if (slot == 27) {//remove xp
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ONE)) {
                    return;
                }

                if (tradeStatus.getXp() > 0) {
                    tradeStatus.removeXp();
                } else {
                    player.sendMessage("can not go below 0 xp");
                }
            } else if (slot == 36) {//refresh
                e.setCancelled(true);
                tradeStatus.updateTrade(inv);
            } else if (((slot > 0) && (slot < 4))
                    || ((slot > 9) && (slot < 13))
                    || ((slot > 18) && (slot < 22))
                    || ((slot > 27) && (slot < 31))
                    || ((slot > 36) && (slot < 40))
                    || ((slot > 45) && (slot < 49))) {

                tradeStatus.updateTrade(inv);

            } else if (slot < inv.getSize())  {
                e.setCancelled(true);
            }


        }
    }
}
