package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
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

    @Override
    public void run(String[] args, Player player) {

        if (!player.hasPermission("Stranded.editDefaultIslands")) {
            player.sendMessage("no permission");
            return;
        }
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("build")) {
                if (args.length == 3) {

                    Files f = new Files(p, "islands.yml");
                    for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        String islandName = f.getConfig().getString("islandData.islandTypes." + s + ".name");
                        if (args[2].equalsIgnoreCase(islandName)) {
                            Location l = (Location) f.getConfig().get("islandData.islandTypes." + s + ".defaultLocation");
                            player.teleport(l);
                            return;
                        }
                    }

                    ArrayList<String> list = new ArrayList<>();

                    for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        list.add(f.getConfig().getString("islandData.islandTypes." + s + ".name"));
                    }

                    if (!list.contains(args[2].toLowerCase())) {
                        player.sendMessage("invalid island type given");

                        StringBuilder is = new StringBuilder().append("island types: ");
                        for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {

                            if (f.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
                                is.append(f.getConfig().getString("islandData.islandTypes." + s + ".name"));
                                is.append(" ");
                            }
                        }
                        player.sendMessage(is.toString());
                    }


                } else {
                    player.sendMessage("wrong use");
                }

            } else if (args[1].equalsIgnoreCase("rename")) {
                if (args.length == 4) {

                    Files f = new Files(p, "islands.yml");

                    for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        if (f.getConfig().getString("islandData.islandTypes." + s + ".name").equalsIgnoreCase(args[3])) {
                            player.sendMessage("this name is already in use");
                            return;
                        }
                    }

                    for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                        if (args[2].equalsIgnoreCase(f.getConfig().getString("islandData.islandTypes." + s + ".name"))) {
                            f.getConfig().set("islandData.islandTypes." + s + ".name", args[3]);
                            player.sendMessage("renamed");
                            f.saveConfig();
                            return;
                        }
                    }
                    player.sendMessage("invalid island type given");

                } else {
                    player.sendMessage("wrong use");
                }
            }
        } else {
            player.sendMessage("wrong use");
        }

    }
}
