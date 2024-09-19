package eshmun.modelchecker;

import java.util.Comparator;

import eshmun.expression.IExpressionVariable;
import eshmun.expression.atomic.arithmetic.ArithmeticVariable;
import eshmun.expression.atomic.string.StringVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;

public class VariableComparator implements Comparator<IExpressionVariable> {

	private Kripke kripke;
	private KripkeState state;
	
	public VariableComparator(Kripke kripke, KripkeState state) {
		super();
		this.kripke = kripke;
		this.state = state;
	}

	@Override
	public int compare(IExpressionVariable var1, IExpressionVariable var2) {
		int returnValue = -1;
		if (var1 instanceof StringVariable && var2 instanceof StringVariable) {
			String value1 = ((StringVariable) var1).evaluate(kripke, state);
			String value2 = ((StringVariable) var2).evaluate(kripke, state);
			returnValue = value1.compareTo(value2);
		} else if (var1 instanceof ArithmeticVariable && var2 instanceof ArithmeticVariable) {
			Float value1 = ((ArithmeticVariable) var1).evaluate(kripke, state);
			Float value2 = ((ArithmeticVariable) var2).evaluate(kripke, state);
			returnValue = value1.compareTo(value2);
		} else {
			Object value1 = var1.evaluate(kripke, state);
			Object value2 = var2.evaluate(kripke, state);
			if (value1 instanceof String && value2 instanceof String) {
				returnValue = ((String)value1).compareTo((String)value2);
			} else if (
				(value1 instanceof Integer ||
				value1 instanceof Float ||
				value1 instanceof Double) && 
				(value2 instanceof Integer ||
				value2 instanceof Float ||
				value2 instanceof Double)
				) {
				returnValue = convertToFloat(value1).compareTo(convertToFloat(value2));
			} else {
				returnValue = -1;
			}
		}
		return returnValue;
	}
	
	private Float convertToFloat(Object value) {
		float returnValue = 0.0f;
		if (value instanceof Integer) {
			returnValue = ((Integer)value).floatValue();
		} else if (value instanceof Float) {
			returnValue = ((Float)value).floatValue();
		} else if (value instanceof Double) {
			returnValue = ((Double)value).floatValue();
		}
		return returnValue;
	}

}
