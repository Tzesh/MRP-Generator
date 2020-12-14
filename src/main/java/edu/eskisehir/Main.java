package edu.eskisehir;

import edu.eskisehir.inventory.InventoryManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /* Necessary declarations but not all the information are filled up
         * In the upcoming versions of the program all the constant data will be stored in .txt file and also be used from .txt file */
        InventoryManager inventoryManager = new InventoryManager(true);

        Scanner scanner = new Scanner(System.in);
        Map<Integer, Integer> demands;

        System.out.println("Welcome to MRP Designer," +
                "\nPlease enter demands according to weeks like given example below:" +
                "\nWeeks: 4 5 7 9" +
                "\nQuantity: 60 50 100 30");
        while (true) {
            System.out.print("Weeks: ");
            String[] weeks = scanner.nextLine().split(" ");
            System.out.print("Quantities: ");
            String[] quantities = scanner.nextLine().split(" ");
            try {
                demands = setDemands(weeks, quantities);
            } catch (NumberFormatException exception) {
                System.out.println("Given weeks and quantities must be integer, please re-type.");
                continue;
            }
            if (isAppropriate(demands)) break;
        }
        System.out.println("Processes have begun");
        inventoryManager.setDemands(demands);
        inventoryManager.produce();
    }

    private static Map<Integer, Integer> setDemands(String[] weeks, String[] quantities) throws NumberFormatException {
        Map<Integer, Integer> demands = new HashMap<>();
        for (String week : weeks)
            for (String quantity : quantities) demands.put(Integer.parseInt(week), Integer.parseInt(quantity));
        return demands;
    }

    private static boolean isAppropriate(Map<Integer, Integer> demands) {
        for (Integer key : demands.keySet()) {
            if (key < 4 && demands.get(key) > 30) { // It's not possible due to lead times of items
                System.out.println("It's not possible to produce a Item 1605 A.K.A 'Shovel Complete' more than 30 before 4th week");
                return false;
            }
            if (key > 10) { // since we're calculating only period which longs 10 weeks
                System.out.println("Program is only calculating 10 weeks period");
                return false;
            }
        }
        return true; // that means our given keys (demands) are valid for our program
    }
}