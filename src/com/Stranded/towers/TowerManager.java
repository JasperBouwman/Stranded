package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TowerManager implements Runnable {

    private Main p;

    public TowerManager(Main main) {
        p = main;
    }

    private List<Player> getNearbyPlayers(Location l, int size) {

        List<Player> entities = new ArrayList<>();
        for (Player e : l.getWorld().getPlayers()) {
            if (l.distance(e.getLocation()) <= size) {
                entities.add(e);
            }
        }
        return entities;
    }

    @SuppressWarnings("deprecation")
    private void removeTower(Files f, String island, String id) {
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

    public void Tower() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, this, 20, 20);
    }

    @Override
    public void run() {
        Files f = new Files(p, "islands.yml");

        if (!f.getConfig().contains("island")) {
            return;
        }

        // get all islands
        for (String island : f.getConfig().getConfigurationSection("island").getKeys(false)) {
            // if the island doesn't own any towers this island will be
            // skipped
            if (f.getConfig().contains("island." + island + ".towers")) {
                // get all towers
                for (String id : f.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                    // get location of tower
                    Location l = (Location) f.getConfig()
                            .get("island." + island + ".towers." + id + ".location");
                    // get tower sign
                    Block b = new Location(l.getWorld(), l.getBlockX(), l.getBlockY() + 1, l.getBlockZ() - 2)
                            .getBlock();
                    Sign s;
                    // if tower doesn't has an sign the location will be
                    // removed, and this will get the tower type

                    if (!b.getType().equals(Material.WALL_SIGN)) {
                        removeTower(f, island, id);
                        return;
                    }

                    try {
                        s = (Sign) b.getState();
                    } catch (ClassCastException cce) {
                        removeTower(f, island, id);
                        return;
                    }

                    if (s.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                        if (!Bukkit.getWorld(b.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getType().equals(Material.WALL_SIGN)) {
                            removeTower(f, island, id);
                            return;
                        }
                    }

                    // this will get the timeout
                    int timeout = f.getConfig().getInt("island." + island + ".towers." + id + ".timeout");
                    // if the timeout is 1 the tower will give an
                    // effect
                    if (timeout == 1) {
                        // reset timeout

                        if (s.getLine(1).startsWith("Tnt")) {
                            if (s.getLine(1).endsWith("2")) {
                                int timer = 40;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("3")) {
                                int timer = 36;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("4")) {
                                int timer = 33;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("5")) {
                                int timer = 30;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("6")) {
                                int timer = 27;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("7")) {
                                int timer = 24;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("8")) {
                                int timer = 22;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            } else if (s.getLine(1).endsWith("MAX")) {
                                int timer = 20;
                                f.getConfig().set("island." + island + ".towers." + id + ".timer", timer);
                            }
                        }

                        f.getConfig().set("island." + island + ".towers." + id + ".timeout",
                                f.getConfig().getInt("island." + island + ".towers." + id + ".timer"));

                        // get all nearby players
                        List<Player> list = getNearbyPlayers(l, 10);
                        // make some players
                        Player closestFriendly = null;
                        Player closestEnemy = null;
                        // get the closest friendly player
                        for (Player e : list) {
                            if (closestFriendly == null) {
                                if (p.getConfig().getString("island." + e.getName()).equals(island)) {
                                    closestFriendly = e;
                                }
                            } else {
                                if (l.distance(e.getLocation()) <= l.distance(closestFriendly.getLocation())
                                        && p.getConfig().getString("island." + e.getName())
                                        .equals(island)) {
                                    closestFriendly = e;
                                }
                            }
                        }
                        // get closest enemy player
                        for (Player e : list) {
                            if (closestEnemy == null) {
                                if (!p.getConfig().getString("island." + e.getName()).equals(island)) {
                                    closestEnemy = e;
                                }
                            } else {
                                if (l.distance(e.getLocation()) <= l.distance(closestEnemy.getLocation())
                                        && !p.getConfig().getString("island." + e.getName())
                                        .equals(island)) {
                                    closestEnemy = e;
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
                        f.getConfig().set("island." + island + ".towers." + id + ".timeout",
                                f.getConfig().getInt("island." + island + ".towers." + id + ".timeout")
                                        - 1);
                    }
                    f.saveConfig();
                }
            }
        }
    }
}