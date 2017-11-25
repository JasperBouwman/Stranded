package com.Stranded.towers.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

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

                    if (s.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                        opposite = (Sign) Bukkit.getWorld(player.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getState();
                    }
                    if (s.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                        opposite = (Sign) Bukkit.getWorld(player.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() - 4).getState();
                    }

                    if (s.getLine(0).equals("§3Friendly Tower") || s.getLine(0).equals("§4Enemy Tower")) {
                        if (s.getLine(1).startsWith("Speed lvl: ") || s.getLine(1).startsWith("Slow lvl: ")
                                || s.getLine(1).startsWith("Regen lvl: ") || s.getLine(1).startsWith("Haste lvl: ")
                                || s.getLine(1).startsWith("Wither lvl: ") || s.getLine(1).startsWith("Hunger lvl: ")
                                || s.getLine(1).startsWith("Tnt lvl: ") || s.getLine(1).startsWith("Arrow lvl: ")) {

                            String lvl = (s.getLine(1).replace("Speed lvl: ", "").replace("Slow lvl: ", "")
                                    .replace("Regen lvl: ", "").replace("Haste lvl: ", "")
                                    .replace("Wither lvl: ", "").replace("Hunger lvl: ", "")
                                    .replace("Tnt lvl: ", "").replace("Arrow lvl: ", ""));
                            int lvlInt;
                            try {
                                lvlInt = Integer.parseInt(lvl);
                            } catch (NumberFormatException nfe) {
                                player.sendMessage("This tower is MAX lvl");
                                return;
                            }

                            int upgrade = Integer.parseInt(s.getLine(3));

                            if (lvlInt == 8) {
                                if (player.getLevel() >= upgrade) {

                                    s.setLine(1, s.getLine(1).replace(lvl, "MAX"));
                                    s.setLine(3, "-");
                                    s.update();

                                    assert opposite != null; //todo check assert
                                    opposite.setLine(1, s.getLine(1).replace(lvl, "MAX"));
                                    opposite.setLine(3, "-");
                                    opposite.update();


                                    player.setLevel(player.getLevel() - upgrade);

                                    player.sendMessage("This tower is now max upgraded");

                                } else {
                                    player.sendMessage("Not enough xp levels");
                                }
                                return;
                            }

                            if (player.getLevel() >= upgrade) {

                                StringBuilder str = new StringBuilder();
                                str.append(lvlInt + 1);

                                s.setLine(1, s.getLine(1).replace(lvl, str.toString()));
                                int newCost = Integer.parseInt(s.getLine(3)) + 2;
                                s.setLine(3, newCost + "");
                                s.update();

                                assert opposite != null; //todo check assert
                                opposite.setLine(1, s.getLine(1).replace(lvl, str.toString()));
                                opposite.setLine(3, newCost + "");
                                opposite.update();

                                player.setLevel(player.getLevel() - upgrade);

                                player.sendMessage("This tower is upgraded");

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
                                .replace("Tnt lvl: ", "").replace("Arrow lvl: ", ""));

                        player.sendMessage(TowerInfo.getTowerInfo(s.getLine(1), lvl));

                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
