package edu.eskisehir.utils;

public class Tree<T> {
    private Node<T> root;
    private Node<T> findedNode;

    public Tree(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() {
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

    public void findNode(Node<T> node, int id) {
        if (node.getID() == id) {
            findedNode = node;
            return;
        }

        Node<T> child = node.firstChild;
        while (child != null) {
            findNode(child, id);
            child = child.nextSibling;
        }
    }

    public Node<T> find(int id) {
        findNode(id);
        return findedNode;
    }

}

