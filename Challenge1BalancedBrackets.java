import java.util.*;

/**
 * Challenge 1 - Balanced Brackets
 *
 * Given a string containing ( ) [ ] { }, determine whether every opening
 * bracket has a matching, correctly-nested closing bracket of the same type.
 * Non-bracket characters are ignored (treated as no-ops).
 */
public class Challenge1BalancedBrackets {

    // Time: O(n), Space: O(n) worst case (a string of all openers)
    public static boolean isBalanced(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> closingToOpening = new HashMap<>();
        closingToOpening.put(')', '(');
        closingToOpening.put(']', '[');
        closingToOpening.put('}', '{');

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (closingToOpening.containsKey(c)) {
                if (stack.isEmpty() || (char) stack.pop() != closingToOpening.get(c)) {
                    return false;
                }
            }
            // any other character is a no-op
        }
        return stack.isEmpty();
    }

    // ---------------- STRETCH GOAL (OPTIONAL) ----------------
    // Instead of a boolean, return the index of the first mismatched bracket,
    // or -1 if the string is balanced.
    // Time: O(n), Space: O(n)
    public static int firstMismatchIndex(String s) {
        Deque<int[]> stack = new ArrayDeque<>(); // {char, index}
        Map<Character, Character> closingToOpening = new HashMap<>();
        closingToOpening.put(')', '(');
        closingToOpening.put(']', '[');
        closingToOpening.put('}', '{');

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(new int[]{c, i});
            } else if (closingToOpening.containsKey(c)) {
                if (stack.isEmpty()) {
                    return i; // unmatched closing bracket
                }
                int[] top = stack.pop();
                if ((char) top[0] != closingToOpening.get(c)) {
                    return i; // mismatched type
                }
            }
        }
        // any openers left unmatched: report the earliest one
        if (!stack.isEmpty()) {
            int earliest = Integer.MAX_VALUE;
            for (int[] entry : stack) {
                earliest = Math.min(earliest, entry[1]);
            }
            return earliest;
        }
        return -1;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("FAILED: " + message);
        }
    }

    public static void main(String[] args) {
        // Test 1: worked examples from the problem statement
        check(isBalanced("(a[b]{c})") == true, "nested mixed brackets should balance");
        check(isBalanced("([)]") == false, "interleaved brackets should not balance");
        check(isBalanced("((") == false, "unmatched openers should not balance");
        System.out.println("Test 1 passed: worked examples");

        // Test 2: edge cases - empty string and non-bracket characters
        check(isBalanced("") == true, "empty string should balance");
        check(isBalanced("hello world 123") == true, "non-bracket text should balance");
        check(isBalanced("(hello [world] {!})") == true, "text mixed with brackets should balance");
        System.out.println("Test 2 passed: empty string and non-bracket characters");

        // Test 3: unmatched brackets on either side
        check(isBalanced(")") == false, "lone closing bracket should not balance");
        check(isBalanced("(") == false, "lone opening bracket should not balance");
        check(isBalanced("]") == false, "lone closing bracket should not balance");
        System.out.println("Test 3 passed: unmatched opening/closing brackets");

        // Test 4: stretch goal - first mismatch index
        check(firstMismatchIndex("(a[b]{c})") == -1, "balanced string should return -1");
        check(firstMismatchIndex("([)]") == 2, "should report index of ')' mismatching '['");
        check(firstMismatchIndex("((") == 0, "should report earliest unmatched opener");
        check(firstMismatchIndex(")") == 0, "should report unmatched closing bracket");
        System.out.println("Test 4 passed: stretch goal first mismatch index");

        System.out.println("All Challenge 1 tests passed.");
    }
}
