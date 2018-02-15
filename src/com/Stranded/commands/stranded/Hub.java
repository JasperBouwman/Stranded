package com.Stranded.commands.stranded;

import com.Stranded.Files;
import com.Stranded.api.ServerMessages;
import com.Stranded.commands.CmdManager;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.Permissions.hasPermission;

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

        Files pluginData = getFiles("pluginData.yml");

        if (args.length == 1) {
            Location hub = (Location) pluginData.getConfig().get("plugin.hub.location");
            player.teleport(hub);
            player.sendMessage(ChatColor.BLUE + "Teleported to the hub");
        } else if (args.length == 2 && args[1].equalsIgnoreCase("set")) {
            pluginData.getConfig().set("plugin.hub.location", player.getLocation());
            pluginData.saveConfig();
            player.sendMessage(ChatColor.GREEN + "Successfully edited the hub");
        } else {
            if (hasPermission(player, "Stranded.setHub")) {
                ServerMessages.sendWrongUse(player, "/stranded hub", "/stranded hub set");
            } else {
                ServerMessages.sendWrongUse(player, "/stranded hub");
            }
        }
    }
}
