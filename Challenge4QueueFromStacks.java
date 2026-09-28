import java.util.*;

/**
 * Challenge 4 - Queue Using Two Stacks
 *
 * Implement a FIFO queue using only two LIFO stacks as internal storage.
 */
public class Challenge4QueueFromStacks {

    // Time: enqueue O(1), dequeue amortized O(1) (worst case O(n)); Space: O(n)
    static class QueueFromStacks<T> {
        private final Deque<T> in = new ArrayDeque<>();
        private final Deque<T> out = new ArrayDeque<>();

        public void enqueue(T x) {
            in.push(x); // always O(1)
        }

        public T dequeue() {
            if (out.isEmpty()) {
                // pour every element of `in` into `out`, reversing their order
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
            if (out.isEmpty()) {
                throw new NoSuchElementException("dequeue() called on an empty queue");
            }
            return out.pop();
        }

        public boolean isEmpty() {
            return in.isEmpty() && out.isEmpty();
        }
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Proof of the amortized O(1) bound using a potential-function argument:
    //
    // Define the potential function Phi = 2 * size(in), i.e. twice the number
    // of elements currently sitting in the `in` stack.
    //
    // enqueue(x): does one push (real cost 1) and increases size(in) by 1,
    // so Phi increases by 2. Amortized cost = real cost + change in Phi
    // = 1 + 2 = 3 = O(1).
    //
    // dequeue() when `out` is non-empty: real cost is 1 pop, and Phi does not
    // change (size(in) is untouched). Amortized cost = 1 + 0 = O(1).
    //
    // dequeue() when `out` is empty and `in` has k elements: real cost is
    // k pops from `in` + k pushes onto `out` + 1 final pop = 2k + 1. This
    // drains `in` to size 0, so Phi drops from 2k to 0, a change of -2k.
    // Amortized cost = (2k + 1) + (-2k) = 1 = O(1).
    //
    // Since every operation has O(1) amortized cost under this potential
    // function, and Phi is always >= 0 (starting at 0 with an empty queue),
    // the total real cost of any sequence of n operations is O(n), i.e.
    // each operation is O(1) amortized.

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example from the problem statement
        QueueFromStacks<Integer> q1 = new QueueFromStacks<>();
        q1.enqueue(1);
        q1.enqueue(2);
        q1.enqueue(3);
        check(q1.dequeue() == 1, "first dequeue should return 1 (FIFO order)");
        check(q1.dequeue() == 2, "second dequeue should return 2");
        check(q1.dequeue() == 3, "third dequeue should return 3");
        System.out.println("Test 1 passed: worked example FIFO order");

        // Test 2: dequeue on an empty queue raises an error
        QueueFromStacks<Integer> q2 = new QueueFromStacks<>();
        boolean threw = false;
        try {
            q2.dequeue();
        } catch (NoSuchElementException e) {
            threw = true;
        }
        check(threw, "dequeue on empty queue should raise NoSuchElementException");
        System.out.println("Test 2 passed: dequeue on empty queue raises an error");

        // Test 3: interleaved enqueue/dequeue operations
        QueueFromStacks<String> q3 = new QueueFromStacks<>();
        q3.enqueue("a");
        q3.enqueue("b");
        check(q3.dequeue().equals("a"), "interleaved: first dequeue should be 'a'");
        q3.enqueue("c");
        check(q3.dequeue().equals("b"), "interleaved: second dequeue should be 'b'");
        check(q3.dequeue().equals("c"), "interleaved: third dequeue should be 'c'");
        check(q3.isEmpty(), "queue should be empty after all elements dequeued");
        System.out.println("Test 3 passed: interleaved enqueue/dequeue operations");

        System.out.println("All Challenge 4 tests passed.");
    }
}
