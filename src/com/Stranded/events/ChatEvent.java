package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.Reply;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

import static com.Stranded.commands.Reply.chatData;

public class ChatEvent implements Listener {

    private Main p;

    public ChatEvent(Main main) {
        p = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void chatEvent(AsyncPlayerChatEvent e) {

        String finalMessage = chatFilter(e.getMessage(), p);

        int chatID = 0;
        while (chatData.containsKey(chatID)) {
            chatID++;
        }
        chatData.put(chatID, new Reply.ChatData(e.getPlayer().getName(), finalMessage.replace("§", "&")));

        e.setCancelled(true);

        if (finalMessage.startsWith("./")) {
            sendCommand(finalMessage, e.getPlayer(), chatID);
        } else {
            sendMessage(finalMessage, e.getPlayer(), chatID);
        }

    }

    public static String chatFilter(String toFilter, Main p) {

        Files chatFilter = new Files(p, "chatFilter.yml");

        ArrayList<String> list = (ArrayList<String>) chatFilter.getConfig().getStringList("chatFilter.filter");
        String filterType = chatFilter.getConfig().getString("chatFilter.filterReplacement");

        int filter = 50;
        try {
            filter = Integer.parseInt(filterType);
        } catch (NumberFormatException ignore) {
        }

        Random r = new Random();

        String tmpMessage = toFilter.toLowerCase();

        HashMap<Integer, String> colors = new HashMap<>();

        List<String> colorsList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r");

        boolean tmp = false;
        int place = 0;
        for (char c : tmpMessage.toCharArray()) {
            if (tmp && colorsList.contains(String.valueOf(c))) {
                colors.put(place, "&" + c);
                tmp = false;
            } else if (String.valueOf(c).equals("&")) {
                tmp = true;
            }

            place++;
        }

        for (String color : colorsList) {
            tmpMessage = tmpMessage.replace("&" + color, "");
        }

        if (filter != 0) {
            for (String badWord : list) {
                while (true) {
                    if (!tmpMessage.contains(badWord)) {
                        break;
                    }
                    StringBuilder str = new StringBuilder();
                    for (int i = 0; i < badWord.length(); i++) {
                        if (r.nextInt(100) < filter) {
                            str.append("*");
                        } else {
                            str.append(badWord.toCharArray()[i]);
                        }
                    }
                    tmpMessage = tmpMessage.replaceFirst(badWord, str.toString());
                }
            }
        }

        StringBuilder capitalMessage = new StringBuilder();
        int i = 0;
        int ii = 0;
        for (char c : tmpMessage.toCharArray()) {
            if (i == 0) {
                i++;
                capitalMessage.append((c + "").toUpperCase());
            } else if (ii == 2) {
                capitalMessage.append((c + "").toUpperCase());
                ii = 0;
            } else {
                capitalMessage.append(c).append("");
                if ((c + "").equals(".")) {
                    ii++;
                }
                if ((c + "").equals(" ")) {
                    if (ii == 1) {
                        ii++;
                    } else {
                        ii = 0;
                    }
                }
            }
        }


        StringBuilder stringBuilder = new StringBuilder();
        int l = 1;
        for (char c : capitalMessage.toString().toCharArray()) {
            if (colors.keySet().contains(l)) {
                stringBuilder.append(colors.get(l));
                l++;
                colors.remove(l);
            }
            stringBuilder.append(c);
            l++;
        }

        return replaceColors(stringBuilder.toString()).replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static String replaceColors(String message) {
        for (String color : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o", "r")) {
            message = message.replace("&" + color, "§" + color);
        }
        return message;
    }

    private void sendMessage(String s, Player sender, int chatID) {
        FancyMessage fm = new FancyMessage();

        fm.addText("<" + sender.getName() + "> " + s);
        fm.addSuggest("/reply " + chatID + " ");

        for (Player p : Bukkit.getOnlinePlayers()) {
            fm.sendMessage(p);
        }
    }

    private void sendCommand(String s, Player sender, int chatID) {
        FancyMessage fm = new FancyMessage();

        s = s.replaceFirst("\\.", "").replace("§", "&");

        fm.addText("<" + sender.getName() + "> " + s);
        fm.addCommand(s);
        fm.addHover("chatID: " + chatID, Colors.GREEN);

        for (Player p : Bukkit.getOnlinePlayers()) {
            fm.sendMessage(p);
        }

    }
}
