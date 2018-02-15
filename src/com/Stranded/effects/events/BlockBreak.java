package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static com.Stranded.GettingFiles.getFiles;

public class BlockBreak implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void Block(BlockBreakEvent e) {
        Files playerData = getFiles("playerData.yml");
        Files pluginData = getFiles("pluginData.yml");

        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        int amplifier = pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier");
        if (playerData.getConfig().getLong("BlockBreak." + uuid) / amplifier != 100) {
            Long oldScore = playerData.getConfig().getLong("BlockBreak." + uuid);
            playerData.getConfig().set("BlockBreak." + uuid, oldScore + 1);
            playerData.saveConfig();
            if (oldScore / amplifier != oldScore + 1 / amplifier) {
                Scoreboard.scores(player);
            }
        } else if (playerData.getConfig().getLong("BlockBreak." + uuid) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier") == 100) {

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
