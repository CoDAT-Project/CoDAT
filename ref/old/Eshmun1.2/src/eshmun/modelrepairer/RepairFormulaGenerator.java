package eshmun.modelrepairer;

import eshmun.expression.*;
import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.expression.ctl.AVOperator;
import eshmun.expression.ctl.AXOperator;
import eshmun.expression.ctl.EVOperator;
import eshmun.expression.ctl.EXOperator;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.lts.kripke.*;

import java.util.List;
import java.util.PriorityQueue;
import java.lang.StringBuilder;
import java.util.*;

/**
 * @author ms186
 * 
 */
public class RepairFormulaGenerator {

	private List<KripkeState> statesList;
	private List<PredicateFormula> atomicPropositions;
	private List<PredicateFormula> subFormula;
	private FormulaStringCollection variableList;
	private PredicateFormula repairFormula;
	private List<KripkeState> initialStatesList;

	public RepairFormulaGenerator(Kripke modal, PredicateFormula formula) {
		this.statesList = modal.getStatesList();
		this.atomicPropositions = modal.getAtomicPropositions();
		this.subFormula = formula.getSubFormulea();
		for (PredicateFormula f : subFormula) {
			//System.out.println(f.toString());
		}
		variableList = new FormulaStringCollection();
		repairFormula = formula;
		initialStatesList = modal.getStartStatesList();
	}
	
	

	
	public FormulaStringCollection getVariableList() {
		return variableList;
	}




	public void setVariableList(FormulaStringCollection variableList) {
		this.variableList = variableList;
	}



	/**
	 * generates repair formula based on definition 5
	 * @return PredicateFormula in CNF format
	 */
	public PredicateFormula getRepairFormula() {
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : initialStatesList) {
			BooleanPredicate Xs0 = new BooleanPredicate(variableList.getString("X", state, null, repairFormula.ConvertToCTL(), -1));
			andOp.And(Xs0);
		}
		
		PredicateFormula andOp0 = GetTotalModelFormula();
		if(andOp0 != null)
		{			
			//System.out.println("Total Model Formula: " + andOp0.toString());			
		}
		PredicateFormula andOp1 = GetPropositionalLabelingS();
		if(andOp1 != null)
		{
			//System.out.println("Propositional Labeling: " + andOp1.toString());
		}
		PredicateFormula andOp2 = GetPropositionalConsistancy();
		if(andOp2 != null)
		{
			//System.out.println("Propositional Consistancy: " + andOp2.toString());
		}
		PredicateFormula andOp3 = GetNextTimeFormula();
		if(andOp3 != null)
		{
			//System.out.println("Next Time Formula: " + andOp3.toString());
		}
		PredicateFormula andOp4 = GetReleaseFormula();
		if(andOp4 != null)
		{
			//System.out.println("Release Formula: " + andOp4.toString());
		}
		andOp.And(andOp0);
		andOp.And(andOp1);
		andOp.And(andOp2);
		andOp.And(andOp3);
		andOp.And(andOp4);		
		PredicateFormula f = andOp.ConvertToCNF(andOp, new Random(), variableList);
		if(f != null)
		{
			//System.out.println("CNF-CNF-CNF-CNF-CNF-CNF-CNF-CNF-CNF");
			//System.out.println(f.toString());
			//System.out.println("CNF-CNF-CNF-CNF-CNF-CNF-CNF-CNF-CNF");
		}
		return f;
	}

	/**
	 * Returns a formula based on the following: for all s in S : V t|s-->t E
	 * s,t
	 * 
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
	 * Returns a formula based on the following: for all p in AP and L(s):X s,p
	 * for all p in AP - L(s): �X s,p
	 * 
	 * @return a conjunction of X s,p and (not X s,p)
	 */
	private PredicateFormula GetPropositionalLabelingS() {
		StringBuilder formula = new StringBuilder("");
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

	/**
	 * Returns a formula based on the following: for all AXfi in sub(n) Xs,AXphi
	 * <-> ^t:t->s (Es,t -> Xt,phi) for all EXfi in sub(n) Xs,EXphi <-> Vt:t->s
	 * (Es,t -> Xt,phi)
	 * 
	 * @return a conjunction of X s,p and (not X s,p)
	 */
	private PredicateFormula GetPropositionalConsistancy() {
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState state : statesList) {
			for (PredicateFormula f : subFormula) {
				// X s,�fi <--> �s s,fi
				// is the same as
				// (�X s,�fi v �X s,fi) & (��X s,�fi v X s,fi)
				// inside not as a convention is : �
				// outside not as a convention is : !
				if (f.getClass().equals(NotOperator.class)) {
					BooleanPredicate boolPred1 = new BooleanPredicate(variableList.getString("X", state, null, new NotOperator(f), -1));//(X s,�fi
					BooleanPredicate boolPred2 = new BooleanPredicate(variableList.getString("X", state, null, f, -1));//X s,fi
					NotOperator notOp1 = new NotOperator(boolPred1);//�X s,�fi
					NotOperator notOp2 = new NotOperator(boolPred2);//�X s,fi
					OrOperator orOp1 = new OrOperator(notOp1, notOp2);// (�X s,�fi v �X s,fi)
					OrOperator orOp2 = new OrOperator(boolPred1, boolPred2);//(��X s,�fi v X s,fi)
					AndOperator andOp1 = new AndOperator(orOp1, orOp2);
					andOp.And(andOp1);
				}
				// X <--> Y v Z
				// is the same as
				// (�X v Y v Z) & (�Y v X) & (�Z v X)
				// inside or as a convention is : v
				// outside not as a convention is : |
				else if (f.getClass().equals(OrOperator.class)){
					BooleanPredicate boolPred0 = new BooleanPredicate(variableList.getString("X", state, null, f, -1));//x
					NotOperator notOp0 = new NotOperator(boolPred0);//�X
					BooleanPredicate boolPred1 = new BooleanPredicate(variableList.getString("X", state, null, ((OrOperator) f).getLeftChild(), -1));//y
					NotOperator notOp1 = new NotOperator(boolPred1);//�Y
					BooleanPredicate boolPred2 = new BooleanPredicate(variableList.getString("X", state, null, ((OrOperator) f).getRightChild(), -1));//z
					NotOperator notOp2 = new NotOperator(boolPred2);//�Z
					OrOperator orOp1 = new OrOperator(notOp0, new OrOperator(boolPred1, boolPred2));// (�X v Y v Z)
					OrOperator orOp2 = new OrOperator(notOp1, boolPred0);//(�Y v X)
					OrOperator orOp3 = new OrOperator(notOp2, boolPred0);//(�Z v X)
					AndOperator andOp2 = new AndOperator(orOp1, new AndOperator(orOp2, orOp3));// (�X v Y v Z) & (�Y v X) & (�Z v X)
					andOp.And(andOp2);
					
				}
				// X <--> Y ^ Z
				// is the same as
				// (�X v Y) ^(�X v Z) ^ (�Y v �Z v X)
				// inside or as a convent ion is : v
				// outside not as a convention is : |
				else if (f.getClass().equals(AndOperator.class)){
					BooleanPredicate boolPred0 = new BooleanPredicate(variableList.getString("X", state, null, f, -1));//x
					NotOperator notOp0 = new NotOperator(boolPred0);//�X
					BooleanPredicate boolPred1 = new BooleanPredicate(variableList.getString("X", state, null, ((AndOperator) f).getLeftChild(), -1));//y
					NotOperator notOp1 = new NotOperator(boolPred1);//�Y
					BooleanPredicate boolPred2 = new BooleanPredicate(variableList.getString("X", state, null, ((AndOperator) f).getRightChild(), -1));//z
					NotOperator notOp2 = new NotOperator(boolPred2);//�Z
					OrOperator orOp1 = new OrOperator(notOp0, boolPred1);// (�X v Y)
					OrOperator orOp2 = new OrOperator(notOp0, boolPred2);//(�X v Z)
					OrOperator orOp3 = new OrOperator(notOp1, new OrOperator(notOp2,boolPred0));//(�Y v �Z v X)
					AndOperator andOp3 = new AndOperator(orOp1, new AndOperator(orOp2, orOp3));// (�X v Y) ^(�X v Z) ^ (�Y v �Z v X)
					andOp.And(andOp3);
				}
			}
		}
		return andOp.CheckForNull();
	}

	/**
	 * Returns a formula based on the following:
	 *  for all AXfi in sub(n) and for all t : s->t X s,axfi <--> ^t:s->t (!Es,t | Xt,fi )
	 *  for all EXfi in sub(n) and for all t : s->t X s,exfi <--> Vt:s->t (Es,t & Xt,fi )
	 * @return a conjunction of X s,p and (not X s,p)
	 */
	private PredicateFormula GetNextTimeFormula() {
		AndOperator andOp = new AndOperator(null, null);
		for (KripkeState s : statesList) {
			for (PredicateFormula f : subFormula) {
				if (f.getClass().equals(AXOperator.class) && s.getTransitions().size() > 0) {
					AXOperator axOp = (AXOperator)f;
					BooleanPredicate boolPred0 = new BooleanPredicate(variableList.getString("X", s, null, f, -1));//X s,axfi
					NotOperator notOp0 = new NotOperator(boolPred0);
					AndOperator andOp1 = new AndOperator(null,null);
					for (Transition t : s.getTransitions()) {
						BooleanPredicate boolPred1 = new BooleanPredicate(variableList.getString("E", s, t.getEndState(), null, -1));//Es,t
						NotOperator notOp1 = new NotOperator(boolPred1);//!Es,t
						
						BooleanPredicate boolPred2 = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), axOp.getChild(), -1));//Xt,fi
						OrOperator orOp2 = new OrOperator(notOp1, boolPred2);//(!Es,t | Xt,fi )
						andOp1.And(orOp2);// = new AndOperator(andOp1, orOp2);//^t:s->t (!Es,t | Xt,fi )
					}
					if(andOp1.CheckForNull() != null)
					{
						OrOperator orOp3 = new OrOperator(notOp0, andOp1.CheckForNull());
						OrOperator orOp4 = new OrOperator(new NotOperator(andOp1.CheckForNull()), boolPred0);
						AndOperator andOp2 = new AndOperator(orOp3, orOp4);
						andOp.And(andOp2);// = new AndOperator(andOp, andOp2);
					}
				}
				if (f.getClass().equals(EXOperator.class) && s.getTransitions().size() > 0) {
					EXOperator exOp = (EXOperator)f;
					BooleanPredicate boolPred0 = new BooleanPredicate(variableList.getString("X", s, null, f, -1));//X s,exfi
					NotOperator notOp0 = new NotOperator(boolPred0); 
					OrOperator orOp1 = new OrOperator(null,null);
					for (Transition t : s.getTransitions()) {
						BooleanPredicate boolPred1 = new BooleanPredicate(variableList.getString("E", s, t.getEndState(), null, -1));//Es,t						
						BooleanPredicate boolPred2 = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), exOp.getChild(), -1));//Xt,fi
						AndOperator andOp2 = new AndOperator(boolPred1, boolPred2);//(Es,t & Xt,fi )
						orOp1.Or(andOp2);// = new OrOperator(orOp1, andOp2);//Vt:s->t (Es,t & Xt,fi )
					}
					if(orOp1.CheckForNull() != null)
					{
						OrOperator orOp3 = new OrOperator(notOp0, orOp1.CheckForNull());
						OrOperator orOp4 = new OrOperator(new NotOperator(orOp1.CheckForNull()), boolPred0);
						AndOperator andOp2 = new AndOperator(orOp3, orOp4);
						andOp.And(andOp2);// = new AndOperator(andOp, andOp2);
					}
				}
			}
		}
		return andOp.CheckForNull();
	}
	
	/**
	 * Returns a formula based on the following:
	 *  for all A[fi V psi] in sub(n):  X s,A[fi V psi] <--> Xn s,A[fi V psi]
	 *  for all A[fi V psi] in sub(n) m in [1..n]: Xm s,A[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,A[fi V psi])))) 
	 *  for all A[fi V psi] in sub(n) X0 s,E[fi V psi] <--> X s,psi 
	 *  for all E[fi V psi] in sub(n):  X s,E[fi V psi] <--> Xn s,E[fi V psi]
	 *  for all E[fi V psi] in sub(n) m in [1..n]: Xm s,E[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,E[fi V psi])))) 
	 *  for all E[fi V psi] in sub(n) X0 s,E[fi V psi] <--> X s,psi 
	 * @return a AndOperator
	 */
	private PredicateFormula GetReleaseFormula() {
		AndOperator andOp = new AndOperator(null, null);
		int n = statesList.size();
		for (KripkeState s : statesList) {
			for (PredicateFormula f : subFormula) {
				if(f.getClass().equals(AVOperator.class))
				{
					BooleanPredicate Xsf = new BooleanPredicate(variableList.getString("X", s, null, f, -1));//X s,A[fi V psi]
					BooleanPredicate Xnsf = new BooleanPredicate(variableList.getString("X", s, null, f, n));//Xn s,A[fi V psi]
					NotOperator notXsf = new NotOperator(Xsf);
					NotOperator notXnsf = new NotOperator(Xnsf);
					OrOperator notXsfORXnsf = new OrOperator(notXsf, Xnsf);
					OrOperator notXnsfORXsf = new OrOperator(notXnsf, Xsf);
					AndOperator andOp0 = new AndOperator(notXsfORXnsf, notXnsfORXsf);
					
					
					
					//X s,A[fi V psi] <--> Xn s,A[fi V psi]
					andOp.And(andOp0);
					//System.out.println( "andOp0: " + andOp0.toString());
					//for all A[fi V psi] in sub(n) m in [1..n]: Xm s,A[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,A[fi V psi])))) 
					AVOperator av = (AVOperator)f;
					BooleanPredicate Xspsi = new BooleanPredicate(variableList.getString("X", s, null, av.getRightChild(), -1));//X s,psi
					BooleanPredicate Xsfi = new BooleanPredicate(variableList.getString("X", s, null, av.getLeftChild(), -1));//X s,fi
					Collection<Transition> stateTrans = s.getTransitions();
					
					
					for(int i=1; i<=n; i++)
					{
						AndOperator stateTransAnds = new AndOperator(null, null);
						BooleanPredicate Xmsf = new BooleanPredicate(variableList.getString("X", s, null, f, i));//Xm s,A[fi V psi]
						
						for (Transition t : stateTrans) {
							BooleanPredicate Est = new BooleanPredicate(variableList.getString("E", s, t.getEndState(), null, -1));//E s,t
							BooleanPredicate Xm_1tf = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), av, i-1));//Xm-1 t,A[fi V psi]
							
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
						OrOperator finalLeftOr = new OrOperator(new NotOperator(Xmsf), rightAnd);
						OrOperator finalRightOr = new OrOperator(new NotOperator(rightAnd), Xmsf);
						AndOperator equivAnd = new AndOperator(finalLeftOr, finalRightOr);
						andOp.And(equivAnd);
						//System.out.println( "equivAnd: " + equivAnd.toString());
						
					}
					//for all A[fi V psi] in sub(n) X0 s,A[fi V psi] <--> X s,psi
					BooleanPredicate boolPred5 = new BooleanPredicate(variableList.getString("X", s, null, f, 0));//X0 s,A[fi V psi]
					OrOperator finalLeftOr = new OrOperator(new NotOperator(Xspsi), boolPred5);
					OrOperator finalRightOr = new OrOperator(new NotOperator(boolPred5), Xspsi);
					AndOperator equivAnd2 = new AndOperator(finalLeftOr, finalRightOr);
					andOp.And(equivAnd2);
					//System.out.println( "equivAnd2: " + equivAnd2.toString());			
				}
				if(f.getClass().equals(EVOperator.class))
				{
					BooleanPredicate boolPred0 = new BooleanPredicate(variableList.getString("X", s, null, f, -1));//X s,E[fi V psi]
					BooleanPredicate boolPred1 = new BooleanPredicate(variableList.getString("X", s, null, f, n));//Xn s,E[fi V psi]
					NotOperator notOp0 = new NotOperator(boolPred0);
					NotOperator notOp1 = new NotOperator(boolPred1);
					OrOperator orOp0 = new OrOperator(notOp0, boolPred1);
					OrOperator orOp1 = new OrOperator(notOp1, boolPred0);
					AndOperator andOp0 = new AndOperator(orOp0, orOp1);
					
					//for all E[fi V psi] in sub(n):  X s,E[fi V psi] <--> Xn s,E[fi V psi]
					andOp.And(andOp0);
					//for all E[fi V psi] in sub(n) m in [1..n]: Xm s,E[fi V psi] <--> X s,psi ^ (X s, fi  v  ^t:s->t( E s,t -> Xm-1 t,E[fi V psi]))))  
					EVOperator ev = (EVOperator)f;
					boolPred1 = new BooleanPredicate(variableList.getString("X", s, null, ev.getRightChild(), -1));//X s,psi
					BooleanPredicate boolPred2 = new BooleanPredicate(variableList.getString("X", s, null, ev.getLeftChild(), -1));//X s,fi
					Collection<Transition> stateTrans = s.getTransitions();
					
					
					for(int i=1; i<=n; i++)
					{
						OrOperator stateTransOrs = new OrOperator(null, null);
						boolPred0 = new BooleanPredicate(variableList.getString("X", s, null, f, i));//Xm s,E[fi V psi]
						
						for (Transition t : stateTrans) {
							BooleanPredicate boolPred3 = new BooleanPredicate(variableList.getString("E", s, t.getEndState(), null, -1));//E s,t
							BooleanPredicate boolPred4 = new BooleanPredicate(variableList.getString("X", null, t.getEndState(), ev, i-1));//Xm-1 t,E[fi V psi]
							AndOperator insideAnd = new AndOperator(boolPred3, boolPred4);
							stateTransOrs.Or(insideAnd);
						}
						
						OrOperator rightOr = new OrOperator(boolPred2, stateTransOrs.CheckForNull());
						AndOperator rightAnd;
						if(ev.getLeftChild().toString().toLowerCase().equals("false"))
						{
							rightAnd = new AndOperator(boolPred1, stateTransOrs.CheckForNull());
						}
						else
							rightAnd = new AndOperator(boolPred1, rightOr.CheckForNull());
						OrOperator finalLeftOr = new OrOperator(new NotOperator(boolPred0), rightAnd);
						OrOperator finalRightOr = new OrOperator(new NotOperator(rightAnd), boolPred0);
						AndOperator equivAnd = new AndOperator(finalLeftOr, finalRightOr);
						andOp.And(equivAnd);
					}
					//for all E[fi V psi] in sub(n) X0 s,E[fi V psi] <--> X s,psi 
					BooleanPredicate boolPred5 = new BooleanPredicate(variableList.getString("X", s, null, f, 0));//X0 s,E[fi V psi]
					OrOperator finalLeftOr = new OrOperator(new NotOperator(boolPred1), boolPred5);
					OrOperator finalRightOr = new OrOperator(new NotOperator(boolPred5), boolPred1);
					AndOperator equivAnd2 = new AndOperator(finalLeftOr, finalRightOr);
					andOp.And(equivAnd2);
				}
			}
		}
		
		return andOp.CheckForNull();
	}

	
}
