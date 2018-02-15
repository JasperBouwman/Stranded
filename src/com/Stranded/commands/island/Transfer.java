package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Transfer extends CmdManager {

    @Override
    public String getName() {
        return "transfer";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //island transfer <player name>

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You must own an island to to transfer your island to another one");
            return;
        }

        if (args.length == 2) {

            Player newOwner = PlayerUUID.getPlayerExact(args[1]);
            String newOwnerUUID = "";

            if (newOwner == null) {
                ArrayList<Player> list = PlayerUUID.getGlobalPlayers(args[1]);
                if (list.size() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no player found with the name " + args[1]);
                    return;
                }
                if (list.size() == 1) {
                    newOwner = list.get(0);

                    if (newOwner == null) {
                        newOwnerUUID = PlayerUUID.getGlobalPlayerUUID(args[1]).get(0);
                    } else {
                        newOwnerUUID = newOwner.getUniqueId().toString();
                    }
                }
                if (list.size() > 1) {
                    player.sendMessage(ChatColor.RED + "There are more '" + args[1] + "' found, this is case sensitive");
                    return;
                }
            }

            if (newOwnerUUID.equals("") && newOwner != null) {
                newOwnerUUID = newOwner.getUniqueId().toString();
            }

            Files islands = getFiles("islands.yml");

            String island = config.getConfig().getString("island." + uuid);
            String newOwnerName = PlayerUUID.getPlayerName(newOwnerUUID);

            Set<String> set = islands.getConfig().getConfigurationSection("island." + island + ".members").getKeys(false);

            if (!set.contains(newOwnerUUID)) {
                player.sendMessage(ChatColor.RED + newOwnerName + " is not in your island");
                return;
            }

            islands.getConfig().set("island." + island + ".owner", newOwnerUUID);
            islands.saveConfig();

            player.sendMessage(ChatColor.GREEN + "Your island is successfully given to " + newOwnerName);

            if (newOwner == null) {

                config.getConfig().set("online.newIslandOwner." + newOwnerUUID, "you are now the owner of " + island);
                config.saveConfig();

            } else {
                newOwner.sendMessage(ChatColor.GREEN + "You are now the owner of " + island);
            }

            for (String memberUUIDs : set) {

                Player tmpPlayer = Bukkit.getPlayer(UUID.fromString(memberUUIDs));

                if (uuid.equals(memberUUIDs) || memberUUIDs.equals(newOwnerUUID)) {
                    continue;
                }

                if (tmpPlayer != null) {
                    tmpPlayer.sendMessage(ChatColor.GREEN + "Your island has a new owner.\n" + newOwnerName + " is now the new owner");
                }

            }

        } else {
            sendWrongUse(player, new String[]{"/island transfer <player>", "/island transfer "});
        }
    }
}
