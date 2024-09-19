package eshmun.expression.guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import eshmun.expression.AbstractExpression;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.parser.LogicParser;
import eshmun.expression.parser.LogicParser.LogicErrorListener;
import eshmun.expression.parser.antlr.LogicExpressionBaseListener;
import eshmun.expression.parser.antlr.LogicExpressionLexer;
import eshmun.expression.parser.antlr.LogicExpressionParser;
import eshmun.expression.parser.antlr.LogicExpressionParser.AndGuardExpressionContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.OrGuardExpressionContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.SingleGuardContext;

/**
 * An Abstract Guard Expression; Could be either an AtomicGuardExpression or OperatedGuardExpression (And or Or).
 * @author kinan
 *
 */
public abstract class GuardExpression {
	
	/**
	 * @param variable the variable to look for (using semantic equals).
	 * @return true if the given variable is referenced inside the expression (Assignment or guard).
	 */
	public abstract boolean containsVariable(BooleanVariable variable);

	/**
	 * @param variableName the name of the variable to look for.
	 * @return true if the given variable is referenced inside the expression (Assignment or guard).
	 */
	public abstract boolean containsVariable(String variableName);

	/**
	 * Simplifies the guard. Turns the guard into the following format:
	 * ( ( Guard1 -&gt; Assignment 1 (OR) Guard2 -&gt; Assignment 2 (OR) ... ) (AND) ( Guard3 -&gt; Asssignment3 (OR) ...) (AND) ... ) 
	 * 
	 * This also combines expression that have the same guard or the same assignments.
	 * @return the simplified guard as explained.
	 */
	public abstract GuardExpression simplify();
	
	/**
	 * Given the current state of processes and shared variables, determine if this guard
	 * is satisfied.
	 * @param state a collection of labels and shared variables (with assignments).
	 * @param processes a list of considered process indices, any boolean variable
	 * that does not belong to these processes is not considered.
	 * @return a set of effects and action names. Actions begin with "@".
	 * 			The outer set represents a "disjunction", in other words.
	 * 			Each of its elements is the effect of a separate transition.
	 * 			The inner set represents a "conjunction", its elements are
	 * 			concatenated into a single effect.
	 * @throws IllegalStateException if the state does not satisfy the guard.
	 */
	public abstract HashSet<HashSet<String>> satisfyGuard(Collection<String> state, ArrayList<String> processes) throws IllegalStateException;
	
	/**
	 * Equality check.
	 * 
	 * <p>The internals of the implementation of this method depends on the implementing class.</p>
	 * 
	 * <p>Comparison is not done based on pointers rather than based on semantic meaning.
	 * Two Expressions are equal if they consist of the same variables (based on Name), and if 
	 * these variables are connected in the same way.</p>
	 * 
	 * <p>Notice that certain Operators are commutative, this doesn't change the equality of 
	 * two Expression.</p>
	 * 
	 * <p>If the given object is an expression of the same type as this, their contents is directly
	 * compared, if it is of a different expression type, then the given object and this are both simplified,
	 * if their simplifications are of the same type they are compared, otherwise return false.</p>
	 * 
	 * @return true if this and the passed object are semantically equal (as Expressions), false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		
		if(!(obj instanceof GuardExpression))
			return false;
		
		GuardExpression exp = (GuardExpression) obj;
		if(this.getClass() == exp.getClass()) {
			return directCompare(exp);
		}
		
		exp = exp.simplify();
		GuardExpression tmp = simplify();
		if(this.getClass() == exp.getClass())
			return tmp.directCompare(exp);
					
		return false;
	}
	
	/**
	 * Equality check, this method is only called if the passed expression has the same type as this.
	 * 
	 * <p>The internals of the implementation of this method depends on the implementing class.</p>
	 * 
	 * <p>Comparison is not done based on pointers rather than based on semantic meaning.
	 * Two Expressions are equal if they consist of the same variables (based on Name), and if 
	 * these variables are connected in the same way.</p>
	 * 
	 * <p>Notice that certain Operators are commutative, this doesn't change the equality of 
	 * two Expression.</p>
	 * 
	 * @param exp the guard expression to compare this with.
	 * 
	 * @return true if this and the passed object are semantically equal (as Expressions), false otherwise.
	 */
	protected abstract boolean directCompare(GuardExpression exp);

	/**
	 * Clones (Shallow copy) this expression.
	 * @return a Shallow copy of this.
	 */
	@Override
	public abstract GuardExpression clone();

	/**
	 * @return a readable formatted string representation of this expression.
	 */
	public abstract String toString();
	
	/**
	 * @return a readable formatted string representation of this expression
	 * where the operator are represented by unicode representative symbols.
	 */
	public abstract String toSymbolsString();
	
	/**
	 * Visitor pattern on guards.
	 * @param visitor the visitor to run.
	 */
	public abstract void accept(GuardVisitor visitor);
	
	/**
	 * A simple visitor over guard expressions.
	 */
	public static interface GuardVisitor {
		/**
		 * Visits an OrGuardOperator.
		 * @param or the visited operator.
		 */
		public void visit(OrGuardOperator or);
		
		/**
		 * Visits an AndGuardOperator.
		 * @param and the visited operator.
		 */
		public void visit(AndGuardOperator and);
		
		/**
		 * Visits an AtomicGuardExpression.
		 * @param atom the visited atomic expression.
		 */
		public void visit(AtomicGuardExpression atom);
	}
	
	/**
	 * Parse a string into some GuardExpression.
	 * @param guard the guard string to parse.
	 * @return the guard expression.
	 * @exception IllegalArgumentException if the string is not parse-able, the
	 * 							exception message will contain comma-separated error positions only.
	 */
	public static GuardExpression parse(String guard) throws IllegalArgumentException {
		if(guard == null || guard.trim().isEmpty()) return new AtomicGuardExpression();
		guard = guard.replace(":=", "=");
		
		LogicExpressionLexer lexer = new LogicExpressionLexer(new ANTLRInputStream(guard));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LogicExpressionParser parser = new LogicExpressionParser(tokens);
		
		LogicErrorListener errorListener = new LogicErrorListener();
		parser.addErrorListener(errorListener);
		
		ParseTree tree = parser.guardExpression(); // parse 
		if(parser.getNumberOfSyntaxErrors() > 0) { // Throw Parse Error
			Integer[] errors = errorListener.getErrorCharacters();
			String errorsString = errors[0] + "";
			for(int i = 1; i < errors.length; i++) {
				errorsString += ","+errors[i];
			}
			
			throw new IllegalArgumentException(errorsString);
		}
		
		final Stack<GuardExpression> stack = new Stack<>();
		final LogicParser logicParser = new LogicParser();
		LogicExpressionBaseListener listener = new LogicExpressionBaseListener() {
			@Override
			public void enterAndGuardExpression(AndGuardExpressionContext ctx) { stack.push(null); }
			
			@Override
			public void exitAndGuardExpression(AndGuardExpressionContext ctx) {
				AndGuardOperator andGuard = new AndGuardOperator();
				
				LinkedList<GuardExpression> ands = new LinkedList<>();
				GuardExpression current = stack.pop();
				while(current != null) {
					ands.addFirst(current);
					current = stack.pop();
				}
				
				if(ands.size() > 1) {
					for(GuardExpression child : ands)
						andGuard.andGuard(child);
					
					stack.push(andGuard);
				} else { // Or expression is parsed as an and expression with single or child (check grammar).
					stack.push(ands.getFirst());
				}
			}
			
			@Override
			public void enterOrGuardExpression(OrGuardExpressionContext ctx) { stack.push(null); }
			
			@Override
			public void exitOrGuardExpression(OrGuardExpressionContext ctx) {
				OrGuardOperator orGuard = new OrGuardOperator();
				
				LinkedList<GuardExpression> ors = new LinkedList<>();
				GuardExpression current = stack.pop();
				while(current != null) {
					ors.addFirst(current);
					current = stack.pop();
				}
				
				if(ors.size() > 1) {
				for(GuardExpression child : ors)
					orGuard.orGuard(child);
				
				stack.push(orGuard);
				} else { // Atomic expression is parsed as an or expression with single atomic child (check grammar).
					stack.push(ors.getFirst());
				}
			}
			
			@Override
			public void exitSingleGuard(SingleGuardContext ctx) {
				String expressionString = ctx.getChild(0).getText();
				AbstractExpression expression = logicParser.parse(expressionString);
				
				int commCount = (ctx.getChildCount() - 2) / 2;
				commCount = Math.max(commCount, 0);
				
				ArrayList<BooleanVariable> commands = new ArrayList<BooleanVariable>(commCount);
				for(int i = 0; i < commCount; i++) {
					String commString = ctx.getChild(2 + i*2).getText();
					commands.add(new BooleanVariable(commString));
				}
				
				stack.push(new AtomicGuardExpression(expression, commands));
			}		
		};
		
		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker 
		walker.walk(listener, tree); // initiate walk of tree with listener

		return stack.pop();
	}
 }
