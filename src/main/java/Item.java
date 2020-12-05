import utils.Node;

import java.util.HashMap;
import java.util.Map;

public class Item extends Node {
    Map<Integer, Integer> demand = new HashMap<Integer, Integer>(); // demand of root (Shovel Complete) item with respect to weeks
    Map<Integer, Integer> deliveries = new HashMap<>(); // deliveries needed to complete the item
    int ID; // item ID of the item
    String name; // name of the item
    int multiplier; // how many products need to be produced when our root item will be produced
    int leadTime; // how many weeks will take to deliver
    int amount; // amount of item
    int lotSizing; // lot sizing of item

    public Item(String name, int ID, int leadTime, int amount, int multiplier, int lotSizing) {
        super(name, ID);
        this.name = name;
        this.ID = ID;
        this.leadTime = leadTime;
        this.amount = amount;
        this.multiplier = multiplier;
        this.lotSizing = lotSizing;
    }

    public void addDemand(int week, int amount) {
        demand.put(week, amount);
    }

    public void produce(Map<Integer, Integer> demand) { // we'll produce the item according to given demand information
        for (int week : demand.keySet()) {
            int amount = demand.get(week);
            if (isReady(week, amount)) {
                this.amount -= amount;
                deliveries.put(week, 0);
                continue; // we don't have to produce or deliver this item it's already in stock
            }
            deliver(week, amount); // we have to deliver or produce this item by starting (week - leadTime)th week
        }
        printMRPAndMoveOn();
    }

    public boolean isReady(int week, int amount) { // to check whether is given amount ready or not
        if (amount * multiplier < amount) return true; // we don't have to deal with required items to produce this item
        int required = this.amount - (multiplier * amount);
        //while (this.nextSibling != null) this.nextSibling.addDemand(week, amount); // to add necessary demands that will be used in printMRPAndMoveOn method
        //while (this.getFirstChild() != null) this.getFirstChild().addDemand(week, amount); // to add necessary demands that will be used in printMRPAndMoveOn method
        return false;
    }

    public void deliver(int week, int amount) { // to deliver items
        if (week - leadTime < 0) {
            System.out.println("This amount of item cannot be produced in the schedule");
            System.exit(0);
        }
        if (lotSizing == 0) deliveries.put(week - leadTime, amount); // we'll use this data in MRP, if lotSizing = 0 that means lotSizing is L4L and amount of delivery is not important
        else deliveries.put(week - leadTime, amount % lotSizing == 0 ? amount : ((amount / lotSizing) * lotSizing) + 1); // checks whether the given amount is suitable or not for lotSizing
    }

    public void printMRPAndMoveOn() {
        // prints out the MRP table according to given data
        // produces another item in the tree
    }

}
