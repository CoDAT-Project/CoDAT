package eshmun.expression.ctl;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the CTL formula E[leftChild V rightChild]
 * @author Emile
 *
 */
public class EVOperator extends BinaryCTLOperator {
	/**
	 * Creates a new parsed tree for the CTL formula E[leftChild V rightChild] 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public EVOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super(leftChild, rightChild);
		this.toStringValue = "E" + getPathIndexesString() + "[" + leftChild.toString() + " V " + rightChild.toString() + "]";
	}
	
	
	@Override
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue = "E" + getPathIndexesString() + "[" + leftChild.toString() + " V " + rightChild.toString() + "]";
	}
	public int superScript=-1;

	public int getSuperScript() {
		return superScript;
	}


	public void setSuperScript(int superScript) {
		this.superScript = superScript;
	}
	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacementContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacementContext);
		EVOperator euOperator = new EVOperator(leftChild, rightChild);
		return euOperator;
	}
	
	
public List<PredicateFormula> getSubFormulea() {
		
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.add(this);
		//EXOperator ex = new EXOperator(this);
		//subformulea.add(ex);
		//OrOperator or = new OrOperator(leftChild, ex);
		//subformulea.add(or);
		//AndOperator and = new AndOperator(rightChild, or);
		//subformulea.add(and);
		subformulea.addAll(this.getRightChild().getSubFormulea());
		subformulea.addAll(this.getLeftChild().getSubFormulea());
		
		return subformulea;	
	}
@Override
public PredicateFormula ConvertToCTL(){
	return new EVOperator(leftChild.ConvertToCTL(), rightChild.ConvertToCTL());
}

}
