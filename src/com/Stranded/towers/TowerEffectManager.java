package com.Stranded.towers;

import com.Stranded.Main;
import com.Stranded.towers.effects.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class TowerEffectManager {

    private  Main p;

    TowerEffectManager(Main main) {
        p = main;
    }

    void Effects(String effect, int lvl, Player closestEnemy, Player closestFriendly, Location l) {

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
//
//
//        if (effect.equals("Tnt")) {
//
//            if (closestEnemy == null) {
//                return;
//            }
//
//            TntEffect eff = new TntEffect();
//            eff.eff(closestEnemy, l);
//
//        } else if (effect.equals("Slow")) {
//
//            if (closestEnemy == null) {
//                return;
//            }
//
//            SlowEffect eff = new SlowEffect();
//            eff.eff(closestEnemy, lvl);
//
//        } else if (effect.equals("Hunger")) {
//
//            if (closestEnemy == null) {
//                return;
//            }
//
//            HungerEffect eff = new HungerEffect();
//            eff.eff(closestEnemy, lvl);
//
//        } else if (effect.equals("Wither")) {
//
//            if (closestEnemy == null) {
//                return;
//            }
//
//            WitherEffect eff = new WitherEffect();
//            eff.eff(closestEnemy, lvl);
//
//        } else if (effect.equals("Speed")) {
//
//            if (closestFriendly == null) {
//                return;
//            }
//
//            SpeedEffect eff = new SpeedEffect();
//            eff.eff(closestFriendly, lvl);
//
//        } else if (effect.equals("Haste")) {
//
//            if (closestFriendly == null) {
//                return;
//            }
//
//            HasteEffect eff = new HasteEffect();
//            eff.eff(closestFriendly, lvl);
//
//        } else if (effect.equals("Regeneration")) {
//
//            if (closestFriendly == null) {
//                return;
//            }
//
//            RegenerationEffect eff = new RegenerationEffect();
//            eff.eff(closestFriendly, lvl);
//
//        } else if (effect.equals("Arrow")) {
//
//            if (closestEnemy == null) {
//                return;
//            }
//
//            ArrowEffect eff = new ArrowEffect(p, closestEnemy);
//            eff.eff(lvl);
//
//        }
    }
}
