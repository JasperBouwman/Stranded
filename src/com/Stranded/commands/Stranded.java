package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.stranded.Hub;
import com.Stranded.commands.stranded.PlayerEffects;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.commands.stranded.Scoreboard;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Stranded implements CommandExecutor {

    public ArrayList<CmdManager> actions = new ArrayList<>();

    private Main p;

    public Stranded(Main main) {
        p = main;

        actions.add(new Hub());
        actions.add(new Scoreboard());
        actions.add(new Reload());
        actions.add(new PlayerEffects());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        //stranded
        //stranded reload
        //stranded reload force
        //stranded scoreboard
        //stranded scoreboard name
        //stranded scoreboard name <name>
        //stranded scoreboard addLine <line> [text...]
        //stranded scoreboard setLine <line> [text...]
        //stranded playerEffects
        //stranded playerEffects walking
        //stranded playerEffects walking <amplifier>
        //stranded playerEffects pvp
        //stranded playerEffects pvp <amplifier>
        //stranded playerEffects mining
        //stranded playerEffects mining <amplifier>
        //stranded playerEffects flying
        //stranded playerEffects flying <amplifier>
        //stranded hub
        //stranded hub set
        //stranded MOTD
        //stranded MOTD setPlayer <text...>
        //stranded MOTD getPlayer
        //stranded MOTD setIsland <text...>
        //stranded MOTD getIsland
        //stranded MOTD setRandom <text...>
        //stranded MOTD getRandom

        //todo make command
        if (args.length > 0 && args[0].equalsIgnoreCase("reload") && commandSender instanceof ConsoleCommandSender) {
            return false;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you have to be a player to use this command");
            return false;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage("i don't know yet");
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
