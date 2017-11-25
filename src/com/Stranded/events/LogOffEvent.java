package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import com.Stranded.commands.war.util.EndWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class LogOffEvent implements Listener {

    private Main p;

    public LogOffEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void logoff(PlayerQuitEvent e) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard.scores(this.p, p);
        }
        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("respawn.teleport");

        if (list.contains(e.getPlayer().getUniqueId().toString())) {
            list.remove(e.getPlayer().getUniqueId().toString());
            p.getConfig().set("respawn.teleport", list);
            p.saveConfig();
        }

        Files warData = new Files(p, "warData.yml");
        Files warIslands = new Files(p, "warIslands.yml");
        Player player = e.getPlayer();

        ArrayList<String> playersInWar = (ArrayList<String>) p.getConfig().getStringList("playersInWar");
        if (playersInWar.contains(player.getUniqueId().toString())) {

            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(player.getUniqueId().toString())) {
                    EndWar.testPlayers(p, warID, "blue");
                    return;
                }
                if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(player.getUniqueId().toString())) {
                    EndWar.testPlayers(p, warID, "red");
                    return;
                }
            }
        }
    }
}
