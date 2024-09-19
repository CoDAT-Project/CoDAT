package t7.expressions;

import t7.StatementHandler;
import t7.types.T7Integer;
import t7.types.VariableType;

/**
 * Represents an Arithmetic Expression,
 * it could be a single value, or a variable, or a binary expression.
 * 
 * @author kinan
 */
public class ArithmeticExpression implements Expression {
	/**
	 * The variable name.
	 */
	private String var;
	
	/**
	 * The integer value.
	 */
	private Integer val;
	
	/**
	 * The left expression.
	 */
	private ArithmeticExpression left;

	/**
	 * The right expression.
	 */
	private ArithmeticExpression right;
	
	/**
	 * The operator.
	 */
	private String operator;
	
	/**
	 * Indicates if the expression is preceded by a minus.
	 */
	private boolean minus = false;
	
	/**
	 * Creates a new arithmetic expression which is a variable.
	 * @param var the variable name.
	 */
	public ArithmeticExpression(String var) {
		this.var = var;
	}

	/**
	 * Creates a new arithmetic expression which is a single integer value.
	 * @param val the value.
	 */
	public ArithmeticExpression(int val) {
		this.val = val;
	}
	
	/**
	 * Creates a new arithmetic expression which is a binary expression.
	 * @param left the left expression.
	 * @param right the right expression.
	 * @param operator the operator.
	 */
	public ArithmeticExpression(ArithmeticExpression left, ArithmeticExpression right, String operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	/**
	 * Negates the expression, i.e. takes into consideration that it is
	 * preceded by a minus sign. 
	 */
	public void negate() {
		minus = !minus;
	}
	
	/**
	 * Evaluates the given arithmetic Expression using the state represented in the given handler.
	 * @param handler contains the state of the program.
	 * @param name the name of the variable to override its value in this expression scope.
	 * @param value the new value of the variable inside this expression scope.
	 * @throws IllegalArgumentException if a variable inside the expression was not an T7Integer.
	 * @throws ArithmeticException if division by zero.
	 * @return the value.
	 */
	@Override
	public T7Integer evaluate(StatementHandler handler, String name, int value) throws IllegalArgumentException, ArithmeticException {
		int result = 0;
		
		if(var != null) {
			if(var.equals(name)) {
				result = value;
			} else {
				VariableType tmp = handler.getVariable(var);
				if(!(tmp instanceof T7Integer)) {
					throw new IllegalArgumentException("Illegal type for variable "+var+", expected Integer in Arithmetic expression.");
				}
				
				result = ((T7Integer) tmp).value();
			}
		} else if(val != null) {
			result = val;
		} else {
			int leftValue = left.evaluate(handler, name, value).value();
			int rightValue = right.evaluate(handler, name, value).value();
						
			if(operator.equals("+")) 
				result = leftValue + rightValue;
			
			else if(operator.equals("-"))
				result = leftValue - rightValue;
			
			else if(operator.equals("*"))
				result = leftValue * rightValue;
			
			else if(operator.equals("/")) 
				if(rightValue == 0)
					throw new ArithmeticException("Division by zero");
				else
					result = leftValue / rightValue;
			
			else if(operator.equals("^"))
				result = (int) Math.pow(leftValue, rightValue);
			
			else if(operator.equals("%"))
				result = leftValue % rightValue;
		}
		
		
		return new T7Integer(minus ? -result : result);
	}
	
	/**
	 * Returns the string representation of this expression, useful for debugging.
	 * @return the string representation.
	 */
	@Override
	public String toString() {
		String neg = minus ? "-" : "";
		if(var != null) return neg + var;
		if(val != null) return neg + val;
		return neg + "(" + left + operator + right +")";
	}
	
	/**
	 * @return Arithmetic.
	 */
	@Override
	public Types getType() {
		return Types.Arithmetic;
	}
}
