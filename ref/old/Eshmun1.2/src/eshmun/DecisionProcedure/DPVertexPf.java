package eshmun.DecisionProcedure;

import eshmun.expression.PredicateFormula;

public class DPVertexPf {
	private PredicateFormula pf;
	private boolean isExpanded;

	public DPVertexPf(PredicateFormula f) {
		pf=f;
		isExpanded = false;
	}
	public DPVertexPf(PredicateFormula f, boolean expanded) {
		pf=f;
		isExpanded = true;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public PredicateFormula getPf() {
		return pf;
	}
	@Override
	public String toString() {
		//return "[pf=" + pf + ", isExpanded=" + isExpanded + "]";
		return "[pf=" + pf + "]";
	}
	
	

}
