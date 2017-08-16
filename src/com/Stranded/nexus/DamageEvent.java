package com.Stranded.nexus;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvent implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityHit(EntityDamageEvent e) {

        if (e.getEntity().getName().equals("§2Nexus") && e.getEntity() instanceof Villager) {
            e.setCancelled(true);
        }
    }
}
