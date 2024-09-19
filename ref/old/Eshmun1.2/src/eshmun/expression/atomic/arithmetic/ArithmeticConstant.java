package eshmun.expression.atomic.arithmetic;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.regex.IndexReplacementContext;

public class ArithmeticConstant extends ArithmeticVariable{

	private Float value;

	public ArithmeticConstant(float value) {
		super("" + value);
		this.value = value;
	}

	@Override
	public Float evaluate(Kripke structure, KripkeState state) {
		return value;
	}
	
	@Override
	public ArithmeticConstant replaceParameters(IndexReplacementContext indexReplacementContext) {
		return this;
	}
}
