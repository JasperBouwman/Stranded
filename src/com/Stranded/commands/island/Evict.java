package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class Evict extends CmdManager {
    @Override
    public String getName() {
        return "evict";
    }

    @Override
    public String getAlias() {
        return "i";
    }

    @Override
    public void run(String[] args) {

        Files f = new Files(p, "islands.yml");

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }
        if (!f.getConfig().getString("island." + p.getConfig().getString("island." + player.getName()) + ".owner").equals(player.getName())) {
            player.sendMessage("you are not the owner of this island, so you can't evict someone");
            return;
        }

        ArrayList<String> list = (ArrayList<String>) f.getConfig().getStringList("island." + p.getConfig().getString("island." + player.getName()) + ".members");


        if (args.length == 2) {
            if (list.contains(args[1])) {
                list.remove(args[1]);
                f.getConfig().set("island." + p.getConfig().getString("island." + player.getName()) + ".members", list);
                f.saveConfig();

                player.sendMessage("player successfully evicted");

                if (Bukkit.getPlayerExact(args[1]) != null) {

                    Files pluginData = new Files(p, "pluginData.yml");
                    Location l = (Location) pluginData.getConfig().get("plugin.hub.location");

                    Bukkit.getPlayerExact(args[1]).sendMessage("you where removed by the owner from this island");
                    Bukkit.getPlayerExact(args[1]).teleport(l);
                } else {
                    ArrayList<String> listOnline = (ArrayList<String>) p.getConfig().getStringList("online.evict");
                    listOnline.add(args[1]);
                    p.getConfig().set("online.evict", listOnline);
                    p.saveConfig();
                }

            } else {
                player.sendMessage("this player isn't in your island");
            }
        } else {
            player.sendMessage("wrong use");
        }

    }
}