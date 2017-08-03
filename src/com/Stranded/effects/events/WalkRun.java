package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class WalkRun implements Listener {

    private Main p;

    public WalkRun(Main instance) {
        p = instance;
    }

    private HashMap<Player, Integer> X = new HashMap<>();
    private HashMap<Player, Integer> Z = new HashMap<>();

    @EventHandler
    @SuppressWarnings("unused")
    public void walk(PlayerMoveEvent e) {

        Files save = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        Player player = e.getPlayer();
        try {
            if (X.get(player) != player.getLocation().getBlockX()
                    || Z.get(player) != player.getLocation().getBlockZ()) {

                if (save.getConfig().getLong("walk." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier") != 100) {

                    save.getConfig().set("walk." + player.getName(), save.getConfig().getLong("walk." + player.getName()) + 1);
                    save.saveConfig();
                    if (save.getConfig().getDouble("walk." + player.getName()) / pluginData.getConfig().getDouble("plugin.scoreboard.walking.amplifier") == 20) {
                        player.sendMessage("slowness is removed");
                        player.removePotionEffect(PotionEffectType.SLOW);

                    }
                }
            }
        } catch (NullPointerException npe) {
            X.put(player, player.getLocation().getBlockX());
            Z.put(player, player.getLocation().getBlockZ());
        }

        X.put(player, player.getLocation().getBlockX());
        Z.put(player, player.getLocation().getBlockZ());
    }
}
