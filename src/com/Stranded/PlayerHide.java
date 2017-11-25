package com.Stranded;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerHide {

    public PlayerHide(Main p) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            Files f = new Files(p, "islands.yml");
            if (p.getConfig().contains("island." + player.getUniqueId().toString())) {
                if (player.getWorld().getName().equals("Islands")) {
                    ArrayList<String> list = (ArrayList<String>) f.getConfig().getStringList("island." + p.getConfig().getString("island." + player.getUniqueId().toString()) + ".members");

                    for (Player player2 : Bukkit.getOnlinePlayers()) {
                        if (!list.contains(player2.getName())) {
                            player.hidePlayer(player2);
                        } else {
                            player.showPlayer(player2);
                        }
                    }
                } else {
                    for (Player player2 : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(player2);
                    }
                }
            } else if (player.getWorld().getName().equals("Islands")) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer(player2);
                }
            } else {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(player2);
                }
            }
        }
    }
}
