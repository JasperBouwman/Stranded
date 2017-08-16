package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created on 06-Jul-17.
 */
public class Leave extends CmdManager {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in a island");
            return;
        }

        Files f = new Files(p, "islands.yml");

        String islandOld = p.getConfig().getString("island." + player.getName());
        ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
        if (old.contains(player.getName())) {
            p.getConfig().set("island." + player.getName(), null);
            p.saveConfig();
            old.remove(player.getName());
            f.getConfig().set("island." + islandOld + ".members", old);

            for (String s : old) {
                if (Bukkit.getPlayerExact(s) != null) {
                    Bukkit.getPlayerExact(s).sendMessage(player.getName() + " left your island");
                }
            }

            Files pluginData = new Files(p, "pluginData.yml");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(l);

            if (old.size() == 0) {
                f.getConfig().set("island." + islandOld, null);
                f.saveConfig();
                player.sendMessage("you have left " + islandOld + " as last one, so this island is now deleted (not from the world)");
                return;
            }
            player.sendMessage("you have left " + islandOld);
            f.saveConfig();

        } else {
            player.sendMessage("you aren't in a island");
        }

    }
}
