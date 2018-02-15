package com.Stranded.commands.map;

import com.Stranded.commands.CmdManager;
import com.Stranded.mapsUtil.MapTool;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Set extends CmdManager {

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //map set <mapTool name> <image name> <width> <height>                                set maps
        //map set <mapTool name> <image name> <width> <height> <center:false>                 set maps
        //map set <mapTool name> <image name> <width> <height> <center:false> <ratio:false>   set maps

        boolean keepCentered = true;
        boolean keepRatio = true;
        int width = 0;
        int height = 0;

        switch (args.length) {
            case 7:
                keepRatio = args[6].equalsIgnoreCase("true");
            case 6:
                keepCentered = args[5].equalsIgnoreCase("true");
            case 5:

                boolean r = false;

                try {
                    width = Integer.parseInt(args[3]);
                } catch (NumberFormatException ignore) {
                    player.sendMessage("your width is not a valid number");
                    r = true;
                }
                try {
                    height = Integer.parseInt(args[4]);
                } catch (NumberFormatException ignore) {
                    player.sendMessage("your height is not a valid number");
                    r = true;
                }
                if (r) return;

                break;
            case 2:

                MapTool tool = MapTool.getMapTool(args[1]);

                if (tool != null) {
                    Block block = player.getTargetBlock(null, 5);
                    tool.setMaps(block, player);
                } else {
                    player.sendMessage("MapTool name '" + args[1] + "' doesn't exist");
                }

                return;

            default:
                player.sendMessage("wrong use");
                return;
        }

        setMaps(args[1], args[2], width, height, keepCentered, keepRatio, player);


    }

    private void setMaps(String mapToolName, String imageName, int width, int height, boolean keepCentered, boolean keepRatio, Player player) {
        try {
            MapTool map = new MapTool(p, imageName, mapToolName);

            Block block = player.getTargetBlock(null, 5);

            map.setMaps(width, height, keepCentered, keepRatio, block, player);
        } catch (IllegalArgumentException iae) {
            player.sendMessage(iae.getMessage());
        }
    }
}
