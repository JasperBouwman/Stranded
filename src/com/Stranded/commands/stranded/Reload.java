package com.Stranded.commands.stranded;

import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Reload extends CmdManager {

    public static void reload(Main p) {
        //todo send reload message
        Bukkit.getScheduler().cancelTasks(p);
        Bukkit.getScheduler().runTaskLater(p, Bukkit::reload, 1);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getAlias() {
        return null;
    }

    public void run(String[] args, ConsoleCommandSender sender) {
        //stranded reload force
        //stranded reload

        if (args.length == 2 && args[0].equalsIgnoreCase("force")) {
            sender.sendMessage("The server is now forced reloading");

            Main.reloadHolds = 0;
            reload(p);
            return;
        } else if (args.length != 1) {
            sender.sendMessage("Usage: /stranded reload \nUsage: /stranded reload force");
            return;
        }

        if (Main.reloadHolds == 0 && !Main.reloadPending) {
            sender.sendMessage("reloading");
            Bukkit.reload();
        } else {
            sender.sendMessage("the server will reload when Stranded is done with some stuff\n" +
                    "if the server isn't reloading soon you will have to force the reload using '/stranded reload force'");
            Main.reloadPending = true;

        }
    }

    @Override
    public void run(String[] args, Player player) {

        if (args.length == 1 && args[0].equalsIgnoreCase("force")) {
            player.sendMessage("the server is now forced reloading");

            Main.reloadHolds = 0;
            reload(p);
            return;
        } else if (args.length != 0) {
            player.sendMessage("Usage: /stranded reload \nUsage: /stranded reload force");
            return;
        }

        if (Main.reloadHolds == 0 && !Main.reloadPending) {
            player.sendMessage("reloading");
            Bukkit.reload();
        } else {
            player.sendMessage("the server is reloading when Stranded is done with some stuff\n" +
                    "if the server isn't reloading soon you will have to force the reload using '/r force'");
            Main.reloadPending = true;
        }
    }
}
