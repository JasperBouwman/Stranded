package com.Stranded.towers.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;

public class TntEffect {

    public static void eff(Player player, Location l) {

//        Location l1 = l;
        Location l2 = player.getLocation();

        double dX = l.getX() - l2.getX();
        double dY = l.getY() - l2.getY();
        double dZ = l.getZ() - l2.getZ();
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        double X = Math.sin(pitch) * Math.cos(yaw);
        double Y = Math.sin(pitch) * Math.sin(yaw);
        double Z = Math.cos(pitch);

        Vector vector = new Vector(X, Z, Y);

        TNTPrimed tnt = player.getWorld().spawn(new Location(l.getWorld(), l.getX(), l.getY() + 4, l.getZ()), TNTPrimed.class);

        tnt.setVelocity(vector);

        tnt.setFuseTicks(35);
    }
}
