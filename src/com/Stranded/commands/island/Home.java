package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.Chat;
import com.Stranded.commands.CmdManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Home extends CmdManager {
    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getAlias() {
        return "h";
    }

    @Override
    public void run(String[] args, Player player) {

        //island home

        String uuid = player.getUniqueId().toString();

        if (!p.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You must be in an island to go to " + ChatColor.BLUE + "home");
            return;
        }

        if (p.getConfig().getStringList("playersInWar").contains(uuid)) {
            player.sendMessage(ChatColor.RED + "You can't go to " + ChatColor.BLUE + "home" + ChatColor.RED + " while you are in a war");
            return;
        }

        Files f = new Files(p, "islands.yml");
        Location l = (Location) f.getConfig().get("island." + p.getConfig().getString("island." + uuid) + ".home");

        player.teleport(l);
        player.sendMessage(ChatColor.GREEN + "Teleported to" + ChatColor.BLUE + " home");
    }
}
