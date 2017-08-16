package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
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

    @Override
    public void run(String[] args, Player player) {

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }

        Long PendingTime;

        Files war = new Files(p, "warData.yml");
        try {
            PendingTime = Long.parseLong(war.getConfig().getString("war.pending.timeIslandWarriors"));
        } catch (NumberFormatException nfe) {
            PendingTime = (long) 180;
        }

        if (war.getConfig().contains("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()))) {
            player.sendMessage("your island is already pending a war, status: warriors accepting");
            return;
        }
        if (war.getConfig().contains("war.pending.islandInvite." + p.getConfig().getString("island." + player.getName()))) {
            player.sendMessage("your island is already pending a war, status: other island accepting");
            return;
        }

        //war start <min> <max> <warIsland> [playerNames - self]
        if (args.length > 4) {

            int min;
            int max;
            String warIsland;

            try {
                min = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                player.sendMessage("not a valid min value");
                return;
            }
            try {
                max = Integer.parseInt(args[2]);
            } catch (NumberFormatException nfe) {
                player.sendMessage("not a valid max value");
                return;
            }

            if (args.length - 3 < min) {
                player.sendMessage("min value is to large");
                return;
            } else if (args.length - 3 > max) {
                player.sendMessage("max value is to small");
                return;
            }
            if (args[3].equals("warIsland")) {

                player.sendMessage("test");
                return;

            }

            Files f = new Files(p, "islands.yml");

            String island = p.getConfig().getString("island." + player.getName());

            ArrayList<String> islandMembers = (ArrayList<String>) f.getConfig().getStringList("island." + island + ".members");
            ArrayList<String> warPlayers = new ArrayList<>();
            boolean cancelCommand = false;
            for (int i = 4; i < args.length; i++) {
                if (Bukkit.getPlayerExact(args[i]) == null) {
                    player.sendMessage(args[i] + " isn't online");
                    cancelCommand = true;
                }
                if (!islandMembers.contains(args[i])) {
                    player.sendMessage(args[i] + " isn't in your island");
                    cancelCommand = true;
                }

                if (warPlayers.contains(args[i])) {
                    player.sendMessage(args[i] + " is already is your list");
                    cancelCommand = true;
                }

                if (!cancelCommand) {
                    warPlayers.add(Bukkit.getPlayerExact(args[i]).getName());
                }


            }
            if (cancelCommand) {
                return;
            }

            if (!warPlayers.contains(player.getName())) {
                warPlayers.add(player.getName());
            }

            int pendingID = Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, PendingTime * 20);

            player.sendMessage("your warriors are invited, they all have to accept it to let the server know that you want to battle");
            player.sendMessage("they have " + PendingTime + " seconds to answer, else it gets deleted");
            war.getConfig().set("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".pendingID", pendingID);
            war.getConfig().set("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".MAX", max);
            war.getConfig().set("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".MIN", min);

            war.getConfig().set("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".warriors", warPlayers);

            StringBuilder warriors = new StringBuilder().append(player.getName());

            for (int ii = 4; ii < args.length; ii++) {
                warriors.append(" ");
                warriors.append(args[ii]);
            }

            for (String s : warPlayers) {
                Bukkit.getPlayerExact(s).sendMessage("you are chosen to battle for you island by " + player.getName() + ", max: " + max + ", min: " + min);

                Bukkit.getPlayerExact(s).sendMessage("you will battle with these players: " + warriors.toString());
                war.getConfig().set("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".warriors." + s, false);
            }

            war.saveConfig();

        } else {
            player.sendMessage("wrong use");
        }

    }

    @Override
    public void run() {

//        Files war = new Files(p, "warData.yml");
//
//        ArrayList<String> list = (ArrayList<String>) war.getConfig().getStringList("war.pending.memberInvite." + p.getConfig().getString("island." + player.getName()) + ".warriors");
//
//        for (String s : list) {
//            if (Bukkit.getPlayerExact(s) != null) {
//                Bukkit.getPlayerExact(s).sendMessage("not everyone answered, the war does,t get send to other islands");
//            }
//        }
//
//        war.getConfig().set("war.pending.memberInvite." + p.getConfig().contains("island." + player.getName()), null);
//        war.saveConfig();

    }
}
