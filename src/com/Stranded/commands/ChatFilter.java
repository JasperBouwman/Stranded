package com.Stranded.commands;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.events.ChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class ChatFilter implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {

        //chatFilter add [filterWords]
        //chatFilter remove [filterWords]
        //chatFilter getFilters
        //chatFilter set <percentage>
        //chatFilter setType <filterType> [star:comic]
        //chatFilter getType

        Files chatFilter = getFiles("chatFilter.yml");

        if (args.length > 1 && args[0].equalsIgnoreCase("add")) {

            ArrayList<String> list = (ArrayList<String>) chatFilter.getConfig().getStringList("chatFilter.filter");
            StringBuilder outcome = new StringBuilder();
            int tmp = 0;

            for (int i = 1; i < args.length; i++) {
                if (!list.contains(args[i].toLowerCase())) {
                    list.add(args[i].toLowerCase());
                    outcome.append(ChatColor.GREEN).append("The word ").append(args[i].toLowerCase()).append(" is added in the filter").append(i == args.length - 1 ? "" : "\n");
                    tmp++;
                } else {
                    outcome.append(ChatColor.RED).append("The word ").append(args[i].toLowerCase()).append(" is already in the filter").append(i == args.length - 1 ? "" : "\n");
                }
            }

            if (tmp != 0) {
                chatFilter.getConfig().set("chatFilter.filter", list);
                chatFilter.saveConfig();
            }
            commandSender.sendMessage(outcome.toString());

            return false;
        }
        if (args.length > 1 && args[0].equalsIgnoreCase("remove")) {

            ArrayList<String> list = (ArrayList<String>) chatFilter.getConfig().getStringList("chatFilter.filter");
            StringBuilder outcome = new StringBuilder();
            int tmp = 0;

            for (int i = 1; i < args.length; i++) {
                if (list.contains(args[i].toLowerCase())) {
                    list.remove(args[i].toLowerCase());
                    outcome.append(ChatColor.GREEN).append("The word ").append(args[i].toLowerCase()).append(" is removed in the filter").append(i == args.length - 1 ? "" : "\n");
                    tmp++;
                } else {
                    outcome.append(ChatColor.RED).append("The word ").append(args[i].toLowerCase()).append(" isn't in the filter").append(i == args.length - 1 ? "" : "\n");
                }
            }

            if (tmp != 0) {
                chatFilter.getConfig().set("chatFilter.filter", list);
                chatFilter.saveConfig();
            }
            commandSender.sendMessage(outcome.toString());

            return false;
        }
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("getFilters".toLowerCase())) {
                    StringBuilder str = new StringBuilder();
                    int i = 0;
                    for (String badWord : chatFilter.getConfig().getStringList("chatFilter.filter")) {
                        str.append(" ");
                        if (i == 0) {
                            str.append(ChatColor.BLUE);
                            i++;
                        } else {
                            str.append(ChatColor.DARK_AQUA);
                            i = 0;
                        }
                        str.append(badWord);
                    }
                    commandSender.sendMessage(ChatColor.DARK_GREEN + "These are all the filters:" + str.toString());
                } else if (args[0].equalsIgnoreCase("getType".toLowerCase())) {
                    commandSender.sendMessage(ChatColor.GREEN + "FilterType is " + chatFilter.getConfig().getString("chatFilter.filterType"));
                    return false;
                } else {
                    sendWrongUse(commandSender, new String[]{"/chatFilter [add:remove:getFilters:set:setType:getType]", "/chatFilter "});
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("set")) {
                    try {
                        int per = Integer.parseInt(args[1]);
                        if (per < 0) {
                            commandSender.sendMessage(ChatColor.RED + "The number has to be at least 0");
                            return false;
                        }
                        if (per > 100) {
                            commandSender.sendMessage(ChatColor.RED + "The number has to be lower or equal than 100");
                            return false;
                        }
                        chatFilter.getConfig().set("chatFilter.filterReplacement", per);
                        chatFilter.saveConfig();
                        commandSender.sendMessage(ChatColor.GREEN + "Successfully edited the filter percentage");
                    } catch (NumberFormatException e) {
                        commandSender.sendMessage(ChatColor.RED + "You have to use a number between 0 and 100");
                    }
                } else if (args[0].equalsIgnoreCase("setType")) {
                    chatFilter.getConfig().set("chatFilter.filterType", args[1]);
                    chatFilter.saveConfig();
                    commandSender.sendMessage(ChatColor.GREEN + "FilterType set to " + args[1]);
                    return false;
                } else {
                    sendWrongUse(commandSender, new String[]{"/chatFilter [add:remove:getFilters:set:setType:getType]", "/chatFilter "});
                }
                break;
            default:
                sendWrongUse(commandSender, new String[]{"/chatFilter [add:remove:getFilters:set:setType:getType]", "/chatFilter "});
                break;
        }

        return false;
    }
}
