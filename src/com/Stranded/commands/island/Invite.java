package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Invite extends CmdManager implements Runnable {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getAlias() {
        return "inv";
    }

    @Override
    public void run(String[] args, Player player) {
        if (args.length == 2) {

            if (!p.getConfig().contains("island." + player.getName())) {
                player.sendMessage("your aren't in an island");
                return;
            }

            Files f = new Files(p, "islands.yml");
            if (!f.getConfig().getString("island." + p.getConfig().getString("island." + player.getName()) + ".owner").equals(player.getName())) {
                player.sendMessage("you are not the owner of this island, so you can't invite someone");
                return;
            }

            if (Bukkit.getPlayerExact(args[1]) == null) {

                OfflinePlayer[] list = Bukkit.getOfflinePlayers();
                for (int i = 0; i <= list.length - 1; i++) {
                    if (list[i].getName().equals(args[1])) {
                        player.sendMessage(ChatColor.RED + "I'm sorry but this player isn't online at the moment");
                        return;
                    }
                }
                player.sendMessage(ChatColor.DARK_RED + "i'm sorry, but this player doesn't exist");
                return;
            }

            Player invited = Bukkit.getPlayerExact(args[1]);
            if (!p.getConfig().contains("island." + invited.getName()) || !p.getConfig().getString("island." + invited.getName()).equals(p.getConfig().getString("island." + player.getName()))) {

                String island = p.getConfig().getString("island." + player.getName());
                invited.sendMessage(ChatColor.DARK_GREEN + "do you want to Join " + island + "?");

                String islandOld = p.getConfig().getString("island." + invited.getName());
                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
                if (old.size() == 1) {
                    invited.sendMessage("NOTE: if you leave you island, this will get deleted. there can't be less than 1 player in an island");
                }
                player.sendMessage(ChatColor.GREEN + "your invitation has been sent to " + invited.getName());
                p.getConfig().set("invited." + invited.getName() + ".island", island);
                p.getConfig().set("invited." + invited.getName() + ".pendingID", Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, 1000));
                p.saveConfig();
            } else {
                player.sendMessage(invited.getName() + " is already in your island");
            }

        } else {
            player.sendMessage("wrong use");
        }
    }

    @Override
    public void run() {
        //pending to remove a invite
    }

//    public static void invite(Player sender, String invited, Main p) {
//
//        if (!p.getConfig().contains("island." + sender.getName())) {
//            sender.sendMessage("your arn't in an island");
//            return;
//        }
//
//        if (Bukkit.getPlayerExact(invited) == null) {
//
//            OfflinePlayer[] list = Bukkit.getOfflinePlayers();
//            for (int i = 0; i <= list.length - 1; i++) {
//                if (list[i].getName().equals(invited)) {
//                    sender.sendMessage(ChatColor.RED + "I'm sorry but this player isn't online at the moment");
//                    return;
//                }
//            }
//            sender.sendMessage(ChatColor.DARK_RED + "i'm sorry, but this player doesn't exist");
//            return;
//        }
//
//        if (p.getConfig().getString("island." + Bukkit.getPlayerExact(invited).getName()).equals(p.getConfig().getString("island." + sender))) {
//
//            String island = p.getConfig().getString("island." + sender.getName());
//            Bukkit.getPlayerExact(invited).sendMessage(ChatColor.DARK_GREEN + "do you want to Join " + island + "?");
//
//            String islandOld = p.getConfig().getString("island." +  Bukkit.getPlayerExact(invited).getName());
//            Files f = new Files(p, "islands.yml");
//            ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
//            if (old.size() == 1) {
//                Bukkit.getPlayerExact(invited).sendMessage("NOTE: if you leave you island, this will get deleted. there can't be less than 1 player in an island");
//            }
//            sender.sendMessage(ChatColor.GREEN + "your invitation has been sent to " + invited);
//            p.getConfig().set("invited." + invited, island);
//            p.saveConfig();
//        } else {
//            sender.sendMessage(invited + " is already in your island");
//        }
//
//    }
}
