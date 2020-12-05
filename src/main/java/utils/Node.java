package utils;

import java.util.Objects;

public class Node {

    public Node nextSibling;
    Node parent;
    Node firstChild;
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
            return;
        }
        Node sibling = this.firstChild.nextSibling;
        Node prevSibling = this.firstChild;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return ID == node.ID &&
                Objects.equals(parent, node.parent) &&
                Objects.equals(firstChild, node.firstChild) &&
                Objects.equals(nextSibling, node.nextSibling) &&
                Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, firstChild, nextSibling, name, ID);
    }
}

