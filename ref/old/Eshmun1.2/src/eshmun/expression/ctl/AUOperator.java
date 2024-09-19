package eshmun.expression.ctl;

import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the CTL formula A[leftChild U rightChild]
 * @author Emile
 *
 */
public class AUOperator extends BinaryCTLOperator {
	/**
	 * Creates a new parsed tree for the CTL formula A[leftChild U rightChild] 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public AUOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super(leftChild, rightChild);
		this.toStringValue =  "A" + getPathIndexesString() + "[" + leftChild.toString() + " U " + rightChild.toString() + "]";
	}
	
	
	@Override
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue =  "A" + getPathIndexesString() + "[" + leftChild.toString() + " U " + rightChild.toString() + "]";
	}

	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacementContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacementContext);
		AUOperator euOperator = new AUOperator(leftChild, rightChild);
		return euOperator;
	}

	
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor)  {
		return expressionVisitor.fillSubFormulae(this);
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea()  {
		//A[p U q] <-> !E[ !p v !q ]
		NotOperator right = new NotOperator(rightChild);
		PredicateFormula left;
		if(!leftChild.toString().toLowerCase().equals("true"))
			left = new NotOperator(leftChild);
		else
			left = new BooleanPredicate(new BooleanConstant(false));
		EVOperator ev = new EVOperator(left, right);
		NotOperator not = new NotOperator(ev);
		return not.getSubFormulea();
	}
	@Override
	public PredicateFormula ConvertToCTL()
	{
		//A[p U q] <-> !E[ !p v !q ]
		NotOperator right = new NotOperator(rightChild);
		PredicateFormula left;
		if(!leftChild.toString().toLowerCase().equals("true"))
			left = new NotOperator(leftChild);
		else
			left = new BooleanPredicate(new BooleanConstant(false));
		EVOperator ev = new EVOperator(left, right);
		NotOperator not = new NotOperator(ev);
		return not.ConvertToCTL();
	}
	
	
	
	
		
	
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
}
