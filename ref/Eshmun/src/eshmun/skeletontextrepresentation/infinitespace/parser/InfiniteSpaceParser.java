package eshmun.skeletontextrepresentation.infinitespace.parser;

 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntExpr;

import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateAction;
import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateActionFactory;
import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.BlockStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.IfStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.Statement;
import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement.AssignmentType;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsBaseListener;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsLexer;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ActionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AdditiveoperatorContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AndOrContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ArithOpContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AssignmentStatementContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.AtomicPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BinaryFunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BinaryPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BlockContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BoolLiteralContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.BoundVariableContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ConjunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.DisjunctionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ExprContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.FormulaContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.FreeVariableContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.GlobalEffectContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.GlobalGuardContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IdentifierContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfStatementContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfboolPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfcondTermContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfconditionContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IfpredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.IntialContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LeftIdentifierContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LeftPredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LocalEffectContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LocalGuardContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.LogicTermContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.MultiplicativeoperatorContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.NegationContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.PredicateContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ProcessContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.ProcessnameContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.QuantifiedFormulaContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.QuantifierContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RelOpContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RelationaloperatorContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RighExprContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.RightBoolContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.SignedNumberContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.SskipContext;
import eshmun.skeletontextrepresentation.infinitespace.parser.antlr.FormulasAndStatementsParser.UnaryFunctionContext;

public class InfiniteSpaceParser {

	public static BoolExpr parseFormula(String formula, Context context) {

		FormulasAndStatementsLexer lexer = new FormulasAndStatementsLexer(new ANTLRInputStream(formula));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FormulasAndStatementsParser parser = new FormulasAndStatementsParser(tokens);

		ParseTree tree = parser.formula();
		if (parser.getNumberOfSyntaxErrors() > 0) { // Throw Parse Error

			throw new IllegalArgumentException("Errors in Syntax");
		}

		final Stack<Expr> stack = new Stack<>();

		final Stack<String> operatorStack = new Stack<>();

		FormulasAndStatementsBaseListener listener = new FormulasAndStatementsBaseListener() {

			/*
			 * Formula Parsing
			 * 
			 */

			@Override
			public void exitBinaryFunction(BinaryFunctionContext ctx) {

				ArithExpr expr = null;

				switch (operatorStack.pop()) {

				case "+":
					expr = context.mkAdd((IntExpr) stack.pop(), (IntExpr) stack.pop());
					break;
				case "-":
					expr = context.mkSub((IntExpr) stack.pop(), (IntExpr) stack.pop());
					break;
				case "*":
					expr = context.mkMul((IntExpr) stack.pop(), (IntExpr) stack.pop());
					break;
				case "/":
					expr = context.mkDiv((IntExpr) stack.pop(), (IntExpr) stack.pop());
					break;

				default:
					break;
				}

				stack.push(expr);

			}

			@Override
			public void exitNegation(NegationContext ctx) {

				if (getParserRuleText(ctx).startsWith("!")) {

					stack.push(context.mkNot((BoolExpr) stack.pop()));
				}

			}

			@Override
			public void exitBoundVariable(BoundVariableContext ctx) {

				stack.push(context.mkIntConst((getParserRuleText(ctx).replace("?", ""))));

			}

			@Override
			public void enterDisjunction(DisjunctionContext ctx) {
				stack.push(null);

			}

			@Override
			public void exitDisjunction(DisjunctionContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();
				BoolExpr current = (BoolExpr) stack.pop();
				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) stack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkOr(result, child);
						}

					stack.push(result);
				} else {
					stack.push(expressions.getFirst());
				}

			}

			@Override
			public void exitRelOp(RelOpContext ctx) {

				operatorStack.push(getParserRuleText(ctx));

			}

			@Override
			public void exitAtomicPredicate(AtomicPredicateContext ctx) {
				stack.push(context.mkBoolConst(getParserRuleText(ctx)));
			}

			@Override
			public void exitBinaryPredicate(BinaryPredicateContext ctx) {
				BoolExpr expr = null;

				IntExpr two = (IntExpr) stack.pop();
				IntExpr one = (IntExpr) stack.pop();

				switch (operatorStack.pop()) {

				case "=":
					expr = context.mkEq(one, two);
					break;
				case "!=":
					expr = context.mkNot(context.mkEq(one, two));
					break;
				case "<":
					expr = context.mkLt(one, two);
					break;
				case "<=":
					expr = context.mkLe(one, two);
					break;
				case ">":
					expr = context.mkGt(one, two);
					break;
				case ">=":
					expr = context.mkGe(one, two);
					break;

				default:
					break;
				}

				stack.push(expr);

			}

			@Override
			public void exitArithOp(ArithOpContext ctx) {
				operatorStack.push(getParserRuleText(ctx));

			}

			@Override
			public void exitFreeVariable(FreeVariableContext ctx) {

				stack.push(context.mkIntConst(getParserRuleText(ctx)));

			}

			@Override
			public void enterConjunction(ConjunctionContext ctx) {

				stack.push(null);

			}

			@Override
			public void exitConjunction(ConjunctionContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();
				BoolExpr current = (BoolExpr) stack.pop();

				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) stack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkAnd(result, child);
						}

					stack.push(result);
				} else {
					stack.push(expressions.getFirst());
				}

			}

			@Override
			public void exitQuantifiedFormula(QuantifiedFormulaContext ctx) {

				BoolExpr dis = (BoolExpr) stack.pop();
				IntExpr x = (IntExpr) stack.pop();

				switch (operatorStack.pop().toLowerCase()) {
				case "forall":
					Expr xAll = context.mkForall(new Expr[] { x }, dis, 1, null, null, null, null);
					stack.push(xAll);
					break;

				case "exists":
					Expr xExists = context.mkExists(new Expr[] { x }, dis, 1, null, null, null, null);
					stack.push(xExists);
					break;

				default:
					break;
				}

			}

			@Override
			public void exitQuantifier(QuantifierContext ctx) {
				operatorStack.push(getParserRuleText(ctx));

			}

			private String getParserRuleText(ParserRuleContext ctx) {
				String text;
				int a = ctx.start.getStartIndex();
				int b = ctx.stop.getStopIndex();
				Interval interval = new Interval(a, b);

				CharStream input = ctx.start.getInputStream();

				text = input.getText(interval);
				return text;
			}

		};

		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		walker.walk(listener, tree); // initiate walk of tree with listener

		BoolExpr boolExpr = (BoolExpr) stack.pop();

		System.out.println();
		System.out.println(boolExpr);

		System.out.println(stack.toString());
		System.out.println(operatorStack.toString());

		return boolExpr;

	}

	public static Statement parseStatement(String command, Context context) {

		FormulasAndStatementsLexer lexer = new FormulasAndStatementsLexer(new ANTLRInputStream(command));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FormulasAndStatementsParser parser = new FormulasAndStatementsParser(tokens);

		ParseTree tree = parser.statement();
		if (parser.getNumberOfSyntaxErrors() > 0) { // Throw Parse Error

			throw new IllegalArgumentException("Errors in Syntax");
		}

		final Stack<Statement> statementStack = new Stack<>();
		final Stack<Expr> expressionStack = new Stack<>();
		final Stack<BoolExpr> ifConditionsStack = new Stack<>();
		final Stack<String> operators = new Stack<>();

		FormulasAndStatementsBaseListener listener = new FormulasAndStatementsBaseListener() {

			/*
			 * Block Statements
			 * 
			 */
			@Override
			public void enterBlock(BlockContext ctx) {

				statementStack.push(new BlockStatement());
				statementStack.push(null);

			}

			@Override
			public void exitBlock(BlockContext ctx) {

				Statement statement;

				ArrayList<Statement> children = new ArrayList<>();

				statement = statementStack.pop();
				while (statement != null) {

					children.add(0, statement);
					statement = statementStack.pop();

				}

				BlockStatement bs = (BlockStatement) statementStack.pop();
				bs.children = children;
				statementStack.push(bs);

			}

			/*
			 * If Statements
			 * 
			 */

			@Override
			public void enterIfStatement(IfStatementContext ctx) {

				statementStack.push(new IfStatement());
				statementStack.push(null);

			}

			public void exitIfStatement(IfStatementContext ctx) {

				Statement two = statementStack.pop();
				Statement one = statementStack.pop();

				if (one == null) {
					// If then statement

					IfStatement statement = (IfStatement) statementStack.pop();
					statement.condition = ifConditionsStack.pop();
					statement.thenBody = two;
					statementStack.push(statement);

				} else {
					// If else statement
					statementStack.pop(); // will remove the null
					IfStatement statement = (IfStatement) statementStack.pop();
					statement.condition = ifConditionsStack.pop();
					statement.thenBody = one;
					statement.elseBody = two;

					statementStack.push(statement);

				}

			}

			/*
			 * If Condition
			 * 
			 */

			@Override
			public void exitIfboolPredicate(IfboolPredicateContext ctx) {

				ifConditionsStack.push((BoolExpr) expressionStack.pop());

			}

			@Override
			public void enterIfcondition(IfconditionContext ctx) {
				expressionStack.push(null);

			}

			@Override
			public void exitIfcondition(IfconditionContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();

				BoolExpr current = (BoolExpr) expressionStack.pop();

				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) expressionStack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkOr(result, child);
						}

					expressionStack.push(result);
				} else {
					expressionStack.push(expressions.getFirst());
				}

			}

			@Override
			public void enterIfcondTerm(IfcondTermContext ctx) {
				expressionStack.push(null);

			}

			@Override
			public void exitIfcondTerm(IfcondTermContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();

				BoolExpr current = (BoolExpr) expressionStack.pop();

				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) expressionStack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkAnd(result, child);
						}

					expressionStack.push(result);
				} else {
					expressionStack.push(expressions.getFirst());
				}

			}

			@Override
			public void exitRelationaloperator(RelationaloperatorContext ctx) {

				operators.push(getParserRuleText(ctx));

			}

			@Override
			public void exitIfpredicate(IfpredicateContext ctx) {

				BoolExpr expr = null;

				IntExpr two = (IntExpr) expressionStack.pop();
				IntExpr one = (IntExpr) expressionStack.pop();

				switch (operators.pop()) {

				case "=":
					expr = context.mkEq(one, two);
					break;
				case "!=":
					expr = context.mkNot(context.mkEq(one, two));
					break;
				case "<":
					expr = context.mkLt(one, two);
					break;
				case "<=":
					expr = context.mkLe(one, two);
					break;
				case ">":
					expr = context.mkGt(one, two);
					break;
				case ">=":
					expr = context.mkGe(one, two);
					break;

				default:
					break;
				}

				expressionStack.push(expr);

			}

			/*
			 * Assignments
			 * 
			 */

			@Override
			public void enterAssignmentStatement(AssignmentStatementContext ctx) {
				statementStack.push(new AssignmentStatement());

			}

			@Override
			public void exitLeftIdentifier(LeftIdentifierContext ctx) {
				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.left = expressionStack.pop();
				statementStack.push(statement);

			}

			@Override
			public void exitLeftPredicate(LeftPredicateContext ctx) {
				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.left = context.mkBoolConst(getParserRuleText(ctx));
				statementStack.push(statement);

			}

			@Override
			public void exitRighExpr(RighExprContext ctx) {

				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.right = expressionStack.pop();
				statement.assignmentType = AssignmentType.IntAssignment;
				statementStack.push(statement);
			}

			@Override
			public void exitRightBool(RightBoolContext ctx) {

				String literal = getParserRuleText(ctx);

				if (literal.toLowerCase().contains("t"))
					expressionStack.push(context.mkBool(true));
				else {
					expressionStack.push(context.mkBool(false));
				}

				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.right = expressionStack.pop();
				statement.assignmentType = AssignmentType.BoolAssignment;

				statementStack.push(statement);

			}

			/*
			 * Expressions
			 * 
			 */

			@Override
			public void enterExpr(ExprContext ctx) {

				operators.push(null);

			}

			@Override
			public void exitExpr(ExprContext ctx) {

				if (operators.peek() == null) {
					operators.pop();

					return;
				}

				if (!operators.isEmpty() && expressionStack.size() > 1) {

					IntExpr two = (IntExpr) expressionStack.pop();
					IntExpr one = (IntExpr) expressionStack.pop();
					switch (operators.pop()) {
					case "+":
						expressionStack.push(context.mkAdd(one, two));
						break;
					case "-":
						expressionStack.push(context.mkSub(one, two));
						break;
					case "*":
						expressionStack.push(context.mkMul(one, two));
						break;
					case "/":
						expressionStack.push(context.mkDiv(one, two));
						break;

					default:
						break;
					}

					operators.pop();

				}

			}

			@Override
			public void exitMultiplicativeoperator(MultiplicativeoperatorContext ctx) {
				operators.push(getParserRuleText(ctx));

			}

			@Override
			public void exitIdentifier(IdentifierContext ctx) {

				expressionStack.push(context.mkIntConst(getParserRuleText(ctx)));

			}

			@Override
			public void exitSignedNumber(SignedNumberContext ctx) {
				expressionStack.push(context.mkInt(getParserRuleText(ctx)));

			}

			@Override
			public void exitAdditiveoperator(AdditiveoperatorContext ctx) {
				operators.push(getParserRuleText(ctx));

			}

			private String getParserRuleText(ParserRuleContext ctx) {
				String text;
				int a = ctx.start.getStartIndex();
				int b = ctx.stop.getStopIndex();
				Interval interval = new Interval(a, b);

				CharStream input = ctx.start.getInputStream();

				text = input.getText(interval);
				return text;
			}

		};

		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		walker.walk(listener, tree); // initiate walk of tree with listener

		Statement statement = statementStack.pop();

		System.out.println(statement.toString());
		// System.out.println("Jaa");
		// System.out.println(statementStack.toString());
		// System.out.println(expressionStack.toString());
		// System.out.println(ifConditionsStack.toString());
		// System.out.println(operators.toString());
		return statement;
	}

	public static void parseProgram(String prog) {

		FormulasAndStatementsLexer lexer = new FormulasAndStatementsLexer(new ANTLRInputStream(prog));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		FormulasAndStatementsParser parser = new FormulasAndStatementsParser(tokens);

		ParseTree tree = parser.prog();
		if (parser.getNumberOfSyntaxErrors() > 0) { // Throw Parse Error

			throw new IllegalArgumentException("Errors in Syntax");
		}

		Context context = InfiniteStateActionFactory.context;
		// For Formulas
		final Stack<Expr> formulaStack = new Stack<>();
		final Stack<String> operatorStack = new Stack<>();

		// For Statements
		final Stack<Statement> statementStack = new Stack<>();
		final Stack<Expr> expressionStack = new Stack<>();
		final Stack<BoolExpr> ifConditionsStack = new Stack<>();
		final Stack<String> operators = new Stack<>();

		FormulasAndStatementsBaseListener listener = new FormulasAndStatementsBaseListener() {
			/*
			 * Program Parsing
			 * 
			 */

			@Override
			public void exitIntial(IntialContext ctx) {
				InfiniteStateActionFactory.setInitialGuard((BoolExpr) formulaStack.pop());

			}

			@Override
			public void enterProcess(ProcessContext ctx) {

				
			}
			
			@Override
			public void enterProcessname(ProcessnameContext ctx) {
				// TODO Auto-generated method stub
				
			}

			int currentProcessNumber = 0;
			@Override
			public void exitProcessname(ProcessnameContext ctx) {
				currentProcessNumber = Integer.parseInt(getParserRuleText(ctx));
				InfiniteStateActionFactory.processNumbers.add(currentProcessNumber);
				
			}


			/*
			 * Actions
			 */

			@Override
			public void enterAction(ActionContext ctx) {

				InfiniteStateAction action = new InfiniteStateAction();
				action.processNumber = currentProcessNumber;
				action.actionNumber = InfiniteStateActionFactory.actions.size() +1 ;
				InfiniteStateActionFactory.currentAction = action;
			}

			@Override
			public void exitAction(ActionContext ctx) {

				
				InfiniteStateActionFactory.actions.add(InfiniteStateActionFactory.currentAction);

			}

			/*
			 * Guards
			 */

			@Override
			public void exitLocalGuard(LocalGuardContext ctx) {

				InfiniteStateActionFactory.currentAction.lGuard = (BoolExpr) formulaStack.pop();
			}

			@Override
			public void exitGlobalGuard(GlobalGuardContext ctx) {

				InfiniteStateActionFactory.currentAction.gGuard = (BoolExpr) formulaStack.pop();
			}

			/*
			 * Effects
			 */

			@Override
			public void exitGlobalEffect(GlobalEffectContext ctx) {
				InfiniteStateActionFactory.currentAction.gEffect = statementStack.pop();
			}

			@Override
			public void exitLocalEffect(LocalEffectContext ctx) {
				InfiniteStateActionFactory.currentAction.lEffect = statementStack.pop();

			}

			/*
			 * Formula Parsing
			 * 
			 */
			@Override
			public void exitBinaryFunction(BinaryFunctionContext ctx) {

				ArithExpr expr = null;

				switch (operatorStack.pop()) {

				case "+":
					expr = context.mkAdd((IntExpr) formulaStack.pop(), (IntExpr) formulaStack.pop());
					break;
				case "-":
					expr = context.mkSub((IntExpr) formulaStack.pop(), (IntExpr) formulaStack.pop());
					break;
				case "*":
					expr = context.mkMul((IntExpr) formulaStack.pop(), (IntExpr) formulaStack.pop());
					break;
				case "/":
					expr = context.mkDiv((IntExpr) formulaStack.pop(), (IntExpr) formulaStack.pop());
					break;

				default:
					break;
				}

				formulaStack.push(expr);

			}

			@Override
			public void exitNegation(NegationContext ctx) {

				if (getParserRuleText(ctx).startsWith("!")) {

					formulaStack.push(context.mkNot((BoolExpr) formulaStack.pop()));
				}

			}

			@Override
			public void exitBoundVariable(BoundVariableContext ctx) {

				formulaStack.push(context.mkIntConst((getParserRuleText(ctx).replace("?", ""))));
				InfiniteStateActionFactory.allIntVariables.add(getParserRuleText(ctx).replace("?", ""));
			}

			@Override
			public void enterDisjunction(DisjunctionContext ctx) {
				formulaStack.push(null);

			}

			@Override
			public void exitDisjunction(DisjunctionContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();
				BoolExpr current = (BoolExpr) formulaStack.pop();
				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) formulaStack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkOr(result, child);
						}

					formulaStack.push(result);
				} else {
					formulaStack.push(expressions.getFirst());
				}

			}

			@Override
			public void exitRelOp(RelOpContext ctx) {

				operatorStack.push(getParserRuleText(ctx));

			}

			@Override
			public void exitAtomicPredicate(AtomicPredicateContext ctx) {
				formulaStack.push(context.mkBoolConst(getParserRuleText(ctx)));
				
				
				InfiniteStateActionFactory.allAPs.add(getParserRuleText(ctx));
			}

			@Override
			public void exitBinaryPredicate(BinaryPredicateContext ctx) {
				BoolExpr expr = null;

				IntExpr two = (IntExpr) formulaStack.pop();
				IntExpr one = (IntExpr) formulaStack.pop();

				switch (operatorStack.pop()) {

				case "=":
					expr = context.mkEq(one, two);
					break;
				case "!=":
					expr = context.mkNot(context.mkEq(one, two));
					break;
				case "<":
					expr = context.mkLt(one, two);
					break;
				case "<=":
					expr = context.mkLe(one, two);
					break;
				case ">":
					expr = context.mkGt(one, two);
					break;
				case ">=":
					expr = context.mkGe(one, two);
					break;

				default:
					break;
				}

				formulaStack.push(expr);

			}

			@Override
			public void exitArithOp(ArithOpContext ctx) {
				operatorStack.push(getParserRuleText(ctx));

			}

			@Override
			public void exitFreeVariable(FreeVariableContext ctx) {

				try {
					
					int result = Integer.parseInt(getParserRuleText(ctx));
					formulaStack.push(context.mkInt(result));
					
					
				} catch (Exception e) {
					formulaStack.push(context.mkIntConst(getParserRuleText(ctx)));
					InfiniteStateActionFactory.allIntVariables.add(getParserRuleText(ctx));
				}
				

			}
			
			@Override
			public void exitBoolLiteral(BoolLiteralContext ctx) {
				String literal = getParserRuleText(ctx);

				if (literal.toLowerCase().contains("t"))
					formulaStack.push(context.mkBool(true));
				else {
					formulaStack.push(context.mkBool(false));
				}
				
			}

			@Override
			public void enterConjunction(ConjunctionContext ctx) {

				formulaStack.push(null);

			}

			@Override
			public void exitConjunction(ConjunctionContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();
				BoolExpr current = (BoolExpr) formulaStack.pop();

				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) formulaStack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkAnd(result, child);
						}

					formulaStack.push(result);
				} else {
					formulaStack.push(expressions.getFirst());
				}

			}

			@Override
			public void exitQuantifiedFormula(QuantifiedFormulaContext ctx) {

				BoolExpr dis = (BoolExpr) formulaStack.pop();
				IntExpr x = (IntExpr) formulaStack.pop();

				switch (operatorStack.pop().toLowerCase()) {
				case "forall":
					Expr xAll = context.mkForall(new Expr[] { x }, dis, 1, null, null, null, null);
					formulaStack.push(xAll);
					break;

				case "exists":
					Expr xExists = context.mkExists(new Expr[] { x }, dis, 1, null, null, null, null);
					formulaStack.push(xExists);
					break;

				default:
					break;
				}

			}

			@Override
			public void exitQuantifier(QuantifierContext ctx) {
				operatorStack.push(getParserRuleText(ctx));

			}

			/*
			 * Block Statements
			 * 
			 */
			
			@Override
			public void enterSskip(SskipContext ctx) {
				statementStack.push(new BlockStatement());
				
			}

			@Override
			public void exitSskip(SskipContext ctx) {
				// TODO Auto-generated method stub
				
			}

			
			
			@Override
			public void enterBlock(BlockContext ctx) {

				statementStack.push(new BlockStatement());
				statementStack.push(null);

			}

			@Override
			public void exitBlock(BlockContext ctx) {

				Statement statement;

				ArrayList<Statement> children = new ArrayList<>();

				statement = statementStack.pop();
				while (statement != null) {

					children.add(0, statement);
					statement = statementStack.pop();

				}

				BlockStatement bs = (BlockStatement) statementStack.pop();
				bs.children = children;
				statementStack.push(bs);

			}
			
			

			/*
			 * If Statements
			 * 
			 */

			@Override
			public void enterIfStatement(IfStatementContext ctx) {

				statementStack.push(new IfStatement());
				statementStack.push(null);

			}

			public void exitIfStatement(IfStatementContext ctx) {

				Statement two = statementStack.pop();
				Statement one = statementStack.pop();

				if (one == null) {
					// If then statement

					IfStatement statement = (IfStatement) statementStack.pop();
					statement.condition = ifConditionsStack.pop();
					statement.thenBody = two;
					statementStack.push(statement);

				} else {
					// If else statement
					statementStack.pop(); // will remove the null
					IfStatement statement = (IfStatement) statementStack.pop();
					statement.condition = ifConditionsStack.pop();
					statement.thenBody = one;
					statement.elseBody = two;

					statementStack.push(statement);

				}

			}

			/*
			 * If Condition
			 * 
			 */

			@Override
			public void exitIfboolPredicate(IfboolPredicateContext ctx) {

				ifConditionsStack.push((BoolExpr) expressionStack.pop());
				
				statementStack.pop();
				IfStatement statement = (IfStatement) statementStack.pop();
				statement.conditionString = getParserRuleText(ctx);
				
				
				statementStack.push(statement);
				statementStack.push(null);
			}

			@Override
			public void enterIfcondition(IfconditionContext ctx) {
				expressionStack.push(null);

			}

			@Override
			public void exitIfcondition(IfconditionContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();

				BoolExpr current = (BoolExpr) expressionStack.pop();

				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) expressionStack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkOr(result, child);
						}

					expressionStack.push(result);
				} else {
					expressionStack.push(expressions.getFirst());
				}

			}

			@Override
			public void enterIfcondTerm(IfcondTermContext ctx) {
				expressionStack.push(null);

			}

			@Override
			public void exitIfcondTerm(IfcondTermContext ctx) {

				BoolExpr result = null;

				LinkedList<BoolExpr> expressions = new LinkedList<BoolExpr>();

				BoolExpr current = (BoolExpr) expressionStack.pop();

				while (current != null) {

					expressions.add(current);
					current = (BoolExpr) expressionStack.pop();
				}

				if (expressions.size() > 1) {
					for (BoolExpr child : expressions)
						if (result == null) {
							result = child;
						} else {
							result = context.mkAnd(result, child);
						}

					expressionStack.push(result);
				} else {
					expressionStack.push(expressions.getFirst());
				}

			}

			@Override
			public void exitRelationaloperator(RelationaloperatorContext ctx) {

				operators.push(getParserRuleText(ctx));

			}

			@Override
			public void exitIfpredicate(IfpredicateContext ctx) {

				BoolExpr expr = null;

				IntExpr two = (IntExpr) expressionStack.pop();
				IntExpr one = (IntExpr) expressionStack.pop();

				switch (operators.pop()) {

				case "=":
					expr = context.mkEq(one, two);
					break;
				case "!=":
					expr = context.mkNot(context.mkEq(one, two));
					break;
				case "<":
					expr = context.mkLt(one, two);
					break;
				case "<=":
					expr = context.mkLe(one, two);
					break;
				case ">":
					expr = context.mkGt(one, two);
					break;
				case ">=":
					expr = context.mkGe(one, two);
					break;

				default:
					break;
				}

				expressionStack.push(expr);

			}

			/*
			 * Assignments
			 * 
			 */

			@Override
			public void enterAssignmentStatement(AssignmentStatementContext ctx) {
				statementStack.push(new AssignmentStatement());

			}

			@Override
			public void exitLeftIdentifier(LeftIdentifierContext ctx) {
				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.left = expressionStack.pop();
				statementStack.push(statement);

			}

			@Override
			public void exitLeftPredicate(LeftPredicateContext ctx) {
				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.left = context.mkBoolConst(getParserRuleText(ctx));
				statementStack.push(statement);

			}

			@Override
			public void exitRighExpr(RighExprContext ctx) {

				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.right = expressionStack.pop();
				statement.assignmentType = AssignmentType.IntAssignment;
				statement.rightHand = getParserRuleText(ctx);
				statementStack.push(statement);
			}

			@Override
			public void exitRightBool(RightBoolContext ctx) {

				String literal = getParserRuleText(ctx);

				if (literal.toLowerCase().contains("t"))
					expressionStack.push(context.mkBool(true));
				else {
					expressionStack.push(context.mkBool(false));
				}

				AssignmentStatement statement = (AssignmentStatement) statementStack.pop();
				statement.right = expressionStack.pop();
				statement.assignmentType = AssignmentType.BoolAssignment;

				statementStack.push(statement);

			}

			/*
			 * Expressions
			 * 
			 */

			@Override
			public void enterExpr(ExprContext ctx) {

				operators.push(null);

			}

			@Override
			public void exitExpr(ExprContext ctx) {

				if (operators.peek() == null) {
					operators.pop();

					return;
				}

				if (!operators.isEmpty() && expressionStack.size() > 1) {

					IntExpr two = (IntExpr) expressionStack.pop();
					IntExpr one = (IntExpr) expressionStack.pop();
					switch (operators.pop()) {
					case "+":
						expressionStack.push(context.mkAdd(one, two));
						break;
					case "-":
						expressionStack.push(context.mkSub(one, two));
						break;
					case "*":
						expressionStack.push(context.mkMul(one, two));
						break;
					case "/":
						expressionStack.push(context.mkDiv(one, two));
						break;

					default:
						break;
					}

					operators.pop();

				}

			}

			@Override
			public void exitMultiplicativeoperator(MultiplicativeoperatorContext ctx) {
				operators.push(getParserRuleText(ctx));

			}

			@Override
			public void exitIdentifier(IdentifierContext ctx) {

				expressionStack.push(context.mkIntConst(getParserRuleText(ctx)));
				InfiniteStateActionFactory.allIntVariables.add(getParserRuleText(ctx).replace("?", ""));

			}

			@Override
			public void exitSignedNumber(SignedNumberContext ctx) {
				expressionStack.push(context.mkInt(getParserRuleText(ctx)));

			}

			@Override
			public void exitAdditiveoperator(AdditiveoperatorContext ctx) {
				operators.push(getParserRuleText(ctx));

			}

			/*
			 * Helper
			 * 
			 */

			private String getParserRuleText(ParserRuleContext ctx) {
				String text;
				int a = ctx.start.getStartIndex();
				int b = ctx.stop.getStopIndex();
				Interval interval = new Interval(a, b);

				CharStream input = ctx.start.getInputStream();

				text = input.getText(interval);
				return text;
			}
		};

		ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
		walker.walk(listener, tree); // initiate walk of tree with listener
		ArrayList<InfiniteStateAction> actions = InfiniteStateActionFactory.actions;

//		for (InfiniteStateAction infiniteStateAction : actions) {
//			System.out.println("Action" + infiniteStateAction.toString());
//		}
	}
}
