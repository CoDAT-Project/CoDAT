package eshmun.lts.ReducedKripke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;

public class KripkyByFrmllWithGrouping extends AbstractReducedKripke{

	Kripke initialKripke;
	List<PredicateFormula> frmls;
	
	
	public KripkyByFrmllWithGrouping(Kripke kripkeModel, List<PredicateFormula> formulas) {
		
		initialKripke = kripkeModel;
		frmls = formulas;
	}
	
	public Kripke oldReduce()
	{
		
		Kripke abstracModel = new Kripke();
		List<KripkeState> noneStates = new ArrayList<KripkeState>();
		//group states by same set of Labels
		Map<PredicateFormula, ArrayList<KripkeState>> statesByFormulaMap = new HashMap<PredicateFormula, ArrayList<KripkeState>>();
		List<KripkeState> absStatesList = initialKripke.getStatesList();
		for (KripkeState state : absStatesList) {
			String lbls = (state.toLabelsString().contains("_")) ? state.toLabelsString().replace(state.getName()+"_", "") : state.toLabelsString().replace(state.getName(), "");
			//lbls = lbls.equals("")? "none" : lbls;//we have to make sure that no other state has the same name
			boolean isInNone = true;
			
			if(lbls.contains("_"))
			{
				for (PredicateFormula frml : frmls) {
					if(frml.isSatisfiedBy(state))
					{
						isInNone = false;
						ArrayList<KripkeState> states ;
						if((states = statesByFormulaMap.get(frml)) != null)
						{
							states.add(state);
						}
						else
						{
							states = new ArrayList<KripkeState>();
							states.add(state);
							statesByFormulaMap.put(frml, states);
						}
					}
				}
			}
			if(isInNone)
			{
				noneStates.add(state);
			}
			
		}		
		//end of group states by same set of Labels
		
		Map<String, PredicateFormula> keyStateMap = new HashMap<String, PredicateFormula>();
		//create states and their labels
		Object[] keys = statesByFormulaMap.keySet().toArray();
		for (Object key : keys) {
			List<BooleanPredicate> labels = ((PredicateFormula)key).getBooleanPredicates();
			String stateName = "";
			for (BooleanPredicate bp : labels) {
				stateName = stateName + "_" + bp.toString();
			}
			stateName = stateName.replaceFirst("_", "");
			keyStateMap.put(stateName, ((PredicateFormula)key));
			KripkeReplacedState state = new KripkeReplacedState(abstracModel, stateName, false, statesByFormulaMap.get(key));
			abstracModel.addState(state);
			//create labels
			
			for (BooleanPredicate lbl : labels) {				
					PredicateFormulaValuation predValue = new PredicateFormulaValuation(
							abstracModel, state, lbl, true);
					state.addPredicateFormulaValuation(predValue);
				}
		}
		if(noneStates.size() > 0)
		{
			KripkeReplacedState state = new KripkeReplacedState(abstracModel, "none", false, noneStates);
			abstracModel.addState(state);
		}
		/// manage the new model transitions
		List<KripkeState> abstracModelStatesList = abstracModel.getListOfStates();
		for (KripkeState state : abstracModelStatesList) {
			if(state instanceof KripkeReplacedState)
			{
				((KripkeReplacedState) state).manageTransitions();
			}
		}	
		
		return abstracModel;
	}
	
	/*
	 * I first clone the initial kripke struct to be used in the reduce
	 * replace formulas by labels
	 * reduce according to KripkeBylblWithGrouping
	 * 
	 */
	public Kripke Reduce()
	{
		
		Kripke abstracModel = null;
		try {
			abstracModel = initialKripke.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<KripkeState> noneStates = new ArrayList<KripkeState>();
		//group states by same set of Labels
		List<KripkeState> absStatesList = abstracModel.getStatesList();
		//Map to hold formulas satisfied by each state
		Map<String, ArrayList<PredicateFormula>> statesFormulas = new HashMap<String, ArrayList<PredicateFormula>>();
		for (KripkeState st : absStatesList) {
			statesFormulas.put(st.getName(), new ArrayList<PredicateFormula>());
		}
		//transform formulas to labels ti
		Map<PredicateFormula,String> newLabelsMap = new HashMap<PredicateFormula, String>();
		List<BooleanPredicate> newLabels = new ArrayList<BooleanPredicate>();
		int counter = 1;
		for (PredicateFormula fa : frmls) {
			newLabelsMap.put(fa, "ttt"+counter);
			newLabels.add(new BooleanPredicate("ttt"+counter));
			counter++;
		}
		
		
		for (KripkeState state : absStatesList) {
			//if the state contains labels
			if(state.getLabels().size() > 0)
			{
				for (PredicateFormula frml : frmls) {
					if(frml.isSatisfiedBy(state))
					{
						statesFormulas.get(state.getName()).add(frml);
					}
				}				
			}			
		}
		//replace states labels by the new labels set that represents the formulas
		for (KripkeState state : absStatesList) {
			state.resetLabels();
			state.resetValuations();
			//create labels
			List<PredicateFormula> stateFas = statesFormulas.get(state.getName());
			for (PredicateFormula lbl : stateFas) {			
				
					PredicateFormulaValuation predValue = new PredicateFormulaValuation(
							abstracModel, state, new BooleanPredicate(newLabelsMap.get(lbl)), true);
					state.addPredicateFormulaValuation(predValue);
				}
		}
		KripkeBylblWithGrouping test = new KripkeBylblWithGrouping(abstracModel, newLabels);
		return test.Reduce();
	}
	
	

}
