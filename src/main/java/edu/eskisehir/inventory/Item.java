package edu.eskisehir.inventory;

import edu.eskisehir.utils.Node;

import java.util.HashMap;
import java.util.Map;

public class Item extends Node<Item> {
    private Map<Integer, Integer> grossRequirements = new HashMap<Integer, Integer>(); // gross requirements of the item
    private Map<Integer, Integer> plannedOrderDeliveries = new HashMap<>(); // deliveries needed to complete the item
    private Map<Integer, Integer> netRequirements = new HashMap<>(); // net requirements of the
    private Map<Integer, Integer> onHandFromPriorPeriod = new HashMap<>(); // information about on hand from prior period
    private Map<Integer, Integer> scheduledReceipts = new HashMap<>(); // scheduled receipts information week by week
    private int ID; // item ID of the item
    private String name; // name of the item
    private int multiplier; // how many products need to be produced when our root item will be produced
    private int leadTime; // how many weeks will take to deliver
    private int lotSizing; // lot sizing of item

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
        Inventory.items.itemList.remove(this);
        if (Inventory.items.itemList.contains(this)) {
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
        Item identical = Inventory.items.itemList.get(Inventory.items.itemList.indexOf(this));
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
