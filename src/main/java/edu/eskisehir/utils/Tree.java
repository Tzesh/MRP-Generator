package edu.eskisehir.utils;

public class Tree<T> {
    private Node<T> root;

    public Tree(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public Node<T> find(int id) {
        if (root.getID() == id) return root;
        else return find(root, id);
    }

    public Node<T> find(Node<T> node, int id) {
        Node<T> sibling = node.firstChild;
        while (sibling != null) {
            if (sibling.getID() == id) return sibling;
            sibling = sibling.nextSibling;
        }

        sibling = node.firstChild;
        while (sibling != null) {
            if (sibling.hasChild()) return find(sibling, id);
            sibling = sibling.nextSibling;
        }
        return null;
    }
}

