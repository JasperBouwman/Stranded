package com.Stranded.towers.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedEffect {

    public static void eff(Player player, int lvl) {

        int strength = 0;
        int length = 1;

        if (lvl == 0) {
            strength = 1;
        }

        length = i(lvl, length);

        player.sendMessage("you just got speed " + (strength + 1) + " for " + length / 20 + " seconds");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, length, strength, false, false));

    }
    public static int i(int lvl, int length) {
        if (lvl == 1) {
            length = 20;
        } else if (lvl == 2) {
            length = 60;
        } else if (lvl == 3) {
            length = 100;
        } else if (lvl == 4) {
            length = 120;
        } else if (lvl == 5) {
            length = 140;
        } else if (lvl == 6) {
            length = 160;
        } else if (lvl == 7) {
            length = 180;
        } else if (lvl == 8) {
            length = 200;
        } else if (lvl == 0) {
            length = 200;

        }
        return length;
    }
}
