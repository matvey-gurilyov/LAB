import java.util.*;

/**
 * Challenge 8 - Validate a Binary Search Tree
 *
 * Given the root of a binary tree, determine whether it is a valid BST by
 * threading (low, high) bounds through the recursion instead of only
 * comparing each node to its immediate children.
 *
 * Assumption: this implementation treats duplicate values as invalid (all
 * values in a valid BST are assumed distinct), matching the classic
 * definition of "left < node < right" with strict inequalities.
 */
public class Challenge8ValidateBST {

    static class Node {
        int val;
        Node left;
        Node right;
        Node(int val) {
            this.val = val;
        }
    }

    // Time: O(n), Space: O(h) recursion stack, h = tree height
    public static boolean isValidBST(Node root) {
        return isValidBST(root, null, null);
    }

    private static boolean isValidBST(Node node, Integer low, Integer high) {
        if (node == null) {
            return true; // an empty (sub)tree is trivially valid
        }
        if ((low != null && node.val <= low) || (high != null && node.val >= high)) {
            return false;
        }
        return isValidBST(node.left, low, node.val) && isValidBST(node.right, node.val, high);
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Rewrite the solution iteratively, using an explicit stack instead of
    // recursion.
    // Time: O(n), Space: O(h) for the explicit stack
    static class Frame {
        Node node;
        Integer low;
        Integer high;
        Frame(Node node, Integer low, Integer high) {
            this.node = node;
            this.low = low;
            this.high = high;
        }
    }

    public static boolean isValidBSTIterative(Node root) {
        Deque<Frame> stack = new ArrayDeque<>();
        stack.push(new Frame(root, null, null));

        while (!stack.isEmpty()) {
            Frame frame = stack.pop();
            Node node = frame.node;
            if (node == null) {
                continue;
            }
            if ((frame.low != null && node.val <= frame.low) ||
                (frame.high != null && node.val >= frame.high)) {
                return false;
            }
            stack.push(new Frame(node.left, frame.low, node.val));
            stack.push(new Frame(node.right, node.val, frame.high));
        }
        return true;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example - invalid tree from the problem statement
        //         5
        //        / \
        //       1   8
        //          / \
        //         4   9
        Node root1 = new Node(5);
        root1.left = new Node(1);
        root1.right = new Node(8);
        root1.right.left = new Node(4);
        root1.right.right = new Node(9);
        check(isValidBST(root1) == false, "node 4 violates the root's bound even though it's under 8");
        System.out.println("Test 1 passed: worked example (invalid tree)");

        // Test 2: empty tree and single node are valid
        check(isValidBST(null) == true, "empty tree should be valid");
        Node single = new Node(10);
        check(isValidBST(single) == true, "single node tree should be valid");
        System.out.println("Test 2 passed: empty tree and single node");

        // Test 3: a genuinely valid BST
        //         5
        //        / \
        //       3   8
        //      / \   \
        //     1   4   9
        Node root3 = new Node(5);
        root3.left = new Node(3);
        root3.right = new Node(8);
        root3.left.left = new Node(1);
        root3.left.right = new Node(4);
        root3.right.right = new Node(9);
        check(isValidBST(root3) == true, "properly ordered tree should be a valid BST");
        System.out.println("Test 3 passed: valid BST");

        // Test 4: stretch goal - iterative version matches recursive results
        check(isValidBSTIterative(root1) == false, "iterative version should detect the same invalid tree");
        check(isValidBSTIterative(root3) == true, "iterative version should validate the same valid tree");
        check(isValidBSTIterative(null) == true, "iterative version should treat empty tree as valid");
        System.out.println("Test 4 passed: stretch goal iterative version");

        System.out.println("All Challenge 8 tests passed.");
    }
}
