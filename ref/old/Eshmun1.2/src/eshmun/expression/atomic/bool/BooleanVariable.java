package eshmun.expression.atomic.bool;

import java.util.Map;

import eshmun.expression.ExpressionVariable;
import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;
import eshmun.regex.IndexReplacementContext;

public class BooleanVariable extends ExpressionVariable{

	public BooleanVariable(String name) {
		super(name);
	}

	@Override
	public Boolean evaluate(Kripke structure, KripkeState state) {
		String[] input = getName().split("\\.");
		
		
		Valuation valuation = state.getValuation(input[0]);
		int x =0;
		boolean returnValue = false;
		if (valuation != null) {
			Object valuationReturn = valuation.getValue();
			if(valuationReturn instanceof Map<?,?> && input.length == 2) {
				valuationReturn = ((Map)valuationReturn).get(input[1]);
			}
			if (valuationReturn instanceof Boolean) {
				returnValue = ((Boolean)valuationReturn).booleanValue();
			} 
		} 
		return returnValue;
	}
	
	@Override
	public BooleanVariable replaceParameters(IndexReplacementContext indexReplacementContext) {
		String replacedName = indexReplacementContext.replaceIndexes(this.getName());
		return new BooleanVariable(replacedName);
	}
	
	@Override
	public BooleanVariable clone() throws CloneNotSupportedException {
		
		String str = name;
		return new BooleanVariable(str);
	}

}
