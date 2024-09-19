package eshmun.expression;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;

public class PredicateFormulaValuation extends Valuation {
	private PredicateFormula formula;
	private Boolean value;
	
	public PredicateFormulaValuation(Kripke kripke, KripkeState state, PredicateFormula formula, Boolean value) {
		super(kripke, state);
		this.value = value;
		this.formula = formula;
	}

	@Override
	public String getName() {
		return formula.toString();
	}

	@Override
	public Boolean getValue() {
		return value;
	}
	
	public PredicateFormula getFormula()
	{
		return formula;
	}

	@Override
	public void setValue(Object value) {
		this.value = (Boolean)value;
	}
	
	public int hashCode() {
		int hash = 1;
		if (formula != null) {
			hash = hash * 31 + formula.hashCode();
		}
		if (value != null) {
			hash = hash * 31 + value.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		return formula.toString() + "=" + value;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		PredicateFormulaValuation prVal = new PredicateFormulaValuation(kripke, kripkeState, this.formula, value);
		return prVal;
	}
}
