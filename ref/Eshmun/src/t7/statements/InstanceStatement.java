package t7.statements;

import java.util.ArrayList;

import t7.expressions.BooleanExpression;
import t7.expressions.Expression;

/**
 * Represents an Instance Statement.
 * @author kinan
 *
 */
public class InstanceStatement implements Statement {
	
	/**
	 * The name of the structure being instantiated.
	 */
	private String name;
		
	/**
	 * The variables/values that make the range of instantiation.
	 */
	private ArrayList<Expression> vars;
	
	/**
	 * The boolean expressions to restrict instantiation range.
	 * Matched to vars by index, could be null in the absence of
	 * such an expression.
	 */
	private ArrayList<BooleanExpression> bools;
	
	/**
	 * The names of variables used, an entry could be null in case the 
	 * related expression was not a variable but a complex expression or literal.
	 * Matched to vars by index.
	 */	
	private ArrayList<String> names;
	
	
	/**
	 * Creates a new Instantiation Statement.
	 * @param name the name of the structure to instantiate.
	 */
	public InstanceStatement(String name) {
		this.name = name;
		
		vars = new ArrayList<Expression>();
		bools = new ArrayList<BooleanExpression>();
		names = new ArrayList<String>();
	}
	
	/**
	 * @return the name of the structure being instantiated.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Adds a variable/value to the ranges.
	 * @param expression the variable/value.
	 */
	public void add(Expression expression) {
		vars.add(expression);
		bools.add(null);
		names.add(null);
	}
	
	/**
	 * Adds a variable/value to the ranges.
	 * @param expression the variable/value.
	 * @param boolExpression the boolean expression that restricts this range.
	 * @param varName the variable name.
	 */
	public void add(Expression expression, BooleanExpression boolExpression, String varName) {
		vars.add(expression);
		bools.add(boolExpression);
		names.add(varName);
	}
	
	/**
	 * @return The variables/values of the range.
	 */
	public ArrayList<Expression> getVars() {
		return vars;
	}
	
	/**
	 * @return The boolean expressions that restrict the ranges. 
	 */
	public ArrayList<BooleanExpression> getBools() {
		return bools;
	}
	
	/**
	 * @return The names of the variables.
	 */
	public ArrayList<String> getNames() {
		return names;
	}
	
	/**
	 * @return InstanceStatement. 
	 */
	@Override
	public Types getType() {
		return Types.InstanceStatement;
	}
}
