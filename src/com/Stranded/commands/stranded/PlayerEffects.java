package com.Stranded.commands.stranded;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class PlayerEffects extends CmdManager {
    @Override
    public String getName() {
        return "PlayerEffects".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //stranded playerEffects
        //stranded playerEffects walking
        //stranded playerEffects walking <amplifier>
        //stranded playerEffects pvp
        //stranded playerEffects pvp <amplifier>
        //stranded playerEffects mining
        //stranded playerEffects mining <amplifier>
        //stranded playerEffects flying
        //stranded playerEffects flying <amplifier>

        if (args.length == 1) {

            FancyMessage fm = new FancyMessage();

            Files pluginData = new Files(p, "pluginData.yml");

            fm.addText("These are the playerEffects amplifiers: ", Colors.DARK_AQUA);

            for (String effect : Arrays.asList("walking", "pvp", "mining" ,"flying")) {
                long amplifier = pluginData.getConfig().getLong("plugin.scoreboard." + effect + ".amplifier");
                fm.addText("\n" + effect + ": §3" + amplifier, Colors.BLUE);
                fm.addSuggest("/stranded playerEffects " + effect + " ");
                fm.addHover("/stranded playerEffects " + effect + " <amplifier>", Colors.BLUE);
            }
            fm.sendMessage(player);


        } else if (args.length == 2) {
            String effect;
            switch (args[1].toLowerCase()) {
                case "walking":
                case "pvp":
                case "mining":
                case "flying":
                    effect = args[1].toLowerCase();
                    break;
                default:
                    player.sendMessage(args[1] + " is not a playerEffect");
                    return;
            }
            FancyMessage fm = new FancyMessage();

            fm.addText("Amplifier for " + effect + " is " + new Files(p, "pluginData.yml").getConfig().getString("plugin.scoreboard." + effect + ".amplifier"), Colors.DARK_AQUA);
            fm.addSuggest("/stranded playerEffects " + effect + " ");
            fm.addHover("/stranded playerEffects " + effect + " <amplifier>", Colors.BLUE);
            fm.sendMessage(player);
        } else if (args.length == 3) {
            String effect;
            switch (args[1].toLowerCase()) {
                case "walking":
                case "pvp":
                case "mining":
                case "flying":
                    effect = args[1].toLowerCase();
                    break;
                default:
                    player.sendMessage(args[1] + " is not a playerEffect");
                    return;
            }

            long i;

            try {
                i = Long.parseLong(args[2]);
            } catch (NumberFormatException nfe) {
                player.sendMessage("please use a positive number");
                return;
            }

            if (i < 0) {
                player.sendMessage("please use a positive number");
                return;
            }

            Files pluginData = new Files(p, "pluginData.yml");
            pluginData.getConfig().set("plugin.scoreboard." + effect + ".amplifier", i);
            pluginData.saveConfig();
            player.sendMessage("successfully edited");
        } else {
            player.sendMessage("wrong use");
        }
    }
}
