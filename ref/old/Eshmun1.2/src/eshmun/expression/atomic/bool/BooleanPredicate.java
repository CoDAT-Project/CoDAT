package eshmun.expression.atomic.bool;

import eshmun.expression.atomic.AtomicPredicate;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.regex.IndexReplacementContext;

/**
 * this class represents a Predicate Formula that evaluates to a boolean value in the context of kripkeState
 * 
 * @author Emile Chartouni
 *
 */
public class BooleanPredicate extends AtomicPredicate{
	/** the boolean variable containing the truth value of the BooleanPredicate */
	protected BooleanVariable variable;
	
	/**
	 * constructs a new BooleanPredicate
	 * @param variable the boolean variable
	 */
	public BooleanPredicate(BooleanVariable variable) {
		super();
		this.variable = variable;
	}
	
	public BooleanPredicate(String variable) {
		super();
		this.variable = new BooleanVariable(variable);
	}
		
	@Override
	public BooleanPredicate replaceParameters(IndexReplacementContext indexReplacementContext) {
		BooleanVariable variable = this.variable.replaceParameters(indexReplacementContext);
		return new BooleanPredicate(variable);
	}
	
	/**
	 * return true
	 */
	@Override
	public boolean isCNF() {
		return true;
	}

	public BooleanVariable getVariable() {
		return variable;
	}

	@Override
	public String toString() {
		return getVariable().getName();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BooleanPredicate other = (BooleanPredicate) obj;
		if (variable == null) {
			if (other.variable != null)
				return false;
		} else if (!variable.equals(other.variable))
			return false;
		return true;
	}

	@Override
	public boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception {
		return modelChecker.visit(this, structure, state);
	}
	
	@Override
	public BooleanPredicate clone() throws CloneNotSupportedException {
		
		return new BooleanPredicate(this.variable.clone());
	}
	
}
