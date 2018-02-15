package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.Chat;
import com.Stranded.commands.CmdManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.Stranded.GettingFiles.getFiles;

public class Home extends CmdManager {
    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getAlias() {
        return "h";
    }

    @Override
    public void run(String[] args, Player player) {

        //island home

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You must be in an island to go to " + ChatColor.BLUE + "home");
            return;
        }
        if (config.getConfig().contains("playersInWar")) {
            if (config.getConfig().getStringList("playersInWar").contains(uuid)) {
                player.sendMessage(ChatColor.RED + "You can't go to " + ChatColor.BLUE + "home" + ChatColor.RED + " while you are in a war");
                return;
            }
        }

        Files islands = getFiles("islands.yml");
        Location l = (Location) islands.getConfig().get("island." + config.getConfig().getString("island." + uuid) + ".home");

        player.teleport(l);
        player.sendMessage(ChatColor.GREEN + "Teleported to" + ChatColor.BLUE + " home");
    }
}
