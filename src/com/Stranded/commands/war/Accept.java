package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.war.runnables.MemberPendingListNewIsland;
import com.Stranded.commands.war.util.WarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Accept extends CmdManager implements Runnable {
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

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }

        Long pendingTime;

        Files warData = new Files(p, "warData.yml");
        Files islands = new Files(p, "islands.yml");

        try {
            pendingTime = Long.parseLong(warData.getConfig().getString("war.pending.timeIslandWarriors"));
        } catch (NumberFormatException nfe) {
            pendingTime = (long) 10;
        }

        String islandName2 = p.getConfig().getString("island." + player.getName());

        if (WarUtil.testIfIsInWarWithComments(p, player))
            return;

        // war accept <island1> [players]
        if ((args.length == 3 && !args[2].equalsIgnoreCase(player.getName())) || args.length > 3) {

            ArrayList<String> island2MemberList = (ArrayList<String>) islands.getConfig().getStringList("island." + islandName2 + ".members");
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

            boolean br = false;
            for (int i = 2; i < args.length; i++) {
                if (!tempUsers.contains(args[i])) {
                    tempUsers.add(args[i]);

                    if (!island2MemberList.contains(args[i])) {
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

            int max = warData.getConfig().getInt("war.pending.islandInvite." + islandName1 + ".islandMax");
            int min = warData.getConfig().getInt("war.pending.islandInvite." + islandName1 + ".islandMin");

            if (tempUsers.size() > max) {
                player.sendMessage("you have chosen to many players, you have to remove " + (tempUsers.size() - max) + " players");
                return;
            }

            if (tempUsers.size() < min) {
                player.sendMessage("you have chosen to few players, you have to add " + (min - tempUsers.size()) + " players");
                return;
            }

            player.sendMessage("all players have to accept this, and than the war will begin");

            ArrayList<String> memberPendingListNewIsland = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
            memberPendingListNewIsland.add(islandName2);
            warData.getConfig().set("war.pending.memberPendingListNewIsland", memberPendingListNewIsland);

            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, new MemberPendingListNewIsland(p), pendingTime * 20);

            warData.getConfig().set("war.pending.island2." + islandName2 + ".pendingID", pendingID);
            warData.getConfig().set("war.pending.island2." + islandName2 + ".island", islandName1);
            for (String user : tempUsers) {
                if (user.equals(player.getName())) {
                    warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + user, true);
                } else {
                    warData.getConfig().set("war.pending.island2." + islandName2 + ".players." + user, false);
                }
            }

            warData.saveConfig();
        } else if ((args.length == 3 && args[2].equalsIgnoreCase(player.getName())) || args.length == 2) {

            if (warData.getConfig().contains("war.pending.acceptation." + player.getName())) {
                String island2 = warData.getConfig().getString("war.pending.acceptation." + player.getName() + ".island2");
                int pendingID = warData.getConfig().getInt("war.pending.acceptation." + player.getName() + ".pendingID");
                Bukkit.getScheduler().cancelTask(pendingID);
                WarUtil.startWar(island2, p);
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

            tempUsers.add(player.getName());

            int max = warData.getConfig().getInt("war.pending.islandInvite." + islandName1 + ".islandMax");
            int min = warData.getConfig().getInt("war.pending.islandInvite." + islandName1 + ".islandMin");

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
                if (user.equals(player.getName())) {
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

    @Override
    public void run() {

        Files warData = new Files(p, "warData.yml");

        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");

        if (list.size() < 1) {
            return;
        }
        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.island2." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayerExact(players) != null) {
                Bukkit.getPlayerExact(players).sendMessage("not everybody accepted this war invite");
            }
        }
        warData.getConfig().set("war.pending.island2." + island, null);

        list.remove(island);
        warData.getConfig().set("war.pending.memberPendingListNewIsland", list);

        warData.saveConfig();
    }
}
