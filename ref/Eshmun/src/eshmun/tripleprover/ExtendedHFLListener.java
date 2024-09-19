package eshmun.tripleprover;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;


public class ExtendedHFLListener implements HFLListener {

    /**
     * OVERVIEW: This is an extended Listener plugged to the parser, it gives an access point to each rule
     * also exit event on a rule. I used it to fill the AST. On each rule a node is added to the abstract syntax tree
     * Some rules add a specific node like Expression Node, Statement Node and Formula Node
     * This is used to avoid augmenting the grammar file HFL.g4 with semantic actions
     * Authors : Chukri Soueidi
     * Created on 5/7/17.
     */

    HFLParser parser;
    HFLLexer lexer;

    Map<String, String> memory = new HashMap<String, String>();


    public ExtendedHFLListener(HFLParser parser, HFLLexer lexer) {
        this.parser = parser;
        this.lexer = lexer;
    }


    @Override
    public void enterEveryRule(ParserRuleContext ctx) {


    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {


    }

    Boolean inRHS = false;

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

        //EFFECTS: Adds to AST a token Node

        String token = terminalNode.getText();

        ASTNode newNode = new ASTNode(AST.current, AST.current.level + 1, terminalNode.getText(), true);
        if (token.equals("{") || token.equals("}") || token.equals(";")) {
            newNode.nonPrintableNode = true;
            AST.current.addChild(newNode);
        } else
            AST.current.addChild(newNode);

        if (token.equals(";")) {

            inRHS = false;
        }


        if (terminalNode.getSymbol().getType() == lexer.TOK_OP_ASSIGN) {

            AST.current.type = ASTNode.Type.AssignmentStatement;
            inRHS = true;
        }

        if (terminalNode.getSymbol().getType() == lexer.TOK_IF) {

            AST.current.type = ASTNode.Type.IfStatement;

        }

        if (terminalNode.getSymbol().getType() == lexer.TOK_ELSE) {

            AST.current.type = ASTNode.Type.IfElseStatement;

        }



        if (AST.current instanceof StatementNode && !token.equals("if")) {

            if (((StatementNode) AST.current).LHS == null)
                ((StatementNode) AST.current).LHS = newNode;
        }

    }

    @Override
    public void enterTriple(HFLParser.TripleContext ctx) {

        AST.root = new ASTNode(null, 0, "Triple", false);
        AST.current = AST.root;
    }

    @Override
    public void exitTriple(HFLParser.TripleContext ctx) {

        MoveUp();

    }

    @Override
    public void enterP(HFLParser.PContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());

    }

    @Override
    public void exitP(HFLParser.PContext ctx) {
        MoveUp();

    }

    @Override
    public void enterS(HFLParser.SContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitS(HFLParser.SContext ctx) {
        MoveUp();

    }

    @Override
    public void enterQ(HFLParser.QContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitQ(HFLParser.QContext ctx) {
        MoveUp();

    }

    @Override
    public void enterFormula(HFLParser.FormulaContext ctx) {

        FormulaNode newNode = new FormulaNode(AST.current, AST.current.level + 1, parser.getRuleNames()[ctx.getRuleIndex()].toString(), false);
        AST.current.addChild(newNode);
        AST.current = newNode;
        AST.current.type = ASTNode.Type.LogicFormula;
    }

    @Override
    public void exitFormula(HFLParser.FormulaContext ctx) {
        MoveUp();

    }

    @Override
    public void enterDisjunction(HFLParser.DisjunctionContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitDisjunction(HFLParser.DisjunctionContext ctx) {
        MoveUp();

    }

    @Override
    public void enterConjunction(HFLParser.ConjunctionContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitConjunction(HFLParser.ConjunctionContext ctx) {
        MoveUp();

    }

    @Override
    public void enterNegation(HFLParser.NegationContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitNegation(HFLParser.NegationContext ctx) {
        MoveUp();

    }

    @Override
    public void enterPredicate(HFLParser.PredicateContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitPredicate(HFLParser.PredicateContext ctx) {
        MoveUp();

    }

    @Override
    public void enterBinaryPredicate(HFLParser.BinaryPredicateContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitBinaryPredicate(HFLParser.BinaryPredicateContext ctx) {

        MoveUp();


    }

    @Override
    public void enterTerm(HFLParser.TermContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitTerm(HFLParser.TermContext ctx) {
        MoveUp();

    }

    @Override
    public void enterFunction(HFLParser.FunctionContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitFunction(HFLParser.FunctionContext ctx) {
        MoveUp();

    }

    @Override
    public void enterBinaryFunction(HFLParser.BinaryFunctionContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitBinaryFunction(HFLParser.BinaryFunctionContext ctx) {
        MoveUp();

    }


    @Override
    public void enterStatement(HFLParser.StatementContext ctx) {


        StatementNode newNode = new StatementNode(AST.current, AST.current.level + 1, parser.getRuleNames()[ctx.getRuleIndex()].toString(), false);
        AST.current.addChild(newNode);
        AST.current = newNode;


    }

    @Override
    public void exitStatement(HFLParser.StatementContext ctx) {
        MoveUp();

    }

    @Override
    public void enterBlock(HFLParser.BlockContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
        if(AST.current.parent instanceof  StatementNode){
            AST.current.type = ASTNode.Type.BlockNode;
            AST.current.parent.type = ASTNode.Type.BlockStatment;

        }
    }

    @Override
    public void exitBlock(HFLParser.BlockContext ctx) {
        MoveUp();

    }

    @Override
    public void enterIfcondition(HFLParser.IfconditionContext ctx) {


        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());

        AST.current.type = ASTNode.Type.IfCondition;

    }

    @Override
    public void exitIfcondition(HFLParser.IfconditionContext ctx) {
        MoveUp();

    }

    @Override
    public void enterIfcondTerm(HFLParser.IfcondTermContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitIfcondTerm(HFLParser.IfcondTermContext ctx) {
        MoveUp();

    }

    @Override
    public void enterIfcondFact(HFLParser.IfcondFactContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitIfcondFact(HFLParser.IfcondFactContext ctx) {
        MoveUp();

    }

    @Override
    public void enterExpr(HFLParser.ExprContext ctx) {


        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
        AST.current.type = ASTNode.Type.Expression;

        if (inRHS && AST.current.parent instanceof StatementNode) {
            ((StatementNode) AST.current.parent).RHS = AST.current;
            inRHS = false;
        }
    }

    @Override
    public void exitExpr(HFLParser.ExprContext ctx) {
        MoveUp();


    }

    @Override
    public void enterExprterm(HFLParser.ExprtermContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());

//        ASTNode newNode = new ASTNode(AST.current, AST.current.level + 1, "(", true);
//        AST.current.addChild(newNode);

    }

    @Override
    public void exitExprterm(HFLParser.ExprtermContext ctx) {

//        ASTNode newNode = new ASTNode(AST.current, AST.current.level + 1, ")", true);
//        AST.current.addChild(newNode);

        MoveUp();

    }

    @Override
    public void enterFactor(HFLParser.FactorContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitFactor(HFLParser.FactorContext ctx) {
        MoveUp();

    }

    @Override
    public void enterRelop(HFLParser.RelopContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitRelop(HFLParser.RelopContext ctx) {
        MoveUp();

    }

    @Override
    public void enterAddop(HFLParser.AddopContext ctx) {
        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitAddop(HFLParser.AddopContext ctx) {
        MoveUp();

    }

    @Override
    public void enterMulop(HFLParser.MulopContext ctx) {

        AddNode(parser.getRuleNames()[ctx.getRuleIndex()].toString());
    }

    @Override
    public void exitMulop(HFLParser.MulopContext ctx) {
        MoveUp();

    }


    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }


    private void AddNode(String ruleName) {
    	 
        ASTNode newNode = new ASTNode(AST.current, AST.current.level + 1, ruleName, false);
        AST.current.addChild(newNode);
        AST.current = newNode;

    }

    private void MoveUp() {
        AST.current = AST.current.getParent();

    }


}

