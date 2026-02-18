import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';

    // root
    TrieNode root;

    private class TrieNode {
        TrieNode[] children = null;
        char value; // initialised as '\u0000'
        boolean endFlag = false;

        // constructor
        TrieNode(char c) {
            value = c;
            children = new TrieNode[62];    // one children per ASCII character
            for (int i = 0; i < 62; i++) {
                children[i] = null;
            }
        }
    }

    public Trie() {
        this.root = new TrieNode('\u0000');
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // Goal: Iterate through using charAt() => not char not present => insert => else traverse to child
        // Edge Case: string is null;
        if (s == null) {
            return ;
        }

        int len = s.length();
        TrieNode currNode = this.root;

        // Iterate through string 
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int cIndex = charToIndex(c);

            // if not present => insert as currNode child, traverse into new Child/existing Child
            TrieNode child = currNode.children[cIndex];
            if (child == null) { 
                currNode.children[cIndex] = new TrieNode(c);
            }
            currNode = currNode.children[cIndex]; // traverse into child
        }
        // place endFlag down
        currNode.endFlag = true;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // Goal: Iterate through s using charAt() => if presentChars => goes in, else => return false
        // Edge Case: if string is null => means contain true?
        if (s == null) {
            return true;
        }

        int len = s.length();
        TrieNode currNode = this.root;

        // Iterate through
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int cIndex = charToIndex(c);

            // Check if char present
            TrieNode child = currNode.children[cIndex];
            if (child == null) { // not present
                return false;
            } else {    // present
                currNode = currNode.children[cIndex];
            }
        }
        return currNode.endFlag;
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
        // Goal: Recursively go through the string, if WILDCARD => open up new branch
        StringBuilder res = new StringBuilder();
        TrieNode currNode = this.root;

        wildpath(s, results, limit, 0, res, currNode);
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
        t.insert("aaa09AzZ");
        t.insert(null);
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
        System.out.println(t.contains(null));
        // String[] result1 = t.prefixSearch("", 10);
        String[] result2 = t.prefixSearch("...e", 3);

        for (String s: result2) {
            System.out.println(s);
        }
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }

    // convert char to cIndex
    private int charToIndex(char c) {
        int cASCII = (int)c;
        
        if (cASCII <= 58) {     // num
            return cASCII - (int)'0';
        } else if (cASCII <= 90) {      // lowercase
            return cASCII - (int)'A' + 9 + 1;
        } else {
            return cASCII - (int)'a' + 9 + 26 + 1;
        }
    }

    private int wildpath(String s, ArrayList<String> results, int limit, int index, StringBuilder res, TrieNode currNode) {
        // Edge Case: limit reached
        if (limit <= 0) {
            return 0;
        }
        
        // Iterate through the prefix first
        while (index < s.length()) {
            char c = s.charAt(index);

            // if WILDCARD => open more wildpath for each children in currNode
            if (c == WILDCARD) {
                int len = res.length(); // use for reset
                for (TrieNode child: currNode.children) {
                    if (child != null) {
                        int newIndex = index + 1;
                        limit = wildpath(s, results, limit, newIndex, res.append(child.value), child);
                    }
                    res.setLength(len);
                }
                // this timeline have to break in order to preserve the time-space balance (. dont exist as a child)
                return limit;

            } else { // if normal character => traverse it to if exist, else break function?
                int cIndex = charToIndex(c);
                TrieNode child = currNode.children[cIndex];

                if (child != null) { // child exist
                    res.append(child.value);
                } else {   // word with prefix doesnt exist
                    return limit;
                }

                currNode = child;
                index++;
            }
        }
        System.out.println(res + " child: " + currNode.value);
        // find words that fits prefix
        limit = captureTheFlag(results, res, currNode, limit);

        return limit;
    }

    private int captureTheFlag(ArrayList<String> results, StringBuilder res, TrieNode currNode, int limit) {
        // GOAL: Depth-first search all flag and CAPTURE
        // Go left then go right then more right till i die

        // Edge case: limit hit, currNode null
        if (limit <= 0) {
            return 0;
        } 

        // Flag gain => TAKE
        if (currNode.endFlag) {
            results.add(res.toString());
            limit--;
        }

        // DFS
        int len = res.length(); // use to reset back to this later
        for (TrieNode child: currNode.children) {
            if (child != null) {
                limit = captureTheFlag(results, res.append(child.value), child, limit);
            }
            res.setLength(len);
        }

        return limit;
    }
}
