package com.Stranded.commands.tower;

import com.Stranded.commands.CmdManager;
import com.Stranded.towers.inventory.ownTowers.InvFilteredTowers;
import com.Stranded.towers.inventory.ownTowers.InvMyTowers;
import com.Stranded.towers.inventory.ownTowers.InvTowers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static com.Stranded.api.ServerMessages.sendWrongUse;
import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class Own extends CmdManager {
    @Override
    public String getName() {
        return "own";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        switch (args.length) {
            case 1:

                InvTowers.openInv(player);

                break;
            case 2:

                switch (args[1].toLowerCase()) {
                    case "all":
                        InvMyTowers.openInv(p, player);
                        break;
                    case "tnt":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "TNT"));
                        break;
                    case "slowness":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Slowness"));
                        break;
                    case "hunger":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Hunger"));
                        break;
                    case "wither":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Wither"));
                        break;
                    case "arrow":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Arrow"));
                        break;
                    case "haste":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Haste"));
                        break;
                    case "regeneration":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Regeneration"));
                        break;
                    case "speed":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Speed"));
                        break;
                    case "teleport":
                        InvFilteredTowers.openInv(player, toItemStack(Material.WOOL, 14, "Teleport"));
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "Wrong filter used");
                        player.sendMessage(ChatColor.RED + "Available filters: tnt, slowness, hunger, wither, arrow, haste, regeneration, speed, all");
                }

                break;
            default:
                sendWrongUse(player, new String[]{"/tower own [all:tnt:slowness:hunger:wither:arrow:haste:regeneration:speed:teleport]", "/tower own"});
                break;
        }
    }
}
