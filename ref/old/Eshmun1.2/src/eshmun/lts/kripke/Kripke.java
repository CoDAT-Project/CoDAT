package eshmun.lts.kripke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import eshmun.DecisionProcedure.KripkeMergeState;
import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.AtomicPredicate;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.modelchecker.FairnessType;

/**
 * Class that describes a Kripke structure. A kripke structure is represented by
 * a map of states
 * 
 * @author Emile
 * 
 */
public class Kripke {
	protected String name;
	protected Map<String, KripkeState> states;
	protected List<KripkeState> statesList; 
	protected boolean labeledExi;
	protected boolean labeledEni;
	protected Map<String, FairnessType> participantsFairnessType;
	protected static int index = 0;
	protected List<Box> boxesList;
	public Map<String, FairnessType> getParticipantsFairnessType() {
		return participantsFairnessType;
	}

	public void setParticipantsFairnessType(Map<String, FairnessType> participantsFairnessType) {
		this.participantsFairnessType = participantsFairnessType;
	}

	//private Map<KripkeState, KripkeState> statesCache;

	/**
	 * Creates a new empty Kripke structure.
	 */
	public Kripke() {
		states = new HashMap<String, KripkeState>();
		statesList = new ArrayList<KripkeState>();
		//statesCache = new HashMap<KripkeState, KripkeState>();	
		labeledExi = false;
		labeledEni = false;
		this.participantsFairnessType = new HashMap<String, FairnessType>();
		boxesList = new ArrayList<Box>();
	}
	
	public void addParticipantFairnessType(String participant, FairnessType fairnessType) {
		participantsFairnessType.put(participant, fairnessType);
	}
	
	public FairnessType getParticipantFairnessType(String participant) {
		return participantsFairnessType.get(participant);
	}
	
	public void changeStateName(String oldName, String newName)
	{
		states.put(newName, states.get(oldName));
		states.remove(oldName);
	}
	
	public List<PredicateFormula> getAtomicPropositions()
	{
		Set set = new HashSet();
		for (KripkeState state : statesList) {
			set.addAll(state.getLabels());
		}
		
		List<PredicateFormula> list  = new ArrayList<PredicateFormula>();
		list.addAll(set);
		return list;
	}

	/**
	 * Returns a state in this kripke structure
	 * 
	 * @param stateName
	 *            : the name of the state
	 * @return the state referenced by the stateName
	 */
	public KripkeState getState(String stateName) {
		return states.get(stateName);
	}
	
	/**
	 * returns a collection of all states in the Kripke structure
	 * @return
	 */
	/*public Collection<KripkeState> getStates() {
		return states.values();
	}*/
	
	/**
	 * returns a collection of all states in the Kripke structure
	 * @return
	 */
	public Collection<KripkeState> getStates() {
		return statesList;
	}
	
	public Map<String, KripkeState> getStatesMap() {
		return states;
	}
	
	public void addBox(Box box)
	{
		if(!boxesList.contains(box))
		boxesList.add(box);
	}
	public void removeBox(Box box)
	{
		boxesList.remove(box);
	}
	
	public List<KripkeState> getListOfStates() {
		return statesList;
	}
	
	/**
	 * returns a list of all states in the Kripke structure
	 * @return the list of all kripke states
	 */
	public List<KripkeState> getStatesList() {
		List<KripkeState> kripkeStates = new ArrayList<KripkeState>();
		kripkeStates.addAll(states.values());
		return kripkeStates;
	}
	
	public List<KripkeState> getStartStatesList() {
		List<KripkeState> startStates = new ArrayList<KripkeState>();
		Collection<KripkeState> kripkeStates = states.values();
		Iterator<KripkeState> iter = kripkeStates.iterator();
		while (iter.hasNext()) {
			KripkeState state = iter.next();
			if (state.isStart()) {
				startStates.add(state);
			}
		}
		return startStates;
	}
	
	/**
	 * retrieves a kripke state that contains all the valuations. a valuation
	 * is in a kripke state is the kripke state contains the valuation and has 
	 * the same value
	 * 
	 * @param valuations list of valuations used as search criterea for a kripke state
	 * @return the kripke state that meets the valuations input
	 */
	public KripkeState getState(Collection<Valuation> valuations) {
		KripkeState retState = null;
		for (KripkeState state : getStatesList()) {
			if (state.getValuations().containsAll(valuations)) {
				retState = state;
				break;
			}
		}
		return retState;
	}
	

	/**
	 * Adds a new state to the kripke structure
	 * 
	 * @param state the new state to be added
	 */
	public void addState(KripkeState state) {
		states.put(state.getName(), state);
		statesList.add(state);
		//statesCache.put(state, state);
	}
	
	public void addState(KripkeState state, int index) {
		states.put(state.getName(), state);
		if(index < statesList.size())
		{
			statesList.add(index, state);
		}
		else
			statesList.add(state);
		//statesCache.put(state, state);
	}
	
	public int GetStateIndex(String stateName)
	{
		int count = 0;
		for (KripkeState child : statesList) {
			if(child.getName().equals(stateName))
				return count;
			count++;
		}
		return -1;
	}
	
	

	/**
	 * Removes a state from the kripke structure
	 * 
	 * @param stateName the name of the state to be removed
	 */
	public void removeState(String stateName) {
		
		KripkeState removedState = states.remove(stateName);
		//statesCache.remove(removedState);
		Transition [] revTransArr = new Transition[removedState.getReverseTransitions().size()];
		for (Transition revTrans : removedState.getReverseTransitions().toArray(revTransArr)) {
			Transition transToRemove = new Transition(revTrans.getEndState(), revTrans.getStartState(), revTrans.getName(), revTrans.getTaskName());
			revTrans.getEndState().removeOutgoingTransition(transToRemove);
		}
		Transition [] transArr = new Transition[removedState.getTransitions().size()];
		for (Transition trans : removedState.getTransitions().toArray(transArr)) {
			removedState.removeOutgoingTransition(trans);
		}
		statesList.remove(removedState);
	}
	
public void msakrRemoveState(String stateName) {
		
		KripkeState removedState = states.remove(stateName);
		//statesCache.remove(removedState);
		Transition [] revTransArr = new Transition[removedState.getReverseTransitions().size()];
		for (Transition revTrans : removedState.getReverseTransitions().toArray(revTransArr)) {
			Transition transToRemove = new Transition(revTrans.getEndState(), revTrans.getStartState(), revTrans.getName(), revTrans.getTaskName());
			revTrans.getEndState().removeOutgoingTransition(transToRemove);
		}
		Transition [] transArr = new Transition[removedState.getTransitions().size()];
		for (Transition trans : removedState.getTransitions().toArray(transArr)) {
			removedState.removeOutgoingTransition(trans);
		}
		int l = statesList.size();
		for (int i=0; i < l; i++) {
			if(statesList.get(i).getName().equals(stateName))
			{
				statesList.remove(i);
				break;
			}
			
		}
	}
	
	
	
	public boolean containsName(String stateName)
	{
		for (KripkeState child : statesList) {
			if(child.getName().equals(stateName))
				return true;
		}
		return false;
	}

	/**
	 * Adds a new transition between startState and endState
	 * 
	 * @param startState the start state of this transition
	 * @param endState the end state of this transition
	 * @param transitionName the name of this transition
	 */
	public void addTransition(KripkeState startState, KripkeState endState, String transitionName, String taskName) {
		Transition transition = new Transition(startState, endState, transitionName, taskName);
		startState.addOutgoingTransition(transition);
	}
	
	public void addTransition(String startStateName, String endStateName, String transitionName, String taskName) {
		KripkeState startState = getState(startStateName);
		KripkeState endState = getState(endStateName);
		addTransition(startState, endState, transitionName, taskName);
	}
	
	/**
	 * Adds a new transition from startState to the endState the transitionToAdd
	 * 
	 * @param startState the start state of this transition
	 * @param transitionToAdd the transition to add
	 */
	public void addTransition(KripkeState startState, Transition transitionToAdd) {
		startState.addOutgoingTransition(transitionToAdd);
	}

	/**
	 * Removes a transition from the kripke structure
	 * 
	 * @param startState the startState where the transition is being removed
	 * @param transition the transition object to be removed
	 */
	public void removeTransition(KripkeState startState, Transition transition) {
		startState.removeOutgoingTransition(transition);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		Iterator<String> stateNamesIter = states.keySet().iterator();
		List<String> stateNames = new ArrayList<String>();
		while (stateNamesIter.hasNext()) {
			stateNames.add(stateNamesIter.next());
		}
		
		Collections.sort(stateNames);
		
		for (String stateName : stateNames) {
			KripkeState state = states.get(stateName);
			builder.append(state.toString());
//			builder.append(state.getName() + " " + state.isStart());
//			
//			for (Valuation valuation : state.getValuations()) {
//				String valuationToString = valuation.toString();
//				if (valuationToString != null && !valuationToString.equals("")) {
//					builder.append("\n\t" + valuation);
//				}
//			}
//			builder.append("\n");
//			for (Transition transition : state.getTransitions()) {
//				builder.append("\n\t" + transition.getName() + "->" + transition.getEndState().getName());
//			}
			builder.append("\n\n");
		}

		return builder.toString();
	}
	
	//public KripkeState getKripkeState(KripkeState state) {
	//	return statesCache.get(state);
	//}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Kripke clone() throws CloneNotSupportedException {
		Kripke cloneKripke = new Kripke();
		cloneKripke.setName(this.getName());
		List<KripkeState> statesList = this.getListOfStates();
		for (KripkeState state : statesList) {
			KripkeState clonedState = state.clone();
			clonedState.setKripke(cloneKripke);
			
			for (Valuation val : clonedState.getValuations()) {
				val.setKripke(cloneKripke);
				val.setKripkeState(clonedState);
			}
			
			
			cloneKripke.addState(clonedState);
		}
		
		for (KripkeState state: this.getStatesList()) {
			for (Transition transition : state.getTransitions()) {
				cloneKripke.addTransition(transition.getStartState().getName(), transition.getEndState().getName(), transition.getName(), transition.getTaskName());
			}
		}
		cloneKripke.setLabeledExi(this.isLabeledExi());
		cloneKripke.setLabeledEni(this.isLabeledEni());
		cloneKripke.setParticipantsFairnessType(new HashMap<String, FairnessType>(getParticipantsFairnessType()));
		return cloneKripke;
	}
	
	
	
	public KripkeState getState(int i){
		return statesList.get(i);
	}

	public boolean isLabeledExi() {
		return labeledExi;
	}

	public void setLabeledExi(boolean labeledExi) {
		this.labeledExi = labeledExi;
	}

	public boolean isLabeledEni() {
		return labeledEni;
	}

	public void setLabeledEni(boolean labeledEni) {
		this.labeledEni = labeledEni;
	}
	
	/**
	 * Generate dot (directed graph description language) text
	 * @param transitions transitions to be removed from the graph
	 * @return
	 */
	public String[] GenerateDotText(Collection<Transition> transitionsToBeDeleted) 
	{
		StringBuilder builder = new StringBuilder();
		builder.append("digraph kripkeModel  { :");
		for (KripkeState state : statesList) 
		{
			if(state.isStart())
				builder.append(state.toLabelsString().replace(",", "_") + " [fillcolor = green, style=filled, label=\"" + state.getName() + "(" +
			state.toLabelsString().replace(",", "_").replace(state.getName() + ((state.getLabels().size() > 0 ) ? "_" : ""), "").replace("_", ",") + ")\"];:");
			else
				builder.append(state.toLabelsString().replace(",", "_") + " [label=\"" + state.getName() + "(" +
			state.toLabelsString().replace(",", "_").replace(state.getName() + ((state.getLabels().size() > 0 ) ? "_" : ""), "").replace("_", ",") + ")\"];:");
		}
		for (KripkeState state : statesList) {
			Collection<Transition> trns = state.getTransitions();
			for (Transition trans : trns) {				
				if(TransListContains(transitionsToBeDeleted, trans))
				{
					builder.append(state.toLabelsString().replace(",", "_") + "->" + trans.getEndState().toLabelsString().replace(",", "_") + " [style=dashed];:");
				}
				else
					builder.append(state.toLabelsString().replace(",", "_") + "->" + trans.getEndState().toLabelsString().replace(",", "_") + ";:");
				
			}
		}
		builder.append("}");
		return builder.toString().split(":");
	}
	
	private boolean TransListContains(Collection<Transition> transitionsToBeDeleted, Transition trans)
	{
		for (Transition trs : transitionsToBeDeleted) {
			if(trs.getStartState().getName().equals(trans.getStartState().getName())
					&& trs.getEndState().getName().equals(trans.getEndState().getName())
					&& trs.getName().equals(trans.getName()))
			{
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Generate dot (directed graph description language) text
	 */
	public String[] GenerateDotText() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append("digraph kripkeModel  { :");
		for (KripkeState state : statesList) 
		{
			if(state.isStart())
				builder.append(state.toLabelsString().replace(",", "_") + " [fillcolor = green, style=filled, label=\"" + state.getName() + "(" +
			state.toLabelsString().replace(",", "_").replace(state.getName() + ((state.getLabels().size() > 0 ) ? "_" : ""), "").replace("_", ",") + ")\"];:");
			else
				builder.append(state.toLabelsString().replace(",", "_") + " [label=\"" + state.getName() + "(" +
			state.toLabelsString().replace(",", "_").replace(state.getName() + ((state.getLabels().size() > 0 ) ? "_" : ""), "").replace("_", ",") + ")\"];:");
		}
		for (KripkeState state : statesList) {
			Collection<Transition> trns = state.getTransitions();
			for (Transition trans : trns) {			
					builder.append(state.toLabelsString().replace(",", "_") + "->" + trans.getEndState().toLabelsString().replace(",", "_") + ";:");
				
			}
		}
		builder.append("}");
		return builder.toString().split(":");
	}
	

	
	/**
	 * This function returns the same kripke object but without states
	 * who does not have any of the ctl formula propositions or labels
	 * and grouping all those in one state 
	 * @param List<BooleanPredicate>  ctl formula propositions or labels 
	 * @return kripke object
	 */
	/*public Kripke getAbstractedModelByLabels(List<BooleanPredicate> proposotions)
	{
		Kripke abstracModel = null;
		
		try {
			
			abstracModel = clone();
			List<KripkeState> abstractedStates = new ArrayList<KripkeState>();
			boolean move = true;
			boolean abstractionExists = false;
			List<KripkeState> clonedStatesList = abstracModel.getStatesList();
			// remove not needed states
			for (KripkeState state : statesList) {
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
				
				KripkeMergeState newState = new KripkeMergeState(abstracModel, "none", false, abstractedStates);
				newState.replaceMergedStates();
			}
			else
				return this;
		} 
		catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return abstracModel;
	}*/
	
	public static boolean formCycle(List<KripkeState> states)
	{
		List<List<KripkeState>> SCC = new ArrayList<List<KripkeState>>();
		Stack st = new Stack<KripkeState>();
		for (KripkeState v : states) {
			if(!v.marked)
				DFS(v, st, states, SCC);
		}
		index = 0;
		for (KripkeState state : states) {
			state.low = -1;
			state.index = -1;
			state.marked = false;
		}
		if(SCC.size() > 0)
		{
			return true;
		}
		return false;
	}
	private static void DFS(KripkeState istate, Stack<KripkeState> st, List<KripkeState> states, List<List<KripkeState>> SCC)
	{
		
		istate.low = index;
		istate.index = index;
		index = index + 1;
		istate.marked = true;
		st.push(istate);
		List<Transition> trans = (List<Transition>) istate.getTransitions();
		for (Transition transition : trans) {
			if(TrxContains(states, transition.getEndState().getName()))
			{
				KripkeState endState = transition.getEndState();
				if(!endState.marked)
				{
					 DFS(endState, st, states, SCC);
					 istate.low = Math.min(istate.low, endState.low);
				}
				else if(StackContains(endState, st))//how come the state contains s4
					 istate.low = Math.min(istate.low, endState.low);
			}
		}
		if(istate.low == istate.index)
		{
			List<KripkeState> cStates = new ArrayList<KripkeState>();
			KripkeState w;
			do
			{
				w = (KripkeState) st.pop();
				cStates.add(w);
			}
			while(!w.getName().equals(istate.getName()));
			if(cStates.size() > 1)
				SCC.add(cStates);
		}
		
	}
	
	private static boolean StackContains(KripkeState istate, Stack<KripkeState> st)
	{
		for (KripkeState w : st)
		{
			if(w.getName().equals(istate.getName()))
				return true;
		}
		return false;
	}
	
	protected static List<BooleanPredicate> GetSharedLabels(List<KripkeState> states)
	{
		List<BooleanPredicate> sharedLabels = new ArrayList<BooleanPredicate>();
		for (KripkeState state : states) {
			List<BooleanPredicate> stateLabels = state.getLabels();
			for (BooleanPredicate lbl : stateLabels) {
				if(!sharedLabels.contains(lbl))
					sharedLabels.add(lbl);
			}
		}
		return sharedLabels;		
	}
	private static boolean TrxContains(List<KripkeState> states, String stateName)
	{
		for (KripkeState state : states) {
			if(state.getName().equals(stateName))
				return true;
		}
		return false;
	}
	protected static List<KripkeState> getReachableStates(KripkeState state, List<KripkeState> allowedStates)
	{
		List<KripkeState> contigeous = new ArrayList<KripkeState>();
		contigeous.add(state);
		Stack<KripkeState> stack = new Stack<KripkeState>();
		stack.push(state);
		while(!stack.empty())
		{
			KripkeState st = (KripkeState)stack.pop();
			Collection<Transition> stateTransitions = st.getTransitions();
			for (Transition trans : stateTransitions) {
				KripkeState endState = trans.getEndState();
				if(allowedStates.contains(endState) && !contigeous.contains(endState))
				{
					contigeous.add(endState);
					stack.push(endState);
				}
			}
			Collection<Transition> stateReversTransitions = st.getReverseTransitions();
			for (Transition trans : stateReversTransitions) {
				KripkeState endState = trans.getEndState();
				if(allowedStates.contains(endState) && !contigeous.contains(endState))
				{
					contigeous.add(endState);
					stack.push(endState);
				}
			}
		}
		
		return contigeous;
	}
	
	
	
	
	
	
	

}
