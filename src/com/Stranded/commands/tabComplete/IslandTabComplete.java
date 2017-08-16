package com.Stranded.commands.tabComplete;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.Island;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IslandTabComplete implements TabCompleter {

    private Main p;

    public IslandTabComplete(Main main) {
        p = main;
    }

    private static List<String> ISLAND = new ArrayList<>();
    private static List<String> VISIT = new ArrayList<>();
    private static List<String> INVITE = new ArrayList<>();


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            return null;
        }

        clearTabComplete();
        fillTabComplete(args, (Player) commandSender);

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], ISLAND,
                    new ArrayList<>(ISLAND.size()));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("visit")) {
                return StringUtil.copyPartialMatches(args[1], VISIT,
                        new ArrayList<>(VISIT.size()));
            }
            if (args[0].equalsIgnoreCase("invite")) {
                return StringUtil.copyPartialMatches(args[1], INVITE,
                        new ArrayList<>(INVITE.size()));
            }
        }

        return null;
    }

    private void clearTabComplete() {
        ISLAND.clear();
        VISIT.clear();
    }

    private void fillTabComplete(String[] args, Player player) {

        Files islands = new Files(p, "islands.yml");

        for (CmdManager command : new Island(p).actions) {
            ISLAND.add(command.getName());
        }
        Collections.sort(ISLAND);

        if (args.length == 2) {
            VISIT.addAll(islands.getConfig().getConfigurationSection("island").getKeys(false));

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!p.getConfig().contains("island." + online.getName())) {
                    INVITE.add(online.getName());
                }
                else if (!p.getConfig().getString("island." + online.getName()).equals(p.getConfig().getString("island." + player.getName()))) {
                    INVITE.add(online.getName());
                }
            }
        }

    }
}
