package com.Stranded.trade;

import com.Stranded.commands.Trade;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class TradeStatus {

    private ArrayList<ItemStack> toTrade;
    private Player player;
    private Player tradeWith;
    private int tradeID;
    private boolean confirm;
    private int xp;

    public TradeStatus(Player player, Player tradeWith) {
        this.player = player;
        this.tradeWith = tradeWith;
        confirm = false;
        xp = 0;
        initPlayer();
    }

    public TradeStatus(Player player) {
        this.player = player;
        initPlayer();
    }

    private void initPlayer() {
        if (Trade.tradeStatus.containsKey(player)) {
            tradeID = Trade.tradeStatus.get(player).getTradeID();
        } else {
            int i = 0;
            while (true) {
                boolean b = true;
                for (Player player : Trade.tradeStatus.keySet()) {
                    if (Trade.tradeStatus.get(player).getTradeID() == i) {
                        b = false;
                    }
                }
                i++;
                if (b) {
                    break;
                }
            }
            this.tradeID = i;
        }

    }

    public int getTradeID() {
        return tradeID;
    }

    public Player getTradeWith() {
        return tradeWith;
    }

    public int getXp() {
        return xp;
    }

    public void addXp() {
        xp++;
    }

    public void removeXp() {
        xp--;
    }

    public boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
        if (confirm && Trade.tradeStatus.get(tradeWith).getConfirm()) {
            //start timer
        } else if (!confirm && Trade.tradeStatus.get(tradeWith).getConfirm()) {
            //stop timer
        }
    }

    public void updateTrade(Inventory inv) {

    }

    public void cancel() {

    }
}
