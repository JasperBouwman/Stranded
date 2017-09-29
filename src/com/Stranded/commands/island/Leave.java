package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

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

        Files warData = new Files(p, "warData.yml");

        if (p.getConfig().getStringList("playersInWar").contains(player.getName())) {
            player.sendMessage("you can't visit an island while you are in a war");
            return;
        }
        if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + player.getName()))) {
            if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + player.getName()) + ".players." + player.getName())) {
                player.sendMessage("you can't leave your island while you are pending for a war");
                return;
            }
        }
        if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + player.getName()))) {
            if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + player.getName()) + ".players." + player.getName())) {
                player.sendMessage("you can't leave your island while you are pending for a war");
                return;
            }
        }

        //todo remove all (towers, xp, inv)
        Files f = new Files(p, "islands.yml");

        String islandOld = p.getConfig().getString("island." + player.getName());
        ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
        if (old.contains(player.getName())) {
            p.getConfig().set("island." + player.getName(), null);
            p.saveConfig();
            old.remove(player.getName());
            f.getConfig().set("island." + islandOld + ".members", old);

            if (f.getConfig().getString("island." + islandOld + ".owner").equals(player.getName())) {

                String newOwner = old.get(new Random().nextInt(old.size()));
                if (Bukkit.getPlayerExact(newOwner) != null) {
                    Bukkit.getPlayerExact(newOwner).sendMessage("you are the new owner of " + islandOld);
                } else {
                    ArrayList<String> newOwnerList = (ArrayList<String>) p.getConfig().getStringList("online.newOwner");
                    newOwnerList.add(newOwner);
                    p.getConfig().set("online.newOwner", newOwnerList);
                }
                f.getConfig().set("island." + islandOld + ".owner", newOwner);
            }

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
