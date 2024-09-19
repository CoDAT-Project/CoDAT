package eshmun.expression.atomic.bool;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.regex.IndexReplacementContext;

public class BooleanConstant extends BooleanVariable{

	private boolean value;
	
	public BooleanConstant(boolean value) {
		super(value ? "true":"false");
		this.value = value;
	}

	@Override
	public Boolean evaluate(Kripke structure, KripkeState state) {
		return value;
	}
	
	@Override
	public BooleanConstant replaceParameters(IndexReplacementContext indexReplacementContext) {
		return this;
	}
}
