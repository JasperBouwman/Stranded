package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Edit extends CmdManager {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        if (!player.hasPermission("Stranded.editWarIsland")) {
            player.sendMessage("you don't have permission");
            return;
        }

        Files f = new Files(p, "warIslands.yml");

        if (args.length == 2) {
            if (f.getConfig().contains("warIslands.island." + args[1])) {
                Location l = (Location) f.getConfig().get("warIslands.island." + args[1] + ".L2");
                l.setY(l.getBlockY() + 5);
                player.teleport(l);
                return;
            }
            player.sendMessage("this war island doesn't exist");
        } else {
            player.sendMessage("wrong use");
        }

    }
}
