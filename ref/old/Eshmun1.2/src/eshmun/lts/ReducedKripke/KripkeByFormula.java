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

public class KripkeByFormula extends AbstractReducedKripke{

	
	Kripke initialKripke;
	List<PredicateFormula> frmls;
	
	
	public KripkeByFormula(Kripke kripkeModel, List<PredicateFormula> formulas) {
		
		initialKripke = kripkeModel;
		frmls = formulas;
	}
	public Kripke Reduce()
	{
		Kripke abstracModel = new Kripke();
		//variable used to group states by same set of frmls
		Map<ArrayList<PredicateFormula>, ArrayList<KripkeState>> statesByFrmllMap = new HashMap<ArrayList<PredicateFormula>, ArrayList<KripkeState>>();
		List<KripkeState> abstractedStates = new ArrayList<KripkeState>();
		List<KripkeState> nonedStates = new ArrayList<KripkeState>();
		boolean none = true;
		int noneIndex = 0;
		boolean abstractionExists = false;
		
		// group states by formula
		ArrayList<KripkeState> tempStates;
		List<KripkeState> abstracModelStatesList = initialKripke.getStatesList();
		for (KripkeState state : abstracModelStatesList) {
			none = true;
			ArrayList<PredicateFormula> stFrml = new ArrayList<PredicateFormula>();
			for (PredicateFormula frml : frmls) {
				if(frml.isSatisfiedBy(state))
				{
					none = false;
					stFrml.add(frml);					
				}
			}
			if(none)
			{
				nonedStates.add(state);
			}
			else
			{
				if((tempStates = statesByFrmllMap.get(stFrml)) != null)
				{
					tempStates.add(state);
				}
				else
				{
					tempStates = new ArrayList<KripkeState>();
					tempStates.add(state);
					statesByFrmllMap.put(stFrml, tempStates);
				}
			}
		}
		/// Create the new States
		Object[] keys = statesByFrmllMap.keySet().toArray();
		for (Object key : keys) 
		{
			List<KripkeState> labelsStates = statesByFrmllMap.get(key);
			while(labelsStates.size() > 0)
			{
				List<BooleanPredicate> currentLabels = GetSharedLabels(labelsStates);
				List<KripkeState> adjacentStates = getReachableStates(labelsStates.get(0), labelsStates);
				labelsStates.removeAll(adjacentStates);
				KripkeReplacedState state = new KripkeReplacedState(abstracModel, toLabelsString(currentLabels), false, adjacentStates);
				abstracModel.addState(state);
				//create labels (shared labels between all grouped states
				for (BooleanPredicate pred : currentLabels) 
				{
						PredicateFormulaValuation predValue = new PredicateFormulaValuation(
								abstracModel, state, pred, true);
						state.addPredicateFormulaValuation(predValue);
				}
			}
			
		}
		//Create none States
		if(nonedStates.size() > 0)
		{
			while(nonedStates.size() > 0) {				
				List<KripkeState> adjacentStates = getReachableStates(nonedStates.get(0), nonedStates);
				nonedStates.removeAll(adjacentStates);
				KripkeReplacedState newState = new KripkeReplacedState(abstracModel, "none_" + noneIndex, false, adjacentStates);
				abstracModel.addState(newState);
				noneIndex ++;
			}
		}
		
		/// manage the new model transitions
	List<KripkeState> abstracStatesList = abstracModel.getListOfStates();
	for (KripkeState state : abstracStatesList) {
		if(state instanceof KripkeReplacedState)
			((KripkeReplacedState) state).manageTransitions();
	}	
	
		
		return abstracModel;
	}
	
	
	
	private String toLabelsString(List<BooleanPredicate> labels)
	{
		StringBuilder builder = new StringBuilder();
		for (BooleanPredicate label : labels) {
			builder.append(label.toString()+ "_");
		}
		String toRet = builder.toString();
		return toRet.substring(0, toRet.lastIndexOf("_")).trim();
	}

}
