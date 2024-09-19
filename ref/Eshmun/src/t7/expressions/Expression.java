package t7.expressions;

import t7.StatementHandler;
import t7.types.VariableType;

/**
 * An abstract expression.
 * @author kinan
 *
 */
public interface Expression {

	/**
	 * Get the type of the expression.
	 * @return the type.
	 */
	public Types getType();
	
	/**
	 * The possible types for expressions.
	 * @author kinan
	 *
	 */
	public static enum Types {
		Arithmetic, Boolean, Variable, Triplet
	}
	
	/**
	 * Evaluates the given Expression using the state represented in the given handler.
	 * @param handler contains the state of the program.
	 * @param name the name of the variable to override its value in this expression scope.
	 * @param value the new value of the variable inside this expression scope.
	 * @throws IllegalArgumentException if a variable type mismatched.
	 * @throws ArithmeticException if division by zero.
	 * @return the value.
	 */
	public VariableType evaluate(StatementHandler handler, String name, int value) throws IllegalArgumentException, ArithmeticException;
}
