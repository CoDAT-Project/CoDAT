package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;

/**
 * class describing CTL Parsed trees with a single child, for example: AU, EU, AW, EW, AV, EV
 * @author Emile Chartouni
 *
 */
public abstract class BinaryCTLOperator extends CTLParseTree {

	/** the left child */
	protected PredicateFormula leftChild;
	/** the right child */
	protected PredicateFormula rightChild;
	
	/**
	 * Creates a binary ctl operator 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public BinaryCTLOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super();
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	public abstract void setChilds(PredicateFormula leftChild, PredicateFormula rightChild);

	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(leftChild.getSubFormulea(expressionVisitor));
		subformulea.addAll(rightChild.getSubFormulea(expressionVisitor));
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea() {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(leftChild.getSubFormulea());
		subformulea.addAll(rightChild.getSubFormulea());
		subformulea.add(this);
		return subformulea;
	}

	public PredicateFormula getLeftChild() {
		return leftChild;
	}

	public PredicateFormula getRightChild() {
		return rightChild;
	}
	
}