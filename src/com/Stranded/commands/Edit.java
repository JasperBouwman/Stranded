package com.Stranded.commands;

import com.Stranded.worldGeneration.Generator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Edit implements CommandExecutor {

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

            for (double x = 1; x <= 100; x++) {

                double y = Math.sinh((x + 30)/20);

                player.sendMessage(x + " " + y);

            }

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
