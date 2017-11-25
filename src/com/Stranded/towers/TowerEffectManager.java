package com.Stranded.towers;

import com.Stranded.Main;
import com.Stranded.towers.effects.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

class TowerEffectManager {

    private Main p;

    TowerEffectManager(Main main) {
        p = main;
    }

    void Effects(String effect, int lvl, Entity closestEnemy, Entity closestFriendly, Location l) {

        switch (effect) {
            case "Tnt":
                if (closestEnemy == null) {
                    return;
                }
                TntEffect.eff(closestEnemy, l);
                break;
            case "Slow":
                if (closestEnemy == null) {
                    return;
                }
                SlowEffect.eff(closestEnemy, lvl);
                break;
            case "Hunger":
                if (closestEnemy == null) {
                    return;
                }
                HungerEffect.eff(closestEnemy, lvl);
                break;
            case "Wither":
                if (closestEnemy == null) {
                    return;
                }
                WitherEffect.eff(closestEnemy, lvl);
                break;
            case "Speed":
                if (closestFriendly == null) {
                    return;
                }
                SpeedEffect.eff(closestFriendly, lvl);
                break;
            case "Haste":
                if (closestFriendly == null) {
                    return;
                }
                HasteEffect.eff(closestFriendly, lvl);
                break;
            case "Regeneration":
                if (closestFriendly == null) {
                    return;
                }
                RegenerationEffect.eff(closestFriendly, lvl);
                break;
            case "Arrow":
                if (closestEnemy == null) {
                    return;
                }
                new ArrowEffect().eff(p, closestEnemy, lvl);
                break;
        }
    }
}
