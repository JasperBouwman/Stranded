package com.Stranded.effects.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WalkRun implements Listener {

    private Main p;
    private HashMap<Player, Integer> X = new HashMap<>();
    private HashMap<Player, Integer> Y = new HashMap<>();
    private HashMap<Player, Integer> Z = new HashMap<>();

    public WalkRun(Main instance) {
        p = instance;
    }


    private void revertUsage(Player player, Files playerData) {
        if (playerData.getConfig().contains("flying.players." + player.getUniqueId().toString())) {

            ItemStack is = player.getInventory().getChestplate();

            List<Short> durability = playerData.getConfig().getShortList("flying.players." + player.getUniqueId().toString());
            is.setDurability(durability.get(0));
            playerData.getConfig().set("flying.players." + player.getUniqueId().toString(), null);
            playerData.saveConfig();
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void walk(PlayerMoveEvent e) {

        Files playerData = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString();
        try {
            if (X.get(player) != player.getLocation().getBlockX() ||
                    Z.get(player) != player.getLocation().getBlockZ() ||
                    Y.get(player) != player.getLocation().getBlockY()) {

                if (player.isGliding()) {
                    int amplifier = pluginData.getConfig().getInt("plugin.scoreboard.flying.amplifier");
                    if (playerData.getConfig().getLong("fly." + uuid) / amplifier != 100) {
                        long oldScore = playerData.getConfig().getLong("fly." + uuid);
                        playerData.getConfig().set("fly." + uuid, oldScore + 1);
                        playerData.saveConfig();
                        if (oldScore / amplifier != oldScore + 1 / amplifier) {
                            Scoreboard.scores(p, player);
                        }
                    } else if (playerData.getConfig().getLong("fly." + uuid) / amplifier == 100) {
                        if (!playerData.getConfig().contains("flying.players." + uuid)) {
                            if (player.getInventory().getChestplate().getType().equals(Material.ELYTRA)) {
                                ArrayList<Short> list = new ArrayList<>();
                                list.add(player.getInventory().getChestplate().getDurability());
                                playerData.getConfig().set("flying.players." + uuid, list);
                                playerData.saveConfig();
                            }
                        }

                    }
                    return;
                } else {
                    revertUsage(player, playerData);
                }


                int amplifier = pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier");
                if (playerData.getConfig().getLong("walk." + uuid) / amplifier != 100) {

                    if (X.get(player) != player.getLocation().getBlockX() ||
                            Z.get(player) != player.getLocation().getBlockZ()) {
                        long oldScore = playerData.getConfig().getLong("walk." + uuid);
                        playerData.getConfig().set("walk." + uuid, oldScore + 1);
                        playerData.saveConfig();
                        if (oldScore / amplifier != (oldScore + 1) / amplifier) {
                            Scoreboard.scores(p, player);
                        }
                        if ((oldScore + 1) == 20 * amplifier) {
                            player.sendMessage("slowness is removed");
                            player.removePotionEffect(PotionEffectType.SLOW);

                        }
                    }
                } else if (playerData.getConfig().getLong("walk." + uuid) / amplifier == 100) {

                    Location l = player.getLocation();
                    if (player.getInventory().getBoots().getType().toString().endsWith("BOOTS") &&
                            !new Location(l.getWorld(), X.get(player), l.getY() - 1, Z.get(player)).getBlock().getType().equals(Material.STATIONARY_WATER)) {

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
