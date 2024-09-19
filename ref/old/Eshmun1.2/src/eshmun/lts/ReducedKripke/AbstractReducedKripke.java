package eshmun.lts.ReducedKripke;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public abstract class AbstractReducedKripke extends Kripke {

	public AbstractReducedKripke() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract Kripke Reduce();
	
	public static List<Transition> GetInitialModelTransitions(Kripke reducedKripke, List<Transition> reducedModelTransitions)
	{
		List<Transition> originalModelTransitions = new ArrayList<Transition>();
		List<KripkeState> statesList = (List<KripkeState>) reducedKripke.getStates();
		for (KripkeState state : statesList) {
			if(state instanceof KripkeReplacedState)
			{
				Map<Transition, List<Transition>> replacedTransitions = ((KripkeReplacedState)state).getReplacedTransitions();
				for (Transition trns : replacedTransitions.keySet()) {
					if(reducedModelTransitions.contains(trns))
					{
						originalModelTransitions.addAll(replacedTransitions.get(trns));
					}
				}
			}
		}
		return originalModelTransitions;
	}

}
