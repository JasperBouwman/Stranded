package com.Stranded.commands.war;

import com.Stranded.FancyMessageUtil;
import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.war.runnables.MemberPendingList;
import com.Stranded.commands.war.util.WarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Start extends CmdManager implements Runnable {
    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getAlias() {
        return "st";
    }

    private void sendWarMessage(Player player, String theme, ArrayList<String> tempUsers, long pendingTime) {
        FancyMessageUtil fm = new FancyMessageUtil();

        fm.addText(player.getName() + " has started a war in the theme " + theme + " they have to answer in " + pendingTime + " seconds to answer,", FancyMessageUtil.Colors.AQUA);
        fm.addText("these players will join this battle:\n", FancyMessageUtil.Colors.AQUA);

        Files playerData = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        for (String user : tempUsers) {
            fm.addText(user + " ", FancyMessageUtil.Colors.RED);

            long walk = playerData.getConfig().getLong("walk." + user) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
            long block = playerData.getConfig().getLong("BlockBreak." + user) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
            long pvp = playerData.getConfig().getLong("HitKill." + user) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
            long fly = playerData.getConfig().getLong("fly." + user) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");

            fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + block, "\nFlying: " + fly, "\nPVP: " + pvp}, new FancyMessageUtil.Colors[]{FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN, FancyMessageUtil.Colors.GREEN});
        }

        fm.addText("\nclick ", FancyMessageUtil.Colors.AQUA);
        fm.addText("here", FancyMessageUtil.Colors.DARK_AQUA);
        fm.addHover(new String[]{"/war ready"}, new FancyMessageUtil.Colors[]{FancyMessageUtil.Colors.RED});
        fm.addCommand("/war ready");
        fm.addText(" to say your'e ready", FancyMessageUtil.Colors.AQUA);

        for (String user : tempUsers) {
            fm.sendMessage(Bukkit.getPlayerExact(user));
        }
    }

    @Override
    public void run(String[] args, Player player) {

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }

        Long pendingTime;

        Files warData = new Files(p, "warData.yml");
        Files warIslands = new Files(p, "warIslands.yml");
        Files islands = new Files(p, "islands.yml");

        try {
            pendingTime = Long.parseLong(warData.getConfig().getString("war.pending.timeIslandWarriors"));
        } catch (NumberFormatException nfe) {
            pendingTime = (long) 10;
        }

        String islandName1 = p.getConfig().getString("island." + player.getName());

        if (WarUtil.testIfIsInWarWithComments(p, player))
            return;

        //war start <war theme> [playerNames]
        if ((args.length == 3 && !args[1].equalsIgnoreCase(player.getName())) || args.length > 3) {


            if (!warIslands.getConfig().contains("warIslands.island." + args[1])) {
                player.sendMessage("there is no theme found");
                return;
            }

            ArrayList<String> island1MemberList = (ArrayList<String>) islands.getConfig().getStringList("island." + islandName1 + ".members");

            ArrayList<String> tempUsers = new ArrayList<>();

            boolean br = false;
            for (int i = 2; i < args.length; i++) {
                if (!tempUsers.contains(args[i])) {
                    tempUsers.add(args[i]);

                    if (!island1MemberList.contains(args[i])) {
                        player.sendMessage(args[i] + " is not a member in your island");
                        br = true;
                    } else if (Bukkit.getPlayerExact(args[i]) == null) {
                        player.sendMessage(args[i] + " is not online");
                        br = true;
                    }
                } else {
                    player.sendMessage(args[i] + " is already mentioned");
                    br = true;
                }
            }
            if (br) return;

            if (!tempUsers.contains(player.getName())) {
                tempUsers.add(player.getName());
            }

            ArrayList<String> islandIDList = new ArrayList<>();

            int totalMin = -1;
            int totalMax = 1;
            boolean initialize = true;

            for (String islandID : warIslands.getConfig().getConfigurationSection("warIslands.island." + args[1]).getKeys(false)) {

                int min = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + islandID + ".minPlayers");
                int max = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + islandID + ".maxPlayers");

                if (max >= tempUsers.size() && min <= tempUsers.size() && !warIslands.getConfig().getBoolean("warIslands.island." + args[1] + "." + islandID + ".inUse")) {
                    br = true;
                    islandIDList.add(islandID);

                    if (totalMin == -1 && initialize) {
                        totalMax = max;
                        totalMin = min;
                        initialize = false;
                    }

                    if (totalMin > min) {
                        totalMin = min;
                    }
                    if (totalMax < max) {
                        totalMax = max;
                    }
                }
            }
            if (!br) {
                player.sendMessage("there is no island in this theme found that you will fit in, choose a different theme");
                return;
            }

            player.sendMessage("all the people have now to accept this and the war will be send to the server");

            ArrayList<String> pendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
            pendingList.add(islandName1);
            warData.getConfig().set("war.pending.memberPendingList", pendingList);

//            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, pendingTime * 20);
            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new MemberPendingList(p), pendingTime * 20);

            warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", pendingID);
            for (String user : tempUsers) {
                if (user.equals(player.getName())) {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, false);
                }
            }
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandList", islandIDList);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMax", totalMax);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMin", totalMin);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".theme", args[1]);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".starter", player.getName());
            warData.saveConfig();

            sendWarMessage(player, args[1], tempUsers, pendingTime);

        } else if ((args.length == 3 && args[1].equalsIgnoreCase(player.getName())) || args.length == 2) {

            if (!warIslands.getConfig().contains("warIslands.island." + args[1])) {
                player.sendMessage("there is no theme found");
                return;
            }

            ArrayList<String> tempUsers = new ArrayList<>();

            if (!tempUsers.contains(player.getName())) {
                tempUsers.add(player.getName());
            }

            ArrayList<String> islandIDList = new ArrayList<>();

            int totalMin = -1;
            int totalMax = 1;
            boolean initialize = true;
            boolean br = false;

            for (String id : warIslands.getConfig().getConfigurationSection("warIslands.island." + args[1]).getKeys(false)) {

                int min = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + id + ".minPlayers");
                int max = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + id + ".maxPlayers");

                if (max >= tempUsers.size() &&
                        min <= tempUsers.size() && !warIslands.getConfig().getBoolean("warIslands.island." + args[1] + "." + id + ".inUse")) {
                    br = true;
                    islandIDList.add(id);

                    if (totalMin == -1 && initialize) {
                        totalMax = max;
                        totalMin = min;
                        initialize = false;
                    }

                    if (totalMin > min) {
                        totalMin = min;
                    }
                    if (totalMax < max) {
                        totalMax = max;
                    }
                }
            }
            if (!br) {
                player.sendMessage("there is no island in this theme found that you will fit in, choose a different theme");
                return;
            }

//            ArrayList<String> pendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//            pendingList.add(island);
//            warData.getConfig().set("war.pending.memberPendingList", pendingList);

            warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", "nil");
            for (String user : tempUsers) {
                if (user.equals(player.getName())) {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, false);
                }
            }
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandList", islandIDList);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMax", totalMax);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMin", totalMin);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".theme", args[1]);
            warData.saveConfig();

            new Ready().skipReady1(p, islandName1, pendingTime);

        } else {
            player.sendMessage("wrong use");
        }
    }

    @Override
    public void run() {

        Files warData = new Files(p, "warData.yml");

        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");

        if (list.size() < 1) {
            return;
        }
        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.island1." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayerExact(players) != null) {
                Bukkit.getPlayerExact(players).sendMessage("not everybody accepted this war invite in time");
            }
        }
        warData.getConfig().set("war.pending.island1." + island, null);

        list.remove(island);
        warData.getConfig().set("war.pending.memberPendingList", list);

        warData.saveConfig();
    }
}
