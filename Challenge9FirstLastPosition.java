/**
 * Challenge 9 - First & Last Position
 *
 * Given a sorted array of integers that may contain duplicates, and a target
 * value, find the first and last index at which target appears using two
 * modified binary searches. If target is not present, return (-1, -1).
 */
public class Challenge9FirstLastPosition {

    // Time: O(log n), Space: O(1)
    public static int[] searchRange(int[] nums, int target) {
        int first = findBound(nums, target, true);
        if (first == -1) {
            return new int[]{-1, -1}; // target not present at all
        }
        int last = findBound(nums, target, false);
        return new int[]{first, last};
    }

    // findFirst = true searches for the leftmost occurrence, false for the rightmost
    private static int findBound(int[] nums, int target, boolean findFirst) {
        int lo = 0;
        int hi = nums.length - 1;
        int result = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) {
                result = mid; // record this match
                if (findFirst) {
                    hi = mid - 1; // keep searching the left half for an earlier occurrence
                } else {
                    lo = mid + 1; // keep searching the right half for a later occurrence
                }
            } else if (nums[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return result;
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Generalize the solution to count the number of occurrences of target
    // without scanning every element between the first and last index.
    // Time: O(log n), Space: O(1)
    public static int countOccurrences(int[] nums, int target) {
        int first = findBound(nums, target, true);
        if (first == -1) {
            return 0;
        }
        int last = findBound(nums, target, false);
        return last - first + 1; // O(1) arithmetic, no scanning
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example from the problem statement
        int[] nums1 = {5, 7, 7, 8, 8, 8, 10};
        int[] result1 = searchRange(nums1, 8);
        check(result1[0] == 3 && result1[1] == 5, "target 8 should span indices [3, 5]");
        System.out.println("Test 1 passed: worked example");

        // Test 2: target not present in the array
        int[] result2 = searchRange(nums1, 6);
        check(result2[0] == -1 && result2[1] == -1, "absent target should return (-1, -1)");
        int[] result3 = searchRange(nums1, 11);
        check(result3[0] == -1 && result3[1] == -1, "target larger than all elements should return (-1, -1)");
        int[] result4 = searchRange(nums1, 4);
        check(result4[0] == -1 && result4[1] == -1, "target smaller than all elements should return (-1, -1)");
        System.out.println("Test 2 passed: target not present");

        // Test 3: target appears exactly once
        int[] result5 = searchRange(nums1, 10);
        check(result5[0] == 6 && result5[1] == 6, "target appearing once should return the same index twice");
        int[] result6 = searchRange(nums1, 5);
        check(result6[0] == 0 && result6[1] == 0, "target at the very start appearing once should be found");
        System.out.println("Test 3 passed: target appears exactly once");

        // Test 4: stretch goal - counting occurrences without scanning
        check(countOccurrences(nums1, 8) == 3, "target 8 occurs 3 times");
        check(countOccurrences(nums1, 7) == 2, "target 7 occurs 2 times");
        check(countOccurrences(nums1, 6) == 0, "absent target occurs 0 times");
        System.out.println("Test 4 passed: stretch goal occurrence counting");

        System.out.println("All Challenge 9 tests passed.");
    }
}
