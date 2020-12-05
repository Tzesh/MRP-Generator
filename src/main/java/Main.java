import utils.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /* Necessary declarations but not all the information are filled up
        * In the upcoming versions of the program all the constant data will be stored in .txt file and also be used from .txt file */
        Item root = new Item("Shovel", 1605, 1, 60, 1, 10);
        Tree tree = new Tree(root);

        tree.find(1605).addChild(new Item("Top Handle", 13122, 0, 0, 0, 0));
        tree.find(1605).addChild(new Item("Scoop-Shaft", 48, 0, 0, 0, 0));
        tree.find(1605).addChild(new Item("Shaft", 118, 0, 0, 0, 0));
        tree.find(1605).addChild(new Item("Nail", 62, 0, 0, 0, 0));
        tree.find(1605).addChild(new Item("Rivet", 14127, 0, 0, 0, 0));
        tree.find(1605).addChild(new Item("Scoop Assembly", 314, 0, 0, 0, 0));

        tree.find(13122).addChild(new Item("Top Handle", 457, 0, 0, 0, 0));
        tree.find(13122).addChild(new Item("Nail", 62, 0, 0, 0, 0));
        tree.find(13122).addChild(new Item("Bracelet", 11495, 0, 0, 0, 0));

        tree.find(11495).addChild(new Item("Top Handle", 129, 0, 0, 0, 0));
        tree.find(11495).addChild(new Item("Top Handle", 1118, 0, 0, 0, 0));


        tree.find(314).addChild(new Item("Scoop", 2142, 0, 0, 0, 0));
        tree.find(314).addChild(new Item("Blade", 19, 0, 0, 0, 0));
        tree.find(314).addChild(new Item("Rivet", 14127, 0, 0, 0, 0));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to MRP Designer," +
                "\nPlease enter demands according to weeks like given example below:" +
                "\nWeeks: 4 5 7 9" +
                "\nQuantity: 60 50 100 30");
        while (true) {
            System.out.print("Weeks: ");
            String[] weeks = scanner.nextLine().split(" ");
            System.out.print("Quantities: ");
            String[] quantities = scanner.nextLine().split(" ");
            Map<Integer, Integer> demands;
            try {
                demands = setDemands(weeks, quantities);
            } catch (NumberFormatException exception) {
                System.out.println("Given weeks and quantities must be integer, please re-type.");
                continue;
            }
            if (isAppropriate(demands)) break;
        }
        System.out.println("Processes have begun");

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
