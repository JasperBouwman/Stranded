package com.Stranded;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Scoreboard {

    public static void updateIslandScoreboard(Main p, String island) {

        Files islands = new Files(p, "islands.yml");

        for (String s : islands.getConfig().getStringList("island." + island + ".members")) {

            Player pl = Bukkit.getPlayer(UUID.fromString(s));

            if (pl != null) {
                scores(p, pl);
            }
        }

    }

    private static String rep(String s, Player player, long walk, long pvp, long block, long fly, int health, int serverOnline, int islandOnline, int islandCount,
                              String island, String islandLvl, int maxOnline) {
        return s.replace("%player%", player.getName())
                .replace("%walk%", walk + "")
                .replace("%pvp%", pvp + "")
                .replace("%mining%", block + "")
                .replace("%fly%", fly + "")
                .replace("%health%", health + "")
                .replace("%islandonline%", islandOnline + "")
                .replace("%island%", island)
                .replace("%islandlvl%", islandLvl)
                .replace("%online%", serverOnline + "")
                .replace("%maxonline%", maxOnline + "")
                .replace("%islandcount%", islandCount + "")
                .replace("%world%", player.getWorld().getName());

    }

    public static void scores(Main p, Player player) {

        Files pluginData = new Files(p, "pluginData.yml");
        Files playerData = new Files(p, "playerData.yml");
        Files islands = new Files(p, "islands.yml");

        String uuid = player.getUniqueId().toString();

        if (playerData.getConfig().contains("scoreboard." + uuid)) {
            if (playerData.getConfig().getInt("scoreboard." + uuid) == 0) {
                return;
            }
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();

        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

        long walk = playerData.getConfig().getLong("walk." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
        long block = playerData.getConfig().getLong("BlockBreak." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
        long pvp = playerData.getConfig().getLong("HitKill." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
        long fly = playerData.getConfig().getLong("fly." + uuid) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");
        int health = (int) player.getHealth();
        String island = "n/a";
        int serverOnline = Bukkit.getOnlinePlayers().size();

        String islandLvl = "0";


        int islandOnline = 0;
        int islandCount = 0;

        if (p.getConfig().contains("island." + uuid)) {
            island = p.getConfig().getString("island." + uuid);

            for (String players : p.getConfig().getConfigurationSection("island").getKeys(false)) {
                Player pl = Bukkit.getPlayer(UUID.fromString(players));
                if (p.getConfig().getString("island." + players).equals(island)) {
                    islandCount += 1;
                    if (pl != null) {
                        islandOnline += 1;
                    }
                }
            }
            if (islands.getConfig().contains("island." + island + ".lvl")) {
                islandLvl = islands.getConfig().getString("island." + island + ".lvl");
            }

        }

        int maxOnline = Bukkit.getMaxPlayers();

        String name = "§2§o§nStranded";
        if (pluginData.getConfig().getString("plugin.scoreboard.default.name").length() <= 16) {
            name = pluginData.getConfig().getString("plugin.scoreboard.default.name");
        }

        Objective objective = board.registerNewObjective(rep(name, player, walk, pvp, block, fly, health, serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline), "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        if (!setData(objective, p, player, walk, pvp, block, fly, health, serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline)) {
            player.setScoreboard(board);
        }
    }


    private static boolean setData(Objective objective, Main p, Player player, long walk, long pvp, long block, long fly, int health, int serverOnline, int islandOnline, int islandCount,
                                String island, String islandLvl, int maxOnline) {
        Files pd = new Files(p, "pluginData.yml");

        ArrayList<String> lines = (ArrayList<String>) pd.getConfig().getStringList("plugin.scoreboard.default.lines");

        int loop = 0;
        for (String color : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")) {
            if (lines.size() > loop) {
                try {
                    Score score = objective.getScore("§" + color + "§r" + rep(lines.get(loop), player, walk, pvp, block, fly, health, serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline));
                    if (!lines.get(loop).equals("%null%")) {
                        score.setScore(1);
                    }
                } catch (Exception e) {
                    objective.unregister();

                    ScoreboardManager manager = Bukkit.getScoreboardManager();
                    org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
                    Objective objective1 = board.registerNewObjective("§cStranded", "dummy");
                    objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
                    Score s1 = objective1.getScore("§1§4There went something wrong");
                    Score s2 = objective1.getScore("§2§4contact a dev for help");
                    Score s3 = objective1.getScore("§3§4common errors: too long lines");
                    Score s4 = objective1.getScore("§4§4can happen while having");
                    Score s5 = objective1.getScore("§5§4a long name");
                    s1.setScore(1);
                    s2.setScore(1);
                    s3.setScore(1);
                    s4.setScore(1);
                    s5.setScore(1);
                    player.setScoreboard(board);
                    return true;
                }
            } else {
                return false;
            }
            loop++;
        }
        return false;
    }
}