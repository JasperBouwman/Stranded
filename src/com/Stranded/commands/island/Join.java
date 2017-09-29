package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Join extends CmdManager {

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        if (p.getConfig().contains("invited." + player.getName() + ".island")) {

            Files warData = new Files(p, "warData.yml");

            if (p.getConfig().getStringList("playersInWar").contains(player.getName())) {
                player.sendMessage("you can't join an island while you are in a war");
                return;
            }
            if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + player.getName()))) {
                if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + player.getName()) + ".players." + player.getName())) {
                    player.sendMessage("you can't join your island while you are pending for a war");
                    return;
                }
            }
            if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + player.getName()))) {
                if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + player.getName()) + ".players." + player.getName())) {
                    player.sendMessage("you can't join your island while you are pending for a war");
                    return;
                }
            }

            Files f = new Files(p, "islands.yml");

            String islandNew = p.getConfig().getString("invited." + player.getName() + ".newIsland");
            ArrayList<String> nw = (ArrayList<String>) f.getConfig().getStringList("island." + islandNew + ".members");
            player.sendMessage("you now joined " + islandNew);

            for (String s : nw) {
                if (Bukkit.getPlayerExact(s) != null) {
                    Bukkit.getPlayerExact(s).sendMessage(player.getName() + " joined your island");
                }
            }
            //todo remove all (towers, xp, inv)
            if (p.getConfig().contains("island." + player.getName())) {
                String islandOld = p.getConfig().getString("island." + player.getName());
                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
                if (old.contains(player.getName())) {
                    old.remove(player.getName());
                    f.getConfig().set("island." + islandOld + ".members", old);

                    for (String s : old) {
                        if (Bukkit.getPlayerExact(s) != null) {
                            Bukkit.getPlayerExact(s).sendMessage(player.getName() + " left your island");
                        }
                    }
                    if (old.size() == 0) {
                        f.getConfig().set("island." + islandOld, null);
                    }
                }
                p.getConfig().set("island." + player.getName(), islandNew);
            }
            ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("invitedList");
            list.remove(player.getName());
            p.getConfig().set("invitedList", list);
            p.saveConfig();
            Bukkit.getScheduler().cancelTask(f.getConfig().getInt("invited." + player.getName() + ".pendingID"));

            p.getConfig().set("invited." + player.getName(), null);
            p.getConfig().set("island." + player.getName(), islandNew);
            nw.add(player.getName());
            f.getConfig().set("island." + islandNew + ".members", nw);
            f.saveConfig();
            p.saveConfig();
            return;

        }
        player.sendMessage("you aren't invited for anything");
    }
}
