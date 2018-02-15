package com.Stranded.events;

import static com.Stranded.GettingFiles.getFiles;
import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import com.Stranded.commands.war.util.EndWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.UUID;

public class DamageEvent implements Listener {

    private Main p;

    public DamageEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityDamage(EntityDamageEvent e) {
        Files config = getFiles("config.yml");
        ArrayList<String> listNexus = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
        if (listNexus.contains(e.getEntity().getUniqueId().toString()) && e.getEntity() instanceof Villager) {
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Player) {
            Scoreboard.scores((Player) e.getEntity());
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityHit(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player) {
            Scoreboard.scores((Player) e.getEntity());
            return;
        }
        Files config = getFiles("config.yml");

        ArrayList<String> listNexus = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");

        if (listNexus.contains(e.getEntity().getUniqueId().toString()) && e.getEntity() instanceof Villager) {

            if (!(e.getDamager() instanceof Player)) {
                return;
            }
            Player player = (Player) e.getDamager();
            e.setCancelled(true);

            Villager v = (Villager) e.getEntity();
            if (!v.getWorld().equals(Bukkit.getWorld("War"))) {
                return;
            }
            Files warData = getFiles("warData.yml");
            Files warIslands = getFiles("warIslands.yml");
            ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("playersInWar");

            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

                String blueVillagerUUID = warData.getConfig().getString("war.war." + warID + ".blue.VillagerUUID");
                String redVillagerUUID = warData.getConfig().getString("war.war." + warID + ".red.VillagerUUID");
                String blueArmorStandUUID = warData.getConfig().getString("war.war." + warID + ".blue.armorStandUUID");
                String redArmorStandUUID = warData.getConfig().getString("war.war." + warID + ".red.ArmorStandUUID");

                if (v.getUniqueId().toString().equals(blueVillagerUUID)) {
                    ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(blueArmorStandUUID));
                    if (as == null) {
                        return;
                    }
                    ArrayList<String> redPlayers = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".red.players");
                    if (redPlayers.contains(player.getName())) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".redSpawn"));
                        int health = Integer.parseInt(as.getName().replace("§9Health: ", ""));
                        health -= 1;
                        as.setCustomName("§9Health: " + health);
                        if (health < 1) {
                            new EndWar().endWar(p, warID, 2, "blue");
                        }
                        return;
                    }
                    return;
                }
                if (v.getUniqueId().toString().equals(redVillagerUUID)) {
                    ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(redArmorStandUUID));
                    if (as == null) {
                        return;
                    }
                    ArrayList<String> redPlayers = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".blue.players");
                    if (redPlayers.contains(player.getName())) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".blueSpawn"));
                        int health = Integer.parseInt(as.getName().replace("§cHealth: ", ""));
                        health -= 1;
                        as.setCustomName("§cHealth: " + health);
                        if (health < 1) {
                            new EndWar().endWar(p, warID, 2, "red");
                        }
                        return;
                    }
                    return;
                }
            }
        }
    }
}
