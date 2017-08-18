package com.Stranded.towers.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RegenerationEffect {

    public static void eff(Player player, int lvl) {

        int strength = 0;
        int length = 1;

        if (lvl == 1) {
            length = 20;
        } else if (lvl == 2) {
            length = 40;
        } else if (lvl == 3) {
            length = 60;
        } else if (lvl == 4) {
            length = 60;
        } else if (lvl == 5) {
            length = 80;
        } else if (lvl == 6) {
            length = 80;
        } else if (lvl == 7) {
            length = 100;
        } else if (lvl == 8) {
            length = 100;
        } else if (lvl == 0) {
            length = 120;
        }
        player.sendMessage("you just got regeneration " + (strength + 1) + " for " + length / 20 + " seconds");
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, length, strength, false, false));

    }
}
