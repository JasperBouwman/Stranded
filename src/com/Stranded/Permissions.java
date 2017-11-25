package com.Stranded;

import org.bukkit.entity.Player;

public class Permissions {

    public static boolean hasPermission(Player player, String permission) {
        if (player.hasPermission(permission)) {
            return true;
        } else {
            player.sendMessage("you don't have permission to do this");
            return false;
        }
    }

}
