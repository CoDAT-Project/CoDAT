package eshmun.expression.atomic;

import eshmun.expression.IExpressionVariable;

/**
 * an abstract class that represents a comparison of two variables, a variable with a constant.
 * the variables should compared must be of the same domain.
 * 
 * @author Emile Chartouni
 *
 */
public abstract class ComparisonOperator extends AtomicPredicate{

	/** the left variable in the comparison */
	protected IExpressionVariable leftChild;
	/** the right variable in the comparison */
	protected IExpressionVariable rightChild;

	/**
	 * Creates a arithmetic formula for comparing leftChild and rightChild 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public  ComparisonOperator(IExpressionVariable leftChild, IExpressionVariable rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	/**
	 * return true
	 */
	@Override
	public boolean isCNF() {
		return true;
	}

	public IExpressionVariable getLeftChild() {
		return leftChild;
	}

	public IExpressionVariable getRightChild() {
		return rightChild;
	}	
}