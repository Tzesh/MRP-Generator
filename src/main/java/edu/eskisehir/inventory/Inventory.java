package edu.eskisehir.inventory;

import edu.eskisehir.utils.Tree;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    public static Tree<Item> items = null; // we should define items tree as static in order to use it as every single data structure that we utilize
    public static Map<Integer, Integer> amounts = new HashMap<>(); // and the amounts of items should be declared separately from the Item class due to some items can be exist more than once
    public static Map<Integer, Map<Integer, Integer>> grossRequirements = new HashMap<>(); // grossRequirements for item IDs required for reducing time complexity
    public static Map<Integer, Map<Integer, Integer>> scheduledReceipts = new HashMap<>(); // scheduledReceipts for item IDs required for reducing time complexity
    public static Map<Integer, Map<Integer, Integer>> onHandFromPriorPeriod = new HashMap<>(); // onHandFromPriorPeriod for item IDs required for reducing time complexity
    public static Map<Integer, Map<Integer, Integer>> netRequirements = new HashMap<>(); // netRequirements for item IDs required for reducing time complexity
    public static Map<Integer, Map<Integer, Integer>> plannedOrderDeliveries = new HashMap<>(); // plannedOrderDeliveries for item IDs required for reducing time complexity

}
