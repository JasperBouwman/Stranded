package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

        Files f = new Files(p, "islands.yml");

        if (args.length != 2) {
            player.sendMessage("wrong use");
            return;
        }

        if (p.getConfig().getStringList("playersInWar").contains(player.getName())) {
            player.sendMessage("you can't visit an island while you are in a war");
            return;
        }

        if (f.getConfig().contains("island." + args[1])) {
            Location l = (Location) f.getConfig().get("island." + args[1] + ".home");

            player.teleport(l);
            player.sendMessage("you are now on the island " + args[1]);
        } else {
            player.sendMessage("this island doesn't exist");
        }

    }
}
