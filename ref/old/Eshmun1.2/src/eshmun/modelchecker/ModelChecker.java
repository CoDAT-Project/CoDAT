package eshmun.modelchecker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.PredicateFormulaValuation;
/*import eshmun.expression.atomic.EqualOperator;
import eshmun.expression.atomic.GreaterThanEqualOperator;
import eshmun.expression.atomic.GreaterThanOperator;
import eshmun.expression.atomic.LessThanEqualOperator;
import eshmun.expression.atomic.LessThanOperator;
import eshmun.expression.atomic.NotEqualOperator;*/
import eshmun.expression.atomic.bool.BooleanConstant;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.expression.ctl.AFOperator;
import eshmun.expression.ctl.AGOperator;
import eshmun.expression.ctl.AUOperator;
import eshmun.expression.ctl.AVOperator;
import eshmun.expression.ctl.AWOperator;
import eshmun.expression.ctl.AXOperator;
import eshmun.expression.ctl.EFOperator;
import eshmun.expression.ctl.EGOperator;
import eshmun.expression.ctl.EUOperator;
import eshmun.expression.ctl.EVOperator;
import eshmun.expression.ctl.EWOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.EquivalentOperator;
import eshmun.expression.propoperator.ImpliesOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;
import eshmun.lts.kripke.Valuation;

/**
 * this implementation of the model checker is based on E.M. Clarke implemntation
 * for each specfication spec do {
 * 		List<ExpressionNode> subformulea = spec.subformulea
 * 		sort(subformulea) by increasing depth (length)
 * 		for (each subformulea sub) {
 * 			traverse the kripke structure starting from its initial state
 * 			Visitor v = sub.getVisitor();
 * 			and for each start state s0 do {
 * 				v.visit(s0) which labels s0 or any subsequent state with sub.toStirng() 
 * 				after checking if s0 satisfies sub. 
 * 			}
 * 		}  
 * }
 * 
 * loop over all the states of kripke and return true if all states are labeled
 * with spec formula
 * @author Emile
 *
 */
public class ModelChecker extends AbstractModelChecker{
	@Override
	public boolean modelCheck(Kripke kripke, Collection<PredicateFormula> specifications) {
		boolean allSpecsValid = true;
		List<KripkeState> kripkeStartStates = kripke.getStartStatesList();
		for (PredicateFormula spec: specifications) {
			Long start = System.currentTimeMillis();
			try {
				processSpec(kripke, spec);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			boolean allValid = true;
			for (KripkeState startState : kripkeStartStates) {
				if (!((Boolean)startState.getValuation(spec.toString()).getValue())) {
					allValid = false;
					allSpecsValid = false;
					break;
				}                                                                   
			}
			
			Long end = System.currentTimeMillis();
			//System.out.println("Model checking" + (allValid ? " success " : " fail ") + " for: " + spec + " is: " + allValid+".\n Time taken: "+ ((end-start))+ " nano");
			allValid = true;
		}
		return allSpecsValid;
	}
	
	protected void processSpec(Kripke kripke, PredicateFormula spec) throws Exception{
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
			} 
			List<KripkeState> kripkeStates = kripke.getStatesList();
			for (KripkeState state : kripkeStates) {
				subFormula.modelCheck(this, kripke, state);
			}
		}
	}
	
	protected boolean labeled(KripkeState state, PredicateFormula operator) {
		boolean retVal = false;
		Valuation valuation = state.getValuation(operator.toString());
		if (valuation != null) {
			retVal = (Boolean)valuation.getValue();
		}
		return retVal;
	}

	@Override
	public Boolean visit(AndOperator operator, Kripke kripke, KripkeState state) {
		boolean leftChildLabeled = labeled(state, operator.getLeftChild());
		boolean rightChildLabeled = labeled(state, operator.getRightChild());
		boolean value = leftChildLabeled && rightChildLabeled;
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(OrOperator operator, Kripke kripke, KripkeState state) {
		boolean leftChildLabeled = labeled(state, operator.getLeftChild());
		boolean rightChildLabeled = labeled(state, operator.getRightChild());
		boolean value = leftChildLabeled || rightChildLabeled;
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(BooleanPredicate operator, Kripke kripke, KripkeState state) {
		BooleanVariable variable =  operator.getVariable();
		boolean value = variable.evaluate(kripke, state);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(NotOperator operator, Kripke kripke, KripkeState state) {
		boolean isChildLabeled = labeled(state,operator.getChild());
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, !isChildLabeled);
		state.addValuation(valuation);
		return !isChildLabeled;
	}
	
	/*@Override
	public Boolean visit(EqualOperator operator, Kripke kripke, KripkeState state) {
		VariableComparator varComp = new VariableComparator(kripke, state);
		int compResult = varComp.compare(operator.getLeftChild(), operator.getRightChild());
		boolean value = (compResult == 0);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(NotEqualOperator operator, Kripke kripke, KripkeState state) {
		VariableComparator varComp = new VariableComparator(kripke, state);
		int compResult = varComp.compare(operator.getLeftChild(), operator.getRightChild());
		boolean value = (compResult != 0);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(LessThanOperator operator, Kripke kripke, KripkeState state) {
		VariableComparator varComp = new VariableComparator(kripke, state);
		int compResult = varComp.compare(operator.getLeftChild(), operator.getRightChild());
		boolean value = (compResult < 0);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(LessThanEqualOperator operator, Kripke kripke, KripkeState state) {
		VariableComparator varComp = new VariableComparator(kripke, state);
		int compResult = varComp.compare(operator.getLeftChild(), operator.getRightChild());
		boolean value = (compResult <= 0);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(GreaterThanOperator operator, Kripke kripke, KripkeState state) {
		VariableComparator varComp = new VariableComparator(kripke, state);
		int compResult = varComp.compare(operator.getLeftChild(), operator.getRightChild());
		boolean value = (compResult > 0);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(GreaterThanEqualOperator operator, Kripke kripke, KripkeState state) {
		VariableComparator varComp = new VariableComparator(kripke, state);
		int compResult = varComp.compare(operator.getLeftChild(), operator.getRightChild());
		boolean value = (compResult >= 0);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
*/
	@Override
	public Boolean visit(AUOperator operator, Kripke kripke, KripkeState state) {
		return labeled(state, operator);  //already labeled by the labeler
	}
	
	@Override
	public Boolean visit(EUOperator operator, Kripke kripke, KripkeState state) {
		return labeled(state, operator);  //already labeled by the labeler
	}

	@Override
	public Boolean visit(AWOperator operator, Kripke kripke, KripkeState state) {
		AUOperator auOperator = new AUOperator(operator.getLeftChild(), operator.getRightChild());
		boolean result = labeled(state, auOperator); 
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, result);
		state.addValuation(valuation);
		return result;
	}
	@Override
	public Boolean visit(EWOperator operator, Kripke kripke, KripkeState state) {
		EUOperator euOperator = new EUOperator(operator.getLeftChild(), operator.getRightChild());
		boolean result = labeled(state, euOperator);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, result);
		state.addValuation(valuation);
		return result;
	}
	
	protected UOperatorLabeler getULabeler() {
		return new UOperatorLabeler();
	}
	
	@Override
	public Boolean visit(AVOperator operator, Kripke kripke, KripkeState state) throws Exception {
		// A[pVq] == !E[!pU!q]
		NotOperator leftChild = new NotOperator(operator.getLeftChild());
		NotOperator rightChild = new NotOperator(operator.getRightChild());
		EUOperator euOperator = new EUOperator(leftChild, rightChild);
		NotOperator rooOperator = new NotOperator(euOperator);
		processSpec(kripke, rooOperator);
		boolean value = visit(rooOperator, kripke, state);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}
	
	@Override
	public Boolean visit(EVOperator operator, Kripke kripke, KripkeState state) throws Exception {
		// E[pVq] == !A[!pU!q]
		NotOperator leftChild = new NotOperator(operator.getLeftChild());
		NotOperator rightChild = new NotOperator(operator.getRightChild());
		AUOperator euOperator = new AUOperator(leftChild, rightChild);
		NotOperator rooOperator = new NotOperator(euOperator);
		processSpec(kripke, rooOperator);//TODO: check this is not longer necessary and currently doing nothing
		boolean value = visit(rooOperator, kripke, state);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(AXOperator operator, Kripke kripke, KripkeState state) {
		if (state != null) {
			List<String> pathIndexes = operator.getPathIndexes();
			if (pathIndexes == null) {
				pathIndexes = new ArrayList<String>();
			}
			boolean allNextStatesSatisfy = true;
			Collection<Transition> transitions =  state.getTransitions();
			for (Transition transition : transitions) {
				String transitionTask = transition.getTaskName();
				//msakr 16-10-2014 if (pathIndexes.contains(transitionTask)) {
					KripkeState nextState = transition.getEndState();
					PredicateFormulaValuation valuation = (PredicateFormulaValuation)nextState.getValuation(operator.getChild().toString());
					if (!valuation.getValue()) {
						allNextStatesSatisfy = false;
						break;
					}
					//msakr 16-10-2014}
			}
			PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, allNextStatesSatisfy);
			state.addValuation(valuation);
			return allNextStatesSatisfy;
		}
		return false;
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
				//msakr 16-10-2014if (pathIndexes.contains(transitionTask)) {
					KripkeState nextState = transition.getEndState();
					PredicateFormulaValuation valuation = (PredicateFormulaValuation)nextState.getValuation(operator.getChild().toString());
					if (valuation != null && valuation.getValue()) {
						PredicateFormulaValuation valuationAdded = new PredicateFormulaValuation(kripke, state, operator, true);
						state.addValuation(valuationAdded);
						return true;
					}
					//msakr 16-10-2014}
			}
		}
		PredicateFormulaValuation valuationAdded = new PredicateFormulaValuation(kripke, state, operator, false);
		state.addValuation(valuationAdded);
		return false;
	}

	@Override
	public Boolean visit(AGOperator operator, Kripke kripke, KripkeState state) {
		// AG(q) == !E(F(!q))
		NotOperator child = new NotOperator(operator.getChild());
		EFOperator afOperator = new EFOperator(child);
		NotOperator rootOperator = new NotOperator(afOperator);
		boolean value = labeled(state,rootOperator);
		
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(EGOperator operator, Kripke kripke, KripkeState state) {
		// EG(q) == !A(F(!q))
		NotOperator child = new NotOperator(operator.getChild());
		AFOperator afOperator = new AFOperator(child);
		NotOperator rootOperator = new NotOperator(afOperator);
		boolean value = labeled(state,rootOperator);
		
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(AFOperator operator, Kripke kripke, KripkeState state) {
		// AF(q) <==> A[true U q]
		BooleanPredicate leftChild = new BooleanPredicate(new BooleanConstant(true));
		AUOperator auOperator = new AUOperator(leftChild, operator.getChild());
		boolean value = labeled(state,auOperator);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
		
	}

	@Override
	public Boolean visit(EFOperator operator, Kripke kripke, KripkeState state) {
		// EF(q) <==> E[true U q]
		BooleanPredicate leftChild = new BooleanPredicate(new BooleanConstant(true));
		EUOperator euOperator = new EUOperator(leftChild, operator.getChild());
		boolean value = labeled(state, euOperator);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(EquivalentOperator operator, Kripke kripke, KripkeState state) {
		//A <=> B is (A=>B) & (B=>A)
		ImpliesOperator newLeftChild = new ImpliesOperator(operator.getLeftChild(), operator.getRightChild());
		ImpliesOperator newRightChild = new ImpliesOperator(operator.getRightChild(), operator.getLeftChild());
		AndOperator rootOperator = new AndOperator(newLeftChild, newRightChild);
		boolean value = labeled(state, rootOperator);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public Boolean visit(ImpliesOperator operator, Kripke kripke, KripkeState state) {
		//A=>B <==> !A | B
		NotOperator notChild = new NotOperator(operator.getLeftChild());
		OrOperator rootOperator = new OrOperator(notChild, operator.getRightChild());
		boolean value = labeled(state, rootOperator);
		PredicateFormulaValuation valuation = new PredicateFormulaValuation(kripke, state, operator, value);
		state.addValuation(valuation);
		return value;
	}

	@Override
	public List<PredicateFormula> fillSubFormulae(AWOperator operator) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(operator.getLeftChild().getSubFormulea(this));
		subformulea.addAll(operator.getRightChild().getSubFormulea(this));
		subformulea.add(operator);
		return subformulea;
	}
	
	@Override
	public List<PredicateFormula> fillSubFormulae(AUOperator operator) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(operator.getLeftChild().getSubFormulea(this));
		subformulea.addAll(operator.getRightChild().getSubFormulea(this));
		subformulea.add(operator);
		return subformulea;
	}

	@Override
	public List<PredicateFormula> fillSubFormulae(AXOperator operator) {
		List<PredicateFormula> subformulea = new ArrayList<PredicateFormula>();
		subformulea.addAll(operator.getChild().getSubFormulea(this));
		subformulea.add(operator);
		return subformulea;
	}
}