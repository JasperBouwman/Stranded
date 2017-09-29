package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Ignore extends CmdManager {
    @Override
    public String getName() {
        return "ignore";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        if (p.getConfig().contains("invited." + player.getName() + ".island")) {
            Files f = new Files(p, "islands.yml");
            ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("invitedList");
            list.remove(player.getName());
            p.getConfig().set("invitedList", list);

            Bukkit.getScheduler().cancelTask(f.getConfig().getInt("invited." + player.getName() + ".pendingID"));

            if (Bukkit.getPlayerExact(p.getConfig().getString("invited." + player + ".inviter")) != null) {
                Bukkit.getPlayerExact(p.getConfig().getString("invited." + player + ".inviter")).sendMessage(player + " has ignored your inventation");
            }
            player.sendMessage("you ignored the inventation of the island " + p.getConfig().getString("invited." + player + ".newIsland"));

            p.getConfig().set("invited." + player, null);
            p.saveConfig();
            return;

        }
        player.sendMessage("you aren't invited for anything");


    }
}
