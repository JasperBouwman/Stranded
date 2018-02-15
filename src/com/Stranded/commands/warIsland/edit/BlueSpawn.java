package com.Stranded.commands.warIsland.edit;

import com.Stranded.Files;
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

import java.util.UUID;

import static com.Stranded.commands.warIsland.edit.RedSpawn.spawnArmorstand;
import static com.Stranded.commands.warIsland.edit.RedSpawn.spawnNexus;
import static com.Stranded.GettingFiles.getFiles;

public class BlueSpawn extends CmdManager {
    @Override
    public String getName() {
        return "bluespawn";
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

            warIslands.saveConfig();

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
            //test if blueSpawn is in warIsland
            if (redX > maxX || redX < minX || redY > maxY || redY < minY || redZ > maxZ || redZ < minZ) {
                player.sendMessage("your red spawn is not in your selected war island");
                r = true;
            }
            if (r) return;

            //test if blueSpawn (plus space for nexus) is in warIsland
            Location blueTmp = l.clone();
            for (int i = 0; i < 2; i++) {
                if (!blueTmp.getBlock().getType().equals(Material.AIR)) {
                    player.sendMessage("the 2 blocks above your blue spawn must be air so there could be spawn a Nexus");
                    return;
                }
                blueTmp.setY(blueTmp.getBlockY() + 1);
            }

            if (blueTmp.getBlockY() - 1 > maxY) {
                player.sendMessage("the place for the blue nexus is not in your selected war island");
                r = true;
            }
            if (r) return;

            Entity blueVillager = Bukkit.getEntity(UUID.fromString(warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".nexus.blue")));
            ArmorStand armorStand = (ArmorStand) Bukkit.getEntity(UUID.fromString(warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".armorStand.blue")));
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
                Location blueSpawn = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.blue");
                armorStand = spawnArmorstand(blueSpawn, "§9");
                warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".armorStand.blue", armorStand.getUniqueId().toString());
            }

            Vector v = Create.getVector(l, redVillager.getLocation());

            l.setDirection(v);

            blueVillager.teleport(l);

            l.setY(l.getBlockY() + 1);
            armorStand.teleport(l);

            l.setY(l.getBlockY() - 1);

            warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".spawn.blue", l);
            warIslands.saveConfig();

            l = redVillager.getLocation();
            l.setDirection(v.multiply(-1));
            redVillager.teleport(l);

            player.sendMessage("the spawn has been edited");
        } else {
            player.sendMessage("wrong use");
        }
    }
}
