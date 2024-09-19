package eshmun.lts.kripke.DFS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class KripkeExiEniLabeler {
	private Kripke generatedKripke;
	private List<String> participants;
	
	public KripkeExiEniLabeler(Kripke kripke) throws CloneNotSupportedException {
		this.generatedKripke = kripke.clone();
		this.participants = new ArrayList<String>();
	}
	
	public Kripke labebelEni() {
		if (generatedKripke.isLabeledEni()) {
			return generatedKripke;
		}
		Collection<KripkeState> states = generatedKripke.getStates();
		KripkeState [] statesArr = new KripkeState[states.size()];
		List<String> processedNames = new ArrayList<String>();
		for (KripkeState state : states.toArray(statesArr)) {
			processedNames.clear();
			
			Collection<Transition> transitionList = state.getTransitions();
			Transition [] transArr = new Transition[transitionList.size()]; 
			transArr = transitionList.toArray(transArr);
			
			for (Transition transition : transArr) {
				if (!processedNames.contains(transition.getTaskName())) {
					EniValuation eniValuation = new EniValuation(generatedKripke, state, transition.getTaskName(), true);
					state.addValuation(eniValuation);
				}
				if (!participants.contains(transition.getTaskName())) {
					participants.add(transition.getTaskName());
				}
			}
		}
		this.generatedKripke.setLabeledEni(true);
		//this.generatedKripke.setParticipatingProcesses(participants);
		return generatedKripke;
	}
	
	public Kripke labelExi() {
		if (generatedKripke.isLabeledExi()) {
			return generatedKripke;
		}
		
		Collection<KripkeState> states = generatedKripke.getStates();
		KripkeState [] statesArr = new KripkeState[states.size()];
		for (KripkeState state : states.toArray(statesArr)) {
			List<Transition> reverseTransitionList = state.getReverseTransitions();
			if (reverseTransitionList != null && reverseTransitionList.size() > 0) {
				Transition [] transArr = new Transition[reverseTransitionList.size()]; 
				transArr = reverseTransitionList.toArray(transArr);
				for (Transition revTransition : transArr) {
	
					String newStateName = getNewStateName(state, revTransition);
					KripkeState indexedState = generatedKripke.getState(newStateName);
					boolean indexedStateCreated = false;
					if (indexedState == null) {
						indexedState = getNewState(state, newStateName, revTransition);
						indexedStateCreated = true;
					}
									
					//if self loop do not add any transition
					if (!isSelfLoop(revTransition)) {
						generatedKripke.addTransition(revTransition.getEndState(), indexedState, revTransition.getName(), revTransition.getTaskName());
					}
					
					if (indexedStateCreated) {
						createNewStateTransition(state, revTransition, indexedState);
					}
				}
				generatedKripke.removeState(state.getName());
			}
		}
		generatedKripke.setLabeledExi(true);
		//generatedKripke.setParticipatingProcesses(participants);
		return generatedKripke;
	}
	
	public void createNewStateTransition(KripkeState state, Transition revTransition, KripkeState newState) {
		for (Transition transition : state.getTransitions()) {
			//if self loop with same index add another selfloop on new state
			if (!isSelfLoop(transition)) {
				generatedKripke.addTransition(newState, transition.getEndState(), transition.getName(), transition.getTaskName());
			} else {
				if (revTransition.getTaskName().equals(transition.getTaskName())) {
					generatedKripke.addTransition(newState, newState, transition.getName(), transition.getTaskName());
				} else {
					String newOutStateName = getNewStateName(state, transition);
					KripkeState newOutState = generatedKripke.getState(newOutStateName);
					boolean outStateCreated = false;
					if (newOutState == null) {
						newOutState = getNewState(state, newOutStateName, transition);
						outStateCreated = true;
					}
					if (outStateCreated) {
						createNewStateTransition(state, transition, newOutState);
					}
					generatedKripke.addTransition(newState, newOutState, transition.getName(), transition.getTaskName());
				}
			}
		}
	}
	
	protected KripkeState getNewState(KripkeState state, String newStateName, Transition revTransition) {
		KripkeState indexedState = null;
		try {
			indexedState = state.clone();
			indexedState.setName(newStateName);
			indexedState.addValuation(new ExiValuation(generatedKripke, indexedState, revTransition.getTaskName(), true));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		generatedKripke.addState(indexedState);
		
		if (!participants.contains(revTransition.getTaskName())) {
			participants.add(revTransition.getTaskName());
		}
		
		return indexedState;

	}
	
	protected String getNewStateName(KripkeState state, Transition transition) {
		String newStateName = state.getName() + "-" + transition.getTaskName();
		return newStateName;
	}
	
	protected boolean isSelfLoop(Transition transition) {
		boolean selfLoop = transition.getStartState().equals(transition.getEndState());
		return selfLoop;
	}

	public Kripke getGeneratedKripke() {
		return generatedKripke;
	}
}
