package eshmun.decisionprocedure;

import java.util.List;

import eshmun.expression.AbstractExpression;

public class OrNode extends TableauNode {	
	/**
	 * Creates a new Or Node with the given formula.
	 * @param formula the formula inside the Or Node.
	 */
	public OrNode(AbstractExpression formula) {
		super(formula);
	}
	
	/**
	 * Creates a new Or Node with the given list of formulae (conjunction).
	 * @param formulae the formulae inside the Or Node.
	 */
	public OrNode(List<AbstractExpression> formulae) {
		super(formulae);
	}

	/**
	 * Two AndNode are equal if they have equal formulae.
	 * @return true if obj equals this, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof OrNode)) 
			return false;
		
		OrNode o = (OrNode) obj;
		return formulae.containsAll(o.formulae) && o.formulae.containsAll(formulae);
	}
}
