package com.Stranded.towers.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.Stranded.towers.effects.SpeedEffect.i;

public class SlowEffect {

    public void eff(Player player, int lvl) {

        int strength = 0;
        int length = 1;
        length = i(lvl, length);

        player.sendMessage("you just got slowness " + (strength + 1) + " for " + length / 20 + " seconds");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, length, strength, false, false));

    }

}
