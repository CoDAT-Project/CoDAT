package eshmun.modelchecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.ctl.EGOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.lts.kripke.Valuation;
import eshmun.lts.kripke.DFS.EniValuation;
import eshmun.lts.kripke.DFS.ExiValuation;
import eshmun.lts.kripke.DFS.KripkeExiEniLabeler;
import eshmun.lts.kripke.DFS.StrongComponent;
import eshmun.lts.kripke.DFS.StrongComponentsGenerator;

public class FairStateProblemSolver {
	
	private FairLableType fairLabelType;
	private PredicateFormula labelChild;

	protected enum FairLableType {
		EX, EG
	}

	public Kripke solveFSP(Kripke kripke, FairLableType fairLabelType, PredicateFormula labelChild) throws CloneNotSupportedException {
		Date date = new Date();
		long startTime = date.getTime();
		this.fairLabelType = fairLabelType;
		this.labelChild = labelChild;
		Kripke exiLabledKripke = null;
		try {
			List<KripkeState> fairStates = new ArrayList<KripkeState>();
			KripkeExiEniLabeler exiLabeler = new KripkeExiEniLabeler(kripke);
			exiLabeler.labelExi();
			exiLabledKripke = exiLabeler.labebelEni();
			
			StrongComponentsGenerator strongCompGen = new StrongComponentsGenerator(exiLabledKripke);
			strongCompGen.computeStrongComponents();
			Iterator<StrongComponent> strongCompIter = strongCompGen.getStrongComponents();
			while (strongCompIter.hasNext()) {
				StrongComponent strongComp = strongCompIter.next();
				if (strongComp.size() >1) {
					if(isComponentFair(exiLabledKripke, strongComp)) {
						labelFairComponent(strongComp, fairStates);
					}
				} 
			}	
			
			labelCloseFairStates(fairStates);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		date = new Date();
		long endTime = date.getTime();
		System.out.println("time to solve fsp for type: " + fairLabelType + "is: "  + (endTime - startTime));
		return exiLabledKripke;
	}
	
	protected boolean isComponentFair(Kripke kripke , StrongComponent strongComp) {
		boolean fairComponent = true;
		for (String participant: kripke.getParticipantsFairnessType().keySet()) {		//check each process alone
			FairnessType fairnessType = kripke.getParticipantFairnessType(participant);
			boolean fairComponentToProcess = false;
			if (fairnessType.equals(FairnessType.STRONG_FAIRNESS)) {
				fairComponentToProcess = checkStrongFairness(strongComp, participant);
			} else if (fairnessType.equals(FairnessType.WEAK_FAIRNESS)) {
				fairComponentToProcess = checkWeakFairness(strongComp, participant);
			}
			if (!fairComponentToProcess) {
				fairComponent = false;
				break;
			}
		}
		return fairComponent;
	}
	
	protected boolean checkWeakFairness(StrongComponent strongComp, String participant) {
		boolean fairComponentToPariticpant = false;
		for (KripkeState kripkeState : strongComp.getStatesList()) {
			EniValuation enVal = new EniValuation(kripkeState.getKripke(), kripkeState, participant, false);
			boolean eni = evaluate(enVal, kripkeState);
			ExiValuation exVal = new ExiValuation(kripkeState.getKripke(), kripkeState, participant, false);
			boolean exi = evaluate(exVal, kripkeState);
			if (!eni | exi) {
				fairComponentToPariticpant = true;
				break;
			}
		}
		return fairComponentToPariticpant;
	}
	
	public boolean checkStrongFairness(StrongComponent strongComp, String participant) {
		boolean eni = false;
		boolean exi = false;
		for (KripkeState kripkeState : strongComp.getStatesList()) {
			if (!eni && isStateEnabled(kripkeState, participant)) {
				eni = true;
			}
			if (!exi && isStateExecuted(kripkeState, participant)) {
				exi = true;
			}
		}
		
		return !eni | exi;
	}
	
	protected boolean isStateEnabled(KripkeState state, String participantProcess) {
		EniValuation enVal = new EniValuation(state.getKripke(), state, participantProcess, false);
		boolean eni = evaluate(enVal, state);
		return eni;
	}
	
	protected boolean isStateExecuted(KripkeState state, String participantProcess) {
		ExiValuation exVal = new ExiValuation(state.getKripke(), state, participantProcess, false);
		boolean exi = evaluate(exVal, state);
		return exi;
	}
	
	
	
	protected void labelCloseFairStates(List<KripkeState> fairComponentsStates) {
		List<KripkeState> pendingStates = new ArrayList<KripkeState>();
		pendingStates.addAll(fairComponentsStates);
		
		List<KripkeState> closeStates = new ArrayList<KripkeState>();
		while (!pendingStates.isEmpty()) {
			KripkeState state = pendingStates.remove(0);
			for (Transition revTransition : state.getReverseTransitions()) {
				KripkeState neighbor = revTransition.getEndState();
				if (!fairComponentsStates.contains(neighbor) && !closeStates.contains(neighbor)) {
					pendingStates.add(neighbor);
					closeStates.add(neighbor);
				}
			}
		}
		if (closeStates.size() > 0) {
			for (KripkeState state : closeStates) {
				labelFairState(state);
			}
		}
	}
	
	protected void labelFairComponent(StrongComponent strongComp,List<KripkeState> fairStates) {
		Iterator<KripkeState> iter = strongComp.getStates();
		
		while (iter.hasNext()) {
			KripkeState state = iter.next();
			labelFairState(state);
			fairStates.add(state);
		}
		
	}
	
	protected void labelFairState(KripkeState state) {
		if (fairLabelType == FairLableType.EX) {
			EXOperator exOperator = new EXOperator(labelChild);
			PredicateFormulaValuation valuation = new PredicateFormulaValuation(state.getKripke(), state, exOperator, true);
			state.addValuation(valuation);	
		} else if (fairLabelType == FairLableType.EG) {
			EGOperator egOperator = new EGOperator(labelChild);
			PredicateFormulaValuation valuation = new PredicateFormulaValuation(state.getKripke(), state, egOperator, true);
			state.addValuation(valuation);
		}
	}

	
	
	protected boolean evaluate(Valuation val, KripkeState state) {
		Valuation stateVal = state.getValuation(val.getName());
		boolean value = false;
		if (stateVal != null) {
			Object valueObj = stateVal.getValue();
			if (valueObj instanceof Boolean) {
				value = ((Boolean) valueObj).booleanValue(); 
			}
		}
		return value;
	}
}
