/* Zach Barnhart
Helpful binary search tree methods
 */

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* First, the driver program */

public class BST {

    public static void main(String[] args) {

        BinaryTree<Integer> tree = new BinarySearchTree<Integer>();
        // The following fills out a Binary Tree to test our methods on
        tree.insert(40);
        tree.insert(20);
        tree.insert(50);
        tree.insert(10);
        tree.insert(30);
        tree.insert(25);
        tree.insert(32);
        tree.insert(60);
        tree.insert(31);
        tree.printNode(tree.root);

        System.out.println("We generated the following binary tree:");
        tree.printNode(tree.root);
        System.out.println("We will now test our number of nodes method. We inserted 9 nodes in our tree, so this method should return 9:");
        System.out.println(tree.numNodes());
        System.out.println("We will now test our leaf-counting method. Our tree currently has 4 leaves, so this method should return 4:");
        System.out.println(tree.numLeaves());
        System.out.println("We will now test our internal node-counting method. Our tree currently has 5 internal nodes, so this method should return 5:");
        System.out.println(tree.numInternal());
        System.out.println("For a binary tree, the number of arcs is simply the number of nodes minus 1. Our method was implemented in this way, and should return 8:");
        System.out.println(tree.numArcs());
        System.out.println("Finally, let us count the depth of a couple of nodes, starting with the node 31 (should return 4)");
        System.out.println(tree.depth(31));
        System.out.println("And now the depth of 50 (should be 1)");
        System.out.println(tree.depth(50));
        System.out.println();
        System.out.println();

        tree.remove(40);
        tree.remove(25);
        System.out.println("We will now delete the root node and 25 node in our tree, resulting in the following tree:");
        System.out.println("       32");
        System.out.println("      /  \\");
        System.out.println("    20    50");
        System.out.println("   /  \\     \\ ");
        System.out.println(" 15    30    60 ");
        System.out.println("         \\");
        System.out.println("          31");
        System.out.println("The tree now has 7 nodes, 3 leaves, 4 internal nodes, and 6 arcs. Let us call these methods in order to check:");
        System.out.println(tree.numNodes());
        System.out.println(tree.numLeaves());
        System.out.println(tree.numInternal());
        System.out.println(tree.numArcs());
        System.out.println("Now we compute the depth of 31 (should be 3) and 60 (should be 2):");
        System.out.println(tree.depth(31));
        System.out.println(tree.depth(60));

    }
}

/* NOW THE BINARY TREE CLASS WHICH CONTAINS ALL THE METHODS FOR THIS ASSIGNMENT */

abstract class BinaryTree<E extends Comparable<? super E>> implements Tree<E> {

    protected class Node<T> {
        protected Node(T data) {
            this.data = data;
        }
        protected T data;
        protected Node<T> left;
        protected Node<T> right;
        protected Node<T> parent;
    }

    protected Node<E> root;

    public int depth(E key) {
        int count = 0;
        return depth(key, root, count);
    }

    public int depth(E key, Node<E> node, int count) {

        int cmp = key.compareTo(node.data);
        if (cmp == 0) {
            return count;
        } else if (cmp < 0) {
            return depth(key, node.left, count + 1);
        } else {
            return depth(key, node.right, count + 1);
        }
    }

    public <E extends Comparable<?>> void printNode(Node<E> root) {
        int maxLevel = this.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private <E extends Comparable<?>> void printNodeInternal(List<Node<E>> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || this.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BinaryTree.printWhitespaces(firstSpaces);

        List<Node<E>> newNodes = new ArrayList<Node<E>>();
        for (Node<E> node : nodes) {
            if (node != null) {
                System.out.print(node.data);
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            BinaryTree.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                BinaryTree.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    BinaryTree.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).left != null)
                    System.out.print("/");
                else
                    BinaryTree.printWhitespaces(1);

                BinaryTree.printWhitespaces(i + i - 1);

                if (nodes.get(j).right != null)
                    System.out.print("\\");
                else
                    BinaryTree.printWhitespaces(1);

                BinaryTree.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private <E extends Comparable<?>> int maxLevel(Node<E> node) {
        if (node == null)
            return 0;

        return Math.max(this.maxLevel(node.left), this.maxLevel(node.right)) + 1;
    }

    private <E> boolean isAllElementsNull(List<E> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }
    
    void print2DUtil(Node<E> root, int space)
    {
        // Base case

        int COUNT = 5;

        if (root == null)
            return;

        // Increase distance between levels
        space += COUNT;

        // Process right child first
        print2DUtil(root.right, space);

        // Print current node after space
        // count
        // System.out.print("\n");
        for (int i = COUNT; i < space; i++)
            System.out.print(" ");
        System.out.print(root.data + "\n");

        // Process left child
        print2DUtil(root.left, space);
    }

    // Wrapper over print2DUtil()
    void print2D(Node<E> root)
    {
        // Pass initial space count as 0
        print2DUtil(root, 0);
    }

    public int numArcs() {
        return numNodes() - 1;
    }

    public int numInternal() {
        // calls private internal node counter using root node as base
        return numInternal(this.root);
    }

    private int numInternal(Node<E> testnode) {
        //first test to see if node itself is null or is a leaf. this will cause method call to terminate
        if (testnode == null || (testnode.left == null && testnode.right == null)) {
            return 0;
        } else {
            // add one for current node add internal nodes in subtrees via recursive calls
            return 1 + numInternal(testnode.left) + numInternal(testnode.right);
        }
    }

    int numLeaves() {
        return numLeaves(this.root); //call private numLeaves method using root as base to get total leaves
    }

    int numLeaves(Node<E> testnode) {
        if (testnode == null) { //in case method is called for null node
            return 0; //add zero in this case to leaf total
        }
        if (testnode.left == null && testnode.right == null) { // test if node is a leaf
            return 1; //if so, add one to total
        } else {
            return numLeaves(testnode.left) + numLeaves(testnode.right); // if not leaf, recursive call method for child nodes
        }
    }

    public int numNodes() {
        return numNodes(this.root); //calls private numNodes method using root as base to get total nodes
    }

    private int numNodes(Node<E> testnode) {
        if (testnode == null) //if node is null
            return 0; //add zero to total
        else
            return(numNodes(testnode.left) + numNodes(testnode.right) + 1); //total nodes contributed by current subtree equals current node plus all nodes contributed by subtrees with children as root
    }
}

interface Tree<E> extends Iterable<E> {
    void insert(E data);
    void remove(E key);
}

class BinarySearchTree<E extends Comparable<? super E>> extends BinaryTree<E> {

    public void insert(E data) {
        Node<E> temp = new Node<E>(data);
        root = insert(root, temp);
    }

    private Node<E> insert(Node<E> curr, Node<E> node) {

        int cmp;
        if (curr == null) {
            curr = node;
        } else {
            cmp = curr.data.compareTo(node.data);
            if (cmp <= 0) {
                curr.right = insert(curr.right,node);
            } else {
                curr.left = insert(curr.left, node);
            }
        }
        return curr;
    }

    public Iterator<E> iterator() {
        return null;
    }

    private Node<E> findIOP(Node<E> node) {

        Node<E> curr;

        for (curr = node.left; curr.right != null; curr = curr.right);

        return curr;
    }

    public void remove(E key) {
        root = remove(root, key);
    }

    private Node<E> remove(Node<E> curr, E key) {

        int cmp;

        if (curr == null) {
            return null;
        } else if((cmp = key.compareTo(curr.data)) ==0) {
            if (curr.left == null || curr.right == null) {
                curr = curr.left != null ? curr.left : curr.right;
            } else {
                Node<E> iop = findIOP(curr);
                E temp = curr.data;
                curr.data = iop.data;
                iop.data = temp;
                Node<E> node = curr;
                if (node.left.data.compareTo(key) == 0) {
                    curr.left = curr.left.left;
                }
                else {
                    for (node = node.left; node.right != iop; node = node.right);
                    node.right = iop.left;
                }
            }
        } else {
            Node <E> node = curr;
            if ((cmp = key.compareTo(node.data)) < 0) {
                curr.left = remove(curr.left,key);
            } else if (cmp > 0) {
                curr.right = remove(curr.right,key);
            }
        }
        return curr;
    }
}