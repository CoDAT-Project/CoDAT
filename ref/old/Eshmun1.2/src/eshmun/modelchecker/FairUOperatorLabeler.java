package eshmun.modelchecker;

import java.util.ArrayList;
import java.util.List;

import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.EUOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class FairUOperatorLabeler extends UOperatorLabeler{

	public FairUOperatorLabeler() {
		super();
	}

	public void label(Kripke kripke, AUOperator auOperator) {
		label(kripke, auOperator, false);
	}
	
	public void label(Kripke kripke, AUOperator auOperator, boolean isWeek) {
		//DO nothing since the processing is done on subformulae and especially on EG
	}
	
	public void label(Kripke kripke, EUOperator euOperator) {
		label(kripke, euOperator, false);
	}
	
	public void label(Kripke kripke, EUOperator euOperator, boolean isWeek) {
		List<KripkeState> pendingStates = new ArrayList<KripkeState>();
		for (KripkeState state : kripke.getStates()) {
			EXOperator exOperator = new EXOperator(new BooleanPredicate(new BooleanConstant(true)));
			boolean valid = labeled(state, exOperator); 
			boolean validEnd = (labeled(state, euOperator.getRightChild()) || (isWeek && labeled(state, euOperator.getLeftChild())));
			valid = valid && validEnd;
			if (valid) {
				addLabel(state, euOperator, true);
				pendingStates.add(state);
			}
		}
		
		while (pendingStates.size() > 0) {
			KripkeState state = pendingStates.remove(0);
			
			for (Transition transition : state.getReverseTransitions()) {
				KripkeState previousState = transition.getEndState();
				EXOperator exOperator = new EXOperator(new BooleanPredicate(new BooleanConstant(true)));
				if (labeled(previousState, exOperator) && labeled(previousState, euOperator.getLeftChild())
					&& !labeled(previousState, euOperator.getRightChild())
					&& !labeled(previousState, euOperator)) {
					addLabel(previousState, euOperator, true);
					pendingStates.add(previousState);
				}
			}
		}
		
		for (KripkeState state : kripke.getStates()) {
			if (!labeled(state, euOperator)) {
				addLabel(state, euOperator, false);	
			}
		}
	}
	
}