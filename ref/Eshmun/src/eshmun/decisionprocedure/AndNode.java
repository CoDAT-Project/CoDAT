package eshmun.decisionprocedure;

import java.util.List;

import eshmun.expression.AbstractExpression;

public class AndNode extends TableauNode {
	/**
	 * Flags if this node is to be deleted.
	 */
	private boolean toBeDeleted;
	
	/**
	 * Creates a new And Node with the given formula.
	 * @param formula the formula inside the And Node.
	 */
	public AndNode(AbstractExpression formula) {
		super(formula);
		
		toBeDeleted = false;
	}
	
	/**
	 * Creates a new And Node with the given list of formulae (conjunction).
	 * @param formulae the formulae inside the And Node.
	 */
	public AndNode(List<AbstractExpression> formulae) {
		super(formulae);
		
		toBeDeleted = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteEdge(TableauEdge edge) {
		super.deleteEdge(edge);
		
		toBeDeleted = true;
	}

	/**
	 * Checks if this node is to be deleted.
	 * @return true if this node is to be deleted, false otherwise.
	 */
	public boolean isToBeDeleted() {
		return toBeDeleted;
	}
	
	/**
	 * Two AndNode are equal if they have equal formulae.
	 * @return true if obj equals this, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof AndNode)) 
			return false;
		
		AndNode o = (AndNode) obj;
		return formulae.containsAll(o.formulae) && o.formulae.containsAll(formulae);
	}
}
