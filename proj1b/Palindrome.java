public class Palindrome {
    public static Deque<Character> wordToDeque(String word) {
        //ArrayDeque<Character> wd = new ArrayDeque<>();
        LinkedListDeque<Character> wd = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            wd.addLast(word.charAt(i));
        }
        return wd;
    }

    public static boolean isPalindrome(String word) {
        int len = word.length();
        int i, j;
        for (i = 0, j = len - 1; i < j; i++, j--) {
            if (word.charAt(i) != word.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wd = wordToDeque(word);
        while (wd.size() > 1) {
            if (!cc.equalChars(wd.removeFirst(), wd.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
