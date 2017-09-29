package com.Stranded.commands.warIsland;

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

        if (args.length == 3) {
            if (f.getConfig().contains("warIslands.motherIsland.island." + args[1])) {
                if (f.getConfig().contains("warIslands.motherIsland.island." + args[1] + "." + args[2])) {
                    Location l = (Location) f.getConfig().get("warIslands.island." + args[1] + "." + args[2] + ".spawn.blue");
                    player.teleport(l);
                } else {
                    player.sendMessage("this war id doesn't exist");
                }
            } else {
                player.sendMessage("this war theme doesn't exist");
            }
        } else {
            player.sendMessage("wrong use");
        }

    }
}
