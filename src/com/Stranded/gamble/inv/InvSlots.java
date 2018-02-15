package com.Stranded.gamble.inv;

import com.Stranded.Main;
import com.Stranded.commands.stranded.Reload;
import com.Stranded.gamble.RandomItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class InvSlots {

    public static String title = "Gamble: Slots";

    private static HashMap<String, HashMap<Integer, Integer>> countMap = new HashMap<>();
    private static HashMap<String, HashMap<Integer, ArrayList<ItemStack>>> itemsMap = new HashMap<>();
    private static HashMap<String, Boolean> endMap = new HashMap<>();

    private static HashMap<Player, HashMap<Integer, HashMap<Material, Integer>>> finalMap = new HashMap<>();
    private static HashMap<Player, ArrayList<Integer>> winList = new HashMap<>();

    private static double getVariables() {
        double max = 0.07;
        double min = 0.02;
        return new Random().nextDouble() * (max - min) + min;
    }

    public static void openInv(Main p, Player player, int width, int height, boolean command) {

        if (Main.reloadPending) {
            player.sendMessage("the server is trying to reload, please wait just a second to let the server reload");
            return;
        }

        if (width != 3 && width != 5 && width != 7 && width != 9) {
            player.sendMessage("for the width you only can use 3, 5, 7, 9");
            return;
        }
        if (command) {
            height = height * 9;
        }

        if (height != 54 && height != 45 && height != 36 && height != 27) {
            player.sendMessage("for the height you can use 3, 4, 5, 6");
            return;
        }

        int offset;

        switch (width) {
            case 3:
                offset = 3;
                break;
            case 5:
                offset = 2;
                break;
            case 7:
                offset = 1;
                break;
            default:
                offset = 0;
                break;
        }

        Main.reloadHolds += 1;

        Inventory inv = Bukkit.createInventory(null, height, title);
        player.openInventory(inv);

        ItemStack black = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        int finalHeight = height;

        for (int slot = 0; slot < width; slot++) {
            final int finalSlot = slot;

            HashMap<Integer, ArrayList<ItemStack>> initMap = new HashMap<>();
            initMap.put(finalSlot, new ArrayList<>());
            itemsMap.put(player.getName() + finalSlot, initMap);

            HashMap<Integer, Integer> initMap1 = new HashMap<>();
            initMap1.put(finalSlot, 0);
            countMap.put(player.getName() + finalSlot, initMap1);

            endMap.put(player.getName() + finalSlot, false);
            double x1 = getVariables();

            for (int loop = 0; loop < 100; loop += 2) {

                double delay = Math.tan((double) loop / 72) / x1;

                Bukkit.getScheduler().runTaskLater(p, () -> {

                    Inventory inventory = Bukkit.createInventory(null, finalHeight, title);

                    ArrayList<ItemStack> list = itemsMap.get(player.getName() + finalSlot).get(finalSlot);

                    for (int l = 0; l < width; l++) {
                        if (l == finalSlot) {
                            double count = countMap.get(player.getName() + finalSlot).get(finalSlot);
                            HashMap<Integer, Integer> initMap4 = new HashMap<>();
                            initMap4.put(finalSlot, (int) (count + 2));
                            countMap.put(player.getName() + finalSlot, initMap4);

                            if (count == 98) {
                                endMap.put(player.getName() + finalSlot, true);
                            }
                        }
                    }

                    ArrayList<ItemStack> newList = new ArrayList<>();
                    ItemStack newItem = RandomItems.getRandomItem();
                    inventory.setItem(9, newItem);
                    newList.add(newItem);
                    if (list.size() > finalHeight / 9 - 1) {
                        list.remove(list.size() - 1);
                    }

                    newList.addAll(list);

                    int endCount = 0;

                    for (int l = 0; l < width; l++) {
                        if (l == finalSlot) {
                            int listLoop = 0;
                            for (int i = offset; i < finalHeight; i = i + 9) {
                                if (newList.size() > listLoop) {
                                    inventory.setItem(i + finalSlot, newList.get(listLoop));
                                }
                                listLoop++;
                            }
                        } else {
                            int listLoop = 0;
                            ArrayList<ItemStack> otherList = itemsMap.get(player.getName() + l).get(l);
                            for (int i = offset; i < finalHeight; i = i + 9) {
                                if (otherList.size() > listLoop) {
                                    inventory.setItem(i + l, otherList.get(listLoop));
                                }
                                listLoop++;
                            }
                        }
                        if (endMap.get(player.getName() + l)) {
                            endCount++;
                        }

                    }

                    for (int off = 0; off < offset; off++) {
                        for (int i = 0; i < finalHeight; i += 9) {
                            inventory.setItem(i + off, black);
                        }
                    }

                    for (int off = 0; off < offset; off++) {
                        for (int i = 8; i < finalHeight; i += 9) {
                            inventory.setItem(i - off, black);

                        }
                    }

                    if (endCount == width) {
                        closeRandom(player, inventory, finalHeight, offset, p);
                    }

                    player.openInventory(inventory);

                    HashMap<Integer, ArrayList<ItemStack>> list1 = new HashMap<>();
                    list1.put(finalSlot, newList);

                    itemsMap.put(player.getName() + finalSlot, list1);

                }, (long) delay);
            }
        }
    }

    private static void addWin(Material m, int size, Player player) {
        int i = 0;

        HashMap<Integer, HashMap<Material, Integer>> tempMap = finalMap.get(player);

        while (tempMap.containsKey(i)) {
            i++;
        }

        HashMap<Material, Integer> map = new HashMap<>();
        map.put(m, size);
        tempMap.put(i, map);
        finalMap.put(player, tempMap);
    }

    private static void addWin(ArrayList<Integer> list, Player player) {
        ArrayList<Integer> tempList = winList.get(player);
        tempList.addAll(list);
        winList.put(player, tempList);
    }

    private static void closeRandom(Player player, Inventory inv, int height, int offset, Main p) {

            finalMap.put(player, new HashMap<>());
            winList.put(player, new ArrayList<>());

            //test horizontal
            for (int y = 0; y < height / 9; y++) {
                ItemStack last = new ItemStack(Material.AIR);
                ArrayList<Integer> tempList = new ArrayList<>();
                int tempSlot = 0;
                int count = 1;
                for (int x = offset; x < 9 - offset; x++) {
                    ItemStack is = inv.getItem(toCoordinate(x, y));

                    if (last.equals(is)) {
                        count++;
                        tempList.add(tempSlot);
                    }
                    if (count > 1 && !last.equals(is)) {
                        addWin(last.getType(), count, player);
                        count = 1;

                        tempList.add(tempSlot);
                        addWin(tempList, player);
                        tempList.clear();
                    }
                    last = is;
                    tempSlot = toCoordinate(x, y);
                }
                if (count > 1) {
                    addWin(last.getType(), count, player);
                    tempList.add(tempSlot);
                    addWin(tempList, player);
                }

            }


            for (int y = 0; y < inv.getSize() && y < height / 9 - 2; y++) {
                ItemStack last = new ItemStack(Material.AIR);
                int count = 1;
                ArrayList<Integer> tempList = new ArrayList<>();
                int tempSlot = 0;
                int yTemp = y;
                for (int x = offset; x < 9 - offset && toCoordinate(x, yTemp) < inv.getSize(); x++) {
                    int finalSlot = toCoordinate(x, yTemp);
                    ItemStack is = inv.getItem(finalSlot);
                    if (last.equals(is)) {
                        count++;
                        tempList.add(tempSlot);
                    }
                    if (count > 2 && !last.equals(is)) {
                        addWin(last.getType(), count - 1, player);
                        count = 1;

                        tempList.add(tempSlot);
                        addWin(tempList, player);
                        tempList.clear();
                    }
                    if (count == 2 && !last.equals(is)) {
                        tempList.clear();
                    }
                    if (!last.equals(is)) {
                        count = 1;
                        tempList.clear();
                    }
                    last = is;
                    yTemp++;
                    tempSlot = finalSlot;


                }
                if (count > 2) {
                    addWin(last.getType(), count - 1, player);
                    tempList.add(tempSlot);
                    addWin(tempList, player);
                }
            }


            for (int xTemp = offset + 1; xTemp < 7 - offset; xTemp++) {
                ItemStack last = new ItemStack(Material.AIR);
                int count = 0;
                int y = 0;
                ArrayList<Integer> tempList = new ArrayList<>();
                int tempSlot = 0;
                for (int x = xTemp; x < 9 - offset && toCoordinate(x, y) < inv.getSize(); x++) {
                    int finalSlot = toCoordinate(x, y);
                    ItemStack is = inv.getItem(finalSlot);
                    if (last.equals(is)) {
                        count++;
                        tempList.add(tempSlot);
                    }
                    if (count > 2 && !last.equals(is)) {
                        addWin(last.getType(), count - 1, player);
                        count = 1;

                        tempList.add(tempSlot);
                        addWin(tempList, player);
                        tempList.clear();
                    }
                    if (count == 2 && !last.equals(is)) {
                        tempList.clear();
                    }
                    if (!last.equals(is)) {
                        count = 1;
                        tempList.clear();
                    }

                    last = is;
                    y++;
                    tempSlot = finalSlot;
                }
                if (count > 2) {
                    addWin(last.getType(), count - 1, player);
                    tempList.add(tempSlot);
                    addWin(tempList, player);
                }
            }


            for (int xTemp = offset + 2; xTemp < 9 - offset; xTemp++) {
                ItemStack last = new ItemStack(Material.AIR);
                int count = 0;
                int yTemp = 0;
                ArrayList<Integer> tempList = new ArrayList<>();
                int tempSlot = 0;
                for (int x = xTemp; x >= offset && toCoordinate(x, yTemp) < inv.getSize(); x--) {
                    int finalSlot = toCoordinate(x, yTemp);
                    ItemStack is = inv.getItem(finalSlot);
                    if (last.equals(is)) {
                        count++;
                        tempList.add(tempSlot);
                    }
                    if (count > 2 && !last.equals(is)) {
                        addWin(last.getType(), count - 1, player);
                        count = 1;

                        tempList.add(tempSlot);
                        addWin(tempList, player);
                        tempList.clear();
                    }
                    if (count == 2 && !last.equals(is)) {
                        tempList.clear();
                    }
                    if (!last.equals(is)) {
                        count = 1;
                        tempList.clear();
                    }
                    last = is;
                    yTemp++;
                    tempSlot = finalSlot;
                }
                if (count > 2) {
                    addWin(last.getType(), count - 1, player);
                    tempList.add(tempSlot);
                    addWin(tempList, player);
                }
            }

            for (int y = 1; y < height / 9 - 2; y++) {
                ItemStack last = new ItemStack(Material.AIR);
                int count = 0;
                int yTemp = y;
                ArrayList<Integer> tempList = new ArrayList<>();
                int tempSlot = 0;
                for (int x = 8 - offset; x >= offset && toCoordinate(x, yTemp) < inv.getSize(); x--) {
                    int finalSlot = toCoordinate(x, yTemp);
                    ItemStack is = inv.getItem(finalSlot);
                    if (last.equals(is)) {
                        count++;
                        tempList.add(tempSlot);
                    }
                    if (count > 2 && !last.equals(is)) {
                        addWin(last.getType(), count - 1, player);
                        count = 1;

                        tempList.add(tempSlot);
                        addWin(tempList, player);
                        tempList.clear();
                    }
                    if (count == 2 && !last.equals(is)) {
                        tempList.clear();
                    }
                    if (!last.equals(is)) {
                        count = 1;
                        tempList.clear();
                    }
                    last = is;
                    yTemp++;
                    tempSlot = finalSlot;
                }
                if (count > 2) {
                    addWin(last.getType(), count - 1, player);
                    tempList.add(tempSlot);
                    addWin(tempList, player);
                }
            }


            HashMap<Integer, HashMap<Material, Integer>> tempMap = finalMap.get(player);
            ArrayList<ItemStack> itemList = new ArrayList<>();

            for (HashMap<Material, Integer> value : tempMap.values()) {
                for (Material key : value.keySet()) {
                    itemList.addAll(player.getInventory().addItem(new ItemStack(key, (value.get(key) - 1) * (value.get(key) - 1))).values());
                }
            }

            if (itemList.size() != 0) {
                player.sendMessage("you inventory was full, so some of your won items are dropped in the floor");
                dropItems(player.getLocation(), itemList);
            }

            for (int i = 0; i < inv.getSize(); i++) {
                ArrayList<Integer> list = winList.get(player);
                if (!list.contains(i)) {
                    inv.setItem(i, new ItemStack(Material.AIR));
                }
            }

            Bukkit.getScheduler().runTaskLater(p, () -> {
                if (player.getOpenInventory().getTitle().equals("Gamble: Slots")) {
                    player.closeInventory();
                }

                Main.reloadHolds -= 1;
                if (Main.reloadPending && Main.reloadHolds == 0) {
                    Reload.reload(p);
                }

            }, 200);
    }

    private static int toCoordinate(int x, int y) {
        return (y) * 9 + (x);
    }

    private static void dropItems(Location l, ArrayList<ItemStack> itemList) {
        for (ItemStack item : itemList) {
            l.setY(l.getY() + 1);
            l.getWorld().dropItem(l, item);
        }
    }

}
