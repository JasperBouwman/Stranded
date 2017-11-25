package com.Stranded.commands.stranded;

import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;

public class MOTD extends CmdManager {
    @Override
    public String getName() {
        return "motd";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

    }
}
