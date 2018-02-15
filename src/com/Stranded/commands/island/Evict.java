package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Evict extends CmdManager {
    @Override
    public String getName() {
        return "evict";
    }

    @Override
    public String getAlias() {
        return "i";
    }

    @Override
    public void run(String[] args, Player player) {

        //island evict <player name>

        String uuid = player.getUniqueId().toString();

        Files islands = getFiles("islands.yml");
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid) || !config.getConfig().contains("island")) {
            player.sendMessage(ChatColor.RED + "You can only use this when you're in an island");
            return;
        }

        String island = config.getConfig().getString("island." + uuid);

        if (!islands.getConfig().getString("island." + island + ".owner").equals(uuid)) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this island, so you can't evict someone");
            return;
        }
        ArrayList<String> list = (ArrayList<String>) islands.getConfig().getStringList("island." + island + ".members");

        if (args.length == 2) {

            String evictPlayerUUID = PlayerUUID.getPlayerUUID(args[1]);

            if (evictPlayerUUID == null) {
                ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[1]);
                if (tempPlayers.size() > 1) {
                    player.sendMessage(ChatColor.RED + "The player '" + args[1] + "' is not found, but there are more players found with the same name (not case sensitive)");
                    return;
                } else if (tempPlayers.size() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no player found with this name");
                    return;
                } else {
                    evictPlayerUUID = tempPlayers.get(0);
                }
            }

            if (evictPlayerUUID == null) {
                player.sendMessage(ChatColor.RED + "This player isn't found, please make sure that you gave the right player name. this is case sensitive");
                return;
            }

            String evictPlayerName = PlayerUUID.getPlayerName(evictPlayerUUID);

            if (list.contains(evictPlayerUUID)) {

                Files warData = getFiles("warData.yml");
                if (config.getConfig().contains("playersInWar")) {
                    if (config.getConfig().getStringList("playersInWar").contains(evictPlayerUUID)) {
                        player.sendMessage(ChatColor.RED + "You can't evict " + evictPlayerName + " while he is in a war");
                        return;
                    }
                }
                if (warData.getConfig().contains("war.pending.island1." + island)) {
                    if (warData.getConfig().contains("war.pending.island1." + island + ".players." + evictPlayerUUID)) {
                        player.sendMessage(ChatColor.RED + "You can't evict " + evictPlayerName + ", this player is pending for a war");
                        return;
                    }
                }
                if (warData.getConfig().contains("war.pending.island2." + island)) {
                    if (warData.getConfig().contains("war.pending.island2." + island + ".players." + evictPlayerUUID)) {
                        player.sendMessage(ChatColor.RED + "You can't evict " + evictPlayerName + ", this player is pending for a war");
                        return;
                    }
                }

                list.remove(evictPlayerUUID);
                islands.getConfig().set("island." + island + ".members", list);
                islands.saveConfig();

                player.sendMessage(ChatColor.GREEN + args[1] + " successfully evicted");

                Player evictPlayer = PlayerUUID.getPlayerExact(args[1]);

                if (evictPlayer != null) {

                    Main.resetPlayerData(evictPlayerUUID);
                    com.Stranded.Scoreboard.updateIslandScoreboard(island);
                    com.Stranded.Scoreboard.scores(evictPlayer);

                    Files pluginData = getFiles("pluginData.yml");
                    Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                    evictPlayer.sendMessage(ChatColor.RED + "You where removed by the owner from this island");
                    evictPlayer.teleport(l);
                } else {
                    String offlineUUID = PlayerUUID.getPlayerUUID(evictPlayerUUID);
                    ArrayList<String> listOnline = (ArrayList<String>) config.getConfig().getStringList("online.evict");
                    listOnline.add(offlineUUID);
                    config.getConfig().set("online.evict", listOnline);
                    config.saveConfig();
                }
            } else {
                player.sendMessage(ChatColor.RED + "This player isn't in your island");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /island evict <player>");
            sendWrongUse(player, new String[]{"/island evict <player>", "/island evict "});
        }
    }
}
