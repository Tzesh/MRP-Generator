package utils;

public class Tree {
    private Node root;

    public Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public Node find(int id) {
        if (root.getID() == id) {
            return root;
        } else {
            return find(root, id);
        }
    }

    public Node find(Node node, int id) {
        for (Node n : node.getChildren()) {
            if (n.getID() == id) {
                return n;
            }

        }

        for (Node n : node.getChildren()) {
            if(n.hasChild())
                return find(n, id);
            else
                continue;
        }

        return null;
    }

}

