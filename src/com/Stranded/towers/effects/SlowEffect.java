package com.Stranded.towers.effects;

import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.Stranded.towers.effects.SpeedEffect.i;

public class SlowEffect {
    public static void eff(Entity entity, int lvl) {

        int strength = 0;
        int length = 1;
        length = i(lvl, length);

        entity.sendMessage("you just got slowness " + (strength + 1) + " for " + length / 20 + " seconds");

        new EffectSupplier(entity, new PotionEffect(PotionEffectType.SLOW, length, strength, false, false));

    }

}
