package com.Stranded.towers.effects;

import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ArrowEffect implements Runnable {

    private Entity entity;

    public void eff(Main p, Entity pl, int lvl) {

        entity = pl;

        int amount = 1;

        if (lvl == 1) {
            amount = 5;
        } else if (lvl == 2) {
            amount = 10;
        } else if (lvl == 3) {
            amount = 15;
        } else if (lvl == 4) {
            amount = 20;
        } else if (lvl == 5) {
            amount = 25;
        } else if (lvl == 6) {
            amount = 30;
        } else if (lvl == 7) {
            amount = 35;
        } else if (lvl == 8) {
            amount = 40;
        } else if (lvl == 0) {
            amount = 45;
        }

        for (int i = 1; i <= amount; i++) {

            Bukkit.getScheduler().scheduleSyncDelayedTask(p, this, i*2);

        }
    }

    @Override
    public void run() {
        Location l1 = new Location(entity.getLocation().getWorld(), entity.getLocation().getX(), entity.getLocation().getY() + 5, entity.getLocation().getZ());
        Location l2 = entity.getLocation();

        double dX = l1.getX() - l2.getX();
        double dY = l1.getY() - l2.getY();
        double dZ = l1.getZ() - l2.getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double X = Math.sin(pitch) * Math.cos(yaw);
        double Y = Math.sin(pitch) * Math.sin(yaw);
        double Z = Math.cos(pitch);

        Vector vector = new Vector(X, Z, Y);

        Arrow arrow = entity.getWorld().spawn(new Location(entity.getLocation().getWorld(), entity.getLocation().getX(),
                entity.getLocation().getY() + 4, entity.getLocation().getZ()), Arrow.class);

        arrow.setVelocity(vector);
    }
}