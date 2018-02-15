package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.Stranded.towers.RemoveTower.removeIslandTower;
import static com.Stranded.towers.RemoveTower.removeWarTower;
import static com.Stranded.GettingFiles.getFiles;

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
                    removeIslandTower(islands, island, towerID);
                    return;
                }

                Sign signNorth;
                Sign signSouth;
                try {
                    signNorth = (Sign) blockNorth.getState();
                } catch (ClassCastException cce) {
                    removeIslandTower(islands, island, towerID);
                    return;
                }
                if (!signNorth.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                    removeIslandTower(islands, island, towerID);
                    return;

                }
                try {
                    signSouth = (Sign) blockSouth.getState();
                } catch (ClassCastException cce) {
                    removeIslandTower(islands, island, towerID);
                    return;
                }
                if (!signSouth.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                    removeIslandTower(islands, island, towerID);
                    return;
                }


                String effect = islands.getConfig().getString("island." + island + ".towers." + towerID + ".type");

                Files config = getFiles("config.yml");

                if (effect.trim().equals("Tp")) {

                    Set<String> tmpList = islands.getConfig().getConfigurationSection("island." + island + ".towers." + towerID + ".players").getKeys(false);

                    for (String playerUUID : tmpList) {

                        Player tmpPlayer = Bukkit.getPlayer(UUID.fromString(playerUUID));
                        if (tmpPlayer == null) {
                            continue;
                        }
                        if (getNearbyPlayers(l).contains(tmpPlayer)) {
                            if (config.getConfig().getString("island." + playerUUID).equals(island)) {
                                long timeout = islands.getConfig().getLong("island." + island + ".towers." + towerID + ".players." + playerUUID + ".timeout");
                                if (timeout == 1) {
                                    tmpPlayer.sendMessage(ChatColor.BLUE + "TeleportTower ready");//todo add command
                                }
                                if (timeout != 0) {
                                    islands.getConfig().set("island." + island + ".towers." + towerID + ".players." + playerUUID + ".timeout", timeout - 1);
                                }
                            }
                        }
                    }

                    for (Player pl : getNearbyPlayers(l)) {
                        if (config.getConfig().getString("island." + pl.getUniqueId().toString()).equals(island)) {
                            if (!tmpList.contains(pl.getUniqueId().toString())) {
                                islands.getConfig().set("island." + island + ".towers." + towerID + ".players." + pl.getUniqueId().toString() + ".timeout",
                                        islands.getConfig().getLong("island." + island + ".towers." + towerID + ".timer"));
                            }
                        }
                    }

                    islands.saveConfig();
                    return;
                }

                // this will get the timeout
                int timeout = islands.getConfig().getInt("island." + island + ".towers." + towerID + ".timeout");
                // if the timeout is 1 the tower will give an effect
                if (timeout == 1) {

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
                        if (!config.getConfig().contains("island." + e.getUniqueId().toString())) {
                            if (closestEnemy == null) {
                                closestEnemy = e;
                            }
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())) {
                                closestEnemy = e;
                            }
                            continue;
                        }

                        if (closestFriendly == null) {
                            if (config.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestFriendly = e;
                            }
                        }
                        if (closestEnemy == null) {
                            if (!config.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestEnemy != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                    && !config.getConfig().getString("island." + e.getUniqueId().toString())
                                    .equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestFriendly != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                    && config.getConfig().getString("island." + e.getUniqueId().toString())
                                    .equals(island)) {
                                closestFriendly = e;
                            }
                        }
                    }
                    // is there is an closest player:
                    if (closestEnemy != null || closestFriendly != null) {

                        TowerEffectManager tem = new TowerEffectManager(p);

                        //towersData
                        String lvl = (signNorth.getLine(1).replace("Speed lvl: ", "")
                                .replace("Slow lvl: ", "").replace("Regen lvl: ", "")
                                .replace("Haste lvl: ", "").replace("Wither lvl: ", "")
                                .replace("Hunger lvl: ", "").replace("Tnt lvl: ", "")
                                .replace("Arrow lvl: ", "").replace("Tp lvl: ", ""));
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

        Files config = getFiles("config.yml");

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
                    removeWarTower(warData, warID, towerID, side);
                    return;
                }

                Sign signNorth;
                Sign signSouth;
                try {
                    signNorth = (Sign) blockNorth.getState();
                } catch (ClassCastException cce) {
                    removeWarTower(warData, warID, towerID, side);
                    return;
                }
                if (!signNorth.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                    removeWarTower(warData, warID, towerID, side);
                    return;
                }
                try {
                    signSouth = (Sign) blockSouth.getState();
                } catch (ClassCastException cce) {
                    removeWarTower(warData, warID, towerID, side);
                    return;
                }
                if (!signSouth.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                    removeWarTower(warData, warID, towerID, side);
                    return;

                }

                String island = config.getConfig().getString("island." + warData.getConfig().getString("war.war." + warID + ".towers." + side + "." + towerID + ".owner"));
                String effect = warData.getConfig().getString("war.war." + warID + ".towers." + side + "." + towerID + ".type");

                if (effect.trim().equals("Tp")) {

                    for (String playerUUID : warData.getConfig().getStringList("war.war." + warID + ".towers." + side + "." + towerID + ".players")) {

                        Player tmpPlayer = Bukkit.getPlayer(UUID.fromString(playerUUID));
                        if (tmpPlayer == null) {
                            continue;
                        }
                        if (getNearbyPlayers(l).contains(tmpPlayer)) {
                            if (config.getConfig().getString("island." + playerUUID).equals(island)) {
                                long timeout = warData.getConfig().getLong("war.war." + warID + ".towers." + side + "." + towerID + ".players." + playerUUID + ".timeout");
                                if (timeout == 1) {
                                    tmpPlayer.sendMessage("you can teleport now");//todo set colors
                                }
                                if (timeout != 0) {
                                    warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".players." + playerUUID + ".timeout", timeout - 1);
                                }
                            }
                        }
                    }
                    warData.saveConfig();
                    return;
                }

                // this will get the timeout
                int timeout = warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".timeout");
                // if the timeout is 1 the tower will give an
                // effect
                if (timeout == 1) {

                    warData.getConfig().set("war.war." + warID + ".towers." + side + "." + towerID + ".timeout",
                            warData.getConfig().getInt("war.war." + warID + ".towers." + side + "." + towerID + ".timer"));

//                String island = config.getConfig().getString(
//                        "island." + warData.getConfig().getStringList("war.war." + warID + "." + side + ".players").get(0));


                    // get all nearby players
                    List<Player> list = getNearbyPlayers(l);
                    // make some players
                    Player closestFriendly = null;
                    Player closestEnemy = null;
                    // get the closest player
                    for (Player e : list) {
                        if (!config.getConfig().contains("island." + e.getUniqueId().toString())) {
                            continue;
                        }
                        if (closestFriendly == null) {
                            if (config.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestFriendly = e;
                            }
                        }
                        if (closestEnemy == null) {
                            if (!config.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestEnemy != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                    && !config.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestEnemy = e;
                            }
                        }
                        if (closestFriendly != null) {
                            if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                    && config.getConfig().getString("island." + e.getUniqueId().toString()).equals(island)) {
                                closestFriendly = e;
                            }
                        }
                    }
                    // is there is an closest player:
                    if (closestEnemy != null || closestFriendly != null) {

                        TowerEffectManager tem = new TowerEffectManager(p);

                        //towersData
                        String lvl = (signNorth.getLine(1).replace("Speed lvl: ", "")
                                .replace("Slow lvl: ", "").replace("Regen lvl: ", "")
                                .replace("Haste lvl: ", "").replace("Wither lvl: ", "")
                                .replace("Hunger lvl: ", "").replace("Tnt lvl: ", "")
                                .replace("Arrow lvl: ", "").replace("Tp lvl: ", ""));

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
                            timeout - 1);
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
        Files islands = getFiles("islands.yml");
        Files warData = getFiles("warData.yml");

        if (!islands.getConfig().contains("island")) {
            return;
        }

        // get all islands
        for (String island : islands.getConfig().getConfigurationSection("island").getKeys(false)) {
            TowerLoopIsland(island, islands);
        }

        //get al wars
        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
            TowerLoopWar(warID, "blue", warData);
            TowerLoopWar(warID, "red", warData);
        }
    }
}
