package t7.expressions;

import t7.StatementHandler;
import t7.types.T7Boolean;
import t7.types.T7Integer;
import t7.types.VariableType;

/**
 * Represents a Boolean Expression,
 * it could be a single value, or a variable, or a binary expression.
 * Binary expression could be a boolean expression between two boolean expressions,
 * or could be two arithemtic expressions with a comparator.
 * 
 * @author kinan
 */
public class BooleanExpression implements Expression {
	/**
	 * The variable name.
	 */
	private String var;
	
	/**
	 * The integer value.
	 */
	private Boolean val;
	
	/**
	 * The left expression.
	 */
	private BooleanExpression left;

	/**
	 * The right expression.
	 */
	private BooleanExpression right;
	
	/**
	 * The operator.
	 */
	private String operator;
	
	/**
	 * The left expression.
	 */
	private ArithmeticExpression leftArith;

	/**
	 * The right expression.
	 */
	private ArithmeticExpression rightArith;
	
	/**
	 * The operator.
	 */
	private String comparator;
	
	
	/**
	 * Whether The expression is negated or not.
	 */
	private boolean negated = false;

	/**
	 * Creates a new boolean expression which is a variable.
	 * @param var the variable name.
	 */
	public BooleanExpression(String var) {
		this.var = var;
	}
	
	/**
	 * Creates a new boolean expression which is a single boolean value.
	 * @param val the value.
	 */
	public BooleanExpression(boolean val) {
		this.val = val;
	}
	
	/**
	 * Creates a new boolean expression which is a binary expression.
	 * @param left the left expression.
	 * @param right the right expression.
	 * @param operator the operator.
	 */
	public BooleanExpression(BooleanExpression left, BooleanExpression right, String operator) {
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	/**
	 * Creates a new arithmetic boolean expression.
	 * @param left the left expression.
	 * @param right the right expression.
	 * @param comparator the comparator.
	 */
	public BooleanExpression(ArithmeticExpression left, ArithmeticExpression right, String comparator) {
		this.leftArith = left;
		this.rightArith = right;
		this.comparator = comparator;
	}
	
	/**
	 * Negates the expression (i.e. the expression is preceded by a Not sign).
	 */
	public void negate() {
		negated = !negated;
	}
	
	/**
	 * Evaluates the given boolean Expression using the state represented in the given handler.
	 * @param handler contains the state of the program.
	 * @param name the name of the variable to override its value in this expression scope.
	 * @param value the new value of the variable inside this expression scope.
	 * @throws IllegalArgumentException if a variable inside the expression was not an T7Integer.
	 * @throws ArithmeticException if division by zero.
	 * @return the value.
	 */
	@Override
	public T7Boolean evaluate(StatementHandler handler, String name, int value) throws IllegalArgumentException, ArithmeticException {
		boolean result = false;
		
		if(var != null) {
			VariableType tmp = handler.getVariable(var);
			if(!(tmp instanceof T7Integer)) {
				throw new IllegalArgumentException("Illegal type for variable "+var+", expected Boolean.");
			}
			
			result = ((T7Boolean) tmp).value();
		} else if(val != null) {
			result = val;
		} else if(left != null) {
			boolean leftValue = left.evaluate(handler, name, value).value();
			boolean rightValue = right.evaluate(handler, name, value).value();
						
			if(operator.equals("&")) 
				result = leftValue && rightValue;
			
			else if(operator.equals("|"))
				result = leftValue || rightValue;
			
			else if(operator.equals("=="))
				result = leftValue == rightValue;
			
			else if(operator.equals("!=")) 
				result = leftValue != rightValue;
		} else {
			int leftValue = leftArith.evaluate(handler, name, value).value();
			int rightValue = rightArith.evaluate(handler, name, value).value();
				
			if(comparator.equals(">")) 
				result = leftValue > rightValue;
			
			else if(comparator.equals("<")) 
				result = leftValue < rightValue;
			
			else if(comparator.equals(">=")) 
				result = leftValue >= rightValue;
				
			else if(comparator.equals("<=")) 
				result = leftValue <= rightValue;
				
			else if(comparator.equals("==")) 
				result = leftValue == rightValue;
				
			else if(comparator.equals("!=")) 
				result = leftValue != rightValue;
		}
		
		
		return new T7Boolean(negated ? !result : result);
	}
	
	/**
	 * Returns the string representation of this expression, useful for debugging.
	 * @return the string representation.
	 */
	@Override
	public String toString() {
		String neg = negated ? "!" : "";
		
		if(var != null) return neg + var;
		if(val != null) return neg + val;
		if(left != null) return neg + "(" + left + operator + right +")";
		return neg + "(" + leftArith + comparator + rightArith + ")";
	}
	
	/**
	 * @return Boolean.
	 */
	@Override
	public Types getType() {
		return Types.Boolean;
	}
}
