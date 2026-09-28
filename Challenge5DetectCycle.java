/**
 * Challenge 5 - Detect a Cycle
 *
 * Given the head of a linked list, determine whether it contains a cycle,
 * using Floyd's cycle detection (the tortoise and the hare) instead of any
 * extra data structure.
 */
public class Challenge5DetectCycle {

    static class Node {
        int val;
        Node next;
        Node(int val) {
            this.val = val;
        }
    }

    // Time: O(n), Space: O(1)
    public static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false; // fast (or fast.next) reached null: no cycle
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Once a cycle is detected, return the exact node where it begins.
    // Standard result: after slow and fast meet inside the cycle, resetting
    // one pointer to head and advancing both one step at a time causes them
    // to meet exactly at the cycle's starting node.
    // Time: O(n), Space: O(1)
    public static Node detectCycleStart(Node head) {
        Node slow = head;
        Node fast = head;
        boolean cycleFound = false;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                cycleFound = true;
                break;
            }
        }

        if (!cycleFound) {
            return null; // no cycle
        }

        Node pointer1 = head;
        Node pointer2 = slow;
        while (pointer1 != pointer2) {
            pointer1 = pointer1.next;
            pointer2 = pointer2.next;
        }
        return pointer1; // the node where the cycle begins
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example - a list with a cycle back into itself
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n5.next = n3; // cycle back to node 3
        check(hasCycle(n1) == true, "list with a cycle should be detected");
        System.out.println("Test 1 passed: worked example with a cycle");

        // Test 2: empty list and single-node list (with and without self-loop)
        check(hasCycle(null) == false, "empty list should have no cycle");
        Node single = new Node(1);
        check(hasCycle(single) == false, "single node without self-loop should have no cycle");
        Node selfLoop = new Node(1);
        selfLoop.next = selfLoop;
        check(hasCycle(selfLoop) == true, "single node with self-loop should be detected as a cycle");
        System.out.println("Test 2 passed: empty list and single-node list cases");

        // Test 3: acyclic list of several nodes
        Node a1 = new Node(1);
        Node a2 = new Node(2);
        Node a3 = new Node(3);
        a1.next = a2;
        a2.next = a3;
        a3.next = null;
        check(hasCycle(a1) == false, "acyclic list should not be detected as having a cycle");
        System.out.println("Test 3 passed: acyclic list");

        // Test 4: stretch goal - find the exact start of the cycle
        Node b1 = new Node(1);
        Node b2 = new Node(2);
        Node b3 = new Node(3);
        Node b4 = new Node(4);
        Node b5 = new Node(5);
        b1.next = b2;
        b2.next = b3;
        b3.next = b4;
        b4.next = b5;
        b5.next = b3; // cycle begins at node b3
        check(detectCycleStart(b1) == b3, "cycle start should be correctly identified as node b3");
        check(detectCycleStart(a1) == null, "acyclic list should return null for cycle start");
        System.out.println("Test 4 passed: stretch goal cycle start detection");

        System.out.println("All Challenge 5 tests passed.");
    }
}
