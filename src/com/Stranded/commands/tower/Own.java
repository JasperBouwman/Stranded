package com.Stranded.commands.tower;

import com.Stranded.commands.CmdManager;
import com.Stranded.towers.inventory.ownTowers.InvFilteredTowers;
import com.Stranded.towers.inventory.ownTowers.InvMyTowers;
import com.Stranded.towers.inventory.ownTowers.InvTowers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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

        if (args.length == 1) {

            InvTowers.openInv(player);

        } else if (args.length == 2) {

            switch (args[1].toLowerCase()) {
                case "all":
                    InvMyTowers.openInv(p, player);
                    break;
                case "tnt":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "TNT"));
                    break;
                case "slowness":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Slowness"));
                    break;
                case "hunger":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Hunger"));
                    break;
                case "wither":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Wither"));
                    break;
                case "arrow":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Arrow"));
                    break;
                case "haste":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Haste"));
                    break;
                case "regeneration":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Regeneration"));
                    break;
                case "speed":
                    InvFilteredTowers.openInv(p, player, toItemStack(Material.WOOL, 14, "Speed"));
                    break;
                default:
                    player.sendMessage(ChatColor.RED + "Wrong filter used");
                    player.sendMessage(ChatColor.RED + "Available filters: tnt, slowness, hunger, wither, arrow, haste, regeneration, speed, all");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /tower own");
        }
    }
}
