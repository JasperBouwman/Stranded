package com.Stranded.towers.effects;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HungerEffect {
    public void eff(Player player, int lvl) {

        int strengt = 0;
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
            length = 100;
        } else if (lvl == 6) {
            length = 120;
        } else if (lvl == 7) {
            length = 140;
        } else if (lvl == 8) {
            length = 160;
        } else if (lvl == 0) {
            length = 200;
            strengt = 1;
        }
        player.sendMessage("you just got hunger " + (strengt + 1) + " for " + length / 20 + " seconds");
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, length, strengt, false, false));

    }
}
