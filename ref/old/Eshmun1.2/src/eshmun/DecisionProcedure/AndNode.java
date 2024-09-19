package eshmun.DecisionProcedure;

import java.util.List;

import eshmun.expression.PredicateFormula;

public class AndNode extends DPVertex {
	
	private boolean toBeDeleted;
	
	public AndNode(String name, List<PredicateFormula> formulas, boolean isStartState) {
		super(name, formulas, isStartState);
		toBeDeleted = false;
	}

	public AndNode(String name, PredicateFormula formula, boolean isStartState) {
		super(name, formula, isStartState);
		toBeDeleted = false;
	}

	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}

}
