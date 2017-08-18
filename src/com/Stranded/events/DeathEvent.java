package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathEvent implements Listener {

    private Main p;

    public DeathEvent(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void Death(PlayerDeathEvent e) {

        if (p.getConfig().contains("warMode." + e.getEntity().getPlayer().getName())) {
            if (p.getConfig().getBoolean("warMode." + e.getEntity().getPlayer().getName())) {
                Files f = new Files(p, "islands.yml");
                Location l = (Location) f.getConfig().get("island." + p.getConfig().getString("island." + e.getEntity().getPlayer().getName()) + ".location");
                e.getEntity().getPlayer().teleport(l);
            }
        }
    }
}
