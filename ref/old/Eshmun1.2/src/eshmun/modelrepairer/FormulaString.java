package eshmun.modelrepairer;



import java.text.AttributedString;

import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

/**
 * @author MSakr
 *
 */
public class FormulaString {
	
	
	private KripkeState _s;
	private PredicateFormula _f;
	private KripkeState _t;
	private String EorX;
	private int _superScriptIndex;
	private int _index;
	//value assigned by the sat solver
	private boolean _valuation;
	
	/**
	 * @param State _s
	 * @param Formula _f
	 */
	public FormulaString(String EorX, KripkeState _s, KripkeState _t, PredicateFormula _f, int superScript,  int _index) {
		super();
		this.EorX = EorX;
		this._superScriptIndex = superScript;
		this._s = _s;
		this._f = _f;
		this._t = _t;
		this._index = _index;
		
	}
	
	public boolean getValuation() {
		return _valuation;
	}
	
	
	public void setValuation(boolean _valuation) {
		this._valuation = _valuation;
	}
	/**
	 * @return KripkeState
	 */
	public KripkeState getEndState() {
		return _t;
	}
	/**
	 * @param KripkeState _t
	 */
	public void setEndState(KripkeState _t) {
		this._t = _t;
	}
	
	/**
	 * @return KripkeState
	 */
	public KripkeState getState() {
		return _s;
	}
	/**
	 * @param KripkeState _s
	 */
	public void setState(KripkeState _s) {
		this._s = _s;
	}
	/**
	 * @return PredicateFormula
	 */
	public PredicateFormula getFormula() {
		return _f;
	}
	/**
	 * @param PredicateFormula _f
	 */
	public void setFormula(PredicateFormula _f) {
		this._f = _f;
	}
	/**
	 * @return index
	 */
	public int getIndex() {
		return _index;
	}
	/**
	 * @param int _index
	 */
	public void setIndex(int _index) {
		this._index = _index;
	}
	
	/**
	 * @return super script index
	 */
	public int getSuperScriptIndex() {
		return _superScriptIndex;
	}
	public void setSuperScriptIndex(int _superScriptIndex) {
		this._superScriptIndex = _superScriptIndex;
	}
	
	
	public String getEorX() {
		return EorX;
	}
	public void setEorX(String eorX) {
		EorX = eorX;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_f == null) ? 0 : _f.hashCode());
		result = prime * result + _index;
		result = prime * result + ((_s == null) ? 0 : _s.hashCode());
		result = prime * result + ((_t == null) ? 0 : _t.hashCode());
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
		FormulaString other = (FormulaString) obj;
		if (_f == null) {
			if (other._f != null)
				return false;
		} else if (!_f.equals(other._f))
			return false;
		if (_index != other._index)
			return false;
		if (_s == null) {
			if (other._s != null)
				return false;
		} else if (!compareStates(_s,other._s))
			return false;
		if (_t == null) {
			if (other._t != null)
				return false;
		} else if (!compareStates(_t, other._t))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "x" + _index;
	}
	
	private boolean compareStates(KripkeState s1, KripkeState s2)
	{
		if(s1 == null && s2 == null)
			return true;
		if(s1 != null && s2 != null && s1.getName().trim().equals(s2.getName().trim()))
			return true;
		return false;
	}
	
	public String toInitialString()
	{
		StringBuilder builder = new StringBuilder();
		AttributedString as = new AttributedString(EorX);
		
		//builder.append();
		return builder.toString();
	}

	
}
