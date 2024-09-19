package eshmun.modelrepairer;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import eshmun.expression.PredicateFormula;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.ctl.AVOperator;
import eshmun.expression.ctl.AXOperator;
import eshmun.expression.ctl.EVOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.lts.kripke.Transition;

public class OptimizedFormulaeGenerator {
	
	
	private List<KripkeState> statesList;
	private List<KripkeState> initialStatesList;
	private List<PredicateFormula> atomicPropositions;
	private FormulaStringCollection variableList;
	public AndOperator generatedFormula;
	private PredicateFormula repairFormula;
	private PredicateFormula ctlFormula;
	private Queue<KripkeState> statesToBeChecked;
	
	
	/**
	 * @param modal: the kripke modal to verify
	 * @param formula: the formula to model check
	 */
	public OptimizedFormulaeGenerator(Kripke modal, PredicateFormula formula) {
		
		this.statesList = modal.getStatesList();
		this.atomicPropositions = modal.getAtomicPropositions();
		variableList = new FormulaStringCollection();
		initialStatesList = modal.getStartStatesList();
		generatedFormula = new AndOperator(null, null);
		repairFormula = formula;
		ctlFormula = formula.ConvertToCTL();
		statesToBeChecked = new LinkedList<KripkeState>();
	}
	
	
	
	public PredicateFormula ComputegeneratedFormula(List<Transition> transitionsToRetain, List<KripkeState> statesToRetain) {
		
		try
		{
			InitializegeneratedFormula();
			PredicateFormula pfToRetain = RetainStatesAndTransitions(transitionsToRetain, statesToRetain);
			if(pfToRetain != null)
				generatedFormula.And(pfToRetain);
			KripkeState state;
			while((state=statesToBeChecked.poll())!=null)
			{
				PredicateFormula f;
				while((f = state.NewS.poll())!=null)
				{
					Propagate(state, f);
				}
			}		
			PredicateFormula f = generatedFormula.ConvertToCNF(generatedFormula, new Random(), variableList);		
			return f;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getStackTrace());
			return null;
		}
		finally
		{
			for (KripkeState state1 : statesList) {
				state1.OldS= new ArrayList<PredicateFormula>();  
				state1.NewS = new LinkedList<PredicateFormula>();
			}
		}
	}
	
	
	
	private void InitializegeneratedFormula(){
		
		PredicateFormula temp;
		temp = RetainInitialStates(); //DONE
		generatedFormula.And(temp);
		temp = TransitionsRules(); //DONE
		generatedFormula.And(temp);
		temp = InitialStatesSatisfyFormula(); //DONE
		generatedFormula.And(temp);
		InitializeInitialStatesNewS();
		temp = GetTotalModelFormula(); //DONE
		generatedFormula.And(temp);
		temp = GetPropositionalLabeling(); //DONE
		generatedFormula.And(temp);
		
	}
	
	private void Propagate(KripkeState state, PredicateFormula formula)
	{
		///optimization check 1
		if(containsFormula(state.OldS, formula))
			return;
		//NotOperator
		if(formula.getClass().equals(NotOperator.class))
		{
			//System.out.println("State: " + state.getName() + " NotOperator: " + formula.toString());
			BooleanPredicate Xsnotfi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
			PredicateFormula fi = ((NotOperator) formula).getChild();
			BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, fi, -1));
			NotOperator notXsfi = new NotOperator(Xsfi);
			AndOperator equiv = TurnEquivToAnd(Xsnotfi, notXsfi);
			//System.out.println("State: " + state.getName() + " NotOperator: " + equiv.toString());
			generatedFormula.And(equiv);
			addToNewS(state, fi);
		}
		//OrOperator
		else if(formula.getClass().equals(OrOperator.class))
		{
			//System.out.println("State: " + state.getName() + " OrOperator: " + formula.toString());
			BooleanPredicate Xsfiorpsi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
			BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, ((OrOperator) formula).getLeftChild(), -1));						
			BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ((OrOperator) formula).getRightChild(), -1));
			OrOperator XsfiorXspsi = new OrOperator(Xsfi, Xspsi);
			generatedFormula.And(TurnEquivToAnd(Xsfiorpsi, XsfiorXspsi));
			addToNewS(state, ((OrOperator) formula).getLeftChild());
			addToNewS(state, ((OrOperator) formula).getRightChild());
		}
		//AndOperator
		else if(formula.getClass().equals(AndOperator.class))
		{
			//System.out.println("State: " + state.getName() + " AndOperator: " + formula.toString());
			BooleanPredicate Xsfiandpsi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
			BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", state, null, ((AndOperator) formula).getLeftChild(), -1));
			BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ((AndOperator) formula).getRightChild(), -1));
			AndOperator XsfiandXspsi = new AndOperator(Xsfi, Xspsi);
			generatedFormula.And(TurnEquivToAnd(Xsfiandpsi, XsfiandXspsi));
			addToNewS(state, ((AndOperator) formula).getLeftChild());
			addToNewS(state, ((AndOperator) formula).getRightChild());
		}
		//AXOperator
		else if(formula.getClass().equals(AXOperator.class))
		{
			//System.out.println("State: " + state.getName() + " AXOperator: " + formula.toString());
			if (state.getTransitions().size() > 0) {
				AXOperator axOp = (AXOperator)formula;
				BooleanPredicate Xsaxfi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,axfi							
				AndOperator transitionsAnd = new AndOperator(null,null);
				for (Transition t : state.getTransitions()) {
					BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));								
					BooleanPredicate Xtfi = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), axOp.getChild(), -1));
					transitionsAnd.And(TurnImplyToOr(Est, Xtfi));
					addToNewS(t.getEndState(), axOp.getChild());
				}
				generatedFormula.And(TurnEquivToAnd(Xsaxfi, transitionsAnd.CheckForNull()));
			}
		}
		//EXOperator
		else if(formula.getClass().equals(EXOperator.class))
		{
			//System.out.println("State: " + state.getName() + " EXOperator: " + formula.toString());
			if (state.getTransitions().size() > 0) {
				EXOperator exOp = (EXOperator)formula;
				BooleanPredicate Xsexfi = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));
				OrOperator transitionsOr = new OrOperator(null,null);
				for (Transition t : state.getTransitions()) {
					BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));//Es,t						
					BooleanPredicate Xtfi = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), exOp.getChild(), -1));//Xt,fi
					transitionsOr.Or(new AndOperator(Est, Xtfi));
					addToNewS(t.getEndState(), exOp.getChild());
				}
				generatedFormula.And(TurnEquivToAnd(Xsexfi, transitionsOr.CheckForNull()));
			}
		}
		//AVOperator
		else if(formula.getClass().equals(AVOperator.class) && ((AVOperator)formula).superScript<0)
		{
			//System.out.println("State: " + state.getName() + " AVOperator -1 : " + formula.toString());
			int n = statesList.size();
			BooleanPredicate Xsf = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,A[fi V psi]
			BooleanPredicate Xnsf = new BooleanPredicate(variableList.getString("X", state, null, formula, n));//Xn s,A[fi V psi]
			generatedFormula.And(TurnEquivToAnd(Xsf, Xnsf));
			AVOperator oldAv = (AVOperator)formula;
			AVOperator newAv = new AVOperator(oldAv.getLeftChild(), oldAv.getRightChild());
			//AVOperator newAv = (AVOperator) UnoptimizedDeepCopy.copy(oldAv);
			newAv.superScript = n;
			
			addToNewS(state, newAv);
			addToNewS(state, oldAv.getLeftChild());
			addToNewS(state, oldAv.getRightChild());
		}
		//AVOperator with m superscript
		else if(formula.getClass().equals(AVOperator.class) && ((AVOperator)formula).superScript>0)
		{
			//System.out.println("State: " + state.getName() + " AVOperator " + ((AVOperator)formula).superScript + " : " + formula.toString());
			//for all A[fi V psi] in sub(n) m in [1..n]: Xm s,A[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,A[fi V psi]))))
			int n = statesList.size();
			AVOperator av = (AVOperator)formula;
			BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, av.getRightChild(), -1));//X s,psi
			BooleanPredicate Xsfi = null;
			if(!(av.getLeftChild().toString().toLowerCase().equals("false")))
				Xsfi = new BooleanPredicate(variableList.getString("X", state, null, av.getLeftChild(), -1));//X s,fi
			Collection<Transition> stateTrans = state.getTransitions();
			
			if(stateTrans != null && stateTrans.size() > 0)
			{
			
				AndOperator stateTransAnds = new AndOperator(null, null);
				BooleanPredicate Xmsf = new BooleanPredicate(variableList.getString("X", state, null, formula, av.superScript));//Xm s,A[fi V psi]
				//to be added to NewS
				AVOperator newAv = new AVOperator(av.getLeftChild(), av.getRightChild());
				newAv.superScript = av.superScript-1;
				//
				for (Transition t : stateTrans) {
					BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));//E s,t
					BooleanPredicate Xm_1tf = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), av, av.superScript-1));//Xm-1 t,A[fi V psi]
					OrOperator implicOr = new OrOperator(new NotOperator(Est), Xm_1tf);
					stateTransAnds.And(implicOr);
					addToNewS(t.getEndState(), newAv);
					addToNewS(t.getEndState(), av.getLeftChild());
					addToNewS(t.getEndState(), av.getRightChild());
				}
				
				AndOperator rightAnd;
				if(av.getLeftChild().toString().toLowerCase().equals("false"))
				{
					rightAnd = new AndOperator(Xspsi, stateTransAnds.CheckForNull());
				}
				else
				{
					OrOperator rightOr = new OrOperator(Xsfi, stateTransAnds.CheckForNull());
					rightAnd = new AndOperator(Xspsi, rightOr.CheckForNull());
				}
				
				AndOperator equivAnd = TurnEquivToAnd(Xmsf, rightAnd);
				generatedFormula.And(equivAnd);		
			}
		}
		//AVOperator with 0 superscript
		else if(formula.getClass().equals(AVOperator.class) && ((AVOperator)formula).superScript==0)
		{
			//System.out.println("State: " + state.getName() + " AVOperator 0: " + formula.toString());
			AVOperator av = (AVOperator)formula;
			BooleanPredicate X0sf = new BooleanPredicate(variableList.getString("X", state, null, formula, 0));//X0 s,A[fi V psi]
			BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, av.getRightChild(), -1));//X s,psi
			generatedFormula.And(TurnEquivToAnd(X0sf, Xspsi));
		}
		//EVOperator
		else if(formula.getClass().equals(EVOperator.class) && ((EVOperator)formula).superScript<0)
		{
			//System.out.println("State: " + state.getName() + " EVOperator -1 : " + formula.toString());
			int n = statesList.size();
			BooleanPredicate Xsf = new BooleanPredicate(variableList.getString("X", state, null, formula, -1));//X s,E[fi V psi]
			BooleanPredicate Xnsf = new BooleanPredicate(variableList.getString("X", state, null, formula, n));//Xn s,E[fi V psi]
			AndOperator equiv = TurnEquivToAnd(Xsf, Xnsf);
			//System.out.println("State: " + state.getName() + " EVOperator -1 : " + equiv.toString());
			generatedFormula.And(equiv);
			EVOperator oldEv = (EVOperator)formula;
			EVOperator newEv = new EVOperator(oldEv.getLeftChild(), oldEv.getRightChild());
			newEv.superScript = n;
			addToNewS(state, newEv);
			addToNewS(state, oldEv.getLeftChild());
			addToNewS(state, oldEv.getRightChild());
			
		}
		//EVOperator with m superscript
		else if(formula.getClass().equals(EVOperator.class) && ((EVOperator)formula).superScript>0)
		{
			//System.out.println("State: " + state.getName() + " EVOperator " + ((EVOperator)formula).superScript + " : " + formula.toString());
			int n = statesList.size();
			//for all E[fi V psi] in sub(n) m in [1..n]: Xm s,E[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,E[fi V psi]))))  
			EVOperator ev = (EVOperator)formula;
			BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ev.getRightChild(), -1));//X s,psi
			BooleanPredicate Xsfi = null;
			if(!(ev.getLeftChild().toString().toLowerCase().equals("false")))
				Xsfi = new BooleanPredicate(variableList.getString("X", state, null, ev.getLeftChild(), -1));//X s,fi
			Collection<Transition> stateTrans = state.getTransitions();
			
			if(stateTrans != null && stateTrans.size() > 0)
			{
				OrOperator stateTransOrs = new OrOperator(null, null);
				BooleanPredicate Xmsf = new BooleanPredicate(variableList.getString("X", state, null, formula, ev.superScript));//Xm s,A[fi V psi]
				//to be added to NewS
				EVOperator newEv = new EVOperator(ev.getLeftChild(), ev.getRightChild());
				newEv.superScript = ev.superScript-1;
				//
				for (Transition t : stateTrans) {
					BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", state, t.getEndState(), null, -1));//E s,t
					BooleanPredicate Xm_1tf = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), ev, ev.superScript-1));//Xm-1 t,E[fi V psi]
					AndOperator implicAnd = new AndOperator(Est, Xm_1tf);
					stateTransOrs.Or(implicAnd);
					addToNewS(t.getEndState(), newEv);
					addToNewS(t.getEndState(), ev.getLeftChild());
					addToNewS(t.getEndState(), ev.getRightChild());
				}
				
				
				AndOperator rightAnd;
				if(ev.getLeftChild().toString().toLowerCase().equals("false"))
				{
					rightAnd = new AndOperator(Xspsi, stateTransOrs.CheckForNull());
				}
				else
				{
					OrOperator rightOr = new OrOperator(Xsfi, stateTransOrs.CheckForNull());
					rightAnd = new AndOperator(Xspsi, rightOr.CheckForNull());
				}
				AndOperator equivAnd = TurnEquivToAnd(Xmsf, rightAnd);
				//System.out.println("State: " + state.getName() + " EVOperator " + ((EVOperator)formula).superScript + " : " + equivAnd.toString());
				generatedFormula.And(equivAnd);						
			
			}
			
		}
		//EVOperator with 0 superscript
		else if(formula.getClass().equals(EVOperator.class) && ((EVOperator)formula).superScript==0)
		{
			//System.out.println("State: " + state.getName() + " EVOperator 0: " + formula.toString());
			
			EVOperator ev = (EVOperator)formula;
			BooleanPredicate X0sf = new BooleanPredicate(variableList.getString("X", state, null, formula, 0));//X0 s,A[fi V psi]
			BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", state, null, ev.getRightChild(), -1));//X s,psi
			AndOperator andOp = TurnEquivToAnd(X0sf, Xspsi);
			generatedFormula.And(andOp);
			//System.out.println("State: " + state.getName() + " EVOperator 0: " + andOp.toString());
		}
		state.OldS.add(formula);
	}
	
	
	
	/**
	 * Initialize Initial States NewS with the formula in CTL form
	 */
	private void InitializeInitialStatesNewS()
	{
		for (KripkeState state : initialStatesList) {
			addToNewS(state, ctlFormula);
		}
	}

	
	/**
	 * all initial states satisfy the formulae 
	 * Returns a formula based on the following:
	 * for all s0 in S0 : Xs0f 
	 * @return a conjunction  of Xs0f
	 */
	private PredicateFormula InitialStatesSatisfyFormula()
	{
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : initialStatesList) {
			BooleanPredicate Xs0 = new BooleanPredicate(variableList.getString("X", state, null, ctlFormula, -1));
			andOp.And(Xs0);
		}
		//System.out.println("InitialStatesSatisfyFormula: " + andOp.CheckForNull());
		return andOp.CheckForNull();
	}
	/**
	 * Retain initial States
	 * Returns a formula based on the following: 
	 * for all S0 in initial states: Xs0
	 */
	private PredicateFormula RetainInitialStates(){
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : initialStatesList) {
			BooleanPredicate Xs0 = new BooleanPredicate(variableList.getString("X", state, null, null, -1));
			andOp.And(Xs0);
		}
		//System.out.println("RetainInitialStates : " + andOp.CheckForNull().toString());
		return andOp.CheckForNull();
	}
	
	private PredicateFormula RetainStatesAndTransitions(List<Transition> transitionsToRetain, List<KripkeState> statesToRetain){
		AndOperator andOp = new AndOperator(null, null);
		boolean changed = false; 
		if(statesToRetain != null && statesToRetain.size() > 0)
		{
			for (KripkeState state : statesToRetain) {
				BooleanPredicate Xs = new BooleanPredicate(variableList.getString("X", state, null, null, -1));
				andOp.And(Xs);
			}
			changed = true;
		}
		
		if(transitionsToRetain != null && transitionsToRetain.size() > 0)
		{
			for (Transition trns : transitionsToRetain) {
				BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", trns.getStartState(), trns.getEndState(), null, -1));
				andOp.And(Est);
			}
			changed = true;
		}
		
		if(changed)
		{
			//System.out.println("RetainStatesAndTransitions : " + andOp.CheckForNull().toString());
			return andOp.CheckForNull();
		}
		return null;
	}
	/**
	 * if a transition is retained both state should be retained
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
			else // if a state has no transition, it should not exist as a prerequisite the modal has to be total
			{
				BooleanPredicate Xstate = new BooleanPredicate(variableList.getString("X", state, null, null, -1));
				NotOperator notXstate = new NotOperator(Xstate);
				andOp.And(notXstate);
			}
		}		
		//System.out.println("TransitionsRules: " + andOp.CheckForNull());
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
		//System.out.println("GetTotalModelFormula: " + andOp.CheckForNull());
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
				if(!f.toString().trim().toLowerCase().equals("false") && !f.toString().trim().toLowerCase().equals("true"))
				if (stateLabels.contains(f))
					andOp.And(new BooleanPredicate(variableList.getString("X", state, null, f, -1)));
				else
					andOp.And( new NotOperator(new BooleanPredicate(variableList.getString("X", state, null, f, -1))));
			}
		}
		
		///start: labels that are in formula but not in structure
		List<BooleanPredicate> frmlBP = repairFormula.getBooleanPredicates();
		
		for (KripkeState state : statesList) {
			List<BooleanPredicate> stateLabels = state.getLabels();
			for (PredicateFormula f : frmlBP) {
				if(! atomicPropositions.contains(f) && !f.toString().trim().toLowerCase().equals("false") && !f.toString().trim().toLowerCase().equals("true"))
				{
					if (stateLabels.contains(f))
						andOp.And(new BooleanPredicate(variableList.getString("X", state, null, f, -1)));
					else
						andOp.And( new NotOperator(new BooleanPredicate(variableList.getString("X", state, null, f, -1))));
				}
			}
		}
		///start: labels that are in formula but not in structure
		//System.out.println("GetPropositionalLabeling: " + andOp.CheckForNull());
		return andOp.CheckForNull();
	}
	
	
	
	public FormulaStringCollection getVariableList() {
		return variableList;
	}
	public void setVariableList(FormulaStringCollection variableList) {
		this.variableList = variableList;
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

	public void addToNewS(KripkeState state, PredicateFormula f)
	{
		if(!containsFormula(state.NewS,f))
			state.NewS.add(f);
		if(!privateContainCheck(statesToBeChecked,state))
			statesToBeChecked.add(state);
	}
	
	private boolean privateContainCheck(Queue<KripkeState> states, KripkeState searchedState)
	{
		for (KripkeState state : states) {
			if(state.getName().trim().equals(searchedState.getName().trim()))
				return true;
		}
		return false;
	}
	
	private boolean containsFormula(Queue<PredicateFormula> q, PredicateFormula formula)
	{
		if(formula.getClass().equals(AVOperator.class))
		{
			for (PredicateFormula f : q) {
				if(f.getClass().equals(AVOperator.class))
				{
					AVOperator newAv = (AVOperator)formula;
					AVOperator oldAv = (AVOperator)f;
					if(f.equals(formula) && oldAv.superScript == newAv.superScript)
						return true;
				}
			}
			return false;
		}
		else if(formula.getClass().equals(EVOperator.class))
		{
			for (PredicateFormula f : q) {
				if(f.getClass().equals(EVOperator.class))
				{
					EVOperator newEv = (EVOperator)formula;
					EVOperator oldEv = (EVOperator)f;
					if(f.equals(formula) && oldEv.superScript == newEv.superScript)
						return true;
				}
			}
			return false;
		}
		else
		{
			return q.contains(formula);
		}
	}
	private boolean containsFormula(List<PredicateFormula> q, PredicateFormula formula)
	{
		if(formula.getClass().equals(AVOperator.class))
		{
			for (PredicateFormula f : q) {
				if(f.getClass().equals(AVOperator.class))
				{
					AVOperator newAv = (AVOperator)formula;
					AVOperator oldAv = (AVOperator)f;
					if(f.equals(formula) && oldAv.superScript == newAv.superScript)
						return true;
				}
			}
			return false;
		}
		else if(formula.getClass().equals(EVOperator.class))
		{
			for (PredicateFormula f : q) {
				if(f.getClass().equals(EVOperator.class))
				{
					EVOperator newEv = (EVOperator)formula;
					EVOperator oldEv = (EVOperator)f;
					if(f.equals(formula) && oldEv.superScript == newEv.superScript)
						return true;
				}
			}
			return false;
		}
		else
		{
			return q.contains(formula);
		}
	}
	
	public void resetRepairer()
	{
		//empty queues
		for (KripkeState state : statesList) {
			state.NewS.clear();
			state.OldS.clear();
		}
		statesToBeChecked.clear();
		//
		//reset global variables
		this.statesList.clear();
		this.atomicPropositions.clear();
		variableList = null;
		initialStatesList.clear();
		generatedFormula = new AndOperator(null, null);
		repairFormula = null;
		ctlFormula = null;
		
	}

}
