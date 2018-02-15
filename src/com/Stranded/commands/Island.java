package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.island.*;
import com.Stranded.commands.island.Edit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Island implements CommandExecutor {
    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public Island(Main main) {
        p = main;

        actions.add(new Create());
        actions.add(new Edit());
        actions.add(new Info());
        actions.add(new Invite());
        actions.add(new Join());
        actions.add(new Leave());
        actions.add(new Delete());
        actions.add(new Evict());
        actions.add(new Confirm());
        actions.add(new Home());
        actions.add(new Move());
        actions.add(new Visit());
        actions.add(new Scoreboard());
        actions.add(new Ignore());
        actions.add(new Transfer());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("you have to be a player to use this command");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {

            // Create a written book
            ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            BookMeta meta = (BookMeta) book.getItemMeta();
            meta.addPage("I see you used /island\n\n" +
                            "In this book you can see how to use it properly",
                    "These are the Commands\n\n" +
                            "/island Create [island name] [island type]:    creates an island\n\n" +
                            "/island info:    shows all the info about your island\n\n" +
                            "/island invite:   invite another player to join your island",
                    "/island join [Island name]:    join another persons island\n\n" +
                            "/island delete:   deletes your island\n\n" +
                            "/island leave:   leave your island");
            book.setItemMeta(meta);
            // open the book
            openBook(book, player);
            return false;
        }

        for (CmdManager action : this.actions) {
            if (args[0].toLowerCase().equals(action.getName()) || args[0].toLowerCase().equals(action.getAlias())) {
                action.setMain(p);
                action.run(args, player);
                return false;
            }
        }
        player.sendMessage("this is not a sub-command");
        return false;
    }

    @SuppressWarnings("All")
    private void openBook(ItemStack book, Player player) {

        int slot = player.getInventory().getHeldItemSlot();
        ItemStack old = player.getInventory().getItem(slot);
        player.getInventory().setItem(slot, book);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte) 0);
        buf.writerIndex(1);

        try {
            //get minecraft server version
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            //get player handle
            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
            //get player connection
            Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            Class<?> packetDataSerializer = Class.forName("net.minecraft.server." + version + ".PacketDataSerializer");
            Constructor<?> packetDataSerializerConstructor = packetDataSerializer.getConstructor(ByteBuf.class);

            Class<?> packetPlayOutCustomPayload = Class.forName("net.minecraft.server." + version + ".PacketPlayOutCustomPayload");
            Constructor packetPlayOutCustomPayloadConstructor = packetPlayOutCustomPayload.getConstructor(String.class,
                    Class.forName("net.minecraft.server." + version + ".PacketDataSerializer"));

            connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"))
                    .invoke(connection, packetPlayOutCustomPayloadConstructor.newInstance("MC|BOpen", packetDataSerializerConstructor.newInstance(buf)));

        } catch (Exception ex) {
            player.getInventory().addItem(book);
        }
        player.getInventory().setItem(slot, old);
    }
}
