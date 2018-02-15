package com.Stranded.trade;

import com.Stranded.commands.Trade;
import com.Stranded.trade.events.InventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class TradeStatus {

    private final Player player1;
    private final Player player2;
    private boolean confirm1;
    private boolean confirm2;
    private int xp1;
    private int xp2;

    public TradeStatus(Player player, Player player2) {
        this.player1 = player;
        this.player2 = player2;
        confirm1 = false;
        confirm2 = false;
        xp1 = 0;
        xp2 = 0;
    }

    public void start() {

        Inventory inv = Bukkit.createInventory(null, 54, "Trade");

        inv.setItem(0, InventoryEvent.confirm);
        inv.setItem(9, InventoryEvent.cancel);
        inv.setItem(18, InventoryEvent.addXP);
        inv.setItem(27, InventoryEvent.removeXP);
        inv.setItem(36, InventoryEvent.refresh);

        player1.openInventory(inv);
        player2.openInventory(inv);

    }

    public Player getPlayer2() {
        return player2;
    }

    public int getXp1() {
        return xp1;
    }

    public void addXp(Player player) {
        if (player.equals(player1)) {
            xp1++;
        } else {
            xp2++;
        }
    }

    public void removeXp(Player player) {
        if (player.equals(player1)) {
            xp1--;
        } else {
            xp2--;
        }
    }

    public boolean getConfirm1() {
        return confirm1;
    }

    public void setConfirm1(boolean confirm, Player player) {
        if (player.equals(player1)) {
            this.confirm1 = confirm;
        } else {
            this.confirm2 = confirm;
        }
    }

    public void updateTrade(Player player) {
        for (HumanEntity pl :player.getOpenInventory().getTopInventory().getViewers()) {
            if (player1.equals(pl)) {
                player2.openInventory(player.getOpenInventory().getTopInventory());
            } else {
                player1.openInventory(player.getOpenInventory().getTopInventory());
            }
        }

    }



    public void cancel() {

    }
}
