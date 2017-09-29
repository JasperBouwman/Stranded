package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.war.util.WarUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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

        Files f = new Files(p, "islands.yml");

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }
        if (!f.getConfig().getString("island." + p.getConfig().getString("island." + player.getName()) + ".owner").equals(player.getName())) {
            player.sendMessage("you are not the owner of this island, so you can't delete this island");
            return;
        }

        int testWar = WarUtil.testIfIsInWar(p, player);

        if (testWar == 1) {
            player.sendMessage("you can't delete your island when your island is pending for a war");
            return;
        }
        if (testWar == 2) {
            player.sendMessage("you can't delete your island when your island is in a war");
            return;
        }

        player.sendMessage("are you shure?, type /island confirm to delete island");
        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("deleteIsland");
        if (!list.contains(player.getName())) {
            list.add(player.getName());
            p.getConfig().set("deleteIsland", list);
        } else {
            player.sendMessage("to confirm type /island confirm");
        }

    }
}
