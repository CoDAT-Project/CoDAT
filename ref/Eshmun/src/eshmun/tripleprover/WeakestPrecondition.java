package eshmun.tripleprover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;


public class WeakestPrecondition {

    /**
     * IMPLEMENTATION:
     * This class helps in generating the weakest precondition formula.
     * Authors : Chukri Soueidi
     * Created on 5/6/17.
     */


    public static String wp(ASTNode s, String q) {

        //EFFECT:
        //   This function takes statement s (as an abstract syntax tree) and predicate q (given as a string) and
    	//   returns the weakest precondition of s w.r.t. q, also as a string.
        //   It is specific for the grammar HFL and needs to be modified to be run on a different grammar.
        //   If the ASTNode received isn't a node of interest it does nothing, just returns the input q //MAKE THIS PRECISE

        //REQUIRES: s.type is defined
      
        //HELPS: Main()

        //IMPLEMENTATION SKETCH:  	
    	//  Applies Dijkstra's definition of wp in the usual recursive manner
    	
    	
        if (s.type == null) return q;   //Statement type undefined, so return q. Client is responsible for detection.

        switch (s.type) {

            case IfStatement:
                // for a one-way IF Statement: apply
                // Weakest precondition: wp(if b then s, q) = b & wp(s,q) | !b & q

                //get the condition of the if-statement
                String b = getIfConditionB(s);

                //The children of an ASTNode are stored in an arraylist. 
                //The last element of the arraylist stores the body of the IF.
                ASTNode body = s.children.get(s.children.size() - 1);

                //apply formula and do recursion incase block has multiple statements
                if (body instanceof StatementNode) { //if body is a valid statement then apply wp definition
                    q = "(" + (b + " & " + wp(body, q)) + ") | (" + " ! (" + b + ") & " + q + ")"; 
                }//if body is not a valid statement, do nothing, so q is returned unchanged
                break;

            case IfElseStatement:

                //for an If Else: apply
                // Weakest precondition:  b & wp(s1,q) | !b & wp(s2,q)

                //get Then Block
                ASTNode firstNode = s.children.get(4);

                //get Else Block
                ASTNode secondNode = s.children.get(s.children.size() - 1);

                //get the if condition
                String B = getIfConditionB(s);

                //Apply formula
                q = "(" + (B + " & " + wp(firstNode, q)) + ") | (" + " ! (" + B + ") & " + wp(secondNode, q) + ")";


                break;


            case BlockStatment:

                //A statement which has a Block Node as its only child
                q = wp((s.children.get(0)), q);

                break;

            case BlockNode:
                // A block can have many statements
                // Weakest precondition: wp(S1;S2, Q) == wp(S1, wp(S2, Q))


                ArrayList<ASTNode> statements = (ArrayList<ASTNode>)s.children; //Get the block statements
                Collections.reverse(statements); //reverse to be able to generate the recursive statements



                for (int i = 0; i < statements.size(); i++) {
                    if (statements.get(i) instanceof StatementNode)
                        q = wp(s.children.get(i), q);
                }
                break;

            case AssignmentStatement:

                //For an assignement apply:
                //wp(x:=e, Q(x)) === Q(e)

                String rhs = ((StatementNode) s).RHS.printRHS("");

                //Here is where all statements end in an asssignment statement to apply wp(x:=e, Q(x)) === Q(e)
                q = substitueForWP(((StatementNode) s).LHS.text, rhs, q);

                break;

            default:
                break;
        }


        return q;

    }

    public static String substitueForWP(String xVariable, String expression, String text) {
        //IMPLEMENTATION SKETCH:
        // Here we substitute based on  wp(x:=e, Q(x))=== Q(e)

        //EFFECTS: takes x and e and text and returns text with x replaced with e
        //HELPS: this.wp()

        //If it is only xVariable just replace it
        if ((text.trim().equals(xVariable.trim()))) {

            return text = "( " + expression.trim() + " )";

        }

        //If not, tokenize and check carefully for the substitution
        ArrayList<String> output = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(text);

        //Add to array of strings
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            output.add(token);

        }

        //Loop and substitute
        for (int i = 0; i < output.size() - 1; i++) {

            if (xVariable.equals(output.get(i))) {
                output.set(i, " ( " + expression + " ) ");
            }
        }

        //return parenthesized
        String ret = "";
        for (String s : output) {
            ret = ret + s + " ";
        }

        return "( " + ret + ")";

    }

    public static String getIfConditionB(ASTNode statementNode) {
        //IMPLEMENTATION SKETCH:
        //   This function gets the ifcondition and returns a printed expression of it

        //EFFECTS: Returns printed condition
        //HELPS: this.wp()


        ASTNode lastNode = statementNode.children.get(2);// ifcondition is always at index 2 after 2 tokens : if ( ifcondition
        return " ( " + lastNode.printn() + " ) ";
    }


}


