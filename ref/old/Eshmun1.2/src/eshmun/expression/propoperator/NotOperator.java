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
 * a class for the implementation of the propositional formula !child
 * @author Emile
 *
 */
public class NotOperator extends PropOperator {
	
	PredicateFormula child;
	
	/**
	 * Creates a new parsed tree for the propositional formula !child 
	 * @param leftChild: the left child of the operator
	 * @param rightChild: the right child of the operator
	 */
	public NotOperator(PredicateFormula child) {
		super();
		this.child = child;
		this.toStringValue = "!(" + child.toString() + ")";
	}
	
	@Override
	public PredicateFormula replaceParameters(IndexReplacementContext indexReplacmentContext) {
		PredicateFormula child = this.getChild().replaceParameters(indexReplacmentContext);
		NotOperator notOperator = new NotOperator(child);
		return notOperator;
	}
	
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke kripke, KripkeState state) throws Exception {
		return modelChecker.visit(this, kripke, state);
	}
	
	
	/**
	 * returns a list containing sub formulae of the child and this predicate.
	 * @return a list ordered by sub formulae inclusion order
	 */
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(child.getSubFormulea(expressionVisitor));
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> getSubFormulea() {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(child.getSubFormulea());
		subformulea.add(this);
		return subformulea;
	}
	
	@Override
	public PredicateFormula ConvertToCTL(){
		return new NotOperator(child.ConvertToCTL());
	}

	/**
	 * return true if the child is an atomic operator else return false 
	 */
	@Override
	public boolean isCNF() {
		return false;
	}
	
	

	public PredicateFormula getChild() {
		return child;
	}
}
