package eshmun.expression.propoperator;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the propositionat formula leftChild => rightChild
 * @author Emile
 *
 */
public class ImpliesOperator extends PropOperator {

	private PredicateFormula leftChild;
	private PredicateFormula rightChild;
	
	/**
	 * Creates a new parsed tree for the propositional formula leftChild => rightChild 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public ImpliesOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		super();
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue = "(" + leftChild.toString() + ") => (" + rightChild.toString() + ")";
	}
	
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.toStringValue = "(" + leftChild.toString() + ") => (" + rightChild.toString() + ")";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacmentContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacmentContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacmentContext);
		ImpliesOperator implOp = new ImpliesOperator(leftChild, rightChild);
		return implOp;
	}
	
	/**
	 * returns a list containing subformulae of the left child and right child and this predicate.
	 * @return a list ordered by subformulae inclusion order
	 */
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		NotOperator notChild = new NotOperator(getLeftChild());
		OrOperator rootOperator = new OrOperator(notChild, rightChild);
		subformulea.addAll(rootOperator.getSubFormulea(expressionVisitor));
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea( ) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		NotOperator notChild = new NotOperator(getLeftChild());
		OrOperator rootOperator = new OrOperator(notChild, rightChild);
		subformulea.addAll(rootOperator.getSubFormulea());
		subformulea.add(this);
		return subformulea;
	}
	
	
	@Override
	public PredicateFormula ConvertToCTL(){		
		NotOperator notChild = new NotOperator(leftChild.ConvertToCTL());
		OrOperator rootOperator = new OrOperator(notChild, rightChild.ConvertToCTL());
		return rootOperator;
	}
	
	/**
	 * return true if its left and right children are in CNF format
	 */
	@Override
	public boolean isCNF() {
		return false;
	}
	
	

	public PredicateFormula getLeftChild() {
		return leftChild;
	}

	public PredicateFormula getRightChild() {
		return rightChild;
	}

	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception  {
		return modelChecker.visit(this, structure, state);
	}
	public OrOperator toAnd()
	{
		return new OrOperator(new NotOperator(leftChild), rightChild);
	}
}
