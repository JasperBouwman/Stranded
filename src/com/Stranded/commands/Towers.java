package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.tower.Own;
import com.Stranded.commands.tower.Shop;
import com.Stranded.towers.inventory.InvTower;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Towers implements CommandExecutor {

    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public Towers(Main main) {
        p = main;

        actions.add(new Own());
        actions.add(new Shop());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s2, String[] args) {

        //tower
        //tower own
        //tower own [all:tnt:slowness:hunger:wither:arrow:haste:regeneration:speed]
        //tower shop
        //tower show [friendly:enemy:tnt:slowness:hunger:wither:arrow:haste:regeneration:speed]

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player to use this");
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 0) {
            InvTower.openInv(player);
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

        /*
        * /tower
        * /tower [own:shop]
        * /tower own [filters:all]
        * /tower shop [friendly:enemy:towers]
        */

    }
}
