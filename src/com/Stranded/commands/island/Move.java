package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.islandBorder.BorderUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

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

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("your aren't in an island");
            return;
        }

        if (new BorderUtils().border(player.getLocation(), p, player)) {
            player.sendMessage("you must be in your own island");
            return;
        }

        Files f = new Files(p, "islands.yml");
        String island = p.getConfig().getString("island." + player.getName());
        String UUID = f.getConfig().getString("island." + island + ".UUID");

        for (Entity e : player.getWorld().getEntities()) {
            if (e instanceof Villager && UUID.equals(e.getUniqueId().toString())) {
                e.teleport(player);
                f.getConfig().set("island." + island + ".home", e.getLocation());
                f.saveConfig();
                return;
            }
        }

        player.sendMessage("your nexus is not found, this can happen when the nexus isn't in a rendered chunk");

    }
}
