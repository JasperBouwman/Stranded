package com.Stranded.commands;

import com.Stranded.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Tower implements CommandExecutor {

    private Main p;

    public Tower(Main main) {
        p = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
