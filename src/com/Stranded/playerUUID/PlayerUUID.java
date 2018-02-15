package com.Stranded.playerUUID;

import com.Stranded.Files;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class PlayerUUID {

    public static void setPlayerUUID(Player player) {

        Files playerUUID = getFiles("playerUUID.yml");

        if (playerUUID.getConfig().contains("UUIDList." + player.getUniqueId().toString())) {
            if (playerUUID.getConfig().getString("UUIDList." + player.getUniqueId().toString()).equals(player.getName())) {
                playerUUID.getConfig().set("UUIDList." + player.getUniqueId().toString(), player.getName());
                playerUUID.saveConfig();
            }
        } else {
            playerUUID.getConfig().set("UUIDList." + player.getUniqueId().toString(), player.getName());
            playerUUID.saveConfig();
        }
    }

    public static String getPlayerName(String uuid) {
        return getFiles("playerUUID.yml").getConfig().getString("UUIDList." + uuid);
    }

    public static Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public static String getPlayerUUID(String playerName) {
        Files playerUUID = getFiles("playerUUID.yml");

        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equals(playerName)) {
                return uuid;
            }
        }
        return null;
    }

    public static ArrayList<String> getGlobalPlayerUUID(String playerName) {
        Files playerUUID = getFiles("playerUUID.yml");
        ArrayList<String> temp = new ArrayList<>();
        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equalsIgnoreCase(playerName)) {
                temp.add(uuid);
            }
        }
        return temp;
    }

    public static Player getPlayerExact(String playerName) {
        Files playerUUID = getFiles("playerUUID.yml");

        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equals(playerName)) {
                return Bukkit.getPlayer(UUID.fromString(uuid));
            }
        }
        return null;
    }

    public static ArrayList<Player> getGlobalPlayers(String playerName) {
        Files playerUUID = getFiles("playerUUID.yml");

        ArrayList<Player> playerList = new ArrayList<>();
        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equalsIgnoreCase(playerName)) {
                Player tmp = Bukkit.getPlayer(UUID.fromString(uuid));
                if (tmp != null) {
                    playerList.add(tmp);
                }
            }
        }
        return playerList;
    }
}
