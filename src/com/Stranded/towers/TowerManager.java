package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.material.Mushroom;

import java.util.ArrayList;
import java.util.List;

public class TowerManager implements Runnable {

    private Main p;

    public TowerManager(Main main) {
        p = main;
    }

    @SuppressWarnings("deprecation")
    public static void removeWarTower(Files warData, String warID, String towerID, String side) {
        //todo give tower
        Location main = (Location) warData.getConfig().get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
        Location L1 = new Location(main.getWorld(), main.getX() + 1, main.getY(), main.getZ() + 2);
        Location L2 = new Location(main.getWorld(), main.getX() - 1, main.getY() + 4, main.getZ() - 2);

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int blockCount = 0;

        // set all blocks in this region to air
        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {
                    Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(xx, yy, zz);
                    byte by = (byte) warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".terrain." + blockCount + ".data");
                    block.setTypeIdAndData(warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".terrain." + blockCount + ".type"), by, true);
                    blockCount++;
                }
            }
        }

        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID, null);
        warData.saveConfig();
    }

    private List<Player> getNearbyPlayers(Location l) {
        int size = 10;
        List<Player> entities = new ArrayList<>();
        for (Player e : l.getWorld().getPlayers()) {
            if (l.distance(e.getLocation()) <= size) {
                entities.add(e);
            }
        }
        return entities;
    }

    private List<Entity> getNearbyEntities(Location l) {
        int size = 10;
        List<Entity> mobs = new ArrayList<>();

        for (Entity e : l.getWorld().getEntities()) {
            if (e instanceof Cow
                    || e instanceof Sheep
                    || e instanceof Pig
                    || e instanceof Chicken
                    || e instanceof Rabbit

                    || e instanceof Zombie
                    || e instanceof Creeper
                    || e instanceof Enderman
                    || e instanceof Evoker
                    || e instanceof Ghast
                    || e instanceof Skeleton
                    || e instanceof Spider
                    || e instanceof Witch

                    || e instanceof Player) {
                if (l.distance(e.getLocation()) <= size) {
                    mobs.add(e);
                }
            }
        }

        return mobs;
    }

    @SuppressWarnings("deprecation")
    private void removeIslandTower(Files f, String island, String id) {
        //todo give tower
        Location main = (Location) f.getConfig().get("island." + island + ".towers." + id + ".location");
        Location L1 = new Location(main.getWorld(), main.getX() + 1, main.getY(), main.getZ() + 2);
        Location L2 = new Location(main.getWorld(), main.getX() - 1, main.getY() + 4, main.getZ() - 2);

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int blockCount = 0;

        // set all blocks in this region to air
        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {
                    Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(xx, yy, zz);
                    byte by = (byte) f.getConfig().getInt("island." + island + ".towers." + id + ".terrain." + blockCount + ".data");
                    block.setTypeIdAndData(f.getConfig().getInt("island." + island + ".towers." + id + ".terrain." + blockCount + ".type"), by, true);
                    blockCount++;
                }
            }
        }

        f.getConfig().set("island." + island + ".towers." + id, null);
        f.saveConfig();
    }

    private void TowerLoopIsland(String island, Files islands) {
        // if the island doesn't own any towers this island will be
        // skipped
        if (islands.getConfig().contains("island." + island + ".towers")) {
            // get all towers
            for (String towerID : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                // get location of tower
                Location l = (Location) islands.getConfig()
                        .get("island." + island + ".towers." + towerID + ".location");
                // get tower sign
                Block b = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2)
                        .getBlock();
                Sign s;
                // if tower doesn't has an sign the location will be
                // removed, and this will get the tower type

                if (!b.getType().equals(Material.WALL_SIGN)) {
                    removeIslandTower(islands, island, towerID);
                    return;
                }

                try {
                    s = (Sign) b.getState();
                } catch (ClassCastException cce) {
                    removeIslandTower(islands, island, towerID);
                    return;
                }

                if (s.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                    if (!Bukkit.getWorld(b.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getType().equals(Material.WALL_SIGN)) {
                        removeIslandTower(islands, island, towerID);
                        return;
                    }
                }

                // this will get the timeout
                int timeout = islands.getConfig().getInt("island." + island + ".towers." + towerID + ".timeout");
                // if the timeout is 1 the tower will give an
                // effect
                if (timeout == 1) {
                    // reset timeout

                    if (s.getLine(1).startsWith("Tnt")) {
                        if (s.getLine(1).endsWith("2")) {
                            int timer = 40;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("3")) {
                            int timer = 36;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("4")) {
                            int timer = 33;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("5")) {
                            int timer = 30;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("6")) {
                            int timer = 27;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("7")) {
                            int timer = 24;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("8")) {
                            int timer = 22;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("MAX")) {
                            int timer = 20;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        }
                    }
                    //reset timeout
                    islands.getConfig().set("island." + island + ".towers." + towerID + ".timeout",
                            islands.getConfig().getInt("island." + island + ".towers." + towerID + ".timer"));

                    // get all nearby players
                    List<Entity> list = getNearbyEntities(l);
                    // make some players
                    Entity closestFriendly = null;
                    Entity closestEnemy = null;
                    // get the closest player
                    for (Entity e : list) {
                        if (!p.getConfig().contains("island." + e.getName())) {

                            if (closestFriendly == null) {

                                closestFriendly = e;
                            }
                            if (closestEnemy == null) {
                                closestEnemy = e;
                            }
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())) {
                                closestEnemy = e;
                            }

                            if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())) {
                                closestFriendly = e;
                            }
                            continue;
                        }

                        if (closestFriendly == null) {
                            if (p.getConfig().getString("island." + e.getName()).equals(island)) {
                                closestFriendly = e;
                            }
                        }
                        if (closestEnemy == null) {
                            if (!p.getConfig().getString("island." + e.getName()).equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestEnemy != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                    && !p.getConfig().getString("island." + e.getName())
                                    .equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestFriendly != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                    && p.getConfig().getString("island." + e.getName())
                                    .equals(island)) {
                                closestFriendly = e;
                            }
                        }

                    }
                    // is there is an closest player:
                    if (closestEnemy != null || closestFriendly != null) {

                        TowerEffectManager tem = new TowerEffectManager(p);
                        // get tower type
                        String effect = (s.getLine(1).replaceAll(" lvl: ", "").replaceAll("MAX", ""));
                        for (int i = 1; i <= 9; i++) {
                            StringBuilder str = new StringBuilder();

                            effect = effect.replaceAll(str.append(i).toString(), "");
                        }
                        // get tower lvl
                        String lvl = (s.getLine(1).replaceAll("Speed lvl: ", "")
                                .replaceAll("Slow lvl: ", "").replaceAll("Regen lvl: ", "")
                                .replaceAll("Haste lvl: ", "").replaceAll("Wither lvl: ", "")
                                .replaceAll("Hunger lvl: ", "").replaceAll("Tnt lvl: ", "")
                                .replaceAll("Arrow lvl: ", ""));
                        int lvlInt = 0;
                        try {
                            lvlInt = Integer.parseInt(lvl);
                        } catch (NumberFormatException nfe) {
                            //
                        }

                        //todo can be removed at some point done testing
                        if (closestFriendly != null) {
                            closestFriendly.sendMessage(effect + " friendly");
                        }
                        if (closestEnemy != null) {
                            closestEnemy.sendMessage(effect + " enemy");
                        }

                        // setup the effects
                        tem.Effects(effect, lvlInt, closestEnemy, closestFriendly, l);

                    }

                } else {
                    // remove a second from the timeout
                    islands.getConfig().set("island." + island + ".towers." + towerID + ".timeout",
                            islands.getConfig().getInt("island." + island + ".towers." + towerID + ".timeout")
                                    - 1);
                }
                islands.saveConfig();
            }
        }
    }

    private void TowerLoopWar(String warID, String side, Files warData) {
        // if the island doesn't own any towers this island will be
        // skipped
        if (warData.getConfig().contains("war.war." + warID + ".towers." + side)) {
            // get all towers
            for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
                // get location of tower
                Location l = (Location) warData.getConfig()
                        .get("war.war." + warID + ".towers." + side + "." + towerID + ".location");
                // get tower sign
                Block b = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2).getBlock();
                Sign s;
                // if tower doesn't has an sign the location will be
                // removed, and this will get the tower type
                if (!b.getType().equals(Material.WALL_SIGN)) {
                    removeWarTower(warData, warID, towerID, side);
                    return;
                }

                try {
                    s = (Sign) b.getState();
                } catch (ClassCastException cce) {
                    removeWarTower(warData, warID, towerID, side);
                    return;
                }

                if (s.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                    if (!Bukkit.getWorld(b.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getType().equals(Material.WALL_SIGN)) {
                        removeWarTower(warData, warID, towerID, side);
                        return;
                    }
                }

                // this will get the timeout
                int timeout = warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".timeout");
                // if the timeout is 1 the tower will give an
                // effect
                if (timeout == 1) {
                    // reset timeout

                    if (s.getLine(1).startsWith("Tnt")) {
                        if (s.getLine(1).endsWith("2")) {
                            int timer = 40;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("3")) {
                            int timer = 36;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("4")) {
                            int timer = 33;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("5")) {
                            int timer = 30;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("6")) {
                            int timer = 27;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("7")) {
                            int timer = 24;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("8")) {
                            int timer = 22;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        } else if (s.getLine(1).endsWith("MAX")) {
                            int timer = 20;
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                        }
                    }

                    warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timeout",
                            warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".timer"));

                    String island = p.getConfig().getString(
                            "island." + warData.getConfig().getStringList("war.war." + warID + "." + side + ".players").get(0));

                    // get all nearby players
                    List<Player> list = getNearbyPlayers(l);
                    // make some players
                    Player closestFriendly = null;
                    Player closestEnemy = null;
                    // get the closest player
                    for (Player e : list) {
                        if (!p.getConfig().contains("island." + e.getName())) {
                            continue;
                        }

                        if (closestFriendly == null) {
                            if (p.getConfig().getString("island." + e.getName()).equals(island)) {
                                closestFriendly = e;
                            }
                        }
                        if (closestEnemy == null) {
                            if (!p.getConfig().getString("island." + e.getName()).equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestEnemy != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                    && !p.getConfig().getString("island." + e.getName())
                                    .equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestFriendly != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                    && p.getConfig().getString("island." + e.getName())
                                    .equals(island)) {
                                closestFriendly = e;
                            }
                        }

                    }
                    // is there is an closest player:
                    if (closestEnemy != null || closestFriendly != null) {

                        TowerEffectManager tem = new TowerEffectManager(p);
                        // get tower type
                        String effect = (s.getLine(1).replaceAll(" lvl: ", "").replaceAll("MAX", ""));
                        for (int i = 1; i <= 9; i++) {
                            StringBuilder str = new StringBuilder();

                            effect = effect.replaceAll(str.append(i).toString(), "");
                        }
                        // get tower lvl
                        String lvl = (s.getLine(1).replaceAll("Speed lvl: ", "")
                                .replaceAll("Slow lvl: ", "").replaceAll("Regen lvl: ", "")
                                .replaceAll("Haste lvl: ", "").replaceAll("Wither lvl: ", "")
                                .replaceAll("Hunger lvl: ", "").replaceAll("Tnt lvl: ", "")
                                .replaceAll("Arrow lvl: ", ""));
                        int lvlInt = 0;
                        try {
                            lvlInt = Integer.parseInt(lvl);
                        } catch (NumberFormatException nfe) {
                            //
                        }

                        //todo can be removed at some point done testing
                        if (closestFriendly != null) {
                            closestFriendly.sendMessage(effect + " friendly");
                        }
                        if (closestEnemy != null) {
                            closestEnemy.sendMessage(effect + " enemy");
                        }

                        // setup the effects
                        tem.Effects(effect, lvlInt, closestEnemy, closestFriendly, l);

                    }

                } else {
                    // remove a second from the timeout
                    warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timeout",
                            warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".timeout")
                                    - 1);
                }
                warData.saveConfig();
            }
        }
    }

    public void Tower() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, this, 20, 20);
    }

    @Override
    public void run() {
        Files islands = new Files(p, "islands.yml");
        Files warData = new Files(p, "warData.yml");

        if (!islands.getConfig().contains("island")) {
            return;
        }

        // get all islands
        for (String island : islands.getConfig().getConfigurationSection("island").getKeys(false)) {
            TowerLoopIsland(island, islands);
        }

        //get al wars
        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
            TowerLoopWar(warID, "red", warData);
            TowerLoopWar(warID, "blue", warData);
        }
    }
}
