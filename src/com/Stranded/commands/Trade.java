package com.Stranded.commands;

import com.Stranded.playerUUID.PlayerUUID;
import com.Stranded.trade.TradeStatus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Trade implements CommandExecutor {

    public static HashMap<Integer, TradeStatus> tradeStatus = new HashMap<>();
    public static HashMap<Player, Integer> tradeID = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player");
            return false;
        }

        //trade <player>

        Player player = (Player) commandSender;

        if (args.length == 1) {

            String invitedUUID = PlayerUUID.getPlayerUUID(args[0]);
            if (invitedUUID == null) {
                ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[0]);
                if (tempPlayers.size() > 1) {
                    player.sendMessage(ChatColor.RED + "The player '" + args[0] + "' is not found, but there are more players found with the same name (not case sensitive)");
                    return false;
                } else if (tempPlayers.size() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no player found with this name");
                    return false;
                } else {
                    invitedUUID = tempPlayers.get(0);
                }
            }
            if (invitedUUID == null) {
                player.sendMessage(ChatColor.RED + "This player isn't found, please make sure that you gave the right player name. this is case sensitive");
                return false;
            }
            Player tradeWith = PlayerUUID.getPlayer(UUID.fromString(invitedUUID));

            TradeStatus tr = new TradeStatus(player, tradeWith);

            //todo add to tradeID

            tr.start();

        }


        return false;
    }
}
