package eshmun.tripleprover;

/**
 * OVERVIEW: This is the container of our AST. It holds the root and current node created in the HFL parser
 * Authors : Chukri Soueidi
 * Created on 5/6/17.
 */

public class AST {

    public static ASTNode root;

    static ASTNode current;

    public static String trace = "";

    public void print() {
        root.print();
    }

}
