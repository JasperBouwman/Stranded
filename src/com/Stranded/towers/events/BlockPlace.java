package com.Stranded.towers.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.towers.models.*;
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

public class BlockPlace implements Listener {

    private Main p;

    public BlockPlace(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void BlockEvent(BlockPlaceEvent e) {

        Player player = e.getPlayer();
        Block block = e.getBlock();
        Material is = block.getType();

        ItemMeta im;

        if (player.getInventory().getItemInMainHand().hasItemMeta()) {
            im = player.getInventory().getItemInMainHand().getItemMeta();
        } else {
            return;
        }
        if (is.equals(Material.WOOL)) {
            if (im.getDisplayName().equals("§4Enemy Tower")) {

                if (!im.hasLore()) {
                    player.sendMessage(ChatColor.RED + "I see what you did there, but please don't");
                    return;
                }

                List<String> lore = im.getLore();
                String type = "";
                for (String s : lore) {
                    type = s;
                }
                e.setCancelled(true);
                if (type.equals("TNT")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    TntTower tower = new TntTower();
                    tower.Tower(l);


                } else if (type.equals("Slowness")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    SlowTower tower = new SlowTower();
                    tower.Tower(l);


                } else if (type.equals("Hunger")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    HungerTower tower = new HungerTower();
                    tower.Tower(l);


                } else if (type.equals("Wither")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    WitherTower tower = new WitherTower();
                    tower.Tower(l);


                } else if (type.equals("Arrow")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    ArrowTower tower = new ArrowTower();
                    tower.Tower(l);


                }
            } else if (im.getDisplayName().equals("§3Friendly Tower")) {

                if (!im.hasLore()) {
                    player.sendMessage(ChatColor.RED + "I see what you did there, but please don't");
                    return;
                }

                List<String> lore = im.getLore();
                String type = "";
                for (String s : lore) {
                    type = s;
                }
                e.setCancelled(true);
                if (type.equals("Haste")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    HasteTower tower = new HasteTower();
                    tower.Tower(l);


                } else if (type.equals("Regeneration")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    RegenerationTower tower = new RegenerationTower();
                    tower.Tower(l);


                } else if (type.equals("Speed")) {

                    Location l = block.getLocation();
                    SaveTower(l, player, type);
                    SpeedTower tower = new SpeedTower();
                    tower.Tower(l);

                }
            }
        }
    }

    private void SaveTower(Location l, Player player, String type) {

        Files f = new Files(p, "islands.yml");

        String island = p.getConfig().getString("island." + player.getName());
        int id = 0;
        boolean loop = true;

        while (loop) {

            if (id == 100) {
                player.sendMessage("an error happend, this is an auto loop prevention. this will only work is you have over 100 towers or an error");
                loop = false;
            }

            int timeout = 30;
            if (type.equals("TNT")) {
                timeout = 45;
            }
            if (!f.getConfig().contains("island." + island + ".towers." + id)) {
                f.getConfig().set("island." + island + ".towers." + id + ".location", l);
                f.getConfig().set("island." + island + ".towers." + id + ".owner", player.getName());
                f.getConfig().set("island." + island + ".towers." + id + ".timeout", timeout);
                f.getConfig().set("island." + island + ".towers." + id + ".timer", timeout);

                Location L1 = new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ() + 2);
                Location L2 = new Location(l.getWorld(), l.getX() - 1, l.getY() + 4, l.getZ() - 2);

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
                            f.getConfig().set("island." + island + ".towers." + id + ".terrain." + blockCount + ".type", block.getTypeId());
                            f.getConfig().set("island." + island + ".towers." + id + ".terrain." + blockCount + ".data", block.getData());
                            blockCount++;
                        }
                    }
                }
                f.getConfig().set("island." + island + ".towers." + id + ".terrain." + 27 + ".type", 0);
                f.getConfig().set("island." + island + ".towers." + id + ".terrain." + 27 + ".data", 0);
                f.saveConfig();

                loop = false;
            } else {
                id++;
            }
        }
    }
}