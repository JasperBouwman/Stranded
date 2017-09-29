package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.war.util.EndWar;
import com.Stranded.commands.war.util.WarUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.UUID;

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
        ArrayList<String> listNewOwner = (ArrayList<String>) p.getConfig().getStringList("online.newOwner");
        Player player = e.getPlayer();
        if (listRemoved.contains(player.getName())) {
            player.sendMessage("you island is removed by the owner");

            Files pluginData = new Files(p, "pluginData.yml");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(l);

            listRemoved.remove(player.getName());
            p.getConfig().set("online.removedIsland", listRemoved);
            p.saveConfig();
            return;
        }
        if (listEvict.contains(player.getName())) {
            player.sendMessage("you where removed by the owner from this island");

            Files pluginData = new Files(p, "pluginData.yml");
            Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(l);

            listEvict.remove(player.getName());
            p.getConfig().set("online.evict", listEvict);
            p.saveConfig();
            return;
        }

        if (listNewOwner.contains(player.getName())) {
            player.sendMessage("you are now the new owner if your island");
            listNewOwner.remove(player.getName());
            p.getConfig().set("online.newOwner", listNewOwner);
            p.saveConfig();
        }

        if (p.getConfig().contains("online.warWin" + player.getName())) {
            String msg = p.getConfig().getString("online.warWin." + player.getName());
            player.sendMessage(msg);
            p.getConfig().set("online.warWin." + player.getName(), null);

            Files islands = new Files(p, "islands.yml");
            Location l = (Location) islands.getConfig().get("island." + p.getConfig().getStringList("island." + player.getName()) + ".home");
            player.teleport(l);
        }
        if (p.getConfig().contains("online.warLose" + player.getName())) {
            String msg = p.getConfig().getString("online.warLose." + player.getName());
            player.sendMessage(msg);
            p.getConfig().set("online.warLose." + player.getName(), null);

            Files islands = new Files(p, "islands.yml");
            Location l = (Location) islands.getConfig().get("island." + p.getConfig().getStringList("island." + player.getName()) + ".home");
            player.teleport(l);
        }

        if (p.getConfig().getStringList("playersInWar").contains(player.getName())) {
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
        }


    }
}
