package com.github.davityle.ngprocessor.xml;

import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by tyler on 7/22/15.
 */
public class NodeCollection implements Collection<Node> {

    private final Nodes nodeList;

    public NodeCollection(Nodes nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public int size() {
        return nodeList.getLength();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Node node : this) {
            if (node.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator(nodeList);
    }

    @Override
    public Node[] toArray() {
        Node[] nodes = new Node[size()];
        for(int i = 0; i < nodes.length; i++) {
            nodes[i] = nodeList.item(i);
        }
        return nodes;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a.length <= size()){
            for(int i = 0; i < size(); i++) {
                a[i] = (T) nodeList.item(i);
            }
            return a;
        }
        return (T[]) toArray();
    }

    @Override
    public boolean add(Node node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object o : c) {
            if(!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof NodeCollection))
            return false;
        NodeCollection collection = (NodeCollection) o;
        if(collection.size() != size())
            return false;
        for(int i = 0; i < size(); i++){
            if(!nodeList.item(i).equals(collection.nodeList.item(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int code = 1;
        for(Node n : this){
            code *= n.hashCode() * 13;
        }
        return code;
    }

    public static class NodeIterator implements Iterator<Node> {

        private final Nodes nodeList;
        private int place = -1;

        public NodeIterator(Nodes nodeList) {
            this.nodeList = nodeList;
        }

        @Override
        public boolean hasNext() {
            return nodeList.getLength() < place - 1;
        }

        @Override
        public Node next() {
            place++;
            return nodeList.item(place);
        }

        @Override
        public void remove() { throw new UnsupportedOperationException(); }
    }

    public interface Nodes {
        Node item(int i);
        int getLength();
    }
}
