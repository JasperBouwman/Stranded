package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
        Files f = new Files(p, "islands.yml");
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

        if (!player.hasPermission("Stranded.editDefaultIslands")) {
            player.sendMessage(ChatColor.RED + "You don't have permission");
            return;
        }
        if (args.length == 2) {

            Files islands = new Files(p, "islands.yml");
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
        } else if (args.length == 3) {
            if (args[2].equalsIgnoreCase("enable")) {
                Files islands = new Files(p, "islands.yml");

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

                Files islands = new Files(p, "islands.yml");
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
                player.sendMessage(ChatColor.RED + "Usage: /island edit <island name> <enable:disable>");
            }

        } else if (args.length == 4) {
            if (args[2].equalsIgnoreCase("rename")) {
                Files islands = new Files(p, "islands.yml");

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
                player.sendMessage(ChatColor.RED + "Usage: /island edit <island name> rename <new name>");
            }

        } else {
            player.sendMessage(ChatColor.RED + "Usage: /island edit <island name>\n/island edit <island name> rename <new name>\n/island edit <island name> <enable:disable>");
        }
    }
}


