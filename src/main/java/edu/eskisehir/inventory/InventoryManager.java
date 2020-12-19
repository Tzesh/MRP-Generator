package edu.eskisehir.inventory;

import edu.eskisehir.utils.Tree;

import java.util.Map;

public class InventoryManager {

    public InventoryManager(boolean isDefault) {
        if (!isDefault) return;
        Item root = new Item("Shovel", 1605, 1, 60, 1); // we are starting to build our tree with the root item
        Inventory.items = new Tree<>(root);
        setDefaultItems();
    }
    
    private void setDefaultItems() {
        Inventory.items.find(1605).addChild(new Item("Top Handle", 13122, 1, 0, 1));
        Inventory.items.find(1605).addChild(new Item("Scoop-Shaft", 48, 3, 30, 1));
        Inventory.items.find(1605).addChild(new Item("Shaft", 118, 2, 0, 1));
        Inventory.items.find(1605).addChild(new Item("Nail", 62, 2, 50, 1));
        Inventory.items.find(1605).addChild(new Item("Rivet", 14127, 1, 60, 4));
        Inventory.items.find(1605).addChild(new Item("Scoop Assembly", 314, 1, 0, 1));

        Inventory.items.find(13122).addChild(new Item("Top Handle", 457, 2, 0, 1));
        Inventory.items.find(13122).addChild(new Item("Nail", 62, 2, 50, 2));
        Inventory.items.find(13122).addChild(new Item("Bracelet", 11495, 1, 0, 0));

        Inventory.items.find(11495).addChild(new Item("Top Handle", 129, 4, 0, 0));
        Inventory.items.find(11495).addChild(new Item("Top Handle", 1118, 3, 30, 0));


        Inventory.items.find(314).addChild(new Item("Scoop", 2142, 0, 0, 0));
        Inventory.items.find(314).addChild(new Item("Blade", 19, 0, 0, 0));
        Inventory.items.find(314).addChild(new Item("Rivet", 14127, 0, 0, 0));
    }

    public void setDemands(Map<Integer, Integer> demands) {
        Item root = (Item)Inventory.items.getRoot();
        for (Integer week : demands.keySet()) root.addDemand(week, demands.get(week));
    }

    public void produce() {
        Item root = (Item)Inventory.items.getRoot();
        root.produce();
    }
}
