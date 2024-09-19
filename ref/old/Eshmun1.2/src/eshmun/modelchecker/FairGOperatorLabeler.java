package eshmun.modelchecker;

import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.EGOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Valuation;
import eshmun.modelchecker.FairStateProblemSolver.FairLableType;

public class FairGOperatorLabeler {

	public void label(Kripke kripke, EGOperator egOperator) throws CloneNotSupportedException {
		Kripke prunedKripke = getPrunedKripke(kripke, egOperator);
		FairStateProblemSolver fspSolver = new FairStateProblemSolver();
		Kripke eniExiLabeledKripke =  fspSolver.solveFSP(prunedKripke, FairLableType.EG, egOperator.getChild());
		for (KripkeState state: eniExiLabeledKripke.getStates()) {
			if (labeled(state, egOperator)) {
				KripkeState origState = kripke.getState(state.getName());
				PredicateFormulaValuation egVal = new PredicateFormulaValuation(kripke, origState, egOperator, true);
				origState.addValuation(egVal);
			}
		}	
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
	
	protected Kripke getPrunedKripke(Kripke kripke, EGOperator egOperator) throws CloneNotSupportedException {
		Kripke prunedKripke = kripke.clone();
		List<KripkeState> states = prunedKripke.getStatesList();
		
		for (KripkeState state: states.toArray(new KripkeState[states.size()])) {
			boolean labeledChild = false;
			Valuation valuation = state.getValuation(egOperator.getChild().toString());
			if (valuation instanceof PredicateFormulaValuation) {
				PredicateFormulaValuation val = (PredicateFormulaValuation) valuation;
				labeledChild = val.getValue();
			} 
			if (!labeledChild) {
				prunedKripke.removeState(state.getName());
			}
		}
		return prunedKripke;
	}
	
}
