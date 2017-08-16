package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.island.*;
import com.Stranded.commands.island.Edit;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class Island implements CommandExecutor {
    private Main p;

    public List<CmdManager> actions = new ArrayList<>();

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
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("you have to be a player to use this command");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
//§
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
//
//
//
//        if (args.length > 0) {
//
//            String sub = args[0];
//            if (sub.equalsIgnoreCase("info") || sub.equalsIgnoreCase("i")) {
////                Info.info(player, p);
//            } else if (sub.equalsIgnoreCase("delete") || sub.equalsIgnoreCase("del")) {
//
//            } else if (sub.equalsIgnoreCase("join")) {
////                Join.join(player, p);
//            } else if (sub.equalsIgnoreCase("invite")) {
//                if (args.length == 2) {
////                    Invite.invite(player, args[1], p);
//                } else {
//                    player.sendMessage("wrong use");
//                }
//            } else if (sub.equalsIgnoreCase("create") || sub.equalsIgnoreCase("cr")) {
//                if (args.length == 1) {
//                    player.sendMessage("pls enter a name and a island type");
//                } else if (args.length == 2) {
//                    Files f = new Files(p, "islands.yml");
//                    StringBuilder is = new StringBuilder().append("island types: ");
//                    for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {
//
//                        if (f.getConfig().getBoolean("islandData.islandTypes." + s + ".enabled")) {
//                            is.append(f.getConfig().getString("islandData.islandTypes." + s + ".name"));
//                            is.append(" ");
//                        }
//                    }
//                    player.sendMessage(is.toString());
//                } else {
//                    if (sub.equalsIgnoreCase("Create") || sub.equalsIgnoreCase("cr")) {
////                        Create.create(player, p, args[1], args[2]);
//                    }
//                }
//            } else if (sub.equalsIgnoreCase("edit")) {
//                if (args[1].equalsIgnoreCase("build")) {
//                    if (args.length == 3) {
////                        com.Stranded.commands.island.Edit.EditIsland(p, player, args[2]);
//                    } else {
//                        player.sendMessage("wrong use");
//                    }
//                } else if (args[1].equalsIgnoreCase("rename")) {
//                    if (args.length == 4) {
////                        com.Stranded.commands.island.Edit.Rename(p, args[2], args[3], player);
//                    } else {
//                        player.sendMessage("wrong use");
//                    }
//                }
//            }
//        }

        return false;
    }

    private void openBook(ItemStack book, Player p) {

        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte) 0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        p.getInventory().setItem(slot, old);
    }
}
