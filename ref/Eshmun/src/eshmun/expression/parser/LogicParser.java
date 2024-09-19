package eshmun.expression.parser;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.modalities.BinaryCTLModality;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.AWModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.binary.EWModality;
import eshmun.expression.modalities.unary.AFModality;
import eshmun.expression.modalities.unary.AGModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EFModality;
import eshmun.expression.modalities.unary.EGModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.ImplicationOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.parser.antlr.LogicExpressionBaseListener;
import eshmun.expression.parser.antlr.LogicExpressionLexer;
import eshmun.expression.parser.antlr.LogicExpressionParser;
import eshmun.expression.parser.antlr.LogicExpressionParser.AndOpContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.BinaryCTLContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.EquivalenceOpContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.ImplicationOpContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.LiteralContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.NotOpContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.OrOpContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.ParenthesizedExpressionContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.UnaryCTLContext;
import eshmun.expression.parser.antlr.LogicExpressionParser.VariableContext;
import eshmun.expression.visitor.visitors.SetValuationVisitor;

/**
 * This class parses a LogicExpression (given as a String).
 * 
 * <p>The parser will parse the expression into a tree-like structure of AbstractExpressions and its subclasses.</p>
 * 
 * <p>The parsing follows the grammar specified in eshmun.expression.parser.antlr.LogicExpression.g4</p>
 * 
 * <p>Details about the syntax:
 * <ul>
 * 	<li>The syntax is case-sensitive.</li>
 * 	<li>Boolean Variable: a Boolean Variable starts with a letter, can contain letters, underscores, and numbers.<br>
 * 			It cannot be equal to 'A', 'E', 'U', 'W', 'V', 'AG', 'AF', 'AX', 'EG', 'EF', 'EX', 'true', 'false'.
 * 	</li>
 * 	<li>Boolean Literal: 'true' or 'false'.</li>
 * 	<li>CTL Operators: Split into two types: <ol>
 * 		<li>Unary CTL Operator: 'AG', 'AF', 'AX', 'EG', 'EF', 'EX'. <br>
 * 			should be written as operator(expression). For example: AG(q =&gt; p).
 * 		</li>
 * 		<li>Binary CTL Operator: 'AU', 'EU', 'AW', 'EW', 'AV', 'EV'. <br>
 * 			should be written as quantifier(expression Modality expression). For example: A(q U p).
 * 		</li>
 * 	</ol></li>
 * 	<li>Boolean Operator: <ol>
 * 		<li>Equivalence Operator: &lt;=&gt; <br>
 * 			Commutative and associative: P &lt;=&gt; Q &lt;=&gt; T is the same as Q &lt;=&gt; T &lt;=&gt; P. <br>
 * 			However, Notice that P &lt;=&gt; Q &lt;=&gt; T means (P &lt;=&gt; Q) and Q &lt;=&gt; T). <br>
 * 			While P &lt;=&gt; (Q &lt;=&gt; T) means P &lt;=&gt; (X) where X is the result of Q &lt;=&gt; T. 
 * 		</li>
 * 		<li>Implication Operator: =&gt;<br>
 * 			Implications can be chained, for example P =&gt; Q =&gt; T, is equivalent to P =&gt; (Q =&gt; T).
 * 		</li>
 * 		<li>And Operator: &amp;<br>
 * 			For example: P &amp; Q &amp; T. and is commutative.
 * 		</li>
 * 		<li>Or Operator: |<br>
 * 			For example: P | Q | T. and is commutative.
 * 		</li>
 * 		<li>Not Operator: !<br>
 * 			For example !P, !(P &amp; Q), ...
 * 		</li>
 * 	</ol></li>
 * <li>Parenthesized Expression: Any of the above types surrounded by parenthesis.</li>
 * </ul> 
 * </p>
 * 
 * <p>Operator Precedence: From Lowest to highest<ul>
 * 	<li>Equivalence: if an expression has an equivalence or more not surrounded 
 * 		by parenthesis then the whole expression is guaranteed to be an equivalence. <br>
 * 		For example: P &amp; Q &lt;=&gt; !Q &lt;=&gt; AG(X) | AG =&gt; x is equivalent to (P &amp; Q) &lt;=&gt; (!Q) &lt;=&gt; (AG(X) | AG =&gt; x)
 * 	</li>
 * 	<li>Implication: has the second lowest precedence after equivalence. The scope of the Implication extends
 * 		(left and right) until the nearest un-parenthesized equivalence. <br>
 * 		For example : P &amp; Q | AG(X) =&gt; S | Q =&gt; X is equivalent to (P &amp; Q | AG(X)) =&gt; ( (S | Q) =&gt; X )
 * 	</li>
 * 	<li>And: When having a sequence of ands and ors, each consecutive sequence of or is grouped as a term in an and. <br>
 * 		For example : P &amp; Q &amp; Z | Y | X &amp; N &amp; M | X &amp; S is equivalent to (P) &amp; (Q) &amp; (Z | Y | X) &amp; (N) &amp; (M | X) &amp; (S)
 * 	</li>
 * 	<li>Or: Cannot contain any other boolean operator except for not (unless operators were parenthesized). <br>
 * 		For example : P &amp; Q | X | !Y | (X &amp; P) is equivalent to (P) &amp; ((Q) | (X) | (!Y) | (X &amp; P))
 * 	</li>
 * 	<li>Not: Highest precedence among boolean operators. Tends to be atomic unless attached to a parenthesized expression. <br>
 * 		!X &amp; Y =&gt; !Q &amp; !(Q &lt;=&gt; K) is equivalent to ( (!(X)) &amp; (Y) ) =&gt; ( (!(Q)) &amp; ( !(Q &lt;=&gt; K) ) )
 * 	</li>
 * 	<li>CTL Operators: the scope of any CTL Operator is only within the parenthesized expression attached to it. <br>
 * 		AG(X =&gt; Q &amp; AF(K)) =&gt; Q is equivalent to ( AG( (X) =&gt; ( Q &amp; ( AF(K) )) ) ) =&gt; Q <br>
 * 		Inside the scope of the CTL Operator the same order of precedence is respected.
 * 	</li>
 * 	<li>Parenthesized Expression: has the highest precedence, used in the usual way.</li>
 * </ul></p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.expression.ExpressionType
 */
public class LogicParser extends LogicExpressionBaseListener {
	/**
	 * The ErrorListener, listens to syntax error, stores the character number's where error were found.
	 */
	LogicErrorListener errorListener;
	
	/**
	 * Stack used in extracting AbstractExpression from ParseTree.
	 * 
	 * <p>Since the Listener works in a DFS-like fashion, a stack can be used upon 
	 * entry to a rule to mark that event, then upon exit from a rule, all the nodes
	 * in the stack up until the marker are children of the expression related to the
	 * current rule.</p>
	 * 
	 * <p>After the parse tree walking is finished (i,e, the listener exited the top rule).
	 * There will be only one child left in the stack, which is the expression representative
	 * of the given string formula.</p>
	 * 
	 * @see eshmun.expression.AbstractExpression
	 */
	private Stack<AbstractExpression> stack;
	
	/**
	 * Creates a new LogicParser that extracts an AbstractExpression from a string satisfying grammar 
	 * eshmun.expression.parser.antlr.LogicExpression.g4
	 * 
	 * @see eshmun.expression.AbstractExpression 
	 */
	public LogicParser() {
		stack = new Stack<AbstractExpression>();
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly this rule's type) is added to the stack,
	 * then popped on exit.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.EquivalenceOperator
	 */
	@Override
	public void enterEquivalenceOp(EquivalenceOpContext ctx) {
		stack.push(new EquivalenceOperator());
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of this rule's type) is reached. By Construction, all the expressions
	 * popped are children of this expression. This expression is then pushed to
	 * the stack to ensure it gets to its parent as well.</p>
	 * 
	 * <p>If an EquivalenceOperator with valuation true (i.e. marked as simplifiable) 
	 * was reached, it gets simplified and added to the current equivalence, and the 
	 * process continues.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.EquivalenceOperator
	 */
	@Override
	public void exitEquivalenceOp(EquivalenceOpContext ctx) {
		ArrayList<AbstractExpression> childrenList = new ArrayList<AbstractExpression>();
		
		AbstractExpression current = stack.pop(); //Get all children (up until the first AndOperator)
		boolean simplifiable = current.getValuation() == null ? false : current.getValuation().booleanValue();
		while(current.getType() != ExpressionType.EquivalenceOperator || simplifiable) {
			if(current.getType() == ExpressionType.EquivalenceOperator) { //marked as simplifiable
				current = current.simplify();
			}
			
			childrenList.add(current);
			
			current = stack.pop();
			simplifiable = current.getValuation() == null ? false : current.getValuation().booleanValue();
		}
		
		boolean hasTwoChildren = (childrenList.size() >= 2);
		assert hasTwoChildren; //Sanity check
		
		AbstractExpression[] children = new AbstractExpression[childrenList.size()];
		for(int j = childrenList.size() - 1; j >= 0; j--) { //reverse to look the same as given formula
			children[j] = childrenList.get(childrenList.size() - j - 1);
		}
		
		EquivalenceOperator orOp = new EquivalenceOperator(children);
		stack.push(orOp);
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly this rule's type) is added to the stack,
	 * then popped on exit.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.ImplicationOperator
	 */
	@Override
	public void enterImplicationOp(ImplicationOpContext ctx) {
		stack.push(new ImplicationOperator(new BooleanVariable(), new BooleanVariable()));
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of this rule's type) is reached. By Construction, all the expressions
	 * popped are children of this expression. This expression is then pushed to
	 * the stack to ensure it gets to its parent as well.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.ImplicationOperator
	 */
	@Override
	public void exitImplicationOp(ImplicationOpContext ctx) {
		AbstractExpression rightChild = stack.pop();
		AbstractExpression leftChild = stack.pop();
		
		boolean type = stack.pop().getType() == ExpressionType.ImplicationOperator;
		assert type; //Sanity check
		
		ImplicationOperator impOp = new ImplicationOperator(leftChild, rightChild);	
		stack.push(impOp);
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly this rule's type) is added to the stack,
	 * then popped on exit.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	@Override
	public void enterAndOp(AndOpContext ctx) {
		stack.push(new AndOperator());
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of this rule's type) is reached. By Construction, all the expressions
	 * popped are children of this expression. This expression is then pushed to
	 * the stack to ensure it gets to its parent as well.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	@Override
	public void exitAndOp(AndOpContext ctx) {
		ArrayList<AbstractExpression> childrenList = new ArrayList<AbstractExpression>();
		
		AbstractExpression current = stack.pop(); //Get all children (up until the first AndOperator)
		while(current.getType() != ExpressionType.AndOperator) {
			childrenList.add(current);
			current = stack.pop();
		}
		
		boolean hasTwoChildren = (childrenList.size() >= 2);
		assert hasTwoChildren; //Sanity check
		
		AbstractExpression[] children = new AbstractExpression[childrenList.size()];
		for(int j = childrenList.size() - 1; j >= 0; j--) { //reverse to look the same as given formula
			children[j] = childrenList.get(childrenList.size() - j - 1);
		}
		
		AndOperator orOp = new AndOperator(children);
		stack.push(orOp);
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly this rule's type) is added to the stack,
	 * then popped on exit.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 */
	@Override
	public void enterOrOp(OrOpContext ctx) {
		stack.push(new OrOperator());
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of this rule's type) is reached. By Construction, all the expressions
	 * popped are children of this expression. This expression is then pushed to
	 * the stack to ensure it gets to its parent as well.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 */
	@Override
	public void exitOrOp(OrOpContext ctx) {
		ArrayList<AbstractExpression> childrenList = new ArrayList<AbstractExpression>();
		
		AbstractExpression current = stack.pop(); //Get all children (up until the first OrOperator)
		while(current.getType() != ExpressionType.OrOperator) {
			childrenList.add(current);
			current = stack.pop();
		}
		
		boolean hasTwoChildren = (childrenList.size() >= 2);
		assert hasTwoChildren; //Sanity check
		
		AbstractExpression[] children = new AbstractExpression[childrenList.size()];
		for(int j = childrenList.size() - 1; j >= 0; j--) { //reverse to look the same as given formula
			children[j] = childrenList.get(childrenList.size() - j - 1);
		}
		
		OrOperator orOp = new OrOperator(children);
		stack.push(orOp);
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly this rule's type) is added to the stack,
	 * then popped on exit.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public void enterNotOp(NotOpContext ctx) {
		stack.push(new NotOperator(null));
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of this rule's type) is reached. By Construction, all the expressions
	 * popped are children of this expression. This expression is then pushed to
	 * the stack to ensure it gets to its parent as well.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public void exitNotOp(NotOpContext ctx) {
		AbstractExpression child = stack.pop();
		
		boolean type = stack.pop().getType() == ExpressionType.NotOperator;
		assert type; //Sanity check

		NotOperator notOp = new NotOperator(child);
		
		stack.push(notOp);
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly AFModality) is added to the stack,
	 * then popped on exit. The exact type of the modality
	 * is evaluated later.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.modalities.UnaryCTLModality
	 * @see eshmun.expression.modalities.unary.AFModality
	 */
	@Override
	public void enterUnaryCTL(UnaryCTLContext ctx) {
		stack.push(new AFModality(null));
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of type AFModality) is reached. By Construction, all the expressions
	 * popped are children of the resulting expression.</p>
	 * 
	 * <p>The exact type of this expression (which Modality) is then evaluated,
	 * and the children are added to it appropriately.</p>
	 * 
	 * <p>This expression is then pushed to the stack to ensure it gets to 
	 * its parent as well.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.modalities.UnaryCTLModality
	 * @see eshmun.expression.modalities.unary.AFModality
	 * @see eshmun.expression.modalities.unary.AGModality
	 * @see eshmun.expression.modalities.unary.AXModality
	 * @see eshmun.expression.modalities.unary.EFModality
	 * @see eshmun.expression.modalities.unary.EGModality
	 * @see eshmun.expression.modalities.unary.EXModality
	 */
	@Override
	public void exitUnaryCTL(UnaryCTLContext ctx) {
		AbstractExpression child = stack.pop();
		
		boolean type = stack.pop().getType() == ExpressionType.AFModality;
		assert type; //Sanity check
		
		UnaryCTLModality modality = null;
		
		String modalityType = ctx.getChild(0).getText().trim();
		switch(modalityType) {
		case "AF":
			modality = new AFModality(child);
			break;
		case "AG":
			modality = new AGModality(child);
			break;
		case "AX":
			modality = new AXModality(child);
			break;
		case "EF":
			modality = new EFModality(child);
			break;
		case "EG":
			modality = new EGModality(child);
			break;
		case "EX":
			modality = new EXModality(child);
			break;
		default: //Maybe Process Indices
			if(modalityType.startsWith("AX_{")) { //AX_{i}
				String processIndex = modalityType.substring(4, modalityType.length() - 1);
				modality = new AXModality(child, processIndex);
				
			} else if(modalityType.startsWith("EX_{")) { //EX_{i}
				String processIndex = modalityType.substring(4, modalityType.length() - 1);
				modality = new EXModality(child, processIndex);
			}
		}
		
		boolean notNull = (modality != null);
		assert notNull; //Sanity check
		
		stack.push(modality);
	}
	
	/**
	 * Marks the entry to this rule. 
	 * 
	 * <p>To mark the entry an empty AbstractExpresssion
	 * (particularly AUModality) is added to the stack,
	 * then popped on exit. The exact type of the modality
	 * is evaluated later.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.modalities.BinaryCTLModality
	 * @see eshmun.expression.modalities.binary.AUModality
	 */
	@Override
	public void enterBinaryCTL(BinaryCTLContext ctx) {
		stack.push(new AUModality(null, null));
	}
	
	/**
	 * Marks the exit of this rule.
	 * 
	 * <p>All the expression in the stack are popped until an AbstractExpression 
	 * (of type AUModality) is reached. By Construction, all the expressions
	 * popped are children of the resulting expression.</p>
	 * 
	 * <p>The exact type of this expression (which Modality) is then evaluated,
	 * and the children are added to it appropriately.</p>
	 * 
	 * <p>This expression is then pushed to the stack to ensure it gets to 
	 * its parent as well.</p>
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.modalities.BinaryCTLModality
	 * @see eshmun.expression.modalities.binary.AUModality
	 * @see eshmun.expression.modalities.binary.AWModality
	 * @see eshmun.expression.modalities.binary.AVModality
	 * @see eshmun.expression.modalities.binary.EUModality
	 * @see eshmun.expression.modalities.binary.EWModality
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	@Override
	public void exitBinaryCTL(BinaryCTLContext ctx) {
		AbstractExpression rightChild = stack.pop();
		AbstractExpression leftChild = stack.pop();
		
		boolean type = stack.pop().getType() == ExpressionType.AUModality;
		assert type; //Sanity check
		
		BinaryCTLModality modality = null;
		String pathQuantifier = ctx.getChild(0).getText().trim(); //A, E
		String modalityType = ctx.getChild(3).getText().trim(); //U, V, W
		switch(modalityType) {
		case "U":
			if(pathQuantifier.equals("A")) 
				modality = new AUModality(leftChild, rightChild);
			
			if(pathQuantifier.equals("E")) 
				modality = new EUModality(leftChild, rightChild);
			else
				assert false; //Sanity check
			
			break;
		case "W":
			if(pathQuantifier.equals("A")) 
				modality = new AWModality(leftChild, rightChild);
			
			if(pathQuantifier.equals("E")) 
				modality = new EWModality(leftChild, rightChild);
			else
				assert false; //Sanity check
			
			break;
		case "R":
		case "V":
			if(pathQuantifier.equals("A")) 
				modality = new AVModality(leftChild, rightChild, modalityType);
			
			if(pathQuantifier.equals("E")) 
				modality = new EVModality(leftChild, rightChild, modalityType);
			else
				assert false; //Sanity check
			
			break;
		}		
		
		boolean notNull = (modality != null);
		assert notNull; //Sanity check
		
		stack.push(modality);
	}
	
	/**
	 * Pushes this literal to the stack.
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.atomic.BooleanLiteral
	 */
	@Override
	public void enterLiteral(LiteralContext ctx) {
		String stringValue = ctx.getText().trim();
		boolean value = stringValue.equals("true") ? true : false;
		stack.push(new BooleanLiteral(value));
	}
	
	/**
	 * Pushes this variable to the stack.
	 * 
	 * @param ctx sub-parse tree with this rule as root
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	@Override
	public void enterVariable(VariableContext ctx) {
		String variableName = ctx.getText().trim();
		stack.push(new BooleanVariable(variableName));
	}
		
	/**
	 * Useful to differentiate between the two types of Equivalence chaining.
	 * 
	 * <p>The two types of equivalence chaining: <ul>
	 * <li><b>No Parenthesis:</b> <br>
	 * P &lt;=&gt; Q &lt;=&gt; T is evaluated as: (P &lt;=&gt; Q) &amp; (Q &lt;=&gt; T).
	 * </li>
	 * <li><b>With Parenthesis:</b> <br>
	 * P &lt;=&gt; (Q &lt;=&gt; T) is evaluated as: P &lt;=&gt; ((Q =&gt; T) &amp; (T =&gt; Q))
	 * </li>
	 * </ul></p>
	 * 
	 * <p>Since this marks the exit of a ParenthesizedExpressionRule, then by
	 * construction, its child is the top element in the stack.</p>
	 * 
	 * <p>The child is popped, if the child was an equivalence operator, it is 
	 * marked as simplifiable (by setting its valuation to true), if it was used
	 * later in another equivalence it gets simplified.</p>
	 * 
	 * @see eshmun.expression.operators.EquivalenceOperator
	 */
	@Override
	public void exitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
		
		AbstractExpression child = stack.pop();
		if(child.getType() == ExpressionType.EquivalenceOperator) {
			child.setValuation(true);
		}
		
		stack.push(child);
	}
	
	/**
	 * parse a string formula into an hierarchy of nested AbstractExpressions, returns the root 
	 * AbstractExpression.
	 * 
	 * @param formula string containing the formula to be parsed (syntax is determined above).
	 * @return an AbstractExpression equivalent to the given formula, null if syntax errors where found.
	 */
	public AbstractExpression parse(String formula) {
		formula = formula.replace(":=", "=");
		
		LogicExpressionLexer lexer = new LogicExpressionLexer(new ANTLRInputStream(formula));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LogicExpressionParser parser = new LogicExpressionParser(tokens);
		
		errorListener = new LogicErrorListener();
		parser.addErrorListener(errorListener);
		
		ParseTree tree = parser.expression(); // parse 
		
		if(parser.getNumberOfSyntaxErrors() > 0) {
			return null;
		}
		
		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker 
		walker.walk(this, tree); // initiate walk of tree with listener
		
		AbstractExpression expression = stack.pop();
		
		SetValuationVisitor svv = new SetValuationVisitor(expression);
		svv.setValuation(null); //reset valuation, in case some equivalences were marked.
		
		return expression;
	}
	
	/**
	 * Gets the character numbers where syntax error were found.
	 * 
	 * @return array with character numbers where syntax error were found, 
	 * null if no syntax error exist or no parsing yet took place.
	 */
	public Integer[] getSyntaxErrorCharacters() {
		if(errorListener != null) {
			return errorListener.getErrorCharacters();
		}
		
		return null;
	}
	
	
	/**
	 * ErrorListener, Listens to syntax errors during parsing, stores the character number of each error.
	 * 
	 * @author Kinan Dak Al Bab
	 * @since 1.0
	 */
	public static class LogicErrorListener implements ANTLRErrorListener {
		/**
		 * Store each syntax error character number.
		 */
		ArrayList<Integer> chars = new ArrayList<Integer>();
		
		/**
		 * listens to syntax errors.
		 * 
		 * @param arg0 recognizer of the error
		 * @param arg1 token of the error
		 * @param arg2 line number
		 * @param arg3 character number
		 * @param arg4 the text
		 * @param arg5 the thrown exception
		 */
		@Override
		public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2,
				int arg3, String arg4, RecognitionException arg5) {
			chars.add(arg3 - 1);
		}

		/**
		 * Does nothing, shown here for completeness
		 */
		@Override
		public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2,
				int arg3, int arg4, ATNConfigSet arg5) {}
		/**
		 * Does nothing, shown here for completeness
		 */
		@Override
		public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2,
				int arg3, BitSet arg4, ATNConfigSet arg5) {}
		
		/**
		 * Does nothing, shown here for completeness
		 */
		@Override
		public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3,
				boolean arg4, BitSet arg5, ATNConfigSet arg6) {}
		
		/**
		 * Checks whether this listener listened to any syntax errors.
		 * @return true if syntax errors were encountered, false otherwise.
		 */
		public boolean hasError() {
			return chars.size() > 0;
		}
		
		/**
		 * Gets the character numbers where syntax error were found.
		 * 
		 * @return array with character numbers where syntax error were found,
		 * null if no errors where found.
		 */
		public Integer[] getErrorCharacters() {
			if(hasError()) 
				return chars.toArray(new Integer[chars.size()]);
			
			return null;
		}
	}
	
	/**
	 * Remove needless parenthesis, i.e. parenthesis that surround other parenthesis (like ((A)), ((((b + c)))) ).
	 * @param formula the formula to remove needless parenthesis from.
	 * @return the new compact formula.
	 */
	public String reduceParenthesis(String formula) {
		//Remove white spaces.
		formula = formula.replaceAll("\\s", " ");
				
		while(formula.contains("  ")) 
			formula = formula.replace("  ", " ");
		
		formula = formula.replace(" )", ")").replace(") ", ")");
		formula = formula.replace("( ", "(").replace(" (", "(");
				
		formula = "("+formula+")";
		
		//Build parenthesis array
		int[] array = new int[formula.length()];
		Stack<Integer> indices = new Stack<Integer>();
		for(int i = 0; i < formula.length(); i++) {
			char c = formula.charAt(i);
			
			if(c == '(') {
				array[i] = 1;
				indices.push(i);
			}
			
			if(c == ')') {
				array[i] = -1;
				int index = indices.pop();
				array[index] = i;
			}
		}
				
		//Remove doubled parenthesis
		String s = "";
		for(int i = 0; i < array.length; i++) {
			if(array[i] == 0) s += formula.charAt(i);
			
			if(array[i] > 0) {
				if(i == array.length - 1) continue;
				int test = array[i+1];
				if(array[i] - test > 1) {
					array[array[i]] = 0;
					s += formula.charAt(i);
				}
			}
		}
		
		//Re construct parenthesis array for the new formatted string
		array = new int[s.length()];
		indices = new Stack<Integer>();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if(c == '(') {
				array[i] = 1;
				indices.push(i);
			}
			
			if(c == ')') {
				array[i] = -1;
				int index = indices.pop();
				array[index] = i;
			}
		}
		
		//Figure out dominating operators in each term
		String[] dominatingOp = new String[s.length()];
		for(int i = 0; i < s.length(); i++) {
			int index = array[i];
			if(array[i] > 0) { //left parenthesis
				String compare = s.substring(i+1, array[i]);
				
				int count = 0;
				for(int  j = i + 1; j < index; j++) {
					if(compare.startsWith("(")) {
						count++;
					} else if(compare.startsWith(")")) {
						count--;
					} else if(count == 0) {
						if(compare.startsWith("&")) {
							dominatingOp[i] = "&";
							dominatingOp[array[i]] = "-&";
							break;
						} else if(compare.startsWith("|")) {
							dominatingOp[i] = "|";
							dominatingOp[array[i]] = "-|";
							break;
						} else if(compare.startsWith("<=>")) {
							dominatingOp[i] = "<=>";
							dominatingOp[array[i]] = "-<=>";
							break;
						} else if(compare.startsWith("=>")) {
							dominatingOp[i] = "=>";
							dominatingOp[array[i]] = "-=>";
							break;
						}
					}
					
					if(compare.length() > 1) {
						compare = compare.substring(1);
					}
				}
			} else if(s.charAt(i) == '!') {
				dominatingOp[i] = "!";
				if(i + 1 < s.length() && s.charAt(i + 1) == ' ') {
					dominatingOp[i+1] = "!";
				}
			}
		}
		
		//fill the rest of indices
		Stack<String> ops = new Stack<String>();
		String lastOp = "";
		for(int i = 0; i < dominatingOp.length; i++) {
			if(dominatingOp[i] != null && !dominatingOp[i].equals("!")) {
				if(dominatingOp[i].startsWith("-")) {
					if(!ops.isEmpty())
						lastOp = ops.pop();
					else lastOp = "";
				} else {
					if(!lastOp.isEmpty()) {
						ops.push(lastOp);
					}
					
					lastOp = dominatingOp[i];
				}
			} else if(dominatingOp[i] == null) {
				dominatingOp[i] = lastOp;
			}
		}
		
		//Remove useless parenthesis from array by giving them -2
		for(int i = 0; i < dominatingOp.length - 1; i++) {
			String d = dominatingOp[i];
			if(d.equalsIgnoreCase("!") || d.startsWith("-")) continue;
			
			if(d.equals(dominatingOp[i+1])) {
				if(array[i+1] > 0) {
					int index = array[i+1];
					array[i+1] = -2;
					array[index] = -2;
				}
			}
		}
		
		//turn into a new string
		String result = "";
		for(int i = 0; i < array.length; i++) {
			if(array[i] == 0) result += s.charAt(i);
			
			if(array[i] > 0) {
				if(i == array.length - 1) continue;
				int test = array[i+1];
				if(array[i] - test > 1) {
					array[array[i]] = 0;
					result += s.charAt(i);
				}
			}
		}
		
		
		return result;
	}
}
