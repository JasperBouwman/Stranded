package com.Stranded.commands.map;

import com.Stranded.commands.CmdManager;
import com.Stranded.mapsUtil.MapTool;
import org.bukkit.entity.Player;

public class Dimensions extends CmdManager {

    @Override
    public String getName() {
        return "dimensions";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //map dimensions <mapTool name>                                                       gives dimension of mapTool

        if (args.length == 2) {

            MapTool tool = MapTool.getMapTool(args[1]);

            if (tool != null) {
                player.sendMessage("the width is: " + tool.getWidth() + ", and the height is: " + tool.getHeight());
            } else {
                player.sendMessage("MapTool name '" + args[1] + "' doesn't exist");
            }

        } else {
            player.sendMessage("wrong use");
        }

    }
}
