package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard extends CmdManager {

    public static void removeScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        String name = "qchhtdfgd";
        Objective objective = board.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
    }

    @Override
    public String getName() {
        return "scoreboard";
    }

    @Override
    public String getAlias() {
        return "sb";
    }

    @Override
    public void run(String[] args, Player player) {

        if (args.length == 2) {

            Files f = new Files(p, "playerData.yml");

            if (args[1].equalsIgnoreCase("off")) {

                removeScoreboard(player);
                f.getConfig().set("scoreboard." + player.getName(), 0);
                f.saveConfig();
                player.sendMessage("scoreboard is now off");

            } else if (args[1].equalsIgnoreCase("on")) {

                f.getConfig().set("scoreboard." + player.getName(), 1);
                f.saveConfig();
                player.sendMessage("scoreboard is now on");

            } else if (args[1].equalsIgnoreCase("show")) {

                f.getConfig().set("scoreboard." + player.getName(), 2);
                f.saveConfig();
                player.sendMessage("scoreboard is now showed for 10 seconds");

            } else {
                player.sendMessage("wrong use");
            }
        } else {
            player.sendMessage("wrong use");
        }
    }
}
