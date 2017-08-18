package com.Stranded.towers.events;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class Tnt implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        Entity e = event.getEntity();
        if (e instanceof TNTPrimed) {
            event.setCancelled(true);

            Location l = event.getEntity().getLocation();
            event.getEntity().getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 2f, false, false);
        }
    }
}
