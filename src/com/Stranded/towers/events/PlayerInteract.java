package com.Stranded.towers.events;

import com.Stranded.Main;
import com.Stranded.towers.TowerInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.Stranded.towers.TowerUtil.upgradeTower;

public class PlayerInteract implements Listener {

    private Main p;

    public PlayerInteract(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void Interact(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block b = e.getClickedBlock();
                    Sign s = (Sign) b.getState();

                    Sign opposite = null;

                    boolean north = true;

                    if (s.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                        if (player.getWorld().getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getType().equals(Material.WALL_SIGN)) {
                            opposite = (Sign) player.getWorld().getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getState();
                        } else {
                            //todo corrupted tower
                            return;
                        }
                    } else if (s.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                        if (player.getWorld().getBlockAt(s.getX(), s.getY(), s.getZ() - 4).getType().equals(Material.WALL_SIGN)) {
                            opposite = (Sign) player.getWorld().getBlockAt(s.getX(), s.getY(), s.getZ() - 4).getState();
                            north = false;
                        } else {
                            //todo corrupted tower
                            return;
                        }
                    }
                    if (opposite == null) {
                        //todo corrupted tower
                        return;
                    }

                    Location towerLoc = new Location(player.getWorld(), s.getLocation().getBlockX(), s.getLocation().getBlockY() - 1, s.getLocation().getBlockZ() + 2);
                    if (!north) {
                        towerLoc = new Location(player.getWorld(), s.getLocation().getBlockX(), s.getLocation().getBlockY() - 1, s.getLocation().getBlockZ() - 2);
                    }

                    if (s.getLine(0).equals("§3Friendly Tower") || s.getLine(0).equals("§4Enemy Tower")) {
                        if (s.getLine(1).startsWith("Speed lvl: ") || s.getLine(1).startsWith("Slow lvl: ")
                                || s.getLine(1).startsWith("Regen lvl: ") || s.getLine(1).startsWith("Haste lvl: ")
                                || s.getLine(1).startsWith("Wither lvl: ") || s.getLine(1).startsWith("Hunger lvl: ")
                                || s.getLine(1).startsWith("Tnt lvl: ") || s.getLine(1).startsWith("Arrow lvl: ")
                                || s.getLine(1).startsWith("Tp lvl: ")) {

                            String lvl = (s.getLine(1).replace("Speed lvl: ", "").replace("Slow lvl: ", "")
                                    .replace("Regen lvl: ", "").replace("Haste lvl: ", "")
                                    .replace("Wither lvl: ", "").replace("Hunger lvl: ", "")
                                    .replace("Tnt lvl: ", "").replace("Arrow lvl: ", "")
                                    .replace("Tp lvl: ", ""));

                            int lvlInt;
                            try {
                                lvlInt = Integer.parseInt(lvl);
                            } catch (NumberFormatException nfe) {
                                player.sendMessage("This tower is MAX lvl");
                                return;
                            }

                            int upgrade = Integer.parseInt(s.getLine(3));

                            if (player.getLevel() >= upgrade) {

                                if (!upgradeTower(lvlInt, lvl, s, opposite, player, upgrade, towerLoc,
                                        towerLoc.getWorld().equals(Bukkit.getWorld("Islands")))) {
                                    player.sendMessage("Could not upgrade tower");
                                } else {
                                    player.sendMessage("Couldn't upgrade tower, if this problem continues you can try removing the tower and place it again");
                                }
                            } else {
                                player.sendMessage("Not enough xp levels");
                            }
                        }
                    }
                } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    Block b = e.getClickedBlock();
                    Sign s = (Sign) b.getState();

                    if (s.getLine(0).equals("§3Friendly Tower") || s.getLine(0).equals("§4Enemy Tower")) {

                        String lvl = (s.getLine(1).replace("Speed lvl: ", "").replace("Slow lvl: ", "")
                                .replace("Regen lvl: ", "").replace("Haste lvl: ", "")
                                .replace("Wither lvl: ", "").replace("Hunger lvl: ", "")
                                .replace("Tnt lvl: ", "").replace("Arrow lvl: ", "")
                                .replace("Tp lvl: ", ""));

                        player.sendMessage(TowerInfo.getTowerInfo(s.getLine(1), lvl));

                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
