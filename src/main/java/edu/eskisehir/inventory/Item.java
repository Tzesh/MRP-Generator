package edu.eskisehir.inventory;

import edu.eskisehir.utils.Node;

import java.util.HashMap;
import java.util.Map;

public class Item extends Node<Item> {
    Map<Integer, Integer> demands = new HashMap<Integer, Integer>(); // demand of root (Shovel Complete) item with respect to weeks
    Map<Integer, Integer> deliveries = new HashMap<>(); // deliveries needed to complete the item
    Map<Integer, Integer> requirements = new HashMap<>();
    Map<Integer, Integer> onHandFromPriorPeriod = new HashMap<>();
    Map<Integer, Integer> scheduledReceipts = new HashMap<>();
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
        scheduledReceipts = Inventory.scheduledReceipts.get(ID);
        Inventory.scheduledReceipts.remove(ID);
        for (int week = 1; week <= 10; week++) {
            int demand = demands.getOrDefault(week, 0);
            int amount = getAmount();
            onHandFromPriorPeriod.put(week, amount);
            if (scheduledReceipts != null && scheduledReceipts.containsKey(week)) {
                amount += scheduledReceipts.get(week);
            }
            if (amount >= demand) {
                setAmount(amount - demand);
                demand = 0;
            }
            else {
                demand -= amount;
                setAmount(0);
                deliver(week, demand);
            }
            requirements.put(week, demand);
        }
        initializeVariables();
    }

    private void deliver(int week, int amount) { // to deliver items
        if (week - leadTime < 0) {
            System.out.println("This amount of item cannot be produced in the schedule");
            return;
        }
        int firstAmount = amount;
        requirements.put(week, amount);
        if (lotSizing == 0) {
            deliveries.put(week - leadTime, amount); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        }
        else {
            if (amount > lotSizing) {
                amount = (double)amount / lotSizing % 1 == 0 ? amount : (amount / lotSizing * lotSizing) + lotSizing;
                deliveries.put(week - leadTime, amount);
            }
            else {
                amount = lotSizing;
                deliveries.put(week - leadTime, amount);
            }
            setAmount(amount - firstAmount);
        }
        if (this.firstChild != null) {
            Item child = (Item)this.firstChild;
            child.addDemand(week - leadTime, amount * child.multiplier);
            while (child.nextSibling != null) {
                child = (Item)child.nextSibling;
                child.addDemand(week - leadTime, amount * child.multiplier);
            }
        }
    }

    private int getAmount() {
        if (Inventory.amounts.containsKey(this.ID)) return Inventory.amounts.get(this.ID);
        Inventory.amounts.put(ID, 0);
        return 0;
    }

    private void setAmount(int amount) {
        Inventory.amounts.put(ID, amount);
    }

    public void initializeVariables() {
        // prints out the MRP table according to given data
        // produces another item in the tree
        if (Inventory.grossRequirements.containsKey(ID)) Inventory.grossRequirements.put(ID, updateVariables(Inventory.grossRequirements.get(ID), demands));
        else Inventory.grossRequirements.put(ID, demands);
        if (Inventory.netRequirements.containsKey(ID)) Inventory.netRequirements.put(ID, updateVariables(Inventory.netRequirements.get(ID), requirements));
        else Inventory.netRequirements.put(ID, requirements);
        if (Inventory.onHandFromPriorPeriod.containsKey(ID)) Inventory.onHandFromPriorPeriod.put(ID, updateVariables(Inventory.onHandFromPriorPeriod.get(ID), onHandFromPriorPeriod));
        else Inventory.onHandFromPriorPeriod.put(ID, onHandFromPriorPeriod);
        if (Inventory.plannedOrderDeliveries.containsKey(ID)) Inventory.plannedOrderDeliveries.put(ID, updateVariables(Inventory.plannedOrderDeliveries.get(ID), deliveries));
        else Inventory.plannedOrderDeliveries.put(ID, deliveries);

        if (this.firstChild != null) {
            Item child = (Item)this.firstChild;
            child.produce();
            while (child.nextSibling != null) {
                child = (Item)child.nextSibling;
                child.produce();
            }
        }

        Inventory.scheduledReceipts.put(ID, scheduledReceipts);
    }

    private Map<Integer, Integer> updateVariables(Map<Integer, Integer> existing, Map<Integer, Integer> replacement) {
            for (Integer temp2 : replacement.keySet()) {
                int tempValue2 = replacement.get(temp2) != null ? replacement.get(temp2) : 0;
                int tempValue1 = replacement.get(temp2) != null ? replacement.get(temp2) : 0;
                if (existing.containsKey(temp2)) existing.put(temp2, tempValue1 + tempValue2);
                else existing.put(temp2, tempValue2);
            }
        return existing;
    }

    @Override
    public String toString() {
        return "Item" + ID + "(" + name + ")";
    }
}
