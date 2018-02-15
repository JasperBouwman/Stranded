package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import static com.Stranded.GettingFiles.getFiles;

public class HitKill implements Listener {

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Files playerData = getFiles("playerData.yml");
            Files pluginData = getFiles("pluginData.yml");
            int amplifier = pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier");
            if (playerData.getConfig().getLong("HitKill." + e.getDamager().getName()) / amplifier != 100) {
                Player player = (Player) e.getDamager();
                String uuid = player.getUniqueId().toString();
                long oldScore = playerData.getConfig().getLong("HitKill." + uuid);
                playerData.getConfig().set("HitKill." + uuid, +1);
                playerData.saveConfig();
                if (oldScore / amplifier != oldScore + 1 / amplifier) {
                    Scoreboard.scores(player);
                }
            } else if (playerData.getConfig().getLong("HitKill." + e.getDamager().getName()) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier") == 100) {
                LivingEntity damaged = (LivingEntity) e.getEntity();
                double damage = e.getDamage();
                double y = damage * damage / 40;
                damaged.setHealth(damaged.getHealth() - y);
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() instanceof Player) {
            Files playerData = getFiles("playerData.yml");
            Files pluginData = getFiles("pluginData.yml");
            int amplifier = pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier");
            if (playerData.getConfig().getLong("HitKill." + e.getEntity().getKiller().getUniqueId().toString()) / amplifier != 100) {
                Player killer = e.getEntity().getKiller();
                long oldScore = playerData.getConfig().getLong("HitKill." + killer.getUniqueId().toString());
                playerData.getConfig().set("HitKill." + killer.getUniqueId().toString(), oldScore + 1);
                playerData.saveConfig();
                if (oldScore / amplifier != oldScore + 1 / amplifier) {
                    Scoreboard.scores(e.getEntity().getKiller());
                }
            }
        }
    }
}
