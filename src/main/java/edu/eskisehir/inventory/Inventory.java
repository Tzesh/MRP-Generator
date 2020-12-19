package edu.eskisehir.inventory;

import edu.eskisehir.utils.Tree;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    static Tree<Item> items = null; // we should define items tree as static in order to use it
    static Map<Integer, Integer> amounts = new HashMap<>(); // and the amounts of items should be declared separately from the Item class due to some items can be exist more than once
    static Map<Integer, Integer> scheduledReceipts = new HashMap<>();
}
