package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;


import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the CTL formula A[leftChild V rightChild]
 * @author Emile
 *
 */
public class AWOperator extends BinaryCTLOperator {

	/**
	 * Creates a new parsed tree for the CTL formula A[leftChild V rightChild] 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public AWOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super(leftChild, rightChild);
		this.toStringValue =  "A" + getPathIndexesString() + "[" + leftChild.toString() + " W " + rightChild.toString() + "]";
	}
	
	
	@Override
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue =  "A" + getPathIndexesString() + "[" + leftChild.toString() + " W " + rightChild.toString() + "]";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacementContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacementContext);
		AWOperator euOperator = new AWOperator(leftChild, rightChild);
		return euOperator;
	}
	
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor)  {
		return expressionVisitor.fillSubFormulae(this);
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea()  {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(this.getLeftChild().getSubFormulea());
		subformulea.addAll(this.getRightChild().getSubFormulea());
		subformulea.add(this);
		return subformulea;
	}
	@Override
	public PredicateFormula ConvertToCTL(){
		Exception ex =  new Exception();
		ex.notify();
		return this;
	}
}
