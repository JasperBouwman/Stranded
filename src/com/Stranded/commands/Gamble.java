package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.gamble.inv.InvGamble;
import com.Stranded.gamble.inv.InvItem;
import com.Stranded.gamble.inv.InvSlots;
import com.Stranded.gamble.inv.InvStartSlots;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamble implements CommandExecutor {

    private Main p;

    public Gamble(Main main) {
        p = main;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        //gamble
        //gamble item
        //gamble slots
        //gamble slots <width> <height>

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player to sue this");
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 0) {
            InvGamble.openInv(player);
            return false;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("item")) {
                InvItem.openInv(p, player);
                return false;
            } else if (args[0].equalsIgnoreCase("slots")) {
                InvStartSlots.openInv(player);
                return false;
            }
        }
        if (args.length == 3) {

            if (args[0].equalsIgnoreCase("slots")) {
                try {
                    int width = Integer.parseInt(args[1]);
                    int height = Integer.parseInt(args[2]);

                    InvSlots.openInv(p, player, width, height, true);
                    return false;

                } catch (NumberFormatException e) {
                    player.sendMessage("you must use numbers");
                }
            }

        } else {
            player.sendMessage("wrong use");
        }


        return false;
    }
}
