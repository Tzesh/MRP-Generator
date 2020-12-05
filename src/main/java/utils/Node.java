package utils;

import java.util.LinkedList;
import java.util.Objects;

public class Node {

    Node parent;
    Node firstChild;
    Node nextSibling;
    String name;
    int ID;

    public Node(String name, int ID) {
        this.parent = null;
        this.firstChild = null;
        this.nextSibling = null;
        this.name = name;
        this.ID = ID;
    }

    public Node getFirstChild() {
        return firstChild;
    }

    public boolean hasChild() {
        return firstChild != null;
    }

    public String toString() {
        return this.name + "(" + this.ID + ")";
    }

    public int findDepth() {
        int counter = 0;
        Node parent = this.parent;
        while (parent != null) {
            counter++;
            parent = parent.getParent();
        }

        return counter;
    }

    public Node getParent() {
        return this.parent;
    }

    public Node getChild(int ID) {
        Node sibling = this.firstChild;
        while (sibling.nextSibling != null) {
            if (sibling.getID() == ID) {
                return sibling;
            }
            sibling = sibling.nextSibling;
        }

        return null;
    }

    public void addChild(Node n) {
        if (!this.hasChild()) {
            this.firstChild = n;
        } else {
            Node sibling = this.firstChild;
            while (sibling.nextSibling != null) {
                sibling = sibling.nextSibling;
            }

            sibling.nextSibling = n;
        }
    }

    public int getID() {
        return this.ID;
    }

}

