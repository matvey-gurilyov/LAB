import java.util.*;

/**
 * Challenge 6 - Min-Stack in O(1)
 *
 * A stack that supports push, pop, top, and get_min, all in O(1) time,
 * using an auxiliary stack that tracks the minimum-so-far at each depth.
 */
public class Challenge6MinStack {

    // Time: O(1) for every operation; Space: O(n) extra for the auxiliary stack
    static class MinStack {
        private final Deque<Integer> mainStack = new ArrayDeque<>();
        private final Deque<Integer> minStack = new ArrayDeque<>();

        public void push(int x) {
            mainStack.push(x);
            int currentMin = minStack.isEmpty() ? x : Math.min(x, minStack.peek());
            minStack.push(currentMin);
        }

        public void pop() {
            if (mainStack.isEmpty()) {
                throw new NoSuchElementException("pop() called on an empty stack");
            }
            mainStack.pop();
            minStack.pop(); // pop both stacks in lockstep
        }

        public int top() {
            if (mainStack.isEmpty()) {
                throw new NoSuchElementException("top() called on an empty stack");
            }
            return mainStack.peek();
        }

        public int getMin() {
            if (minStack.isEmpty()) {
                throw new NoSuchElementException("get_min() called on an empty stack");
            }
            return minStack.peek(); // single O(1) peek
        }

        public boolean isEmpty() {
            return mainStack.isEmpty();
        }
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Reduce auxiliary space by storing only the delta from the running
    // minimum instead of the minimum itself at every level. The auxiliary
    // stack still has O(n) entries in the worst case, but each entry can be
    // a smaller/cheaper value than a full duplicate minimum, and this
    // technique is the standard way to implement the trick with a single
    // stack of deltas instead of two parallel stacks of full values.
    // Time: O(1) for every operation; Space: O(n), one delta per element
    static class MinStackDelta {
        private final Deque<Long> deltaStack = new ArrayDeque<>();
        private long min;

        public void push(int x) {
            if (deltaStack.isEmpty()) {
                deltaStack.push(0L);
                min = x;
            } else {
                long delta = (long) x - min;
                deltaStack.push(delta);
                if (delta < 0) {
                    min = x; // new minimum
                }
            }
        }

        public void pop() {
            if (deltaStack.isEmpty()) {
                throw new NoSuchElementException("pop() called on an empty stack");
            }
            long delta = deltaStack.pop();
            if (delta < 0) {
                // the popped element was the minimum; recover the previous min
                min = min - delta;
            }
        }

        public int top() {
            if (deltaStack.isEmpty()) {
                throw new NoSuchElementException("top() called on an empty stack");
            }
            long delta = deltaStack.peek();
            if (delta < 0) {
                return (int) min;
            }
            return (int) (min + delta);
        }

        public int getMin() {
            if (deltaStack.isEmpty()) {
                throw new NoSuchElementException("get_min() called on an empty stack");
            }
            return (int) min;
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example from the problem statement
        MinStack s1 = new MinStack();
        s1.push(3);
        s1.push(5);
        s1.push(2);
        check(s1.getMin() == 2, "min after pushing 3,5,2 should be 2");
        s1.pop();
        check(s1.getMin() == 3, "min after popping 2 should revert to 3");
        System.out.println("Test 1 passed: worked example");

        // Test 2: pop/top/get_min on an empty stack raise errors
        MinStack s2 = new MinStack();
        boolean poppedEmpty = false;
        try {
            s2.pop();
        } catch (NoSuchElementException e) {
            poppedEmpty = true;
        }
        check(poppedEmpty, "pop on empty stack should raise an error");
        System.out.println("Test 2 passed: operations on empty stack raise errors");

        // Test 3: duplicate minimums and top() correctness
        MinStack s3 = new MinStack();
        s3.push(1);
        s3.push(1);
        s3.push(2);
        check(s3.getMin() == 1, "min with duplicate minimums should be 1");
        check(s3.top() == 2, "top should be the most recently pushed value");
        s3.pop();
        check(s3.getMin() == 1, "min should still be 1 after popping the top (non-min) value");
        s3.pop();
        check(s3.getMin() == 1, "min should still be 1 with one 1 remaining");
        System.out.println("Test 3 passed: duplicate minimums");

        // Test 4: stretch goal - delta-based MinStack behaves the same
        MinStackDelta s4 = new MinStackDelta();
        s4.push(3);
        s4.push(5);
        s4.push(2);
        check(s4.getMin() == 2, "delta-based min after pushing 3,5,2 should be 2");
        check(s4.top() == 2, "delta-based top should be 2");
        s4.pop();
        check(s4.getMin() == 3, "delta-based min after popping 2 should revert to 3");
        check(s4.top() == 5, "delta-based top after popping 2 should be 5");
        System.out.println("Test 4 passed: stretch goal delta-based MinStack");

        System.out.println("All Challenge 6 tests passed.");
    }
}
