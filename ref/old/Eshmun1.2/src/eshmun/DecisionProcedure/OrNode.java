package eshmun.DecisionProcedure;

import java.util.List;

import eshmun.expression.PredicateFormula;

public class OrNode extends DPVertex {
	
	private boolean _isExpandable;

	public boolean isExpandable() {
		return _isExpandable;
	}

	public void setExpandable(boolean _isExpandable) {
		this._isExpandable = _isExpandable;
	}

	public OrNode(String name, List<PredicateFormula> formulas, boolean isStartState) {
		super(name, formulas, isStartState);
		_isExpandable = true;
	}

	public OrNode(String name, PredicateFormula formula, boolean isStartState) {
		super(name, formula, isStartState);
		_isExpandable = true;
	}

}
