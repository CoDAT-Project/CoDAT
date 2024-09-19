package eshmun.lts.ReducedKripke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;

public class KripkeBylblWithGrouping extends AbstractReducedKripke {

	Kripke initialModel;
	List<BooleanPredicate> proposotions;
	
	public KripkeBylblWithGrouping(Kripke kripkeModel, List<BooleanPredicate> props) {
		initialModel = kripkeModel;
		proposotions = props;
	}
	/**
	 * This function returns the same kripke object but without states
	 * who does not have any of the ctl formula propositions or labels 
	 * and then by merging states that satisfy the same set of the ctl labels
	 * @param List<BooleanPredicate>  ctl formula propositions or labels 
	 * @return kripke object
	 */
	public Kripke Reduce()
	{
		
		Kripke abstracModel = new Kripke();
		//group states by same set of Labels
		Map<String, ArrayList<KripkeState>> statesByLabelMap = new HashMap<String, ArrayList<KripkeState>>();
		List<KripkeState> absStatesList = initialModel.getStatesList();
		for (KripkeState state : absStatesList) {
			String lbls = (state.toLabelsString().contains("_")) ? state.toLabelsString().replace(state.getName()+"_", "") : state.toLabelsString().replace(state.getName(), "");
			lbls = lbls.equals("")? "none" : lbls;//we have to make sure that no other state has the same name
			if(lbls.contains("_"))
			{
				boolean isnon = true;
				StringBuilder builder = new StringBuilder();
				String[] lblsArray = lbls.split("_");
				Arrays.sort(lblsArray);
				for (String lbl : lblsArray) {
					BooleanPredicate label = new BooleanPredicate(lbl);
					if(proposotions.contains(label))
					{
						builder.append(lbl + ",");
						isnon = false;
					}
				}
				if(!isnon)
				{
					lbls = builder.toString().substring(0, builder.toString().lastIndexOf(",")).trim();
				}
				else
				{
					lbls = "none";
				}
			}
			ArrayList<KripkeState> states ;
			if((states = statesByLabelMap.get(lbls)) != null)
			{
				states.add(state);
			}
			else
			{
				states = new ArrayList<KripkeState>();
				states.add(state);
				statesByLabelMap.put(lbls, states);
			}
		}		
		//end of group states by same set of Labels
		
		
		//create states and their labels
		Object[] keys = statesByLabelMap.keySet().toArray();
		for (Object key : keys) {
			KripkeReplacedState state = new KripkeReplacedState(abstracModel, key.toString(), false, statesByLabelMap.get(key));
			abstracModel.addState(state);
			//create labels
			String[] lbs = key.toString().split(",");
			for (String str : lbs) {
				if(!str.trim().equals(""))
				{
					BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(
							str.trim()));
					PredicateFormulaValuation predValue = new PredicateFormulaValuation(
							abstracModel, state, pred, true);
					state.addPredicateFormulaValuation(predValue);
				}
			}
		}
		/// manage the new model transitions
		List<KripkeState> abstracModelStatesList = abstracModel.getListOfStates();
		for (KripkeState state : abstracModelStatesList) {
			if(state instanceof KripkeReplacedState)
				((KripkeReplacedState) state).manageTransitions();
		}	
		
		return abstracModel;
	}

}
