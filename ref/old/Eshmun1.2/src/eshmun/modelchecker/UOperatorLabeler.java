package eshmun.modelchecker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.EUOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.lts.kripke.Valuation;

public class UOperatorLabeler {
	
	Map<KripkeState, Boolean> markedStates; 
	
	public UOperatorLabeler() {
		this.markedStates = new HashMap<KripkeState, Boolean>();
	}

	public void label(Kripke kripke, AUOperator auOperator) {
		label(kripke, auOperator, false);
	}
	
	public void label(Kripke kripke, AUOperator auOperator, boolean isWeek) {
		markedStates.clear();
		for (KripkeState state: kripke.getStates()) {
			au(auOperator, state, isWeek);
		}
	}
	
	public void label(Kripke kripke, EUOperator euOperator) {
		label(kripke, euOperator, false);
	}
	
	public void label(Kripke kripke, EUOperator euOperator, boolean isWeek) {
		markedStates.clear();
		for (KripkeState state: kripke.getStates()) {
			eu(euOperator, state, isWeek);
		}
	}
	
	protected boolean handledMarkedState(KripkeState state, PredicateFormula pf, boolean isWeek) {
		if (hasValuation(state, pf)) {
			if (labeled(state, pf)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (isWeek) {
				addLabel(state, pf, true);
				return true;
			} else {
				//addLabel(state, pf, false); this should be labeled false at end of recursive calls
				return false;
			}
		}
	}
	
	protected boolean eu(EUOperator euOperator, KripkeState state, boolean isWeek) {
		if (marked(state)) {
			return handledMarkedState(state, euOperator, isWeek);
		}
		
		mark(state);
		if (labeled(state, euOperator.getRightChild())) {
			addLabel(state, euOperator, true);
			return true;
		} else if (!labeled(state, euOperator.getLeftChild())) {
			addLabel(state, euOperator, false);
			return false;
		}
		
		Collection<Transition> transitions =  state.getTransitions();
		boolean onePathValid =false;
		for (Transition transition : transitions) {
			KripkeState nextsState = transition.getEndState();
			boolean resultNextState = eu(euOperator, nextsState, isWeek);
			if (resultNextState) {
				onePathValid = true;
				break;
			}
		}
		if (onePathValid) {
			addLabel(state, euOperator, true);
			return true;
		} else {
			addLabel(state, euOperator, false);
			return false;
		}
	}
	
	protected boolean au(AUOperator auOperator, KripkeState state, boolean isWeek) {
		if (marked(state)) {
			return handledMarkedState(state, auOperator, isWeek);
		}
		mark(state);
		if (labeled(state, auOperator.getRightChild())) {
			addLabel(state, auOperator, true);
			return true;
		} else if (!labeled(state, auOperator.getLeftChild())) {
			addLabel(state, auOperator, false);
			return false;
		}
		
		Collection<Transition> transitions =  state.getTransitions();
		if (transitions.size() == 0 && !isWeek) {
			addLabel(state, auOperator, false);
			return false;
		}
		
		for (Transition transition : transitions) {
			KripkeState nextsState = transition.getEndState();
			boolean resultNextState = au(auOperator, nextsState, isWeek);
			if (!resultNextState) {
				addLabel(state, auOperator, false);
				return false;
			}
		}
		addLabel(state, auOperator, true);
		return true; 
	}
	
	
	protected void addLabel(KripkeState state, PredicateFormula formula, boolean value) {
		PredicateFormulaValuation val  = new PredicateFormulaValuation(state.getKripke(), state, formula, value);
		state.addValuation(val);
	}
	
	protected boolean hasValuation(KripkeState state, PredicateFormula formula) {
		Valuation val = state.getValuation(formula.toString());
		return val != null;
	}
	protected boolean labeled(KripkeState state, PredicateFormula formula) {
		if (formula instanceof BooleanPredicate) {
			BooleanPredicate booleanOperator = (BooleanPredicate) formula;
			if (booleanOperator.getVariable() instanceof BooleanConstant) {
				BooleanConstant booleanConst = ((BooleanConstant)  booleanOperator.getVariable());
				return booleanConst.evaluate(state.getKripke(), state);
			}
		}
		
		Valuation val = state.getValuation(formula.toString());
		if (val != null) {
			return (Boolean)val.getValue();
		} else {
			return false;
		}
	}
	
	protected void mark(KripkeState state) {
		markedStates.put(state, true);
	}
	
	protected boolean marked(KripkeState state) {
		Boolean markedObj = markedStates.get(state);
		if (markedObj != null) {
			return markedObj.booleanValue();
		} else {
			return false;
		}
	}
}
