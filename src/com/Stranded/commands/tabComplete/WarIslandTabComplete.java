package com.Stranded.commands.tabComplete;

import com.Stranded.Files;
import com.Stranded.Main;
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

public class WarIslandTabComplete implements TabCompleter {

    private static List<String> nill = new ArrayList<>();
    private static List<String> WARISLAND = new ArrayList<>();

    private static List<String> WARISLAND_DELETE = new ArrayList<>();

    private static List<String> WARISLAND_POS = new ArrayList<>();

    private static List<String> WARISLAND_EDIT = new ArrayList<>();
    private static List<String> WARISLAND_EDIT_THEME = new ArrayList<>();
    private static List<String> WARISLAND_EDIT_THEME_WARISLANDID = new ArrayList<>();
    private Main p;

    public WarIslandTabComplete(Main main) {
        p = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return null;
        }

        try {
            clearTabComplete();
        } catch (IllegalAccessException e) {
            //do nothing bc this just should work
        }

        fillTabComplete(args);

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], WARISLAND,
                    new ArrayList<>(WARISLAND.size()));
        }
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], WARISLAND_EDIT_THEME,
                    new ArrayList<>(WARISLAND_EDIT_THEME.size()));
        }
        if (args.length == 3) {
            return StringUtil.copyPartialMatches(args[2], WARISLAND_EDIT_THEME_WARISLANDID,
                    new ArrayList<>(WARISLAND_EDIT_THEME_WARISLANDID.size()));
        }

        if (args.length > 3) {
            return nill;
        }

        return null;
    }

    private void clearTabComplete() throws IllegalAccessException {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.getType() == List.class) {
                ((List<String>) f.get(f)).clear();
            }
        }
    }

    private void fillTabComplete(String[] args) {

        Files warIslands = new Files(p, "warIslands.yml");
        if (warIslands.getConfig().contains("warIslands.island")) {
            WARISLAND.addAll(warIslands.getConfig().getConfigurationSection("warIslands.island").getKeys(false));
        }
        if (warIslands.getConfig().contains("warIslands.island." + args[0])) {
            WARISLAND_EDIT_THEME.addAll(warIslands.getConfig().getConfigurationSection("warIslands.island." + args[0]).getKeys(false));
        }
        for (CmdManager command : new WarIsland(p).actions) {
            WARISLAND_EDIT_THEME_WARISLANDID.add(command.getName());
        }


    }
}
