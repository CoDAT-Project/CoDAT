package eshmun.lts.kripke.DFS;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;

public class ExiValuation extends Valuation{

	private final String prefix = "ex";
	private Boolean value;
	private final String transitionName;
	
	public ExiValuation(Kripke kripke, KripkeState state, String transitionName, boolean value) {
		super(kripke, state);
		this.transitionName = transitionName;
		this.value = value;
	}

	@Override
	public String getName() {
		return prefix + transitionName;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = (Boolean) value;
	}

	@Override
	public String toString() {
		return getName() + " : " + getValue();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ExiValuation exiVal = new ExiValuation(kripke, kripkeState, transitionName, value);
		return exiVal;
	}
	
	public int hashCode() {
		int hash = 1;
		if (value != null) {
			hash = hash * 31 + value.hashCode();
		}
		if (transitionName != null) {
			hash = hash * 31 + transitionName.hashCode();
		}
		hash += prefix.hashCode();
		return hash;
	}
	
}
