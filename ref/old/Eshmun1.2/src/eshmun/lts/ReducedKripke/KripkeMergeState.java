package eshmun.lts.ReducedKripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

/**
 * A Kripke state that is a result of merging a list of kripke states
 * @author MSakr
 *
 */
public class KripkeMergeState extends KripkeState {

	private List<KripkeState> mergedStates;
	private Map<Transition, List<Transition>> replacedTransitions;
	
	//private  List<Transition> deletedTransitions;
	
	/**
	 * The constructor will merge all the statesToBeMerged by one state,
	 * remove these states from the kripke model and replace them with the new state
	 * @param kripke
	 * @param stateName
	 * @param statesToBeMerged
	 */
	public KripkeMergeState(Kripke kripke, String stateName, List<KripkeState> statesToBeMerged, String[] labels) {
		super(kripke, stateName, false);
		mergedStates = statesToBeMerged;
		replacedTransitions = new HashMap<Transition, List<Transition>>();
		for (String lb : labels) 
		{
			BooleanPredicate pred = new BooleanPredicate(new BooleanVariable(lb));
				PredicateFormulaValuation predValue = new PredicateFormulaValuation(
						kripke, this, pred, true);
				this.addPredicateFormulaValuation(predValue);
		}
	}
	private void checkForCycles()
	{
		boolean hasCycle = Kripke.formCycle(mergedStates);
		if(hasCycle)
		{
			boolean hasSelfLoop = false;
			Collection<Transition> transitions = this.getTransitions();
			for (Transition transition : transitions) {
				if(this.getName().equals(transition.getEndState().getName()))
					hasSelfLoop = true;
			}
			if(!hasSelfLoop)
			{
				Transition newTrans = new Transition(this, this, this.getName() + System.currentTimeMillis(), this.getName() + System.currentTimeMillis());
				this.addOutgoingTransition(newTrans);
				
			}
		}
	}
	
	public void replaceMergedStates()
	{
		checkForCycles();
		for (KripkeState state : mergedStates) {
			if(state.isStart())
				this.setStart(true);			
			Collection<Transition> stateTransitions = state.getTransitions();
			for (Transition trans : stateTransitions) {
				if(!specialStateContain(mergedStates,trans.getEndState()))
				{
					KripkeState endState = trans.getEndState();
					Transition newTrans = new Transition(this, endState, trans.getName(), trans.getName());
					StringBuilder trnsName = new StringBuilder ();
					StringBuilder trnsTaskName = new StringBuilder ();
					if(!specialTransitionContain(transitions, newTrans, trnsName, trnsTaskName))
					{
						addOutgoingTransition(newTrans);						
					}
					else
					{
						newTrans =  new Transition(this, endState, trnsName.toString(), trnsTaskName.toString());
					}
					AddNewReplacedTransition(newTrans, trans);
				}
				
			}
			stateTransitions = state.getReverseTransitions();
			for (Transition trans1 : stateTransitions) {
				if(!specialStateContain(mergedStates,trans1.getEndState()))
				{
										
					KripkeState startState = trans1.getEndState();
					Transition newTrans = new Transition(startState, this, trans1.getName(), trans1.getName());
					StringBuilder trnsName = new StringBuilder ();
					StringBuilder trnsTaskName = new StringBuilder ();
					if(!specialTransitionContain(startState.getTransitions(), newTrans, trnsName, trnsTaskName))
					{						
						startState.addOutgoingTransition(newTrans);						
					}
					else
					{
						newTrans = new Transition(startState, this, trnsName.toString(), trnsTaskName.toString());
					}
					AddNewReplacedTransition(newTrans, trans1);
				}
				
			}
			this.kripke.msakrRemoveState(state.getName());
		}
		this.kripke.addState(this);
	}
	
	
	public void splitMergedState()
	{
		Map<Transition, List<Transition>> rransitions = replacedTransitions;
		for (KripkeState state : mergedStates) {
			this.kripke.addState(state);
		}
		
		Collection<Transition> stateTransitions = getTransitions();
		
		stateTransitions = getReverseTransitions();
		
	}

	public List<KripkeState> getMergedStates() {
		return mergedStates;
	}


	public Map<Transition, List<Transition>> getReplacedTransitions() {
		return replacedTransitions;
	}

	public void setMergedStates(List<KripkeState> mergedStates) {
		this.mergedStates = mergedStates;
	}
	
	private void AddNewReplacedTransition(Transition key, Transition value)
	{
		if(replacedTransitions.containsKey(key))
		{
			replacedTransitions.get(key).add(value);
		}
		else
		{
			List<Transition> trs = new ArrayList<Transition>();
			trs.add(value);
			replacedTransitions.put(key, trs);
		}
	}
	
	
	

}
