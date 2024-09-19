package eshmun.expression.atomic.string;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.regex.IndexReplacementContext;

public class StringConstant extends StringVariable{

	public StringConstant(String name) {
		super(name);
		if (name.contains("'")) {
			this.name = name.replaceAll("'", "");
		}
	}

	@Override
	public String evaluate(Kripke structure, KripkeState state) {
		return getName();
	}
	
	@Override
	public StringConstant replaceParameters(IndexReplacementContext indexReplacementContext) {
		return this;
	}

	@Override
	public String toString() {
		return "'" + super.toString() + "'";
	}
}
