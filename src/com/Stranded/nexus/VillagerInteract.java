package com.Stranded.nexus;

import com.Stranded.Main;
import com.Stranded.nexus.inv.InvMain;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.ArrayList;

public class VillagerInteract implements Listener {

    private Main p;

    public VillagerInteract(Main main) {
        p = main;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void EntityInteract(PlayerInteractEntityEvent e) {

        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();

        if (e.getRightClicked() instanceof Villager) {
            ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");
            if (list.contains(e.getRightClicked().getUniqueId().toString())) {

                InvMain.openInv(p, player);

                e.setCancelled(true);
            }
        }
    }

}
