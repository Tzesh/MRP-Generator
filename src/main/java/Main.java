import utils.Node;
import utils.Tree;

public class Main {
    public static void main(String[] args) {
        Item it = new Item("Shovel", 1605, 1, 60, 1, 10);

        Tree tree = new Tree(it);


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


    }
}
