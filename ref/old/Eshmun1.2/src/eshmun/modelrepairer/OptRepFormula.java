package eshmun.modelrepairer;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.*;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class OptRepFormula 
{
	
	private List<KripkeState> statesList;
	private List<KripkeState> initialStatesList;
	private List<PredicateFormula> atomicPropositions;
	private List<PredicateFormula> subFormula;
	private FormulaStringCollection variableList;
	public AndOperator generatedFormula;
	private PredicateFormula repairFormula;
	
	
	/**
	 * @param modal: the kripke modal to verify
	 * @param formula: the formula to model check
	 */
	public OptRepFormula(Kripke modal, PredicateFormula formula) {
		
		this.statesList = modal.getStatesList();
		this.atomicPropositions = modal.getAtomicPropositions();
		this.subFormula = formula.getSubFormulea();
		variableList = new FormulaStringCollection();
		initialStatesList = modal.getStartStatesList();
		generatedFormula = new AndOperator(null, null);
		repairFormula = formula;
	}
	
	public PredicateFormula getRepairFormula() {
		ComputegeneratedFormula();
		PredicateFormula f = generatedFormula.ConvertToCNF(generatedFormula, new Random(), variableList);
		return f;
	}
	private void ComputegeneratedFormula()
	{
		InitializeStateFormulas();
		InitializegeneratedFormula();
		for (KripkeState state : statesList) {
			for (int i=0;i < state.OldS.size();i++) {
				PredicateFormula formula = state.OldS.get(i);
					if(formula.getClass().equals(NotOperator.class))
					{
						//System.out.println("NotOperator: " + formula.toString());
						BooleanPredicate Xsnotfi = new BooleanPredicate(variableList.getString("X", state, null, new NotOperator(formula), -1));
						BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
						NotOperator notXsnotfi = new NotOperator(Xsfi);
						generatedFormula.And(TurnEquivToAnd(Xsnotfi, notXsnotfi));
					}
					if(formula.getClass().equals(OrOperator.class))
					{
						//System.out.println("OrOperator: " + formula.toString());
						BooleanPredicate Xsfiorpsi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
						BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, ((OrOperator) formula).getLeftChild(), -1));						
						BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ((OrOperator) formula).getRightChild(), -1));
						OrOperator XsfiorXspsi = new OrOperator(Xsfi, Xspsi);
						generatedFormula.And(TurnEquivToAnd(Xsfiorpsi, XsfiorXspsi));
					}
					if(formula.getClass().equals(AndOperator.class))
					{
						//System.out.println("AndOperator: " + formula.toString());
						BooleanPredicate Xsfiandpsi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
						BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, ((AndOperator) formula).getLeftChild(), -1));
						BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ((AndOperator) formula).getRightChild(), -1));
						AndOperator XsfiandXspsi = new AndOperator(Xsfi, Xspsi);
						generatedFormula.And(TurnEquivToAnd(Xsfiandpsi, XsfiandXspsi));
					}
					if(formula.getClass().equals(AXOperator.class))
					{
						//System.out.println("AXOperator: " + formula.toString());
						if (state.getTransitions().size() > 0) {
							AXOperator axOp = (AXOperator)formula;
							BooleanPredicate Xsaxfi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,axfi							
							AndOperator transitionsAnd = new AndOperator(null,null);
							for (Transition t : state.getTransitions()) {
								BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));								
								BooleanPredicate Xtfi = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), axOp.getChild(), -1));
								transitionsAnd.And(TurnImplyToOr(Est, Xtfi));
							}
							generatedFormula.And(TurnEquivToAnd(Xsaxfi, transitionsAnd));
						}
					}
					if(formula.getClass().equals(EXOperator.class))
					{
						//System.out.println("EXOperator: " + formula.toString());
						if (state.getTransitions().size() > 0) {
							EXOperator exOp = (EXOperator)formula;
							BooleanPredicate Xsexfi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
							OrOperator transitionsOr = new OrOperator(null,null);
							for (Transition t : state.getTransitions()) {
								BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));//Es,t						
								BooleanPredicate Xtfi = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), exOp.getChild(), -1));//Xt,fi
								transitionsOr.Or(TurnImplyToOr(Est, Xtfi));
							}
							generatedFormula.And(TurnEquivToAnd(Xsexfi, transitionsOr));
						}
					}
					if(formula.getClass().equals(AVOperator.class) && ((AVOperator)formula).superScript<0)
					{
						//System.out.println("AVOperator -1 : " + formula.toString());
						int n = statesList.size();
						BooleanPredicate Xsf = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,A[fi V psi]
						BooleanPredicate Xnsf = new BooleanPredicate(variableList.getString("X", state, null, formula, n));//Xn s,A[fi V psi]
						generatedFormula.And(TurnEquivToAnd(Xsf, Xnsf));
						AVOperator oldAv = (AVOperator)formula;
						AVOperator newAv = new AVOperator(oldAv.getLeftChild(), oldAv.getRightChild());
						newAv.superScript = n;
						state.OldS.add(newAv);
					}
					if(formula.getClass().equals(AVOperator.class) && ((AVOperator)formula).superScript>0)
					{
						//System.out.println("AVOperator " + ((AVOperator)formula).superScript + " : " + formula.toString());
						//for all A[fi V psi] in sub(n) m in [1..n]: Xm s,A[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,A[fi V psi]))))
						int n = statesList.size();
						AVOperator av = (AVOperator)formula;
						BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, av.getRightChild(), -1));//X s,psi
						BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, av.getLeftChild(), -1));//X s,fi
						Collection<Transition> stateTrans = state.getTransitions();
						
						if(stateTrans != null && stateTrans.size() > 0)
						{
						
							AndOperator stateTransAnds = new AndOperator(null, null);
							BooleanPredicate Xmsf = new BooleanPredicate(variableList.getString("X", state, null, formula, av.superScript));//Xm s,A[fi V psi]
							
							for (Transition t : stateTrans) {
								BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));//E s,t
								BooleanPredicate Xm_1tf = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), av, av.superScript-1));//Xm-1 t,A[fi V psi]
								OrOperator implicOr = new OrOperator(new NotOperator(Est), Xm_1tf);
								stateTransAnds.And(implicOr);
							}
							OrOperator rightOr = new OrOperator(Xsfi, stateTransAnds.CheckForNull());
							AndOperator rightAnd;
							if(av.getLeftChild().toString().toLowerCase().equals("false"))
							{
								rightAnd = new AndOperator(Xspsi, stateTransAnds.CheckForNull());
							}
							else
								rightAnd = new AndOperator(Xspsi, rightOr.CheckForNull());
							
							AndOperator equivAnd = TurnEquivToAnd(Xmsf, rightAnd);
							generatedFormula.And(equivAnd);							
							AVOperator newAv = new AVOperator(av.getLeftChild(), av.getRightChild());
							newAv.superScript = av.superScript-1;
							state.OldS.add(newAv);
						}
					}
					if(formula.getClass().equals(AVOperator.class) && ((AVOperator)formula).superScript==0)
					{
						//System.out.println("AVOperator 0: " + formula.toString());
						AVOperator av = (AVOperator)formula;
						BooleanPredicate X0sf = new BooleanPredicate(variableList.getString("X", state, null, formula, 0));//X0 s,A[fi V psi]
						BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, av.getRightChild(), -1));//X s,psi
						generatedFormula.And(TurnEquivToAnd(X0sf, Xspsi));
					}
					if(formula.getClass().equals(EVOperator.class) && ((EVOperator)formula).superScript<0)
					{
						
						int n = statesList.size();
						BooleanPredicate Xsf = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,A[fi V psi]
						BooleanPredicate Xnsf = new BooleanPredicate(variableList.getString("X", state, null, formula, n));//Xn s,A[fi V psi]
						generatedFormula.And(TurnEquivToAnd(Xsf, Xnsf));
						EVOperator oldEv = (EVOperator)formula;
						EVOperator newEv = new EVOperator(oldEv.getLeftChild(), oldEv.getRightChild());
						newEv.superScript = n;
						state.OldS.add(newEv);
						
					}
					if(formula.getClass().equals(EVOperator.class) && ((EVOperator)formula).superScript>0)
					{
						int n = statesList.size();
						BooleanPredicate Xsf = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,E[fi V psi]
						BooleanPredicate Xnsf = new BooleanPredicate(variableList.getString("X", state, null, formula, n));//Xn s,E[fi V psi]
						//for all E[fi V psi] in sub(n) m in [1..n]: Xm s,E[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,E[fi V psi]))))  
						EVOperator ev = (EVOperator)formula;
						BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ev.getRightChild(), -1));//X s,psi
						BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, ev.getLeftChild(), -1));//X s,fi
						Collection<Transition> stateTrans = state.getTransitions();
						
						if(stateTrans != null && stateTrans.size() > 0)
						{
							OrOperator stateTransOrs = new OrOperator(null, null);
							BooleanPredicate Xmsf = new BooleanPredicate(variableList.getString("X", state, null, formula, ev.superScript));//Xm s,A[fi V psi]
							
							for (Transition t : stateTrans) {
								BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));//E s,t
								BooleanPredicate Xm_1tf = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), ev, ev.superScript-1));//Xm-1 t,E[fi V psi]
								OrOperator implicOr = new OrOperator(new NotOperator(Est), Xm_1tf);
								stateTransOrs.Or(implicOr);
							}
							
							OrOperator rightOr = new OrOperator(Xsfi, stateTransOrs.CheckForNull());
							AndOperator rightAnd;
							if(ev.getLeftChild().toString().toLowerCase().equals("false"))
							{
								rightAnd = new AndOperator(Xspsi, stateTransOrs.CheckForNull());
							}
							else
								rightAnd = new AndOperator(Xspsi, rightOr.CheckForNull());
							AndOperator equivAnd = TurnEquivToAnd(Xmsf, rightAnd);
							generatedFormula.And(equivAnd);							
							EVOperator newEv = new EVOperator(ev.getLeftChild(), ev.getRightChild());
							newEv.superScript = ev.superScript-1;
							state.OldS.add(newEv);					
						
						}
						
					}
					if(formula.getClass().equals(EVOperator.class) && ((EVOperator)formula).superScript==0)
					{
						EVOperator ev = (EVOperator)formula;
						BooleanPredicate X0sf = new BooleanPredicate(variableList.getString("X", state, null, formula, 0));//X0 s,A[fi V psi]
						BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ev.getRightChild(), -1));//X s,psi
						generatedFormula.And(TurnEquivToAnd(X0sf, Xspsi));
					}
			}
		}
	}
	
	
	/**
	 * forall s in S; f in sub(F) : marked(s; f) := false
	 */
	private void InitializeStateFormulas()
	{
		for (KripkeState state : statesList) {
			for (PredicateFormula f : subFormula) {
				//f.marked = false;
				state.OldS.add(f);
			}
		}
	}
	
	
	private void InitializegeneratedFormula(){
		generatedFormula.And(InitialStatesSatisfyFormula());
		generatedFormula.And(RetainInitialStates());
		generatedFormula.And(GetTotalModelFormula());
		generatedFormula.And(GetPropositionalLabeling());
		generatedFormula.And(TransitionsRules());
	}
	
	
	/**
	 * E s,t --> Xs and Xt
	 * Xs <--> for all t| s-->t  Es,t and Xt
	 */
	private PredicateFormula TransitionsRules(){
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : statesList) {
			OrOperator orOp = new OrOperator(null, null);
			BooleanPredicate Xs = new BooleanPredicate(variableList.getString("X", state, null, null, -1));
			Collection<Transition> stateTrans = state.getTransitions();
			if(stateTrans.size() > 0)
			{
				for (Transition trans : stateTrans) {
					BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, trans.getEndState(), null, -1));
					BooleanPredicate Xt = new BooleanPredicate(variableList.getString("X", trans.getEndState(), null, null, -1));
					orOp.Or(new AndOperator(Est, Xt));
					//E s,t --> Xs and Xt
					NotOperator notEst = new NotOperator(Est);
					AndOperator XsanXt = new AndOperator(Xs, Xt);
					andOp.And(new OrOperator(notEst, XsanXt));				
				}
				//Xs <--> for all t| s-->t  Es,t and Xt
				AndOperator equiv = TurnEquivToAnd(Xs, orOp.CheckForNull());
				andOp.And(equiv);
			}
		}		
		return andOp.CheckForNull();
	}
	
	private AndOperator TurnEquivToAnd(PredicateFormula left, PredicateFormula right){
		NotOperator notLeft = new NotOperator(left);
		NotOperator notRight = new NotOperator(right);
		OrOperator orLeft  = new OrOperator(notLeft, right);
		OrOperator orRight  = new OrOperator(notRight, left);
		return new AndOperator(orLeft, orRight);
	}
	private OrOperator TurnImplyToOr(PredicateFormula left, PredicateFormula right){
		NotOperator notLeft = new NotOperator(left);
		OrOperator orOp  = new OrOperator(notLeft, right);
		return orOp;
	}
	
	
	
	/**
	 * Returns a formula based on the following: 
	 * for all S0 in initial states: Xs0
	 */
	private PredicateFormula RetainInitialStates(){
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : initialStatesList) {
			BooleanPredicate Xs0 = new BooleanPredicate(variableList.getString("X", state, null, null, -1));
			andOp.And(Xs0);
		}
		return andOp.CheckForNull();
	}
	
	private PredicateFormula InitialStatesSatisfyFormula()
	{
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : initialStatesList) {
			BooleanPredicate Xs0 = new BooleanPredicate(variableList.getString("X", state, null, repairFormula.ConvertToCTL(), -1));
			andOp.And(Xs0);
		}
		return andOp.CheckForNull();
	}
	/**
	 * Returns a formula based on the following: 
	 * for all s in S : V t|s-->t E s,t
	 * @return a disjunction of E s,t
	 */
	private PredicateFormula GetTotalModelFormula() {
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : statesList) {
			OrOperator orOp = new OrOperator(null, null);
			Collection<Transition> stateTrans = state.getTransitions();
			for (Transition trans : stateTrans) {
				orOp.Or(new BooleanPredicate(variableList.getString("E", state, trans.getEndState(), null, -1)));
			}
			andOp.And(orOp.CheckForNull());
		}
		return andOp.CheckForNull();
	}
	/**
	 * Returns a formula based on the following: 
	 * for all p in AP and L(s):X s,p
	 * for all p in AP - L(s): �X s,p
	 * 
	 * @return a conjunction of X s,p and (not X s,p)
	 */
	private PredicateFormula GetPropositionalLabeling() {
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : statesList) {
			List<BooleanPredicate> stateLabels = state.getLabels();
			for (PredicateFormula f : atomicPropositions) {
				if (stateLabels.contains(f))
					andOp.And(new BooleanPredicate(variableList.getString("X", state, null, f, -1)));
				else
					andOp.And( new NotOperator(new BooleanPredicate(variableList.getString("X", state, null, f, -1))));
			}
		}
		return andOp.CheckForNull();
	}
	
	
	public void printSubFormulas(){
		for (PredicateFormula f : subFormula) {
			//System.out.println(f.toString());
		}
	}
	public FormulaStringCollection getVariableList() {
		return variableList;
	}




	public void setVariableList(FormulaStringCollection variableList) {
		this.variableList = variableList;
	}

}
