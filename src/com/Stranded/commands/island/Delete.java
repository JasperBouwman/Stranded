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
                if (player.hasPermission("Stranded.deleteOtherIsland")) {

                    Files islands = new Files(p, "islands.yml");
                    if (islands.getConfig().contains("island." + args[1])) {

                        int testWar = WarUtil.testIfIsInWar(p, args[1]);

                        if (testWar == 1) {
                            player.sendMessage(ChatColor.RED + "You can't delete this island when this island is pending for a war");
                            return;
                        }
                        if (testWar == 2) {
                            player.sendMessage(ChatColor.RED + "You can't delete this island when this island is in a war");
                            return;
                        }

                        FancyMessage fm = new FancyMessage();

                        if (p.getConfig().contains("deleteIsland." + uuid)) {
                            player.sendMessage("you can't delete this island while you already are deleting your own");
                            return;
                        }

                        if (!p.getConfig().contains("deleteOtherIsland." + uuid)) {

                            fm.addText("Are you sure? Click ", Colors.GREEN);
                            fm.addText("here", Colors.DARK_GREEN);
                            fm.addCommand("/island confirm");
                            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
                            fm.addText(" to delete the island " + args[1] + ", you have 30 seconds to do this", Colors.GREEN);
                            fm.sendMessage(player);

                            Main.reloadHolds += 1;

                            int taskID = Bukkit.getScheduler().runTaskLater(p, () -> {

                                player.sendMessage(ChatColor.RED + "Your island delete has been expired");
                                p.getConfig().set("deleteOtherIsland." + uuid, null);
                                p.saveConfig();

                                Main.reloadHolds -= 1;
                                if (Main.reloadPending && Main.reloadHolds == 0) {
                                    Reload.reload(p);
                                }

                            }, 600).getTaskId();

                            p.getConfig().set("deleteOtherIsland." + uuid + ".taskID", taskID);
                            p.getConfig().set("deleteOtherIsland." + uuid + ".island", args[1]);
                            p.getConfig().set("deleteOtherIsland." + uuid + ".reason", Joiner.on(" ").join(Arrays.asList(args).subList(3, args.length)));
                            p.saveConfig();

                        } else {
                            fm.addText("To confirm click ", Colors.GREEN);
                            fm.addText("here", Colors.DARK_GREEN);
                            fm.addCommand("/island confirm");
                            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
                            fm.addText(" to delete the island" + args[0], Colors.GREEN);
                            fm.sendMessage(player);
                        }

                    } else {
                        player.sendMessage("This island doesn't exist");
                        return;
                    }
                }
                return;
            }

            player.sendMessage(ChatColor.RED + "Usage: /island delete");
            return;
        }

        Files islands = new Files(p, "islands.yml");

        if (!p.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You can only use this when you are in an island and the owner of that island");
            return;
        }
        if (!islands.getConfig().getString("island." + p.getConfig().getString("island." + uuid) + ".owner").equals(uuid)) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this island, so you can't delete this island");
            return;
        }

        int testWar = WarUtil.testIfIsInWar(p, player);

        if (testWar == 1) {
            player.sendMessage(ChatColor.RED + "You can't delete your island when your island is pending for a war");
            return;
        }
        if (testWar == 2) {
            player.sendMessage(ChatColor.RED + "You can't delete your island when your island is in a war");
            return;
        }

        FancyMessage fm = new FancyMessage();

        if (p.getConfig().contains("deleteOtherIsland." + uuid)) {
            player.sendMessage("you can't delete your island while you already are deleting someones island");
            return;
        }

        if (!p.getConfig().contains("deleteIsland." + uuid)) {

            fm.addText("Are you sure? Click ", Colors.GREEN);
            fm.addText("here", Colors.DARK_GREEN);
            fm.addCommand("/island confirm");
            fm.addHover(new String[]{"/island confirm"}, new Colors[]{Colors.GREEN});
            fm.addText(" to delete your island, you have 30 seconds to do this", Colors.GREEN);
            fm.sendMessage(player);

            Main.reloadHolds += 1;

            int taskID = Bukkit.getScheduler().runTaskLater(p, () -> {

                player.sendMessage(ChatColor.RED + "Your island delete has been expired");
                p.getConfig().set("deleteIsland." + player.getName(), null);
                p.saveConfig();

                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

            }, 600).getTaskId();

            p.getConfig().set("deleteIsland." + uuid, taskID);
            p.saveConfig();

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
