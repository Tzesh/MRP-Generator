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
                demand = 0;
                Inventory.amounts.put(ID, amount - demand);
            }
            else {
                demand -= amount;
                Inventory.amounts.put(ID, 0);
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
        amount *= multiplier;
        requirements.put(week, amount);
        if (lotSizing == 0) {
            deliveries.put(week - leadTime, amount); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        }
        else {
            if (amount > lotSizing) {
                amount = (double)amount / lotSizing % 1 == 0 ? amount : (amount / lotSizing * lotSizing) + lotSizing;
                Inventory.amounts.put(ID, amount - firstAmount);
            }
            else amount = lotSizing;
            deliveries.put(week - leadTime, amount);
            Inventory.amounts.put(ID, amount - firstAmount);
        }
        if (this.firstChild != null) this.firstChild.addDemand(week, amount);
        if (this.nextSibling != null) this.nextSibling.addDemand(week, firstAmount);

    }

    private int getAmount() {
        return Inventory.amounts.get(this.ID) == null ? 0 : Inventory.amounts.get(this.ID);
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
            this.firstChild.produce();
            this.firstChild.initializeVariables();
        }
        if (this.nextSibling != null) {
            this.nextSibling.produce();
            this.nextSibling.initializeVariables();
        }
        Inventory.scheduledReceipts.put(ID, scheduledReceipts);
    }

    private Map<Integer, Integer> updateVariables(Map<Integer, Integer> existing, Map<Integer, Integer> replacement) {
        for (Integer temp : existing.keySet()) {
            int tempValue = existing.get(temp) != null ? existing.get(temp) : 0;
            int tempValue2 = replacement.get(temp) != null ? replacement.get(temp) : 0;
            existing.put(temp, tempValue + tempValue2);
        }
        return existing;
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
