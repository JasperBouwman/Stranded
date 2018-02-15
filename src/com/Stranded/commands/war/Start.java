package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.war.runnables.island1askPlayers;
import com.Stranded.commands.war.util.WarUtil;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;
import static com.Stranded.GettingFiles.getFiles;

public class Start extends CmdManager {

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getAlias() {
        return "st";
    }

    private void sendWarMessage(Player player, String theme, ArrayList<String> tempUsers, long pendingTime) {
        FancyMessage fm = new FancyMessage();

        fm.addText(player.getName() + " has started a war in the theme " + theme + " they have to answer in " + pendingTime + " seconds to answer,", Colors.AQUA);
        fm.addText("these players will join this battle:\n", Colors.AQUA);

        Files playerData = getFiles("playerData.yml");
        Files pluginData = getFiles("pluginData.yml");

        for (String user : tempUsers) {
            fm.addText(PlayerUUID.getPlayerName(user) + " ", Colors.RED);

            long walk = playerData.getConfig().getLong("walk." + user) / pluginData.getConfig().getLong("plugin.scoreboard.walking.amplifier");
            long block = playerData.getConfig().getLong("BlockBreak." + user) / pluginData.getConfig().getLong("plugin.scoreboard.mining.amplifier");
            long pvp = playerData.getConfig().getLong("HitKill." + user) / pluginData.getConfig().getLong("plugin.scoreboard.pvp.amplifier");
            long fly = playerData.getConfig().getLong("fly." + user) / pluginData.getConfig().getLong("plugin.scoreboard.flying.amplifier");

            fm.addHover(new String[]{"Walking: " + walk, "\nMining: " + block, "\nFlying: " + fly, "\nPVP: " + pvp},
                    new Colors[]{Colors.GREEN, Colors.GREEN, Colors.GREEN, Colors.GREEN});
        }

        fm.addText("\nclick ", Colors.AQUA);
        fm.addText("here", Colors.DARK_AQUA);
        fm.addHover(new String[]{"/war ready"}, new Colors[]{Colors.RED});
        fm.addCommand("/war ready");
        fm.addText(" to say your'e ready", Colors.AQUA);

        for (String user : tempUsers) {
            fm.sendMessage(Bukkit.getPlayerExact(user));
        }
    }

    @Override
    public void run(String[] args, Player player) {

        //war start <war theme> [playerNames...]

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage("you aren't in an island");
            return;
        }

        Files warData = getFiles("warData.yml");
        Files warIslands = getFiles("warIslands.yml");
        Files islands = getFiles("islands.yml");

        Set<String> set0 = warIslands.getConfig().getConfigurationSection("warIslands.island").getKeys(false);
        if (set0.size() == 0) {
            player.sendMessage("there there are no themes found, please report this to the server admins");
            return;
        }

        long pendingTime = 30;

        String islandName1 = config.getConfig().getString("island." + uuid);

        if (WarUtil.testIfIsInWarWithComments(player))
            return;

        if ((args.length == 3 && !args[2].equalsIgnoreCase(player.getName())) || args.length > 3) {

            if (!args[1].equalsIgnoreCase("random")) {
                if (!warIslands.getConfig().contains("warIslands.island." + args[1])) {
                    player.sendMessage("there is no theme found with the theme name " + args[1]);
                    return;
                }
                Set<String> set = warIslands.getConfig().getConfigurationSection("warIslands.island." + args[1]).getKeys(false);

                if (set.size() == 0) {
                    player.sendMessage("there is no theme found with the theme name " + args[1]);
                    return;
                }
            }

            ArrayList<String> island1MemberList = (ArrayList<String>) islands.getConfig().getStringList("island." + islandName1 + ".members");

            ArrayList<String> tempUsers = new ArrayList<>();

            boolean br = false;
            for (int i = 2; i < args.length; i++) {


                //get player uuid
                String playerUUID = PlayerUUID.getPlayerUUID(args[i]);
                //test if player is found
                if (playerUUID == null) {
                    ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[i]);
                    if (tempPlayers.size() > 1) {
                        //more players found with name
                        player.sendMessage("there are more players found with the " + args[i]);
                        br = true;
                    } else if (tempPlayers.size() == 0) {
                        //no players found with name
                        player.sendMessage("there is no player found with the name " + args[i]);
                        br = true;
                    } else {
                        //set full on player uuid
                        playerUUID = tempPlayers.get(0);
                    }
                }
                //test if player is found (not really necessary)
                if (playerUUID == null) {
                    return;
                }
                //get final player name (case sensitive)
                String playerName = PlayerUUID.getPlayerName(playerUUID);
                //get player
                Player tmpPlayer = PlayerUUID.getPlayerExact(playerName);

                if (!tempUsers.contains(playerUUID)) {
                    if (!island1MemberList.contains(playerUUID)) {
                        player.sendMessage(playerName + " is not a member in your island");
                        br = true;
                    } else if (tmpPlayer == null) {
                        player.sendMessage(playerName + " is not online");
                        br = true;
                    } else {
                        tempUsers.add(playerUUID);
                    }
                } else {
                    player.sendMessage(playerName + " is already mentioned" + ChatColor.RED);
                    br = true;
                }
            }
            if (br) return;

            if (!tempUsers.contains(uuid)) {
                tempUsers.add(uuid);
            }

            ArrayList<String> islandIDList = new ArrayList<>();

            int totalMin = -1;
            int totalMax = 1;
            boolean initialize = true;

            if (args[1].equalsIgnoreCase("random")) {
                for (String newTheme : warIslands.getConfig().getConfigurationSection("warIslands.island").getKeys(false)) {
                    for (String islandID : warIslands.getConfig().getConfigurationSection("warIslands.island." + newTheme).getKeys(false)) {
                        int min = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + islandID + ".minPlayers");
                        int max = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + islandID + ".maxPlayers");
                        if (max >= tempUsers.size() && min <= tempUsers.size() && !warIslands.getConfig().getBoolean("warIslands.island." + args[1] + "." + islandID + ".inUse")) {
                            br = true;
                            islandIDList.add(newTheme + "." + islandID);
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
                }
            } else {
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
            }
            if (!br) {
                player.sendMessage(ChatColor.RED + "There are no islands in this theme found that you will fit in, choose a different theme");
                return;
            }

            player.sendMessage("All people have to accept this and the war will be send to the server");

//            ArrayList<String> pendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//            pendingList.add(islandName1);
//            warData.getConfig().set("war.pending.memberPendingList", pendingList);

            int pendingID = Bukkit.getScheduler().runTaskLater(p, new island1askPlayers(islandName1), pendingTime * 20).getTaskId();

            warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", pendingID);
            for (String user : tempUsers) {
                if (user.equals(uuid)) {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, false);
                }
            }
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandList", islandIDList);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMax", totalMax);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMin", totalMin);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".theme", args[1]);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".starter", uuid);
            warData.saveConfig();

            sendWarMessage(player, args[1], tempUsers, pendingTime);

        } else if ((args.length == 3 && args[2].equalsIgnoreCase(player.getName())) || args.length == 2) {

            if (!args[1].equalsIgnoreCase("random")) {
                if (!warIslands.getConfig().contains("warIslands.island." + args[1])) {
                    player.sendMessage("there is no theme found with the theme name " + args[1]);
                    return;
                }
                Set<String> set = warIslands.getConfig().getConfigurationSection("warIslands.island." + args[1]).getKeys(false);

                if (set.size() == 0) {
                    player.sendMessage("there is no theme found with the theme name " + args[1]);
                    return;
                }
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

            if (args[1].equalsIgnoreCase("random")) {
                for (String newTheme : warIslands.getConfig().getConfigurationSection("warIslands.island").getKeys(false)) {
                    for (String islandID : warIslands.getConfig().getConfigurationSection("warIslands.island." + newTheme).getKeys(false)) {
                        int min = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + islandID + ".minPlayers");
                        int max = warIslands.getConfig().getInt("warIslands.island." + args[1] + "." + islandID + ".maxPlayers");
                        if (max >= tempUsers.size() && min <= tempUsers.size() && !warIslands.getConfig().getBoolean("warIslands.island." + args[1] + "." + islandID + ".inUse")) {
                            br = true;
                            islandIDList.add(newTheme + "." + islandID);
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
                }
                if (!br) {
                    player.sendMessage("there was no island found that you will fit in, please remove or add players");
                    return;
                }
            } else {
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
            }


//            ArrayList<String> pendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//            pendingList.add(island);
//            warData.getConfig().set("war.pending.memberPendingList", pendingList);

            warData.getConfig().set("war.pending.island1." + islandName1 + ".pendingID", "nil");
            for (String user : tempUsers) {
                if (user.equals(uuid)) {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island1." + islandName1 + ".players." + user, false);
                }
            }
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandList", islandIDList);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMax", totalMax);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".islandMin", totalMin);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".theme", args[1]);
            warData.getConfig().set("war.pending.island1." + islandName1 + ".starter", uuid);
            warData.saveConfig();

            new Ready().skipReady1(p, islandName1, pendingTime);

        } else {
            player.sendMessage("wrong use");
        }
    }
}
