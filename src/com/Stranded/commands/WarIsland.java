package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.warIsland.Create;
import com.Stranded.commands.warIsland.Delete;
import com.Stranded.commands.warIsland.Edit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarIsland implements CommandExecutor {

    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public WarIsland(Main main) {
        p = main;

        actions.add(new Edit());
        actions.add(new Create());
        actions.add(new Delete());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player yo use this");
            return false;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("Stranded.createWarIsland")) {
            player.sendMessage("you don't have permission");
            return false;
        }
        if (args.length == 0) {
            player.sendMessage("pls use args");
            return false;
        }

        for (CmdManager action : this.actions) {
            if (args[0].toLowerCase().equals(action.getName()) || args[0].toLowerCase().equals(action.getAlias())) {
                action.setMain(p);
                action.run(args, player);
                return false;
            }
        }
        player.sendMessage("this is not a sub-command");
        return false;
    }
}
