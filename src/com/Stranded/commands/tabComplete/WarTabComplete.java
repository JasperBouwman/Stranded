package com.Stranded.commands.tabComplete;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.War;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class WarTabComplete implements TabCompleter {

    private Main p;

    public WarTabComplete(Main main) {
        p = main;
    }

    private static List<String> War = new ArrayList<>();


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
        War.clear();
    }

    private void fillTabComplete(String[] args, Player player) {

        Files islands = new Files(p, "islands.yml");

        for (CmdManager command : new War(p).actions) {
            War.add(command.getName());
        }

    }
}
