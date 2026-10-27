import java.util.*;

/**
 * Challenge 2 - Two Sum via Hash Map
 *
 * Given an array of integers nums and an integer target, return the indices
 * of the two numbers that add up to target, in a single pass using a hash map.
 */
public class Challenge2TwoSum {

    // Time: O(n), Space: O(n)
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> valueToIndex = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (valueToIndex.containsKey(complement)) {
                return new int[]{valueToIndex.get(complement), i};
            }
            valueToIndex.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution exists for the given input.");
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Extend the solution to return every pair of indices that sums to
    // target, correctly handling duplicate values in nums.
    // Time: O(n), Space: O(n)
    public static List<int[]> allTwoSumPairs(int[] nums, int target) {
        // Map from value -> list of indices where that value occurs (in order seen)
        Map<Integer, List<Integer>> valueToIndices = new HashMap<>();
        List<int[]> pairs = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            List<Integer> complementIndices = valueToIndices.get(complement);
            if (complementIndices != null) {
                for (int earlierIndex : complementIndices) {
                    pairs.add(new int[]{earlierIndex, i});
                }
            }
            valueToIndices.computeIfAbsent(nums[i], k -> new ArrayList<>()).add(i);
        }
        return pairs;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked example from the problem statement
        int[] result1 = twoSum(new int[]{2, 7, 11, 15}, 9);
        check(Arrays.equals(result1, new int[]{0, 1}), "worked example should return (0, 1)");
        System.out.println("Test 1 passed: worked example");

        // Test 2: negative numbers and zero
        int[] result2 = twoSum(new int[]{-3, 4, 3, 90}, 0);
        check(Arrays.equals(result2, new int[]{0, 2}), "negative numbers and zero should be handled");
        int[] result3 = twoSum(new int[]{0, 4, 3, 0}, 0);
        check(Arrays.equals(result3, new int[]{0, 3}), "two zeros should sum to zero correctly");
        System.out.println("Test 2 passed: negative numbers and zero");

        // Test 3: pair not at the start of the array
        int[] result4 = twoSum(new int[]{3, 2, 4}, 6);
        check(Arrays.equals(result4, new int[]{1, 2}), "pair in the middle/end of array should be found");
        System.out.println("Test 3 passed: pair located later in the array");

        // Test 4: stretch goal - all pairs with duplicate values
        List<int[]> allPairs = allTwoSumPairs(new int[]{3, 3, 4, 2, 3}, 6);
        // Expected pairs (by index): (0,1) 3+3, (0,4) 3+3, (1,4) 3+3, (2,3) 4+2
        check(allPairs.size() == 4, "should find all 4 index pairs summing to target, including duplicates");
        Set<String> pairStrings = new HashSet<>();
        for (int[] p : allPairs) {
            pairStrings.add(p[0] + "," + p[1]);
        }
        check(pairStrings.contains("0,1"), "should include pair (0,1)");
        check(pairStrings.contains("0,4"), "should include pair (0,4)");
        check(pairStrings.contains("1,4"), "should include pair (1,4)");
        check(pairStrings.contains("2,3"), "should include pair (2,3)");
        System.out.println("Test 4 passed: stretch goal all pairs with duplicates");

        System.out.println("All Challenge 2 tests passed.");
    }
}
