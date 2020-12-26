package edu.eskisehir.inventory;

import edu.eskisehir.utils.Node;

import java.util.HashMap;
import java.util.Map;

public class Item extends Node<Item> {
    Map<Integer, Integer> grossRequirements = new HashMap<Integer, Integer>(); // demand of root (Shovel Complete) item with respect to weeks
    Map<Integer, Integer> plannedOrderDeliveries = new HashMap<>(); // deliveries needed to complete the item
    Map<Integer, Integer> netRequirements = new HashMap<>();
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
        int preAmount = grossRequirements.getOrDefault(week, 0);
        grossRequirements.put(week, amount + preAmount);
    }

    public void produce() { // we'll produce the item according to given demand information
        scheduledReceipts = Inventory.scheduledReceipts.get(ID);
        Inventory.items.items.remove(this);
        if (Inventory.items.items.contains(this)) {
            hasIdentical();
            return;
        }
        Inventory.scheduledReceipts.remove(ID);
        for (int week = 1; week <= 10; week++) {
            int demand = grossRequirements.getOrDefault(week, 0);
            int amount = getAmount();
            onHandFromPriorPeriod.put(week, amount);
            if (scheduledReceipts != null && scheduledReceipts.containsKey(week)) {
                amount += scheduledReceipts.get(week);
            }
            if (amount >= demand) {
                setAmount(amount - demand);
                demand = 0;
            } else {
                demand -= amount;
                setAmount(0);
                deliver(week, demand);
            }
            netRequirements.put(week, demand);
        }
        initializeVariables();
    }

    private void hasIdentical() {
        Item identical = Inventory.items.items.get(Inventory.items.items.indexOf(this));
        grossRequirements.keySet().forEach(week -> {
            identical.addDemand(week, grossRequirements.get(week));
        });
    }

    private void deliver(int week, int amount) { // to deliver items
        if (week - leadTime < 0) {
            System.out.println("This amount of item cannot be produced in the schedule");
            return;
        }
        int firstAmount = amount;
        netRequirements.put(week, amount);
        if (lotSizing == 0) {
            plannedOrderDeliveries.put(week - leadTime, amount); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        } else {
            if (amount > lotSizing) {
                amount = (double) amount / lotSizing % 1 == 0 ? amount : (amount / lotSizing * lotSizing) + lotSizing;
                plannedOrderDeliveries.put(week - leadTime, amount);
            } else {
                amount = lotSizing;
                plannedOrderDeliveries.put(week - leadTime, amount);
            }
            setAmount(amount - firstAmount);
        }
        if (this.firstChild != null) {
            Item child = (Item) this.firstChild;
            child.addDemand(week - leadTime, amount * child.multiplier);
            while (child.nextSibling != null) {
                child = (Item) child.nextSibling;
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
        Inventory.grossRequirements.put(ID, grossRequirements);
        Inventory.netRequirements.put(ID, netRequirements);
        Inventory.onHandFromPriorPeriod.put(ID, onHandFromPriorPeriod);
        Inventory.plannedOrderDeliveries.put(ID, plannedOrderDeliveries);

        if (this.firstChild != null) {
            Item child = (Item) this.firstChild;
            child.produce();
            while (child.nextSibling != null) {
                child = (Item) child.nextSibling;
                child.produce();
            }
        }

        Inventory.scheduledReceipts.put(ID, scheduledReceipts);
    }

    public int getLeadTime() {
        return leadTime;
    }

    @Override
    public String toString() {
        return "Item" + ID + "(" + name + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return ID == item.ID &&
                name.equals(item.name);
    }
}
