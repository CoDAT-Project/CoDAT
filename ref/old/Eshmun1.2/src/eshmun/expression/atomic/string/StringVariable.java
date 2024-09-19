package eshmun.expression.atomic.string;

import java.util.Map;

import eshmun.expression.ExpressionVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;
import eshmun.regex.IndexReplacementContext;

public class StringVariable extends ExpressionVariable{

	public StringVariable(String name) {
		super(name);
	}

	@Override
	public String evaluate(Kripke kripke, KripkeState state) {
		Valuation valuation = state.getValuation(getName());
		String returnValue = "";
		if (valuation != null) {
			Object valuationReturn = valuation.getValue();
			if (valuationReturn instanceof String) {
				returnValue = (String)valuationReturn;
			} 
		} else {
			String [] split = getName().split(".");
			if (split.length > 1) {
				String mapName = split[0];
				valuation = state.getValuation(mapName);
				Object valuationReturn = valuation.getValue();
				if (valuationReturn instanceof Map<?, ?>) {
					Map<?, ?> map = (Map<?, ?>) valuationReturn;
					Object mapValue = map.get(split[1]);
					if (mapValue instanceof String) {
						returnValue = (String)mapValue;
					}
				}
			}
		}
		return returnValue;
	}
	
	@Override
	public StringVariable replaceParameters(IndexReplacementContext indexReplacementContext) {
		String replacedName = indexReplacementContext.replaceIndexes(this.getName());
		return new StringVariable(replacedName);
	}
}
