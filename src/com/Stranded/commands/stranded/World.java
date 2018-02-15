package com.Stranded.commands.stranded;

import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.Stranded.Permissions.hasPermission;
import static com.Stranded.Permissions.noPermMessage;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class World extends CmdManager {
    @Override
    public String getName() {
        return "world";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //stranded world <world>
        //stranded world <world> <X> <Y> <Z>

        if (!hasPermission(player, "Stranded.worldTP")) {
            return;
        }

        if (args.length == 2) {
            try {
                player.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
                player.sendMessage(ChatColor.BLUE + "Teleported to the spawn of the world " + args[1]);
            } catch (NullPointerException ignore) {
                player.sendMessage(ChatColor.RED + "This world doesn't exist");
            }
        } else if (args.length == 5) {

            int x = 0;
            int y = 0;
            int z = 0;
            boolean r = false;
            try {
                x = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + args[2] + " isn't a number");
                r = true;
            }
            try {
                y = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + args[3] + " isn't a number");
                r = true;
            }
            try {
                z = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + args[4] + " isn't a number");
                r = true;
            }
            if (r) return;

            try {
                player.teleport(new Location(Bukkit.getWorld(args[1]), x, y, z));
                player.sendMessage(String.format(ChatColor.GREEN + "Teleported to %o, %o, %o", x, y ,z));
            } catch (NullPointerException ignore) {
                player.sendMessage(ChatColor.RED + "This world doesn't exist");
            }

        } else {
            sendWrongUse(player, "/stranded world", new String[]{"/stranded world <x> <y> <z>", "/stranded world "});
        }

    }
}
