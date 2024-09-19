package eshmun.lts.kripke;

/**
 * an abstract class for a valuation of some Atomic Proposition / Variable 
 * in a Kripke State
 * @author Emile
 *
 */
public abstract class Valuation {
	/** the parent kripke structure */
	protected Kripke kripke;
	/** the parent kripke state */
	protected KripkeState kripkeState;
	
	/**
	 * creates a new valuation in a Kripke State of some KripkeStructure
	 * @param state
	 */
	public Valuation(Kripke kripke, KripkeState state) {
		this.kripke = kripke;
		this.kripkeState = state;
	}
	
	/**
	 * returns the value of the valuation
	 * @return
	 */
	public abstract Object getValue();
	
	/**
	 * returns the name of the valuation
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * sets the value of the valuation
	 * @param value
	 */
	public abstract void setValue(Object value);
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public void setKripke(Kripke kripke) {
		this.kripke = kripke;
	}

	public void setKripkeState(KripkeState kripkeState) {
		this.kripkeState = kripkeState;
	}
}
