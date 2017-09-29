package com.Stranded.towers.effects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class WitherEffect {
    public static void eff(Entity entity, int lvl) {

        int strength = 0;
        int length = 1;

        if (lvl == 1) {
            length = 20;
        } else if (lvl == 2) {
            length = 40;
        } else if (lvl == 3) {
            length = 60;
        } else if (lvl == 4) {
            length = 80;
        } else if (lvl == 5) {
            length = 80;
        } else if (lvl == 6) {
            length = 100;
        } else if (lvl == 7) {
            length = 100;
        } else if (lvl == 8) {
            length = 120;
        } else if (lvl == 0) {
            length = 140;
        }
        entity.sendMessage("you just got wither " + (strength + 1) + " for " + length / 20 + " seconds");

        new EffectSupplier(entity, new PotionEffect(PotionEffectType.WITHER, length, strength, false, false));

    }

}
