package com.Stranded.commands.tabComplete;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.Island;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandTabComplete implements TabCompleter {

    private static List<String> ISLAND = new ArrayList<>();
    private static List<String> ISLAND_CREATE_NAME = new ArrayList<>();
    private static List<String> ISLAND_DELETE = new ArrayList<>(); /*returns a list of island to delete {only for OPs}*/
    private static List<String> ISLAND_EDIT = new ArrayList<>(); /*returns all islands*/
    private static List<String> ISLAND_EDIT_ISLAND = new ArrayList<>(); /*enable:disable:rename*/
    private static List<String> ISLAND_EVICT = new ArrayList<>(); /*returns island members {only for islandOwner !self}*/
    private static List<String> ISLAND_INVITE = new ArrayList<>(); /*returns all players !island members*/
    private static List<String> ISLAND_SCOREBOARD = new ArrayList<>(); /*on:off:show:{show Integer}*/
    private static List<String> ISLAND_TRANSVER = new ArrayList<>(); /*returns island members {only for islandOwner !self}*/
    private static List<String> ISLAND_VISIT = new ArrayList<>();
    private Main p;

    public IslandTabComplete(Main main) {
        p = main;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if (!(commandSender instanceof Player)) {
            return null;
        }

        clearTabComplete();
        fillTabComplete(args, (Player) commandSender);

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0].replace(ChatColor.GREEN.toString(), ""), ISLAND,
                    new ArrayList<>(ISLAND.size()));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("visit")) {
                //island visit
                return StringUtil.copyPartialMatches(args[1], ISLAND_VISIT,
                        new ArrayList<>(ISLAND_VISIT.size()));
            }
            if (args[0].equalsIgnoreCase("invite")) {
                //island invite
                return StringUtil.copyPartialMatches(args[1], ISLAND_INVITE,
                        new ArrayList<>(ISLAND_INVITE.size()));
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                //island create <name>
                return StringUtil.copyPartialMatches(args[2], ISLAND_CREATE_NAME,
                        new ArrayList<>(ISLAND_CREATE_NAME.size()));
            }
        }

        return null;
    }

    private void clearTabComplete() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (f.getType() == List.class) {
                try {
                    ((List<String>) f.get(f)).clear();
                } catch (IllegalAccessException e) {
                    //this should just work
                }
            }
        }
    }

    private void fillTabComplete(String[] args, Player player) {

        String uuid = player.getUniqueId().toString();

        Files islands = new Files(p, "islands.yml");

        for (CmdManager command : new Island(p).actions) {
            ISLAND.add(command.getName());
        }

        for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
            if (islands.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
                ISLAND_CREATE_NAME.add(islands.getConfig().getString("islandData.islandTypes." + s + ".name"));
            }
        }

        if (player.hasPermission("Stranded.deleteIsland")) {
            ISLAND_DELETE.addAll(islands.getConfig().getConfigurationSection("island").getKeys(false));
        }

        for (String s : islands.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
            ISLAND_EDIT.add(islands.getConfig().getString("islandData.islandTypes." + s + ".name"));
        }
        ISLAND_EDIT_ISLAND.addAll(Arrays.asList("enable", "disable", "rename"));

        if (p.getConfig().contains("island." + uuid)) {
            String island = p.getConfig().getString("island." + uuid);
            if (islands.getConfig().getString("island." + island + ".owner").equals(uuid)) {
                for (String tempUUID : islands.getConfig().getStringList("island." + island + ".members")) {
                    if (!tempUUID.equals(uuid)) {
                        String playerName = PlayerUUID.getPlayerName(tempUUID, p);
                        ISLAND_EVICT.add(playerName);
                    }
                }
            }
        }

        if (p.getConfig().contains("island." + uuid)) {
            String island = p.getConfig().getString("island." + uuid);
            if (islands.getConfig().getString("island." + island + ".owner").equals(uuid)) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    ISLAND_INVITE.add(pl.getName());
                }
                for (String tempUUID : islands.getConfig().getStringList("island." + island + ".members")) {
                    String playerName = PlayerUUID.getPlayerName(tempUUID, p);
                    ISLAND_INVITE.remove(playerName);
                }
            }
        }

        if (args.length == 2) {
            ISLAND_VISIT.addAll(islands.getConfig().getConfigurationSection("island").getKeys(false));

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!p.getConfig().contains("island." + online.getName())) {
                    ISLAND_INVITE.add(online.getName());
                } else if (!p.getConfig().getString("island." + online.getName()).equals(p.getConfig().getString("island." + player.getName()))) {
                    ISLAND_INVITE.add(online.getName());
                }
            }
        }

    }
}
