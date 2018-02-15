package com.Stranded.commands.map;

import com.Stranded.commands.CmdManager;
import com.Stranded.mapsUtil.MapTool;
import org.bukkit.entity.Player;

public class Edit extends CmdManager {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //map edit <mapTool name> ratio <true:false>                                   edits the keepRatio
        //map edit <mapTool name> center <true:false>                                  edits the keepCentered

        if (args.length == 4) {

            MapTool tool = MapTool.getMapTool(args[1]);

            if (tool != null) {
                if (args[2].equalsIgnoreCase("ratio")) {
                    tool.setKeepRatio(args[3].equalsIgnoreCase("true"));
                } else if (args[2].equalsIgnoreCase("center")) {
                    tool.setKeepCentered(args[3].equalsIgnoreCase("true"));
                } else {
                    player.sendMessage("wrong use");
                }
            } else {
                player.sendMessage("this mapTool doesn't exist");
            }


        } else {
            player.sendMessage("wrong use");
        }


    }
}
