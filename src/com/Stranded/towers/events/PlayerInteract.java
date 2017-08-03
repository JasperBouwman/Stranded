package com.Stranded.towers.events;

import com.Stranded.Main;
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

                    Sign opposide = null;

                    if (s.getData().toString().equals("WALL_SIGN(2) facing NORTH")) {
                        opposide = (Sign) Bukkit.getWorld(player.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() + 4).getState();
                    }
                    if (s.getData().toString().equals("WALL_SIGN(3) facing SOUTH")) {
                        opposide = (Sign) Bukkit.getWorld(player.getWorld().getName()).getBlockAt(s.getX(), s.getY(), s.getZ() - 4).getState();
                    }

                    if (s.getLine(0).equals("§3Friendly Tower") || s.getLine(0).equals("§4Enemy Tower")) {
                        if (s.getLine(1).startsWith("Speed lvl: ") || s.getLine(1).startsWith("Slow lvl: ")
                                || s.getLine(1).startsWith("Regen lvl: ") || s.getLine(1).startsWith("Haste lvl: ")
                                || s.getLine(1).startsWith("Wither lvl: ") || s.getLine(1).startsWith("Hunger lvl: ")
                                || s.getLine(1).startsWith("Tnt lvl: ") || s.getLine(1).startsWith("Arrow lvl: ")) {

                            String lvl = (s.getLine(1).replaceAll("Speed lvl: ", "").replaceAll("Slow lvl: ", "")
                                    .replaceAll("Regen lvl: ", "").replaceAll("Haste lvl: ", "")
                                    .replaceAll("Wither lvl: ", "").replaceAll("Hunger lvl: ", "")
                                    .replaceAll("Tnt lvl: ", "").replaceAll("Arrow lvl: ", ""));
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

                                    s.setLine(1, s.getLine(1).replaceAll(lvl, "MAX"));
                                    s.setLine(3, "-");
                                    s.update();

                                    opposide.setLine(1, s.getLine(1).replaceAll(lvl, "MAX"));
                                    opposide.setLine(3, "-");
                                    opposide.update();


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

                                s.setLine(1, s.getLine(1).replaceAll(lvl, str.toString()));
                                int newCost = Integer.parseInt(s.getLine(3)) + 2;
                                s.setLine(3, newCost + "");
                                s.update();

                                opposide.setLine(1, s.getLine(1).replaceAll(lvl, str.toString()));
                                opposide.setLine(3, newCost + "");
                                opposide.update();

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

                        String lvl = (s.getLine(1).replaceAll("Speed lvl: ", "").replaceAll("Slow lvl: ", "")
                                .replaceAll("Regen lvl: ", "").replaceAll("Haste lvl: ", "")
                                .replaceAll("Wither lvl: ", "").replaceAll("Hunger lvl: ", "")
                                .replaceAll("Tnt lvl: ", "").replaceAll("Arrow lvl: ", ""));

                        if (s.getLine(1).startsWith("Speed lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 1 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 3 seconds");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 3 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 5 second");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 5 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 6 seconds");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 6 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 7 seconds");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 7 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 8 seconds");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 8 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 9 seconds");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 9 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 1 for 10 seconds");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 1 for 10 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Speed 2 for 10 seconds");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Speed Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Speed 2 for 10 second");
                            }
                        } else if (s.getLine(1).startsWith("Slow lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 1 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 3 seconds");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 3 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 5 seconds");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 6 seconds");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 6 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 7 seconds");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 7 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 8 seconds");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 8 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 9 seconds");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 9 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 1 for 10 seconds");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 1 for 10 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Slowness 2 for 10 seconds");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Slowness Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Slowness 2 for 10 seconds");
                            }
                        } else if (s.getLine(1).startsWith("Regen lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 1 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 2 seconds");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 2 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 3 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 4 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Regeneration 1 for 6 seconds");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Regeneration Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Regeneration 1 for 6 seconds");
                            }
                        } else if (s.getLine(1).startsWith("Haste lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 1 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 2 seconds");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 2 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 3 seconds");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 3 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 4 seconds");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 4 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 5 seconds");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 6 seconds");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 6 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 7 seconds");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 7 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 1 for 7 seconds");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 1 for 7 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Haste 2 for 8 seconds");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Haste Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Haste 2 for 8 seconds");
                            }
                        } else if (s.getLine(1).startsWith("Wither lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 1 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 2 seconds");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 2 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 3 seconds");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 3 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 4 seconds");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 4 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 4 seconds");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 4 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 5 seconds");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 5 seconds");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 6 seconds");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 1 for 6 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Wither 1 for 7 seconds");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Wither Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Wither 2 for 7 seconds");
                            }
                        } else if (s.getLine(1).startsWith("Hunger lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 1 second");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 2 seconds");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 2 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 3 seconds");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 3 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 4 seconds");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 4 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 5 seconds");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 5 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 6 seconds");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 6 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 7 seconds");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 7 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 8 seconds");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 1 for 8 seconds");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: Hunger 1 for 10 seconds");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Hunger Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: Hunger 2 for 10 seconds");
                            }
                        } else if (s.getLine(1).startsWith("Tnt lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 45\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 40\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 40\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 36\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 36\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 33\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 33\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 27\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 27\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 24\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 24\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 22\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 22\nEffect: shoot 1 tnt");
                                player.sendMessage("Next upgrade:\nCooldown: 20\nEffect: shoot 1 tnt");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Tnt Tower lvl " + lvl+ ":\nCooldown: 20\nEffect: shoot 1 tnt");
                            }
                        } else if (s.getLine(1).startsWith("Arrow lvl: ")) {
                            if (lvl.equals("1")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 5 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 10 arrows");
                            } else if (lvl.equals("2")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 10 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 15 arrows");
                            } else if (lvl.equals("3")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 15 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 20 arrows");
                            } else if (lvl.equals("4")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 20 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 25 arrows");
                            } else if (lvl.equals("5")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 25 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 30 arrows");
                            } else if (lvl.equals("6")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 30 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 35 arrows");
                            } else if (lvl.equals("7")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 35 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 40 arrows");
                            } else if (lvl.equals("8")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 40 arrows");
                                player.sendMessage("Next upgrade:\nCooldown: 30\nEffect: shoot 45 arrows");
                            } else if (lvl.equals("MAX")) {
                                player.sendMessage("Arrow Tower lvl " + lvl+ ":\nCooldown: 30\nEffect: shoot 45 arrows");
                            }
                        }
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
