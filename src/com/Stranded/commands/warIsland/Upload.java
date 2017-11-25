package com.Stranded.commands.warIsland;

import com.Stranded.commands.CmdManager;
import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Upload extends CmdManager {

    @Override
    public String getName() {
        return "upload";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland upload <warIslandFile>

        PastebinFactory factory = new PastebinFactory();
        Pastebin pastebin = factory.createPastebin("00f19ba9c201baad83c7defcacf01156");

        Response<String> userKey = pastebin.login("StrandedBukkitPlugin", "StrandedPlugin");

        if (userKey.hasError()) {
            player.sendMessage(userKey.getError());
            return;
        }

        final PasteBuilder post = factory.createPaste();

        File file = new File(p.getDataFolder().toPath().toString() + "/warIslands", "NaMe.yml");

        StringBuilder str = new StringBuilder();

        try {
            List<String> lines = Files.readLines(file, Charsets.UTF_8);

            for (String line : lines) {
                str.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        post.setTitle("This is an test");
        post.setRaw(str.toString());
        post.setMachineFriendlyLanguage("text");
        post.setVisiblity(PasteVisiblity.Public);
        post.setExpire(PasteExpire.TenMinutes);

        Paste paste = post.build();

        final Response<String> postResult = pastebin.post(paste, userKey.get());

        if (postResult.hasError()) {
            player.sendMessage("could not be uploaded, try later. error: " + postResult.getError());
        } else {
            player.sendMessage("post: " + postResult.get());
        }
    }
}
