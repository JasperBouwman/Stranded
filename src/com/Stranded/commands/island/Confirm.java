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

        String playerName = PlayerUUID.getPlayerName(player.getUniqueId(), p);
        String uuid = player.getUniqueId().toString();

        if (args.length == 1) {
            if (p.getConfig().contains("deleteOtherIsland." + uuid)) {

                Files pluginData = new Files(p, "pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                int taskID = p.getConfig().getInt("deleteOtherIsland." + uuid + ".taskID");
                String island = p.getConfig().getString("deleteOtherIsland." + uuid + ".island");
                String reason = p.getConfig().getString("deleteOtherIsland." + uuid + ".reason");

                Bukkit.getScheduler().cancelTask(taskID);
                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

                p.getConfig().set("deleteOtherIsland." + uuid, null);
                p.saveConfig();

                Files islands = new Files(p, "islands.yml");

                ArrayList<String> list1 = (ArrayList<String>) islands.getConfig().getStringList("island." + island + ".members");

                for (String s : list1) {

                    Main.resetPlayerData(s, p);

                    Player tempPlayer = PlayerUUID.getPlayerExact(s, p);

                    if (tempPlayer != null) {
                        tempPlayer.sendMessage(ChatColor.RED + "Your island has been removed by a " +
                                ChatColor.DARK_BLUE + "moderator (" + playerName + ") " + ChatColor.RED + " with the reason: " + ChatColor.GREEN + reason);

                        tempPlayer.teleport(l);

                        com.Stranded.Scoreboard.scores(p, tempPlayer);

                    } else {
                        p.getConfig().set("online.removedIslandByMod." + PlayerUUID.getPlayerUUID(s, p), reason);
                        p.saveConfig();
                    }
                }

                player.sendMessage(ChatColor.GREEN + "This island has been removed");

                islands.getConfig().set("island." + island, null);
                islands.saveConfig();

            } else if (p.getConfig().contains("deleteIsland." + uuid)) {

                Files pluginData = new Files(p, "pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                int taskID = p.getConfig().getInt("deleteIsland." + uuid);

                Bukkit.getScheduler().cancelTask(taskID);
                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

                p.getConfig().set("deleteIsland." + uuid, null);
                p.saveConfig();

                Files islands = new Files(p, "islands.yml");

                ArrayList<String> list1 = (ArrayList<String>) islands.getConfig().getStringList("island." + p.getConfig().getString("island." + uuid) + ".members");

                for (String s : list1) {

                    Main.resetPlayerData(s, p);

                    Player tempPlayer = PlayerUUID.getPlayerExact(s, p);

                    if (tempPlayer != null && !s.equals(player.getName())) {
                        tempPlayer.sendMessage(ChatColor.RED + "Your island has been removed by the " + ChatColor.DARK_BLUE + "owner");


                        tempPlayer.teleport(l);

                        com.Stranded.Scoreboard.scores(p, tempPlayer);

                    } else if (!s.equals(player.getName())) {
                        ArrayList<String> listOnline = (ArrayList<String>) p.getConfig().getStringList("online.removedIsland");
                        String uuid2 = PlayerUUID.getPlayerUUID(s, p);
                        listOnline.add(uuid2);
                        p.getConfig().set("online.removedIsland", listOnline);
                        p.saveConfig();
                    }
                }

                com.Stranded.Scoreboard.scores(p, player);
                Main.resetPlayerData(player.getUniqueId().toString(), p);

                player.sendMessage(ChatColor.GREEN + "Your island has been removed");
                player.teleport(l);

                islands.getConfig().set("island." + p.getConfig().getString("island." + uuid), null);
                islands.saveConfig();

            } else {
                player.sendMessage(ChatColor.RED + "No island delete pending");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /island confirm");
        }
    }
}
