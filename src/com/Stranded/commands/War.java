package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.war.Accept;
import com.Stranded.commands.war.Ready;
import com.Stranded.commands.war.Start;
import com.Stranded.lootTable.LootBox;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class War implements CommandExecutor {

    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public War(Main main) {
        p = main;

        actions.add(new Start());
        actions.add(new Accept());
        actions.add(new Ready());
    }

    private double pi(long accuracy) {
        double pi = 0;
        boolean b = true;
        for (long i = 1; i < accuracy; i = i + 2) {
            if (b) {
                pi += 4D / i;
            } else {
                pi -= 4D / i;
            }
            b = !b;
        }
        return pi;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //war
        //war start <war theme> [playerNames...]
        //war accept <island2>
        //war accept <island1> [players...]
        //war ready

        if (!(sender instanceof Player)) {
            sender.sendMessage("I don't know what you are!");
            return false;
        }

        Player player = (Player) sender;

        if (Main.reloadPending) {
            player.sendMessage("the server is trying to reload, please wait just a second to let the server reload");
            return false;
        }

        if (args.length == 0) {
            new Thread(() -> Main.println(pi(Long.MAX_VALUE) + "")).start();
            Main.println("some text");
            return false;
        }

        if (args.length == 1) {
            player.openInventory(new LootBox(p, args[0]).getLootBox(LootBox.lootBoxSize.SMALL));

            return false;
        }

        for (CmdManager action : this.actions) {
            if (args[0].toLowerCase().equals(action.getName()) || args[0].toLowerCase().equals(action.getAlias())) {
                action.setMain(p);
                action.run(args, player);
                return false;
            }
        }

        player.sendMessage("this is not a sub-command");
        return false;

    }
}
