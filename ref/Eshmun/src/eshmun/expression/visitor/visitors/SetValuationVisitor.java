package eshmun.expression.visitor.visitors;

import eshmun.expression.AbstractExpression;
import eshmun.expression.visitor.VisitorAdapter;

/**
 * Sets the valuation of an AbstractExpression and all of its children, children of children, etc..
 * 
 * <p>Uses Visitor Pattern, in specific VisitorAdapter. Whenever an AbstractExpression is visited,
 * the valuation of that expression is set to the given valuation.</p> 
 * 
 * <p>Usage: <br>
 * &nbsp;&nbsp;&nbsp; SetValuationVisitor visitor = new SetValuationvisitor(some_expression); <br>
 * &nbsp;&nbsp;&nbsp; visitor.setValuation(true); //set the valuation of some_expression to true. <br>
 * </p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.expression.visitor.VisitorAdapter
 */
public class SetValuationVisitor extends VisitorAdapter {
	private AbstractExpression expression;
	private Boolean valuation;
	
	/**
	 * Create a SetValuationVisitor that sets the valuation of the passed expression and its children.
	 * @param expression the expression to set its valuation
	 */
	public SetValuationVisitor(AbstractExpression expression) {
		this.expression = expression;
	}
	
	/**
	 * Sets the valuation of initial expression and all 
	 * its children to the given valuation.
	 * 
	 * @param valuation the new valuation of expression and its children.
	 */
	public void setValuation(Boolean valuation) {
		this.valuation = valuation;
		
		expression.accept(this);
	}

	/**
	 * Does Nothing. Shown here for completeness.
	 */
	@Override
	public void childrenSeparator() {}
	
	/**
	 * Does Nothing. Shown here for completeness.
	 */
	@Override
	public void separator() {}

	/**
	 * Visits an AbstractExpression, set its valuation to the given valuation.
	 * @param v the AbstractExpression to be visited.
	 */
	@Override
	protected void visit(AbstractExpression v) {
		v.setValuation(valuation);		
	}
	
	
}
