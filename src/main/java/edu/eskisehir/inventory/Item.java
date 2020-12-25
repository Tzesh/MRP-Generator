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
        Map<Integer, Integer> scheduledReceipt = Inventory.scheduledReceipts.get(ID) == null ? new HashMap<>() : Inventory.scheduledReceipts.get(ID);
        Map<Integer, Integer> onHandFromPriorPeriod = Inventory.onHandFromPriorPeriod.get(ID) == null ? new HashMap<>() : Inventory.onHandFromPriorPeriod.get(ID);
        for (int week = 1; week <= 10; week++) {
            if (scheduledReceipt.containsKey(week)) {
                Inventory.amounts.put(ID, getAmount() + scheduledReceipt.get(ID));
            }
            if (onHandFromPriorPeriod.containsKey(week) && onHandFromPriorPeriod.get(week) != 0) onHandFromPriorPeriod.put(week, getAmount() + onHandFromPriorPeriod.get(week));
            else onHandFromPriorPeriod.put(week, getAmount());
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
        Inventory.onHandFromPriorPeriod.put(ID, onHandFromPriorPeriod);
        initializeVariables();
    }

    private void deliver(int week, int amount) { // to deliver items
        if (week - leadTime < 0) {
            System.out.println("This amount of item cannot be produced in the schedule");
            System.exit(0);
        }
        amount *= multiplier;
        if (lotSizing == 0)
            deliveries.put(week - leadTime, amount); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        else
            deliveries.put(week - leadTime, (amount / (float)lotSizing) * lotSizing == (amount / (float)lotSizing) * lotSizing ? amount / lotSizing * lotSizing : (amount / lotSizing * lotSizing) + lotSizing); // checks whether the given amount is suitable or not for lotSizing
        if (this.nextSibling != null)
            this.nextSibling.addDemand(week, amount); // to add necessary demands that will be used in printMRPAndMoveOn method
        if (this.getFirstChild() != null)
            this.getFirstChild().addDemand(week, amount); // to add necessary demands that will be used in printMRPAndMoveOn method
    }

    private int getAmount() {
        return Inventory.amounts.get(this.ID) == null ? 0 : Inventory.amounts.get(this.ID);
    }

    public void initializeVariables() {
        // prints out the MRP table according to given data
        // produces another item in the tree

        if (this.nextSibling != null) {
            this.nextSibling.produce();
            this.nextSibling.initializeVariables();
        }
        if (this.getFirstChild() != null) {
            this.getFirstChild().produce();
            this.getFirstChild().initializeVariables();
        }
    }

    public Map<Integer, Integer> getDemands() {
        return demands;
    }

    public Map<Integer, Integer> getDeliveries() {
        return deliveries;
    }

    @Override
    public String toString() {
        return "Item" + ID + "(" + name + ")";
    }
}
