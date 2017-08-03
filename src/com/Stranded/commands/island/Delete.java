package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;

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
    public void run(String[] args) {

        Files f = new Files(p, "islands.yml");

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }
        if (!f.getConfig().getString("island." + p.getConfig().getString("island." + player.getName()) + ".owner").equals(player.getName())) {
            player.sendMessage("you are not the owner of this island, so you can't delete this island");
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
