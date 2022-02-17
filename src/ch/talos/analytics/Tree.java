package ch.talos.analytics;

import java.util.List;

interface Tree<T extends Comparable<T>> {

    boolean isEmpty();

    Tree<T> insert(T elem);

    Tree<T> remove(T elem);

    List<T> toList();

    Tree<T> search(String key);
}

class NonEmptyTree<T extends Comparable<T>> implements Tree<T> {
    public final T elem;
    public final NonEmptyTree<T> leftTree;
    public final NonEmptyTree<T> rightTree;

    public NonEmptyTree(NonEmptyTree<T> left, NonEmptyTree<T> right, T elem) {
        this.elem = elem;
        this.leftTree = left;
        this.rightTree =  right;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Tree<T> insert(T elem) {
        return null;
    }

    @Override
    public Tree<T> remove(T elem) {
        return null;
    }

    @Override
    public List<T> toList() {
        return null;
    }

    @Override
    public Tree<T> search(String key) {
        return null;
    }
}

class EmptyTree<T extends Comparable<T>> implements Tree<T> {

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Tree<T> insert(T elem) {
        return null;
    }

    @Override
    public Tree<T> remove(T elem) {
        return null;
    }

    @Override
    public List<T> toList() {
        return null;
    }

    @Override
    public Tree<T> search(String key) {
        return null;
    }
}

