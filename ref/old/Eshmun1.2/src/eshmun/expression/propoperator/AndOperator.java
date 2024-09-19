package eshmun.expression.propoperator;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * a class for the implementation of the propositional formula leftChild & rightChild
 * @author Emile
 *
 */
public class AndOperator extends PropOperator {

	private PredicateFormula leftChild;
	private PredicateFormula rightChild;
	
	/**
	 * Creates a new parsed tree for the propositional formula leftChild & rightChild 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public AndOperator(PredicateFormula leftChild, PredicateFormula rightChild) {
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		
//		else if(leftChild == null && rightChild == null)
//			this.toStringValue = "1";
		
		 if(leftChild != null && rightChild != null)
				this.toStringValue = "(" + leftChild.toString() + ") & (" + rightChild.toString() + ")";
	}
	
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacmentContext) {
		PredicateFormula leftChild = this.getLeftChild().replaceParameters(indexReplacmentContext);
		PredicateFormula rightChild = this.getRightChild().replaceParameters(indexReplacmentContext);
		AndOperator euOperator = new AndOperator(leftChild, rightChild);
		return euOperator;
	}
	
	/**
	 * returns a list containing subformulae of the left child and right child and this predicate.
	 * @return a list ordered by subformulae inclusion order
	 */
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressinVisitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(leftChild.getSubFormulea(expressinVisitor));
		subformulea.addAll(rightChild.getSubFormulea(expressinVisitor));
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea( ) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(leftChild.getSubFormulea());
		subformulea.addAll(rightChild.getSubFormulea());
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public PredicateFormula ConvertToCTL(){
		return new AndOperator(leftChild.ConvertToCTL(), rightChild.ConvertToCTL());
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
	
	public void And(PredicateFormula formula)
	{
		if(formula != null)
		{
			if(this.leftChild == null)
				this.leftChild = formula;
			else if(this.rightChild == null)
				this.rightChild = formula;
			else
				this.rightChild = new AndOperator(rightChild, formula);
			if(leftChild != null && rightChild != null)
				this.toStringValue = "(" + leftChild.toString() + ") & (" + rightChild.toString() + ")";
		}
		
	}
	/*
	 * if left child is null and right child is null it returns null
	 * else if one of its childs is null it returns the not null child 
	 * else it returns the object itself
	 */
	public PredicateFormula CheckForNull() {
		if(leftChild == null && rightChild == null)
			return null;
		else if(leftChild == null)
			return rightChild;
		else if(rightChild == null)
			return leftChild;
		return this;		
		
	}
	
	public void setChilds(PredicateFormula leftChild, PredicateFormula rightChild)
	{
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		 if(leftChild != null && rightChild != null)
				this.toStringValue = "(" + leftChild.toString() + ") & (" + rightChild.toString() + ")";
	}

	public PredicateFormula getRightChild() {
		return rightChild;
	}

	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}	
}
