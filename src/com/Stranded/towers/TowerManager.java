package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;

import static com.Stranded.towers.RemoveTower.removeIslandTower;
import static com.Stranded.towers.RemoveTower.removeWarTower;

public class TowerManager implements Runnable {

    private Main p;

    public TowerManager(Main main) {
        p = main;
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
                Block blockNorth = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2)
                        .getBlock();
                Block blockSouth = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2)
                        .getBlock();

                // if tower doesn't has an sign the location will be
                // removed, and this will get the tower type
                if (!blockNorth.getType().equals(Material.WALL_SIGN) && !blockSouth.getType().equals(Material.WALL_SIGN)) {
                    removeIslandTower(p, islands, island, towerID);
                    return;
                }

                Sign signNorth;
                Sign signSouth;
                try {
                    signNorth = (Sign) blockNorth.getState();
                } catch (ClassCastException cce) {
                    removeIslandTower(p, islands, island, towerID);
                    return;
                }
                if (!signNorth.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                    removeIslandTower(p, islands, island, towerID);
                    return;

                }
                try {
                    signSouth = (Sign) blockSouth.getState();
                } catch (ClassCastException cce) {
                    removeIslandTower(p, islands, island, towerID);
                    return;
                }
                if (!signSouth.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                    removeIslandTower(p, islands, island, towerID);
                    return;
                }

                // this will get the timeout
                int timeout = islands.getConfig().getInt("island." + island + ".towers." + towerID + ".timeout");
                // if the timeout is 1 the tower will give an effect
                if (timeout == 1) {
                    // reset timeout

                    if (signNorth.getLine(1).startsWith("Tnt")) {
                        if (signNorth.getLine(1).endsWith("2")) {
                            int timer = 40;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("3")) {
                            int timer = 36;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("4")) {
                            int timer = 33;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("5")) {
                            int timer = 30;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("6")) {
                            int timer = 27;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("7")) {
                            int timer = 24;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("8")) {
                            int timer = 22;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        } else if (signNorth.getLine(1).endsWith("MAX")) {
                            int timer = 20;
                            islands.getConfig().set("island." + island + ".towers." + towerID + ".timer", timer);
                        }

                        islands.saveConfig();
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
                        if (!p.getConfig().contains("island." + e.getUniqueId().toString())) {

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
                            if (p.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestFriendly = e;
                            }
                        }
                        if (closestEnemy == null) {
                            if (!p.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestEnemy != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                    && !p.getConfig().getString("island." + e.getUniqueId().toString())
                                    .equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestFriendly != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                    && p.getConfig().getString("island." + e.getUniqueId().toString())
                                    .equals(island)) {
                                closestFriendly = e;
                            }
                        }

                    }
                    // is there is an closest player:
                    if (closestEnemy != null || closestFriendly != null) {

                        TowerEffectManager tem = new TowerEffectManager(p);
                        // get tower type
                        String effect = (signNorth.getLine(1).replace(" lvl: ", "").replace("MAX", ""));
                        for (int i = 1; i <= 9; i++) {
                            StringBuilder str = new StringBuilder();

                            effect = effect.replace(str.append(i).toString(), "");
                        }
                        // get tower lvl
                        String lvl = (signNorth.getLine(1).replace("Speed lvl: ", "")
                                .replace("Slow lvl: ", "").replace("Regen lvl: ", "")
                                .replace("Haste lvl: ", "").replace("Wither lvl: ", "")
                                .replace("Hunger lvl: ", "").replace("Tnt lvl: ", "")
                                .replace("Arrow lvl: ", ""));
                        int lvlInt = Tower.MAX_UPGRADE;
                        try {
                            lvlInt = Integer.parseInt(lvl);
                        } catch (NumberFormatException ignore) {
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
                // get tower sign
                Block blockNorth = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2)
                        .getBlock();
                Block blockSouth = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() + 2)
                        .getBlock();

                // if tower doesn't has an sign the location will be
                // removed, and this will get the tower type
                if (!blockNorth.getType().equals(Material.WALL_SIGN) && !blockSouth.getType().equals(Material.WALL_SIGN)) {
                    removeWarTower(p, warData, warID, towerID, side, "full");
                    return;
                }

                Sign signNorth;
                Sign signSouth;
                try {
                    signNorth = (Sign) blockNorth.getState();
                } catch (ClassCastException cce) {
                    removeWarTower(p, warData, warID, towerID, side, "full");
                    return;
                }
                if (!signNorth.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                    removeWarTower(p, warData, warID, towerID, side, "full");
                    return;

                }
                try {
                    signSouth = (Sign) blockSouth.getState();
                } catch (ClassCastException cce) {
                    removeWarTower(p, warData, warID, towerID, side, "full");
                    return;
                }
                if (!signSouth.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                    removeWarTower(p, warData, warID, towerID, side, "full");
                    return;

                }


            // this will get the timeout
            int timeout = warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".timeout");
            // if the timeout is 1 the tower will give an
            // effect
            if (timeout == 1) {
                // reset timeout

                if (signNorth.getLine(1).startsWith("Tnt")) {
                    if (signNorth.getLine(1).endsWith("2")) {
                        int timer = 40;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("3")) {
                        int timer = 36;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("4")) {
                        int timer = 33;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("5")) {
                        int timer = 30;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("6")) {
                        int timer = 27;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("7")) {
                        int timer = 24;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("8")) {
                        int timer = 22;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    } else if (signNorth.getLine(1).endsWith("MAX")) {
                        int timer = 20;
                        warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timer", timer);
                    }
                    warData.saveConfig();
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
                    if (!p.getConfig().contains("island." + e.getUniqueId().toString())) {
                        continue;
                    }

                    if (closestFriendly == null) {
                        if (p.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                            closestFriendly = e;
                        }
                    }
                    if (closestEnemy == null) {
                        if (!p.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                            closestEnemy = e;
                        }
                    }
                    if (closestEnemy != null) {
                        if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                && !p.getConfig().getString("island." + e.getUniqueId().toString())
                                .equals(island)) {
                            closestEnemy = e;
                        }
                    }
                    if (closestFriendly != null) {
                        if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                && p.getConfig().getString("island." + e.getUniqueId().toString())
                                .equals(island)) {
                            closestFriendly = e;
                        }
                    }

                }
                // is there is an closest player:
                if (closestEnemy != null || closestFriendly != null) {

                    TowerEffectManager tem = new TowerEffectManager(p);
                    // get tower type
                    String effect = (signNorth.getLine(1).replace(" lvl: ", "").replace("MAX", ""));
                    for (int i = 1; i <= 9; i++) {
                        StringBuilder str = new StringBuilder();

                        effect = effect.replace(str.append(i).toString(), "");
                    }
                    // get tower lvl
                    String lvl = (signNorth.getLine(1).replace("Speed lvl: ", "")
                            .replace("Slow lvl: ", "").replace("Regen lvl: ", "")
                            .replace("Haste lvl: ", "").replace("Wither lvl: ", "")
                            .replace("Hunger lvl: ", "").replace("Tnt lvl: ", "")
                            .replace("Arrow lvl: ", ""));

                    int lvlInt = Tower.MAX_UPGRADE;
                    try {
                        lvlInt = Integer.parseInt(lvl);
                    } catch (NumberFormatException ignore) {
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
