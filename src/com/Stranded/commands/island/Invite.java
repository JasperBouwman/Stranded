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

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("your aren't in an island");
            return;
        }

        Files f = new Files(p, "islands.yml");
        if (!f.getConfig().getString("island." + p.getConfig().getString("island." + player.getName()) + ".owner").equals(player.getName())) {
            player.sendMessage("you are not the owner of this island, so you can't invite someone");
            return;
        }

        if (args.length == 2) {

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

                if (p.getConfig().contains("invited." + invited.getName())) {
                    player.sendMessage("this player has already an inventation ready, wait for this player to react to that invite");
                    return;
                }

                String island = p.getConfig().getString("island." + player.getName());
                invited.sendMessage(ChatColor.DARK_GREEN + "do you want to Join " + island + "?");

                String islandOld = p.getConfig().getString("island." + invited.getName());
                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
                if (old.size() == 1) {
                    invited.sendMessage("NOTE: if you leave you island, this will get deleted. there can't be less than 1 player in an island");
                }
                player.sendMessage(ChatColor.GREEN + "your invitation has been sent to " + invited.getName());
                ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("invitedList");
                list.add(invited.getName());
                p.getConfig().set("invitedList", list);

                p.getConfig().set("invited." + invited.getName() + ".newIsland", island);
                p.getConfig().set("invited." + invited.getName() + ".pendingID", Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, 1000));
                p.getConfig().set("invited." + invited.getName() + ".inviter", player.getName());

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

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("invitedList");
        if (list.size() == 0) {
            return;
        }

        String player = list.get(0);

        if (Bukkit.getPlayerExact(p.getConfig().getString("invited." + player + ".inviter")) != null) {
            Bukkit.getPlayerExact(p.getConfig().getString("invited." + player + ".inviter")).sendMessage(player + " hasn't reacted in time to join your island");
        }
        if (Bukkit.getPlayerExact(player) != null) {
            Bukkit.getPlayerExact(player).sendMessage("you ignored the inventation of the island " + p.getConfig().getString("invited." + player + ".newIsland"));
        }

        p.getConfig().set("invited." + player, null);

        list.remove(player);
        p.getConfig().set("invitedList", list);
        p.saveConfig();
    }
}
