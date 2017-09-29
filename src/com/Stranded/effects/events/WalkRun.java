package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class WalkRun implements Listener {

    private Main p;
    private HashMap<Player, Integer> X = new HashMap<>();
    private HashMap<Player, Integer> Y = new HashMap<>();
    private HashMap<Player, Integer> Z = new HashMap<>();

    public WalkRun(Main instance) {
        p = instance;
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void walk(PlayerMoveEvent e) {

        Files save = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        Player player = e.getPlayer();
        try {
            if (X.get(player) != player.getLocation().getBlockX() ||
                    Z.get(player) != player.getLocation().getBlockZ() ||
                    Y.get(player) != player.getLocation().getBlockY()) {

                if (player.isGliding()) {

                    if (save.getConfig().getLong("fly." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.flying.amplifier") != 100) {
                        save.getConfig().set("fly." + player.getName(), save.getConfig().getLong("fly." + player.getName()) + 1);
                        save.saveConfig();
                    }


                    return;
                }
                if (save.getConfig().getLong("walk." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier") != 100) {

                    if (X.get(player) != player.getLocation().getBlockX() ||
                            Z.get(player) != player.getLocation().getBlockZ()) {

                        save.getConfig().set("walk." + player.getName(), save.getConfig().getLong("walk." + player.getName()) + 1);
                        save.saveConfig();
                        if (save.getConfig().getDouble("walk." + player.getName()) / pluginData.getConfig().getDouble("plugin.scoreboard.walking.amplifier") == 20) {
                            player.sendMessage("slowness is removed");
                            player.removePotionEffect(PotionEffectType.SLOW);

                        }
                    }
                } else if (save.getConfig().getLong("walk." + player.getName()) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier") == 100) {
                    Location l = player.getLocation();
                    if (player.getInventory().getBoots().getType().toString().endsWith("BOOTS") && !new Location(l.getWorld(), X.get(player), l.getY() - 1, Z.get(player)).getBlock().getType().equals(Material.STATIONARY_WATER)) {

                        l.setY(l.getY() - 1);
                        int x = l.getBlockX();
                        int z = l.getBlockZ();

                        for (int xx = x - 2; xx < (x + 3); xx++) {
                            for (int zz = z - 2; zz < (z + 3); zz++) {

                                Block block = new Location(l.getWorld(), xx, l.getY(), zz).getBlock();

                                if ((block.getType().equals(Material.STATIONARY_WATER) || block.getType().equals(Material.WATER)) && new Location(l.getWorld(), xx, l.getY() + 1, zz).getBlock().getType().equals(Material.AIR)) {
                                    block.setType(Material.FROSTED_ICE);
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException npe) {
            X.put(player, player.getLocation().getBlockX());
            Y.put(player, player.getLocation().getBlockZ());
            Z.put(player, player.getLocation().getBlockZ());
        }

        X.put(player, player.getLocation().getBlockX());
        Y.put(player, player.getLocation().getBlockZ());
        Z.put(player, player.getLocation().getBlockZ());
    }
}
