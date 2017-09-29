package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class HitKill implements Listener {

    private Main p;

    public HitKill(Main instance) {
        p = instance;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Files save = new Files(p, "playerData.yml");
            Files pluginData = new Files(p, "pluginData.yml");
            if (save.getConfig().getLong("HitKill." + e.getDamager().getName()) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier") != 100) {
                Player player = (Player) e.getDamager();
                save.getConfig().set("HitKill." + player.getName(), save.getConfig().getLong("HitKill." + player.getName()) + 1);
                save.saveConfig();
            } else if (save.getConfig().getLong("HitKill." + e.getDamager().getName()) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier") == 100) {
                LivingEntity damaged = (LivingEntity) e.getEntity();
                double damage = e.getDamage();
                double y = damage*damage/40;
                damaged.setHealth(damaged.getHealth() - y);
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityKill(EntityDeathEvent e) {


        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            Files save = new Files(p, "playerData.yml");
            Files pluginData = new Files(p, "pluginData.yml");
            if (save.getConfig().getLong("HitKill." + e.getEntity().getKiller().getName()) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier") != 100) {
                Player killer = e.getEntity().getKiller();
                save.getConfig().set("HitKill." + killer.getName(), save.getConfig().getLong("HitKill." + killer.getName()) + 1);
                save.saveConfig();
            }
        }
    }
}
