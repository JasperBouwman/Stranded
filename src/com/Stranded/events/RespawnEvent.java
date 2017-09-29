package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;

public class RespawnEvent implements Listener, Runnable {

    private Main p;

    public RespawnEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onRespawn(PlayerRespawnEvent e) {

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("respawn.teleport");

        list.add(e.getPlayer().getName());
        p.getConfig().set("respawn.teleport", list);

        Bukkit.getScheduler().runTaskLater(p, this, 1);
    }

    @Override
    public void run() {

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("respawn.teleport");
        String player1 = list.get(0);
        list.remove(player1);
        p.getConfig().set("respawn.teleport", list);

        Player player = Bukkit.getPlayerExact(player1);

        if (player != null) {

            Files warData = new Files(p, "warData.yml");

            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(player.getName())) {
                    player.teleport((Location) warData.getConfig().get("war.war." + warID + ".blueSpawn"));
                    return;
                }
                if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(player.getName())) {
                    player.teleport((Location) warData.getConfig().get("war.war." + warID + ".redSpawn"));
                    return;
                }
            }

            if (p.getConfig().contains("island." + player.getName())) {

                Files islands = new Files(p, "islands.yml");
                String island = p.getConfig().getString("island." + player.getName());
                player.teleport((Location) islands.getConfig().get("island." + island + ".home"));
                return;
            }

            Files pluginData = new Files(p, "pluginData.yml");
            Location spawn = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(spawn);
        }

    }
}
