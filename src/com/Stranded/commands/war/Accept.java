package com.Stranded.commands.war;

import com.Stranded.commands.CmdManager;

public class Accept extends CmdManager {
    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getAlias() {
        return "ac";
    }

    @Override
    public void run(String[] args) {

        if (!p.getConfig().contains("island." + player.getName())) {
            player.sendMessage("you aren't in an island");
            return;
        }

    }
}
