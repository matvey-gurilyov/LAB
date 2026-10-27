import java.util.*;

/**
 * Challenge 7 - Implement a Dynamic Array
 *
 * A growable array built on top of a fixed-size backing array, growing by
 * doubling capacity when full.
 */
public class Challenge7DynamicArray {

    // Time: append O(1) amortized (O(n) worst case on resize); get/set O(1)
    // Space: O(n)
    static class DynamicArray {
        private int[] backing;
        private int size;
        private int capacity;

        public DynamicArray() {
            this.capacity = 2; // small initial capacity to make growth easy to test
            this.backing = new int[capacity];
            this.size = 0;
        }

        public int size() {
            return size;
        }

        public void append(int x) {
            if (size == capacity) {
                grow();
            }
            backing[size] = x;
            size++;
        }

        private void grow() {
            int newCapacity = Math.max(1, capacity * 2);
            int[] newBacking = new int[newCapacity];
            for (int i = 0; i < size; i++) {
                newBacking[i] = backing[i];
            }
            backing = newBacking;
            capacity = newCapacity;
        }

        public int get(int i) {
            if (i < 0 || i >= size) {
                throw new IndexOutOfBoundsException("Index " + i + " out of range [0, " + size + ")");
            }
            return backing[i];
        }

        public void set(int i, int x) {
            if (i < 0 || i >= size) {
                throw new IndexOutOfBoundsException("Index " + i + " out of range [0, " + size + ")");
            }
            backing[i] = x;
        }

        // ---------------- STRETCH GOAL (OPTIONAL) ----------------
        // Add remove(i), shifting later elements down by one, and shrink the
        // backing array's capacity once usage drops below 25%.
        // Time: O(n) for remove (shifting); Space: O(n)
        public void remove(int i) {
            if (i < 0 || i >= size) {
                throw new IndexOutOfBoundsException("Index " + i + " out of range [0, " + size + ")");
            }
            for (int j = i; j < size - 1; j++) {
                backing[j] = backing[j + 1];
            }
            size--;
            // shrink if usage drops below 25% of capacity, but never below
            // the small initial capacity of 2
            if (capacity > 2 && size < capacity / 4) {
                int newCapacity = Math.max(2, capacity / 2);
                int[] newBacking = new int[newCapacity];
                for (int k = 0; k < size; k++) {
                    newBacking[k] = backing[k];
                }
                backing = newBacking;
                capacity = newCapacity;
            }
        }

        public int getCapacity() {
            return capacity;
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example - appending causes capacity to double
        DynamicArray d1 = new DynamicArray(); // capacity starts at 2
        d1.append(10);
        d1.append(20);
        check(d1.getCapacity() == 2, "capacity should still be 2 after filling it exactly");
        d1.append(30); // triggers growth: capacity should double to 4
        check(d1.getCapacity() == 4, "capacity should double to 4 after exceeding initial capacity");
        check(d1.size() == 3, "size should be 3 after three appends");
        check(d1.get(0) == 10 && d1.get(1) == 20 && d1.get(2) == 30, "existing items should be preserved after growth");
        System.out.println("Test 1 passed: worked example doubling on growth");

        // Test 2: get/set with valid and out-of-range indices
        d1.set(1, 99);
        check(d1.get(1) == 99, "set should update the value at the given index");
        boolean threwGet = false;
        try {
            d1.get(10);
        } catch (IndexOutOfBoundsException e) {
            threwGet = true;
        }
        check(threwGet, "get with out-of-range index should raise an error");
        boolean threwSet = false;
        try {
            d1.set(-1, 5);
        } catch (IndexOutOfBoundsException e) {
            threwSet = true;
        }
        check(threwSet, "set with negative index should raise an error");
        System.out.println("Test 2 passed: get/set bounds checking");

        // Test 3: many appends, verifying all data is retained correctly
        DynamicArray d2 = new DynamicArray();
        for (int i = 0; i < 20; i++) {
            d2.append(i * i);
        }
        check(d2.size() == 20, "size should be 20 after 20 appends");
        boolean allCorrect = true;
        for (int i = 0; i < 20; i++) {
            if (d2.get(i) != i * i) {
                allCorrect = false;
                break;
            }
        }
        check(allCorrect, "all appended values should be retrievable in order");
        System.out.println("Test 3 passed: many appends retain correct data");

        // Test 4: stretch goal - remove() shifts elements and shrinks capacity
        DynamicArray d3 = new DynamicArray();
        for (int i = 0; i < 8; i++) {
            d3.append(i); // grows capacity to 8
        }
        check(d3.getCapacity() == 8, "capacity should be 8 after 8 appends from initial 2");
        d3.remove(0); // remove value 0, shifting everything down
        check(d3.get(0) == 1, "after removing index 0, the new index 0 should hold the old index 1's value");
        check(d3.size() == 7, "size should be 7 after one removal");
        // remove enough elements to drop usage below 25% of capacity 8 (i.e. below 2)
        d3.remove(0);
        d3.remove(0);
        d3.remove(0);
        d3.remove(0);
        d3.remove(0); // size now 2, which is not below 25% of 8 (2 is not < 2)
        check(d3.size() == 2, "size should be 2 after five more removals");
        d3.remove(0); // size now 1, which is below 25% of 8 -> should shrink
        check(d3.size() == 1, "size should be 1 after the sixth removal");
        check(d3.getCapacity() < 8, "capacity should shrink once usage drops below 25%");
        System.out.println("Test 4 passed: stretch goal remove() with shrinking");

        System.out.println("All Challenge 7 tests passed.");
    }
}
