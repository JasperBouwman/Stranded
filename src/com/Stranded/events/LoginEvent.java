package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;

public class LoginEvent implements Listener {

    private Main p;

    public LoginEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onOnline(PlayerLoginEvent e) {
        ArrayList<String> listRemoved = (ArrayList<String>) p.getConfig().getStringList("online.removedIsland");
        ArrayList<String> listEvict = (ArrayList<String>) p.getConfig().getStringList("online.evict");
        if (listRemoved.contains(e.getPlayer().getName())) {
            e.getPlayer().sendMessage("you island is removed by the owner");

            Files pluginData = new Files(p, "pluginData");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            e.getPlayer().teleport(l);

            listRemoved.remove(e.getPlayer().getName());
            p.getConfig().set("online.removedIsland", listRemoved);
        }
        if (listEvict.contains(e.getPlayer().getName())) {
            e.getPlayer().sendMessage("you where removed by the owner from this island");

            Files pluginData = new Files(p, "pluginData");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            e.getPlayer().teleport(l);

            listRemoved.remove(e.getPlayer().getName());
            p.getConfig().set("online.evict", listEvict);
        }



    }
}
