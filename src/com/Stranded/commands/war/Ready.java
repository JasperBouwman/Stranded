package com.Stranded.commands.war;

import com.Stranded.FancyMessageUtil;
import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.war.runnables.IslandPendingList;
import com.Stranded.commands.war.runnables.IslandPendingListStartWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Ready extends CmdManager implements Runnable {
    @Override
    public String getName() {
        return "ready";
    }

    @Override
    public String getAlias() {
        return "r";
    }

    void skipReady1(Main p, String island, Long pendingTime) {
        Files warData = new Files(p, "warData.yml");

        ArrayList<String> islandPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");
        islandPendingList.add(island);
        warData.getConfig().set("war.pending.islandPendingList", islandPendingList);

        ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
        memberPendingList.remove(island);
        warData.getConfig().set("war.pending.memberPendingList", memberPendingList);

        int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new IslandPendingList(p), pendingTime * 20);

        sendWarMessage(island, p, pendingTime);

//        Bukkit.getScheduler().cancelTask(war.getConfig().getInt("war.pending.memberInvite." + island + ".pendingID"));

        warData.getConfig().set("war.pending.island1." + island + ".pendingID", pendingID);

        warData.saveConfig();
    }

    private void memberInvite(Player player) {
        Long pendingTime;

        Files warData = new Files(p, "warData.yml");
        try {
            pendingTime = Long.parseLong(warData.getConfig().getString("war.pending.timeIslandWarriors"));
        } catch (NumberFormatException nfe) {
            pendingTime = (long) 10 /*300 (5 min)*/;
        }

        String islandName1 = p.getConfig().getString("island." + player.getName());

        if (!warData.getConfig().contains("war.pending.island1." + islandName1)) {
            player.sendMessage("your island isn't pending for a war");
            return;
        }

        if (warData.getConfig().getBoolean("war.pending.island1." + islandName1 + ".players." + player.getName())) {
            player.sendMessage("you already accepted");

        } else {
            warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + player.getName(), true);
            warData.saveConfig();

            boolean complete = true;
            int onlineIsland1PlayersSize = 1;
            ArrayList<Player> onlineIsland1Players = new ArrayList<>();

            for (String island1Players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                if (!warData.getConfig().getBoolean("war.pending.island1." + islandName1 + ".players." + island1Players)) {
                    complete = false;
                }

                if (!island1Players.equals(player.getName())) {
                    if (Bukkit.getPlayerExact(island1Players) != null) {
                        onlineIsland1PlayersSize++;
                        onlineIsland1Players.add(Bukkit.getPlayerExact(island1Players));
                        Bukkit.getPlayerExact(island1Players).sendMessage(player.getName() + " has accepted the war inventation");
                    }
                }

            }

            int minPlayers = warData.getConfig().getInt("war.pending.island1." + islandName1 + ".islandMin");

            if (onlineIsland1PlayersSize < minPlayers) {
                ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
                memberPendingList.remove(islandName1);
                warData.getConfig().set("war.pending.memberPendingList", memberPendingList);

                for (Player island1Player : onlineIsland1Players) {
                    island1Player.sendMessage("war request is canceled, to many people logged off");
                }

                return;
            }

            if (complete) {

                ArrayList<String> islandPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");
                islandPendingList.add(islandName1);
                warData.getConfig().set("war.pending.islandPendingList", islandPendingList);

                ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
                memberPendingList.remove(islandName1);
                warData.getConfig().set("war.pending.memberPendingList", memberPendingList);

                int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, pendingTime * 20);

                sendWarMessage(islandName1, p, pendingTime);

                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

                warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", pendingID);

                warData.saveConfig();

//                skipReady1(p, island, pendingTime);

            } else {

                StringBuilder str = new StringBuilder().append("still waiting for:");

                for (String island1Players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                    if (!warData.getConfig().getBoolean("war.pending.island1." + islandName1 + ".players." + island1Players)) {
                        str.append(" ");
                        str.append(island1Players);
                    }
                }

                for (String island1Players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                    if (Bukkit.getPlayerExact(island1Players) != null) {
                        Bukkit.getPlayerExact(island1Players).sendMessage(str.toString());
                    }
                }
            }
        }
    }

    void skipReady2(Main p, Player player) {
        Files warData = new Files(p, "warData.yml");

        String islandName2 = p.getConfig().getString("island." + player.getName());

        ArrayList<String> memberPendingListNewIsland = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
        memberPendingListNewIsland.remove(islandName2);
        warData.getConfig().set("war.pending.memberPendingListNewIsland", memberPendingListNewIsland);

        String islandName1 = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

        if (Bukkit.getPlayerExact(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter")) != null) {
            Player starter = Bukkit.getPlayerExact(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter"));
            //todo make this beauty
            starter.sendMessage(islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");

            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new IslandPendingListStartWar(p), 10);

            warData.getConfig().set("war.pending.acceptation." + starter.getName() + ".pendingID", pendingID);
            warData.getConfig().set("war.pending.acceptation." + starter.getName() + ".island2", islandName2);
            warData.saveConfig();
        } else {
            ArrayList<String> island1 = new ArrayList<>();
            island1.addAll(warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false));
            while (true) {
                String newRandomStarter = island1.get(new Random().nextInt(island1.size()));
                Player newStarter = Bukkit.getPlayerExact(newRandomStarter);

                if (newStarter == null) {
                    island1.remove(newRandomStarter);
                } else {
                    //todo make this beauty
                    newStarter.sendMessage(islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");

                    int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new IslandPendingListStartWar(p), 10);

                    warData.getConfig().set("war.pending.acceptation." + newStarter.getName() + ".pendingID", pendingID);
                    warData.getConfig().set("war.pending.acceptation." + newStarter.getName() + ".island2", islandName2);
                    warData.saveConfig();
                    break;
                }
            }
        }

//        startWar(islandName2, p);
    }

    private void memberInviteStartWar(Player player) {

        Files warData = new Files(p, "warData.yml");

        String islandName2 = p.getConfig().getString("island." + player.getName());

        if (!warData.getConfig().contains("war.pending.island2." + islandName2)) {
            player.sendMessage("your island isn't pending for a war");
            return;
        }

        if (warData.getConfig().getBoolean("war.pending.island2." + islandName2 + ".players." + player.getName())) {
            player.sendMessage("you already accepted");

        } else {
            warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + player.getName(), true);
            warData.saveConfig();

            boolean complete = true;
            for (String s : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {
                if (!warData.getConfig().getBoolean("war.pending.island2." + islandName2 + ".players." + s)) {
                    complete = false;
                }
            }

            for (String s : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {

                if (!s.equals(player.getName())) {
                    if (Bukkit.getPlayerExact(s) != null) {
                        Bukkit.getPlayerExact(s).sendMessage(player.getName() + " has accepted the war inventation");
//                        if (complete) {
//                            Bukkit.getPlayerExact(s).sendMessage("everybody has accepted, war request is now sending to the other islands");
//                        }
                    }
                }
                /*else if (s.equals(player.getName())) {
                    if (complete) {
                        Bukkit.getPlayerExact(s).sendMessage("everybody has accepted, war request is now sending to the other islands");
                    }
                }*/
            }

            if (complete) {

                ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");
                memberPendingList.remove(islandName2);
                warData.getConfig().set("war.pending.islandPendingList", memberPendingList);

                String otherIsland = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.islandInvite." + otherIsland + ".pendingID"));

                String islandName1 = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

                if (Bukkit.getPlayerExact(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter")) != null) {
                    Player starter = Bukkit.getPlayerExact(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter"));

                    starter.sendMessage(islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");

                    int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new IslandPendingListStartWar(p), 10);

                    warData.getConfig().set("war.pending.acceptation." + starter.getName() + ".pendingID", pendingID);
                    warData.getConfig().set("war.pending.acceptation." + starter.getName() + ".island2", islandName2);
                    warData.saveConfig();
                } else {
                    ArrayList<String> island1 = new ArrayList<>();
                    island1.addAll(warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false));
                    while (true) {

                        String newRandomStarter = island1.get(new Random().nextInt(island1.size()));

                        Player newStarter = Bukkit.getPlayerExact(newRandomStarter);

                        if (newStarter == null) {
                            island1.remove(newRandomStarter);
                        } else {
                            newStarter.sendMessage(islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");

                            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new IslandPendingListStartWar(p), 10);

                            warData.getConfig().set("war.pending.acceptation." + newStarter.getName() + ".pendingID", pendingID);
                            warData.getConfig().set("war.pending.acceptation." + newStarter.getName() + ".island2", islandName2);
                            warData.saveConfig();
                            break;
                        }
                    }
                }

//                startWar(islandName2, p);

            } else {

                StringBuilder str = new StringBuilder().append("still waiting for:");

                for (String island2Players : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {
                    if (!warData.getConfig().getBoolean("war.pending.island2." + islandName2 + ".players." + island2Players)) {
                        str.append(" ");
                        str.append(island2Players);
                    }
                }

                for (String island2Players : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {
                    if (Bukkit.getPlayerExact(island2Players) != null) {
                        Bukkit.getPlayerExact(island2Players).sendMessage(str.toString());
                    }
                }
            }
        }
    }

    @Override
    public void run(String[] args, Player player) {

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }
        Files warData = new Files(p, "warData.yml");
        String ownIslandName = p.getConfig().getString("island." + player.getName());

        if (warData.getConfig().contains("war.pending.island1." + ownIslandName)) {
            memberInvite(player);
            return;
        }
        if (warData.getConfig().contains("war.pending.island2." + ownIslandName)) {
            memberInviteStartWar(player);
            return;
        }
        player.sendMessage("your island isn't asking for a war");
    }

    private void sendWarMessage(String island, Main p, long time) {
        Files war = new Files(p, "warData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        FancyMessageUtil fm = new FancyMessageUtil();

        fm.addText(island + " want war you have " + time + " seconds to answer\nPlayers:", FancyMessageUtil.Colors.BLUE);

        for (String s : war.getConfig().getConfigurationSection("war.pending.memberInvite." + island + ".players").getKeys(false)) {
            fm.addText(" " + s, FancyMessageUtil.Colors.RED);


            Files playerData = new Files(p, "playerData.yml");
            long walk = playerData.getConfig().getLong("walk." + s) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
            long block = playerData.getConfig().getLong("BlockBreak." + s) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
            long pvp = playerData.getConfig().getLong("HitKill." + s) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
            long fly = playerData.getConfig().getLong("fly." + s) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");

            fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + block, "\nFlying: " + fly, "\nPVP: " + pvp},
                    new FancyMessageUtil.Colors[]{FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN});

        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            fm.sendMessage(pl);
        }
    }

    @Override
    public void run() {

        Files warData = new Files(p, "warData.yml");

        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");

        if (list.size() < 1) {
            return;
        }
        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.islandInvite." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayerExact(players) != null) {
                Bukkit.getPlayerExact(players).sendMessage("no other island reacted in time");
            }
        }
        warData.getConfig().set("war.pending.islandInvite." + island, null);

        list.remove(island);
        warData.getConfig().set("war.pending.islandPendingList", list);

        warData.saveConfig();
    }
}
