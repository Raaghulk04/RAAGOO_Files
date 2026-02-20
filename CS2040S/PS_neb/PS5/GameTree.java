import java.util.Scanner;
/**
 * Game Tree class
 *
 * This class contains the basic code for implementing a Game Tree. It includes functionality to load a game tree.
 */

public class GameTree {

    // Standard enumerations for tic-tac-toe variations
    public enum Player {ONE, TWO}

    public enum Game {Regular, Misere, NoTie, Arbitrary}

    // Some constants for tic-tac-toe
    final static int bsize = 3;
    final static int btotal = bsize * bsize;
    final static char EMPTYCHAR = '_';

    // Specifies the values for the arbitrary variant
    final int[] valArray = {1, -2, 1, -3, -2, 2, -2, -1, 1};

    /**
     * Simple TreeNode class.
     *
     * This class holds the data for a node in a game tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    static class TreeNode {
        public String name = "";
        public TreeNode[] children = null;
        public int numChildren = 0;
        public int value = Integer.MIN_VALUE;
        public boolean leaf = false;

        // Empty constructor for building an empty node
        TreeNode() {}

        // Simple constructor for build a node with a name and a leaf specification
        TreeNode(String s, boolean l) {
            name = s;
            leaf = l;
            children = new TreeNode[btotal];
            for (int i = 0; i < btotal; i++) {
                children[i] = null;
            }
            numChildren = 0;
        }
    }

    // Root of the tree - public to facilitate grading
    public TreeNode root = null;

    //--------------
    // Utility Functions
    //--------------

    // Simple function for determining the other player
    private Player other(Player p) {
        if (p == Player.ONE) return Player.TWO;
        else return Player.ONE;
    }

    // Draws a board using the game state stored as the name of the node.
    public void drawBoard(String s) {
        System.out.println("-------");
        for (int j = 0; j < bsize; j++) {
            System.out.print("|");
            for (int i = 0; i < bsize; i++) {
                char c = s.charAt(i + 3 * j);
                if (c != EMPTYCHAR)
                    System.out.print(c);
                else System.out.print(" ");
                System.out.print("|");
            }
            System.out.println();
            System.out.println("-------");
        }
    }

    /**
     * readTree reads a game tree from the specified file. If the file does not exist, cannot be found, or there is
     * otherwise an error opening or reading the file, it prints out "Error reading file" along with additional error
     * information.
     *
     * @param fName name of file to read from
     */
    public void readTree(String fName) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(fName));
            root = readTree(reader);
        } catch (java.io.IOException e) {
            System.out.println("Error reading file: " + e);
        }
    }

    // Helper function: reads a game tree from the specified reader
    private TreeNode readTree(java.io.BufferedReader reader) throws java.io.IOException {
        String s = reader.readLine();
        if (s == null) {
            throw new java.io.IOException("File ended too soon.");
        }
        TreeNode node = new TreeNode();
        node.numChildren = Integer.parseInt(s.substring(0, 1));
        node.children = new TreeNode[node.numChildren];
        node.leaf = (s.charAt(1) == '1');
        node.value = Integer.MIN_VALUE;
        if (node.leaf) {
            char v = s.charAt(2);
            node.value = Character.getNumericValue(v);
            node.value--;
        }
        node.name = s.substring(3);

        for (int i = 0; i < node.numChildren; i++) {
            node.children[i] = readTree(reader);
        }
        return node;
    }

    /**
     * findValue determines the value for every node in the game tree and sets the value field of each node. If the root
     * is null (i.e., no tree exists), then it returns Integer.MIN_VALUE.
     *
     * @return value of the root node of the game tree
     */

    int valueHelp(TreeNode node, boolean onesTurn) {
        if (!node.leaf) {
            // Since there exists an child,
            // steal its personal information
            if (onesTurn) {
                // same name as root node -> make best decision
                int currVal = Integer.MIN_VALUE;
                for (TreeNode child : node.children) {
                    valueHelp(child, !onesTurn);
                    currVal = Integer.max(currVal, child.value);
                }
                node.value = currVal;
            } else {
                // not "my" turn -> time to burn bridges
                int currVal = Integer.MAX_VALUE;
                for (TreeNode child : node.children) {
                    valueHelp(child, !onesTurn);
                    currVal = Integer.min(currVal, child.value);
                }
                node.value = currVal;
            }
        }
        return node.value;
    }

    int findValue() {
        // TODO: Implement this
        if (this.root == null) {
            return Integer.MIN_VALUE;
        } else {
            valueHelp(this.root, true);
            return this.root.value;
        }
    }


    // Simple main for testing purposes
    public static void main(String[] args) {
        GameTree tree = new GameTree();
        String pwd = "C:/Users/start/OneDrive/Desktop/CS2040S/PS5/ps5/ps5/";

        tree.readTree(pwd + "games/tictac_9_empty.txt");
        System.out.printf("standard: %d\n", tree.findValue());

        tree.readTree(pwd + "variants/arbitrary.txt");
        System.out.printf("arbitrary: %d\n", tree.findValue());

        tree.readTree(pwd + "variants/misere.txt");
        System.out.printf("misere: %d\n", tree.findValue());

        tree.readTree(pwd + "variants/notie.txt");
        System.out.printf("notie: %d\n", tree.findValue());



        // WARNING: YOU ARE ABOUT TO READ THE WORST WRITTEN CODE
        // IN EXISTENCE.

        // I WAS NOT OF SOUND BODY AND MIND.

//        // start from -10
//        for (int i = 0; i < 9; i++) {
//            tree.valArray[i] = -10;
//        }
//
//        // look, there is probably a nice, pretty solution
//        // but its 11pm I'mma come up with
//        // chaos
//        int low = -10;
//        int high = 10;
//        // okay look I would StringBuilder properly but to use the same one
//        // I would need to .format anyway for padding in order to .setLength
//        // which is more pain than this is honestly worth
//        for (int a = low; a <= high; a++) {
//            tree.valArray[0] = a;
//            for (int b = low; b <= high; b++) {
//                tree.valArray[1] = b;
//                for (int c = low; c <= high; c++) {
//                    tree.valArray[2] = c;
//                    for (int d = low; d <= high; d++) {
//                        tree.valArray[3] = d;
//                        for (int e = low; e <= high; e++) {
//                            tree.valArray[4] = e;
//                            for (int f = low; f <= high; f++) {
//                                tree.valArray[5] = f;
//                                for (int g = low; g <= high; g++) {
//                                    tree.valArray[6] = g;
//                                    for (int h = low; h <= high; h++) {
//                                        tree.valArray[7] = h;
//                                        for (int i = low; i <= high; i++) {
//                                            tree.valArray[8] = i;
//                                            tree.readTree(pwd + "variants/arbitrary.txt");
//                                            StringBuilder s = new StringBuilder();
//                                            for (int someInt : tree.valArray) {
//                                                s.append(someInt).append(" ");
//                                            }
//                                            int treeVal = tree.findValue();
//                                            System.out.println(String.format("%s: %d", s.toString(), treeVal));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        // reset valArray
//        tree.valArray[0] = 1;
//        tree.valArray[1] = -2;
//        tree.valArray[2] = 1;
//        tree.valArray[3] = -3;
//        tree.valArray[4] = -2;
//        tree.valArray[5] = 2;
//        tree.valArray[6] = -2;
//        tree.valArray[7] = -1;
//        tree.valArray[8] = 1;

        // Start of game
        StringBuilder gm = new StringBuilder();
        Scanner input = new Scanner(System.in);
        System.out.print("Player ONE or TWO: ");
        String player = input.next();
        while (player.compareTo("ONE") != 0 && player.compareTo("TWO") != 0) {
            System.out.println("Invalid input. ONE or TWO: ");
            player = input.next();
        }
        System.out.print("standard, arbitrary, misere or notie: ");
        String game = input.next();
        while (game.compareTo("standard") != 0 && game.compareTo("arbitrary") != 0
                && game.compareTo("misere") != 0 && game.compareTo("notie") != 0) {
            System.out.println("Invalid input. standard, arbitrary, misere or notie: ");
            game = input.next();
        }

        if (game.compareTo("standard") == 0) {
            tree.readTree(pwd + "games/tictac_9_empty.txt");
            tree.findValue();
        } else if (game.compareTo("arbitrary") == 0) {
            tree.readTree(pwd + "variants/arbitrary.txt");
            tree.findValue();
        } else if (game.compareTo("misere") == 0) {
            tree.readTree(pwd + "variants/misere.txt");
            tree.findValue();
        } else {
            tree.readTree(pwd + "variants/notie.txt");
            tree.findValue();
        }

        if (player.compareTo("ONE") == 0) {
            tree.drawBoard(tree.root.name);
            while (!tree.root.leaf) {
                int pos = input.nextInt();
                // have to consider the logic; some slots already taken
                while (pos < 0 || pos > 8) {
                    System.out.print("Invalid input. Input a value from 0 to 8: ");
                    pos = input.nextInt();
                }
                gm.setLength(0); // reset StringBuilder
                boolean yes = true; // honestly im not good with names this is js a red flag
                int next = 0;
                int i = 0;
                while (i < btotal) {
                    if (pos == i) {
                        if (tree.root.name.charAt(i) != EMPTYCHAR) {
                            System.out.print("Not a legal move. Input a value from 0 to 8: ");
                            pos = input.nextInt();
                            i = 0; // start the for loop all over again
                            next = 0;
                        } else {
                            gm.append('X'); // player ONE, i.e. human
                            yes = false;
                            i++;
                        }
                    } else {
                        gm.append(tree.root.name.charAt(i));
                        if (yes && tree.root.name.charAt(i) == EMPTYCHAR) {
                            next++;
                        }
                        i++;
                    }
                }
                tree.root = tree.root.children[next];
                tree.drawBoard(tree.root.name);
                TreeNode soFar = null;
                int small = Integer.MAX_VALUE;
                for (TreeNode child : tree.root.children) {
                    if (child.value < small) {
                        soFar = child;
                        small = child.value;
                    }
                }
                tree.root = soFar;
                tree.drawBoard(tree.root.name);
            }
        } else {
            while (!tree.root.leaf) {
                TreeNode soFar = null;
                int large = Integer.MIN_VALUE;
                for (TreeNode child : tree.root.children) {
                    if (child.value > large) {
                        soFar = child;
                        large = child.value;
                    }
                }
                tree.root = soFar;
                tree.drawBoard(tree.root.name);
                int pos = input.nextInt();
                while (pos < 0 || pos > 8) {
                    System.out.print("Invalid input. Input a value from 0 to 8: ");
                    pos = input.nextInt();
                }
                gm.setLength(0); // reset StringBuilder
                boolean yes = true;
                int next = 0;
                int i = 0;
                while (i < btotal) {
                    if (pos == i) {
                        if (tree.root.name.charAt(i) != EMPTYCHAR) {
                            System.out.print("Not a legal move. Input a value from 0 to 8: ");
                            pos = input.nextInt();
                            i = 0; // start the for loop all over again
                            next = 0;
                        } else {
                            gm.append('O'); // player TWO, i.e. human
                            yes = false;
                            i++;
                        }
                    } else {
                        gm.append(tree.root.name.charAt(i));
                        if (yes && tree.root.name.charAt(i) == EMPTYCHAR) {
                            next++;
                        }
                        i++;
                    }
                }
                tree.root = tree.root.children[next];
                tree.drawBoard(tree.root.name);
            }
        }
    }
}
