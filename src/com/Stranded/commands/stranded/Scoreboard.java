package com.Stranded.commands.stranded;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.events.ChatEvent;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class Scoreboard extends CmdManager {

    private void updateScoreboard() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            com.Stranded.Scoreboard.scores(p, pl);
        }
    }

    @Override
    public String getName() {
        return "scoreboard";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //stranded scoreboard
        //stranded scoreboard name
        //stranded scoreboard name <name...>
        //stranded scoreboard removeLine <line>
        //stranded scoreboard addLine <line> [text...]
        //stranded scoreboard setLine <line> [text...]

        Files pluginData = new Files(p, "pluginData.yml");

        if (args.length > 2) {
            if (args[1].equalsIgnoreCase("name")) {
                pluginData.getConfig().set("plugin.scoreboard.default.name", ChatEvent.replaceColors(Joiner.on(" ").join(Arrays.asList(args).subList(2, args.length))));
                pluginData.saveConfig();
                player.sendMessage("successfully renamed");

                updateScoreboard();
                return;
            } else if (args[1].equalsIgnoreCase("addLine".toLowerCase())) {

                int line;
                try {
                    line = Integer.parseInt(args[2]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage("you must use a number as line number");
                    return;
                }
                line--;

                if (line < 0) {
                    player.sendMessage("go higher than 0");
                    return;
                }

                ArrayList<String> scoreboard = (ArrayList<String>) pluginData.getConfig().getStringList("plugin.scoreboard.default.lines");
                scoreboard.add(line, ChatEvent.replaceColors(Joiner.on(" ").join(Arrays.asList(args).subList(3, args.length))));
                pluginData.getConfig().set("plugin.scoreboard.default.lines", scoreboard);
                pluginData.saveConfig();
                player.sendMessage("line added");
                updateScoreboard();
                return;

            } else if (args[1].equalsIgnoreCase("setLine".toLowerCase())) {
                int line;
                try {
                    line = Integer.parseInt(args[2]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage("you must use a number as line number");
                    return;
                }
                line--;

                if (line < 0) {
                    player.sendMessage("go higher than 0");
                    return;
                }

                ArrayList<String> scoreboard = (ArrayList<String>) pluginData.getConfig().getStringList("plugin.scoreboard.default.lines");
                scoreboard.set(line, ChatEvent.replaceColors(Joiner.on(" ").join(Arrays.asList(args).subList(3, args.length))));
                pluginData.getConfig().set("plugin.scoreboard.default.lines", scoreboard);
                pluginData.saveConfig();
                player.sendMessage("line set");
                updateScoreboard();
                return;
            } else {
                player.sendMessage("wrong use");
            }
        }

        if (args.length == 1) {
            FancyMessage fm = new FancyMessage();

            fm.addText("This is the scoreboard text:", Colors.DARK_AQUA);

            fm.addText("\nname: " + pluginData.getConfig().getString("plugin.scoreboard.default.name"), Colors.BLUE);

            int i = 1;
            for (String line : pluginData.getConfig().getStringList("plugin.scoreboard.default.lines")) {
                fm.addText("\nline " + i + ": §r" + line, Colors.BLUE);
                fm.addSuggest("/stranded scoreboard setLine " + i + " ");
                fm.addHover("/stranded scoreboard setLine " + i + " [text...]", Colors.BLUE);
                i++;
            }

            fm.sendMessage(player);

        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("name")) {
                player.sendMessage("The scoreboard name is: " + pluginData.getConfig().getString("plugin.scoreboard.default.name"));
            } else {
                player.sendMessage("wrong use");
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("removeLine".toLowerCase())) {

                int line;
                try {
                    line = Integer.parseInt(args[2]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage("you must use a number as line number");
                    return;
                }
                line--;

                if (line < 0) {
                    player.sendMessage("go higher than 0");
                    return;
                }

                ArrayList<String> scoreboard = (ArrayList<String>) pluginData.getConfig().getStringList("plugin.scoreboard.default.lines");
                if (scoreboard.size() > line) {
                    scoreboard.remove(line);

                    player.sendMessage("successfully removed");
                    pluginData.getConfig().set("plugin.scoreboard.default.lines", scoreboard);
                    pluginData.saveConfig();

                    updateScoreboard();
                } else {
                    player.sendMessage("this line doesn't exist");
                }
            } else {
                player.sendMessage("wrong use");
            }
        }
    }
}
