package com.Stranded.islandBorder.events;

import com.Stranded.Main;
import com.Stranded.islandBorder.BorderUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import static com.Stranded.commands.warIsland.Create.wandStuff;

public class BorderEvents implements Listener {

    private Main p;

    public BorderEvents(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void BlockBreak(BlockBreakEvent e) {
        e.setCancelled(BorderUtils.border(e.getBlock().getLocation(), p, e.getPlayer()));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void BlockPlace(BlockPlaceEvent e) {
        e.setCancelled(BorderUtils.border(e.getBlock().getLocation(), p, e.getPlayer()));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void Hit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player)
            e.setCancelled(BorderUtils.border(e.getEntity().getLocation(), p, (Player) e.getDamager()));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent e) {

        e.setCancelled(wandStuff(e, p));

        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            e.setCancelled(BorderUtils.border(e.getClickedBlock().getLocation(), p, e.getPlayer()));

            return;
        }

        e.setCancelled(BorderUtils.border(e.getPlayer().getLocation(), p, e.getPlayer()));

    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(BorderUtils.border(e.getRightClicked().getLocation(), p, e.getPlayer()));
    }

    @EventHandler
    @SuppressWarnings({"unused", "deprecation"})
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        e.setCancelled(BorderUtils.border(e.getPlayer().getLocation(), p, e.getPlayer()));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        e.setCancelled(BorderUtils.border(e.getPlayer().getLocation(), p, e.getPlayer()));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPlayerPickupArrow(PlayerPickupArrowEvent e) {
        e.setCancelled(BorderUtils.border(e.getPlayer().getLocation(), p, e.getPlayer()));
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPistonPistonExtend(BlockPistonExtendEvent e) {

        for (Block b : e.getBlocks()) {
            if (BorderUtils.border(b.getLocation(), p)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onPistonPistonRetract(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            if (BorderUtils.border(b.getLocation(), p)) {
                e.setCancelled(true);
                return;
            }
        }
    }

}
