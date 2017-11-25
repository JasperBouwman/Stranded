package com.Stranded.commands;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import java.util.ArrayList;

public class Nexus implements CommandExecutor {

    private Main p;

    public Nexus(Main instance) {
        p = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        //nexus
        //nexus <island>

        if (!(sender instanceof Player)) {
            sender.sendMessage("you must be a player to use this");
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("Stranded.spawnNexus")) {
            player.sendMessage("y u no permission");
            return false;
        }
        if (args.length == 0) {
            spawnNexus(player);
        } else if (args.length == 1) {
            Files islands = new Files(p, "islands.yml");
            if (islands.getConfig().contains("island." + args[0])) {
                islands.getConfig().set("island." + args[0] + ".UUID", spawnNexus(player));
                islands.saveConfig();
                player.sendMessage("nexus spawned");
            } else {
                player.sendMessage("this island doesn't exist");
            }

        } else {
            player.sendMessage("Usage: /nexus <island> or /nexus");
        }

        return false;

    }

    private String spawnNexus(Player player) {

        Villager v = player.getLocation().getWorld().spawn(player.getLocation(), Villager.class);

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("nexus.uuid");
        list.add(v.getUniqueId().toString());
        p.getConfig().set("nexus.uuid", list);
        p.saveConfig();

        v.setCustomName("§2Nexus");
        v.setCustomNameVisible(true);
        v.setProfession(Profession.NITWIT);
        v.setAI(false);
        v.isSilent();
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);

        return v.getUniqueId().toString();
    }
}
