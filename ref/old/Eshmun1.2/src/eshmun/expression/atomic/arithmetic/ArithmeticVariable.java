package eshmun.expression.atomic.arithmetic;

import eshmun.expression.ExpressionVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;
import eshmun.regex.IndexReplacementContext;

public class ArithmeticVariable extends ExpressionVariable{

	public ArithmeticVariable(String name) {
		super(name);
	}

	@Override
	public Float evaluate(Kripke structure, KripkeState state) {
		Valuation valuation = state.getValuation(getName());
		float returnValue = 0.0f;
		if (valuation != null) {
			Object valuationReturn = valuation.getValue();
			if (valuationReturn instanceof Integer) {
				returnValue = ((Integer)valuationReturn).floatValue();
			} else if (valuationReturn instanceof Float) {
				returnValue = ((Float)valuationReturn).floatValue();
			} else if (valuationReturn instanceof Double) {
				returnValue = ((Double)valuationReturn).floatValue();
			} 
		} 
		return returnValue;
	}
	
	@Override
	public ArithmeticVariable replaceParameters(IndexReplacementContext indexReplacementContext) {
		String replacedName = indexReplacementContext.replaceIndexes(this.getName());
		return new ArithmeticVariable(replacedName);
	}
}
