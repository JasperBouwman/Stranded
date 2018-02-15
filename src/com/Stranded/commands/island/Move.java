package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.border.islandBorder.BorderUtils.border2;

public class Move extends CmdManager {

    @Override
    public String getName() {
        return "move";
    }

    @Override
    public String getAlias() {
        return "m";
    }


    @Override
    public void run(String[] args, Player player) {

        //island move

        String uuidPlayer = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuidPlayer)) {
            player.sendMessage(ChatColor.RED + "You're not in an island");
            return;
        }

        if (border2(player.getLocation(), player)) {//todo test this
            player.sendMessage(ChatColor.RED + "You must be in your own island");
            return;
        }

        Files islands = getFiles("islands.yml");
        String island = config.getConfig().getString("island." + uuidPlayer);
        String uuid = islands.getConfig().getString("island." + island + ".UUID");

        Entity e = Bukkit.getEntity(UUID.fromString(uuid));
        if (e != null) {
            e.teleport(player);
            islands.getConfig().set("island." + island + ".home", e.getLocation());
            islands.saveConfig();
            return;
        }

        player.sendMessage(ChatColor.RED + "Your nexus is not found, this can happen when the nexus isn't in a rendered chunk, if this isn't the case contact a server moderator to spawn a new nexus");

    }
}
