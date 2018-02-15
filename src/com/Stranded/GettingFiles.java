package com.Stranded;

import java.util.Collection;
import java.util.HashMap;

public class GettingFiles {

    private static HashMap<String, Files> list;

    public GettingFiles(Main main) {

        list = new HashMap<>();

        list.put("chatFilter", new Files(main, "chatFilter.yml"));
        list.put("islands", new Files(main, "islands.yml"));
        list.put("playerData", new Files(main, "playerData.yml"));
        list.put("playerUUID", new Files(main, "playerUUID.yml"));
        list.put("pluginData", new Files(main, "pluginData.yml"));
        list.put("towers", new Files(main, "towers.yml"));
        list.put("warData", new Files(main, "warData.yml"));
        list.put("warIslands", new Files(main, "warIslands.yml"));
        list.put("maps", new Files(main, "/maps","maps.yml"));
        list.put("config", new Files(main, "config.yml"));

    }

    public static void addFile(String fileName, Files file) {
        list.put(fileName, file);
    }

    public static Files getFiles(String file) {
        return list.getOrDefault(file.replace(".yml", ""), null);
    }

    public static Collection<Files> getFiles() {
        return list.values();
    }

    public static void reloadFile(String file, Main p) {
        list.put(file.replace(".yml", ""), new Files(p, file + (file.endsWith(".yml") ? "" : ".yml")));
    }
}
