package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;

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

        //island ignore

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (config.getConfig().contains("invited." + uuid + ".newIsland")) {

            Bukkit.getScheduler().cancelTask(config.getConfig().getInt("invited." + uuid + ".pendingID"));
            Main.reloadHolds -= 1;
            if (Main.reloadPending && Main.reloadHolds == 0) {
                Reload.reload(p);
            }

            if (Bukkit.getPlayerExact(config.getConfig().getString("invited." + uuid + ".inviter")) != null) {
                Bukkit.getPlayerExact(config.getConfig().getString("invited." + uuid + ".inviter"))
                        .sendMessage(ChatColor.DARK_RED + player.getName() + ChatColor.RED + " has ignored your inventation");
            }
            player.sendMessage(ChatColor.GREEN + "You ignored the invitation of the island " + config.getConfig().getString("invited." + uuid + ".newIsland"));

            config.getConfig().set("invited." + uuid, null);
            config.saveConfig();
            return;

        }
        player.sendMessage(ChatColor.RED + "You aren't invited for anything");


    }
}
