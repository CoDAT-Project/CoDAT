package eshmun.expression.ctl;


import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the CTL formula E[leftChild V rightChild]
 * @author Emile
 *
 */
public class EWOperator extends BinaryCTLOperator {
	/**
	 * Creates a new parsed tree for the CTL formula E[leftChild V rightChild] 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public EWOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super(leftChild, rightChild);
		this.toStringValue = "E" + getPathIndexesString() + "[" + leftChild.toString() + " W " + rightChild.toString() + "]";
	}
	
	@Override
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue = "E" + getPathIndexesString() + "[" + leftChild.toString() + " W " + rightChild.toString() + "]";
	}
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacementContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacementContext);
		EWOperator euOperator = new EWOperator(leftChild, rightChild);
		return euOperator;
	}

	
	@Override
	public PredicateFormula ConvertToCTL(){
		Exception ex =  new Exception();
		ex.notify();
		return this;
	}
}
