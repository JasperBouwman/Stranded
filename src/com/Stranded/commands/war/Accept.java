package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.war.runnables.island2askPlayers;
import com.Stranded.commands.war.util.StartWar;
import com.Stranded.commands.war.util.WarUtil;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;

public class Accept extends CmdManager {
    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getAlias() {
        return "ac";
    }

    @Override
    public void run(String[] args, Player player) {

        // war accept <island1> [players]

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage("you aren't in an island" + ChatColor.RED);
            return;
        }

        long pendingTime = 30;

        Files warData = getFiles("warData.yml");
        Files islands = getFiles("islands.yml");

        String islandName2 = config.getConfig().getString("island." + uuid);

        if (WarUtil.testIfIsInWarWithComments(player))
            return;

        if ((args.length == 3 && !args[2].equalsIgnoreCase(player.getName())) || args.length > 3) {

            ArrayList<String> island2MemberList = (ArrayList<String>) islands.getConfig().getStringList("island." + islandName2 + ".members");
            ArrayList<String> tempUsersUUID = new ArrayList<>();

            String islandName1 = args[1];

            if (islandName1.equalsIgnoreCase(islandName2)) {
                player.sendMessage("you can't start a war with your own island" + ChatColor.RED);
                return;
            }

            if (!warData.getConfig().contains("war.pending.islandInvite." + islandName1)) {
                player.sendMessage("the island " + islandName1 + " is not asking for a war" + ChatColor.RED);
                return;
            }

            boolean br = false;
            for (int i = 2; i < args.length; i++) {

                //get player uuid
                String playerUUID = PlayerUUID.getPlayerUUID(args[i]);
                //test if player is found
                if (playerUUID == null) {
                    ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[i]);
                    if (tempPlayers.size() > 1) {
                        //more players found with name
                        player.sendMessage("there are more players found with the " + args[i] + ChatColor.RED);
                        br = true;
                    } else if (tempPlayers.size() == 0) {
                        //no players found with name
                        player.sendMessage("there is no player found with the name " + args[i] + ChatColor.RED);
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


                if (!tempUsersUUID.contains(playerUUID)) {
                    tempUsersUUID.add(playerUUID);

                    if (!island2MemberList.contains(playerUUID)) {
                        player.sendMessage(playerName + " is not a member in your island" + ChatColor.RED);
                        br = true;
                    } else if (tmpPlayer == null) {
                        player.sendMessage(playerName + " is not online" + ChatColor.RED);
                        br = true;
                    }
                } else {
                    player.sendMessage(playerName + " is already mentioned" + ChatColor.YELLOW);
                    br = true;
                }
            }
            if (br) return;


            if (!tempUsersUUID.contains(uuid)) {
                tempUsersUUID.add(uuid);
            }

            int max = warData.getConfig().getInt("war.pending.islandInvite." + islandName1 + ".islandMax");
            int min = warData.getConfig().getInt("war.pending.islandInvite." + islandName1 + ".islandMin");

            if (tempUsersUUID.size() > max) {
                player.sendMessage("You have chosen too many players, you have to remove " + (tempUsersUUID.size() - max) + " players");
                return;
            }

            if (tempUsersUUID.size() < min) {
                player.sendMessage("You have chosen to few players, you have to add " + (min - tempUsersUUID.size()) + " players");
                return;
            }

            player.sendMessage("All players have to accept this, and than the war will begin");

//            ArrayList<String> memberPendingListNewIsland = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
//            memberPendingListNewIsland.add(islandName2);
//            warData.getConfig().set("war.pending.memberPendingListNewIsland", memberPendingListNewIsland);


            int pendingID = Bukkit.getScheduler().runTaskLater(p, new island2askPlayers(p, islandName2), pendingTime * 20).getTaskId();

            warData.getConfig().set("war.pending.island2." + islandName2 + ".pendingID", pendingID);
            warData.getConfig().set("war.pending.island2." + islandName2 + ".island", islandName1);
            for (String user : tempUsersUUID) {
                if (user.equals(uuid)) {
                    warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + user, false);
                }
            }

            warData.saveConfig();
        } else if ((args.length == 3 && args[2].equalsIgnoreCase(player.getName())) || args.length == 2) {

            //war accept <island2Name>
            if (warData.getConfig().contains("war.pending.acceptation." + uuid + "." + args[1]) && args.length == 2) {
                //war accept <island2Name>
                String island2 = args[1];
                int pendingID = warData.getConfig().getInt("war.pending.acceptation." + uuid + "." + island2 + ".pendingID");
                //canceled island1AcceptIsland2
                Bukkit.getScheduler().cancelTask(pendingID);

                StartWar.startWar(island2);
                return;
            }

            ArrayList<String> tempUsers = new ArrayList<>();
            String islandName1 = args[1];

            if (islandName1.equalsIgnoreCase(islandName2)) {
                player.sendMessage("You can't start a war with your own island" + ChatColor.RED);
                return;
            }

            if (!warData.getConfig().contains("war.pending.islandInvite." + islandName1)) {
                player.sendMessage("The island " + islandName1 + " is not asking for a war" + ChatColor.RED);
                return;
            }

            tempUsers.add(uuid);

            int max = warData.getConfig().getInt("war.pending.island1." + islandName1 + ".islandMax");
            int min = warData.getConfig().getInt("war.pending.island1." + islandName1 + ".islandMin");

            if (tempUsers.size() > max) {
                player.sendMessage("You have chosen to many players, you have to remove " + (tempUsers.size() - max) + " players" + ChatColor.RED);
                return;
            }

            if (tempUsers.size() < min) {
                player.sendMessage("You have chosen to few players, you have to add " + (min - tempUsers.size()) + " players" + ChatColor.RED);
                return;
            }

//            ArrayList<String> pendingList = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
//            pendingList.add(island);
//            warData.getConfig().set("war.pending.memberPendingListNewIsland", pendingList);

            warData.getConfig().set("war.pending.island2." + islandName2 + ".pendingID", "nil");
            warData.getConfig().set("war.pending.island2." + islandName2 + ".island", islandName1);
            for (String user : tempUsers) {
                if (user.equals(uuid)) {
                    warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + user, false);
                }
            }

            warData.saveConfig();

            new Ready().skipReady2(p, player);

        } else {
            player.sendMessage("Wrong use" + ChatColor.RED);
        }
    }
}
