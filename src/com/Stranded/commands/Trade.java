package com.Stranded.commands;

import com.Stranded.trade.TradeStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Trade implements CommandExecutor {


    public static HashMap<Player, TradeStatus> tradeStatus = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player");
            return false;
        }





        return false;
    }
}
