package com.bobocode.cs;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * {@link RecursiveBinarySearchTree} is an implementation of a {@link BinarySearchTree} that is based on a linked nodes
 * and recursion. A tree node is represented as a nested class {@link Node}. It holds an element (a value) and
 * two references to the left and right child nodes.
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com/learn">visit our website</a></strong>
 * <p>
 *
 * @param <T> a type of elements that are stored in the tree
 * @author Taras Boychuk
 * @author Maksym Stasiuk
 */
public class RecursiveBinarySearchTree<T extends Comparable<T>> implements BinarySearchTree<T> {
    private static class Node<T> {
        T element;
        Node<T> left;
        Node<T> right;

        public Node(T element) {
            this.element = element;
        }
    }

    private Node<T> root;
    private int size;

    @SafeVarargs
    public static <T extends Comparable<T>> RecursiveBinarySearchTree<T> of(T... elements) {
        var bst = new RecursiveBinarySearchTree<T>();
        Arrays.stream(elements)
                .forEach(bst::insert);
        return bst;
    }

    @Override
    public boolean insert(T element) {
        return Optional.ofNullable(root)
                .map(x -> insert(element, x))
                .orElseGet(() -> {
                    root = new Node<>(element);
                    size++;
                    return true;
                });
    }

    public boolean insert(T element, Node<T> root) {
        if (element.compareTo(root.element) < 0) {
            return Optional.of(root)
                    .map(node -> node.left)
                    .map(node -> insert(element, node))
                    .orElseGet(() -> {
                        root.left = new Node<>(element);
                        size++;
                        return true;
                    });
        } else if (element.compareTo(root.element) > 0) {
            return Optional.of(root)
                    .map(node -> node.right)
                    .map(node -> insert(element, node))
                    .orElseGet(() -> {
                        root.right = new Node<>(element);
                        size++;
                        return true;
                    });
        } else {
            return false;
        }
    }

    @Override
    public boolean contains(T element) {
        return contains(element, root);
    }

    private boolean contains(T element, Node<T> root) {
        if (element.compareTo(root.element) < 0) {
            return Optional.of(root)
                    .map(node -> node.left)
                    .map(node -> contains(element, node))
                    .orElse(false);
        } else if (element.compareTo(root.element) > 0) {
            return Optional.of(root)
                    .map(node -> node.right)
                    .map(node -> contains(element, node))
                    .orElse(false);
        } else {
            return true;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        return Optional.ofNullable(root)
                .map(node -> depth(node) - 1)
                .orElse(0);
    }

    private int depth(Node<T> root) {
        return Optional.ofNullable(root)
                .map(node -> 1 + Math.max(depth(node.left), depth(node.right)))
                .orElse(0);
    }

    @Override
    public void inOrderTraversal(Consumer<T> consumer) {
        inOrderTraversal(consumer, root);
    }

    private void inOrderTraversal(Consumer<T> consumer, Node<T> root) {
        Optional.ofNullable(root)
                .ifPresent(node -> {
                    inOrderTraversal(consumer, node.left);
                    consumer.accept(node.element);
                    inOrderTraversal(consumer, node.right);
                });

    }
}
