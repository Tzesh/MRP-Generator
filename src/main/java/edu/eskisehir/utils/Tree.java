package edu.eskisehir.utils;

import edu.eskisehir.inventory.Item;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> { // our generic m-ary Tree design (LinkedList implementation)
    private final Node<T> root; // root of the tree
    private Node<T> findedNode; // if we're looking for the same node that would be make things easier
    public List<Item> itemList = new ArrayList<>(); // to use items

    public Tree(Node<T> root) {
        this.root = root;
    }

    public Node<T> getRoot() {
        return root;
    }

    public void findNode(int id) { // to find node
        if (root.getID() == id) {
            findedNode = root;
        } else {
            findNode(root, id);
        }
    }

    public void findNode(Node<T> node, int id) { // to find node
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

    public Node<T> find(int id) { // to find node
        findNode(id);
        return findedNode;
    }

    public void getPreOrder() {
        getPreOrder(root);
    } // basically iterates preordered in tree

    public void getPreOrder(Node baseNode) { // to save all of the nodes once and forget about traversing between them just using the ArrayList
        itemList.add((Item) baseNode);
        Node child = baseNode.firstChild;
        while (child != null) {
            getPreOrder(child);
            child = child.nextSibling;
        }
    }
}

