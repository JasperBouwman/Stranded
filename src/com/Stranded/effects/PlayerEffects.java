package com.Stranded.effects;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEffects implements Runnable {

    private Main p;

    public PlayerEffects(Main main) {
        p = main;
    }

    public void Effects() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(p, this, 20, 20);
    }


    @Override
    public void run() {
        Files playerData = new Files(p, "playerData.yml");
        Files pluginData = new Files(p, "pluginData.yml");

        if (playerData.getConfig().contains("walk")) {

            // effect for walking
            for (String s : playerData.getConfig().getConfigurationSection("walk").getKeys(false)) {

                if (Bukkit.getServer().getPlayerExact(s) == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("walk." + s) / pluginData.getConfig().getInt("plugin.scoreboard.walking.amplifier");

                if (l < 20 && l >= 0) {

                    Bukkit.getServer().getPlayerExact(s)
                            .addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999, 0, true, false));

                } else if (l < 60 && l >= 40) {

                    Bukkit.getServer().getPlayerExact(s)
                            .addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0, true, false));

                } else if (l < 80 && l >= 60) {

                    Bukkit.getServer().getPlayerExact(s)
                            .addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false));

                } else if (l < 100 && l >= 80) {

                    Bukkit.getServer().getPlayerExact(s)
                            .addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 1, true, false));
                    Bukkit.getServer().getPlayerExact(s)
                            .addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 999999, 0, true, false));

                } else if (l == 100) {
                    //todo

                }
            }
        }
        if (playerData.getConfig().contains("BlockBreak")) {

            // effects for mining
            for (String s : playerData.getConfig().getConfigurationSection("BlockBreak").getKeys(false)) {

                if (Bukkit.getServer().getPlayerExact(s) == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("BlockBreak." + s) / pluginData.getConfig().getInt("plugin.scoreboard.mining.amplifier");

                if (l < 40 && l >= 20) {


                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 0, true, false));


                } else if (l < 60 && l >= 40) {

                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1, true, false));

                } else if (l < 80 && l >= 60) {

                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 2, true, false));

                } else if (l < 100 && l >= 80) {

                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 3, true, false));
                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false));

                } else if (l == 100) {
                    //todo

                }
            }
        }
        if (playerData.getConfig().contains("HitKill")) {
            // effects for PvP
            for (String s : playerData.getConfig().getConfigurationSection("HitKill").getKeys(false)) {

                if (Bukkit.getServer().getPlayerExact(s) == null) {
                    continue;
                }

                long l = playerData.getConfig().getLong("HitKill." + s) / pluginData.getConfig().getInt("plugin.scoreboard.pvp.amplifier");

                if (l <= 40 && l >= 20) {


                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 0, true, false));


                } else if (l <= 60 && l >= 40) {

                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));

                } else if (l <= 80 && l >= 60) {

                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.REGENERATION, 999999, 0, true, false));

                } else if (l < 100 && l >= 80) {

                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 999999, 1, true, false));
                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.REGENERATION, 999999, 0, true, false));
                    Bukkit.getServer().getPlayerExact(s).addPotionEffect(
                            new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, true, false));

                } else if (l == 100) {
                    //todo

                }
            }
        }
    }
}
