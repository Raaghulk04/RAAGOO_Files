import com.sun.source.tree.Tree;

import java.util.ArrayList;

public class Trie {
    // Wildcards
    final char WILDCARD = '.';

    TrieNode root;

    public Trie() {
        // TODO: Initialise a trie class here.
        this.root = new TrieNode();
    }

    static class TrieNode {
        // TODO: Create your TrieNode class here.
        static final int size = 62;
        boolean flag = false;
        TrieNode[] presentChars = new TrieNode[size];
        // 62 =
        // 10 ('0' to '9') +
        // 26 ('A' to 'Z') +
        // 26 ('a' to 'z')
    }

    /**
     * Converts character to int pos
     */
    int pos(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('A' <= c && c <= 'Z') {
            return 10 + c - 'A';
        } else if ('a' <= c && c <= 'z') {
            return 36 + c - 'a';
        } else {
            return -1;
        }
    }

    /*
     * Converts int pos to char
     */
    char toChar(int i) {
        if (0 <= i && i <= 9) {
            return (char) (i + '0');
        } else if (10 <= i && i <= 35) {
            return (char) (i - 10 + 'A');
        } else if (36 <= i && i <= 61) {
            return (char) (i - 36 + 'a');
        } else {
            return '\0';
        }
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // TODO
        TrieNode curr = this.root;
        for (int i = 0; i < s.length(); i++) {
            int idx = pos(s.charAt(i));
            if (curr.presentChars[idx] == null) {
                curr.presentChars[idx] = new TrieNode();
            }
            curr = curr.presentChars[idx];
        }
        curr.flag = true;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        TrieNode curr = this.root;
        for (int i = 0; i < s.length(); i++) {
            int idx = pos(s.charAt(i));
            if (curr.presentChars[idx] == null) {
                return false;
            } else {
                curr = curr.presentChars[idx];
            }
        }
        return curr.flag;
    }

    void buildHelper(ArrayList<String> results, TrieNode curr, int[] counter,
                     int limit, StringBuilder str) {
        // assume starting limit is positive (handled by OG func)
        int len = str.length();
        if (curr.flag) { // start from curr TrieNode and if flagged, add it
            results.add(str.toString());
            counter[0]++;
        }
        // iterate over its children
        for (int i = 0; i < TrieNode.size; i++) {
            TrieNode t = curr.presentChars[i];
            if (t == null) {
                continue;
            } else if (counter[0] >= limit) {
                return;
            } else {
                str.setLength(len);
                str.append(toChar(i));
                buildHelper(results, t, counter, limit, str);
            }
        }
    }

    void searchHelper(String s, ArrayList<String> results, int limit,
                      int[] counter, TrieNode curr, StringBuilder str) {
        int preLen = str.length(); // str length at start of iteration
        while (preLen < s.length()) {
            char c = s.charAt(preLen); // when str len i, get ith char
            if (c == WILDCARD) {
                // iterate through all children nodes
                for (int i = 0; i < TrieNode.size; i++) {
                    TrieNode t = curr.presentChars[i];
                    // for this iteration, terminate if already reached limit
                    if (counter[0] >= limit) {
                        return;
                    } else if (t == null) {
                        continue; // skip null children nodes
                    } else {
                        str.setLength(preLen); // cut str back to this length
                        str.append(toChar(i)); // append the node's character
                        // recursive call
                        searchHelper(s, results, limit, counter, t, str);
                    }
                }
                // terminate; buildHelper called in recursive calls
                return;
            } else {
                // go to node corresponding to new char
                curr = curr.presentChars[pos(c)];
                if (curr == null) { // if no such node terminate
                    return;
                } else { // else append the given char c
                    str.append(c);
                    preLen++;
                }
            }
        }
        // str == s -> start building results array
        buildHelper(results, curr, counter, limit, str);
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        // TODO
        if (limit == 0) {
            return;
        } else {
            int[] counter = {0}; // initialise counter (< limit)
            TrieNode curr = this.root;
            StringBuilder str = new StringBuilder();
            searchHelper(s, results, limit, counter, curr, str);
        }
    }

    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}