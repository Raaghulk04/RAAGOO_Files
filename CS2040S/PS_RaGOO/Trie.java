import java.util.ArrayList;

public class Trie {
    TrieNode root;

    // Wildcards
    final char WILDCARD = '.';

    private class TrieNode {
        public boolean isEnd = false;
        public TrieNode[] children = new TrieNode[62];

        //constructor
        public TrieNode(boolean isEnd, TrieNode[] children) {
            this.isEnd = isEnd;
            this.children = children;
        }

    }

    public Trie() {
        // TODO
        this.root = new TrieNode(false, new TrieNode[63]);
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        insertHelper(s, 0, root);
    }

    private int hash(int x) {
        if (x < 58 && x > 47) {
            return x - 48;
        } else if (x < 91 && x > 64) {
            return x - 65 + 10;
        } else {
            return x - 97 + 26 + 10;
        }
    }

    private int reversehash(int x) {
        if (x <= 9) {
            return x + 48;
        } else if (x < 36) {
            return x + 65 - 10;
        } else {
            return x + 97 - 26 - 10;
        }
    }

    private void insertHelper(String s, int idx, TrieNode node) {
        if (s == "") {
            node.children[62] = new TrieNode(true, new TrieNode[63]);
        }
        if (idx >= s.length()) {
            return;
        }
        TrieNode curr = node;
        //find matching child, if there is one
        int hashval = hash(s.charAt(idx));
        if (curr.children[hashval] != null) {
            //if reach end of word, mark the last
            if (idx == s.length() - 1) {
                curr.children[hashval].isEnd = true;
            }
            insertHelper(s, ++idx, curr.children[hashval]);
        } else {
            //if no matching child, insert the char and update the parent's children array
            curr.children[hashval] = new TrieNode(false, new TrieNode[63]);
            //if it is the end of the word set isEnd to be true;
            if (idx == s.length() - 1) {
                curr.children[hashval].isEnd = true;
            }
            insertHelper(s, ++idx, curr.children[hashval]);
        }
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        return containsHelper(s, 0, root);
    }

    private boolean containsHelper(String s, int idx, TrieNode node) {
        if (s == "") {
            return (node.children[62] != null);
        }
        if (idx >= s.length()) {
            return false;
        }
        TrieNode curr = node;
        //find a matching child node, hashval of char will lead to the correct array index, if it's null then don't have the string, if not traverse further
        int hashval = hash(s.charAt(idx));
        if (curr.children[hashval] == null) {
            return false;
        }
        if ((idx == s.length() - 1) && (curr.children[hashval].isEnd)) {
            return true;
        }
        return containsHelper(s, ++idx, curr.children[hashval]);
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
        StringBuilder str = new StringBuilder();
        prefixSearchHelper(s, 0, results, limit, root, str);
    }

    private void prefixSearchHelper(String s, int idx, ArrayList<String> results, int limit, TrieNode node, StringBuilder str) {
        //traverse until you get to the end of the string s
        if (node == null || results.size() >= limit) { return ;}

        //still matching s
        if (idx <= s.length() - 1) {
            int hashval = hash(s.charAt(idx));
            //case without the special character
            if (s.charAt(idx) != '.' && node.children[hashval] != null) {
                str.append(s.charAt(idx));
                //append the current char from s and recurse further
                prefixSearchHelper(s, idx + 1, results, limit, node.children[hashval], str);
            //if there is a special character, recurse through every child that is not empty
            } else if (s.charAt(idx) == '.') {
                for (int i = 0; i < 63; i++) {
                    if (node.children[i] != null) {
                        int lenbefore = str.length();
                        char x = (char) reversehash(i);
                        str.append(x);
                        prefixSearchHelper(s, idx + 1, results, limit, node.children[i], str);
                        str.setLength(lenbefore);
                    }
                }
            }
        //after going through each char in s already
        } else {
            //if this node is a word, add the current str to the result
            if (node.isEnd) {
                results.add(str.toString());
                if (results.size() >= limit) { return; }
            }
            //else continue to traverse through all other children
            for (int i = 0; i < 63; i++) {
                if (node.children[i] != null) {
                    //simply append the char into the str, continue to traverse further
                    int lenbefore = str.length();
                    char x = (char) reversehash(i);
                    str.append(x);
                    prefixSearchHelper(s, idx + 1, results, limit, node.children[i], str);
                    str.setLength(lenbefore);
                }
            }
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
            t.insert("peak");
            t.insert("peat");
            t.insert("peter");
            t.insert("pete");
            t.insert("pick");
            t.insert("picked");
            t.insert("a");
            t.insert("peck");
            t.insert("of");
            t.insert("pickled");
            t.insert("peppers");
            t.insert("pepppito");
            t.insert("pepi");
            t.insert("pik");
            t.insert("");
            t.insert("cat");
            t.insert("abc");
            t.insert("abcd");
            t.insert("abbde");
            t.insert("abcdef");
            t.insert("abed");


            //boolean test = t.contains("pepito");
            //System.out.println(test);

            String[] result2 = t.prefixSearch("ab.d", 10);
            System.out.println(result2.length);
            for (int i = 0; i < result2.length; i++) {
                System.out.println(result2[i]);
            }
            //String[] result2 = t.prefixSearch("pe.", 10);
            // result1 should be:
            // ["peck", "pepi", "peppers", "pepppito", "peter"]
            // result2 should contain the same elements with result1 but may be ordered arbitrarily
        }
}
