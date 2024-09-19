package eshmun.expression;

import java.util.Map;

import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;
import eshmun.regex.IndexReplacementContext;

public class ExpressionVariable implements IExpressionVariable{
	
	protected String name;

	public ExpressionVariable(String name) {
		super();
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpressionVariable other = (ExpressionVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public Object evaluate(Kripke structure, KripkeState state) {
		Valuation valuation = state.getValuation(getName());
		Object returnValue = null;
		if (valuation != null) {
			returnValue = valuation.getValue();
		} else {
			String [] split = getName().split(".");
			if (split.length > 1) {
				String mapName = split[0];
				valuation = state.getValuation(mapName);
				Object valuationReturn = valuation.getValue();
				if (valuationReturn instanceof Map<?, ?>) {
					Map<?, ?> map = (Map<?, ?>) valuationReturn;
					returnValue = map.get(split[1]);
				}
			}
		}
		return returnValue;
	}

	@Override
	public IExpressionVariable replaceParameters(IndexReplacementContext indexReplacementContext) {
		String replacedName = indexReplacementContext.replaceIndexes(this.name);
		return new ExpressionVariable(replacedName);
	}

	@Override
	public String toString() {
		return getName();
	}
	
	
	
}
