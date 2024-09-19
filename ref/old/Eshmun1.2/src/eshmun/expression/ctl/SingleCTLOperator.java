package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;

/**
 * class describing CTL Parsed trees with a single child, for example: AF, EF, AX, EX, AG, EG
 * @author Emile Chartouni
 *
 */
public abstract class SingleCTLOperator extends CTLParseTree{
	protected PredicateFormula child;
	
	/**
	 * Creates a new CTL parsed tree with a single child  
	 * @param child: the child of the operator
	 */
	public SingleCTLOperator(PredicateFormula child) {
		super();
		this.child = child;
	}
	
	public abstract void setChild(PredicateFormula pchild);
	/**
	 * returns a list containing this predicate and the subformulea of its child.
	 * the list is ordered by the formulae inclusion order
	 */
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVsitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(child.getSubFormulea(expressionVsitor));
		subformulea.add(this);
		return subformulea;	
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea() {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(child.getSubFormulea());
		subformulea.add(this);
		return subformulea;	
	}
	
	public PredicateFormula getChild() {
		return child;
	}	
	
	
}
