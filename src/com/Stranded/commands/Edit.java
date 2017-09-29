package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.PlayerList;
import com.Stranded.UpdateTabList;
import com.Stranded.worldGeneration.Generator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

            new UpdateTabList(player, p);

        }


        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("test")) {
                PlayerList list = PlayerList.getPlayerList(player);
                list.updateSlot(2, "lol");
                return false;
            }
            if (args[0].equalsIgnoreCase("rem")) {
                PlayerList list = PlayerList.getPlayerList(player);
                list.addExistingPlayer(3, Bukkit.getOfflinePlayer("7he_4ch3r"));
                return false;
            }

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
