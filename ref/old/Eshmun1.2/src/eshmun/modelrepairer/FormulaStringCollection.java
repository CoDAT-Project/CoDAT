package eshmun.modelrepairer;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class FormulaStringCollection {
	
	List<FormulaString> stringsList;

	
	public FormulaStringCollection() {
		super();
		this.stringsList = new ArrayList<FormulaString>();
	}	
	
	/**
	 * @return List<FormulaString>
	 */
	public List<FormulaString> getStringsList() {
		return stringsList;
	}
	
	/**
	 * return a string that corresponds to a state, transition and formula 
	 * @param KripkeState s
	 * @param Transition t
	 * @param PredicateFormula f
	 * @param String eorX
	 * @param int superScriptOndex
	 * @return String
	 */
	public String getString(String eorX, KripkeState s, KripkeState t, PredicateFormula f, int superScriptOndex)
	{
		FormulaString strF = getFormulaString(eorX, s, t, f, superScriptOndex);
		if(strF != null)
			return strF.toString();
		else
		{
			strF = new FormulaString(eorX, s, t, f, superScriptOndex, stringsList.size() + 1);
			stringsList.add(strF);
			return strF.toString();
		}
			
	}



	/**
	 * checks if there exists a string that corresponds to a state, transition and Formula
	 * @param KripkeState s
	 * @param Transition t
	 * @param  PredicateFormula f
	 * @param  String eorX
	 * @param  int superScriptOndex
	 * @return boolean
	
	public boolean exists(KripkeState s, Transition t, PredicateFormula f, String eorX, int superScriptOndex)
	{
		for (FormulaString fs : stringsList) {
			if(compareStates(fs.getState(),s) && fs.getFormula().equals(f) && fs.getEndState().equals(t)
					&& fs.getEorX().equals(eorX) && fs.getSuperScriptIndex() == superScriptOndex )
				return true;
		}
		return false;
	}*/
	private boolean compareStates(KripkeState s1, KripkeState s2)
	{
		if(s1 == null && s2 == null)
			return true;
		if(s1 != null && s2 != null && s1.getName().trim().equals(s2.getName().trim()))
			return true;
		return false;
	}
	
	/**
	 * @param String eorX
	 * @param KripkeState s
	 * @param Transition t
	 * @param PredicateFormula f
	 * @param int superScriptOndex
	 * @return FormulaString if exists else it returns null
	 */
	public FormulaString getFormulaString(String eorX, KripkeState s, KripkeState t, PredicateFormula f, int superScriptOndex)
	{
		for (FormulaString fs : stringsList) {
			if(					
					CompareEorXandStates(fs, eorX, s, t)
					&& ((fs.getFormula() != null && fs.getFormula().equals(f))  | (fs.getFormula() == null && f == null))
					&& (fs.getSuperScriptIndex() == superScriptOndex) )
				return fs;
		}
		return null;
	}
	
	private boolean CompareEorXandStates(FormulaString fs, String eorX, KripkeState s, KripkeState t)
	{
		if((fs.getEorX() != null && fs.getEorX().equals(eorX)))
		{
			if(eorX.trim().toLowerCase().equals("e"))
			return
			((fs.getState() != null && s != null && fs.getState().getName().trim().equals(s.getName().trim())) | (fs.getState() == null && s == null) )					
			&& ((fs.getEndState() != null && t != null && fs.getEndState().getName().trim().equals(t.getName().trim()))   | (fs.getEndState() == null && t == null))
			;
			if(eorX.trim().toLowerCase().equals("x"))
			{
				if(fs.getState() != null && ( (s != null && fs.getState().getName().trim().equals(s.getName().trim())) | (t!= null &&  fs.getState().getName().trim().equals(t.getName().trim())) ))
				return true;
									
				if((fs.getEndState() != null && ((t!= null &&  fs.getEndState().getName().trim().equals(t.getName().trim())) | (s != null && fs.getEndState().getName().trim().equals(s.getName().trim())))))
					return true;
			}
		}
		return false;
	}
	
	public FormulaString FindByIndex(int index)
	{
		for (FormulaString fs : stringsList) {
			if(fs.getIndex() == index)
				return fs;
		}
		return null;
	}
	
	public List<FormulaString> GetByIndices(String[] indices)
	{
		List<FormulaString> allFs = new ArrayList<FormulaString>();
		for (String str : indices) {
			if(!str.equals("0"))
			{
				boolean valuation = !str.contains("-");
				str = str.replace("-", "");
				FormulaString fs = FindByIndex(Integer.parseInt(str.trim()));
				fs.setValuation(valuation);
				if(fs != null)
					allFs.add(fs);
			}
		}
		return allFs;
	}
	
}
