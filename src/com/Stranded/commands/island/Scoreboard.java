package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard extends CmdManager {

    private static void removeScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        String name = "something";
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

        //island scoreboard on
        //island scoreboard off
        //island scoreboard show
        //island scoreboard show <time>

        String uuid = player.getUniqueId().toString();

        if (Main.reloadPending) {
            player.sendMessage("the server is trying to reload, please wait just a second to let the server reload");
            return;
        }
        Files playerData = new Files(p, "playerData.yml");

        if (args.length == 2) {

            if (args[1].equalsIgnoreCase("off")) {

                removeScoreboard(player);
                playerData.getConfig().set("scoreboard." + uuid, 0);
                playerData.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Scoreboard is now off");

            } else if (args[1].equalsIgnoreCase("on")) {

                playerData.getConfig().set("scoreboard." + uuid, 1);
                playerData.saveConfig();
                player.sendMessage(ChatColor.GREEN + "Scoreboard is now on");
                com.Stranded.Scoreboard.scores(p, player);

            } else if (args[1].equalsIgnoreCase("show")) {

                playerData.getConfig().set("scoreboard." + uuid, 2);
                playerData.saveConfig();

                com.Stranded.Scoreboard.scores(p, player);

                Bukkit.getScheduler().runTaskLater(p, () -> {

                    Files playerDataTemp = new Files(p, "playerData.yml");
                    removeScoreboard(player);
                    playerDataTemp.getConfig().set("scoreboard." + uuid, 0);
                    playerDataTemp.saveConfig();

                }, 200);

                player.sendMessage(ChatColor.GREEN + "Scoreboard is now showed for 10 seconds");
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /island scoreboard <on:off:show>");
            }
        } else if (args.length == 3) {

            if (args[1].equalsIgnoreCase("show")) {
                int time;
                try {
                    time = Integer.parseInt(args[2]);
                } catch (NumberFormatException nfe) {
                    player.sendMessage(ChatColor.RED + "You must use a number as time");
                    return;
                }

                playerData.getConfig().set("scoreboard." + uuid, 2);
                playerData.saveConfig();

                com.Stranded.Scoreboard.scores(p, player);

                Bukkit.getScheduler().runTaskLater(p, () -> {
                    Files playerDataTemp = new Files(p, "playerData.yml");
                    removeScoreboard(player);
                    playerDataTemp.getConfig().set("scoreboard." + uuid, 0);
                    playerDataTemp.saveConfig();

                }, time * 20);
                if (time == 1) {
                    player.sendMessage(ChatColor.GREEN + "Scoreboard is now showed for " + time + " second");
                } else {
                    player.sendMessage(ChatColor.GREEN + "Scoreboard is now showed for " + time + " seconds");
                }
            }

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /island scoreboard <on:off:show>");
        }
    }
}
