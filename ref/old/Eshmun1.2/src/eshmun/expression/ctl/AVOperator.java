package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the CTL formula A[leftChild V rightChild]
 * @author Emile
 *
 */
public class AVOperator extends BinaryCTLOperator {

	public int superScript=-1;

	public int getSuperScript() {
		return superScript;
	}


	public void setSuperScript(int superScript) {
		this.superScript = superScript;
	}
	
	

	/**
	 * Creates a new parsed tree for the CTL formula A[leftChild V rightChild] 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public AVOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super(leftChild, rightChild);
		this.toStringValue =  "A" + getPathIndexesString() + "[" + leftChild.toString() + " V " + rightChild.toString() + "]";
	}

	@Override
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue =  "A" + getPathIndexesString() + "[" + leftChild.toString() + " V " + rightChild.toString() + "]";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacementContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacementContext);
		AVOperator euOperator = new AVOperator(leftChild, rightChild);
		return euOperator;
	}
	
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	
	
	public List<PredicateFormula> getSubFormulea() {
		
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.add(this);
		//AXOperator ax = new AXOperator(this);
		//subformulea.add(ax);
		//OrOperator or = new OrOperator(leftChild, ax);
		//subformulea.add(or);
		//AndOperator and = new AndOperator(rightChild, or);
		//subformulea.add(and);
		subformulea.addAll(this.getRightChild().getSubFormulea());
		subformulea.addAll(this.getLeftChild().getSubFormulea());		
		return subformulea;		
	}
	@Override
	public PredicateFormula ConvertToCTL(){
		return new AVOperator(leftChild.ConvertToCTL(), rightChild.ConvertToCTL());
	}
	
	
}
