package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Create extends CmdManager {
    public static Vector getVector(Location blue, Location red) {
        return red.toVector().subtract(blue.toVector());
    }

    static String spawnNexus(Location l, Vector vector, Main p) {

        l.setY(l.getY() - 1);

        Villager v = l.getWorld().spawn(l, Villager.class);

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");
        list.add(v.getUniqueId().toString());
        p.getConfig().set("nexus.uuid", list);
        p.saveConfig();

        v.setCustomNameVisible(false);
        v.setProfession(Villager.Profession.NITWIT);
        v.setAI(false);
        v.isSilent();
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);

        l.setDirection(vector);

        v.teleport(l);
        return v.getUniqueId().toString();
    }

    public static String spawnArmorstand(Location l, String color) {
        l.setX(l.getX() + 0.5);
        l.setZ(l.getZ() + 0.5);
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

        return a.getUniqueId().toString();

    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        Files warIslands = new Files(p, "warIslands.yml");

        //warIsland create <theme> <min> <max>

        if (args.length != 4) {
            if (args.length == 1) {
                player.sendMessage("use /warIsland create <theme> <min> <max> for creating a new war island");
            } else if (args.length == 2) {
                player.sendMessage("you didn't gave a name, min players and max players");
            } else if (args.length == 3) {
                player.sendMessage("you didn't gave a min players and max players");
            } else if (args.length > 4) {
                player.sendMessage("you gave to many arguments, usage: /warIsland create <theme> <min> <max> for creating a new war island");
            }
            return;
        }

        String uuid = player.getUniqueId().toString();

        boolean r = false;
        //test if player has offset
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid)) {
            player.sendMessage("you don't have an valid offset for a war Island, missing: blue Spawn and red Spawn, use /warIsland wand to get a regen selector");
            return;
        }
        //test if player has blueSpawn offset
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".blueSpawn")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: blue Spawn");
            r = true;
        }
        //test if player has redSpawn offset
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".redSpawn")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: red Spawn");
            r = true;
        }
        //test if player had first offset
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".first")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: first pos");
            r = true;
        }
        //test if player has second offset
        if (!warIslands.getConfig().contains("warIslands.offset." + uuid + ".second")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: second pos");
            r = true;
        }
        if (r) return;


        Location blueSpawn = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".blueSpawn");
        Location redSpawn = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".redSpawn");
        Location first = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".first");
        Location second = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".second");

        int maxX = Math.max(first.getBlockX(), second.getBlockX());
        int maxY = Math.max(first.getBlockY(), second.getBlockY());
        int maxZ = Math.max(first.getBlockZ(), second.getBlockZ());
        int minX = Math.min(first.getBlockX(), second.getBlockX());
        int minY = Math.min(first.getBlockY(), second.getBlockY());
        int minZ = Math.min(first.getBlockZ(), second.getBlockZ());

        int blueX = blueSpawn.getBlockX();
        int blueY = blueSpawn.getBlockY();
        int blueZ = blueSpawn.getBlockZ();
        int redX = redSpawn.getBlockX();
        int redY = redSpawn.getBlockY();
        int redZ = redSpawn.getBlockZ();

        //test if blueSpawn is in warIsland
        if (blueX > maxX || blueX < minX || blueY > maxY || blueY < minY || blueZ > maxZ || blueZ < minZ) {
            player.sendMessage("your blue spawn is not in your selected war island");
            r = true;
        }
        //test if redSpawn is in warIsland
        if (redX > maxX || redX < minX || redY > maxY || redY < minY || redZ > maxZ || redZ < minZ) {
            player.sendMessage("your red spawn is not in your selected war island");
            r = true;
        }
        if (r) return;

        //test if blueSpawn (plus space for nexus) is in warIsland
        Location blueTmp = blueSpawn.clone();
        for (int i = 0; i < 2; i++) {
            blueTmp.setY(blueTmp.getBlockY() + 1);
            if (!blueTmp.getBlock().getType().equals(Material.AIR)) {
                player.sendMessage("the 2 blocks above your blue spawn must be air so there could be spawn a Nexus");
                return;
            }
        }
        //test if redSpawn (plus space for nexus) is in warIsland
        Location redTmp = redSpawn.clone();
        for (int i = 0; i < 2; i++) {
            redTmp.setY(redTmp.getBlockY() + 1);
            if (!redTmp.getBlock().getType().equals(Material.AIR)) {
                player.sendMessage("the 2 blocks above your red spawn must be air so there could be spawn a Nexus");
                return;
            }
        }

        if (blueTmp.getBlockY() > maxY) {
            player.sendMessage("the place for the blue nexus is not in your selected war island");
            r = true;
        }
        if (redTmp.getBlockY() > maxY) {
            player.sendMessage("the place for the red nexus is not in your selected war island");
            r = true;
        }
        if (r) return;

        World blueWorld = blueSpawn.getWorld();
        World redWorld = redSpawn.getWorld();
        World firstWorld = first.getWorld();
        World secondWorld = second.getWorld();
        World warWorld = Bukkit.getWorld("War");

        if (!blueWorld.equals(warWorld)) {
            player.sendMessage("your blue spawn isn't in the world war");
            r = true;
        }
        if (!redWorld.equals(warWorld)) {
            player.sendMessage("your blue spawn isn't in the world war");
            r = true;
        }
        if (!firstWorld.equals(warWorld)) {
            player.sendMessage("your first pos isn't in the world war");
            r = true;
        }
        if (!secondWorld.equals(warWorld)) {
            player.sendMessage("your second pos isn't in the world war");
            r = true;
        }
        if (r) return;


        //test theme for normal name
        if (Main.containsSpecialCharacter(args[1])) {
            player.sendMessage("you can't use a special character in you war island theme");
            return;
        }

        //test min and max players value
        int max = 0;
        int min = 0;
        try {
            max = Integer.parseInt(args[3]);
        } catch (NumberFormatException nfe) {
            player.sendMessage("the max value isn't a number");
            r = true;
        }
        try {
            min = Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            player.sendMessage("the min value isn't a number");
            r = true;
        }
        if (r) return;
        if (max < min) {
            player.sendMessage("the max value is smaller than the min value, this isn't right");
            r = true;
        }
        if (min <= 0) {
            player.sendMessage("your min value can't be smaller than 0");
            r = true;
        }
        if (r) return;

        if (args[1].equalsIgnoreCase("random")) {
            player.sendMessage("please do not use this theme name, this is used for players to choose a random theme");
            return;
        }

        int id = 0;
        while (true) {
            if (!warIslands.getConfig().contains("warIslands.island." + args[1] + "." + id)) {
                break;
            } else {
                id++;
            }
        }

        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".spawn.blue", blueSpawn);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".spawn.red", redSpawn);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".islandSize.first", first);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".islandSize.second", second);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".maxPlayers", max);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".minPlayers", min);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".inUse", false);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".armorStand.blue", spawnArmorstand(blueSpawn, "§9"));
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".armorStand.red", spawnArmorstand(redSpawn, "§c"));

        Vector v = getVector(blueSpawn, redSpawn);

        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".nexus.blue", spawnNexus(blueSpawn, v, p));
        v = v.multiply(-1);
        warIslands.getConfig().set("warIslands.island." + args[1] + "." + id + ".nexus.red", spawnNexus(redSpawn, v, p));

        warIslands.getConfig().set("warIslands.offset." + uuid, null);

        warIslands.saveConfig();

    }

}
