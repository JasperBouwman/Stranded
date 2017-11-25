package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEvent implements Listener {

    private Main p;

    public RespawnEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onRespawn(PlayerRespawnEvent e) {

        Main.reloadHolds += 1;

        Bukkit.getScheduler().runTaskLater(p, () -> {

            Player player = e.getPlayer();

            if (player != null) {

                String uuid = player.getUniqueId().toString();

                Scoreboard.scores(p, player);

                Files warData = new Files(p, "warData.yml");

                for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                    if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(uuid)) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".blueSpawn"));
                        return;
                    }
                    if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(uuid)) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".redSpawn"));
                        return;
                    }
                }

                if (p.getConfig().contains("island." + uuid)) {

                    Files islands = new Files(p, "islands.yml");
                    String island = p.getConfig().getString("island." + uuid);
                    player.teleport((Location) islands.getConfig().get("island." + island + ".home"));
                    return;
                }

                Files pluginData = new Files(p, "pluginData.yml");
                Location spawn = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(spawn);
            }

            Main.reloadHolds -= 1;
            if (Main.reloadPending && Main.reloadHolds == 0) {
                Reload.reload(p);
            }

        }, 1);
    }
}
