package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.Chat;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.commands.war.runnables.askServerForWar;
import com.Stranded.commands.war.runnables.island1AcceptIsland2;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class Ready extends CmdManager {
    @Override
    public String getName() {
        return "ready";
    }

    @Override
    public String getAlias() {
        return "r";
    }

    void skipReady1(Main p, String islandName1, Long pendingTime) {
        Files warData = getFiles("warData.yml");

//                ArrayList<String> islandPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");
//                islandPendingList.add(islandName1);
//                warData.getConfig().set("war.pending.islandPendingList", islandPendingList);

//        ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//        memberPendingList.remove(islandName1);
//        warData.getConfig().set("war.pending.memberPendingList", memberPendingList);

        int pendingID = Bukkit.getScheduler().runTaskLater(p, new askServerForWar(islandName1), pendingTime * 20).getTaskId();

        sendWarMessage(islandName1, pendingTime);

//        Bukkit.getScheduler().cancelTask(war.getConfig().getInt("war.pending.memberInvite." + island + ".pendingID"));

        warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", pendingID);

        warData.saveConfig();
    }

    private void memberInvite(Player player) {
        Long pendingTime;

        Files warData = getFiles("warData.yml");
        Files config = getFiles("config.yml");
        try {
            pendingTime = Long.parseLong(warData.getConfig().getString("war.pending.timeIslandWarriors"));
        } catch (NumberFormatException nfe) {
            pendingTime = (long) 10 /*300 (5 min)*/;
        }

        String islandName1 = config.getConfig().getString("island." + player.getUniqueId().toString());

        if (!warData.getConfig().contains("war.pending.island1." + islandName1)) {
            player.sendMessage("Your island isn't pending for a war" + ChatColor.RED);
            return;
        }

        if (warData.getConfig().getBoolean("war.pending.island1." + islandName1 + ".players." + player.getUniqueId().toString())) {
            player.sendMessage("You already accepted" + ChatColor.RED);

        } else {
            warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + player.getUniqueId().toString(), true);
            warData.saveConfig();

            boolean complete = true;
            int onlineIsland1PlayersSize = 1;
            ArrayList<Player> onlineIsland1Players = new ArrayList<>();

            for (String island1Players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                if (!warData.getConfig().getBoolean("war.pending.island1." + islandName1 + ".players." + island1Players)) {
                    complete = false;
                }

                if (!island1Players.equals(player.getUniqueId().toString())) {
                    if (Bukkit.getPlayer(UUID.fromString(island1Players)) != null) {
                        onlineIsland1PlayersSize++;
                        onlineIsland1Players.add(Bukkit.getPlayer(UUID.fromString(island1Players)));
                        Bukkit.getPlayer(UUID.fromString(island1Players)).sendMessage(player.getName() + " has accepted the war inventation");
                    }
                }

            }

            int minPlayers = warData.getConfig().getInt("war.pending.island1." + islandName1 + ".islandMin");

            if (onlineIsland1PlayersSize < minPlayers) {
//                ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//                memberPendingList.remove(islandName1);
//                warData.getConfig().set("war.pending.memberPendingList", memberPendingList);

                //canceled memberPendingList
                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

                warData.saveConfig();
                for (Player island1Player : onlineIsland1Players) {
                    island1Player.sendMessage("War request is canceled, to many people logged off" + ChatColor.RED);
                }

                return;
            }

            if (complete) {

//                ArrayList<String> islandPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");
//                islandPendingList.add(islandName1);
//                warData.getConfig().set("war.pending.islandPendingList", islandPendingList);

//                ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//                memberPendingList.remove(islandName1);
//                warData.getConfig().set("war.pending.memberPendingList", memberPendingList);

                int pendingID = Bukkit.getScheduler().runTaskLater(p, new askServerForWar(islandName1), pendingTime * 20).getTaskId();

                sendWarMessage(islandName1, pendingTime);

                //canceled memberPendingList
                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

                warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", pendingID);

                warData.saveConfig();

//                skipReady1(p, island, pendingTime);

            } else {

                StringBuilder str = new StringBuilder().append("still waiting for:");

                for (String island1Players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                    if (!warData.getConfig().getBoolean("war.pending.island1." + islandName1 + ".players." + island1Players)) {
                        str.append(" ");
                        str.append(PlayerUUID.getPlayerName(island1Players));
                    }
                }

                for (String island1Players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                    if (Bukkit.getPlayer(UUID.fromString(island1Players)) != null) {
                        Bukkit.getPlayer(UUID.fromString(island1Players)).sendMessage(str.toString());
                    }
                }
            }
        }
    }

    void skipReady2(Main p, Player player) {
        Files warData = getFiles("warData.yml");
        Files config = getFiles("config.yml");

        String islandName2 = config.getConfig().getString("island." + player.getUniqueId().toString());

//        ArrayList<String> memberPendingListNewIsland = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
//        memberPendingListNewIsland.remove(islandName2);
//        warData.getConfig().set("war.pending.memberPendingListNewIsland", memberPendingListNewIsland);

        String islandName1 = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

        Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

        if (Bukkit.getPlayer(UUID.fromString(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter"))) != null) {
            Player starter = Bukkit.getPlayer(UUID.fromString(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter")));

            Files pluginData = getFiles("pluginData.yml");
            FancyMessage fm = new FancyMessage();
            fm.addText(islandName2, Colors.AQUA);
            fm.addText(" has accepted your war request, click ", Colors.DARK_AQUA);
            fm.addText("here", Colors.AQUA);
            fm.addCommand("/war accept " + islandName2);
            fm.addHover(new String[]{"/war accept " + islandName2}, new Colors[]{Colors.DARK_AQUA});
            fm.addText(" to go into war. Players who are in ", Colors.DARK_AQUA);
            fm.addText(islandName2, Colors.AQUA);
            fm.addText(" are:", Colors.DARK_AQUA);
            for (String s : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {

                String tmpPlayerName = PlayerUUID.getPlayerName(s);

                fm.addText(" " + tmpPlayerName, Colors.RED);
                Files playerData = getFiles("playerData.yml");
                long walk = playerData.getConfig().getLong("walk." + s) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
                long block = playerData.getConfig().getLong("BlockBreak." + s) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
                long pvp = playerData.getConfig().getLong("HitKill." + s) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
                long fly = playerData.getConfig().getLong("fly." + s) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");
                fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + block, "\nFlying: " + fly, "\nPVP: " + pvp},
                        new Colors[]{Colors.GREEN, Colors.GREEN, Colors.GREEN, Colors.GREEN});
            }
            fm.addText(" you can wait for for another island to accept, but don't wait to long or your war request will expire", Colors.DARK_PURPLE);
            for (String players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                Player pl = Bukkit.getPlayer(UUID.fromString(players));
                if (pl != null) {
                    fm.sendMessage(pl);
                }
            }

            int pendingID = Bukkit.getScheduler().runTaskLater(p, new island1AcceptIsland2(p, islandName2), 10).getTaskId();

            warData.getConfig().set("war.pending.acceptation." + starter.getName() + ".pendingID", pendingID);
            warData.getConfig().set("war.pending.acceptation." + starter.getName() + ".island2", islandName2);
            warData.saveConfig();
        } else {
            ArrayList<String> island1 = new ArrayList<>(warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false));

            while (true) {
                String newRandomStarter = island1.get(new Random().nextInt(island1.size()));
                Player newStarter = Bukkit.getPlayer(UUID.fromString(newRandomStarter));

                if (newStarter == null) {
                    island1.remove(newRandomStarter);
                } else {
//                    newStarter.sendMessage(islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");
                    Files pluginData = getFiles("pluginData.yml");
                    FancyMessage fm = new FancyMessage();
                    fm.addText(islandName2, Colors.AQUA);
                    fm.addText(" has accepted your war request, click ", Colors.DARK_AQUA);
                    fm.addText("here", Colors.AQUA);
                    fm.addCommand("/war accept " + islandName2);
                    fm.addHover(new String[]{"/war accept " + islandName2}, new Colors[]{Colors.DARK_AQUA});
                    fm.addText(" to go into war. Players who are in ", Colors.DARK_AQUA);
                    fm.addText(islandName2, Colors.AQUA);
                    fm.addText(" are:", Colors.DARK_AQUA);
                    for (String s : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {
                        fm.addText(" " + PlayerUUID.getPlayerName(s), Colors.RED);
                        Files playerData = getFiles("playerData.yml");
                        long walk = playerData.getConfig().getLong("walk." + s) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
                        long block = playerData.getConfig().getLong("BlockBreak." + s) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
                        long pvp = playerData.getConfig().getLong("HitKill." + s) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
                        long fly = playerData.getConfig().getLong("fly." + s) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");
                        fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + block, "\nFlying: " + fly, "\nPVP: " + pvp},
                                new Colors[]{Colors.GREEN, Colors.GREEN, Colors.GREEN, Colors.GREEN});
                    }
                    fm.addText("you can wait for for another island to accept, but don't wait to long or your war request will expire", Colors.DARK_PURPLE);
                    for (String players : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
                        Player pl = Bukkit.getPlayer(UUID.fromString(players));
                        if (pl != null) {
                            fm.sendMessage(pl);
                        }
                    }

                    int pendingID = Bukkit.getScheduler().runTaskLater(p, new island1AcceptIsland2(p, islandName2), 10).getTaskId();

                    warData.getConfig().set("war.pending.acceptation." + newRandomStarter + ".pendingID", pendingID);
                    warData.getConfig().set("war.pending.acceptation." + newRandomStarter + ".island2", islandName2);
                    warData.saveConfig();
                    break;
                }
            }
        }

//        startWar(islandName2, p);
    }

    private void memberInviteStartWar(Player player) {

        Files warData = getFiles("warData.yml");
        Files config = getFiles("config.yml");

        String islandName2 = config.getConfig().getString("island." + player.getUniqueId().toString());

        if (!warData.getConfig().contains("war.pending.island2." + islandName2)) {
            player.sendMessage("Your island isn't pending for a war" + ChatColor.RED);
            return;
        }

        if (warData.getConfig().getBoolean("war.pending.island2." + islandName2 + ".players." + player.getUniqueId().toString())) {
            player.sendMessage("You already accepted" + ChatColor.RED);

        } else {
            warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + player.getUniqueId().toString(), true);
            warData.saveConfig();

            boolean complete = true;
            for (String s : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {
                if (!warData.getConfig().getBoolean("war.pending.island2." + islandName2 + ".players." + s)) {
                    complete = false;
                }
            }

            for (String s : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {

                if (!s.equals(player.getUniqueId().toString())) {
                    if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                        Bukkit.getPlayer(UUID.fromString(s)).sendMessage(player.getName() + " has accepted the war inventation");
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

//                ArrayList<String> memberPendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
//                memberPendingList.remove(islandName2);
//                warData.getConfig().set("war.pending.memberPendingListNewIsland", memberPendingList);

                String islandName1 = warData.getConfig().getString("war.pending.island2." + islandName2 + ".island");

                //canceled memberPendingListNewIsland
                Bukkit.getScheduler().cancelTask(warData.getConfig().getInt("war.pending.island1." + islandName1 + ".pendingID"));

                if (Bukkit.getPlayerExact(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter")) != null) {
                    Player starter = Bukkit.getPlayer(UUID.fromString(warData.getConfig().getString("war.pending.island1." + islandName1 + ".starter")));

                    starter.sendMessage(ChatColor.GREEN + islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");

                    int pendingID = Bukkit.getScheduler().runTaskLater(p, new island1AcceptIsland2(p, islandName2), 10).getTaskId();

                    warData.getConfig().set("war.pending.acceptation." + starter.getUniqueId().toString() + ".pendingID", pendingID);
                    warData.getConfig().set("war.pending.acceptation." + starter.getUniqueId().toString() + ".island2", islandName2);
                    warData.saveConfig();
                } else {
                    ArrayList<String> island1 = new ArrayList<>(warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false));
                    while (true) {

                        String newRandomStarter = island1.get(new Random().nextInt(island1.size()));

                        Player newStarter = Bukkit.getPlayer(UUID.fromString(newRandomStarter));

                        if (newStarter == null) {
                            island1.remove(newRandomStarter);
                        } else {
                            newStarter.sendMessage(ChatColor.GREEN + islandName2 + " has accepted your war request, type /war accept " + islandName2 + " to go into war");

                            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new island1AcceptIsland2(p, islandName2), 10);

                            warData.getConfig().set("war.pending.acceptation." + newRandomStarter + ".pendingID", pendingID);
                            warData.getConfig().set("war.pending.acceptation." + newRandomStarter + ".island2", islandName2);
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
                        str.append(PlayerUUID.getPlayerName(island2Players));
                    }
                }

                for (String island2Players : warData.getConfig().getConfigurationSection("war.pending.island2." + islandName2 + ".players").getKeys(false)) {
                    if (Bukkit.getPlayer(UUID.fromString(island2Players)) != null) {
                        Bukkit.getPlayer(UUID.fromString(island2Players)).sendMessage(str.toString());
                    }
                }
            }
        }
    }

    @Override
    public void run(String[] args, Player player) {

        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "You aren't in an island");
            return;
        }
        Files warData = getFiles("warData.yml");
        String ownIslandName = config.getConfig().getString("island." + player.getUniqueId().toString());

        if (warData.getConfig().contains("war.pending.island1." + ownIslandName)) {
            memberInvite(player);
            return;
        }
        if (warData.getConfig().contains("war.pending.island2." + ownIslandName)) {
            memberInviteStartWar(player);
            return;
        }
        player.sendMessage(ChatColor.RED + "Your island isn't asking for a war");
    }

    private void sendWarMessage(String islandName1, long time) {
        Files warData = getFiles("warData.yml");
        Files pluginData = getFiles("pluginData.yml");

        FancyMessage fm = new FancyMessage();

        fm.addText(islandName1 + " want war you have " + time + " seconds to answer\nPlayers:", Colors.BLUE);

        for (String s : warData.getConfig().getConfigurationSection("war.pending.island1." + islandName1 + ".players").getKeys(false)) {
            fm.addText(" " + PlayerUUID.getPlayerName(s), Colors.RED);

            Files playerData = getFiles("playerData.yml");
            long walk = playerData.getConfig().getLong("walk." + s) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
            long block = playerData.getConfig().getLong("BlockBreak." + s) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
            long pvp = playerData.getConfig().getLong("HitKill." + s) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
            long fly = playerData.getConfig().getLong("fly." + s) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");

            fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + block, "\nFlying: " + fly, "\nPVP: " + pvp},
                    new Colors[]{Colors.GREEN, Colors.GREEN, Colors.GREEN, Colors.GREEN});

        }
        for (Player pl : Bukkit.getOnlinePlayers()) {
            fm.sendMessage(pl);
        }
    }
}
