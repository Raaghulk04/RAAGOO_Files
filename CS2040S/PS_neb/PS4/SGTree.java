/**
 * Scapegoat Tree class
 *
 * This class contains an implementation of a Scapegoat tree.
 */

public class SGTree {
    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        int weight = 1;
        public TreeNode left = null;
        public TreeNode right = null;
        public TreeNode parent = null;

        TreeNode(int k) {
            key = k;
        }
    }
    public static class Position {
        int pos;

        Position() { pos = 0; }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the subtree rooted at node
     *
     * @param node the root of the subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node) {
        // TODO: Implement this
        if (node == null) {
            return 0;
        } else {
            return 1 + countNodes(node.left) + countNodes(node.right);
        }
    }

    /**
     * Builds an array of nodes in the subtree rooted at node
     *
     * @param node the root of the subtree
     * @return array of nodes
     */

    public void enum_helper(TreeNode node, TreeNode[] retArr, Position p) {
        if (node.left == null && node.right == null) {
            // add current node
            retArr[p.pos] = node;
            p.pos++;
        } else if (node.left == null) {
            // add current node
            retArr[p.pos] = node;
            p.pos++;
            // recurse on right
            enum_helper(node.right, retArr, p);
        } else if (node.right == null) {
            // recurse on left
            enum_helper(node.left, retArr, p);
            // add current node
            retArr[p.pos] = node;
            p.pos++;
        } else {
            // recurse on left
            enum_helper(node.left, retArr, p);
            // add current node
            retArr[p.pos] = node;
            p.pos++;
            // recurse on right
            enum_helper(node.right, retArr, p);
        }
    }

    public TreeNode[] enumerateNodes(TreeNode node) {
        // TODO: Implement this
        int nodeCount = countNodes(node);
        TreeNode[] retArr = new TreeNode[nodeCount];
        Position p = new Position();
        enum_helper(node, retArr, p);
        return retArr;
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode build_helper(TreeNode node, TreeNode[] nodeList, int min, int max) {
        if (max < min) {
            return null;
        } else {
            int mid = min + (max - min) / 2;
            TreeNode currNode = nodeList[mid];
            currNode.left = build_helper(currNode, nodeList, min, mid - 1);
            currNode.right = build_helper(currNode, nodeList, mid + 1, max);
            currNode.parent = node;

            int left = currNode.left == null ? 0 : currNode.left.weight;
            int right = currNode.right == null ? 0 : currNode.right.weight;
            currNode.weight = left + right + 1;
            return currNode;
        }
    }

    public TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        int max = nodeList.length - 1;
        TreeNode initNode = nodeList[max / 2];
        build_helper(initNode, nodeList, 0, max);
        return initNode;
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // TODO: Implement this
        if (node == null) {
            return true;
        } else {
            final double ratio = 2.0/3.0;
            double left = node.left == null ? 0 : node.left.weight;
            double right = node.right == null ? 0 : node.right.weight;
            boolean checkCurr = left / node.weight <= ratio && right / node.weight <= ratio;
            return checkCurr;
        }
    }

    /**
    * Rebuilds the subtree rooted at node
    * 
    * @param node the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node) {
        // Error checking: cannot rebuild null tree
        if (node == null) {
            return;
        }

        TreeNode p = node.parent;
        TreeNode[] nodeList = enumerateNodes(node);
        TreeNode newRoot = buildTree(nodeList);

        if (p == null) {
            root = newRoot;
        } else if (node == p.left) {
            p.left = newRoot;
        } else {
            p.right = newRoot;
        }

        newRoot.parent = p;
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        rebuild(insert(key, root));
    }

    // Helper method to insert a key into the tree
    private TreeNode insert(int key, TreeNode node) {
        if (key < node.key) {
            if (node.left == null) {
                node.left = new TreeNode(key);
                node.left.parent = node;
                node.weight++;
                if (!checkBalance(node)) {
                    return node;
                }
                return null;
            } else {
                TreeNode highest = insert(key, node.left);
                node.weight++;
                if (!checkBalance(node)) {
                    return node;
                }
                return highest;
            }
        } else if (key > node.key) {
            if (node.right == null) {
                node.right = new TreeNode(key);
                node.right.parent = node;
                node.weight++;
                if (!checkBalance(node)) {
                    return node;
                }
                return null;
            } else {
                TreeNode highest = insert(key, node.right);
                node.weight++;
                if (!checkBalance(node)) {
                    return node;
                }
                return highest;
            }
        } else {
            return null; // if key is a duplicate, leave tree unchanged
        }
    }

    // A voice (that sounds suspiciously like Prof Eldon) whispers
    // let's do a sanity check

    // Analyse runtime:

    // Worst case: checkBalance, updating weight is O(1)
    // Number of recursive calls: max O(h), h = height of tree
    // Recurrence relation: T(h) = T(h-1) + O(1) = O(h)

    // rebuild(highest): max O(k), k = number of nodes in tree

    // Runtime = O(h) + O(k) = O(k)
    // Scenario: (WLOG wrt left/right) Imagine before insertion,
    // left subtree of root node is full subtree of height k+1
    // and right subtree of root node has at least one node of height k
    // then inserting a node on left subtree would cause
    // max rebuild runtime of O(k), k = number of nodes in tree

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root);
    }
}
