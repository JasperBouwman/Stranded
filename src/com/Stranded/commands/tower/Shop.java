package com.Stranded.commands.tower;

import com.Stranded.commands.CmdManager;
import com.Stranded.towers.inventory.InventoryEvent;
import com.Stranded.towers.inventory.shop.InvShop;
import com.Stranded.towers.inventory.shop.InvShopFriendly;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.Stranded.api.ServerMessages.sendWrongUse;
import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class Shop extends CmdManager {
    @Override
    public String getName() {
        return "shop";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        switch (args.length) {
            case 1:
                InvShop.openInv(player);
                break;
            case 2:
                //towersData
                switch (args[1].toLowerCase()) {
                    case "tnt":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 14, "TNT", "cost: 15"), "§4Enemy Towers");
                        break;
                    case "slowness":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 10, "Slowness", "cost: 15"), "§4Enemy Towers");
                        break;
                    case "hunger":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 13, "Hunger", "cost: 15"), "§4Enemy Towers");
                        break;
                    case "wither":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 15, "Wither", "cost: 15"), "§4Enemy Towers");
                        break;
                    case "arrow":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 8, "Arrow", "cost: 15"), "§4Enemy Towers");
                        break;
                    case "haste":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 4, "Haste", "cost: 10"), "§3Friendly Towers");
                        break;
                    case "regeneration":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 1, "Regeneration", "cost: 15"), "§3Friendly Towers");
                        break;
                    case "speed":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 0, "Speed", "cost: 7"), "§3Friendly Towers");
                        break;
                    case "teleport":
                        InventoryEvent.confirm(player, toItemStack(Material.WOOL, 3, "Teleport", "cost: 15"), "§3Friendly Towers");
                        break;
                    case "friendly":
                        InvShopFriendly.openInv(player);
                        break;
                    case "enemy":
                        InvShopFriendly.openInv(player);
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "Tower not found");
                        player.sendMessage(ChatColor.RED + "Available towers: tnt, slowness, hunger, wither, arrow, haste, regeneration, speed, teleport, friendly, enemy");
                }
                break;
            default:
                sendWrongUse(player, "/tower shop");
                break;
        }

    }
}
