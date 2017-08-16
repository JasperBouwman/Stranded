package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.nexus.InventoryEvent;
import com.Stranded.worldGeneration.IslandGeneration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Add extends CmdManager {

    private static boolean loop = false;
    private Location L1S;
    private Location L2S;

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
        return "add";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        if (!player.hasPermission("Stranded.createWarIsland")) {
            player.sendMessage("you don't have permission");
            return;
        }

        Files f = new Files(p, "warIslands.yml");

        //war generate <name> <min> <max>

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
        } else if (!f.getConfig().contains("warIslands.offset." + player.getName() + ".X")) {
            player.sendMessage("you don't have a valid offset for a war Island, missing: Second selection");
            return;
        }

//        if (!f.getConfig().getString("warIslands.offset." + player.getName() + ".X.world").equals(f.getConfig().getString("warIslands.offset." + player.getName() + ".Z.world"))) {
//            player.sendMessage("the selections aren't in the same world, that must be");
//            return;
//        }

        if (f.getConfig().contains("warIslands.island." + args[1])) {
            player.sendMessage("this name is already in use");
            return;
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

        f.getConfig().set("warIslands.island." + args[1] + ".maxPlayers", max);
        f.getConfig().set("warIslands.island." + args[1] + ".minPlayers", min);
        f.getConfig().set("warIslands.island." + args[1] + ".L1", L1S);
        f.getConfig().set("warIslands.island." + args[1] + ".L2", L2S);
        f.saveConfig();

        copyIsland(player);

    }

    private void copyIsland(Player player) {

        Files f = new Files(p, "warIslands.yml");

        //get locations for copy
        Location L1 = (Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X");
        Location L2 = (Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z");

        HashMap<Integer, Block> blockSet = new HashMap<>();
        int blockCount = 1;

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        //copy island
        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    Block block = player.getWorld().getBlockAt(xx, yy, zz);

                    blockSet.put(blockCount, block);

                    blockCount++;
                }
            }
        }

        //set data for the final place of the island
        int x = f.getConfig().getInt("warIslands.nextLocation.X");
        int z = f.getConfig().getInt("warIslands.nextLocation.Z");
        int y = Math.min(((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X")).getBlockY(),
                ((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z")).getBlockY());

        int offsetX = Math.abs(((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X")).getBlockX() -
                ((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z")).getBlockX());
        int offsetY = Math.abs(((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X")).getBlockY() -
                ((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z")).getBlockY());
        int offsetZ = Math.abs(((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".X")).getBlockZ() -
                ((Location) f.getConfig().get("warIslands.offset." + player.getName() + ".Z")).getBlockZ());

        int x2 = x + offsetX;
        int y2 = y + offsetY;
        int z2 = z + offsetZ;

        blockCount = 1;

        L1S = new Location(Bukkit.getWorld("War"), x, y, z);
        L2S = new Location(Bukkit.getWorld("War"), x2, y2, z2);

        //place the island
        for (int xx = x; xx <= x2; xx++) {
            for (int yy = y; yy <= y2; yy++) {
                for (int zz = z; zz <= z2; zz++) {
                    Block block = Bukkit.getServer().getWorld("War").getBlockAt(xx, yy, zz);
                    IslandGeneration.setBlock(blockSet.get(blockCount), block);
                    blockCount++;
                }
            }
        }

        //set other data
        if (f.getConfig().getInt("warIslands.nextLocation.nextZ") < offsetZ + 1000) {
            f.getConfig().set("warIslands.nextLocation.nextZ", offsetZ + 1000);
            f.saveConfig();
            player.sendMessage("z is bigger");
        }
        if (x2 > 300000) {
            f.getConfig().set("warIslands.nextLocation.X", 300000);
            f.getConfig().set("warIslands.nextLocation.Z", f.getConfig().getInt("warIslands.nextLocation.nextZ"));
            f.saveConfig();
            player.sendMessage("x2 > 300000");
        } else {
            f.getConfig().set("warIslands.nextLocation.X", f.getConfig().getInt("warIslands.nextLocation.X") + offsetX + 1000);
            f.saveConfig();
            player.sendMessage("x = " + f.getConfig().getInt("warIslands.nextLocation.X") + " + " + offsetX + " + 1000");
        }

        player.teleport(new Location(Bukkit.getWorld("War"), x, y, z));

    }
}
