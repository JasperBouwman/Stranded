package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import static com.Stranded.GettingFiles.getFiles;

public class Pos2 extends CmdManager {

    @Override
    public String getName() {
        return "pos2";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland pos2

        if (args.length == 1) {

            Files f = getFiles("warIslands.yml");
            Location l = player.getLocation();
            String uuid = player.getUniqueId().toString();

            if (args[0].equalsIgnoreCase("pos2")) {

                f.getConfig().set("warIslands.offset." + uuid + ".second", l);
                if (f.getConfig().contains("warIslands.offset." + uuid + ".first")) {

                    Location L2 = (Location) f.getConfig().get("warIslands.offset." + uuid + ".first");

                    int minX = Math.min(l.getBlockX(), L2.getBlockX());
                    int minY = Math.min(l.getBlockY(), L2.getBlockY());
                    int minZ = Math.min(l.getBlockZ(), L2.getBlockZ());
                    int maxX = Math.max(l.getBlockX(), L2.getBlockX());
                    int maxY = Math.max(l.getBlockY(), L2.getBlockY());
                    int maxZ = Math.max(l.getBlockZ(), L2.getBlockZ());

                    int size = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);

                    player.sendMessage(String.format("Second selection set (%d, %d, %d) (%d)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), size));

                } else {
                    player.sendMessage(String.format("Second selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
                }
                f.saveConfig();

            } else {
                player.sendMessage("wrong use");
            }
        } else {
            player.sendMessage("wrong use");
        }

    }
}
