package t7.statements;

import java.util.ArrayList;
import java.util.List;

import t7.StatementHandler;
import t7.types.T7Boolean;
import t7.types.VariableType;
import t7.expressions.Expression;

/**
 * Represents an If[-Else] Statement.
 * @author kinan
 *
 */
public class IfStatement implements Statement {
	/**
	 * The boolean condition for the If statement (Could be a boolean expression,
	 * or a boolean variable).
	 */
	private Expression expression;
	
	/**
	 * The statements in the if body.
	 */
	private List<Statement> ifBody;
	
	/**
	 * The else statement (or null if there is no else).
	 */
	private ElseStatement elseStatement;
	
	/**
	 * Creates a new if Statement.
	 */
	public IfStatement() {
		this.ifBody = new ArrayList<Statement>();		
	}	
	
	/**
	 * Sets the condition of the if Condition.
	 * @param expression the condition.
	 */
	public void setCondition(Expression expression) {
		this.expression = expression;
	}
	
	/**
	 * Evaluates the If condition, returns it as a Boolean.
	 * @param handler the statement handler encapsulating the state of the program.
	 * @throws IllegalArgumentException if the if condition was of an illegal type.
	 * @return The result of evaluating the condition.
	 */
	public T7Boolean evaluateCondition(StatementHandler handler) throws IllegalArgumentException {
		VariableType var = expression.evaluate(handler, null, 0);
		if(var instanceof T7Boolean) {
			return (T7Boolean) var;
		} else {
			throw new IllegalArgumentException("Illegal type in the while loop condition, expected Boolean.");
		}
	}
	
	/**
	 * Appends a statement to the end of the body of the if statement.
	 * @param bodyStatement the statement to append.
	 */
	public void appendIfBody(Statement bodyStatement) {
		this.ifBody.add(bodyStatement);
	}
	
	/**
	 * Sets the else statement of this if.
	 * @param elseStatement the else statement (null means no else).
	 */
	public void setElseStatement(ElseStatement elseStatement) {
		this.elseStatement = elseStatement;
	}
	
	/**
	 * @return The if body.
	 */
	public List<Statement> getIfBody() {
		return ifBody;
	}
	
	/**
	 * @return The else statement, or null if no else.
	 */
	public ElseStatement getElseStatement() {
		return elseStatement;
	}
	
	/**
	 * @return true if this statement has an else statement, false otherwise.
	 */
	public boolean hasElse() {
		return elseStatement != null;
	}
	
	/**
	 * @return If.
	 */
	@Override
	public Types getType() {
		return Types.If;
	}

}
