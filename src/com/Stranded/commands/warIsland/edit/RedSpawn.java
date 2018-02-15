package com.Stranded.commands.warIsland.edit;

import static com.Stranded.GettingFiles.getFiles;
import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.warIsland.Create;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.commands.warIsland.Create.spawnArmorstand;
import static com.Stranded.worldGeneration.IslandGeneration.spawnNexus;

public class RedSpawn extends CmdManager {
    @Override
    public String getName() {
        return "redspawn";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        //warIsland edit <theme> <war island ID> blueSpawn

        Files warIslands = getFiles("warIslands.yml");

        if (args.length == 4) {

            String theme = args[1];
            String warIslandID = args[2];

            if (!warIslands.getConfig().contains("warIslands.island." + theme)) {
                player.sendMessage("this war theme doesn't exist");
                return;
            }

            if (!warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID)) {
                player.sendMessage("this war island id doesn't exist");
                return;
            }

            Location l = player.getLocation().clone();
            l.setX(l.getBlockX() + 0.5);
            l.setZ(l.getBlockZ() + 0.5);

            Location first = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".islandSize.first");
            Location second = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".islandSize.second");

            int maxX = Math.max(first.getBlockX(), second.getBlockX());
            int maxY = Math.max(first.getBlockY(), second.getBlockY());
            int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
            int minX = Math.min(first.getBlockX(), second.getBlockX());
            int minY = Math.min(first.getBlockY(), second.getBlockY());
            int minZ = Math.min(first.getBlockZ(), second.getBlockZ());

            int redX = l.getBlockX();
            int redY = l.getBlockY();
            int redZ = l.getBlockZ();

            boolean r = false;
            //test if redSpawn is in warIsland
            if (redX > maxX || redX < minX || redY > maxY || redY < minY || redZ > maxZ || redZ < minZ) {
                player.sendMessage("your red spawn is not in your selected war island");
                r = true;
            }
            if (r) return;

            //test if redSpawn (plus space for nexus) is in warIsland
            Location redTmp = l.clone();
            for (int i = 0; i < 2; i++) {
                if (!redTmp.getBlock().getType().equals(Material.AIR)) {
                    player.sendMessage("the 2 blocks above your red spawn must be air so there could be spawn a Nexus");
                    return;
                }
                redTmp.setY(redTmp.getBlockY() + 1);
            }

            if (redTmp.getBlockY() - 1 > maxY) {
                player.sendMessage("the place for the red nexus is not in your selected war island");
                r = true;
            }
            if (r) return;

            Entity blueVillager = Bukkit.getEntity(UUID.fromString(warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".nexus.blue")));
            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".armorStand.red")));
            Villager redVillager = (Villager) Bukkit.getEntity(UUID.fromString(warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".nexus.red")));

            if (blueVillager == null) {
                Location blueSpawn = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.blue");
                blueVillager = spawnNexus(blueSpawn);
                warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".nexus.blue", blueVillager.getUniqueId().toString());
            }
            if (redVillager == null) {
                Location redSpawn = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.red");
                redVillager = spawnNexus(redSpawn);
                warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".nexus.red", redVillager.getUniqueId().toString());
            }
            if (armorStand == null) {
                Location redSpawn = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.red");
                armorStand = spawnArmorstand(redSpawn, "§c");
                warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".armorStand.red", armorStand.getUniqueId().toString());
            }

            Vector v = Create.getVector(l, blueVillager.getLocation());

            l.setDirection(v);

            redVillager.teleport(l);

            l.setY(l.getBlockY() + 1);
            armorStand.teleport(l);

            l.setY(l.getBlockY() - 1);

            warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".spawn.red", l);
            warIslands.saveConfig();

            l = blueVillager.getLocation();
            l.setDirection(v.multiply(-1));
            blueVillager.teleport(l);

            player.sendMessage("the spawn has been edited");
        } else {
            player.sendMessage("wrong use");
        }
    }

    static ArmorStand spawnArmorstand(Location l, String color) {
        l.setY(l.getY() + 2);

        ArmorStand a = l.getWorld().spawn(l, ArmorStand.class);

        a.setCustomName(color + "Health: 20");
        a.setArms(false);
        a.setBasePlate(false);
        a.setSmall(true);
        a.setVisible(false);
        a.setCanPickupItems(false);
        a.setGravity(false);
        a.setInvulnerable(true);
        a.setCustomNameVisible(true);

        return a;

    }

    static Villager spawnNexus(Location l) {

        Villager v = l.getWorld().spawn(l, Villager.class);
        Files config = getFiles("config.yml");

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
        list.add(v.getUniqueId().toString());
        config.getConfig().set("nexus.uuid", list);
        config.saveConfig();

        v.setCustomNameVisible(true);
        v.setProfession(Villager.Profession.NITWIT);
        v.setAI(false);
        v.isSilent();
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);

        return v;
    }
}
