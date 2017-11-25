package com.Stranded.events;

import com.Stranded.Main;
import com.Stranded.PlayerHide;
import com.Stranded.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class TeleportEvent implements Listener {

    private Main p;

    public TeleportEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void teleportEvent(PlayerTeleportEvent e) {
        Scoreboard.scores(p, e.getPlayer());
        Bukkit.getScheduler().runTaskLater(p, () -> new PlayerHide(p), 1);
    }
}
