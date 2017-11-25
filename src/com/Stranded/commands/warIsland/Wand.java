package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.nexus.InventoryEvent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Wand extends CmdManager {
    private static boolean loop = false;

    public static boolean wandStuff(PlayerInteractEvent e, Main p) {
        Files warIslands = new Files(p, "warIslands.yml");
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        if (player.getInventory().getItemInMainHand().equals(InventoryEvent.newItemStack(Material.STICK, 0, "War Island Selector"))) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                Location l = e.getClickedBlock().getLocation();

                if (player.isSneaking()) {
                    warIslands.getConfig().set("warIslands.offset." + uuid + ".first", l);
                    if (warIslands.getConfig().contains("warIslands.offset." + uuid + ".second")) {

                        Location L2 = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".second");

                        int minX = Math.min(l.getBlockX(), L2.getBlockX());
                        int minY = Math.min(l.getBlockY(), L2.getBlockY());
                        int minZ = Math.min(l.getBlockZ(), L2.getBlockZ());
                        int maxX = Math.max(l.getBlockX(), L2.getBlockX());
                        int maxY = Math.max(l.getBlockY(), L2.getBlockY());
                        int maxZ = Math.max(l.getBlockZ(), L2.getBlockZ());

                        int size = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);

                        player.sendMessage(String.format("First selection set (%d, %d, %d) (%d)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), size));

                    } else {
                        player.sendMessage(String.format("First selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                    }
                    warIslands.saveConfig();
                    return true;
                } else {
                    warIslands.getConfig().set("warIslands.offset." + uuid + ".blueSpawn", l);
                    player.sendMessage(String.format(ChatColor.BLUE + "blue spawn selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                    warIslands.saveConfig();
                    return true;
                }
            } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && loop) {
                Location l = e.getClickedBlock().getLocation();

                if (player.isSneaking()) {
                    warIslands.getConfig().set("warIslands.offset." + uuid + ".second", l);
                    if (warIslands.getConfig().contains("warIslands.offset." + uuid + ".first")) {

                        Location L2 = (Location) warIslands.getConfig().get("warIslands.offset." + uuid + ".first");

                        int minX = Math.min(l.getBlockX(), L2.getBlockX());
                        int minY = Math.min(l.getBlockY(), L2.getBlockY());
                        int minZ = Math.min(l.getBlockZ(), L2.getBlockZ());
                        int maxX = Math.max(l.getBlockX(), L2.getBlockX());
                        int maxY = Math.max(l.getBlockY(), L2.getBlockY());
                        int maxZ = Math.max(l.getBlockZ(), L2.getBlockZ());

                        int size = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);

                        player.sendMessage(String.format("Second selection set (%d, %d, %d) (%d)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), size));

                    } else {
                        player.sendMessage(String.format("Second selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                    }
                    warIslands.saveConfig();
                    loop = false;
                    return true;
                } else {
                    warIslands.getConfig().set("warIslands.offset." + uuid + ".redSpawn", l);
                    player.sendMessage(String.format(ChatColor.RED + "red spawn set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                    warIslands.saveConfig();
                    loop = false;
                    return true;
                }

            }
            loop = !loop;
        }
        return false;
    }

    @Override
    public String getName() {
        return "wand";
    }

    @Override
    public String getAlias() {
        return "w";
    }

    static ItemStack wand = InventoryEvent.newItemStack(Material.STICK, 0, "War Island Selector");

    @Override
    public void run(String[] args, Player player) {

        //warIsland wand

        if (!player.hasPermission("stranded.wand")) {
            player.sendMessage("y u no has permission");
            return;
        }

        ItemStack is = wand;
        player.getInventory().addItem(is);
        player.sendMessage("right click for the blue Spawn, left click for the red Spawn " +
                "\nshift right click for first pos, and shift left click for second pos. there last are used when you want to export an island");
    }
}
