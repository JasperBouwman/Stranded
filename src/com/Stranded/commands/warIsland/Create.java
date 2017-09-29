package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.nexus.InventoryEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Create extends CmdManager {
    private static boolean loop = false;

    public static boolean wandStuff(PlayerInteractEvent e, Main p) {
        Files f = new Files(p, "warIslands.yml");
        Player player = e.getPlayer();
        if (player.getInventory().getItemInMainHand().equals(InventoryEvent.newItemStack(Material.STICK, 0, "War Island Selector"))) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Location l = e.getClickedBlock().getLocation();
                f.getConfig().set("warIslands.offset." + e.getPlayer().getName() + ".X", l);
                if (f.getConfig().contains("warIslands.offset." + e.getPlayer().getName() + ".Z")) {

                    Location L1 = e.getClickedBlock().getLocation();
                    Location L2 = (Location) f.getConfig().get("warIslands.offset." + e.getPlayer().getName() + ".Z");

                    int minX = Math.min(L1.getBlockX(), L2.getBlockX());
                    int minY = Math.min(L1.getBlockY(), L2.getBlockY());
                    int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
                    int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
                    int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
                    int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

                    int size = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);

                    player.sendMessage(String.format("First selection set (%d, %d, %d) (%d)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), size));

                } else {
                    player.sendMessage(String.format("First selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                }
                f.saveConfig();
                return true;
            } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && loop) {
                Location l = e.getClickedBlock().getLocation();
                f.getConfig().set("warIslands.offset." + e.getPlayer().getName() + ".Z", l);
                if (f.getConfig().contains("warIslands.offset." + e.getPlayer().getName() + ".X")) {

                    Location L1 = e.getClickedBlock().getLocation();
                    Location L2 = (Location) f.getConfig().get("warIslands.offset." + e.getPlayer().getName() + ".X");

                    int minX = Math.min(L1.getBlockX(), L2.getBlockX());
                    int minY = Math.min(L1.getBlockY(), L2.getBlockY());
                    int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
                    int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
                    int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
                    int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

                    int size = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);

                    player.sendMessage(String.format("Second selection set (%d, %d, %d) (%o)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), size));

                } else {
                    player.sendMessage(String.format("Second selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                }
                f.saveConfig();
                loop = false;
                return true;
            }
            loop = !loop;
        }
        return false;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        Files f = new Files(p, "warIslands.yml");

        //war generate <theme> <min> <max>

        if (args.length != 4) {
            if (args.length == 1) {
                player.sendMessage("use /war generate wand for a wand, use /war generate <name> <min> <max> for creating a new war island");
            } else if (args.length == 2) {
                if (args[1].equalsIgnoreCase("wand")) {
                    ItemStack is = InventoryEvent.newItemStack(Material.STICK, 0, "War Island Selector");
                    player.getInventory().addItem(is);
                    player.sendMessage("right click for the First selection, left click for the Second selection");
                    return;
                }
                player.sendMessage("you didn't gave a name, min players and max players");
            } else if (args.length == 3) {
                player.sendMessage("you didn't gave a min players and max players");
            } else if (args.length > 4) {
                player.sendMessage("you gave to many arguments, usage: /war generate <name> <min> <max>");
            }
            return;
        }

        if (!f.getConfig().contains("warIslands.offset." + player.getName())) {
            player.sendMessage("you don't have an valid offset for a war Island, missing: First and Second selection, use /war generate wand to get a regen selector");
            return;
        }
        if (!f.getConfig().contains("warIslands.offset." + player.getName() + ".X")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: First selection");
            return;
        } else if (!f.getConfig().contains("warIslands.offset." + player.getName() + ".Z")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: Second selection");
            return;
        }

        if (!((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X")).getWorld().getName()
                .equals(((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z")).getWorld().getName())) {

            if (!((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X")).getWorld().getName()
                    .equals("War")) {
                player.sendMessage("the selections aren't in the world 'War', that must be");
                return;
            }
        }

        int max;
        int min;
        try {
            max = Integer.parseInt(args[3]);
        } catch (NumberFormatException nfe) {
            player.sendMessage("the max value isn't a number");
            return;
        }
        try {
            min = Integer.parseInt(args[2]);
        } catch (NumberFormatException nfe) {
            player.sendMessage("the min value isn't a number");
            return;
        }

        if (max < min) {
            player.sendMessage("the max value is smaller than the min value, this isn't right");
            return;
        }

        if (args[1].equalsIgnoreCase("random")) {
            player.sendMessage("please do not use this theme name, this is used for players to choose a random theme");
            return;
        }

        int id = 0;
        while (true) {
            if (!f.getConfig().contains("warIslands.island." + args[1] + "." + id)) {
                break;
            } else {
                id++;
            }
        }

        Location blueSpawn = (Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X");
        Location redSpawn = (Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z");


        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".spawn.blue", blueSpawn);
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".spawn.red", redSpawn);
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".maxPlayers", max);
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".minPlayers", min);
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".inUse", false);
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".armorStand.blue", spawnArmorstand(blueSpawn, "§9"));
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".armorStand.red", spawnArmorstand(redSpawn, "§c"));

        Vector v = getVector(blueSpawn, redSpawn);

        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".nexus.blue", spawnNexus(blueSpawn, v));
        v = v.multiply(-1);
        f.getConfig().set("warIslands.island." + args[1] + "." + id + ".nexus.red", spawnNexus(redSpawn, v));

        f.getConfig().set("warIslands.offset." + player.getName(), null);

        f.saveConfig();

    }

    private Vector getVector(Location blue, Location red) {
        return red.toVector().subtract(blue.toVector());
    }

    private String spawnNexus(Location l, Vector vector) {

        l.setY(l.getY() - 1);

        Villager v = l.getWorld().spawn(l, Villager.class);

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");
        list.add(v.getUniqueId().toString());
        p.getConfig().set("nexus.uuid", list);
        p.saveConfig();

        v.setCustomNameVisible(false);
        v.setProfession(Villager.Profession.NITWIT);
        v.setAI(false);
        v.isSilent();
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);

        l.setDirection(vector);

        v.teleport(l);
        return v.getUniqueId().toString();
    }

    private String spawnArmorstand(Location l, String color) {
        l.setX(l.getX() + 0.5);
        l.setZ(l.getZ() + 0.5);
        l.setY(l.getY() + 2);

        ArmorStand a = l.getWorld().spawn(l, ArmorStand.class);

        a.setCustomName(color + "Health: 20");
        a.setArms(false);
        a.setBasePlate(false);
        a.setSmall(true);
        a.setVisible(false);
        a.setCanPickupItems(false);
        a.setGravity(false);
        a.setInvulnerable(true);
        a.setCustomNameVisible(true);

        return a.getUniqueId() + "";

    }

}
