package edu.eskisehir.inventory;

import edu.eskisehir.utils.Node;

import java.util.HashMap;
import java.util.Map;

public class Item extends Node<Item> {
    Map<Integer, Integer> demands = new HashMap<Integer, Integer>(); // demand of root (Shovel Complete) item with respect to weeks
    Map<Integer, Integer> deliveries = new HashMap<>(); // deliveries needed to complete the item
    int ID; // item ID of the item
    String name; // name of the item
    int multiplier; // how many products need to be produced when our root item will be produced
    int leadTime; // how many weeks will take to deliver
    int lotSizing; // lot sizing of item

    public Item(int ID, String name, int leadTime, int lotSizing, int multiplier) {
        super(name, ID);
        this.name = name;
        this.ID = ID;
        this.leadTime = leadTime;
        this.multiplier = multiplier;
        this.lotSizing = lotSizing;
    }

    public void addDemand(int week, int amount) {
        demands.put(week, amount);
    }

    public void produce() { // we'll produce the item according to given demand information
        for (int week = 1; week <= 10; week++) {
            if (Inventory.scheduledReceipts.get(week) != null) {
                Inventory.amounts.put(ID, getAmount());
                Inventory.scheduledReceipts.remove(week);
            }
            if (demands.get(week) == null) continue;
            if (getAmount() > demands.get(week)) {
                Inventory.amounts.put(ID, getAmount() - demands.get(week));
                continue;
            }
            if (getAmount() == 0) {
                deliver(week, demands.get(week));
                continue;
            }
            deliver(week, demands.get(week) - getAmount());
            Inventory.amounts.put(ID, 0);
        }
        printMRPAndMoveOn();
    }

    private void deliver(int week, int amount) { // to deliver items
        if (week - leadTime < 0) {
            System.out.println("This amount of item cannot be produced in the schedule");
            System.exit(0);
        }
        if (lotSizing == 0) deliveries.put(week - leadTime, amount * multiplier); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        else deliveries.put(week - leadTime, amount % lotSizing == 0 ? amount * multiplier : ((amount * multiplier / lotSizing) * lotSizing) + 1); // checks whether the given amount is suitable or not for lotSizing
        if (Inventory.items.getRoot().equals(this)) return; // if the current node is not the root of the tree processes below are unnecessary
        while (this.nextSibling != null) this.nextSibling.addDemand(week, amount); // to add necessary demands that will be used in printMRPAndMoveOn method
        while (this.getFirstChild() != null) this.getFirstChild().addDemand(week, amount); // to add necessary demands that will be used in printMRPAndMoveOn method
    }

    private int getAmount() {
        return Inventory.amounts.get(this.ID) == null ? 0 : Inventory.amounts.get(this.ID);
    }

    public void printMRPAndMoveOn() {
        // prints out the MRP table according to given data
        // produces another item in the tree
        System.out.println("Deneme");
    }
}
