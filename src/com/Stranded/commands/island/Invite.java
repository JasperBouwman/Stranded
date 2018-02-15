package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.fancyMassage.Colors;
import com.Stranded.fancyMassage.FancyMessage;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.api.ServerMessages.sendWrongUse;

public class Invite extends CmdManager {

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getAlias() {
        return "inv";
    }

    @Override
    public void run(String[] args, Player player) {

        //island invite <player name>

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (Main.reloadPending) {
            player.sendMessage(ChatColor.RED + "The server is trying to reload, please wait just a second to let the server reload");
            return;
        }

        if (!config.getConfig().contains("island." + uuid)) {
            player.sendMessage(ChatColor.RED + "You can't invite a player while you aren't in an island");
            return;
        }

        Files f = getFiles("islands.yml");
        if (!f.getConfig().getString("island." + config.getConfig().getString("island." + uuid) + ".owner").equals(uuid)) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this island, so you can't invite someone");
            return;
        }

        if (args.length == 2) {

            String playerName = args[1];
            //get player uuid
            String invitedUUID = PlayerUUID.getPlayerUUID(playerName);
            //test if player is found
            if (invitedUUID == null) {
                ArrayList<String> tempPlayers = PlayerUUID.getGlobalPlayerUUID(args[1]);
                if (tempPlayers.size() > 1) {
                    player.sendMessage(ChatColor.RED + "The player '" + args[1] + "' is not found, but there are more players found with the same name (not case sensitive)");
                    return;
                } else if (tempPlayers.size() == 0) {
                    player.sendMessage(ChatColor.RED + "There is no player found with this name");
                    return;
                } else {
                    //set full on player uuid
                    invitedUUID = tempPlayers.get(0);
                }
            }
            //test if player is found (not really necessary)
            if (invitedUUID == null) {
                player.sendMessage(ChatColor.RED + "This player isn't found, please make sure that you gave the right player name. this is case sensitive");
                return;
            }
            //get final player name (case sensitive)
            String invitedPlayerName = PlayerUUID.getPlayerName(invitedUUID);
            //get player
            Player invited = PlayerUUID.getPlayerExact(invitedPlayerName);

            if (invited != null) {

                if (config.getConfig().contains("island." + invitedUUID) && config.getConfig().getString("island." + invitedUUID).equals(config.getConfig().getString("island." + uuid))) {
                    player.sendMessage(ChatColor.RED + invitedPlayerName + " is already in your island");
                    return;
                }

                if (config.getConfig().contains("invited." + invitedUUID)) {
                    player.sendMessage(ChatColor.RED + "This player has already an invitation to join another island");
                    return;
                }

                String island = config.getConfig().getString("island." + uuid);

//                invited.sendMessage(ChatColor.DARK_GREEN + player.getName() + " asks if you want to join " + island + "?");
                FancyMessage fm = new FancyMessage();
                fm.addText(player.getName() + " asks if you want to join ", Colors.GREEN);
                fm.addText(island, Colors.DARK_GREEN);
                fm.addText("? To join click ", Colors.GREEN);
                fm.addText("here", Colors.DARK_GREEN);
                fm.addCommand("/island join");
                fm.addHover(new String[]{"/island join"}, new Colors[]{Colors.GREEN});
                fm.addText(" or to deny click ", Colors.AQUA);
                fm.addText("here", Colors.DARK_GREEN);
                fm.addCommand("/island ignore");
                fm.addHover(new String[]{"/island ignore"}, new Colors[]{Colors.GREEN});

                fm.sendMessage(invited);

                String islandOld = config.getConfig().getString("island." + invitedUUID);
                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
                if (old.size() == 1) {
                    invited.sendMessage(ChatColor.DARK_RED + "NOTE: If you leave you island, this will get deleted. there can't be less than 1 player in an island");
                }
                player.sendMessage(ChatColor.GREEN + "Your invitation has been sent to " + invited.getName());

                Main.reloadHolds += 1;

                String finalInvitedUUID = invitedUUID;

                int taskID = Bukkit.getScheduler().runTaskLater(p, () -> {

                    Files finalConfig = getFiles("config.yml");

                    if (Bukkit.getPlayer(UUID.fromString(finalConfig.getConfig().getString("invited." + finalInvitedUUID + ".inviter"))) != null) {
                        Bukkit.getPlayer(UUID.fromString(finalConfig.getConfig().getString("invited." + finalInvitedUUID + ".inviter")))
                                .sendMessage(invitedPlayerName + " hasn't reacted in time to join your island");
                    }
                    if (Bukkit.getPlayerExact(invitedPlayerName) != null) {
                        Bukkit.getPlayerExact(invitedPlayerName).sendMessage(
                                ChatColor.RED + "You ignored the inventation of the island " + finalConfig.getConfig().getString("invited." + finalInvitedUUID + ".newIsland"));
                    }

                    finalConfig.getConfig().set("invited." + finalInvitedUUID, null);
                    finalConfig.saveConfig();

                    Main.reloadHolds -= 1;
                    if (Main.reloadPending && Main.reloadHolds == 0) {
                        Reload.reload(p);
                    }

                }, 1000).getTaskId();

                config.getConfig().set("invited." + invitedUUID + ".newIsland", island);
                config.getConfig().set("invited." + invitedUUID + ".pendingID", taskID);
                config.getConfig().set("invited." + invitedUUID + ".inviter", uuid);

                config.saveConfig();

            } else {
                player.sendMessage(ChatColor.RED + "This player is not online");
            }

        } else {
            sendWrongUse(player, new String[]{"/island invite <player>", "/island invite "});
        }

    }
}