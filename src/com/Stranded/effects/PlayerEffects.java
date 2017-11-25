package com.Stranded.effects;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.warIsland.Pos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.UUID;

public class PlayerEffects implements Runnable {

    private Main p;

    public PlayerEffects(Main main) {
        p = main;
    }

    private void walkEffect(Files playerData, Files pluginData) {
        if (playerData.getConfig().contains("walk")) {

            // effect for walking
            for (String s : playerData.getConfig().getConfigurationSection("walk").getKeys(false)) {

                Player player = Bukkit.getPlayer(UUID.fromString(s));

                if (player == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("walk." + s) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier");

                if (l < 20 && l >= 0) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 0, true, false));
                } else if (l < 60 && l >= 40) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0, true, false));
                } else if (l < 80 && l >= 60) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false));
                } else if (l < 100 && l >= 80) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 0, true, false));
                } else if (l == 100) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 1, true, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 999999, 0));
                }
            }
        }
    }

    private void flyEffect(Files playerData, Files pluginData) {
        if (playerData.getConfig().contains("fly")) {
            // effects for PvP
            for (String s : playerData.getConfig().getConfigurationSection("fly").getKeys(false)) {

                Player player = Bukkit.getPlayer(UUID.fromString(s));

                if (player == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("fly." + s) / pluginData.getConfig().getInt("plugin.scoreboard.flying.amplifier");

                if (!player.getInventory().getItemInOffHand().getType().equals(Material.AIR)) {
                    continue;
                }

                if (!player.isGliding()) {
                    continue;
                }

                int lvl1 = 25;
                int lvl2 = 50;
                int lvl3 = 75;
                int lvl4 = 100;
                int lvl5 = 300;

                int random = new Random().nextInt(1000);

                if (l <= 40 && l >= 20) {
                    if (random <= lvl1) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.FIREWORK));
                    }
                } else if (l <= 60 && l >= 40) {
                    if (random <= lvl2) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.FIREWORK));
                    }
                } else if (l <= 80 && l >= 60) {
                    if (random <= lvl3) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.FIREWORK));
                    }
                } else if (l < 100 && l >= 80) {
                    if (random <= lvl4) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.FIREWORK));
                    }
                } else if (l == 100) {
                    if (random <= lvl5) {
                        player.getInventory().setItemInOffHand(new ItemStack(Material.FIREWORK));
                    }
                }
            }
        }
    }

    private void mineEffect(Files playerData, Files pluginData) {
        if (playerData.getConfig().contains("BlockBreak")) {

            // effects for mining
            for (String s : playerData.getConfig().getConfigurationSection("BlockBreak").getKeys(false)) {

                Player player = Bukkit.getPlayer(UUID.fromString(s));

                if (player == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("BlockBreak." + s) / pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier");

                if (l < 40 && l >= 20) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0, true, false));
                } else if (l < 60 && l >= 40) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1, true, false));
                } else if (l < 80 && l >= 60) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 2, true, false));
                } else if (l < 100 && l >= 80) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 3, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false));
                } else if (l == 100) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 3, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                }
            }
        }
    }

    private void pvpEffect(Files playerData, Files pluginData) {
        if (playerData.getConfig().contains("HitKill")) {
            // effects for PvP
            for (String s : playerData.getConfig().getConfigurationSection("HitKill").getKeys(false)) {

                Player player = Bukkit.getPlayer(UUID.fromString(s));

                if (player == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("HitKill." + s) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier");

                if (l <= 40 && l >= 20) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false));
                } else if (l <= 60 && l >= 40) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                } else if (l <= 80 && l >= 60) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.REGENERATION, 999999, 0, true, false));
                } else if (l < 100 && l >= 80) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.REGENERATION, 999999, 0, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, true, false));
                } else if (l == 100) {
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.REGENERATION, 999999, 1, true, false));
                    player.addPotionEffect(
                            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 1, true, false));
                }
            }
        }
    }

    public void Effects() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, this, 20, 20);
    }

    @Override
    public void run() {
        Files playerData = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        walkEffect(playerData, pluginData);
        flyEffect(playerData, pluginData);
        mineEffect(playerData, pluginData);
        pvpEffect(playerData, pluginData);

        Pos.showOffset(p);
        Pos.showBoundary(p);

    }
}
