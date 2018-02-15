package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import static com.Stranded.GettingFiles.getFiles;

public class Pos1 extends CmdManager {

    @Override
    public String getName() {
        return "pos1";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland pos1

        if (args.length == 1) {

            Files f = getFiles("warIslands.yml");
            Location l = player.getLocation();
            String uuid = player.getUniqueId().toString();

            if (args[0].equalsIgnoreCase("pos1")) {

                f.getConfig().set("warIslands.offset." + uuid + ".first", l);
                if (f.getConfig().contains("warIslands.offset." + uuid + ".second")) {

                    Location L2 = (Location) f.getConfig().get("warIslands.offset." + uuid + ".second");

                    int minX = Math.min(l.getBlockX(), L2.getBlockX());
                    int minY = Math.min(l.getBlockY(), L2.getBlockY());
                    int minZ = Math.min(l.getBlockZ(), L2.getBlockZ());
                    int maxX = Math.max(l.getBlockX(), L2.getBlockX());
                    int maxY = Math.max(l.getBlockY(), L2.getBlockY());
                    int maxZ = Math.max(l.getBlockZ(), L2.getBlockZ());

                    int size = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);

                    player.sendMessage(String.format("First selection set (%d, %d, %d) (%d)", l.getBlockX(), l.getBlockY(), l.getBlockZ(), size));

                } else {
                    player.sendMessage(String.format("First selection set (%d, %d, %d)", l.getBlockX(), l.getBlockY(), l.getBlockZ()));
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
