package com.Stranded.commands.map;

import com.Stranded.commands.CmdManager;
import com.Stranded.mapsUtil.MapTool;
import org.bukkit.entity.Player;

public class Remove extends CmdManager {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //map remove <mapTool name>                                                           removes the CustomMapRenderer for all the maps in that mapTool

        if (args.length == 2) {

            MapTool tool = MapTool.getMapTool(args[1]);

            if (tool != null) {

                tool.removeMapTool();

            } else {
                player.sendMessage("MapTool name '" + args[1] + "' doesn't exist");
            }

        } else {
            player.sendMessage("wrong use");
        }

    }
}
