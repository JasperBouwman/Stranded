package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.islandBorder.BorderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

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

        if (!p.getConfig().contains("island." + uuidPlayer)) {
            player.sendMessage(ChatColor.RED + "You're not in an island");
            return;
        }

        if (BorderUtils.border(player.getLocation(), p, player)) {
            player.sendMessage(ChatColor.RED + "You must be in your own island");
            return;
        }

        Files f = new Files(p, "islands.yml");
        String island = p.getConfig().getString("island." + uuidPlayer);
        String uuid = f.getConfig().getString("island." + island + ".UUID");

        Entity e = Bukkit.getEntity(UUID.fromString(uuid));
        if (e != null) {
            e.teleport(player);
            f.getConfig().set("island." + island + ".home", e.getLocation());
            f.saveConfig();
            return;
        }

        player.sendMessage(ChatColor.RED + "Your nexus is not found, this can happen when the nexus isn't in a rendered chunk, if this isn't the case contact a server moderator to spawn a new nexus");

    }
}
