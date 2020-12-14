package edu.eskisehir.utils;

import edu.eskisehir.inventory.Item;

import java.util.Objects;

public class Node<T> {

    public Node<T> nextSibling;
    Node<T> firstChild;
    String name;
    int ID;

    public Node(String name, int ID) {
        this.firstChild = null;
        this.nextSibling = null;
        this.name = name;
        this.ID = ID;
    }

    public Node<T> getFirstChild() {
        return firstChild;
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
        if (this instanceof Item) {
            Item item = (Item)this;
            item.addDemand(week, amount);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return ID == node.ID &&
                Objects.equals(firstChild, node.firstChild) &&
                Objects.equals(nextSibling, node.nextSibling) &&
                Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstChild, nextSibling, name, ID);
    }
}

