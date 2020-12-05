package utils;

import java.util.LinkedList;
import java.util.Objects;

public class Node {

    private String itemName;
    private int ID;
    private Node parent;
    private LinkedList<Node> children;

    public Node(String name, int ID) {
        this.itemName = name;
        this.ID = ID;
        this.children = new LinkedList<>();
    }

    public int getID() {
        return ID;
    }

    public LinkedList<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


    public void addChild(Node node) {
        node.setParent(this);
        this.children.add(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Node)) return false;
        Node node = (Node) o;
        return Objects.equals(itemName, node.itemName) &&
                Objects.equals(parent, node.parent) &&
                Objects.equals(children, node.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, parent, children);
    }

    public Node getChild(int id) {
        for (Node n : this.children) {
            if (n.getID() == id) {
                return n;
            }
        }

        return null;
    }

    public void removeChild(Node node) {
        this.children.remove(node);
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

    public boolean hasChild() {
        return this.children.size() > 0;
    }

    @Override
    public String toString() {
        return this.itemName + "(" + this.ID + ")";
    }
}

