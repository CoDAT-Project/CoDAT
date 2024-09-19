package eshmun.sat;

import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.AtomicPredicate;


/**
 *The abstract implementation for the SAT solver interface. This interface
 *will take an ExpressionTree object that represents a propositional formula
 *and solve its satisfying valuation.
 * @author Emile
 *
 */
public abstract class AbstractSatSolver {
	
	protected PredicateFormula predicate;
	
	/**
	 * creates a new SatSolver for propositional formula represented by 
	 * expressionTree
	 * @param expressionTree
	 */
	public AbstractSatSolver(PredicateFormula predicate) {	
		this.predicate = predicate;
	}
	
	
	/**
	 * returns true if the propositional formula is satisfiable else 
	 * it returns false
	 * EFFECTS: fills all the satisfiable solutions in memory if one exists
	 * @return
	 */
	public abstract boolean isSatisfiable();
	
	
	/**
	 * returns true as long as we still have satisfiable valuations which
	 * are not queried.
	 */
	public boolean hasNextSatisfiableValuation(){
		return false;
	}
	
	/**
	 * each time called the next satisfiable valuation will be returned. 
	 * when the satisfiable valuations are over, a null value will be returned
	 * @return
	 */
	public List<AtomicPredicate> getNextValuation(){
		return null;
	}
}
