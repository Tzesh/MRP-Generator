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

        Node sibling = node.firstChild;
        while (sibling.nextSibling != null) {
            if (sibling.getID() == id) {
                return sibling;
            } else {
                sibling = sibling.nextSibling;
            }
        }

        sibling = node.firstChild;
        while (sibling.nextSibling != null) {
            if (sibling.hasChild()) {
                find(sibling, id);
            } else {
                continue;
            }
        }

        return null;
    }

}

