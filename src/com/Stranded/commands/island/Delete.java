package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.commands.war.util.WarUtil;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.google.common.base.Joiner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.Permissions.hasPermission;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Delete extends CmdManager {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getAlias() {
        return "del";
    }

    @Override
    public void run(String[] args, Player player) {

        //island delete
        //island delete [island name] [reason]

        String uuid = player.getUniqueId().toString();

        if (Main.reloadPending) {
            player.sendMessage(ChatColor.RED + "The server is trying to reload, please wait just a second to let the server reload");
            return;
        }

        if (args.length != 1) {

            if (args.length > 2) {
                if (hasPermission(player, "Stranded.islandDelete")) {

                    Files islands = getFiles("islands.yml");
                    Files config = getFiles("config.yml");
                    if (islands.getConfig().contains("island." + args[1])) {

                        int testWar = WarUtil.testIfIsInWar(args[1]);

                        if (testWar == 1) {
                            player.sendMessage(ChatColor.RED + "You can't delete this island when this island is pending for a war");
                            return;
                        }
                        if (testWar == 2) {
                            player.sendMessage(ChatColor.RED + "You can't delete this island when this island is in a war");
                            return;
                        }

                        if (config.getConfig().contains("deleteIsland." + uuid)) {
                            player.sendMessage(ChatColor.RED + "You can't delete this island while you already are deleting your own");
                            return;
                        }

                        FancyMessage fm = new FancyMessage();

                        if (!config.getConfig().contains("deleteOtherIsland." + uuid)) {

                            fm.addText("Are you sure? Click ", Colors.GREEN);
                            fm.addText("here", Colors.DARK_GREEN);
                            fm.addCommand("/island confirm");
                            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
                            fm.addText(" to delete the island " + args[1] + ", you have 30 seconds to do this", Colors.GREEN);
                            fm.sendMessage(player);

                            Main.reloadHolds += 1;

                            int taskID = Bukkit.getScheduler().runTaskLater(p, () -> {

                                Files finalConfig = getFiles("config.yml");

                                player.sendMessage(ChatColor.RED + "Your island delete has been expired");
                                finalConfig.getConfig().set("deleteOtherIsland." + uuid, null);
                                finalConfig.saveConfig();

                                Main.reloadHolds -= 1;
                                if (Main.reloadPending && Main.reloadHolds == 0) {
                                    Reload.reload(p);
                                }

                            }, 600).getTaskId();

                            config.getConfig().set("deleteOtherIsland." + uuid + ".taskID", taskID);
                            config.getConfig().set("deleteOtherIsland." + uuid + ".island", args[1]);
                            config.getConfig().set("deleteOtherIsland." + uuid + ".reason", Joiner.on(" ").join(Arrays.asList(args).subList(3, args.length)));
                            config.saveConfig();

                        } else {
                            fm.addText("To confirm click ", Colors.GREEN);
                            fm.addText("here", Colors.DARK_GREEN);
                            fm.addCommand("/island confirm");
                            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
                            fm.addText(" to delete the island" + args[0], Colors.GREEN);
                            fm.sendMessage(player);
                        }
                        return;

                    } else {
                        player.sendMessage(ChatColor.RED + "This island doesn't exist");
                        return;
                    }
                }
            }
            sendWrongUse(player, "/island delete");
            return;
        }

        Files islands = getFiles("islands.yml");
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You can only use this when you are in an island and the owner of that island");
            return;
        }
        if (!islands.getConfig().getString("island." + config.getConfig().getString("island." + uuid) + ".owner").equals(uuid)) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this island, so you can't delete this island");
            return;
        }

        int testWar = WarUtil.testIfIsInWar(player);

        if (testWar == 1) {
            player.sendMessage(ChatColor.RED + "You can't delete your island when your island is pending for a war");
            return;
        }
        if (testWar == 2) {
            player.sendMessage(ChatColor.RED + "You can't delete your island when your island is in a war");
            return;
        }

        FancyMessage fm = new FancyMessage();

        if (config.getConfig().contains("deleteOtherIsland." + uuid)) {
            player.sendMessage(ChatColor.RED + "You can't delete your island while you already are deleting an island");
            return;
        }

        if (!config.getConfig().contains("deleteIsland." + uuid)) {

            fm.addText("Are you sure? Click ", Colors.GREEN);
            fm.addText("here", Colors.DARK_GREEN);
            fm.addCommand("/island confirm");
            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
            fm.addText(" to delete your island, you have 30 seconds to do this", Colors.GREEN);
            fm.sendMessage(player);

            Main.reloadHolds += 1;

            int taskID = Bukkit.getScheduler().runTaskLater(p, () -> {

                player.sendMessage(ChatColor.RED + "Your island delete has been expired");
                Files finalConfig = getFiles("config.yml");
                finalConfig.getConfig().set("deleteIsland." + player.getName(), null);
                finalConfig.saveConfig();

                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

            }, 600).getTaskId();

            config.getConfig().set("deleteIsland." + uuid, taskID);
            config.saveConfig();

        } else {
            fm.addText("To confirm click ", Colors.GREEN);
            fm.addText("here", Colors.DARK_GREEN);
            fm.addCommand("/island confirm");
            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
            fm.addText(" to delete your island", Colors.GREEN);
            fm.sendMessage(player);
        }
    }
}
