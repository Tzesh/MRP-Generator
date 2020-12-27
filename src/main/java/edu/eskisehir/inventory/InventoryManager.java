package edu.eskisehir.inventory;

import edu.eskisehir.utils.Tree;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryManager {
    public String itemsPath = null;

    public InventoryManager(boolean setItems) { // if we don't want to setItems while initializing InventoryManager it's possible
        if (!setItems) return;
        try {
            setItems();
        } catch (IOException e) {
            System.out.println("An unknown error has occurred.");
        }
    }

    private void setItems() throws IOException { // simply gets the information in items.txt if it's doesn't exist just creates a temporary file than uses it
        File items = new File("items.txt");
        if (!items.exists()) {
            InputStream input = getClass().getResourceAsStream("/constants/items.txt");
            items = File.createTempFile("items", ".txt");
            OutputStream out = new FileOutputStream(items);
            int read;
            byte[] bytes = new byte[1024];

            while ((read = input.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.close();
            System.out.println("Created a temporary items.txt due to not found in the same directory of the program.");
        }
        itemsPath = items.getAbsolutePath();
        List<String> lines = Files.readAllLines(Paths.get(items.getAbsolutePath()));
        lines.forEach(line -> {
            if (!line.contains("//") && line.length() != 0) {
                String[] information = line.split(",  ");
                //root-item-ID,  Item-ID,  Name of item,  Amount-on-Hand,  Scheduled-Receipt,  Arrival-on-week,  Lead-Time,  Lot-Sizing-Rule,  Multiplier
                int rootID = Integer.parseInt(information[0]);
                int itemID = Integer.parseInt(information[1]);
                String name = information[2];
                int amount = Integer.parseInt(information[3]);
                int scheduledReceipt = Integer.parseInt(information[4]);
                int arrivalOnWeek = Integer.parseInt(information[5]);
                int leadTime = Integer.parseInt(information[6]);
                int lotSizing = Integer.parseInt(information[7]);
                int multiplier = Integer.parseInt(information[8]);
                if (rootID == 0) {
                    Item root = new Item(itemID, name, amount, leadTime, lotSizing, multiplier);
                    if (arrivalOnWeek != 0 && scheduledReceipt != 0) {
                        Map<Integer, Integer> receipts = new HashMap<Integer, Integer>() {{
                            put(arrivalOnWeek, scheduledReceipt);
                        }};
                        Inventory.scheduledReceipts.put(itemID, receipts);
                    }
                    Inventory.items = new Tree(root);
                } else {
                    Inventory.items.find(rootID).addChild(new Item(itemID, name, amount, leadTime, lotSizing, multiplier));
                    if (arrivalOnWeek != 0 && scheduledReceipt != 0) {
                        Map<Integer, Integer> receipts = new HashMap<Integer, Integer>() {{
                            put(arrivalOnWeek, scheduledReceipt);
                        }};
                        Inventory.scheduledReceipts.put(itemID, new HashMap<>(receipts));
                    }
                }
            }
        });
        Inventory.items.getPreOrder();
    }

    public void setDemands(Map<Integer, Integer> demands) { // to set demands of the root according to table in Interface
        for (Integer week : demands.keySet()) Inventory.items.getRoot().addDemand(week, demands.get(week));
    }

    public void produce() { // to produce the root
        Item root = (Item) Inventory.items.getRoot();
        root.produce();
    }
}
