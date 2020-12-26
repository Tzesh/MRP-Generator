package edu.eskisehir.utils;

import edu.eskisehir.inventory.Item;

import java.util.LinkedList;
import java.util.List;

public class Tree<T> {
    private final Node<T> root;
    private Node<T> findedNode;
    public List<Item> items = new LinkedList<>();

    public Tree(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() {
        return root;
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

    public void getPreOrder() {
        getPreOrder(root);
    }

    public void getPreOrder(Node baseNode) {
        items.add((Item) baseNode);
        Node child = baseNode.firstChild;
        while (child != null) {
            getPreOrder(child);
            child = child.nextSibling;
        }
    }

}

