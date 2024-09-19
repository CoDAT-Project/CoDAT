package eshmun.expression.atomic;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;

/**
 * This class represents an atomic predicate in a PredicateFormula. 
 * An atomic predicate could be an {@code BooleanOperator} or {@code ComparisonOperator}. 
 * for the BNF for parsing an AtomicPredicate 
 * 
 * @see PredicateFormula 
 *
 * @author Emile
 *
 */
public abstract class AtomicPredicate extends PredicateFormula{

	/**
	 * creates a new Atomic predicate
	 */
	public AtomicPredicate() {
	}
	
	/**
	 * @return a list containing this AtomicPredicate
	 */
	@Override
	public List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor) {
		List<PredicateFormula> returnList = new ArrayList<PredicateFormula>();
		returnList.add(this);
		return returnList;
	}
	@Override
	public List<PredicateFormula> getSubFormulea() {
		List<PredicateFormula> returnList = new ArrayList<PredicateFormula>();
		returnList.add(this);
		return returnList;
	}
	
	@Override
	public PredicateFormula ConvertToCTL(){
		return this;
	}
	
	/**
	 * returns true since every atomic predicate is in CNF
	 * @return
	 */
	@Override
	public boolean isCNF(){
		return true;
	}

}
