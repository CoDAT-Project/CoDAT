package t7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import t7.expressions.ArithmeticExpression;
import t7.expressions.BooleanExpression;
import t7.expressions.Expression;
import t7.expressions.TripletExpression;
import t7.expressions.VariableExpression;
import t7.grammar.T7BaseListener;
import t7.grammar.T7Parser.Arith_exp1Context;
import t7.grammar.T7Parser.Arith_exp2Context;
import t7.grammar.T7Parser.Arith_exp3Context;
import t7.grammar.T7Parser.Atomic_arith_expContext;
import t7.grammar.T7Parser.Atomic_bool_expContext;
import t7.grammar.T7Parser.Bool_arith_expContext;
import t7.grammar.T7Parser.Bool_exp1Context;
import t7.grammar.T7Parser.Bool_exp2Context;
import t7.grammar.T7Parser.Bool_exp3Context;
import t7.grammar.T7Parser.ClearContext;
import t7.grammar.T7Parser.ConditionContext;
import t7.grammar.T7Parser.DelContext;
import t7.grammar.T7Parser.Else_condContext;
import t7.grammar.T7Parser.ExportContext;
import t7.grammar.T7Parser.ExtractContext;
import t7.grammar.T7Parser.For_loopContext;
import t7.grammar.T7Parser.HeaderContext;
import t7.grammar.T7Parser.If_condContext;
import t7.grammar.T7Parser.InstantiationContext;
import t7.grammar.T7Parser.ListContext;
import t7.grammar.T7Parser.LoadContext;
import t7.grammar.T7Parser.RangeContext;
import t7.grammar.T7Parser.SearchContext;
import t7.grammar.T7Parser.StartContext;
import t7.grammar.T7Parser.Structure_definitionContext;
import t7.grammar.T7Parser.TripletContext;
import t7.grammar.T7Parser.Var_definitionContext;
import t7.grammar.T7Parser.While_loopContext;
import t7.statements.ClearStatement;
import t7.statements.DelStatement;
import t7.statements.ElseStatement;
import t7.statements.ExportStatement;
import t7.statements.ExtractStatement;
import t7.statements.ForStatement;
import t7.statements.IfStatement;
import t7.statements.InstanceStatement;
import t7.statements.ListStatement;
import t7.statements.LoadStatement;
import t7.statements.SearchStatement;
import t7.statements.StartStatement;
import t7.statements.Statement;
import t7.statements.StructureDefinitionStatement;
import t7.statements.VariableDefinitionStatement;
import t7.statements.WhileStatement;
import t7.statements.Statement.Types;
import t7.types.StructureTemplate;

/**
 * Walks the parse tree for a statement, returns the statement as an
 * object.
 * @author kinan
 */
public class StatementParser extends T7BaseListener {	
	/**
	 * A Stack containing the arithmetic expressions.
	 */
	private Stack<ArithmeticExpression> arithStack;
	
	/**
	 * A Stack containing the boolean expressions.
	 */
	private Stack<BooleanExpression> boolStack;
		
	/**
	 * The start expression of the last read range.
	 */
	private ArithmeticExpression range_start;
	
	/**
	 * The start expression of the last read range.
	 */
	private ArithmeticExpression range_end;
	
	/**
	 * The start expression of the last read range.
	 */
	private ArithmeticExpression range_step;
	
	/**
	 * The current Statement.
	 */
	private Stack<Statement> statements;
	
	/**
	 * The visited Statement (used for nesting).
	 */
	private HashSet<Statement> visited;
		
	/**
	 * Creates a new Statement Parser.
	 */
	public StatementParser() {
		arithStack = new Stack<ArithmeticExpression>();
		boolStack = new Stack<BooleanExpression>();
		statements = new Stack<Statement>();
		visited = new HashSet<Statement>();
	}
	
	/**
	 * Gets the last parsed statement.
	 * @return the last parsed statement.
	 */
	public Statement getStatement() {
		if(statements.isEmpty()) return null; //Comment.
		
		return statements.pop();
	}
	
	/**
	 * Resets the parser.
	 */
	public void clear() {
		arithStack.clear();
		boolStack.clear();
		statements.clear();
		visited.clear();
		
		range_start = null;
		range_end = null;
		range_step = null;
	}
	
	/*
	// =====================================
	// Core Statements
	// =====================================
	*/
	
	/**
	 * Exiting a structure definitions.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitStructure_definition(Structure_definitionContext ctx) {
		String name = ctx.getChild(1).getText().trim();
		
		int paramCount = (ctx.getChildCount() - 10) / 2;
		paramCount++; //One parameter is guaranteed.
		
		String[] indices = new String[paramCount];
		for(int i = 0; i < paramCount; i++) {
			int child = i*2 + 3;
			indices[i] = ctx.getChild(child).getText().trim();
		}
		
		String definition_body = ctx.getChild(ctx.getChildCount() - 2).getText().trim();
		
		StructureTemplate template = new StructureTemplate(name, indices, definition_body, ctx.getChild(0).getText().trim().startsWith("@"));
		statements.push(new StructureDefinitionStatement(name, template));
	}
		
	/**
	 * Visits a variable definition.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitVar_definition(Var_definitionContext ctx) {
		String name = ctx.identifier().getText();
		Expression expression;
		
		if(range_end != null) { //Value is a triplet.
			expression = new TripletExpression(range_start, range_end, range_step);

			range_start = null;
			range_end = null;
			range_step = null;
		} else if(!arithStack.isEmpty()) { //Value is an arithmetic expression.
			expression = arithStack.pop();
		} else if(!boolStack.isEmpty()) { //Value is a boolean expression.
			expression = boolStack.pop();
		} else { //Value is an identifier.
			expression = new VariableExpression(ctx.value().getText()); //Value is an identifier.
		}
		
		statements.push(new VariableDefinitionStatement(name, expression));
	}
	
	/**
	 * Enter an instantiation of a structure.
	 * @param ctx the parse tree.
	 */
	@Override
	public void enterInstantiation(InstantiationContext ctx) {
		statements.push(new InstanceStatement(ctx.getChild(1).getText().trim()));
	}
		
	/*
	// =====================================
	// Loops and Conditions
	// =====================================
	*/
	
	/**
	 * Visiting a For Loop rule.
	 * @param ctx the parse tree.
	 */
	@Override
	public void enterFor_loop(For_loopContext ctx) {		
		statements.push(new ForStatement(ctx.getChild(1).getText()));
	}
	
	/**
	 * On Exiting the for loop rule, all the statements in its body are parsed and added
	 * to the statements stack. Thus, pop them and add them (in reverse order because LIFO stack)
	 * to the body of the for loop.
	 * Same logic as exiting a while loop.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitFor_loop(For_loopContext ctx) {
		Stack<Statement> bodyStack = new Stack<Statement>(); //Flips the order back to the way it should be.
		
		Statement currentStatement = statements.pop();
		while(currentStatement.getType() != Types.For || visited.contains(currentStatement)) {
			bodyStack.push(currentStatement);
			currentStatement = statements.pop();
		}
		
		ForStatement forStatement = (ForStatement) currentStatement;
		while(!bodyStack.isEmpty()) {
			forStatement.appendBody(bodyStack.pop());
		}
		
		visited.add(forStatement);
		statements.push(forStatement); //return the for statement to the stack.
	}
	
	/**
	 * Visiting a While Loop rule.
	 * @param ctx the parse tree.
	 */
	@Override
	public void enterWhile_loop(While_loopContext ctx) { 
		statements.push(new WhileStatement());
	}
	
	/**
	 * On Exiting the while loop rule, all the statements in its body are parsed and added
	 * to the statements stack. Thus, pop them and add them (in reverse order because LIFO stack)
	 * to the body of the while loop.
	 * Same logic as exiting a for loop.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitWhile_loop(While_loopContext ctx) {
		Stack<Statement> bodyStack = new Stack<Statement>(); //Flips the order back to the way it should be.

		Statement currentStatement = statements.pop();
		while(currentStatement.getType() != Types.While || visited.contains(currentStatement)) {
			bodyStack.push(currentStatement);
			currentStatement = statements.pop();
		}
		
		WhileStatement whileStatement = (WhileStatement) currentStatement;
		while(!bodyStack.isEmpty()) {
			whileStatement.appendBody(bodyStack.pop());
		}
		
		visited.add(whileStatement);
		statements.push(whileStatement); //return the while statement to the stack.
	}
	
	/**
	 * Visit An If Statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void enterIf_cond(If_condContext ctx) {
		statements.push(new IfStatement());
	}
		
	/**
	 * On Exiting the if BODY rule, the if body statements are all on the stack. Thus, pop 
	 * them and add them (in reverse order because LIFO stack)
	 * to the body of the IF.
	 * Same logic as exiting a for/while loop.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitIf_cond(If_condContext ctx) {
		Stack<Statement> bodyStack = new Stack<Statement>(); //Flips the order back to the way it should be.

		Statement currentStatement = statements.pop();
		while(currentStatement.getType() != Types.If || visited.contains(currentStatement)) {
			bodyStack.push(currentStatement);
			currentStatement = statements.pop();
		}
		
		IfStatement ifStatement = (IfStatement) currentStatement;
		while(!bodyStack.isEmpty()) {
			ifStatement.appendIfBody(bodyStack.pop());
		}
		
		visited.add(ifStatement);
		statements.push(ifStatement); //return the if statement to the stack.
	}
	
	/**
	 * Visit An Else Statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void enterElse_cond(Else_condContext ctx) {
		statements.push(new ElseStatement());
	}
	
	/**
	 * On Exiting the else condition rule, the else body statements are all on the stack. Thus, pop 
	 * them and add them (in reverse order because LIFO stack)
	 * to the body of the ELSE in the appropriate If statement..
	 * Same logic as exiting a for/while loop.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitElse_cond(Else_condContext ctx) {
		Stack<Statement> bodyStack = new Stack<Statement>(); //Flips the order back to the way it should be.

		Statement currentStatement = statements.pop();
		while(currentStatement.getType() != Types.Else) {
			bodyStack.push(currentStatement);
			currentStatement = statements.pop();
		}
		
		ElseStatement elseStatement = (ElseStatement) currentStatement;
		while(!bodyStack.isEmpty()) {
			elseStatement.appendElseBody(bodyStack.pop());
		}
		
		//Find the else's if statement without disrupting the stack.
		bodyStack = new Stack<Statement>();
		
		currentStatement = statements.pop();
		while(currentStatement.getType() != Types.If || visited.contains(currentStatement)) {
			bodyStack.push(currentStatement);
			currentStatement = statements.pop();
		}

		//Attach else to its if statement.
		IfStatement ifStatement = (IfStatement) currentStatement;
		ifStatement.setElseStatement(elseStatement);
		
		//Restore Stack (removing the else statement and its body).
		statements.push(ifStatement);
		while(!bodyStack.isEmpty()) {
			statements.push(bodyStack.pop()); //return the if statement to the stack.
		}
	}
		
	/*
	// =====================================
	// Edit - Export - Start Statements.
	// =====================================
	*/
	
	/**
	 * Exiting a clear statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitClear(ClearContext ctx) {
		statements.push(new ClearStatement());
	}
	
	/**
	 * Exiting a list statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitList(ListContext ctx) {
		statements.push(new ListStatement());
	}
	
	/**
	 * Exiting a start statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitStart(StartContext ctx) {
		statements.push(new StartStatement());
	}
	
	/**
	 * Exiting a load statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitLoad(LoadContext ctx) {
		statements.push(new LoadStatement(ctx.getChild(1).getText()));
	}
	
	/**
	 * Exiting a list statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitExport(ExportContext ctx) {
		statements.push(new ExportStatement(ctx.getChild(1).getText()));
	}
	
	/**
	 * Exiting a del statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitDel(DelContext ctx) {
		ArrayList<String> structures = new ArrayList<String>();
		for(HeaderContext c : ctx.header()) 
			structures.add(c.getText());
		
		statements.push(new DelStatement(structures));
	}
	
	/**
	 * Exiting a extract statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitExtract(ExtractContext ctx) {
		HashSet<String> structures = new HashSet<>();
		for(HeaderContext c : ctx.header()) 
			structures.add(c.getText());
		
		statements.push(new ExtractStatement(structures, ctx.getChild(0).toString().trim().startsWith("@")));
	}
	
	/**
	 * Exiting a search statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitSearch(SearchContext ctx) {		
		statements.push(new SearchStatement(ctx.getChild(1).getText()));
	}
	
	/*
	// =====================================
	// Common & Utility Rules.
	// =====================================
	*/
	
	/**
	 * Visits a triplet.
	 * @param ctx parse tree.
	 */
	@Override
	public void exitTriplet(TripletContext ctx) {
		if(ctx.getChildCount() == 3) { //No step.
			range_end = arithStack.pop();
			range_step = null;
			range_start = arithStack.pop();
		} else {
			range_end = arithStack.pop();
			range_step = arithStack.pop();
			range_start = arithStack.pop();
		}
	}
	
	/**
	 * Visits a range rule.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitRange(RangeContext ctx) {
		Expression expression;
		
		if(range_end != null) { //Value is a triplet.
			expression = new TripletExpression(range_start, range_end, range_step);
			
			range_start = null;
			range_end = null;
			range_step = null;
		} else if(!arithStack.isEmpty()) { //Value is an arithmetic expression.
			expression = arithStack.pop();
		} else { //Value is an identifier.
			expression = new VariableExpression(ctx.getChild(0).getText()); //Value is an identifier.
		}
		
		Statement currentStatement = statements.peek();
		if(currentStatement.getType() == Types.InstanceStatement) {
			InstanceStatement instanceStatement = (InstanceStatement) currentStatement;
			if(boolStack.isEmpty()) { //Checks if the range is restricted by a boolean expression.
				instanceStatement.add(expression);
			} else {
				instanceStatement.add(expression, boolStack.pop(), ctx.getChild(0).getText());
			}
		} else if(currentStatement.getType() == Types.For) {
			((ForStatement) currentStatement).setRangeExpression(expression);
		}
	}
	
	/*
	// =====================================
	// Boolean Expression Parsing Section
	// =====================================
	*/
	
	/**
	 * Exits a boolean condition, checks if the current statement is a while or if statement. 
	 * if it is a while loop, or an if condition without a boolean expression, 
	 * the then boolean expression visited is the condition for the while/if 
	 * statement.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitCondition(ConditionContext ctx) {
		//Temporary store popped statements.
		Stack<Statement> storage = new Stack<Statement>();
		while(!statements.isEmpty()) {
			Statement currentStatement = statements.pop();
			storage.push(currentStatement);
			
			if(currentStatement == null) break;
			
			if(currentStatement.getType() == Types.While && !visited.contains(currentStatement)) {
				WhileStatement whileStatement = (WhileStatement) currentStatement;
				whileStatement.setLoopCondition(boolStack.pop());
				break;
			} else if(currentStatement.getType() == Types.If && !visited.contains(currentStatement)) {
				IfStatement ifStatement = (IfStatement) currentStatement;
				ifStatement.setCondition(boolStack.pop());
				break;
			}
		}
		
		//Restore
		while(!storage.isEmpty()) statements.push(storage.pop());
	}

	/**
	 * A boolean arithmetic expression, i.e. two arithmetic expression with a comparator.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitBool_arith_exp(Bool_arith_expContext ctx) {
		String comparator = ctx.getChild(1).getText();
		
		ArithmeticExpression right = arithStack.pop();
		BooleanExpression arith = new BooleanExpression(arithStack.pop(), right, comparator.trim());
		boolStack.push(arith);
	}
	
	/**
	 * An atomic Arithmetic expression: integer, variable, parenthesis, or a negation of an atomic expression.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitAtomic_bool_exp(Atomic_bool_expContext ctx) {
		if(ctx.getChildCount() == 3) return; //Parenthesized expression, don't do anything.
		if(ctx.getChildCount() == 2) { //not expression
			boolStack.peek().negate();
			return;
		}
				
		String text = ctx.getText().trim();
		if(text.equals("true") || text.equals("false")) { //literal
			boolean val = Boolean.parseBoolean(text);
			boolStack.push(new BooleanExpression(val));
		}
		
		else if(!(text.contains("=") || text.contains(">") || text.contains("<"))) { //Boolean var
			boolStack.push(new BooleanExpression(text));
		} //Else => Arithmetic boolean expression, do not do anything.
	}
	
	@Override
	public void exitBool_exp1(Bool_exp1Context ctx) {
		if(ctx.getChildCount() == 1) return;
		booleanExpression(ctx.getChild(1).getText());
	}
	
	@Override
	public void exitBool_exp2(Bool_exp2Context ctx) {
		if(ctx.getChildCount() == 1) return;
		booleanExpression(ctx.getChild(1).getText());
	}
	
	@Override
	public void exitBool_exp3(Bool_exp3Context ctx) {
		if(ctx.getChildCount() == 1) return;
		booleanExpression(ctx.getChild(1).getText());
	}
	
	/**
	 * This is called to finalize parsing an boolean expression with the given operator.
	 * @param operator the operator in the expression.
	 */
	public void booleanExpression(String operator) {
		BooleanExpression right = boolStack.pop();
		BooleanExpression arith = new BooleanExpression(boolStack.pop(), right, operator.trim());
		boolStack.push(arith);
	}
	
	/*
	// =====================================
	// Arithmetic Expression Parsing Section
	// =====================================
	*/
	
	/**
	 * An atomic Arithmetic expression: integer, variable, parenthesis, or a negation of an atomic expression.
	 * @param ctx the parse tree.
	 */
	@Override
	public void exitAtomic_arith_exp(Atomic_arith_expContext ctx) {
		if(ctx.getChildCount() == 3) return; //Parenthesized expression, don't do anything.
		if(ctx.getChildCount() == 2) {
			arithStack.peek().negate();
			return;
		}
		
		String text = ctx.getText().trim();
		if(text.matches("^\\d+$")) { //integer
			int val = Integer.parseInt(text);
			arithStack.push(new ArithmeticExpression(val));
		} else { //variable
			arithStack.push(new ArithmeticExpression(text));
		}
	}
	
	@Override
	public void exitArith_exp1(Arith_exp1Context ctx) {
		if(ctx.getChildCount() == 1) return;
		arithmeticExpression(ctx.getChild(1).getText());
	}
	
	@Override
	public void exitArith_exp2(Arith_exp2Context ctx) {
		if(ctx.getChildCount() == 1) return;
		arithmeticExpression(ctx.getChild(1).getText());
	}
	
	@Override
	public void exitArith_exp3(Arith_exp3Context ctx) {
		if(ctx.getChildCount() == 1) return;
		arithmeticExpression(ctx.getChild(1).getText());
	}
	
	/**
	 * This is called to finalize parsing an arithmetic expression with the given operator.
	 * @param operator the operator in the expression.
	 */
	public void arithmeticExpression(String operator) {
		ArithmeticExpression right = arithStack.pop();
		ArithmeticExpression arith = new ArithmeticExpression(arithStack.pop(), right, operator.trim());
		arithStack.push(arith);
	}
}
