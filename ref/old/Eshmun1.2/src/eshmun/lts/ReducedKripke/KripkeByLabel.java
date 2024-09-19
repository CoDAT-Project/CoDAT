/**
 * 
 */
package eshmun.lts.ReducedKripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

/**
 * @author ms186
 *
 */
public class KripkeByLabel extends AbstractReducedKripke {
	
	Kripke initialKripke;
	List<BooleanPredicate> atomicProposotions;

	/**
	 * 
	 */
	public KripkeByLabel(Kripke kripkeModel, List<BooleanPredicate> atomicProps) {
		initialKripke = kripkeModel;
		atomicProposotions = atomicProps;
	}
	
	/**
	 * This function returns a new kripke structure
	 * by grouping adjacent states that have the same common
	 * labels with the ctl formula being repaired 
	 * @param List<BooleanPredicate>  ctl formula propositions or labels 
	 * @return kripke object
	 */
	public Kripke Reduce()
	{
		Kripke abstracModel = new Kripke();
		//group states by same set of Labels
		Map<ArrayList<BooleanPredicate>, ArrayList<KripkeState>> statesByLabelMap = new HashMap<ArrayList<BooleanPredicate>, ArrayList<KripkeState>>();
		ArrayList<KripkeState> noneStates = new ArrayList<KripkeState>();
		List<KripkeState> absStatesList = initialKripke.getStatesList();
		for (KripkeState state : absStatesList) 
		{
			ArrayList<BooleanPredicate> commonLabels = new ArrayList<BooleanPredicate>(state.getLabels());
			commonLabels.retainAll(atomicProposotions);
			if(commonLabels != null && commonLabels.size() >0)
			{
				Collections.sort(commonLabels);
				ArrayList<KripkeState> states ;
				if((states = statesByLabelMap.get(commonLabels)) != null)
				{
					states.add(state);
				}
				else
				{
					states = new ArrayList<KripkeState>();
					states.add(state);
					statesByLabelMap.put(commonLabels, states);
				}
			}
			else
			{
				noneStates.add(state);
			}
		}		
		//end of group states by same set of Labels
		
		
		//create states and their labels
		Object[] keys = statesByLabelMap.keySet().toArray();
		for (Object key : keys) 
		{
			List<BooleanPredicate> currentLabels = (List<BooleanPredicate>) key;
			List<KripkeState> labelsStates = statesByLabelMap.get(key);
			while(labelsStates.size() > 0)
			{
				List<KripkeState> adjacentStates = getReachableStates(labelsStates.get(0), labelsStates);
				labelsStates.removeAll(adjacentStates);
				KripkeReplacedState state = new KripkeReplacedState(abstracModel, toLabelsString(currentLabels), false, adjacentStates);
				abstracModel.addState(state);
				//create labels
				for (BooleanPredicate pred : currentLabels) 
				{
						PredicateFormulaValuation predValue = new PredicateFormulaValuation(
								abstracModel, state, pred, true);
						state.addPredicateFormulaValuation(predValue);
				}
			}
			
		}
		//Create none states
		if(noneStates.size() > 0)
		{
			KripkeReplacedState state = new KripkeReplacedState(abstracModel, "none_1", false, noneStates);
			abstracModel.addState(state);
		}
		
		/// manage the new model transitions
		List<KripkeState> abstracModelStatesList = abstracModel.getListOfStates();
		for (KripkeState state : abstracModelStatesList) {
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
	
	
	
	
	/*public Kripke getAbsModelByLblsNoGrp(List<BooleanPredicate> proposotions)
	{
		Kripke abstracModel = null;
		
		try {
			
			abstracModel = clone();
			List<KripkeState> abstractedStates = new ArrayList<KripkeState>();
			boolean move = true;
			boolean abstractionExists = false;
			List<KripkeState> clonedStatesList = abstracModel.getStatesList();
			// remove not needed states
			for (KripkeState state : clonedStatesList) {
				move = true;
				List<BooleanPredicate> stateLabels = state.getLabels();
				for (BooleanPredicate pred : stateLabels) {
					BooleanPredicate bpred = (BooleanPredicate) pred;
					if(proposotions.contains(bpred))
					{
						move = false;
						break;
					}
				}
				if(move)
				{
					abstractedStates.add(state);
					//abstracModel.removeState(state.getName());
					abstractionExists = true;
				}				
			}
			/// manage the new model transitions
			if(abstractionExists)
			{
				
				
				List<KripkeState> tempList = new ArrayList<KripkeState>();
				tempList.addAll(abstractedStates);
				int count=0;
				for (KripkeState state : abstractedStates) {
					if(tempList.contains(state))
					{
						List<KripkeState> contigeous = getReachableStates(state, abstractedStates);
						if(contigeous.size() > 1)
						{
							count++;							
							tempList.removeAll(contigeous);
							//check if state is start state
							KripkeReplacedState newsSate = new KripkeReplacedState(abstracModel, "none_" + count , false, contigeous);
							abstracModel.addState(newsSate);					
						}
						else
						{
							abstracModel.addState(state);	
						}
					}
				}
				
				List<KripkeState> abstracModelStatesList = abstracModel.getListOfStates();
				for (KripkeState state : abstracModelStatesList) {
					if(state instanceof KripkeReplacedState)
					{
						//((KripkeReplacedState) state).manageTransitions();
						((KripkeReplacedState) state).manageAllTransitions();
					}
				}
				for (KripkeState state : abstracModelStatesList) {
					if(state instanceof KripkeReplacedState)
					{
						Iterator<Transition> trss = state.getTransitions().iterator();
						while(trss.hasNext())
						{
							Transition outTrs = trss.next();
							if(outTrs.getStartState().equals(outTrs.getEndState()))
								trss.remove();
							
						}
						while(trss.hasNext())
						{
							Transition outTrs = trss.next();
							state.removeOutgoingTransition(outTrs);
						}
					}
				}
				for (KripkeState kripkeState : abstractedStates) {
					abstracModel.removeState(kripkeState.getName());
				}
				
			}
			else
				return abstracModel;
		} 
		catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return abstracModel;
	}*/
	
	

}
