package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.Chat;
import com.Stranded.commands.CmdManager;
import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Leave extends CmdManager {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //island leave

        String uuid = player.getUniqueId().toString();

        if (!p.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You aren't in an island");
            return;
        }

        Files warData = new Files(p, "warData.yml");

        if (p.getConfig().getStringList("playersInWar").contains(uuid)) {
            player.sendMessage(ChatColor.RED + "You can't leave an island while you are in a war");
            return;
        }
        if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + uuid))) {
            if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + uuid) + ".players." + uuid)) {
                player.sendMessage(ChatColor.RED + "You can't leave your island while you are pending for a war");
                return;
            }
        }
        if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + player.getName()))) {
            if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + uuid) + ".players." + uuid)) {
                player.sendMessage(ChatColor.RED + "You can't leave your island while you are pending for a war");
                return;
            }
        }


        Files f = new Files(p, "islands.yml");

        String islandOld = p.getConfig().getString("island." + uuid);

        com.Stranded.Scoreboard.updateIslandScoreboard(p, islandOld);
        com.Stranded.Scoreboard.scores(p, player);
        Main.resetPlayerData(uuid, p);

        ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
        if (old.contains(uuid)) {
            p.getConfig().set("island." + uuid, null);
            p.saveConfig();
            old.remove(uuid);
            f.getConfig().set("island." + islandOld + ".members", old);

            if (f.getConfig().getString("island." + islandOld + ".owner").equals(uuid)) {

                String newOwner = old.get(new Random().nextInt(old.size()));
                if (Bukkit.getPlayer(UUID.fromString(newOwner)) != null) {
                    Bukkit.getPlayer(UUID.fromString(newOwner)).sendMessage(ChatColor.RED + "You are the new owner of " + islandOld);
                } else {
                    ArrayList<String> newOwnerList = (ArrayList<String>) p.getConfig().getStringList("online.newOwner");
                    newOwnerList.add(newOwner);
                    p.getConfig().set("online.newOwner", newOwnerList);
                    p.saveConfig();
                }
                f.getConfig().set("island." + islandOld + ".owner", newOwner);
            }

            for (String s : old) {
                if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                    Bukkit.getPlayer(UUID.fromString(s)).sendMessage(player.getName() + " left your island");
                }
            }

            Files pluginData = new Files(p, "pluginData.yml");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(l);

            if (old.size() == 0) {
                f.getConfig().set("island." + islandOld, null);
                f.saveConfig();
                player.sendMessage(ChatColor.RED + "You left " + islandOld + " as last one, so this island is now deleted (not from the world)");
                return;
            }
            player.sendMessage(ChatColor.RED + "You left " + islandOld);
            f.saveConfig();

        } else {
            player.sendMessage(ChatColor.RED + "You aren't in an island");
        }

    }
}
