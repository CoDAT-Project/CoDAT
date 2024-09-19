package t7.statements;

import java.util.ArrayList;
import java.util.List;

import t7.StatementHandler;
import t7.types.T7Integer;
import t7.types.T7Range;
import t7.types.VariableType;
import t7.expressions.Expression;

/**
 * Represents a For Loop Statement.
 * @author kinan
 *
 */
public class ForStatement implements Statement {
	/**
	 * The name of the loop counter.
	 */
	private String varName;
	
	/**
	 * The expression yielding the range of looping.
	 */
	private Expression expression;
	
	/**
	 * The statements in the body.
	 */
	private List<Statement> body;
	
	/**
	 * Creates a new For Statement.
	 * @param varName the name of the looping variable.
	 */
	public ForStatement(String varName) {
		this.varName = varName;
		this.body = new ArrayList<Statement>();
	}	
	
	/**
	 * Sets the range of looping (as an expression), the expression
	 * should yield either an integer or a range.
	 * @param expression the looping expression.
	 */
	public void setRangeExpression(Expression expression) {
		this.expression = expression;
	}
	
	/**
	 * @return the variable name.
	 */
	public String getVarName() {
		return varName;
	}
	
	/**
	 * Evaluates the range of this for to get it as a T7Range.
	 * @param handler the statement handler encapsulating the state of the program.
	 * @throws IllegalArgumentException if the for loop was given a range of an illegal type.
	 * @return The looping range.
	 */
	public T7Range evaluateRange(StatementHandler handler) throws IllegalArgumentException {
		VariableType var = expression.evaluate(handler, null, 0);
		if(var instanceof T7Integer) {
			return new T7Range((T7Integer) var, (T7Integer) var);
		} else if(var instanceof T7Range) {
			return (T7Range) var;
		} else {
			throw new IllegalArgumentException("Illegal type in for loop range, expected Range or Integer.");
		}
	}
	
	/**
	 * Appends a statement to the end of the body of the for loop.
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
	 * @return For.
	 */
	@Override
	public Types getType() {
		return Types.For;
	}
}
