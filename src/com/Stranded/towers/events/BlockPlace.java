package com.Stranded.towers.events;

import com.Stranded.Files;
import com.Stranded.border.islandBorder.BorderUtils;
import com.Stranded.commands.war.util.WarUtil;
import com.Stranded.towers.Tower;
import com.Stranded.towers.TowerUtil;
import com.Stranded.worldGeneration.regions.SaveRegion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.commands.war.util.WarUtil.testIfIsInWar;

public class BlockPlace implements Listener {

    private String warID;
    private String side;

    private boolean test(Player player, Block block) {
        int temp = WarUtil.testIfPlayerIsInWar(player);
        boolean borderUtils = BorderUtils.testBorder(block.getLocation(), player); //true == not in island

        if (!player.getWorld().getName().equals("War") && !player.getWorld().getName().equals("Islands")) {
            player.sendMessage("you can only place a tower in your own island or in a war");
            return true;
        }

        if (borderUtils && temp == 0) {
            player.sendMessage("you can only place a tower in your own island");
            return true;
        }
        if (!borderUtils && temp == 1) {
            player.sendMessage("you only can place a tower when you are in a war or in your own island");
            return true;
        }

        if (TowerUtil.testTowerLocationFromTower(block.getLocation())) {
            player.sendMessage("you can't place a tower to close to another tower");
            return true;
        }
        if (TowerUtil.testTowerLocationFromNexus(block.getLocation())) {
            player.sendMessage("you can't place a tower to close to a nexus");
            return true;
        }
        return false;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void BlockEvent(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        Block block = e.getBlock();
        Material is = block.getType();

        ItemMeta im;

        if (player.getInventory().getItemInMainHand().hasItemMeta()) {
            im = player.getInventory().getItemInMainHand().getItemMeta();
        } else {
            return;
        }
        if (is.equals(Material.WOOL)) {
            if (im.getDisplayName().equals("§4Enemy Tower") || im.getDisplayName().equals("§3Friendly Tower")) {


                Files config = getFiles("config.yml");
                if (!config.getConfig().contains("island." + uuid)) {
                    e.setCancelled(true);
                    player.sendMessage("you aren't in an island");
                    return;
                }

                if (test(player, block)) {
                    e.setCancelled(true);
                    return;
                }

                if (!im.hasLore()) {
                    player.sendMessage(ChatColor.RED + "I see what you did there, but please don't");//todo
                    return;
                }

                List<String> lore = im.getLore();
                String type = lore.get(0);
                //this is the start level of the tower
                int lvl = 1;
                if (lore.size() == 2) {
                    try {
                        lvl = Integer.parseInt(lore.get(1).replace("lvl: ", ""));
                    } catch (NumberFormatException ignore) {
                    }
                }

                if (type.equals("Teleport") || type.equals("Tp")) {

                    int teleportTowers = 0;
                    String island = config.getConfig().getString("island." + uuid);

                    if (testIfIsInWar(player.getName()) == 2) {
                        String warTowerPath = getWarTowerPath(island);
                        Files warData = getFiles("warData.yml");

                        for (String towerID : warData.getConfig().getConfigurationSection(warTowerPath).getKeys(false)) {
                            if (warData.getConfig().contains("war.war." + warID + ".towers." + side + "." + towerID + ".type")) {
                                if (warData.getConfig().getString("war.war." + warID + ".towers." + side + "." + towerID + ".type").equals("Teleport")) {
                                    teleportTowers++;
                                }
                            }
                        }
                    }
                    Files islands = getFiles("islands.yml");

                    if (islands.getConfig().contains("island." + island + ".towers")) {
                        for (String towerID : islands.getConfig().getConfigurationSection("island." + island + ".towers").getKeys(false)) {
                            if (islands.getConfig().getString("island." + island + ".towers." + towerID + ".type").equals("Teleport")) {
                                teleportTowers++;
                            }
                        }
                    }

                    if (Math.floor(islands.getConfig().getDouble("island." + island + ".lvl") / 10D + 1D) <= teleportTowers) {
                        player.sendMessage("you have your maximum amount of TeleportTowers reached");
                        return;
                    }
                }

                e.setCancelled(true);

                Location location = block.getLocation();
                if (WarUtil.testIfPlayerIsInWar(player) == 1) {
                    SaveWarTower(location, player, type);
                } else {
                    SaveIslandTower(location, player, type);
                }
                new Tower(player.getUniqueId(), type, lvl).setTower(location);
//                switch (type) {
//                    case "TNT":
//
//                        TntTower.Tower(location);
//                        break;
//                    case "Slowness":
//                        Location SlowLocation = block.getLocation();
//                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
//                            SaveWarTower(SlowLocation, player, type);
//                        } else {
//                            SaveIslandTower(SlowLocation, player, type);
//                        }
//                        SlowTower.Tower(SlowLocation);
//                        break;
//                    case "Hunger":
//                        Location HungerLocation = block.getLocation();
//                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
//                            SaveWarTower(HungerLocation, player, type);
//                        } else {
//                            SaveIslandTower(HungerLocation, player, type);
//                        }
//                        HungerTower.Tower(HungerLocation);
//                        break;
//                    case "Wither":
//                        Location WitherLocation = block.getLocation();
//                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
//                            SaveWarTower(WitherLocation, player, type);
//                        } else {
//                            SaveIslandTower(WitherLocation, player, type);
//                        }
//                        WitherTower.Tower(WitherLocation);
//                        break;
//                    case "Arrow":
//                        Location ArrowLocation = block.getLocation();
//                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
//                            SaveWarTower(ArrowLocation, player, type);
//                        } else {
//                            SaveIslandTower(ArrowLocation, player, type);
//                        }
//                        ArrowTower.Tower(ArrowLocation);
//                        break;
//                }
            }
//            else if () {
//
//                if (test(player, block)) {
//                    e.setCancelled(true);
//                    return;
//                }
//
//                if (!im.hasLore()) {
//                    player.sendMessage(ChatColor.RED + "I see what you did there, but please don't");
//                    return;
//                }
//
//                List<String> lore = im.getLore();
//                String type = lore.get(0);
//                e.setCancelled(true);
//
//
//                Location location = block.getLocation();
//                if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
//                    SaveWarTower(location, player, type);
//                } else {
//                    SaveIslandTower(location, player, type);
//                }
//                new Tower(player, type).setTower(location);
////                switch (type) {
////                    case "Haste":
////                        Location HasteLocation = block.getLocation();
////                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
////                            SaveWarTower(HasteLocation, player, type);
////                        } else {
////                            SaveIslandTower(HasteLocation, player, type);
////                        }
////                        HasteTower.Tower(HasteLocation);
////                        break;
////                    case "Regeneration":
////                        Location RegenLocation = block.getLocation();
////                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
////                            SaveWarTower(RegenLocation, player, type);
////                        } else {
////                            SaveIslandTower(RegenLocation, player, type);
////                        }
////                        RegenerationTower.Tower(RegenLocation);
////                        break;
////                    case "Speed":
////                        Location SpeedLocation = block.getLocation();
////                        if (WarUtil.testIfPlayerIsInWar(p, player) == 1) {
////                            SaveWarTower(SpeedLocation, player, type);
////                        } else {
////                            SaveIslandTower(SpeedLocation, player, type);
////                        }
////                        SpeedTower.Tower(SpeedLocation);
////                        break;
////                }
//            }
        }
    }

    private static String getWarTowerPath(String island) {

        Files warData = getFiles("warData.yml");

        if (warData.getConfig().contains("war.pending.island1." + island)) {
            return null;
        }
        if (warData.getConfig().contains("war.pending.island2." + island)) {
            return null;
        }

        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

            if (warData.getConfig().getString("war.war." + warID + ".blue.islandName").equals(island)) {
                return "war.war." + warID + ".towers.blue";
            }
            if (warData.getConfig().getString("war.war." + warID + ".red.islandName").equals(island)) {
                return "war.war." + warID + ".towers.red";
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private void SaveIslandTower(Location l, Player player, String type) {

        Files islands = getFiles("islands.yml");

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        String island = config.getConfig().getString("island." + uuid);
        int id = 0;
        boolean loop = true;

        while (loop) {

            int timeout = 30;
            if (type.equals("TNT")) {
                timeout = 45;
            } else if (type.equals("Teleport")) {
                timeout = 300;
            }
            if (!islands.getConfig().contains("island." + island + ".towers." + id)) {
                islands.getConfig().set("island." + island + ".towers." + id + ".location", l);
                islands.getConfig().set("island." + island + ".towers." + id + ".owner", uuid);
                islands.getConfig().set("island." + island + ".towers." + id + ".timeout", timeout);
                islands.getConfig().set("island." + island + ".towers." + id + ".timer", timeout);
                islands.getConfig().set("island." + island + ".towers." + id + ".type", type);

                Location L1 = new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ() + 2);
                Location L2 = new Location(l.getWorld(), l.getX() - 1, l.getY() + 4, l.getZ() - 2);
//
                int minX = Math.min(L1.getBlockX(), L2.getBlockX());
                int minY = Math.min(L1.getBlockY(), L2.getBlockY());
                int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
                int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
                int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
                int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

                int blockCount = 0;

                for (int xx = minX; xx <= maxX; xx++) {
                    for (int yy = minY; yy <= maxY; yy++) {
                        for (int zz = minZ; zz <= maxZ; zz++) {
                            Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(xx, yy, zz);
                            islands.getConfig().set("island." + island + ".towers." + id + ".terrain." + blockCount + ".type", block.getType().getId());
                            islands.getConfig().set("island." + island + ".towers." + id + ".terrain." + blockCount + ".data", block.getData());
                            blockCount++;
                        }
                    }
                }
                islands.getConfig().set("island." + island + ".towers." + id + ".terrain." + 27 + ".type", 0);
                islands.getConfig().set("island." + island + ".towers." + id + ".terrain." + 27 + ".data", 0);
                islands.saveConfig();

                islands.saveConfig();

//                new SaveRegion().saveRegion(L1, L2, islands, "island." + island + ".towers." + id + ".terrain");

                loop = false;
            } else {
                id++;
            }
        }
    }

    private void setSaveWarTowerData(Player player, Files warData) {
        Files config = getFiles("config.yml");
        String island = config.getConfig().getString("island." + player.getUniqueId().toString());
        this.warID = "";
        this.side = "";

        for (String warIDLoop : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
            if (warData.getConfig().getString("war.war." + warIDLoop + ".blue.islandName").equals(island)) {
                warID = warIDLoop;
                side = "blue";
                break;
            }
            if (warData.getConfig().getString("war.war." + warIDLoop + ".red.islandName").equals(island)) {
                warID = warIDLoop;
                side = "red";
                break;
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void SaveWarTower(Location l, Player player, String type) {

        Files warData = getFiles("warData.yml");

        setSaveWarTowerData(player, warData);

        int id = 0;

        while (true) {

            int timeout = 30;
            if (type.equals("TNT")) {
                timeout = 45;
            } else if (type.equals("Teleport")) {
                timeout = 300;
            }
            if (!warData.getConfig().contains("war.war." + warID + ".towers." + side + "." + id)) {
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".location", l);
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".owner", player.getUniqueId().toString());
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".timeout", timeout);
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".timer", timeout);
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".type", type);

                Location L1 = new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ() + 2);
                Location L2 = new Location(l.getWorld(), l.getX() - 1, l.getY() + 4, l.getZ() - 2);

                int minX = Math.min(L1.getBlockX(), L2.getBlockX());
                int minY = Math.min(L1.getBlockY(), L2.getBlockY());
                int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
                int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
                int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
                int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

                int blockCount = 0;

                for (int xx = minX; xx <= maxX; xx++) {
                    for (int yy = minY; yy <= maxY; yy++) {
                        for (int zz = minZ; zz <= maxZ; zz++) {
                            Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(xx, yy, zz);
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".terrain." + blockCount + ".type", block.getType().getId());
                            warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".terrain." + blockCount + ".data", block.getData());
                            blockCount++;
                        }
                    }
                }
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".terrain." + 27 + ".type", 0);
                warData.getConfig().set("war.war." + warID + ".towers." + side + "." + id + ".terrain." + 27 + ".data", 0);
                warData.saveConfig();
                return;
            } else {
                id++;
            }
        }
    }
}
