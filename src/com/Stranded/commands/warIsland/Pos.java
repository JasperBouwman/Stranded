package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static com.Stranded.commands.warIsland.Wand.wand;

public class Pos extends CmdManager {

    public static void showBoundary(Main p) {
        Files warIslands = new Files(p, "warIslands.yml");

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player == null) {
                continue;
            }

            ItemStack is = player.getInventory().getItemInMainHand();

            if (!is.getType().equals(Material.STICK)) {
                continue;
            }

            if (!is.hasItemMeta() || !is.getItemMeta().hasLore() || !is.getItemMeta().hasDisplayName()) {
                continue;
            }

            if (!is.getItemMeta().getDisplayName().equals("WarIsland Boundary Shower")) {
                continue;
            }

            ItemMeta im = is.getItemMeta();

            String theme;
            int warIslandID;

            if (im.getLore().size() != 2) {
                continue;
            }
            theme = im.getLore().get(0).replace("Theme: ", "");
            warIslandID = Integer.parseInt(im.getLore().get(1).replace("WarIslandID: ", ""));

            if (!warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID)) {
                continue;
            }

            int range = 100;
            if (warIslands.getConfig().contains("warIslands.showOffset.range")) {
                range = warIslands.getConfig().getInt("warIslands.showOffset.range");
            }

            if (warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID + ".islandSize.first") && warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID + ".islandSize.second")) {
                int finalRange = range;

                new Thread(() -> {

                    Location first = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".islandSize.first");
                    Location second = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".islandSize.second");

                    if (player.getWorld().equals(second.getWorld()) && player.getWorld().equals(first.getWorld())) {

                        double minX = Math.min(first.getBlockX(), second.getBlockX());
                        double minY = Math.min(first.getBlockY(), second.getBlockY());
                        double minZ = Math.min(first.getBlockZ(), second.getBlockZ());
                        double maxX = Math.max(first.getBlockX(), second.getBlockX()) + 1;
                        double maxY = Math.max(first.getBlockY(), second.getBlockY()) + 1;
                        double maxZ = Math.max(first.getBlockZ(), second.getBlockZ()) + 1;

                        Location tmp = new Location(player.getWorld(), minX, minY, minZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setX(minX);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setY(minY);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(player.getWorld(), minX, minY, maxZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setX(minX);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(player.getWorld(), minX, maxY, minZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setX(minX);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }

                        tmp = new Location(player.getWorld(), minX, maxY, maxZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }

                        tmp = new Location(player.getWorld(), maxX, minY, minZ);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setY(minY);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(player.getWorld(), maxX, minY, maxZ);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(player.getWorld(), maxX, maxY, minZ);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                    }
                }).start();
            }
//            if (warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID + ".spawn.blue")) {
//                Location redSpawn = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.blue");
//
//                redSpawn.setY(redSpawn.getY() + 1);
//                redSpawn.setX(redSpawn.getX() + 0.57);
//                redSpawn.setZ(redSpawn.getZ() + 0.55);
//
//                if (player.getLocation().getWorld().equals(redSpawn.getWorld()) && player.getLocation().distance(redSpawn) <= range) {
//                    for (double d = 0; d < 1.8; d = d + 0.1) {
//                        redSpawn.setY(redSpawn.getY() + 0.1);
//                        redSpawn.getWorld().spawnParticle(Particle.DRIP_LAVA, redSpawn, 10);
//                    }
//                }
//
//            }
//
//            if (warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID + ".spawn.blue")) {
//                Location blueSpawn = (Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.blue");
//
//                blueSpawn.setY(blueSpawn.getY() + 1);
//                blueSpawn.setX(blueSpawn.getX() + 0.57);
//                blueSpawn.setZ(blueSpawn.getZ() + 0.55);
//
//                if (player.getLocation().getWorld().equals(blueSpawn.getWorld()) && player.getLocation().distance(blueSpawn) <= range) {
//                    for (double d = 0; d < 1.8; d = d + 0.1) {
//                        blueSpawn.setY(blueSpawn.getY() + 0.1);
//                        blueSpawn.getWorld().spawnParticle(Particle.DRIP_WATER, blueSpawn, 10);
//                    }
//                }
//
//            }

        }
    }

    public static void showOffset(Main p) {

        Files warIslands = new Files(p, "warIslands.yml");

        for (String uuid : warIslands.getConfig().getConfigurationSection("warIslands.offset").getKeys(false)) {

            Player player = Bukkit.getPlayer(UUID.fromString(uuid));

            if (player == null) {
                continue;
            }
            if (!player.getInventory().getItemInMainHand().equals(wand)) {
                continue;
            }

            int range = 100;
            if (warIslands.getConfig().contains("warIslands.showOffset.range")) {
                range = warIslands.getConfig().getInt("warIslands.showOffset.range");
            }

            if (warIslands.getConfig().contains("warIslands.offset." + uuid + ".first") && warIslands.getConfig().contains("warIslands.offset." + uuid + ".second")) {
                int finalRange = range;

                new Thread(() -> {

                    Location first = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".first");
                    Location second = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".second");

                    if (first.getWorld().equals(second.getWorld()) && player.getWorld().equals(first.getWorld())) {

                        double minX = Math.min(first.getBlockX(), second.getBlockX());
                        double minY = Math.min(first.getBlockY(), second.getBlockY());
                        double minZ = Math.min(first.getBlockZ(), second.getBlockZ());
                        double maxX = Math.max(first.getBlockX(), second.getBlockX()) + 1;
                        double maxY = Math.max(first.getBlockY(), second.getBlockY()) + 1;
                        double maxZ = Math.max(first.getBlockZ(), second.getBlockZ()) + 1;

                        Location tmp = new Location(first.getWorld(), minX, minY, minZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setX(minX);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setY(minY);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(first.getWorld(), minX, minY, maxZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setX(minX);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(first.getWorld(), minX, maxY, minZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setX(minX);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }

                        tmp = new Location(first.getWorld(), minX, maxY, maxZ);
                        for (double i = minX; i < maxX; i += 0.1) {
                            tmp.setX(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }

                        tmp = new Location(first.getWorld(), maxX, minY, minZ);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                        tmp.setY(minY);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(first.getWorld(), maxX, minY, maxZ);
                        for (double i = minY; i < maxY; i += 0.1) {
                            tmp.setY(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }


                        tmp = new Location(first.getWorld(), maxX, maxY, minZ);
                        for (double i = minZ; i < maxZ; i += 0.1) {
                            tmp.setZ(i);
                            if (player.getLocation().distance(tmp) > finalRange) {
                                continue;
                            }
                            tmp.getWorld().spawnParticle(Particle.REDSTONE, tmp, 1);
                        }
                    }
                }).start();
            }
            if (warIslands.getConfig().contains("warIslands.offset." + uuid + ".redSpawn")) {
                Location redSpawn = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".redSpawn");

                redSpawn.setY(redSpawn.getY() + 1);
                redSpawn.setX(redSpawn.getX() + 0.57);
                redSpawn.setZ(redSpawn.getZ() + 0.55);

                if (player.getLocation().getWorld().equals(redSpawn.getWorld()) && player.getLocation().distance(redSpawn) <= range) {
                    for (double d = 0; d < 1.8; d = d + 0.1) {
                        redSpawn.setY(redSpawn.getY() + 0.1);
                        redSpawn.getWorld().spawnParticle(Particle.DRIP_LAVA, redSpawn, 10);
                    }
                }

            }
            if (warIslands.getConfig().contains("warIslands.offset." + uuid + ".blueSpawn")) {
                Location blueSpawn = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".blueSpawn");

                blueSpawn.setY(blueSpawn.getY() + 1);
                blueSpawn.setX(blueSpawn.getX() + 0.57);
                blueSpawn.setZ(blueSpawn.getZ() + 0.55);

                if (player.getLocation().getWorld().equals(blueSpawn.getWorld()) && player.getLocation().distance(blueSpawn) <= range) {
                    for (double d = 0; d < 1.8; d = d + 0.1) {
                        blueSpawn.setY(blueSpawn.getY() + 0.1);
                        blueSpawn.getWorld().spawnParticle(Particle.DRIP_WATER, blueSpawn, 10);
                    }
                }

            }

        }

    }

    @Override
    public String getName() {
        return "pos";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland pos delete              deletes your current pos
        //warIsland pos showPos             shows the max distance for the particles
        //warIsland pos showPos <range>     set the showPos

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("delete")) {

                Files warIslands = new Files(p, "warIslands.yml");
                String uuid = player.getUniqueId().toString();

                if (warIslands.getConfig().contains("warIslands.offset." + uuid)) {

                    warIslands.getConfig().set("warIslands.offset." + uuid, null);
                    warIslands.saveConfig();

                    player.sendMessage("successfully removed your offset");

                } else {
                    player.sendMessage("you don't have an offset to delete");
                }

            } else if (args[1].equalsIgnoreCase("showPos")) {

                Files warIslands = new Files(p, "warIslands.yml");

                int range = 100;
                if (warIslands.getConfig().contains("warIslands.showOffset.range")) {
                    range = warIslands.getConfig().getInt("warIslands.showOffset.range");
                }

                player.sendMessage("the range for the particles are " + range);

            } else {
                player.sendMessage("wrong use");
            }

        } else if (args.length == 3) {

            if (args[1].equalsIgnoreCase("showPos")) {

                int range;

                try {
                    range = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("you must use a number as range");
                    return;
                }

                Files warIslands = new Files(p, "warIslands.yml");

                warIslands.getConfig().set("warIslands.showOffset.range", range);
                warIslands.saveConfig();

                player.sendMessage("the showPos is now set to " + range);

            }

        } else {
            player.sendMessage("wrong use");
        }
    }
}
