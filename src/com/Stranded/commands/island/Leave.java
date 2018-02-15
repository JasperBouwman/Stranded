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

import static com.Stranded.GettingFiles.getFiles;

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
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You aren't in an island");
            return;
        }

        Files warData = getFiles("warData.yml");

        if (config.getConfig().contains("playersInWar")) {
            if (config.getConfig().getStringList("playersInWar").contains(uuid)) {
                player.sendMessage(ChatColor.RED + "You can't leave an island while you are in a war");
                return;
            }
        }
        if (warData.getConfig().contains("war.pending.island1." + config.getConfig().getString("island." + uuid))) {
            if (warData.getConfig().contains("war.pending.island1." + config.getConfig().getString("island." + uuid) + ".players." + uuid)) {
                player.sendMessage(ChatColor.RED + "You can't leave your island while you are pending for a war");
                return;
            }
        }
        if (warData.getConfig().contains("war.pending.island2." + config.getConfig().getString("island." + player.getName()))) {
            if (warData.getConfig().contains("war.pending.island2." + config.getConfig().getString("island." + uuid) + ".players." + uuid)) {
                player.sendMessage(ChatColor.RED + "You can't leave your island while you are pending for a war");
                return;
            }
        }


        Files files = getFiles("islands.yml");

        String islandOld = config.getConfig().getString("island." + uuid);

        com.Stranded.Scoreboard.updateIslandScoreboard(islandOld);
        com.Stranded.Scoreboard.scores(player);
        Main.resetPlayerData(uuid);

        ArrayList<String> old = (ArrayList<String>) files.getConfig().getStringList("island." + islandOld + ".members");
        if (old.contains(uuid)) {
            config.getConfig().set("island." + uuid, null);
            config.saveConfig();
            old.remove(uuid);
            files.getConfig().set("island." + islandOld + ".members", old);

            if (files.getConfig().getString("island." + islandOld + ".owner").equals(uuid)) {

                String newOwner = old.get(new Random().nextInt(old.size()));
                if (Bukkit.getPlayer(UUID.fromString(newOwner)) != null) {
                    Bukkit.getPlayer(UUID.fromString(newOwner)).sendMessage(ChatColor.RED + "You are the new owner of " + islandOld);
                } else {
                    ArrayList<String> newOwnerList = (ArrayList<String>) config.getConfig().getStringList("online.newOwner");
                    newOwnerList.add(newOwner);
                    config.getConfig().set("online.newOwner", newOwnerList);
                    config.saveConfig();
                }
                files.getConfig().set("island." + islandOld + ".owner", newOwner);
            }

            for (String s : old) {
                if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                    Bukkit.getPlayer(UUID.fromString(s)).sendMessage(ChatColor.RED + player.getName() + " left your island");
                }
            }

            Files pluginData = getFiles("pluginData.yml");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(l);

            if (old.size() == 0) {
                files.getConfig().set("island." + islandOld, null);
                files.saveConfig();
                player.sendMessage(ChatColor.RED + "You left " + islandOld + " as last one, so this island is now deleted (not from the world)");
                return;
            }
            player.sendMessage(ChatColor.RED + "You left " + islandOld);
            files.saveConfig();

        } else {
            player.sendMessage(ChatColor.RED + "You aren't in an island");
        }

    }
}
