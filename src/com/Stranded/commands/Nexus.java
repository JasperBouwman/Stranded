package com.Stranded.commands;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.Permissions.hasPermission;
import static com.Stranded.Permissions.noPermMessage;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Nexus implements CommandExecutor {

    private Main p;

    public Nexus(Main instance) {
        p = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        //nexus
        //nexus <island>
        //nexus remove
        //nexus remove <radius>

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this");
            return false;
        }
        Player player = (Player) sender;

        if (!hasPermission(player, "Stranded.spawnNexus", false)
                && !hasPermission(player, "Stranded.islandNexus", false)
                && !hasPermission(player, "Stranded.killNexus", false)) {

            player.sendMessage(noPermMessage);
            return false;
        }

        switch (args.length) {
            case 0:
                if (!hasPermission(player, "Stranded.spawnNexus")) {
                    return false;
                }
                spawnNexus(player.getLocation());
                player.sendMessage(ChatColor.DARK_AQUA + "There is a nexus spawned at your location");
                break;
            case 1:
                if (args[0].equalsIgnoreCase("remove")) {
                    if (!hasPermission(player, "Stranded.killNexus")) {
                        return false;
                    }
                    int i = killNearbyNexus(player.getLocation(), 10);
                    if (i == 1) {
                        player.sendMessage(ChatColor.GREEN + "1 nexus was removed in a radius of 10");
                    } else {
                        player.sendMessage(ChatColor.GREEN + "" + i + " nexus where removed in a radius of 10");
                    }
                    return false;
                }
                if (!hasPermission(player, "Stranded.islandNexus")) {
                    return false;
                }
                Files islands = getFiles("islands.yml");
                Files config = getFiles("config.yml");
                if (islands.getConfig().contains("island." + args[0])) {

                    String oldUUID = islands.getConfig().getString("island." + args[0] + ".UUID");
                    String newUUID = spawnNexus((Location) islands.getConfig().get("island." + args[0] + ".home"));

                    ArrayList<String> nexusUUIDs = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
                    nexusUUIDs.remove(oldUUID);
                    try {
                        Bukkit.getEntity(UUID.fromString(oldUUID)).remove();
                    } catch (Exception ignore) {
                    }

                    nexusUUIDs.add(newUUID);
                    config.getConfig().set("nexus.uuid", nexusUUIDs);
                    config.saveConfig();

                    islands.getConfig().set("island." + args[0] + ".UUID", newUUID);
                    islands.saveConfig();
                    player.sendMessage(ChatColor.DARK_AQUA + "Nexus spawned");
                } else {
                    player.sendMessage(ChatColor.RED + "This island doesn't exist");
                }

                break;
            case 2:

                if (args[0].equalsIgnoreCase("remove")) {
                    if (!hasPermission(player, "Stranded.killNexus")) {
                        return false;
                    }
                    try {
                        int i = killNearbyNexus(player.getLocation(), Integer.parseInt(args[1]));
                        if (i == 1) {
                            player.sendMessage(ChatColor.GREEN + "1 nexus was removed in a radius of " + args[1]);
                        } else {
                            player.sendMessage(ChatColor.GREEN + "" + i + " nexus where removed in a radius of " + args[1]);
                        }
                        return false;
                    } catch (NumberFormatException nfe) {
                        player.sendMessage(ChatColor.RED + "Your radius must be a number");
                    }

                } else {
                    sendWrongUse(player, new String[]{"/nexus", "/nexus"}, new String[]{"/nexus <island>", "/nexus "}, new String[]{"/nexus remove [radius]", "/nexus remove "}, true, false, false);
                }
                break;
            default:
                sendWrongUse(player, new String[]{"/nexus", "/nexus"}, new String[]{"/nexus <island>", "/nexus "}, new String[]{"/nexus remove [radius]", "/nexus remove "}, true, false, false);
                break;
        }
        return false;
    }

    private int killNearbyNexus(Location l, int radius) {
        Files config = getFiles("config.yml");
        List<String> uuid = config.getConfig().getStringList("nexus.uuid");
        int i = 0;
        for (Entity e : l.getWorld().getEntities()) {
            if (e instanceof Villager) {
                if (l.distance(e.getLocation()) <= radius) {
                    if (uuid.contains(e.getUniqueId().toString())) {
                        e.remove();
                        uuid.remove(e.getUniqueId().toString());
                        i++;
                    }
                }
            }
        }
        config.getConfig().set("nexus.uuid", uuid);
        config.saveConfig();

        return i;
    }

    private String spawnNexus(Location l) {
        Files config = getFiles("config.yml");

        Villager v = l.getWorld().spawn(l, Villager.class);

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
        list.add(v.getUniqueId().toString());
        config.getConfig().set("nexus.uuid", list);
        config.saveConfig();

        v.setCustomName("§2Nexus");
        v.setCustomNameVisible(true);
        v.setProfession(Profession.NITWIT);
        v.setAI(false);
        v.isSilent();
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);

        return v.getUniqueId().toString();
    }
}
