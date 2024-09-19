package eshmun.lts.automaton.modelchecker;

import eshmun.expression.PredicateFormula;


public class Specification {

	private String name;
	private PredicateFormula formula;

	public Specification(String name, PredicateFormula forumlla) {
		this.name = name;
		formula = forumlla;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PredicateFormula getFormula() {
		return formula;
	}

	public void setFormula(PredicateFormula formula) {
		this.formula = formula;
	}

	

}
