package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.war.util.EndWar;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.UUID;

public class DeathEvent implements Listener {

    private Main p;

    public DeathEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void entityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Villager) {
            Bukkit.broadcastMessage("test");
            ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");
            Bukkit.broadcastMessage("test " + e.getEntity().getUniqueId());
            if (list.contains(e.getEntity().getUniqueId().toString())) {
                Bukkit.broadcastMessage("test2");
                list.remove(e.getEntity().getUniqueId().toString());
                p.getConfig().set("nexus.uuid", list);
                p.saveConfig();
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void Death(PlayerDeathEvent e) {

        e.setDeathMessage("");

        Files warData = new Files(p, "warData.yml");
        Files warIslands = new Files(p, "warIslands.yml");
        Player player = e.getEntity().getPlayer();

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("playersInWar");
        if (list.contains(player.getName())) {
            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(player.getName())) {
                    String uuid = warData.getConfig().getString("war.war." + warID + ".blue.ArmorStandUUID");
                    ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(uuid));
                    if (as == null) {
                        return;
                    }
                    int health = Integer.parseInt(as.getName().replace("§9Health: ", ""));
                    health -= 2;
                    as.setCustomName("§9Health: " + health);
                    if (health < 1) {
                        new EndWar().endWar(p, warID, 2, "blue");
                    }
                    return;
                }
                if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(player.getName())) {
                    String uuid = warData.getConfig().getString("war.war." + warID + ".red.ArmorStandUUID");
                    ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(uuid));
                    if (as == null) {
                        return;
                    }
                    int health = Integer.parseInt(as.getName().replace("§cHealth: ", ""));
                    health -= 2;
                    as.setCustomName("§cHealth: " + health);
                    if (health < 1) {
                        new EndWar().endWar(p, warID, 2, "red");
                    }
                    return;
                }
            }
        }
    }
}
