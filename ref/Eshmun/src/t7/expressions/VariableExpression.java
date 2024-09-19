package t7.expressions;

import t7.StatementHandler;
import t7.types.T7Integer;
import t7.types.VariableType;

/**
 * A Variable expression is just a variable access.
 * @author kinan
 *
 */
public class VariableExpression implements Expression {
	/**
	 * The variable name.
	 */
	private String varName;
	
	/**
	 * Creates a new variable expression.
	 * @param varName the name of the variable.
	 */
	public VariableExpression(String varName) {
		this.varName = varName;
	}
	
	/**
	 * @return The variable name.
	 */
	public String getVarName() {
		return varName;
	}
	
	/**
	 * @return Variable.
	 */
	@Override
	public Types getType() {
		return Types.Variable;
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
		if(varName.equals(name))
			return new T7Integer(value);
		
		return handler.getVariable(varName).clone();
	}
}
