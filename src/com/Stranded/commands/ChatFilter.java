package com.Stranded.commands;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.events.ChatEvent;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class ChatFilter implements CommandExecutor {

    private Main p;

    public ChatFilter(Main main) {
        p = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] args) {

        //chatFilter add [filterWords]
        //chatFilter remove [filterWords]
        //chatFilter getFilters
        //chatFilter set <percentage (Integer)>

        Files chatFilter = new Files(p, "chatFilter.yml");

        if (args.length > 1 && args[0].equalsIgnoreCase("add")) {

            ArrayList<String> list = (ArrayList<String>) chatFilter.getConfig().getStringList("chatFilter.filter");
            StringBuilder outcome = new StringBuilder();
            int tmp = 0;

            for (int i = 1; i < args.length; i++) {
                if (!list.contains(args[i].toLowerCase())) {
                    list.add(args[i].toLowerCase());
                    outcome.append("the word ").append(args[i].toLowerCase()).append(" is added in the filter").append(i == args.length - 1 ? "" : "\n");
                    tmp++;
                } else {
                    outcome.append("the word ").append(args[i].toLowerCase()).append(" is already in the filter").append(i == args.length - 1 ? "" : "\n");
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
                    outcome.append("the word ").append(args[i].toLowerCase()).append(" is removed in the filter").append(i == args.length - 1 ? "" : "\n");
                    tmp++;
                } else {
                    outcome.append("the word ").append(args[i].toLowerCase()).append(" isn't in the filter").append(i == args.length - 1 ? "" : "\n");
                }
            }

            if (tmp != 0) {
                chatFilter.getConfig().set("chatFilter.filter", list);
                chatFilter.saveConfig();
            }
            commandSender.sendMessage(outcome.toString());

            return false;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("getFilters".toLowerCase())) {
                StringBuilder str = new StringBuilder();
                for (String badWord : chatFilter.getConfig().getStringList("chatFilter.filter")) {
                    str.append(badWord).append(" ");
                }
                commandSender.sendMessage("there are all filters: " + str.toString());
            } else {
                commandSender.sendMessage("wrong use");
            }
        } else if (args.length == 2) {
             if (args[0].equalsIgnoreCase("set")) {
                try {
                    int per = Integer.parseInt(args[1]);
                    chatFilter.getConfig().set("chatFilter.filterReplacement", per);
                    chatFilter.saveConfig();
                    commandSender.sendMessage("successfully edited");
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("you must use a number");
                }
            } else {
                commandSender.sendMessage("wrong use");
            }
        } else {
            commandSender.sendMessage("wrong use");
        }

        return false;
    }
}
