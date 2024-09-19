package t7.statements;

import t7.expressions.Expression;

/**
 * Represents a Variable Definition Statement.
 * @author kinan
 */
public class VariableDefinitionStatement implements Statement {
	/**
	 * The variable's name.
	 */
	private String name;
	
	/**
	 * The variable's type and value.
	 */
	private Expression expression;
	
	/**
	 * Creates a Variable Definition statement.
	 * @param name the variable name.
	 * @param expression the assigned expression.
	 */
	public VariableDefinitionStatement(String name, Expression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	/**
	 * @return The name of the variable.
	 */
	public String getName() {
		return name;
	}
	
	/**	
	 * @return The type of the assigned expression.
	 */
	public Expression.Types getExpressionType() {
		return expression.getType();
	}
	
	/**
	 * @return the assigned expression.
	 */
	public Expression getExpression() {
		return expression;
	}

	/**
	 * @return VariableDefinition.
	 */
	@Override
	public Types getType() {
		return Types.VariableDefinition;
	}
	
	/**
	 * @return the hash code of the name.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
