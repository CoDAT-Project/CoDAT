package eshmun.expression.visitor.visitors;

import java.util.Collection;
import java.util.HashSet;

import eshmun.expression.AbstractExpression;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.VisitorAdapter;

/**
 * Lists the variables inside a general expression (no pre-conditions).
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class GeneralVariableListerVisitor extends VisitorAdapter {
	
	/**
	 * the list of variables to be returned.
	 */
	private HashSet<String> variables;
	
	/**
	 * Gets the variables in this expression.
	 * @param expression the expression to look for variables in.
	 * @return a collection of variable names.
	 */
	public Collection<String> getVariables(AbstractExpression expression) {
		variables = new HashSet<String>();
		
		expression.accept(this);
		
		return variables;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanVariable v) {
		if(!variables.contains(v.getName().trim()))
			variables.add(v.getName().trim());
		
	}

	/**
	 * Does nothing. Shown here for completeness.
	 */
	@Override
	public void separator() { }
	
	/**
	 * Does nothing. Shown here for completeness.
	 */
	@Override
	public void childrenSeparator() { }
	
	/**
	 * Does nothing. Shown here for completeness.
	 */
	@Override
	protected void visit(AbstractExpression v) { }
}
