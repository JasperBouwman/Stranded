package com.Stranded.commands.war;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;

public class Generate extends CmdManager {

    @Override
    public String getName() {
        return "generate";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args) {

        Files f = new Files(p, "warIslands");

        if (!f.getConfig().contains("warIslands.offset")) {
            return;
        }

    }
}
