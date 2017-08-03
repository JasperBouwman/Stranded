package com.Stranded.commands;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.islandBorder.BorderUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

public class Nexus implements CommandExecutor {

    private Main p;

    public Nexus(Main instance) {
        p = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("you must be a player to use this");
            return false;
        }
        Player player = (Player) sender;

        spawnNexus(player);

        return false;

    }

    private void spawnNexus(Player player) {

        Villager v = player.getLocation().getWorld().spawn(player.getLocation(), Villager.class);

        v.setCustomName("§2Nexus");
        v.setCustomNameVisible(true);
        v.setProfession(Profession.NITWIT);
        v.setAI(false);
        v.isSilent();
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);
    }
}
