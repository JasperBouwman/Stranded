package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Confirm extends CmdManager {
    @Override
    public String getName() {
        return "confirm";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        if (args.length == 1) {
            ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("deleteIsland");
            if (list.contains(player.getName())) {

                Files pluginData = new Files(p, "pluginData.yml");
                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                list.remove(player.getName());
                p.getConfig().set("deleteIsland", list);

                Files f = new Files(p, "islands.yml");

                ArrayList<String> list1 = (ArrayList<String>) f.getConfig().getStringList("island." + p.getConfig().getString("island." + player.getName()) + ".members");

                //todo remove all (towers, xp, inv)

                for (String s : list1) {
                    if (Bukkit.getPlayerExact(s) != null && !s.equals(player.getName())) {
                        Bukkit.getPlayerExact(s).sendMessage("you island is removed by the owner");

                        player.teleport(l);

                    } else if (!s.equals(player.getName())) {
                        ArrayList<String> listOnline = (ArrayList<String>) p.getConfig().getStringList("online.removedIsland");
                        listOnline.add(s);
                        p.getConfig().set("online.removedIsland", listOnline);
                        p.saveConfig();
                    }
                }

                player.sendMessage("island is removed");
                player.teleport(l);

                f.getConfig().set("island." + p.getConfig().getString("island." + player.getName()), null);
                f.saveConfig();

            } else {
                player.sendMessage("no island delete pending");
            }

        } else {
            player.sendMessage("wrong use");
        }
    }
}
