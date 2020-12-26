package edu.eskisehir.inventory;

import edu.eskisehir.utils.Tree;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    public static Tree<Item> items = null; // we should define items tree as static in order to use it
    public static Map<Integer, Integer> amounts = new HashMap<>(); // and the amounts of items should be declared separately from the Item class due to some items can be exist more than once
    public static Map<Integer, Map<Integer, Integer>> grossRequirements = new HashMap<>();
    public static Map<Integer, Map<Integer, Integer>> scheduledReceipts = new HashMap<>();
    public static Map<Integer, Map<Integer, Integer>> onHandFromPriorPeriod = new HashMap<>();
    public static Map<Integer, Map<Integer, Integer>> netRequirements = new HashMap<>();
    public static Map<Integer, Map<Integer, Integer>> plannedOrderDeliveries = new HashMap<>();

}
