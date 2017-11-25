package com.Stranded.gamble;

import com.Stranded.Main;
import com.Stranded.gamble.inv.InvGamble;
import com.Stranded.gamble.inv.InvItem;
import com.Stranded.gamble.inv.InvSlots;
import com.Stranded.gamble.inv.InvStartSlots;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvClickEvent implements Listener {

    private Main p;

    public InvClickEvent(Main main) {
        p = main;
    }


    @EventHandler
    @SuppressWarnings("unused")
    public void clickEvent(InventoryClickEvent e) {

        Inventory inv = e.getInventory();
        String title = inv.getTitle();
        Player player = (Player) e.getWhoClicked();

        if (title.equals(InvStartSlots.title)) {
            if (e.getRawSlot() < inv.getSize()) {
                if (e.getCurrentItem() == null) {
                    return;
                }
                if (!e.getCurrentItem().hasItemMeta()) {
                    return;
                }

                ItemStack is = e.getCurrentItem();

                if (is.equals(InvStartSlots.size33)) {
                    InvSlots.openInv(p, player, 3, 27, false);
                } else if (is.equals(InvStartSlots.size34)) {
                    InvSlots.openInv(p, player, 3, 36, false);
                } else if (is.equals(InvStartSlots.size35)) {
                    InvSlots.openInv(p, player, 3, 45, false);
                } else if (is.equals(InvStartSlots.size36)) {
                    InvSlots.openInv(p, player, 3, 54, false);
                } else if (is.equals(InvStartSlots.size53)) {
                    InvSlots.openInv(p, player, 5, 27, false);
                } else if (is.equals(InvStartSlots.size54)) {
                    InvSlots.openInv(p, player, 5, 36, false);
                } else if (is.equals(InvStartSlots.size55)) {
                    InvSlots.openInv(p, player, 5, 45, false);
                } else if (is.equals(InvStartSlots.size56)) {
                    InvSlots.openInv(p, player, 5, 54, false);
                } else if (is.equals(InvStartSlots.size73)) {
                    InvSlots.openInv(p, player, 7, 27, false);
                } else if (is.equals(InvStartSlots.size74)) {
                    InvSlots.openInv(p, player, 7, 36, false);
                } else if (is.equals(InvStartSlots.size75)) {
                    InvSlots.openInv(p, player, 7, 45, false);
                } else if (is.equals(InvStartSlots.size76)) {
                    InvSlots.openInv(p, player, 7, 54, false);
                } else if (is.equals(InvStartSlots.size93)) {
                    InvSlots.openInv(p, player, 9, 27, false);
                } else if (is.equals(InvStartSlots.size94)) {
                    InvSlots.openInv(p, player, 9, 36, false);
                } else if (is.equals(InvStartSlots.size95)) {
                    InvSlots.openInv(p, player, 9, 45, false);
                } else if (is.equals(InvStartSlots.size96)) {
                    InvSlots.openInv(p, player, 9, 54, false);
                }
            }
        } else if (title.equals(InvGamble.title)) {

            if (e.getRawSlot() < inv.getSize()) {
                if (e.getCurrentItem() == null) {
                    return;
                }
                if (!e.getCurrentItem().hasItemMeta()) {
                    return;
                }

                ItemStack is = e.getCurrentItem();

                if (is.equals(InvGamble.random)) {
                    InvItem.openInv(p, player);
                } else if (is.equals(InvGamble.slots)) {
                    InvStartSlots.openInv(player);
                }
            }
        } else if (title.equals(InvSlots.title)) {
            e.setCancelled(true);
        } else if (title.equals(InvItem.title)) {
            e.setCancelled(true);
        }
    }
}
