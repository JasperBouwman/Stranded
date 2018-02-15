package com.Stranded.border.warLimitations;

import com.Stranded.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.Stranded.commands.war.util.WarUtil.testIfPlayerIsInWarFast;

public class WarLimitations implements Listener {

    private Main p;

    public WarLimitations(Main main) {
        p = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void BlockBreak(BlockBreakEvent e) {
        if (testIfPlayerIsInWarFast(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void BlockPlace(BlockPlaceEvent e) {
        if (testIfPlayerIsInWarFast(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void Hit(EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player) && testIfPlayerIsInWarFast((Player) e.getDamager())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (testIfPlayerIsInWarFast(e.getPlayer())) {
                e.setCancelled(true);
            }
        }//todo test this
    }
}
