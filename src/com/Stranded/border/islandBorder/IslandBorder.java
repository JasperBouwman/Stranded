package com.Stranded.border.islandBorder;

import com.Stranded.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import static com.Stranded.commands.warIsland.Wand.wandStuff;

public class IslandBorder implements Listener {

    private final Main p;

    public IslandBorder(Main main) {
        p = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void BlockBreak(BlockBreakEvent e) {
        e.setCancelled(BorderUtils.testBorder(e.getBlock().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void BlockPlace(BlockPlaceEvent e) {
        e.setCancelled(BorderUtils.testBorder(e.getBlock().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void Hit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player)
            e.setCancelled(BorderUtils.testBorder(e.getEntity().getLocation(), (Player) e.getDamager()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent e) {

        e.setCancelled(wandStuff(e));

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            e.setCancelled(BorderUtils.testBorder(e.getClickedBlock().getLocation(), e.getPlayer()));
            return;
        }
        e.setCancelled(BorderUtils.testBorder(e.getPlayer().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(BorderUtils.testBorder(e.getRightClicked().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings({"unused", "deprecation"})
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        e.setCancelled(BorderUtils.testBorder(e.getPlayer().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(BorderUtils.testBorder(e.getPlayer().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerPickupArrow(PlayerPickupArrowEvent e) {
        e.setCancelled(BorderUtils.testBorder(e.getPlayer().getLocation(), e.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPistonPistonExtend(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            if (BorderUtils.testPiston(b.getLocation(), e.getDirection(), false)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPistonPistonRetract(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (BorderUtils.testPiston(b.getLocation(), e.getDirection(), true)) {
                e.setCancelled(true);
                return;
            }
        }
    }
}
