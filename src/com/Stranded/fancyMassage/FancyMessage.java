package com.Stranded.fancyMassage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class FancyMessage  {

    private String message = "";

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public void sendMessage(Player player) {

        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            Class<?> chatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
            Class<?> chatComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");

            Class<?> packet = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");

            Constructor constructor = packet.getConstructor(chatComponent);

            Object text = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "[\"\"," + removeLastChar(message) + "]");
            Object packetFinal = constructor.newInstance(text);

            Field field = packetFinal.getClass().getDeclaredField("a");
            field.setAccessible(true);
            field.set(packetFinal, text);
            connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet")).invoke(connection, packetFinal);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void clearMessage() {
        message = "";
    }

    public final void addText(String text) {
        message += "{\"text\":\"" + text + "\"},";
    }

    public final void addText(String text, Colors color) {
        message += "{\"text\":\"" + text + "\",\"color\":\"" + color.toString().toLowerCase() + "\"},";
    }

    public final void addText(String text, Colors color, Attributes attribute1) {
        message += "{\"text\":\"" + text + "\",\"color\":\"" + color.toString().toLowerCase() + "\",\"" + attribute1 + "\":\"true\"},";
    }

    public final void addText(String text, Colors color, Attributes attribute1,
                              Attributes attribute2) {
        message += "{\"text\":\"" + text + "\",\"color\":\"" + color.toString().toLowerCase() + "\",\"" + attribute1 + "\":\"true\",\"" +
                attribute2 + "\":\"true\"},";
    }

    public final void addText(String text, Colors color, Attributes attribute1,
                              Attributes attribute2, Attributes attribute3) {
        message += "{\"text\":\"" + text + "\",\"color\":\"" + color.toString().toLowerCase() + "\",\"" + attribute1 + "\":\"true\",\"" +
                attribute2 + "\":\"true\",\"" + attribute3 + "\":\"true\"},";
    }

    public final void addText(String text, Colors color, Attributes attribute1,
                              Attributes attribute2, Attributes attribute3, Attributes attribute4) {

        message += "{\"text\":\"" + text + "\",\"color\":\"" + color.toString().toLowerCase() + "\",\"" + attribute1 + "\":\"true\",\"" +
                attribute2 + "\":\"true\",\"" + attribute3 + "\":\"true\",\"" + attribute4 + "\":\"true\"},";
    }

    public final void addText(String text, Colors color, Attributes attribute1,
                              Attributes attribute2, Attributes attribute3, Attributes attribute4, Attributes attribute5) {
        message += "{\"text\":\"" + text + "\",\"color\":\"" + color.toString().toLowerCase() + "\",\"" + attribute1 + "\":\"true\",\"" +
                attribute2 + "\":\"true\",\"" + attribute3 + "\":\"true\",\"" + attribute4 + "\":\"true\",\"" + attribute5 + "\":\"true\"},";
    }

    public final void addHover(String hoverText) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        String str = message + "{\"text\":\"" + hoverText + "\"},";

        message = removeLastChar(str) + "]}},";

    }

    public final void addHover(String hoverText, Colors hoverColor) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        String str = message +
                "{\"text\":\"" +
                hoverText +
                "\",\"color\":\"" +
                hoverColor.toString().toLowerCase() +
                "\"},";

        message = removeLastChar(str) + "]}},";

    }

    public final void addHover(String[] hoverText, Colors[] hoverColor) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        StringBuilder str = new StringBuilder().append(message);

        for (int i = 0; i < hoverText.length; i++) {
            str.append("{\"text\":\"");
            str.append(hoverText[i]);
            str.append("\",\"color\":\"");
            str.append(hoverColor[i].toString().toLowerCase());
            str.append("\"},");
        }

        message = removeLastChar(str.toString()) + "]}},";

    }

    public final void addHover(String[] hoverText, Colors[] hoverColor,
                               Attributes[] attribute1) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        StringBuilder str = new StringBuilder().append(message);

        for (int i = 0; i < hoverText.length; i++) {
            str.append("{\"text\":\"");
            str.append(hoverText[i]);
            str.append("\",\"color\":\"");
            str.append(hoverColor[i].toString().toLowerCase());
            str.append("\",\"");
            str.append(attribute1[i]);
            str.append("\":\"true\"},");
        }

        message = removeLastChar(str.toString()) + "]}},";
    }

    public final void addHover(String[] hoverText, Colors[] hoverColor,
                               Attributes[] attribute1, Attributes[] attribute2) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        StringBuilder str = new StringBuilder().append(message);

        for (int i = 0; i < hoverText.length; i++) {
            str.append("{\"text\":\"");
            str.append(hoverText[i]);
            str.append("\",\"color\":\"");
            str.append(hoverColor[i].toString().toLowerCase());
            str.append("\",\"");
            str.append(attribute1[i]);
            str.append("\":\"true\",\"");
            str.append(attribute2[i]);
            str.append("\":\"true\"},");
        }

        message = removeLastChar(str.toString()) + "]}},";
    }

    public final void addHover(String[] hoverText, Colors[] hoverColor,
                               Attributes[] attribute1, Attributes[] attribute2, Attributes[] attribute3) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        StringBuilder str = new StringBuilder().append(message);

        for (int i = 0; i < hoverText.length; i++) {
            str.append("{\"text\":\"");
            str.append(hoverText[i]);
            str.append("\",\"color\":\"");
            str.append(hoverColor[i].toString().toLowerCase());
            str.append("\",\"");
            str.append(attribute1[i]);
            str.append("\":\"true\",\"");
            str.append(attribute2[i]);
            str.append("\":\"true\",\"");
            str.append(attribute3[i]);
            str.append("\":\"true\"},");
        }

        message = removeLastChar(str.toString()) + "]}},";
    }

    public final void addHover(String[] hoverText, Colors[] hoverColor,
                               Attributes[] attribute1, Attributes[] attribute2, Attributes[] attribute3, Attributes[] attribute4) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        StringBuilder str = new StringBuilder().append(message);

        for (int i = 0; i < hoverText.length; i++) {
            str.append("{\"text\":\"");
            str.append(hoverText[i]);
            str.append("\",\"color\":\"");
            str.append(hoverColor[i].toString().toLowerCase());
            str.append("\",\"");
            str.append(attribute1[i]);
            str.append("\":\"true\",\"");
            str.append(attribute2[i]);
            str.append("\":\"true\",\"");
            str.append(attribute3[i]);
            str.append("\":\"true\",\"");
            str.append(attribute4[i]);
            str.append("\":\"true\"},");
        }

        message = removeLastChar(str.toString()) + "]}},";
    }

    public final void addHover(String[] hoverText, Colors[] hoverColor,
                               Attributes[] attribute1, Attributes[] attribute2, Attributes[] attribute3, Attributes[] attribute4, Attributes[] attribute5) {

        message = removeLastChar(removeLastChar(message));
        message += ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",";

        StringBuilder str = new StringBuilder().append(message);

        for (int i = 0; i < hoverText.length; i++) {
            str.append("{\"text\":\"");
            str.append(hoverText[i]);
            str.append("\",\"color\":\"");
            str.append(hoverColor[i].toString().toLowerCase());
            str.append("\",\"");
            str.append(attribute1[i]);
            str.append("\":\"true\",\"");
            str.append(attribute2[i]);
            str.append("\":\"true\",\"");
            str.append(attribute3[i]);
            str.append("\":\"true\",\"");
            str.append(attribute4[i]);
            str.append("\":\"true\",\"");
            str.append(attribute5[i]);
            str.append("\":\"true\"},");
        }

        message = removeLastChar(str.toString()) + "]}},";
    }

    public final void addUrl(String url) {
        message = removeLastChar(removeLastChar(message));
        message += ",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"}},";
    }

    public final void addCommand(String command) {
        message = removeLastChar(removeLastChar(message));
        message += ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + command + "\"}},";
    }

    public final void addSuggest(String suggestion) {
        message = removeLastChar(removeLastChar(message));
        message += ",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + suggestion + "\"}},";
    }
}