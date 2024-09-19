package eshmun.expression.atomic;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;

/**
 * This class represents a boolean variable.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class BooleanVariable extends AbstractExpression {
	/**
	 * Counter, guarantees unique names for auto generated variables.
	 */
	private static int index = 0;
	
	/**
	 * Resets Counter.
	 */
	public static void reset() {
		index = 0;
	}
	
	/**
	 * Name of the variable. 
	 * Can be either <ul>
	 * 	<li> Auto-generated: with format Zi where i is an index. </li>
	 * 	<li> Specified explicitly by the user as part of some input formula. </li>
	 * 	<li> Follows naming standards of variables related to states and transitions. </li>
	 * </ul>
	 */
	private final String name;
	
	/**
	 * Create a Variable with an Auto-generated Name (Zi).
	 */
	public BooleanVariable() {
		super(ExpressionType.BooleanVariable);
		
		name = "Z"+index;
		index++;
	}
		
	/**
	 * Create a Variable with a given name.
	 * @param name the name of the variable.
	 */
	public BooleanVariable(String name) {
		super(ExpressionType.BooleanVariable);
		
		if(name.contains("="))
			name = name.replace("=", ":=");
		
		while(name.contains("::=")) {
			name = name.replace("::=", ":=");
		}
		
		this.name = name;
	}
	
	/**
	 * Create a Variable with a standard name equal to X_&lt;StateName&gt;. 
	 * @param state The state the variable is related to
	 */
	public BooleanVariable(AbstractState state) {
		super(ExpressionType.BooleanVariable);
		
		this.name = state.getVarName();
	}
	
	/**
	 * Create a Variable with a standard name equal to E_&lt;StateFromName&gt;_&lt;StateToName&gt;.
	 * @param transition The transition the variable is related to
	 */
	public BooleanVariable(AbstractTransition transition) {
		super(ExpressionType.BooleanVariable);
		
		this.name = transition.getVarName();
	}
	
	/** 
	 * Getter for the variable name.
	 * @return The variable's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true.
	 */

	@Override
	public boolean isAtomic() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true.
	 */
	@Override
	public boolean isBooleanVariable() {
		return false;
	}		
	
	/**
	 * {@inheritDoc}
	 * @return true.
	 */
	@Override
	public boolean isCNF() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isLiteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean containsCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if this variable is the same as the given variable (based on name).
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return this.equals(variable);
	}

	/**
	 * {@inheritDoc}
	 * @return true if this variable's name is the same as the given one.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		variableName = variableName.trim();
		return name.equals(variableName) || name.startsWith(variableName+":=");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this);
	}

	/**
	 * {@inheritDoc}
	 * @return this.
	 */
	@Override
	public AbstractExpression simplify() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 * @return this.
	 */
	@Override
	public AbstractExpression toCNF() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @return this.
	 */
	@Override
	public AbstractExpression toNNF() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		BooleanVariable var = (BooleanVariable) exp;
		
		return var.getName().equals(this.getName());
	}
	
	/**
	 * @return The hashcode of the name of the variable.
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	/**
	 * Clones this BooleanVariable.
	 *  
	 * @return a completely independent copy of this BooleanVariable.
	 */
	@Override
	public BooleanVariable clone() {
		return new BooleanVariable(this.name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		visitor.visit(this);
		visitor.separator();
	}
}
