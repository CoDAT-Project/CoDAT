package eshmun.modelchecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.AWOperator;
import eshmun.expression.ctl.AXOperator;
import eshmun.expression.ctl.EGOperator;
import eshmun.expression.ctl.EUOperator;
import eshmun.expression.ctl.EWOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.modelchecker.FairStateProblemSolver.FairLableType;


public class WeekFairnessModelChecker extends ModelChecker{

	public WeekFairnessModelChecker() {
		super();
	}
	
	public void prepareKripke() {
		
	}
	
	protected void processSpec(Kripke kripke, PredicateFormula spec) throws Exception {
		List<PredicateFormula> subFormulae = spec.getSubFormulea(this);
		for (PredicateFormula subFormula: subFormulae) {
			if (subFormula instanceof EUOperator) {
				EUOperator euOperator = (EUOperator) subFormula;
				UOperatorLabeler labeler = getULabeler();
				labeler.label(kripke, euOperator);
			} else if (subFormula instanceof AUOperator) {
				AUOperator auOperator = (AUOperator) subFormula;
				UOperatorLabeler labeler = getULabeler();
				labeler.label(kripke, auOperator);
			} else if (subFormula instanceof AWOperator) {
				AWOperator operator = (AWOperator) subFormula;
				UOperatorLabeler labeler = getULabeler();
				AUOperator auOperator = new AUOperator(operator.getLeftChild(), operator.getRightChild());
				labeler.label(kripke, auOperator, true); 
			} else if (subFormula instanceof EWOperator) {
				EWOperator operator = (EWOperator) subFormula;
				UOperatorLabeler labeler = getULabeler();
				EUOperator auOperator = new EUOperator(operator.getLeftChild(), operator.getRightChild());
				labeler.label(kripke, auOperator, true); 
			} else if (subFormula instanceof EGOperator) {
				EGOperator egOperator = (EGOperator) subFormula;
				FairGOperatorLabeler gOpLabeler = new FairGOperatorLabeler();
				gOpLabeler.label(kripke, egOperator);
			} 
			
			List<KripkeState> kripkeStates = kripke.getStatesList();
			for (KripkeState state : kripkeStates) {
				subFormula.modelCheck(this, kripke, state);
			}
		}
	}

	@Override
	public boolean modelCheck(Kripke kripke, Collection<PredicateFormula> specifications) {
		FairStateProblemSolver fspSolver = new FairStateProblemSolver();
		try {
			BooleanPredicate booleanPredicate = new BooleanPredicate(new BooleanConstant(true));
			long start = System.nanoTime();
			Kripke solvedKrike = fspSolver.solveFSP(kripke, FairLableType.EX, booleanPredicate);
			long end  = System.nanoTime();
			System.out.println("Time to solve FSP " + (end-start )); 
			return super.modelCheck(solvedKrike, specifications);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean visit(AXOperator operator, Kripke kripke, KripkeState state) {
		NotOperator child = new NotOperator(operator.getChild());
		EXOperator exOperator = new EXOperator(child);
		exOperator.setPathIndexes(operator.getPathIndexes());
		NotOperator rootOperator = new NotOperator(exOperator);
		boolean value = labeled(state,rootOperator);
		
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(EXOperator operator, Kripke kripke, KripkeState state) {
		if (state != null) {
			List<String> pathIndexes = operator.getPathIndexes();
			if (pathIndexes == null) {
				pathIndexes = new ArrayList<String>();
			}
			Collection<Transition> transitions =  state.getTransitions();
			for (Transition transition : transitions) {
				String transitionTask = transition.getTaskName();
				if (pathIndexes.contains(transitionTask)) {
					KripkeState nextState = transition.getEndState();
					BooleanPredicate booleanPredicate = new BooleanPredicate(new BooleanConstant(true));
					EXOperator exOperator = new EXOperator(booleanPredicate);
					PredicateFormulaValuation valuationEX = (PredicateFormulaValuation) nextState.getValuation(exOperator.toString());
					PredicateFormulaValuation valuationChild = (PredicateFormulaValuation)nextState.getValuation(operator.getChild().toString());
					if (valuationChild != null && valuationChild.getValue() && valuationEX != null && valuationEX.getValue()) {
						PredicateFormulaValuation valuationAdded = new PredicateFormulaValuation(kripke, state, operator, true);
						state.addValuation(valuationAdded);
						return true;
					}
				}
			}
		}
		PredicateFormulaValuation valuationAdded = new PredicateFormulaValuation(kripke, state, operator, false);
		state.addValuation(valuationAdded);
		return false;
	}
	
	@Override
	public Boolean visit(AUOperator operator, Kripke kripke, KripkeState state) {
		NotOperator notLeftOp = new NotOperator(operator.getLeftChild());
		NotOperator notRightOp = new NotOperator(operator.getRightChild());
		AndOperator notLeftRightOp = new AndOperator(notLeftOp, notRightOp);
		EUOperator uOperator = new EUOperator(notRightOp, notLeftRightOp);
		EGOperator egNotRight = new EGOperator(notRightOp);
		OrOperator uOrGOp = new OrOperator(uOperator, egNotRight);
		NotOperator notUOrG = new NotOperator(uOrGOp);
		
		boolean value = labeled(state, notUOrG);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(AWOperator operator, Kripke kripke, KripkeState state) {
		NotOperator notLeftOp = new NotOperator(operator.getLeftChild());
		NotOperator notRightOp = new NotOperator(operator.getRightChild());
		AndOperator notLeftRightOp = new AndOperator(notLeftOp, notRightOp);
		EUOperator uOperator = new EUOperator(notRightOp, notLeftRightOp);
		
		boolean value = labeled(state, uOperator);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public List<PredicateFormula> fillSubFormulae(AUOperator operator) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		
		subformulea.addAll(operator.getLeftChild().getSubFormulea(this));
		subformulea.addAll(operator.getRightChild().getSubFormulea(this));
		
		NotOperator notLeftOp = new NotOperator(operator.getLeftChild());
		subformulea.add(notLeftOp);
		NotOperator notRightOp = new NotOperator(operator.getRightChild());
		subformulea.add(notRightOp);
		AndOperator notLeftRightOp = new AndOperator(notLeftOp, notRightOp);
		subformulea.add(notLeftRightOp);
		EUOperator uOperator = new EUOperator(notRightOp, notLeftRightOp);
		subformulea.add(uOperator);
		EGOperator egNotRight = new EGOperator(notRightOp);
		subformulea.add(egNotRight);
		OrOperator uOrGOp = new OrOperator(uOperator, egNotRight);
		subformulea.add(uOrGOp);
		NotOperator notUOrG = new NotOperator(uOrGOp);
		subformulea.add(notUOrG);
		subformulea.add(operator);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> fillSubFormulae(AWOperator operator) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		
		subformulea.addAll(operator.getLeftChild().getSubFormulea(this));
		subformulea.addAll(operator.getRightChild().getSubFormulea(this));
		
		NotOperator notLeftOp = new NotOperator(operator.getLeftChild());
		subformulea.add(notLeftOp);
		NotOperator notRightOp = new NotOperator(operator.getRightChild());
		subformulea.add(notRightOp);
		AndOperator notLeftRightOp = new AndOperator(notLeftOp, notRightOp);
		subformulea.add(notLeftRightOp);
		EWOperator uOperator = new EWOperator(notRightOp, notLeftRightOp);
		subformulea.add(uOperator);
		subformulea.add(operator);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> fillSubFormulae(AXOperator operator) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		NotOperator child = new NotOperator(operator.getChild());
		EXOperator exOperator = new EXOperator(child);
		exOperator.setPathIndexes(operator.getPathIndexes());
		NotOperator rootOperator = new NotOperator(exOperator);
		subformulea.addAll(rootOperator.getSubFormulea(this));
		subformulea.add(operator);
		return subformulea;
	}

	@Override
	protected FairUOperatorLabeler getULabeler() {
		return new FairUOperatorLabeler();
	}

	@Override
	public Boolean visit(EGOperator operator, Kripke kripke, KripkeState state) {
		// TODO remove !q states and apply FSP and then label (create FairEGLabeler)
		return false;
	}
}