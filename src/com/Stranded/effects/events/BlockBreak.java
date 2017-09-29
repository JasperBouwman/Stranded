package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {

    private Main p;

    public BlockBreak(Main instance) {
        p = instance;
    }

    @EventHandler
    public void Block(BlockBreakEvent e) {
        Files save = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");
        Player player = e.getPlayer();

        if (save.getConfig().getLong("BlockBreak." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier") != 100) {
            save.getConfig().set("BlockBreak." + player.getName(), save.getConfig().getLong("BlockBreak." + player.getName()) + 1);
            save.saveConfig();
        }
        else if (save.getConfig().getLong("BlockBreak." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier") == 100) {

            ItemStack is = player.getInventory().getItemInMainHand();
                if (!is.getType().equals(Material.DIAMOND_PICKAXE) &&
                        !is.getType().equals(Material.GOLD_PICKAXE) &&
                        !is.getType().equals(Material.IRON_PICKAXE) &&
                        !is.getType().equals(Material.STONE_PICKAXE) &&
                        !is.getType().equals(Material.WOOD_PICKAXE)) {
                    return;
                }

                switch (e.getBlock().getType()) {
                    case IRON_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_ORE));
                        break;
                    case COAL_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.COAL));
                        break;
                    case DIAMOND_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND));
                        break;
                    case EMERALD_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.EMERALD));
                        break;
                    case GLOWING_REDSTONE_ORE:
                    case REDSTONE_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE));
                        break;
                    case GOLD_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_ORE));
                        break;
                    case LAPIS_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(351, 1, (byte) 4));
                        break;
                    case QUARTZ_ORE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.QUARTZ));
                        break;
                    case GLOWSTONE:
                        e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GLOWSTONE_DUST));
                        break;

            }
        }
    }
}
