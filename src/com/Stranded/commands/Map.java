package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.map.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Map implements CommandExecutor {

    private Main p;
    public ArrayList<CmdManager> actions = new ArrayList<>();
    public Map(Main main) {
        p = main;

        actions.add(new Dimensions());
        actions.add(new Get());
        actions.add(new Remove());
        actions.add(new Set());
        actions.add(new Update());
        actions.add(new com.Stranded.commands.map.Edit());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        //map set <mapTool name> <image name> <width> <height>                                set maps
        //map set <mapTool name> <image name> <width> <height> <center:false>                 set maps
        //map set <mapTool name> <image name> <width> <height> <center:false> <ratio:false>   set maps
        //map set <maoTool name>                                                              set existing maps
        //map get <mapTool name> <image name> <width> <height>                                gives maps
        //map get <mapTool name> <image name> <width> <height> <center:false>                 gives maps
        //map get <mapTool name> <image name> <width> <height> <center:false> <ratio:false>   gives maps
        //map get <mapTool name>                                                              gives existing maps
        //map dimensions <mapTool name>                                                       gives dimension of mapTool
        //map remove <mapTool name>                                                           removes the CustomMapRenderer for all the maps in that mapTool
        //map update <mapTool name>                                                           reload the image in the maps
        //map edit <mapTool name> ratio <true:false>                                          edits the keepRatio
        //map edit <mapTool name> center <true:false>                                         edits the keepCentered

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player to use this");
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 0) {
            //todo send /map help
            return false;
        }

        if (args.length > 1) {
            for (CmdManager action : this.actions) {
                if (args[0].toLowerCase().equals(action.getName()) || args[0].toLowerCase().equals(action.getAlias())) {
                    action.setMain(p);
                    action.run(args, player);
                    return false;
                }
            }
            player.sendMessage("this is not a sub-command");
        }



        return false;
    }
}
