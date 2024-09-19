package eshmun.expression.ctl;

import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.propoperator.NotOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the CTL formula E[leftChild V rightChild]
 * @author Emile
 *
 */
public class EUOperator extends BinaryCTLOperator {
	/**
	 * Creates a new parsed tree for the CTL formula E[leftChild V rightChild] 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public EUOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super(leftChild, rightChild);
		toStringValue = "E" + getPathIndexesString() + "[" + leftChild.toString() + " U " + rightChild.toString() + "]";
	}
	
	
	@Override
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		toStringValue = "E" + getPathIndexesString() + "[" + leftChild.toString() + " U " + rightChild.toString() + "]";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacementContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacementContext);
		EUOperator euOperator = new EUOperator(leftChild, rightChild);
		return euOperator;
	}
	
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	

	
	
	public List<PredicateFormula> getSubFormulea()  {
		//E[p U q] <-> !A[ !p v !q ]
		NotOperator right = new NotOperator(rightChild);
		NotOperator left = new NotOperator(leftChild);
		AVOperator av = new AVOperator(left, right);
		NotOperator not = new NotOperator(av);
		return not.getSubFormulea();
	}
	@Override
	public PredicateFormula ConvertToCTL()
	{
		//E[p U q] <-> !A[ !p v !q ]
		NotOperator right = new NotOperator(rightChild);
		NotOperator left = new NotOperator(leftChild);
		AVOperator av = new AVOperator(left, right);
		NotOperator not = new NotOperator(av);
		return not.ConvertToCTL();
	}
}
