package com.Stranded.api;

import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerMessages {

    public static void sendWrongUse(Player player, String command) {
        FancyMessage fm = new FancyMessage();

        fm.addText("Usage: ", Colors.RED);
        fm.addText(command, Colors.YELLOW);
        fm.addCommand(command);
        fm.addHover(command, Colors.YELLOW);

        fm.sendMessage(player);
    }

    public static void sendWrongUse(CommandSender sender, String command) {
        if (sender instanceof Player) {
            FancyMessage fm = new FancyMessage();

            fm.addText("Usage: ", Colors.RED);
            fm.addText(command, Colors.YELLOW);
            fm.addCommand(command);
            fm.addHover(command, Colors.YELLOW);

            fm.sendMessage((Player) sender);
        } else {
            sender.sendMessage("Usage: " + command);
        }
    }

    public static void sendWrongUse(CommandSender sender, String[] command) {
        if (sender instanceof Player) {
            FancyMessage fm = new FancyMessage();

            fm.addText("Usage: ", Colors.RED);
            fm.addText(command[0], Colors.YELLOW);
            fm.addSuggest(command[1]);
            fm.addHover(command[0], Colors.YELLOW);

            fm.sendMessage((Player) sender);

        } else {
            sender.sendMessage("Usage: " + command[0]);
        }
    }

    public static void sendWrongUse(Player player, String[] command) {
        FancyMessage fm = new FancyMessage();

        fm.addText("Usage: ", Colors.RED);
        fm.addText(command[0], Colors.YELLOW);
        fm.addSuggest(command[1]);
        fm.addHover(command[0], Colors.YELLOW);

        fm.sendMessage(player);
    }

    public static void sendWrongUse(Player player, String command1, String command2) {
        FancyMessage fm = new FancyMessage();

        fm.addText("Usage: ", Colors.RED);
        fm.addText(command1, Colors.YELLOW);
        fm.addCommand(command1);
        fm.addHover(command1, Colors.YELLOW);
        fm.addText(" or ", Colors.RED);
        fm.addText(command2, Colors.YELLOW);
        fm.addCommand(command2);
        fm.addHover(command2, Colors.YELLOW);

        fm.sendMessage(player);
    }

    public static void sendWrongUse(Player player, String command1, String[] command2) {
        FancyMessage fm = new FancyMessage();

        fm.addText("Usage: ", Colors.RED);
        fm.addText(command1, Colors.YELLOW);
        fm.addCommand(command1);
        fm.addHover(command1, Colors.YELLOW);
        fm.addText(" or ", Colors.RED);
        fm.addText(command2[0], Colors.YELLOW);
        fm.addSuggest(command2[1]);
        fm.addHover(command2[0], Colors.YELLOW);

        fm.sendMessage(player);
    }

    public static void sendWrongUse(Player player, String[] command1, String[] command2, String[] command3) {

        FancyMessage fm = new FancyMessage();

        fm.addText("Usage: ", Colors.RED);
        fm.addText(command1[0], Colors.YELLOW);
        fm.addSuggest(command1[1]);
        fm.addHover(command1[0], Colors.YELLOW);
        fm.addText(", ", Colors.RED);
        fm.addText(command2[0], Colors.YELLOW);
        fm.addSuggest(command2[1]);
        fm.addHover(command2[0], Colors.YELLOW);
        fm.addText(" or ", Colors.RED);
        fm.addText(command3[0], Colors.YELLOW);
        fm.addSuggest(command3[1]);
        fm.addHover(command3[0], Colors.YELLOW);

        fm.sendMessage(player);
    }

    public static void sendWrongUse(Player player, String[] command1, String[] command2, String[] command3, boolean b1, boolean b2, boolean b3) {

        FancyMessage fm = new FancyMessage();

        fm.addText("Usage: ", Colors.RED);
        fm.addText(command1[0], Colors.YELLOW);
        if (b1) {
            fm.addCommand(command1[1]);
        } else {
            fm.addSuggest(command1[1]);
        }
        fm.addHover(command1[0], Colors.YELLOW);
        fm.addText(", ", Colors.RED);
        fm.addText(command2[0], Colors.YELLOW);
        if (b2) {
            fm.addCommand(command2[1]);
        } else {
            fm.addSuggest(command2[1]);
        }
        fm.addHover(command2[0], Colors.YELLOW);
        fm.addText(", ", Colors.RED);
        fm.addText(command3[0], Colors.YELLOW);
        if (b3) {
            fm.addCommand(command3[1]);
        } else {
            fm.addSuggest(command3[1]);
        }
        fm.addHover(command3[0], Colors.YELLOW);

        fm.sendMessage(player);
    }
}
