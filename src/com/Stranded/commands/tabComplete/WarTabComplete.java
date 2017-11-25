package com.Stranded.commands.tabComplete;

import com.Stranded.Main;
import com.Stranded.commands.War;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.WarIsland;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WarTabComplete implements TabCompleter {

    private static List<String> War = new ArrayList<>();
    private Main p;

    public WarTabComplete(Main main) {
        p = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            return null;
        }

        clearTabComplete();
        fillTabComplete(args, (Player) commandSender);

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], War,
                    new ArrayList<>(War.size()));
        }

        return null;
    }

    private void clearTabComplete() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.getType() == List.class) {
                try {
                    ((List<String>) f.get(f)).clear();
                } catch (IllegalAccessException e) {
                }
            }
        }
    }

    private void fillTabComplete(String[] args, Player player) {

        for (CmdManager command : new War(p).actions) {
            War.add(command.getName());
        }
    }
}
