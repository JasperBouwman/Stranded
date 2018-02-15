package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.Stranded.GettingFiles.getFiles;

public class Visit extends CmdManager {
    @Override
    public String getName() {
        return "visit";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //island visit <island name>

        Files islands = getFiles("islands.yml");
        Files config = getFiles("config.yml");

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Usage: /island visit <island>");
            return;
        }
        if (config.getConfig().contains("playersInWar")) {
            if (config.getConfig().getStringList("playersInWar").contains(player.getUniqueId().toString())) {
                player.sendMessage(ChatColor.RED + "You can't visit an island while you are in a war");
                return;
            }
        }

        if (islands.getConfig().contains("island." + args[1])) {
            Location l = (Location) islands.getConfig().get("island." + args[1] + ".home");

            player.teleport(l);
            player.sendMessage(ChatColor.GREEN + "You are now on the island " + args[1]);
        } else {
            player.sendMessage(ChatColor.RED + "This island doesn't exist");
        }

    }
}
