package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;

import static com.Stranded.events.ChatEvent.chatFilter;
import static com.Stranded.events.ChatEvent.replaceColors;

public class Reply implements CommandExecutor {

    public static HashMap<Integer, ChatData> chatData = new HashMap<>();

    private Main p;

    public Reply(Main main) {
        p = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        //reply <chatID> <message>
        //reply reset

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player");
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            if (player.hasPermission("Stranded.replyReset")) {
                chatData.clear();
                Bukkit.broadcastMessage("replies is reset");
            } else {
                player.sendMessage("you don't have permission to reset the replies");
            }
            return false;
        }

        if (args.length > 1) {

            int chatID;

            try {
                chatID = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                player.sendMessage("yhe chatID must be a number, " + args[0] + " is not a number");
                return false;
            }

            if (!chatData.containsKey(chatID)) {
                player.sendMessage("this chatID doesn't exist");
                return false;
            }

            String message = chatData.get(chatID).getMessage();
            String player1 = chatData.get(chatID).getPlayer();

            String newMessage = chatFilter(Joiner.on(" ").join(Arrays.asList(args).subList(1, args.length)), p);

            if (message.startsWith("./")) {
                message = message.replaceFirst("\\./", "/");
            }

            int chatID1 = 0;
            while (chatData.containsKey(chatID1)) {
                chatID1++;
            }
            chatData.put(chatID1, new ChatData(player.getName(), newMessage.replace("§", "&")));

            FancyMessage fm = new FancyMessage();
            fm.addText("[replied] <" + player.getName() + "> " + newMessage);
            fm.addHover("<" + player1 + "> " + replaceColors(message));
            fm.addSuggest("/reply " + chatID1 + " ");
            for (Player pl : Bukkit.getOnlinePlayers()) {
                fm.sendMessage(pl);
            }

        } else {
            player.sendMessage("wrong use");
        }

        return false;
    }

    public static class ChatData {
        private String player;
        private String message;

        public ChatData(String player, String message) {
            this.player = player;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public String getPlayer() {
            return player;
        }
    }
}
