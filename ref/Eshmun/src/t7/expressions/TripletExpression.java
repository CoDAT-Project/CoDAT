package t7.expressions;

import t7.StatementHandler;
import t7.types.T7Range;
import t7.types.VariableType;

/**
 * A Triplet Expression is made of three arithmetic expressions (start, end, step which is optional). 
 * Evaluating the expression will result in a Range.
 * @author kinan
 *
 */
public class TripletExpression implements Expression {
	/**
	 * The start expression.
	 */
	private ArithmeticExpression start;
	
	/**
	 * The end expression.
	 */
	private ArithmeticExpression end;
	
	/**
	 * The step expression.
	 */
	private ArithmeticExpression step;
	
	/**
	 * Creates a new variable expression.
	 * @param start the start expression.
	 * @param end the end expression.
	 * @param step the step expression.
	 */
	public TripletExpression(ArithmeticExpression start, ArithmeticExpression end, ArithmeticExpression step) {
		this.start = start;
		this.end = end;
		this.step = step;
	}
	
	/**
	 * @return Triplet.
	 */
	@Override
	public Types getType() {
		return Types.Triplet;
	}
	
	/**
	 * Evaluates the given variable Expression using the state represented in the given handler.
	 * @param handler contains the state of the program.
	 * @param name the name of the variable to override its value in this expression scope.
	 * @param value the new value of the variable inside this expression scope.
	 * @return the value.
	 */
	@Override
	public VariableType evaluate(StatementHandler handler, String name, int value) {
		if(step == null)
			return new T7Range(start.evaluate(handler, name, value), end.evaluate(handler, name, value));
		else
			return new T7Range(start.evaluate(handler, name, value), end.evaluate(handler, name, value), step.evaluate(handler, name, value));
	}
}
