package t7.statements;

import java.util.ArrayList;
import java.util.List;

import t7.StatementHandler;
import t7.types.T7Boolean;
import t7.types.VariableType;
import t7.expressions.Expression;

/**
 * Represents a While Loop Statement.
 * @author kinan
 *
 */
public class WhileStatement implements Statement {
	/**
	 * The boolean condition for looping (Could be a boolean expression,
	 * or a boolean variable).
	 */
	private Expression expression;
	
	/**
	 * The statements in the body.
	 */
	private List<Statement> body;
	
	/**
	 * Creates a new while Statement.
	 */
	public WhileStatement() {
		this.body = new ArrayList<Statement>();
	}	
	
	/**
	 * Sets the looping condition of the While loop.
	 * @param expression the loop expression.
	 */
	public void setLoopCondition(Expression expression) {
		this.expression = expression;
	}
	
	/**
	 * Evaluates the condition of this while loop and returns it as a boolean.
	 * @param handler the statement handler encapsulating the state of the program.
	 * @throws IllegalArgumentException if the while loop was given a range of an illegal type.
	 * @return The result of evaluating the looping condition.
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
	 * Appends a statement to the end of the body of the while loop.
	 * @param bodyStatement the statement to append.
	 */
	public void appendBody(Statement bodyStatement) {
		body.add(bodyStatement);
	}
	
	/**
	 * @return The loop's body.
	 */
	public List<Statement> getBody() {
		return body;
	}
	
	/**
	 * @return While.
	 */
	@Override
	public Types getType() {
		return Types.While;
	}

}
