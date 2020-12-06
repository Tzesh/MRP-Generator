package utils;

public class Tree {
    private Node root;
    private Node findedNode;

    public Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void findNode(int id) {
        if (root.getID() == id) {
            findedNode = root;
        } else {
            findNode(root, id);
        }
    }

    public void findNode(Node node, int id) {
        if (node.getID() == id) {
            findedNode = node;
            return;
        }

        Node child = node.firstChild;
        while (child != null) {
            findNode(child, id);
            child = child.nextSibling;
        }
    }

    public Node find(int id) {
        findNode(id);
        return findedNode;
    }

    public void displayPreorder() {
        displayPreorder(root, 0);
    }

    public void displayPreorder(Node baseNode, int indent) {
        for (int i = 0; i < indent; i++) System.out.print(" ");
        System.out.println(baseNode.toString());
        Node child = baseNode.firstChild;
        while (child != null) {
            displayPreorder(child, indent + 3);
            child = child.nextSibling;
        }
    }

    public void displayPostorder() {
        displayPostorder(root, 0);
    }

    public void displayPostorder(Node baseNode, int indent) {
        Node child = baseNode.firstChild;
        while (child != null) {
            displayPostorder(child, indent + 3);
            child = child.nextSibling;
        }
        for (int i = 0; i < indent; i++) System.out.print(" ");
        System.out.println(baseNode.toString());
    }


}

