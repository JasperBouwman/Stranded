package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("your aren't in an island");
            return;
        }
        Files f = new Files(p, "islands.yml");
        Location l = (Location) f.getConfig().get("island." + p.getConfig().getString("island." + player.getName()) + ".home");

        player.teleport(l);
        player.sendMessage("teleported");

    }
}
