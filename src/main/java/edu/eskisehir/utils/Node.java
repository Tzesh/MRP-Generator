package edu.eskisehir.utils;

import edu.eskisehir.inventory.Item;

public class Node<T> {

    public Node<T> nextSibling;
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
    }

    public String toString() {
        return this.name + "(" + this.ID + ")";
    }

    public Node<T> getChild(int ID) {
        Node<T> sibling = this.firstChild;
        while (sibling.nextSibling != null) {
            if (sibling.getID() == ID) {
                return sibling;
            }
            sibling = sibling.nextSibling;
        }

        return null;
    }

    public void addChild(Node<T> n) {
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
    }

    public void addDemand(int week, int amount) {
        Item item = (Item) this;
        item.addDemand(week, amount);
    }

    public void initializeVariables() {
        Item item = (Item) this;
        item.initializeVariables();
    }

    public void produce() {
        Item item = (Item) this;
        item.produce();
    }


}

