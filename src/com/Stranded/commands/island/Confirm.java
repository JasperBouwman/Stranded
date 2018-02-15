package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Confirm extends CmdManager {
    @Override
    public String getName() {
        return "confirm";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //island confirm

        String playerName = player.getName();
        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (args.length == 1) {
            if (config.getConfig().contains("deleteOtherIsland." + uuid)) {

                Files pluginData = getFiles("pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                int taskID = config.getConfig().getInt("deleteOtherIsland." + uuid + ".taskID");
                String island = config.getConfig().getString("deleteOtherIsland." + uuid + ".island");
                String reason = config.getConfig().getString("deleteOtherIsland." + uuid + ".reason");

                Bukkit.getScheduler().cancelTask(taskID);
                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

                config.getConfig().set("deleteOtherIsland." + uuid, null);
                config.saveConfig();

                Files islands = getFiles("islands.yml");

                ArrayList<String> list1 = (ArrayList<String>) islands.getConfig().getStringList("island." + island + ".members");

                for (String s : list1) {

                    Main.resetPlayerData(s);

                    Player tempPlayer = PlayerUUID.getPlayerExact(s);

                    if (tempPlayer != null) {
                        tempPlayer.sendMessage(ChatColor.RED + "Your island has been removed by a " +
                                ChatColor.DARK_BLUE + "moderator (" + playerName + ") " + ChatColor.RED + " with the reason: " + ChatColor.GREEN + reason);

                        tempPlayer.teleport(l);

                        com.Stranded.Scoreboard.scores(tempPlayer);

                    } else {
                        config.getConfig().set("online.removedIslandByMod." + PlayerUUID.getPlayerUUID(s), reason);
                        config.saveConfig();
                    }
                }

                player.sendMessage(ChatColor.GREEN + "This island has been removed");

                islands.getConfig().set("island." + island, null);
                islands.saveConfig();

            } else if (config.getConfig().contains("deleteIsland." + uuid)) {

                Files pluginData = getFiles("pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                int taskID = config.getConfig().getInt("deleteIsland." + uuid);

                Bukkit.getScheduler().cancelTask(taskID);
                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

                config.getConfig().set("deleteIsland." + uuid, null);
                config.saveConfig();

                Files islands = getFiles("islands.yml");

                ArrayList<String> list1 = (ArrayList<String>) islands.getConfig().getStringList("island." + config.getConfig().getString("island." + uuid) + ".members");

                for (String s : list1) {

                    Main.resetPlayerData(s);

                    Player tempPlayer = PlayerUUID.getPlayerExact(s);

                    if (tempPlayer != null && !s.equals(player.getName())) {
                        tempPlayer.sendMessage(ChatColor.RED + "Your island has been removed by the " + ChatColor.DARK_BLUE + "owner");


                        tempPlayer.teleport(l);

                        com.Stranded.Scoreboard.scores(tempPlayer);

                    } else if (!s.equals(player.getName())) {
                        ArrayList<String> listOnline = (ArrayList<String>) config.getConfig().getStringList("online.removedIsland");
                        String uuid2 = PlayerUUID.getPlayerUUID(s);
                        listOnline.add(uuid2);
                        config.getConfig().set("online.removedIsland", listOnline);
                        config.saveConfig();
                    }
                }

                com.Stranded.Scoreboard.scores(player);
                Main.resetPlayerData(player.getUniqueId().toString());

                player.sendMessage(ChatColor.GREEN + "Your island has been removed");
                player.teleport(l);

                islands.getConfig().set("island." + config.getConfig().getString("island." + uuid), null);
                islands.saveConfig();

            } else {
                player.sendMessage(ChatColor.RED + "No island delete pending");
            }

        } else {
            sendWrongUse(player, "/island confirm");
        }
    }
}
