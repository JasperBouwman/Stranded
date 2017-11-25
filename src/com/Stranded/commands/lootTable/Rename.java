package com.Stranded.commands.lootTable;

import com.Stranded.commands.CmdManager;
import com.google.common.base.Charsets;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Rename extends CmdManager {

    @Override
    public String getName() {
        return "rename";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable rename <name> <newName>
        //todo rename lootTable in lootBoxes (eh, no i guess)

        if (args.length == 3) {

            File oldFile = new File(p.getDataFolder() + "/lootTables", args[1]);
            File newFile = new File(p.getDataFolder() + "/lootTables", args[2]);

            if (newFile.exists()) {
                player.sendMessage("this lootTable already exist");
            } else if (!oldFile.exists()) {
                player.sendMessage("this lootTable doesn't exist");
            } else {

                try {
                    List<String> lines = com.google.common.io.Files.readLines(oldFile, Charsets.UTF_8);

                    if (!oldFile.delete()) {
                        player.sendMessage("can not rename file");
                        return;
                    }

                    StringBuilder str = new StringBuilder(lines.size());

                    for (String line : lines) {
                        str.append(line).append("\n");
                    }

                    FileWriter fw = new FileWriter(newFile);
                    PrintWriter pw = new PrintWriter(fw);

                    pw.print(str.toString());

                    pw.flush();
                    pw.close();


                } catch (IOException e) {
                    player.sendMessage("can not rename file");
                }

            }
        } else {
            player.sendMessage("wrong use");
        }

    }
}
