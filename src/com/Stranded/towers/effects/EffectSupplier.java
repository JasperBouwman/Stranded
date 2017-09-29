package com.Stranded.towers.effects;

import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;

class EffectSupplier {

    EffectSupplier(Entity entity, PotionEffect potionEffect) {


        if (entity instanceof Cow) {
            Cow cow = (Cow) entity;
            cow.addPotionEffect(potionEffect);
        }
        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep) entity;
            sheep.addPotionEffect(potionEffect);
        }
        if (entity instanceof Pig) {
            Pig pig = (Pig) entity;
            pig.addPotionEffect(potionEffect);
        }
        if (entity instanceof Chicken) {
            Chicken chicken = (Chicken) entity;
            chicken.addPotionEffect(potionEffect);
        }
        if (entity instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) entity;
            rabbit.addPotionEffect(potionEffect);
        }

        if (entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;
            zombie.addPotionEffect(potionEffect);
        }
        if (entity instanceof Creeper) {
            Creeper creeper = (Creeper) entity;
            creeper.addPotionEffect(potionEffect);
        }
        if (entity instanceof Enderman) {
            Enderman enderman = (Enderman) entity;
            enderman.addPotionEffect(potionEffect);
        }
        if (entity instanceof Evoker) {
            Evoker evoker = (Evoker) entity;
            evoker.addPotionEffect(potionEffect);
        }
        if (entity instanceof Ghast) {
            Ghast ghast = (Ghast) entity;
            ghast.addPotionEffect(potionEffect);
        }
        if (entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) entity;
            skeleton.addPotionEffect(potionEffect);
        }
        if (entity instanceof Spider) {
            Spider spider = (Spider) entity;
            spider.addPotionEffect(potionEffect);
        }
        if (entity instanceof Witch) {
            Witch witch = (Witch) entity;
            witch.addPotionEffect(potionEffect);
        }
    }
}
