package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.worldGeneration.Generator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Edit implements CommandExecutor {

    private Main p;

    public Edit(Main instance) {
        p = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("Stranded.edit")) {
            player.sendMessage("you don't have the permission to use this");
            return false;
        }

        if (args.length == 0) {

            int ping = ((CraftPlayer) player).getHandle().ping;
            player.sendMessage(ping + "");

        }

        if (args.length == 1) {
            try {
                World w = Bukkit.getWorld(args[0]);
                player.teleport(w.getSpawnLocation());
            } catch (NullPointerException npe) {
                player.sendMessage("world doesn't exist");
            }
        }
        if (args.length == 2 && args[0].equals("create")) {

            WorldCreator WC = new WorldCreator(args[1]);

            Generator g = new Generator();

            WC.generator(g);

            Bukkit.getServer().createWorld(WC);
        }


        return false;
    }
}
