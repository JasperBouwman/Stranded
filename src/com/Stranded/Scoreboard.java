package com.Stranded;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;

public class Scoreboard {

    private static String rep(String s, Player online, int walk, int pvp, int block, int health, int serverOnline, ArrayList<String> islandOnline, ArrayList<String> islandCount,
                              String island, String islandLvl, int maxOnline) {
        return s.replaceAll("%player%", online.getName())
                .replaceAll("%walk%", walk + "")
                .replaceAll("%pvp%", pvp + "")
                .replaceAll("%mining%", block + "")
                .replaceAll("%health%", health + "")
                .replaceAll("%islandonline%", islandOnline.size() + "")
                .replaceAll("%island%", island)
                .replaceAll("%islandlvl%", islandLvl)
                .replaceAll("%online%", serverOnline + "")
                .replaceAll("%maxonline%", maxOnline + "")
                .replaceAll("%islandcount%", islandCount.size() + "");

    }

    public static void scores(Main p) {

        Files pd = new Files(p, "pluginData.yml");

        for (Player online : Bukkit.getOnlinePlayers()) {

            Files save = new Files(p, "playerData.yml");
            Files pluginData = new Files(p, "pluginData.yml");

            ScoreboardManager manager = Bukkit.getScoreboardManager();

            org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

            String name = "§2§o§nStranded";

            if (pd.getConfig().getString("plugin.scoreboard.default.name").length() <= 16) {
                name = pd.getConfig().getString("plugin.scoreboard.default.name");
            }

            Objective objective = board.registerNewObjective(name, "dummy");

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            int walk = save.getConfig().getInt("walk." + online.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier");
            int block = save.getConfig().getInt("BlockBreak." + online.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier");
            int pvp = save.getConfig().getInt("HitKill." + online.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier");
            int health = (int) online.getHealth();
            String island = "n/a";
            int serverOnline = Bukkit.getOnlinePlayers().size();

            String islandLvl = "0";

            Files isf = new Files(p, "islands.yml");

            ArrayList<String> islandOnline = new ArrayList<>();
            ArrayList<String> islandCount = new ArrayList<>();

            if (p.getConfig().contains("island." + online.getName())) {
                island = p.getConfig().getString("island." + online.getName());

                islandOnline.add(online.getName());
                for (String player : p.getConfig().getConfigurationSection("island").getKeys(false)) {
                    Player pl = Bukkit.getPlayerExact(player);
                    if (pl != null) {
                        if (p.getConfig().getString("island." + pl.getName()).equals(island) && !pl.getName().equals(online.getName())) {
                            islandOnline.add(player);
                        }
                    }
                    if (p.getConfig().getString("island." + player).equals(island)) {
                        islandCount.add(player);
                    }
                }
                if (isf.getConfig().contains("island." + island + ".lvl")) {
                    islandLvl = isf.getConfig().getString("island." + island + ".lvl");
                }


            }

            int maxOnline = Bukkit.getMaxPlayers();

            String line1 = rep(pd.getConfig().getString("plugin.scoreboard.default.line1"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line2 = rep(pd.getConfig().getString("plugin.scoreboard.default.line2"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line3 = rep(pd.getConfig().getString("plugin.scoreboard.default.line3"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line4 = rep(pd.getConfig().getString("plugin.scoreboard.default.line4"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line5 = rep(pd.getConfig().getString("plugin.scoreboard.default.line5"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line6 = rep(pd.getConfig().getString("plugin.scoreboard.default.line6"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line7 = rep(pd.getConfig().getString("plugin.scoreboard.default.line7"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line8 = rep(pd.getConfig().getString("plugin.scoreboard.default.line8"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line9 = rep(pd.getConfig().getString("plugin.scoreboard.default.line9"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line10 = rep(pd.getConfig().getString("plugin.scoreboard.default.line10"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line11 = rep(pd.getConfig().getString("plugin.scoreboard.default.line11"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line12 = rep(pd.getConfig().getString("plugin.scoreboard.default.line12"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line13 = rep(pd.getConfig().getString("plugin.scoreboard.default.line13"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line14 = rep(pd.getConfig().getString("plugin.scoreboard.default.line14"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);
            String line15 = rep(pd.getConfig().getString("plugin.scoreboard.default.line15"), online, walk, pvp, block, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline);

            try {
                Score score1 = objective.getScore("§1§r" + line1);
                Score score2 = objective.getScore("§2§r" + line2);
                Score score3 = objective.getScore("§3§r" + line3);
                Score score4 = objective.getScore("§4§r" + line4);
                Score score5 = objective.getScore("§5§r" + line5);
                Score score6 = objective.getScore("§6§r" + line6);
                Score score7 = objective.getScore("§7§r" + line7);
                Score score8 = objective.getScore("§8§r" + line8);
                Score score9 = objective.getScore("§9§r" + line9);
                Score score10 = objective.getScore("§a§r" + line10);
                Score score11 = objective.getScore("§b§r" + line11);
                Score score12 = objective.getScore("§c§r" + line12);
                Score score13 = objective.getScore("§d§r" + line13);
                Score score14 = objective.getScore("§e§r" + line14);
                Score score15 = objective.getScore("§f§r" + line15);

                if (!line1.equals("%null%")) {
                    score1.setScore(1);
                }
                if (!line2.equals("%null%")) {
                    score2.setScore(1);
                }
                if (!line3.equals("%null%")) {
                    score3.setScore(1);
                }
                if (!line4.equals("%null%")) {
                    score4.setScore(1);
                }
                if (!line5.equals("%null%")) {
                    score5.setScore(1);
                }
                if (!line6.equals("%null%")) {
                    score6.setScore(1);
                }
                if (!line7.equals("%null%")) {
                    score7.setScore(1);
                }
                if (!line8.equals("%null%")) {
                    score8.setScore(1);
                }
                if (!line9.equals("%null%")) {
                    score9.setScore(1);
                }
                if (!line10.equals("%null%")) {
                    score10.setScore(1);
                }
                if (!line11.equals("%null%")) {
                    score11.setScore(1);
                }
                if (!line12.equals("%null%")) {
                    score12.setScore(1);
                }
                if (!line13.equals("%null%")) {
                    score13.setScore(1);
                }
                if (!line14.equals("%null%")) {
                    score14.setScore(1);
                }
                if (!line15.equals("%null%")) {
                    score15.setScore(1);
                }
            } catch (IllegalArgumentException iae) {
                Score s1 = objective.getScore("§1§4There went something wrong");
                Score s2 = objective.getScore("§2§4contact a dev for help");
                Score s3 = objective.getScore("§3§4common errors: too long lines");
                Score s4 = objective.getScore("§4§4can happen while having");
                Score s5 = objective.getScore("§5§4a to long name");
                s1.setScore(1);
                s2.setScore(1);
                s3.setScore(1);
                s4.setScore(1);
                s5.setScore(1);
            }
            online.setScoreboard(board);
        }
    }
}
