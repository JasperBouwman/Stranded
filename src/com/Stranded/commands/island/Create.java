package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.worldGeneration.IslandGeneration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Create extends CmdManager {

    private static void islandDataCheck() {
        Files islands = getFiles("islands.yml");
        if (!islands.getConfig().contains("islandData.default")) {
            int l = -200000;
            islands.getConfig().set("islandData.default.X", l);
            islands.getConfig().set("islandData.default.Z", l);
            islands.saveConfig();
        }
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getAlias() {
        return "cr";
    }

    private void sendIslandTypes(Player player) {
        Files islands = getFiles("islands.yml");
        StringBuilder is = new StringBuilder().append(ChatColor.AQUA);
        is.append(ChatColor.GREEN).append("Available island types: ");
        int color = 0;
        for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
            if (islands.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
                if (color == 0) {
                    color++;
                    is.append(ChatColor.BLUE);
                } else {
                    color = 0;
                    is.append(ChatColor.DARK_BLUE);
                }
                is.append(islands.getConfig().getString("islandData.islandTypes." + s + ".name"));
                is.append(" ");
            }
        }
        player.sendMessage(is.toString());
    }

    @Override
    public void run(String[] args, Player player) {

        //island create <name> <islandType>

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (Bukkit.getWorld("Islands") != null) {
            Files islands = getFiles("islands.yml");
            if (islands.getConfig().contains("islandData.islandTypesCopied")) {
                if (!islands.getConfig().getBoolean("islandData.islandTypesCopied")) {
                    player.sendMessage(ChatColor.DARK_RED + "Please wait, default Islands are not generated yet");
                    return;
                }
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Please wait, default Islands are not generated yet");
                return;
            }
        } else {
            player.sendMessage(ChatColor.DARK_RED + "Please wait, the world Islands is not generated yet");
            return;
        }

        if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "Please enter a name and an island type");
        } else if (args.length == 2) {
            sendIslandTypes(player);
        } else if (args.length == 3) {

            String name = args[1];
            String islandType = args[2];

            if (Main.containsSpecialCharacter(name)) {
                player.sendMessage(ChatColor.RED + "You can't use a special character in your island name");
                return;
            }
            if (name.equalsIgnoreCase("remove")) {
                player.sendMessage(ChatColor.RED + "Please do not use this name");
                return;
            }

            if (config.getConfig().contains("island." + player.getName())) {
                player.sendMessage(ChatColor.RED + "You are already in an island");
                return;
            }

            islandDataCheck();

            Files islands = getFiles("islands.yml");

            ArrayList<String> list = new ArrayList<>();

            for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                list.add(islands.getConfig().getString("islandData.islandTypes." + s + ".name"));
            }

            if (!list.contains(islandType)) {
                player.sendMessage(ChatColor.RED + "Invalid island type given");
                sendIslandTypes(player);
                return;
            }

            for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
                if (islandType.equalsIgnoreCase(islands.getConfig().getString("islandData.islandTypes." + s + ".name"))) {
                    if (!islands.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
                        player.sendMessage(ChatColor.RED + "Invalid island type given");
                        sendIslandTypes(player);
                        return;
                    }
                }
            }

            for (String island : islands.getConfig().getConfigurationSection("island").getKeys(false)) {
                if (island.equalsIgnoreCase(name)) {
                    player.sendMessage(ChatColor.RED + "This name is already in use");
                    return;
                }
            }

            int x = islands.getConfig().getInt("islandData.default.X");
            int z = islands.getConfig().getInt("islandData.default.Z");

            Location l = new Location(Bukkit.getWorld("Islands"), x, 64, z);

            islands.getConfig().set("island." + name + ".location", l);
            islands.getConfig().set("island." + name + ".owner", uuid);
            islands.getConfig().set("island." + name + ".lvl", 1);
            islands.getConfig().set("island." + name + ".nexusLvl", 1);
            islands.getConfig().set("island." + name + ".nexusHealth", 20);

            config.getConfig().set("island." + uuid, name);

            ArrayList<String> members = new ArrayList<>();
            members.add(uuid);
            islands.getConfig().set("island." + name + ".members", members);

            player.sendMessage(ChatColor.GREEN + "Your island is being generated");
            //island generation
            IslandGeneration.generate(l, islandType);

            islands.getConfig().set("island." + name + ".UUID", IslandGeneration.UUID.toString());

            //tp player to island

            player.teleport(IslandGeneration.nexusLocation);
            player.setBedSpawnLocation(IslandGeneration.nexusLocation, true);
            com.Stranded.Scoreboard.scores(player);

            islands.getConfig().set("island." + name + ".home", IslandGeneration.nexusLocation);

            if (x >= 200002) {
                x = -200000;
                z += 1000;
            } else {
                x += 1000;
            }

            islands.getConfig().set("islandData.default.X", x);
            islands.getConfig().set("islandData.default.Z", z);


            islands.saveConfig();
            config.saveConfig();


        } else {
            sendWrongUse(player, new String[] {"/island create <name> <island>", "/island create "});
        }
    }
}
