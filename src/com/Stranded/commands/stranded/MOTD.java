package com.Stranded.commands.stranded;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.events.ChatEvent.replaceColors;

public class MOTD extends CmdManager {
    @Override
    public String getName() {
        return "motd";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //stranded MOTD
        //stranded MOTD player <text...>
        //stranded MOTD player
        //stranded MOTD island <text...>
        //stranded MOTD island
        //stranded MOTD random <text...>
        //stranded MOTD random

        if (args.length == 1) {

            FancyMessage fm = new FancyMessage();

            Files pluginData = getFiles("pluginData.yml");

            fm.addText("These are the MOTDs: ", Colors.DARK_AQUA);

            for (String motd : Arrays.asList("player", "island", "random")) {
                String amplifier = pluginData.getConfig().getString("plugin.server.MOTD." + motd);
                fm.addText("\n§9" + motd + ": §3" + amplifier.replace(" ", " §3"));
                fm.addSuggest("/stranded MOTD " + motd + " ");
                fm.addHover("/stranded MOTD " + motd + " <text...>", Colors.BLUE);
            }
            fm.sendMessage(player);

        } else if (args.length == 2) {
            String motd;
            switch (args[1].toLowerCase()) {
                case "island":
                case "player":
                case "random":
                    motd = args[1].toLowerCase();
                    break;
                default:
                    player.sendMessage(args[1] + " is not a motd");
                    return;
            }
            FancyMessage fm = new FancyMessage();

            fm.addText("MOTD for " + motd + " is " + getFiles("pluginData.yml").getConfig().getString("plugin.server.MOTD." + motd), Colors.DARK_AQUA);
            fm.addSuggest("/stranded MOTD " + motd + " ");
            fm.addHover("/stranded MOTD " + motd + " <text...>", Colors.BLUE);
            fm.sendMessage(player);
        } else if (args.length > 2) {
            String motd;
            switch (args[1].toLowerCase()) {
                case "island":
                case "player":
                case "random":
                    motd = args[1].toLowerCase();
                    break;
                default:
                    player.sendMessage(args[1] + " is not a MOTD");
                    return;
            }

            String motdMessage = replaceColors(Joiner.on(" ").join(Arrays.asList(args).subList(2, args.length)));

            Files pluginData = getFiles("pluginData.yml");
            pluginData.getConfig().set("plugin.server.MOTD." + motd, motdMessage);
            pluginData.saveConfig();
            player.sendMessage("Successfully edited" + ChatColor.GREEN);
        } else {
            player.sendMessage("Wrong use" + ChatColor.RED);
        }

    }
}
