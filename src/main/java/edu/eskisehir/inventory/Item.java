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
    private int amount; // amount of the item
    private int multiplier; // how many products need to be produced when our root item will be produced
    private int leadTime; // how many weeks will take to deliver
    private int lotSizing; // lot sizing of the item

    public Item(int ID, String name, int amount, int leadTime, int lotSizing, int multiplier) {
        super(name, ID);
        this.ID = ID;
        this.name = name;
        this.amount = amount;
        this.leadTime = leadTime;
        this.multiplier = multiplier;
        this.lotSizing = lotSizing;
    }

    public void addDemand(int week, int amount) { // we should consider if grossRequirements value is already in present
        int preAmount = grossRequirements.getOrDefault(week, 0);
        grossRequirements.put(week, amount + preAmount);
    }

    public void produce() { // we'll produce the item according to given demand information
        scheduledReceipts = Inventory.scheduledReceipts.get(ID);
        Inventory.items.itemList.remove(this);
        if (Inventory.items.itemList.contains(this)) {
            hasIdentical();
            return; // we got all we need the rest is just the calculation of MRP in identical item
        }
        for (int week = 1; week <= 10; week++) {
            int demand = grossRequirements.getOrDefault(week, 0);
            int amount = getAmount();
            onHandFromPriorPeriod.put(week, amount); // saving on hand from prior period before making calculations
            if (scheduledReceipts != null && scheduledReceipts.containsKey(week)) { // if any kind of scheduled receipt is available
                amount += scheduledReceipts.get(week);
            }
            if (amount >= demand) { // we don't have to deliver anything
                setAmount(amount - demand);
                demand = 0;
            } else {
                demand -= amount; // calculating the really required demand
                setAmount(0); // setting amount to 0
                deliver(week, demand); // requesting deliver
            }
            netRequirements.put(week, demand);
        }
        initializeVariables();
    }

    private void hasIdentical() { // to calculate identical items simultaneously
        Item identical = Inventory.items.itemList.get(Inventory.items.itemList.indexOf(this));
        grossRequirements.keySet().forEach(week -> {
            identical.addDemand(week, grossRequirements.get(week));
        });
    }

    private void deliver(int week, int amount) { // to deliver items
        if (week - leadTime < 0) { // this is obvious
            System.out.println("This amount of item cannot be produced in the schedule");
            return;
        }
        int firstAmount = amount; // we'll use it to set amount if and only if lotSizing different than L4L
        netRequirements.put(week, amount);
        if (lotSizing == 0) { // this means lotSizing is L4L which is we are only delivering how much we really need
            plannedOrderDeliveries.put(week - leadTime, amount); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        } else { // if it's not we should calculate the really amount of item according to lotSizing value
            if (amount > lotSizing) {
                amount = (double) amount / lotSizing % 1 == 0 ? amount : (amount / lotSizing * lotSizing) + lotSizing;
                plannedOrderDeliveries.put(week - leadTime, amount);
            } else {
                amount = lotSizing;
                plannedOrderDeliveries.put(week - leadTime, amount);
            }
            setAmount(amount - firstAmount); // and also we should set amount since we might be getting more than we need
        }
        if (this.firstChild != null) { // and then since our planned order deliveries are demands for the child items we should add this demand to them
            Item child = (Item) this.firstChild;
            child.addDemand(week - leadTime, amount * child.multiplier);
            while (child.nextSibling != null) { // on other words is there any child?
                child = (Item) child.nextSibling;
                child.addDemand(week - leadTime, amount * child.multiplier);
            }
        }
    }

    private int getAmount() { // to prevent confusing
        return amount;
    }

    private void setAmount(int amount) { // to prevent confusing
        this.amount = amount;
    }

    public void initializeVariables() { // we are saving all information into Inventory class to access them in Interface class its much easier and meaningful when compared to iterating and getting values
        Inventory.grossRequirements.put(ID, grossRequirements); // saving grossRequirements
        Inventory.netRequirements.put(ID, netRequirements); // saving netRequirements
        Inventory.onHandFromPriorPeriod.put(ID, onHandFromPriorPeriod); // saving onHandFromPriorPeriod
        Inventory.plannedOrderDeliveries.put(ID, plannedOrderDeliveries); // saving plannedOrderDeliveries

        if (this.firstChild != null) { // and of course we are going to next items (nodes) and produce them according to given demands we've gave
            Item child = (Item) this.firstChild;
            child.produce();
            while (child.nextSibling != null) { // on other words is there any child?
                child = (Item) child.nextSibling;
                child.produce();
            }
        }
    }

    public int getLeadTime() { // to use in Interface
        return leadTime;
    }

    @Override
    public String toString() { // to use in Interface
        return "Item" + ID + "(" + name + ")";
    }

    @Override
    public boolean equals(Object o) { // to make comparisons
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return ID == item.ID &&
                name.equals(item.name);
    }

    public int getLotSizing() { // to use in Interface
        return lotSizing;
    }
}
