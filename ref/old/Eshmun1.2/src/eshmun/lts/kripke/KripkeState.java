package eshmun.lts.kripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanPredicate;

/**
 * Class that represents a State inside a Kripke structure. A state has a name,
 * a set of atomic propositions/variables valuations and the set of outgoing
 * transitions
 * 
 * @author Emile
 * 
 */
// TODO add constraint to kripke structure to avoid states with duplicate name,
// and the structure is total
// add constraint invariants methods to do the check
public class KripkeState {
	/** the name of the sate */
	private String name;
	/** is start state of the parent kripke structure */
	private boolean isStart;
	/** the parent kripke structure */
	protected Kripke kripke;
	/** list of valuation values on the state */
	private Map<String, Valuation> valuations;
	/** list of outgoing trasitions from the state */
	protected List<Transition> /* Map<String, Transition> */transitions;
	private List<Transition> reverseTransitions;
	private List<BooleanPredicate> Labels;
	/** list of subformuleas and if they where checked on this state to be used in model repairer */
	public List<PredicateFormula> OldS;
	public Queue<PredicateFormula> NewS;
	public int index;
	public int low;
	boolean marked = false;
	boolean isPartOfMerge;

	/**
	 * create a new state
	 * 
	 * @param k
	 *            parent kripke structure
	 * @param stateName
	 *            name of the state
	 * @param isStart
	 *            : declares whether state is start state.
	 */
	public KripkeState(Kripke kripke, String stateName, boolean isStart) {
		this.name = stateName;
		this.isStart = isStart;
		this.kripke = kripke;
		this.valuations = new HashMap<String, Valuation>();
		this.transitions = new ArrayList<Transition>();
		this.reverseTransitions = new ArrayList<Transition>();
		this.Labels = new ArrayList<BooleanPredicate>();
		this.OldS= new ArrayList<PredicateFormula>();  
		this.NewS = new LinkedList<PredicateFormula>();
		isPartOfMerge = false;
	}
	
	

	public boolean isPartOfMerge() {
		return isPartOfMerge;
	}



	public void setPartOfMerge(boolean isPartOfMerge) {
		this.isPartOfMerge = isPartOfMerge;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void changeName(String newName)
	{
		kripke.changeStateName(this.name, newName);
		this.name = newName;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public Kripke getKripke() {
		return kripke;
	}
	public List<BooleanPredicate> getLabels() {
		return Labels;
	}
	
	public void resetLabels() {
		this.Labels = new ArrayList<BooleanPredicate>();
	}

	@Override
	public KripkeState clone() throws CloneNotSupportedException {
		KripkeState clonedState = new KripkeState(kripke, getName(), isStart());
		for (Valuation val : this.valuations.values()) {
			Valuation clonedVal = (Valuation) val.clone();
			clonedState.addValuation(clonedVal);
		}
		//msakr
		for (PredicateFormula frml : this.Labels) {
			if(frml.getClass().equals(BooleanPredicate.class))
			{
				BooleanPredicate bPred = (BooleanPredicate)frml;
				BooleanPredicate newPred = bPred.clone();		
				clonedState.Labels.add(newPred);
			}
		}
		return clonedState;
	}
	
	

	/**
	 * retrieves a vation from the state by its name
	 * 
	 * @param name
	 *            the name of the valuation
	 * @return
	 */
	public Valuation getValuation(String name) {
		if (name != null) {
			return valuations.get(name);
		}
		return null;
	}

	/**
	 * Adds a new valuation of AtomicProposition/variable that is satisfied in
	 * this state.
	 * 
	 * @param atomicProposition
	 *            : atomic proposition to be added
	 */
	public void addValuation(Valuation valuation) {
		valuations.put(valuation.getName(), valuation);
	}
	public void addPredicateFormulaValuation(PredicateFormulaValuation valuation) {
		valuations.put(valuation.getName(), valuation);
		if(valuation.getFormula() instanceof BooleanPredicate)
		{
			Labels.add((BooleanPredicate) valuation.getFormula());
		}
		
	}

	/**
	 * @return the list of valuations in the state
	 */
	public Collection<Valuation> getValuations() {
		return valuations.values();
	}
	
	public void resetValuations() {
		this.valuations = new HashMap<String, Valuation>();
	}

	/**
	 * set the valuation value in the state
	 * 
	 * @param name
	 *            the name of the valuation
	 * @param value
	 *            the new value of the valuation
	 */
	public void setValuation(String name, Object value) {
		if (name != null && !"".equals(name)) {
			Valuation valuation = getValuation(name);
			if (valuation != null) {
				valuation.setValue(value);
			}
		}
	}

	/**
	 * @return all outgoing transitions
	 */
	public Collection<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * Adds a new outgoing transition from this state.
	 * 
	 * @param transition
	 *            : The transition to be added
	 */
	public void addOutgoingTransition(Transition transition) {
		transitions.add(transition);
		Transition reverseTransition = new Transition(transition.getEndState(), transition.getStartState(), transition.getName(), transition.getTaskName());
		transition.getEndState().addReverseTransition(reverseTransition);
	}

	/**
	 * removes an outgoing transition from the state
	 * 
	 * @param transition
	 *            the transition to remove
	 */
	public void removeOutgoingTransition(Transition transition) {
		boolean t = transitions.remove(transition);
		if (t) {
			Transition reverseTransition = new Transition(transition.getEndState(), transition.getStartState(), transition.getName(), transition.getTaskName());
			transition.getEndState().removeReverseTrasition(reverseTransition);
		}

	}

	/**
	 * Adds a new reverse transition from this state.
	 * 
	 * @param transition
	 *            : The reverse transition to be added
	 */
	public void addReverseTransition(Transition transition) {
		reverseTransitions.add(transition);
	}

	/**
	 * removes a reverse transition from the state
	 * 
	 * @param transition
	 *            the transition to remove
	 */
	public void removeReverseTrasition(Transition transition) {
		reverseTransitions.remove(transition);
	}

	public List<Transition> getReverseTransitions() {
		return reverseTransitions;
	}

	/**
	 * adds a list of valuations to the valuations of the state
	 * 
	 * @param valuations
	 *            list of valuations to add
	 */
	public void addValuations(List<Valuation> valuations) {
		for (Valuation valuation : valuations) {
			valuation.setKripke(this.getKripke());
			valuation.setKripkeState(this);
			addValuation(valuation);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KripkeState) {
			KripkeState kripkeState = (KripkeState) obj;
			Map<String, Valuation> valuations = kripkeState.valuations;
			if (this.valuations == null) {
				return valuations == null; //true if both are empty
			}
			if (valuations == null) { 
				return false;
			}
			return equalMaps(this.valuations, valuations);
//			Collection<Valuation> valuations = kripkeState.getValuations();
//			return this.valuations.values().containsAll(valuations);
		}
		return super.equals(obj);
	}
	
	
	
	boolean equalMaps(Map m1, Map m2) {
		if (m1.size() != m2.size())
			return false;
		for (Object key : m1.keySet()) {
			if (!m1.get(key).equals(m2.get(key))) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int hash = 1;
		if (valuations != null) {
			hash +=  valuations.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// builder.append("[");
		// for (Valuation valuation : getValuations()) {
		// builder.append(" " + valuation);
		// }
		// builder.append("\n]");
		// for (Transition transition : getTransitions()) {
		// builder.append("\n\t" + transition);
		// }
		// return builder.toString();

		builder.append(getName() + " " + isStart());

		for (Valuation valuation : getValuations()) {
			String valuationToString = valuation.toString();
			if (valuationToString != null && !valuationToString.equals("")) {
				builder.append("\n\t" + valuation);
			}
		}
		builder.append("\n");
		for (Transition transition : getTransitions()) {
			builder.append("\n\t" + transition.getName() + "(Task:" + transition.getTaskName() + ")" + "->" + transition.getEndState().getName());
		}
		return builder.toString();
	}

	public Transition getTransition(String name) {
		Transition result = null;
		for (Transition transition : transitions) {
			if (transition.getName().equals(name)) {
				result = transition;
				break;
			}
		}
		return result;
	}
	public Transition getTransitionByStateName(String endStateName) {
		Transition result = null;
		for (Transition transition : transitions) {
			if (transition.getEndState().getName().trim().equals(endStateName)) {
				result = transition;
				break;
			}
		}
		return result;
	}
	
	public static Transition getTransition(Kripke kripke, String stateFrom, String stateTo)
	{
		KripkeState startState = kripke.getState(stateFrom);
		return startState.getTransitionByStateName(stateTo);
	}
	/**
	 * returns a string that starts with the state name and _ and then appended to it the list of labels seperated by _
	 * @return
	 */
	public String toLabelsString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getName() + "_");
		List<BooleanPredicate> labels = getLabels();
		
		for (BooleanPredicate label : labels) {
			builder.append(label.toString()+ "_");
		}
		String toRet = builder.toString();
		return toRet.substring(0, toRet.lastIndexOf("_")).trim();
	}
	
	/**
	 * copy current state transition to tostate 
	 * @param toState Kripke state that will get current state transitions
	 */
	public void SwitchTransitionsTo(KripkeState toState)
	{
		for (Transition trans : transitions) {
				
				String endStateName = trans.getEndState().getName();	
				KripkeState endState = this.getKripke().getState(endStateName);
				Transition newTrans = new Transition(toState, endState, trans.getName(), trans.getName());
				if(!specialTransitionContain(toState.getTransitions(), newTrans))
					toState.addOutgoingTransition(newTrans);
			}
			
			for (Transition trans1 : reverseTransitions) {
				
					String startStateName = trans1.getEndState().getName();					
					KripkeState startState = this.getKripke().getState(startStateName);
					Transition newTrans = new Transition(startState, this, trans1.getName(), trans1.getName());
					if(!specialTransitionContain(startState.getTransitions(), newTrans))
					{
						startState.addOutgoingTransition(newTrans);
					}
				
			}
	}
public static boolean specialTransitionContain(Collection<Transition> stateTransitions, Transition transition)
{
	for (Transition trans : stateTransitions) {
		if(trans.getEndState().equals(transition.getEndState()) && trans.getStartState().equals(transition.getStartState()))
		{
			return true;
		}
	}
	return false;
}
public static boolean specialTransitionContain(Collection<Transition> stateTransitions, Transition transition, StringBuilder trnsName, StringBuilder trnsTaskName)
{
	for (Transition trans : stateTransitions) {
		if(trans.getEndState().getName().equals(transition.getEndState().getName()) && trans.getStartState().getName().equals(transition.getStartState().getName()))
		{
			trnsName.append(trans.getName());
			trnsTaskName.append(trans.getTaskName());
			return true;
		}
	}
	return false;
}
	public void setKripke(Kripke kripke) {
		this.kripke = kripke;
	}
	
	protected boolean specialStateContain(List<KripkeState> mergedStates, KripkeState searchState)
	{
		for (KripkeState state : mergedStates) {
			if(state.getName().equals(searchState.getName()))
				return true;				
		}
		return false;
	}
	
	/**
	 * Comma separeted
	 */
	public String getLabelsString(char delimiter)
	{
		StringBuilder sb = new StringBuilder();
		for (BooleanPredicate i : Labels) {
		    sb.append(delimiter).append(i);
		}
		return sb.toString().substring(1);
	}
}
