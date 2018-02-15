package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.Permissions.hasPermission;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Edit extends CmdManager {

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getAlias() {
        return null;
    }

    private void sendIslandTypes(Player player) {
        Files f = getFiles("islands.yml");
        StringBuilder is = new StringBuilder().append(ChatColor.AQUA);
        is.append("Available island types: ");
        int color = 0;
        for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
            if (color == 0) {
                color++;
                is.append(ChatColor.BLUE);
            } else {
                color = 0;
                is.append(ChatColor.DARK_BLUE);
            }
            is.append(f.getConfig().getString("islandData.islandTypes." + s + ".name")).append(" ");
        }
        player.sendMessage(is.toString());
    }

    @Override
    public void run(String[] args, Player player) {

        //island edit <island name>
        //island edit <island name> rename <new name>
        //island edit <island name> <enable:disable>

        if (!hasPermission(player, "Stranded.islandEdit")) {
            return;
        }
        switch (args.length) {
            case 2:

                Files islands = getFiles("islands.yml");
                for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                    String islandName = islands.getConfig().getString("islandData.islandTypes." + s + ".name");
                    if (args[1].equalsIgnoreCase(islandName)) {
                        Location l = (Location) islands.getConfig().get("islandData.islandTypes." + s + ".defaultLocation");
                        player.teleport(l);
                        return;
                    }
                }

                ArrayList<String> list = new ArrayList<>();

                for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                    list.add(islands.getConfig().getString("islandData.islandTypes." + s + ".name").toLowerCase());
                }

                if (!list.contains(args[1].toLowerCase())) {
                    player.sendMessage(ChatColor.RED + "Invalid island type given");
                    sendIslandTypes(player);
                }
                break;
            case 3:
                if (args[2].equalsIgnoreCase("enable")) {
                    islands = getFiles("islands.yml");

                    for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        String islandName = islands.getConfig().getString("islandData.islandTypes." + s + ".name");
                        if (args[1].equalsIgnoreCase(islandName)) {

                            boolean state = islands.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled");
                            if (state) {
                                player.sendMessage(ChatColor.RED + "This island is already enabled");
                            } else {
                                islands.getConfig().set("islandData.islandTypes." + s + ".enabled", true);
                                islands.saveConfig();
                            }
                            return;
                        }
                    }

                } else if (args[2].equalsIgnoreCase("disable")) {

                    islands = getFiles("islands.yml");
                    for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        String islandName = islands.getConfig().getString("islandData.islandTypes." + s + ".name");
                        if (args[1].equalsIgnoreCase(islandName)) {

                            boolean state = islands.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled");
                            if (!state) {
                                player.sendMessage(ChatColor.RED + "This island is already disabled");
                            } else {
                                islands.getConfig().set("islandData.islandTypes." + s + ".enabled", false);
                                islands.saveConfig();
                            }
                            return;
                        }
                    }

                } else {
                    sendWrongUse(player, new String[]{"/island edit <island name> <enable:disable>", "/island edit "});
                }

                break;
            case 4:
                if (args[2].equalsIgnoreCase("rename")) {
                    islands = getFiles("islands.yml");

                    if (Main.containsSpecialCharacter(args[3])) {
                        player.sendMessage(ChatColor.RED + "Your name can't contains any special characters");
                        return;
                    }

                    for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        if (islands.getConfig().getString("islandData.islandTypes." + s + ".name").equalsIgnoreCase(args[3])) {
                            player.sendMessage(ChatColor.RED + "This name is already in use");
                            return;
                        }
                    }

                    for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        if (args[1].equalsIgnoreCase(islands.getConfig().getString("islandData.islandTypes." + s + ".name"))) {
                            islands.getConfig().set("islandData.islandTypes." + s + ".name", args[3]);
                            player.sendMessage(ChatColor.GREEN + "The island has been renamed to " + args[3]);
                            islands.saveConfig();
                            return;
                        }
                    }
                    player.sendMessage(ChatColor.RED + "Invalid island type given");
                    sendIslandTypes(player);
                } else {
                    sendWrongUse(player, new String[]{"/island edit <island name> rename <new name>", "/island edit "});
                }

                break;
            default:
                sendWrongUse(player, new String[]{"/island edit <island name>", "/island edit "},
                        new String[]{"/island edit <island name> rename <new name>", "/island edit "},
                        new String[]{"/island edit <island name> <enable:disable>", "/island edit "});
                break;
        }
    }
}


