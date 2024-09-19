package eshmun.expression.visitor.visitors;

import java.util.Collection;
import java.util.LinkedList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.visitor.VisitorAdapter;

/**
 * Gets the set biggest of none-ctl sub formulae of a general expression (no pre-conditions).
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class GreatestSubFormulaListerVisitor extends VisitorAdapter {

	/**
	 * the list of sub-formulae to be returned.
	 */
	private LinkedList<AbstractExpression> expressions;
	
	/**
	 * Gets the sub-formulae in this expression (the largest formulae that does not contain CTL, the propositional argument to a CTL modality).
	 * @param expression the expression to look for sub-formulae in.
	 * @return a collection of purely propositional expressions (no CTL).
	 */
	public Collection<AbstractExpression> getSubFormulae(AbstractExpression expression) {
		expressions = new LinkedList<AbstractExpression>();
		
		expression.accept(this);
		
		return expressions;
	}
	
	/**
	 * counts how many sub formulas were encountered after the one that doesn't
	 * contain CTL.
	 */
	private int subFormulaCounter;
	
	/**
	 * checks if v contains CTL, if it doesn't, it is added to the list, with all its
	 * sub-formulae ignored.
	 */
	@Override
	protected void visit(AbstractExpression v) {
		if(subFormulaCounter != 0) { //inside a bigger sub formula, ignore.
			subFormulaCounter++;
			return;
		}
		
		//not inside a bigger sub formula.
		if(!v.containsCTL()) {
			for(AbstractExpression e : expressions) {
				if(e.equals(v)) {
					subFormulaCounter++;
					return;
				}
			}
			
			expressions.add(v);
			subFormulaCounter++;
		}	
	}
	
	/**
	 * Decrements the counter of sub-formulae met. signifies exiting a sub-formula.
	 */
	@Override
	public void separator() { 
		if(subFormulaCounter > 0)
			subFormulaCounter--; 
	}
	
	/**
	 * Does nothing. Shown here for completeness.
	 */
	@Override
	public void childrenSeparator() { }
	
}
