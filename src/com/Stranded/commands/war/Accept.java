package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.commands.war.runnables.island2askPlayers;
import com.Stranded.commands.war.util.StartWar;
import com.Stranded.commands.war.util.WarUtil;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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


        if (!p.getConfig().contains("island." + uuid)) {
            player.sendMessage("you aren't in an island");
            return;
        }

        long pendingTime = 30;

        Files warData = new Files(p, "warData.yml");
        Files islands = new Files(p, "islands.yml");

        String islandName2 = p.getConfig().getString("island." + uuid);

        if (WarUtil.testIfIsInWarWithComments(p, player))
            return;

        if ((args.length == 3 && !args[2].equalsIgnoreCase(player.getName())) || args.length > 3) {

            ArrayList<String> island2MemberList = (ArrayList<String>) islands.getConfig().getStringList("island." + islandName2 + ".members");
            ArrayList<String> tempUsersUUID = new ArrayList<>();

            String islandName1 = args[1];

            if (islandName1.equalsIgnoreCase(islandName2)) {
                player.sendMessage("you can't start a war with your own island");
                return;
            }

            if (!warData.getConfig().contains("war.pending.islandInvite." + islandName1)) {
                player.sendMessage("the island " + islandName1 + " is not asking for a war");
                return;
            }

            boolean br = false;
            for (int i = 2; i < args.length; i++) {

                //get player uuid
                String playerUUID = PlayerUUID.getPlayerUUID(args[i], p);
                //test if player is found
                if (playerUUID == null) {
                    ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[i], p);
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
                String playerName = PlayerUUID.getPlayerName(playerUUID, p);
                //get player
                Player tmpPlayer = PlayerUUID.getPlayerExact(playerName, p);


                if (!tempUsersUUID.contains(playerUUID)) {
                    tempUsersUUID.add(playerUUID);

                    if (!island2MemberList.contains(playerUUID)) {
                        player.sendMessage(playerName + " is not a member in your island");
                        br = true;
                    } else if (tmpPlayer == null) {
                        player.sendMessage(playerName + " is not online");
                        br = true;
                    }
                } else {
                    player.sendMessage(playerName + " is already mentioned");
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
                player.sendMessage("you have chosen to many players, you have to remove " + (tempUsersUUID.size() - max) + " players");
                return;
            }

            if (tempUsersUUID.size() < min) {
                player.sendMessage("you have chosen to few players, you have to add " + (min - tempUsersUUID.size()) + " players");
                return;
            }

            player.sendMessage("all players have to accept this, and than the war will begin");

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

                StartWar.startWar(island2, p);
                return;
            }

            ArrayList<String> tempUsers = new ArrayList<>();
            String islandName1 = args[1];

            if (islandName1.equalsIgnoreCase(islandName2)) {
                player.sendMessage("you can't start a war with your own island");
                return;
            }

            if (!warData.getConfig().contains("war.pending.islandInvite." + islandName1)) {
                player.sendMessage("the island " + islandName1 + " is not asking for a war");
                return;
            }

            tempUsers.add(uuid);

            int max = warData.getConfig().getInt("war.pending.island1." + islandName1 + ".islandMax");
            int min = warData.getConfig().getInt("war.pending.island1." + islandName1 + ".islandMin");

            if (tempUsers.size() > max) {
                player.sendMessage("you have chosen to many players, you have to remove " + (tempUsers.size() - max) + " players");
                return;
            }

            if (tempUsers.size() < min) {
                player.sendMessage("you have chosen to few players, you have to add " + (min - tempUsers.size()) + " players");
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
            player.sendMessage("wrong use");
        }
    }
}
