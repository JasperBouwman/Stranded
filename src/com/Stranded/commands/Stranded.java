package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.stranded.*;
import com.Stranded.fancyMassage.FancyMessage;
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
        actions.add(new MOTD());
        actions.add(new World());
        actions.add(new FilesCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        //stranded
        //stranded files autoSave [on:off] {on=beta}
        //stranded files reload
        //stranded files save
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
        //stranded MOTD player <text...>
        //stranded MOTD player
        //stranded MOTD island <text...>
        //stranded MOTD island
        //stranded MOTD random <text...>
        //stranded MOTD random
        //stranded world <world>
        //stranded world <world> <X> <Y> <Z>

        if (args.length > 0 && args[0].equalsIgnoreCase("reload") && commandSender instanceof ConsoleCommandSender) {
            return false;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you have to be a player to use this command");
            return false;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {

            FancyMessage fm = new FancyMessage();
            fm.addText("Stranded info:\nAuthors: ");
            fm.addText("The_Spaceman");
            fm.addUrl("https://dev.bukkit.org/members/the_spaceman2000/projects");
            fm.addHover("https://dev.bukkit.org/members/the_spaceman2000/projects");
            fm.addText(", 7h3_4ch3r\nVersion: " + p.getDescription().getVersion());
            fm.sendMessage(player);

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
