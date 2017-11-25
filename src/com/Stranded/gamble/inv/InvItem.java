package com.Stranded.gamble.inv;

import com.Stranded.Main;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.gamble.RandomItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class InvItem {

    public static String title = "Gamble: Random Item";
    private static HashMap<Player, Integer> countMapItem = new HashMap<>();
    private static HashMap<Player, ArrayList<ItemStack>> itemsMapItem = new HashMap<>();

    private static double getVariables() {

        Random random = new Random();

        int i = random.nextInt(7);
        while (i < 2) {
            i = random.nextInt(7);
        }
        return (double) i / 100;
    }

    public static void openInv(Main p, Player player) {

        if (Main.reloadPending) {
            player.sendMessage("the server is trying to reload, please wait just a second to let the server reload");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, title);
        player.openInventory(inv);
        countMapItem.put(player, 0);
        itemsMapItem.put(player, new ArrayList<>());

        ItemStack green = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemStack red = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        double x1 = getVariables();

        Main.reloadHolds += 1;

        for (int loop = 0; loop < 100; loop += 2) {

            double delay = Math.tan((double) loop / 72) / x1;

            Bukkit.getScheduler().runTaskLater(p, () -> {

                Inventory inventory = Bukkit.createInventory(null, 27, title);
                ArrayList<ItemStack> list = itemsMapItem.get(player);

                double count = countMapItem.get(player);
                if (count == 98) {
                    closeRandom(player, list.get(4), p);
                    return;
                }
                countMapItem.put(player, (int) count + 2);

                ArrayList<ItemStack> newList = new ArrayList<>();
                ItemStack newItem = RandomItems.getRandomItem();
                inventory.setItem(9, newItem);
                newList.add(newItem);
                if (list.size() > 9) {
                    list.remove(list.size() - 1);
                }

                for (int i = 0; i < 9; i++) {
                    inventory.setItem(i, red);
                }
                newList.addAll(list);
                for (int i = 9; i < 18; i++) {

                    if (newList.size() > i - 9) {
                        inventory.setItem(i, newList.get(i - 9));
                    }

                }
                for (int i = 18; i < 27; i++) {
                    inventory.setItem(i, red);
                }
                inventory.setItem(4, green);
                inventory.setItem(22, green);

                player.openInventory(inventory);

                itemsMapItem.put(player, newList);

            }, (long) delay);

        }
    }

    private static void closeRandom(Player player, ItemStack is, Main p) {
        new Thread(() -> {
            if (countMapItem.containsKey(player)) {
                player.sendMessage("you just won " + is.getType().toString().toLowerCase());
                player.getInventory().addItem(is);
                Bukkit.getScheduler().runTaskLater(p, () -> {
                    if (player.getOpenInventory().getTitle().equals("Gamble: Random Item")) {
                        player.closeInventory();
                    }

                    Main.reloadHolds -= 1;
                    if (Main.reloadPending && Main.reloadHolds == 0) {
                        Reload.reload(p);
                    }

                }, 50);
            }
        }
        ).start();
    }
}
