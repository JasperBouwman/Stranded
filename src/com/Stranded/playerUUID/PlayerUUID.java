package com.Stranded.playerUUID;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerUUID {

    public static void setPlayerUUID(Player player, Main p) {

        Files playerUUID = new Files(p, "playerUUID.yml");

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

    public static String getPlayerName(UUID uuid, Main p) {
        Files playerUUID = new Files(p, "playerUUID.yml");
        return playerUUID.getConfig().getString("UUIDList." + uuid.toString());
    }

    public static String getPlayerName(String uuid, Main p) {
        Files playerUUID = new Files(p, "playerUUID.yml");
        return playerUUID.getConfig().getString("UUIDList." + uuid);
    }

    public static Player getPlayer(UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }

    public static String getPlayerUUID(String playerName, Main p) {
        Files playerUUID = new Files(p, "playerUUID.yml");

        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equals(playerName)) {
                return uuid;
            }
        }
        return null;
    }

    public static ArrayList<String> getGlobalPlayerUUID(String playerName, Main p) {
        Files playerUUID = new Files(p, "playerUUID.yml");
        ArrayList<String> temp = new ArrayList<>();
        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equalsIgnoreCase(playerName)) {
                temp.add(uuid);
            }
        }
        return temp;
    }

    public static Player getPlayerExact(String playerName, Main p) {
        Files playerUUID = new Files(p, "playerUUID.yml");

        for (String uuid : playerUUID.getConfig().getConfigurationSection("UUIDList").getKeys(false)) {
            if (playerUUID.getConfig().getString("UUIDList." + uuid).equals(playerName)) {
                return Bukkit.getPlayer(UUID.fromString(uuid));
            }
        }
        return null;
    }

    public static ArrayList<Player> getGlobalPlayers(String playerName, Main p) {
        Files playerUUID = new Files(p, "playerUUID.yml");

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


//    //get player uuid
//    String PlayerUUID = PlayerUUID.getPlayerUUID(playerName, p);
//
//    //test if player is found
//    if (PlayerUUID == null) {
//        ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[1], p);
//        if (tempPlayers.size() > 1) {
//            //more players found with name
//            return;
//        } else if (tempPlayers.size() == 0) {
//            //no players found with name
//            return;
//        } else {
//            //set full on player uuid
//            PlayerUUID = tempPlayers.get(0);
//        }
//    }
//
//    //test if player is found (not really necessary)
//    if (PlayerUUID == null) {
//        return;
//    }
//
//    //get final player name (case sensitive)
//    String evictPlayerName = PlayerUUID.getPlayerName(PlayerUUID, p);
//
//    //get player
//    Player evictPlayer = PlayerUUID.getPlayerExact(player name, p);
//    Player pl = PlayerUUID.getPlayer(UUID.fromString(PlayerName));
//
//    //get offline player uuid
//    String offlineUUID = PlayerUUID.getPlayerUUID(PlayerName, p);


}
