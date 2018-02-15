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
import java.util.Arrays;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class DeathEvent implements Listener {

    private Main p;

    public DeathEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void entityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Villager) {
            Files config = getFiles("config.yml");
            ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
            if (list.contains(e.getEntity().getUniqueId().toString())) {
                list.remove(e.getEntity().getUniqueId().toString());
                config.getConfig().set("nexus.uuid", list);
                config.saveConfig();
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void Death(PlayerDeathEvent e) {

        e.setDeathMessage("");

        Files warData = getFiles("warData.yml");
//        Files warIslands = getFiles("warIslands.yml");
        Files config = getFiles("config.yml");
        Player player = e.getEntity().getPlayer();
        String playerUUID = player.getUniqueId().toString();

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("playersInWar");

        if (list.contains(playerUUID)) {

            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

                for (String side : Arrays.asList("blue", "red")) {
                    if (warData.getConfig().getStringList("war.war." + warID + "." + side + ".players").contains(playerUUID)) {
                        String uuid = warData.getConfig().getString("war.war." + warID + "." + side + ".ArmorStandUUID");
                        ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(uuid));
                        if (as == null) {
                            return;
                        }
                        int health = Integer.parseInt(as.getName().replace("§9Health: ", ""));
                        health -= 3;
                        as.setCustomName("§9Health: " + health);

                        warData.getConfig().set("war.war." + warID + "." + side + ".killStreak." + player.getUniqueId().toString(), 0);

                        if (player.getKiller() != null) {//todo test this
                            Player killer = player.getKiller();
                            String killerUuid = killer.getUniqueId().toString();
                            if (warData.getConfig().getStringList("war.war." + warID + "." + invertSide(side) + ".players").contains(killerUuid)) {

                                int oldKillAmount = warData.getConfig().contains("war.war." + warID + "." + invertSide(side) + ".kills." + killerUuid)
                                        ? warData.getConfig().getInt("war.war." + warID + "." + invertSide(side) + ".kills." + killerUuid) : 0;
                                warData.getConfig().set("war.war." + warID + "." + invertSide(side) + ".kills." +
                                        killerUuid, warData.getConfig().getInt("war.war." + warID + "." + invertSide(side) + ".kills." + killerUuid) + 1);

                                int oldKillStreak = warData.getConfig().contains("war.war." + warID + "." + invertSide(side) + ".killStreak." + killerUuid)
                                        ? warData.getConfig().getInt("war.war." + warID + "." + invertSide(side) + ".killStreak." + killerUuid) : 0;
                                warData.getConfig().set("war.war." + warID + "." + invertSide(side) + ".killStreak." +
                                        killerUuid, warData.getConfig().getInt("war.war." + warID + "." + invertSide(side) + ".killStreak." + oldKillStreak) + 1);
                            }
                        }
                        warData.saveConfig();

                        if (health < 1) {
                            new EndWar().endWar(p, warID, 2, side);
                        }
                        return;
                    }
                }
            }
        }
    }

    private String invertSide(String side) {
        switch (side) {
            case "blue":
                return "red";
            case "red":
                return "blue";
            default:
                return side;
        }
    }

}
