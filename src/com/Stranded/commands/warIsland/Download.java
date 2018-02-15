package com.Stranded.commands.warIsland;

import com.Stranded.commands.CmdManager;
import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.response.Response;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Download extends CmdManager {
    @Override
    public String getName() {
        return "download";
    }

    @Override
    public String getAlias() {
        return "dl";
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland download <file> <pasteBinCode>

        if (args.length == 3) {

            PastebinFactory factory = new PastebinFactory();
            Pastebin pastebin = factory.createPastebin("");

//            final String pasteToRead = "tbhDs150";
            String pasteName = args[1];

            String[] name = args[2].split("/");
            final String pasteToRead = name[name.length - 1];
            player.sendMessage("downloading");

            final Response<String> rawResponse = pastebin.getRawPaste(pasteToRead);
            if (rawResponse.hasError()) {
                player.sendMessage("this file has not been found");
                return;
            }

            if (!pasteName.endsWith(".yml")) {
                pasteName += ".yml";
            }

            File f = new File(p.getDataFolder() + "/warIslands", pasteName);

            try {
                FileWriter fw = new FileWriter(f);
                PrintWriter pw = new PrintWriter(fw);

                pw.print(rawResponse.get());

                pw.flush();
                pw.close();

                player.sendMessage("saved");

            } catch (IOException e) {
                player.sendMessage("oops, there went something wrong...");
            }
            return;

        }

        player.sendMessage("Usage: /warisland download <war island name> <pastebin code>");
    }
}
