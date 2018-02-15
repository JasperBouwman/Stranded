package com.Stranded.commands.map;

import com.Stranded.commands.CmdManager;
import com.Stranded.mapsUtil.MapTool;
import org.bukkit.entity.Player;

public class Update extends CmdManager {

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //map update <mapTool name>                                                           reload the image in the maps

        if (args.length == 2) {

            MapTool tool = MapTool.getMapTool(args[1]);

            if (tool != null) {

                tool.loadMap();

            } else {
                player.sendMessage("MapTool name '" + args[1] + "' doesn't exist");
            }

        } else {
            player.sendMessage("wrong use");
        }

    }
}
