import java.util.*;

/**
 * Challenge 3 - Reverse a Linked List
 *
 * Given the head of a singly linked list, reverse the direction of every
 * link in place so that the last node becomes the new head. Return the new
 * head.
 */
public class Challenge3ReverseLinkedList {

    static class Node {
        int val;
        Node next;
        Node(int val) {
            this.val = val;
        }
    }

    // Time: O(n), Space: O(1)
    public static Node reverseListIterative(Node head) {
        Node prev = null;
        Node curr = head;
        while (curr != null) {
            Node next = curr.next; // save curr.next before overwriting it
            curr.next = prev;      // flip curr.next to point back at prev
            prev = curr;           // advance prev
            curr = next;           // advance curr
        }
        return prev; // once curr is null, prev is the new head
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // A recursive version. This uses O(n) space instead of O(1) because each
    // recursive call adds a new frame to the call stack, and all n frames
    // must be kept alive simultaneously (one per node) until the base case
    // is reached and the stack unwinds -- unlike the iterative version, which
    // only ever needs three pointer variables regardless of list length.
    // Time: O(n), Space: O(n) (recursion stack)
    public static Node reverseListRecursive(Node head) {
        if (head == null || head.next == null) {
            return head; // empty list or single node: already "reversed"
        }
        Node newHead = reverseListRecursive(head.next);
        head.next.next = head; // make the next node point back at this one
        head.next = null;      // detach this node's old forward link
        return newHead;
    }

    // Helper: build a linked list from an array of values
    private static Node buildList(int[] values) {
        Node dummy = new Node(0);
        Node curr = dummy;
        for (int v : values) {
            curr.next = new Node(v);
            curr = curr.next;
        }
        return dummy.next;
    }

    // Helper: convert a linked list to an array for easy comparison
    private static int[] toArray(Node head) {
        List<Integer> values = new ArrayList<>();
        while (head != null) {
            values.add(head.val);
            head = head.next;
        }
        int[] arr = new int[values.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = values.get(i);
        }
        return arr;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example from the problem statement (iterative)
        Node list1 = buildList(new int[]{1, 2, 3});
        Node reversed1 = reverseListIterative(list1);
        check(Arrays.equals(toArray(reversed1), new int[]{3, 2, 1}),
                "1->2->3 should reverse to 3->2->1");
        System.out.println("Test 1 passed: worked example (iterative)");

        // Test 2: empty list and single-node list
        Node emptyList = reverseListIterative(null);
        check(emptyList == null, "reversing an empty list should return null");
        Node singleNode = buildList(new int[]{42});
        Node reversedSingle = reverseListIterative(singleNode);
        check(Arrays.equals(toArray(reversedSingle), new int[]{42}),
                "reversing a single-node list should return the same single node");
        System.out.println("Test 2 passed: empty list and single-node list");

        // Test 3: longer list (iterative)
        Node list3 = buildList(new int[]{1, 2, 3, 4, 5});
        Node reversed3 = reverseListIterative(list3);
        check(Arrays.equals(toArray(reversed3), new int[]{5, 4, 3, 2, 1}),
                "longer list should fully reverse");
        System.out.println("Test 3 passed: longer list (iterative)");

        // Test 4: stretch goal - recursive version matches iterative behavior
        Node list4 = buildList(new int[]{1, 2, 3, 4, 5});
        Node reversed4 = reverseListRecursive(list4);
        check(Arrays.equals(toArray(reversed4), new int[]{5, 4, 3, 2, 1}),
                "recursive reversal should match iterative reversal");
        check(reverseListRecursive(null) == null, "recursive reversal of empty list should return null");
        Node singleNodeRec = buildList(new int[]{7});
        check(Arrays.equals(toArray(reverseListRecursive(singleNodeRec)), new int[]{7}),
                "recursive reversal of single-node list should return the same node");
        System.out.println("Test 4 passed: stretch goal recursive version");

        System.out.println("All Challenge 3 tests passed.");
    }
}
