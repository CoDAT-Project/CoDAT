package eshmun.tripleprover;

import java.util.ArrayList;
import java.util.List;


public class ASTNode {

    /**
     * IMPLEMENTATION:
     * This is my implementation of the AST. It is a recursive data structure that holds pointers to children and parent.
     * It gets filled while the parser visit the rules and terminals, then after that we call method print on root node to print it on the output file
     * Authors : Chukri Soueidi
     * Created on 5/6/17.
     */


    public ASTNode parent;
    public int level;
    public List<ASTNode> children;
    public String text;
    public boolean isToken;
    public boolean nonPrintableNode;

    public Type type;

    public static enum Type { //different types of Nodes
        AssignmentStatement, LogicFormula, Expression, IfStatement, IfElseStatement,
         IfCondition, BlockNode, BlockStatment
    }

    public ASTNode() {
        children = new ArrayList<>();
    }

    public static ASTNode lastPrintedNode;

    public ASTNode(ASTNode parent, int level, String text, boolean isToken) {
        this.parent = parent;
        this.level = level;
        this.text = text;
        this.isToken = isToken;
        children = new ArrayList<>();

    }

    public void addChild(ASTNode child) {

        children.add(child);
    }

    public ASTNode getParent() {
        return this.parent;
    }

    static boolean showNonTokens = true;

    public String printn() {

        //EFFECTS: Returns a string representation of a node with all sub nodes.
        //HELPS: TestGrammar.main(), getIfConditionB

        AST.trace = "";
        showNonTokens = false;
        print();
        showNonTokens = true;
        return AST.trace;
    }

    public void print() {

        //EFFECTS: Returns a string representation of a Right hand side expression.
        //HELPS: this.printn(), this. printRHS()

        //Don't print if flagged nonPrintableNode
        if (!nonPrintableNode) {

            if (isToken) {
                AST.trace = AST.trace + text + " ";

            } else {
                if (showNonTokens)
                    AST.trace = AST.trace + text + " ";
            }


        }

        if (text.equals(";") || text.equals("S:") || text.equals("P:") || text.equals("Q:")) {
            AST.trace = AST.trace + "\n";
        }

        //loop and print children, which calls recursively
        for (ASTNode child : children) {

            child.print();
        }

    }


    public String printRHS(String g) {
        //EFFECTS: Returns a string representation of a Right hand side expression.
        //HELPS: WeakestPrecondition.wp()

        if (isToken && !text.trim().equals(";")) {
            return g + " " + text;
        }
        for (ASTNode child : children) { //loop through all linked nodes
            g = child.printRHS(g);
        }

        return g;
    }


}


class StatementNode extends ASTNode {
    /*
        Subtype of ASTNode specific for Statement nodes
     */

    ASTNode LHS;
    ASTNode RHS;

    public StatementNode(ASTNode parent, int level, String text, boolean isToken) {
        super(parent, level, text, isToken);
    }

}


class ExpressionNode extends ASTNode {
    /*
        Subtype of ASTNode specific for Expression nodes
     */

    public ExpressionNode(ASTNode parent, int level, String text, boolean isToken) {
        super(parent, level, text, isToken);
    }


}

class FormulaNode extends ASTNode {
    /*
           Subtype of ASTNode specific for Formula nodes
    */

    public FormulaNode(ASTNode parent, int level, String text, boolean isToken)
    {
        super(parent, level, text, isToken);
    }


}


