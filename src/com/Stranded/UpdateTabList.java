package com.Stranded;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UpdateTabList {

    private ArrayList<String> onlineIslandPlayers = new ArrayList<>();
    private OfflinePlayer[] otherOfflinePlayers = Bukkit.getOfflinePlayers();
    private ArrayList<String> offlineIslandPlayers = new ArrayList<>();
    private ArrayList<String> otherPlayers = new ArrayList<>();
    private PlayerList list;

    public UpdateTabList(Player player, Main p) {
        Files islands = new Files(p, "islands.yml");
        list = PlayerList.getPlayerList(player);

        String island = p.getConfig().getString("island." + player.getName());
        ArrayList<String> islandPlayers = (ArrayList<String>) islands.getConfig().getStringList("island." + island + ".members");

        for (String players : islandPlayers) {
            if (Bukkit.getPlayerExact(players) == null) {
                offlineIslandPlayers.add(players);
            } else {
                onlineIslandPlayers.add(players);
            }
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            otherPlayers.add(players.getName());
        }
        otherPlayers.removeAll(onlineIslandPlayers);

        if (islandPlayers.contains(player.getName())) {
            islandAction();
        } else {
            serverAction();
        }

    }

    private void islandAction() {

        new Thread(() -> {

            list.updateSlot(0, "Your island");
            for (int i = 1; i < onlineIslandPlayers.size(); i++) {
                list.addExistingPlayer(i, ChatColor.GREEN + onlineIslandPlayers.get(i),
                        Bukkit.getOfflinePlayer(Bukkit.getPlayerExact(onlineIslandPlayers.get(i)).getUniqueId()));
            }
            for (int i = onlineIslandPlayers.size(); i < offlineIslandPlayers.size() + onlineIslandPlayers.size(); i++) {
//            list.addExistingPlayer(i, onlineIslandPlayers.get(i),
//                  Bukkit.getOfflinePlayer(Bukkit.getOfflinePlayer(offlineIslandPlayers.get(i)).getUniqueId()));
                list.updateSlot(i, ChatColor.RED + offlineIslandPlayers.get(i - onlineIslandPlayers.size()) + " " + i);
            }
            for (int i = offlineIslandPlayers.size() + onlineIslandPlayers.size(); i < 19; i++) {
                list.updateSlot(i, "");
            }
        }).start();
    }

    private void serverAction() {
        new Thread(() -> {

            list.updateSlot(20, "offline players");
            for (int i = 21; i < otherPlayers.size(); i++) {
                list.addExistingPlayer(i, ChatColor.DARK_GREEN + otherPlayers.get(i), Bukkit.getOfflinePlayer(Bukkit.getPlayerExact(otherPlayers.get(i)).getUniqueId()));
            }
            for (int i = 21 + otherPlayers.size(); i < otherPlayers.size() + otherOfflinePlayers.length + 21; i++) {
                list.updateSlot(i, ChatColor.DARK_RED + otherOfflinePlayers[i - 21 - otherPlayers.size()].getName() + " " + i);
            }
            for (int i = otherPlayers.size() + otherOfflinePlayers.length + 21; i < 80; i++) {
                list.updateSlot(i, "");
            }
        }).start();
    }
}
