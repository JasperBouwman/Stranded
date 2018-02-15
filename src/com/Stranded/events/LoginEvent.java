package com.Stranded.events;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.Scoreboard;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import static com.Stranded.GettingFiles.getFiles;

public class LoginEvent implements Listener {

    private Main p;

    public LoginEvent(Main main) {
        p = main;
    }

    @SuppressWarnings("All")
    private Object isConnection(Player player) {
        Object nmsPlayer;
        try {
            nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException ignore) {
            return null;
        }
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onOnline(PlayerLoginEvent e) {

        PlayerUUID.setPlayerUUID(e.getPlayer());

        Bukkit.getScheduler().runTaskLater(p, () -> {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                Scoreboard.scores(pl);
            }

            Player player = e.getPlayer();
            String uuid = player.getUniqueId().toString();

            for (int i = 0; i < 10; i++) {
                if (isConnection(player) == null) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e1) {
                        //well... fuck after the tenth time, this means that the player isn't logged in, so he/she wont get the messages
                    }
                }
            }

            new ServerPingEvent(p).setPlayer(e.getPlayer());
            Files config = getFiles("config.yml");
            Files islands = getFiles( "islands.yml");
            Files pluginData = getFiles("pluginData.yml");
            Files warData = getFiles("warData.yml");

            ArrayList<String> listRemoved = (ArrayList<String>) config.getConfig().getStringList("online.removedIsland");
            ArrayList<String> listEvict = (ArrayList<String>) config.getConfig().getStringList("online.evict");
            ArrayList<String> listNewOwner = (ArrayList<String>) config.getConfig().getStringList("online.newOwner");
            if (config.getConfig().contains("online.removeAll." + uuid)) {
                config.getConfig().set("online.removeAll." + uuid, null);
                config.saveConfig();

                player.setLevel(0);
                player.getInventory().clear();
            }
            if (config.getConfig().contains("online.warWin." + uuid)) {
                String msg = config.getConfig().getString("online.warWin." + uuid);
                player.sendMessage(msg);
                config.getConfig().set("online.warWin." + uuid, null);
                config.saveConfig();

                Location l = (Location) islands.getConfig().get("island." + config.getConfig().getStringList("island." + uuid) + ".home");
                player.teleport(l);
            }
            if (config.getConfig().contains("online.warLose." + uuid)) {
                String msg = config.getConfig().getString("online.warLose." + uuid);
                player.sendMessage(msg);
                config.getConfig().set("online.warLose." +uuid, null);
                config.saveConfig();

                Location l = (Location) islands.getConfig().get("island." + config.getConfig().getStringList("island." + uuid) + ".home");
                player.teleport(l);
            }

            if (config.getConfig().contains("online.removedIslandByMod." + uuid)) {
                player.sendMessage("you island is removed by an moderator with the reason: " + config.getConfig().getString("online.removedIslandByMod." + uuid + ".reason"));

                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(l);

                config.getConfig().set("online.removedIslandByMod." + uuid, null);
                config.saveConfig();
                return;
            }
            if (listRemoved.contains(uuid)) {
                player.sendMessage("you island is removed by the owner");

                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(l);

                listRemoved.remove(uuid);
                config.getConfig().set("online.removedIsland", listRemoved);
                config.saveConfig();
                return;
            }
            if (listEvict.contains(uuid)) {
                player.sendMessage("you where removed by the owner from this island");

                Location l = (Location) pluginData.getConfig().get("plugin.hub.location");
                player.teleport(l);

                listEvict.remove(uuid);
                config.getConfig().set("online.evict", listEvict);
                config.saveConfig();
                return;
            }

            if (listNewOwner.contains(uuid)) {
                player.sendMessage("you are now the new owner if your island");
                listNewOwner.remove(uuid);
                config.getConfig().set("online.newOwner", listNewOwner);
                config.saveConfig();
            }
            if (config.getConfig().contains("online.newIslandOwner." + uuid)) {
                player.sendMessage(config.getConfig().getString("online.newIslandOwner." + uuid));
                config.getConfig().set("online.newIslandOwner." + uuid, null);
                config.saveConfig();
            }

            if (config.getConfig().getStringList("playersInWar").contains(uuid)) {
                for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                    if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(uuid)) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".blueSpawn"));
                        return;
                    }
                    if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(uuid)) {
                        player.teleport((Location) warData.getConfig().get("war.war." + warID + ".redSpawn"));
                        return;
                    }
                }
            }
        }, 1);

    }
}
