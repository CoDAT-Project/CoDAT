package eshmun.lts.ReducedKripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class KripkeReplacedState extends KripkeState {

	private List<KripkeState> _replacedStates;
	private Map<Transition, List<Transition>> _replacedTransitions;
	
	public KripkeReplacedState(Kripke newKripke, String stateName, boolean isStart, List<KripkeState> replacedStates) {
		super(newKripke, stateName, isStart);
		_replacedStates = replacedStates;
		for (KripkeState st : replacedStates) {
			if(st.isStart())
				this.setStart(true);
		}
		///// manage the new model transitions
		_replacedTransitions = new HashMap<Transition, List<Transition>>();
	}
	
	public void manageTransitions()
	{
		Kripke abstracModel = this.getKripke();
			for (KripkeState kripkeState : _replacedStates) {
				if(kripkeState.isStart())
					this.setStart(true);
				//transitions
				Collection<Transition> transitions = kripkeState.getTransitions();
				for (Transition trans : transitions) {
					
					
					KripkeState endState = getParentState(trans.getEndState());
					Transition newTrans = new Transition(this, endState, trans.getName(), trans.getTaskName());
					StringBuilder trnsName = new StringBuilder ();
					StringBuilder trnsTaskName = new StringBuilder ();
					if(this.getName().equals(endState.getName()) && !trans.getStartState().getName().equals(trans.getEndState().getName()))
						continue;
					if(!KripkeState.specialTransitionContain(this.getTransitions(), newTrans, trnsName, trnsTaskName))
					{
						this.addOutgoingTransition(newTrans);
					}
					else
					{
						newTrans = new Transition(this, endState, trnsName.toString(), trnsTaskName.toString());
					}
					AddNewReplacedTransition(newTrans, trans);
					
				}
				
			}
			boolean hasCycle = Kripke.formCycle(_replacedStates);
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
	
	public List<KripkeState> getReplacedStates() {
		return _replacedStates;
	}

	public Map<Transition, List<Transition>> getReplacedTransitions() {
		return _replacedTransitions;
	}

	public KripkeState getParentState(KripkeState subState)
	{
		Kripke kripke = this.getKripke();
		for (KripkeState state : kripke.getStatesList()) {
			if(state instanceof KripkeReplacedState)
			{
				if(((KripkeReplacedState)state).getReplacedStates().contains(subState))
					return state;
			}
		}
		return subState;
	}
	
	private void AddNewReplacedTransition(Transition key, Transition value)
	{
		if(_replacedTransitions.containsKey(key))
		{
			_replacedTransitions.get(key).add(value);
		}
		else
		{
			List<Transition> trs = new ArrayList<Transition>();
			trs.add(value);
			_replacedTransitions.put(key, trs);
		}
	}
}
