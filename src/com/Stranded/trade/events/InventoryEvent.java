package com.Stranded.trade.events;

import com.Stranded.Main;
import com.Stranded.commands.Trade;
import com.Stranded.trade.TradeStatus;
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

    public static ItemStack confirm = toItemStack(Material.STAINED_GLASS_PANE, 5, "Confirm");
    public static ItemStack edit = toItemStack(Material.STAINED_GLASS_PANE, 4, "Edit");
    public static ItemStack cancel = toItemStack(Material.STAINED_GLASS_PANE, 14, "Cancel");
    public static ItemStack addXP = toItemStack(Material.EXP_BOTTLE, 0, "Plus 1 XP level");
    public static ItemStack removeXP = toItemStack(Material.EXP_BOTTLE, 0, "Minus 1 XP level");
    public static ItemStack refresh = toItemStack(Material.GOLDEN_APPLE, 1, "Refresh");

    private Main p;

    public InventoryEvent(Main main) {
        p = main;
    }

    @EventHandler
    public void inventoryCloseEvent(InventoryCloseEvent e) {
//        Inventory inv = e.getInventory();
//        Player player = (Player) e.getPlayer();
//        if (inv.getTitle().startsWith("Trade") && Trade.tradeStatus.containsKey(player)) {
//            Bukkit.getScheduler().runTaskLater(p, () -> player.openInventory(inv), 1);
//        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();

        if (inv.getTitle().startsWith("Trade")) {

            Player player = (Player) e.getWhoClicked();
            int slot = e.getRawSlot();
            InventoryAction a = e.getAction();

            if (!Trade.tradeStatus.containsKey(Trade.tradeID.get(player))) {
                return;
            }
            TradeStatus tradeStatus = Trade.tradeStatus.get(Trade.tradeID.get(player));

            if (e.getCurrentItem() == null) {
                return;
            }

            if (slot == 0) {//confirm
                e.setCancelled(true);
                Main.println(e.getCurrentItem() + " " + a);

                if (!a.equals(InventoryAction.PICKUP_ALL)) {
                    return;
                }
                if (e.getCurrentItem().equals(confirm)) {
                    tradeStatus.setConfirm1(true, player);
                    tradeStatus.updateTrade(player);
                } else {
                    tradeStatus.setConfirm1(false, player);
                    inv.setItem(0, confirm);
                    tradeStatus.updateTrade(player);
                }
            } else if (slot == 9) {//cancel
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ALL)) {
                    return;
                }
                if (tradeStatus.getConfirm1()) {
                    tradeStatus.cancel();
                } else {
                    player.sendMessage("if you want to edit your trade you have to click on 'edit'");
                }

            } else if (slot == 18) {//add xp
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ALL)) {
                    return;
                }
                if (!tradeStatus.getConfirm1()) {
                    if (player.getLevel() > tradeStatus.getXp1()) {
                        tradeStatus.addXp(player);
                    } else {
                        player.sendMessage("not enough xp");
                    }
                } else {
                    player.sendMessage("if you want to edit your trade you have to click on 'edit'");
                }
            } else if (slot == 27) {//remove xp
                e.setCancelled(true);
                if (!a.equals(InventoryAction.PICKUP_ALL)) {
                    return;
                }
                if (!tradeStatus.getConfirm1()) {
                    if (tradeStatus.getXp1() > 0) {
                        tradeStatus.removeXp(player);
                    } else {
                        player.sendMessage("can not go below 0 xp");
                    }
                } else {
                    player.sendMessage("if you want to edit your trade you have to click on 'edit'");
                }

            } else if (slot == 36) {//refresh
                e.setCancelled(true);
                tradeStatus.updateTrade(player);
            } else if (((slot > 0) && (slot < 4))
                    || ((slot > 9) && (slot < 13))
                    || ((slot > 18) && (slot < 22))
                    || ((slot > 27) && (slot < 31))
                    || ((slot > 36) && (slot < 40))
                    || ((slot > 45) && (slot < 49))) {
                if (!tradeStatus.getConfirm1()) {
                    tradeStatus.updateTrade(player);
                } else {
                    player.sendMessage("if you want to edit your trade you have to click on 'edit'");
                }

            } else if (slot < inv.getSize()) {
                e.setCancelled(true);
            }
        }
    }
}
