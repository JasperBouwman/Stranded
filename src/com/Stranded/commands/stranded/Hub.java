package com.Stranded.commands.stranded;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Hub extends CmdManager {

    @Override
    public String getName() {
        return "hub";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //stranded hub
        //stranded hub set

        Files pluginData = new Files(p, "pluginData.yml");

        if (args.length == 1) {
            Location hub = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(hub);
            player.sendMessage("teleported to the hub");
        } else if (args.length == 2 && args[1].equalsIgnoreCase("set")) {
            pluginData.getConfig().set("plugin.hub.location", player.getLocation());
            pluginData.saveConfig();
            player.sendMessage("successfully edited the hub");
        } else {
            player.sendMessage("wrong use");
        }

    }
}
