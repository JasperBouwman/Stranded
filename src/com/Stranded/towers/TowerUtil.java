package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.UUID;

public class TowerUtil {

    public static boolean testTowerLocationFromTower(Location l, Main p) {

        //true == to close
        int maxDistance = 10;

        Files islands = new Files(p, "islands.yml");
        Files warData = new Files(p, "warData.yml");

        for (String island : islands.getConfig().getConfigurationSection("island").getKeys(false)) {
            if (islands.getConfig().contains("island." + island + ".towers")) {
                // get all towers
                for (String towerID : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    Location loc = (Location) islands.getConfig()
                            .get("island." + island + ".towers." + towerID + ".location");
                    if (l.distance(loc) < maxDistance) {
                        return true;
                    }
                }
            }
        }

        String side = "blue";
        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
            if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
                // get all towers
                for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
                    // get location of tower
                    Location loc = (Location) warData.getConfig()
                            .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
                    if (l.distance(loc) < maxDistance) {
                        return true;
                    }
                }
            }
        }

        side = "red";
        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
            if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
                // get all towers
                for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
                    // get location of tower
                    Location loc = (Location) warData.getConfig()
                            .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
                    if (l.distance(loc) < maxDistance) {
                        return true;
                    }
                }
            }
        }

        return false;

    }

    public static boolean testTowerLocationFromNexus(Location l, Main p) {

        //true == to close
        int maxDistance = 10;

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");

        for (String uuid : list) {

            Entity e = Bukkit.getEntity(UUID.fromString(uuid));
            if (e != null) {
                if (e.getWorld() == l.getWorld()) {
                    if (e instanceof Villager) {
                        Villager v = (Villager) e;
                        if (l.distance(v.getLocation()) < maxDistance) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
