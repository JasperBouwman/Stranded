package com.Stranded;

import com.Stranded.worldGeneration.DefaultIslandGenerator;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

class Scoreboard {

    private static String rep(String s, Player online, long walk, long pvp, long block, long fly, int health, int serverOnline, ArrayList<String> islandOnline, ArrayList<String> islandCount,
                              String island, String islandLvl, int maxOnline, int ping) {
        return s.replaceAll("%player%", online.getName())
                .replaceAll("%walk%", walk + "")
                .replaceAll("%pvp%", pvp + "")
                .replaceAll("%mining%", block + "")
                .replaceAll("%fly%", fly + "")
                .replaceAll("%health%", health + "")
                .replaceAll("%islandonline%", islandOnline.size() + "")
                .replaceAll("%island%", island)
                .replaceAll("%islandlvl%", islandLvl)
                .replaceAll("%online%", serverOnline + "")
                .replaceAll("%maxonline%", maxOnline + "")
                .replaceAll("%islandcount%", islandCount.size() + "")
                .replaceAll("%world%", online.getWorld().getName())
                .replaceAll("%ping%", ping + "");

    }

    private static void setTabList(Player player, Main p) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a("{\"text\":\"hei fggt\"}");
        IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a("{\"text\":\"lol\"}");
        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, header);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            b.set(packet, footer);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);



    }

    static void scores(Main p) {


        if (Bukkit.getWorld("Islands") != null) {
            Files islands = new Files(p, "islands.yml");
            if (islands.getConfig().contains("islandData.islandTypesCopied")) {
                if (!islands.getConfig().getBoolean("islandData.islandTypesCopied")) {
                    Bukkit.broadcastMessage("the default islands are now loading in the server");
                    new DefaultIslandGenerator(p);
                }
            } else {
                Bukkit.broadcastMessage("the default islands are now loading in the server 2");
                new DefaultIslandGenerator(p);
            }
        }


        Files pd = new Files(p, "pluginData.yml");

        for (Player online : Bukkit.getOnlinePlayers()) {

            Files save = new Files(p, "playerData.yml");
            Files pluginData = new Files(p, "pluginData.yml");

            if (save.getConfig().contains("scoreboard." + online.getName())) {
                if (save.getConfig().getInt("scoreboard." + online.getName()) == 0) {
                    continue;
                } else if (save.getConfig().getInt("scoreboard." + online.getName()) > 1) {

                    if (save.getConfig().getInt("scoreboard." + online.getName()) == 24) {
                        save.getConfig().set("scoreboard." + online.getName(), 0);
                        save.saveConfig();

                        com.Stranded.commands.island.Scoreboard.removeScoreboard(online);
                        continue;
                    } else {
                        save.getConfig().set("scoreboard." + online.getName(), save.getConfig().getInt("scoreboard." + online.getName()) + 1);
                        save.saveConfig();
                    }
                }
            }

            ScoreboardManager manager = Bukkit.getScoreboardManager();

            org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();

            String name = "§2§o§nStranded";

            if (pd.getConfig().getString("plugin.scoreboard.default.name").length() <= 16) {
                name = pd.getConfig().getString("plugin.scoreboard.default.name");
            }

            Objective objective = board.registerNewObjective(name, "dummy");

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            long walk = save.getConfig().getLong("walk." + online.getName()) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
            long block = save.getConfig().getLong("BlockBreak." + online.getName()) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
            long pvp = save.getConfig().getLong("HitKill." + online.getName()) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
            long fly = save.getConfig().getLong("fly." + online.getName()) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");
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

            int ping = 0;

            try {String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

                Object nmsPlayer = online.getClass().getMethod("getHandle").invoke(online);
                Object pingObject = nmsPlayer.getClass().getField("ping").get(nmsPlayer);
                ping = (int) pingObject;
            } catch (Exception e) {
                e.printStackTrace();
            }

            int maxOnline = Bukkit.getMaxPlayers();

            String line1 = rep(pd.getConfig().getString("plugin.scoreboard.default.line1"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line2 = rep(pd.getConfig().getString("plugin.scoreboard.default.line2"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line3 = rep(pd.getConfig().getString("plugin.scoreboard.default.line3"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line4 = rep(pd.getConfig().getString("plugin.scoreboard.default.line4"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line5 = rep(pd.getConfig().getString("plugin.scoreboard.default.line5"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line6 = rep(pd.getConfig().getString("plugin.scoreboard.default.line6"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line7 = rep(pd.getConfig().getString("plugin.scoreboard.default.line7"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line8 = rep(pd.getConfig().getString("plugin.scoreboard.default.line8"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line9 = rep(pd.getConfig().getString("plugin.scoreboard.default.line9"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line10 = rep(pd.getConfig().getString("plugin.scoreboard.default.line10"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line11 = rep(pd.getConfig().getString("plugin.scoreboard.default.line11"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line12 = rep(pd.getConfig().getString("plugin.scoreboard.default.line12"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line13 = rep(pd.getConfig().getString("plugin.scoreboard.default.line13"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line14 = rep(pd.getConfig().getString("plugin.scoreboard.default.line14"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);
            String line15 = rep(pd.getConfig().getString("plugin.scoreboard.default.line15"), online, walk, pvp, block, fly, health,
                    serverOnline, islandOnline, islandCount, island, islandLvl, maxOnline, ping);

            setData(objective, line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12, line13, line14, line15);

            online.setScoreboard(board);
            setTabList(online, p);
        }
    }

    private static void setData(Objective objective, String line1, String line2, String line3, String line4,
                                String line5, String line6, String line7, String line8, String line9, String line10,
                                String line11, String line12, String line13, String line14, String line15) {

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
    }

}