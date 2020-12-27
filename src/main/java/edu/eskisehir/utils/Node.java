package edu.eskisehir.utils;

import edu.eskisehir.inventory.Item;

public class Node<T> { // our generic Node design

    public Node<T> nextSibling; // access modifiers both nextSibling and firstChild are set public to make things easier
    public Node<T> firstChild;
    String name;
    int ID;

    public Node(String name, int ID) {
        this.firstChild = null;
        this.nextSibling = null;
        this.name = name;
        this.ID = ID;
    }

    public boolean hasChild() {
        return firstChild != null;
    } // to check Node has child

    public String toString() {
        return this.name + "(" + this.ID + ")";
    } // used in Interface

    public void addChild(Node<T> n) { // we are using this when we are defining relationships between nodes and adding nodes into Tree
        if (!this.hasChild()) {
            this.firstChild = n;
            return;
        }
        Node<T> sibling = this.firstChild.nextSibling;
        Node<T> prevSibling = this.firstChild;
        if (sibling == null) {
            prevSibling.nextSibling = n;
            return;
        }
        while (sibling != null) {
            prevSibling = prevSibling.nextSibling;
            sibling = sibling.nextSibling;
        }
        prevSibling.nextSibling = n;
    }

    public int getID() {
        return this.ID;
    } // used in Interface

    public void addDemand(int week, int amount) { // to access easily addDemand method from Node class
        Item item = (Item) this;
        item.addDemand(week, amount);
    }

    public void initializeVariables() { // to access easily initializeVariables method from Node class
        Item item = (Item) this;
        item.initializeVariables();
    }

    public void produce() { // to access easily produce method from Node class
        Item item = (Item) this;
        item.produce();
    }


}

