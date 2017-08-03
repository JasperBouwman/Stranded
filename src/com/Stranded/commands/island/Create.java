package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.worldGeneration.IslandGeneration;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class Create extends CmdManager {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getAlias() {
        return "cr";
    }

    @Override
    public void run(String[] args) {
        if (args.length == 1) {
            player.sendMessage("pls enter a name and a island type");
        } else if (args.length == 2) {
            Files f = new Files(p, "islands.yml");
            StringBuilder is = new StringBuilder().append("island types: ");
            for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {

                if (f.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
                    is.append(f.getConfig().getString("islandData.islandTypes." + s + ".name"));
                    is.append(" ");
                }
            }
            player.sendMessage(is.toString());
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("Create") || args[0].equalsIgnoreCase("cr")) {

                String name = args[1];
                String islandType = args[2];

                if (p.getConfig().contains("island." + player.getName())) {
                    player.sendMessage("you are already in an island");
                    return;
                }

                islandDataCheck(p);

                Files f = new Files(p, "islands.yml");

                ArrayList<String> list = new ArrayList<>();

                for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                    list.add(f.getConfig().getString("islandData.islandTypes." + s + ".name"));
                }

                if (!list.contains(islandType)) {
                    player.sendMessage("invalid island type given");

                    StringBuilder is = new StringBuilder().append("island types: ");
                    for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {

                        if (f.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
                            is.append(f.getConfig().getString("islandData.islandTypes." + s + ".name"));
                            is.append(" ");
                        }
                    }
                    player.sendMessage(is.toString());
                    return;
                }

                for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                    if (islandType.equalsIgnoreCase(f.getConfig().getString("islandData.islandTypes." + s + ".name"))) {
                        if (!f.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {

                            player.sendMessage("invalid island type given");
                            return;
                        }
                    }
                }

                if (f.getConfig().contains("island." + name)) {
                    player.sendMessage("this name is already in use");
                    return;
                }
                int x = f.getConfig().getInt("islandData.default.X");
                int z = f.getConfig().getInt("islandData.default.Z");

                Location l = new Location(Bukkit.getWorld("Islands"), x, 64, z);

                f.getConfig().set("island." + name + ".location", l);

                f.getConfig().set("island." + name + ".owner", player.getName());

                f.getConfig().set("island." + name + ".lvl", 1);

                p.getConfig().set("island." + player.getName(), name);

                ArrayList<String> members = new ArrayList<>();
                members.add(player.getName());
                f.getConfig().set("island." + name + ".members", members);

                player.sendMessage("island is being generated");
                //island generation
                IslandGeneration.generate(l, p, islandType);

                f.getConfig().set("island." + name + ".UUID", IslandGeneration.UUID.toString());

                //tp player to island

                player.teleport(IslandGeneration.nexusLocation);
                player.setBedSpawnLocation(IslandGeneration.nexusLocation, true);

                f.getConfig().set("island." + name + ".home", IslandGeneration.nexusLocation);
                f.saveConfig();

                if (x >= 200002) {
                    x = -200000;
                    z += 1000;
                } else {
                    x += 1000;
                }

                f.getConfig().set("islandData.default.X", x);
                f.getConfig().set("islandData.default.Z", z);


                f.saveConfig();
                p.saveConfig();
            }

        } else {
            player.sendMessage("wrong use");
        }
    }

    private static void islandDataCheck(Main p) {
        Files f = new Files(p, "islands.yml");
        if (!f.getConfig().contains("islandData.default")) {
            int l = -200000;
            f.getConfig().set("islandData.default.X", l);
            f.getConfig().set("islandData.default.Z", l);
            f.saveConfig();
        }
    }
}
