package com.Stranded.commands.tabComplete;

import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.WarIsland;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class WarIslandTabComplete implements TabCompleter {

    private static List<String> WarIsland = new ArrayList<>();
    private Main p;

    public WarIslandTabComplete(Main main) {
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
            return StringUtil.copyPartialMatches(args[0], WarIsland,
                    new ArrayList<>(WarIsland.size()));
        }

        return null;
    }

    private void clearTabComplete() {
        WarIsland.clear();
    }

    private void fillTabComplete(String[] args, Player player) {

        for (CmdManager command : new WarIsland(p).actions) {
            WarIsland.add(command.getName());
        }
    }
}
