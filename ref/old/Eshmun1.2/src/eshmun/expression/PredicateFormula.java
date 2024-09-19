package eshmun.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eshmun.expression.atomic.bool.BooleanPredicate;
import eshmun.expression.atomic.bool.BooleanVariable;
import eshmun.expression.ctl.*;
import eshmun.expression.propoperator.AndOperator;
import eshmun.expression.propoperator.EquivalentOperator;
import eshmun.expression.propoperator.ImpliesOperator;
import eshmun.expression.propoperator.NotOperator;
import eshmun.expression.propoperator.OrOperator;
import eshmun.expression.propoperator.PropOperator;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;
import eshmun.lts.kripke.KripkeState;
import eshmun.modelchecker.AbstractModelChecker;
import eshmun.modelrepairer.FormulaStringCollection;
import eshmun.regex.IndexReplacementContext;

/**
 * Class that describes an abstract predicate formula. A predicate formula
 * can either be an AtomicPredicate, CTLParsedTree, MPCTLParseTree, or PropositionalOperator
 * the BNF for parsing each type is:
 *
 *  ctl_state_formula  
 * 		: ctl_state_sub_formula (PROPOSITIONAL_CONNECTIVE ctl_state_sub_formula)*
 * 
 * 	mpctl_formula 
 * 		: MPCTL_CONNECTIVE LEFT_BRACKET ctl_state_formula RIGHT_BRACKET;
 *
 *	ctl_state_sub_formula
 * 		: ctl_neg_sub_formula
 * 		| CTL_BRANCH_OP LEFT_BRACKET ctl_path_formula RIGHT_BRACKET
 * 		| atomic_formula;
 *
 *
 *	ctl_neg_sub_formula 
 * 		: (NOT_CONNECTIVE)? LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS;
 *  
 *  ctl_path_formula 
 * 		: CTL_PATH_OP LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS 
 * 		| MPCTL_PATH_OP LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS 
 * 		| LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS CTL_PATH_CONNECTIVE LEFT_PARANTHESIS ctl_state_formula RIGHT_PARANTHESIS
 * 
 * 	atomic_formula
 * 		: var_expression
 * 		| BOOLEAN;
 * 
 * 	var_expression
 * 		: VAR COMPARISON_OPERATOR VAR 
 * 		| VAR COMPARISON_OPERATOR CONSTANT 
 * 		| VAR COMPARISON_OPERATOR STRING
 *		| VAR;  
 *
 *	PROPOSITIONAL_CONNECTIVE : '|' | '&' | '=>' | '<=>'; 
 * 	NOT_CONNECTIVE : '!';
 * 	COMPARISON_OPERATOR : '<' | '<=' | '>' | '>=' | '==' | '!=';
 *	NEGATIVE_SIGN : '-';
 *	CONSTANT : (NEGATIVE_SIGN)? (DIGIT)+ ;
 * 	LEFT_PARANTHESIS : '(';
 *	RIGHT_PARANTHESIS : ')';
 *	LEFT_BRACKET : '[';
 *	RIGHT_BRACKET : ']';
 *	CTL_BRANCH_OP : 'A' | 'E';
 *	CTL_PATH_CONNECTIVE : 'U' | 'V' |'W' ;
 * 	CTL_PATH_OP : 'X' | 'F' | 'G';
 * 	MPCTL_CONNECTIVE: 'AND' (SMALL_LETTER)+ | 'OR' (SMALL_LETTER)+;
 *	MPCTL_PATH_OP: CTL_PATH_OP  (SMALL_LETTER)+;
 * 	BOOLEAN : 'true' | 'false';
 *	STRING : '\'' (LETTER|DIGIT)*  '\'';
 *	VAR : (LETTER|DIGIT|'_'|'.')+;
 * 
 * @author Emile
 *
 */
public abstract class PredicateFormula implements Comparable<PredicateFormula>{
	
	protected String toStringValue;
	private List<String> variableList;
	
	
	public PredicateFormula() {
		this.toStringValue = "";
		variableList = new ArrayList<String>();
	}
	
	/**
	 * returns a list of sub formulae of this Predicate formulae. The list
	 * is ordered by the sub formulae inclusion order
	 * @return a list containing all subformulea ordered in the subformulae 
	 * inclusion order.
	 */
	public abstract List<PredicateFormula> getSubFormulea(IExpressionVisitor expressionVisitor);
	
	/**
	 * get SubFormula according to Preliminaries Definition 1 in paper "Model and Program Repair via SAT Solving"
	 */
	public abstract List<PredicateFormula> getSubFormulea();
	
	/**
	 * convert to CTL as in Preliminaries ctl definition in paper "Model and Program Repair via SAT Solving"
	 */
	
	/**
	 * get all boolean predicates
	 * @return
	 */
	public List<BooleanPredicate> getBooleanPredicates()
	{
		List<BooleanPredicate> labels = new ArrayList<BooleanPredicate>();
		List<PredicateFormula> subFormulas = getSubFormulea();
		for (PredicateFormula pred : subFormulas) {
			if(pred.getClass().equals(BooleanPredicate.class))
					labels.add((BooleanPredicate)pred);
		}
		return labels;
	}
	
	
	public abstract PredicateFormula ConvertToCTL(); 
	
	/**
	 * used to replace parameters with actual values, this is used during the 
	 * MPCTL formula composition
	 * @param indexReplacementContext contains the context for replacing parameters
	 * @return the Predicate Formula with replaced parameters
	 */
	public abstract PredicateFormula replaceParameters(IndexReplacementContext indexReplacementContext);
	
	/**
	 * checks if the propositional formula represented by this Predicate Formulae
	 * is in CNF (Conjunctive Normal form). 
	 * a Predicate Formulae is in CNF when:
	 * 	it is a conjunction (sequence of ANDs) consisting of one or more conjuncts, 
	 * 	each of which is a disjunction (OR) of one or more literals. 
	 * 	Examples of conjunctive normal forms include 
	 *		(1)  N1
 	 *		(2)  (N1|T1) AND (N2|T2)
 	 *		(3)  N1|T1
 	 *		(4)  (N1|T1) AND N2
	 *
	 * @return
	 */
	public abstract boolean isCNF();
	
//	private static boolean isLiteral(PredicateFormula formula)
//	{
//		if(formula.getClass().equals(BooleanPredicate.class))
//			return true;
//		else
//			{
//				if(formula.getClass().equals(NotOperator.class) && ((NotOperator)formula).getChild().getClass().equals(BooleanPredicate.class))
//					return true;
//				else
//					return false;
//			}
//	}
	
	public  boolean isLiteral()
	{
		if(this.getClass().equals(BooleanPredicate.class))
			return true;
		else if(this.getClass().equals(NotOperator.class) && ((NotOperator)this).getChild().getClass().equals(BooleanPredicate.class))
			return true;
		return false;
			
	}
	
	private boolean doesVariableExist(String variable)
	{
		
		if(variableList.contains(variable))
			return true;
		else
		{
			variableList.add(variable);
		return false;
		}
	}
	public List<PredicateFormula> GetNotCTLSubFormulae(List<PredicateFormula> subs, PredicateFormula formula, boolean pairs)
	{
		PredicateFormula frml = formula.ConvertToCTL();
		//System.out.println(frml.toString());
		GetNotCTL(subs, frml, pairs);
		return subs;
	}
	
	 
	public static List<PredicateFormula> GetAbstractionSubs(PredicateFormula formula, boolean withNot)
	{
		List<PredicateFormula> formulas = new ArrayList<PredicateFormula>();
		if(formula instanceof ImpliesOperator)
		{
			NotOperator notChild = new NotOperator(((ImpliesOperator) formula).getLeftChild().ConvertToCTL());
			OrOperator rootOperator = new OrOperator(notChild, ((ImpliesOperator) formula).getRightChild().ConvertToCTL());
			formulas.addAll(GetAbstractionSubs(rootOperator, withNot));
		}
		if(formula instanceof EquivalentOperator)
		{
			ImpliesOperator newLeftChild = new ImpliesOperator(((EquivalentOperator) formula).getLeftChild().ConvertToCTL(), ((EquivalentOperator) formula).getRightChild().ConvertToCTL());
			ImpliesOperator newRightChild = new ImpliesOperator(((EquivalentOperator) formula).getRightChild().ConvertToCTL(), ((EquivalentOperator) formula).getLeftChild().ConvertToCTL());
			AndOperator rootOperator = new AndOperator(newLeftChild, newRightChild);
			formulas.addAll(GetAbstractionSubs(rootOperator, withNot));
		}
		if(formula instanceof PropOperator || formula instanceof BooleanPredicate)
		{
			if(formula.containsCTLType())
			{
				if(formula instanceof NotOperator)
				{
					formulas.addAll(GetAbstractionSubs(((NotOperator) formula).getChild(), withNot));
					return formulas;
				}
				else if (formula instanceof AndOperator) 
				{
					PredicateFormula left = ((AndOperator) formula).getLeftChild();
					PredicateFormula right = ((AndOperator) formula).getRightChild();
					if(left.containsCTLType())
					{
						formulas.addAll(GetAbstractionSubs(left, withNot));
					}
					else
					{
						formulas.add(left);
						
					}
					if(right.containsCTLType())
					{
						formulas.addAll(GetAbstractionSubs(right, withNot));
					}
					else
					{
						formulas.add(right);
					}
					return formulas;
				}
				else if (formula instanceof OrOperator) 
				{
					PredicateFormula left = ((OrOperator) formula).getLeftChild();
					PredicateFormula right = ((OrOperator) formula).getRightChild();
					if(left.containsCTLType())
					{
						formulas.addAll(GetAbstractionSubs(left, withNot));
					}
					else
					{
						formulas.add(left);
						
					}
					if(right.containsCTLType())
					{
						formulas.addAll(GetAbstractionSubs(right, withNot));
					}
					else
					{
						formulas.add(right);
					}
				}
			}
			else				
			{
				if(!withNot && formula instanceof NotOperator && !(((NotOperator) formula).getChild() instanceof BooleanPredicate))
				{
					formulas.add(((NotOperator) formula).getChild());
				}
				else
				{
					formulas.add(formula);
				}
			}
		}
		else if(formula instanceof SingleCTLOperator)
		{
			formulas.addAll(GetAbstractionSubs(((SingleCTLOperator) formula).getChild(), withNot));
			return formulas;
		}
		else if(formula instanceof BinaryCTLOperator)
		{
			formulas.addAll(GetAbstractionSubs(((BinaryCTLOperator) formula).getLeftChild(), withNot));
			formulas.addAll(GetAbstractionSubs(((BinaryCTLOperator) formula).getRightChild(), withNot));
			return formulas;
		}
		return formulas;
	}
	/*
	 * check if a state satisfy a 
	 */
	public boolean isSatisfiedBy(KripkeState state)
	{
		PredicateFormula formula = this;
		List<BooleanPredicate> stateLabels = state.getLabels();
		if(formula instanceof BooleanPredicate)
		{
			BooleanPredicate bp = (BooleanPredicate) formula;
			if(stateLabels.contains(bp))
				return true;
			return false;
		}
		if(formula instanceof NotOperator)
		{
			NotOperator not = (NotOperator) formula;
			return !(not.getChild().isSatisfiedBy(state));
		}
		if(formula instanceof OrOperator)
		{
			OrOperator or = (OrOperator) formula;
			return (or.getLeftChild().isSatisfiedBy(state) || or.getRightChild().isSatisfiedBy(state));
		}
		if(formula instanceof AndOperator)
		{
			AndOperator and = (AndOperator) formula;
			return (and.getLeftChild().isSatisfiedBy(state) && and.getRightChild().isSatisfiedBy(state));
		}
		return false;
	}
	
	public void GetNotCTL(List<PredicateFormula> subs, PredicateFormula formula, boolean pairs)
	{
		
		
			if(formula.getClass().equals(BooleanPredicate.class))
			{
				subs.add(formula);
				//return subs;
			}			
			//NOT////NOT////NOT////NOT////NOT//
			else if(formula.getClass().equals(NotOperator.class))
			{
				NotOperator not = (NotOperator)formula;
				PredicateFormula child = not.getChild();
				if(child.containsCTLType())
				{
					//subs.addAll(GetNotCTL(subs, child));
					//return subs;
					GetNotCTL(subs, child, pairs);
				}
				else
				{
					if(pairs)
						GetPureAndOR(subs, not);
					else
						subs.add(formula);
					//return subs;
				}
			}
			//OR////OR///OR////OR////OR////OR////OR
			else if(formula.getClass().equals(OrOperator.class))
			{
				OrOperator or = (OrOperator)formula;
				PredicateFormula left = or.getLeftChild();
				PredicateFormula right = or.getRightChild();
				boolean leftCTL = left.containsCTLType();
				boolean rightCTL =  right.containsCTLType();
				if(leftCTL && rightCTL)
				{
//					subs.addAll(GetNotCTL(subs, right));
//					subs.addAll(GetNotCTL(subs, left));
//					return subs;
					GetNotCTL(subs, right, pairs);
					GetNotCTL(subs, left, pairs);
				}
				else if(leftCTL)
				{
					//subs.addAll(GetNotCTL(subs, left));
					subs.add(right);
					GetNotCTL(subs, left, pairs);
					//return subs;
				}
				else if(rightCTL)
				{
					//subs.addAll(GetNotCTL(subs, right));
					subs.add(left);
					//return subs;
					GetNotCTL(subs, right, pairs);
				}
				else
				{
					if(pairs) 
						GetPureAndOR(subs, formula);
					else
						subs.add(formula);

					//return subs;
				}
			}
			///AND/////AND/////AND/////AND/////AND/////AND//
			else if(formula.getClass().equals(AndOperator.class))
			{
				AndOperator and = (AndOperator)formula;
				PredicateFormula left = and.getLeftChild();
				PredicateFormula right = and.getRightChild();
				boolean leftCTL = left.containsCTLType();
				boolean rightCTL =  right.containsCTLType();
				if(leftCTL && rightCTL)
				{
					//subs.addAll(GetNotCTL(subs, right));
					//subs.addAll(GetNotCTL(subs, left));
					//return subs;
					GetNotCTL(subs, right, pairs);
					GetNotCTL(subs, left, pairs);
				}
				else if(leftCTL)
				{
					//subs.addAll(GetNotCTL(subs, left));
					subs.add(right);
					GetNotCTL(subs, left, pairs);
					//return subs;
				}
				else if(rightCTL)
				{
					//subs.addAll(GetNotCTL(subs, right));
					subs.add(left);
					GetNotCTL(subs, right, pairs);
					//return subs;
				}
				else
				{
					if(pairs) 
						GetPureAndOR(subs, formula);
					else
						subs.add(formula);

					//return subs;
				}
					
			}
			//AV//AV//AV//AV//AV//AV//AV//AV//AV//AV
			else if(formula.getClass().equals(AVOperator.class))
			{
				AVOperator av = (AVOperator)formula;
				//subs.addAll(GetNotCTL(subs, av.getRightChild()));
				//subs.addAll(GetNotCTL(subs, av.getLeftChild()));
				//return subs;
				GetNotCTL(subs, av.getRightChild(), pairs);
				GetNotCTL(subs, av.getLeftChild(), pairs);
			}
			//EV//EV//EV//EV//EV//EV//EV//EV//EV
			else if(formula.getClass().equals(EVOperator.class))
			{
				EVOperator ev = (EVOperator)formula;
				//subs.addAll(GetNotCTL(subs, ev.getRightChild()));
				//subs.addAll(GetNotCTL(subs, ev.getLeftChild()));
				GetNotCTL(subs, ev.getRightChild(), pairs);
				GetNotCTL(subs, ev.getLeftChild(), pairs);
				//return subs;
			}
			//AX//AX//AX//AX//AX//AX//AX//AX//AX//AX
			else if(formula.getClass().equals(AXOperator.class))
			{
				AXOperator ax = (AXOperator)formula;
				//subs.addAll(GetNotCTL(subs, ax.getChild()));
				//return subs;
				GetNotCTL(subs, ax.getChild(), pairs);
			}
			//EX//EX//EX//EX//EX//EX//EX//EX//EX
			else if(formula.getClass().equals(EXOperator.class))
			{
				EXOperator ex = (EXOperator)formula;
				//subs.addAll(GetNotCTL(subs, ex.getChild()));
				//return subs;
				GetNotCTL(subs, ex.getChild(), pairs);
			}
		//return null;
	}
	/*
	 * The below function is an implementation of this algorithm:  http://www.cs.jhu.edu/~jason/tutorials/convert-to-CNF.html
	 * also uses  Switching Variables to Keep the Converted Formula Small
	 *  P v Q is the same as ((Z -> P) ^ (~Z -> Q)) from a sat solver perspective
	 */
	public PredicateFormula ConvertToCNF(PredicateFormula formula, Random randomGenerator, FormulaStringCollection variableList)
	{
		//System.out.println(formula.toStringValue);
		//If phi is a variable, then:
		//return phi.
		// this is a CNF formula consisting of 1 clause that contains 1 literal
		if(formula.getClass().equals(BooleanPredicate.class)) //1:1:1:1
		{			
			return formula;
		}
		//If phi has the form P ^ Q, then:
	    //CONVERT(P) must have the form P1 ^ P2 ^ ... ^ Pm, and
	    //CONVERT(Q) must have the form Q1 ^ Q2 ^ ... ^ Qn,
	    //where all the Pi and Qi are disjunctions of literals.
	    //So return P1 ^ P2 ^ ... ^ Pm ^ Q1 ^ Q2 ^ ... ^ Qn.
		else if(formula.getClass().equals(AndOperator.class))///AND/////AND/////AND/////AND/////AND/////AND// //2:2:2:2
		{
			AndOperator andOp = (AndOperator) formula;			
			return new AndOperator(ConvertToCNF(andOp.getLeftChild(), randomGenerator, variableList), ConvertToCNF(andOp.getRightChild(), randomGenerator, variableList));
		}
		//If phi has the form P v Q, then:
	    //CONVERT(P) must have the form P1 ^ P2 ^ ... ^ Pm, and
	    //CONVERT(Q) must have the form Q1 ^ Q2 ^ ... ^ Qn,
	    //where all the Pi and Qi are dijunctions of literals.
	    //So we need a CNF formula equivalent to
	    //  (P1 ^ P2 ^ ... ^ Pm) v (Q1 ^ Q2 ^ ... ^ Qn).
	    //So return (P1 v Q1) ^ (P1 v Q2) ^ ... ^ (P1 v Qn)
	    //       ^ (P2 v Q1) ^ (P2 v Q2) ^ ... ^ (P2 v Qn)
	    //         ...
	    //       ^ (Pm v Q1) ^ (Pm v Q2) ^ ... ^ (Pm v Qn)
		else if(formula.getClass().equals(OrOperator.class))//OR////OR///OR////OR////OR////OR////OR //3:3:3:3
		{
			OrOperator orOp = (OrOperator) formula;
			if(orOp.getLeftChild().isLiteral() && orOp.getRightChild().isLiteral())
					return formula;
			else if(orOp.getLeftChild().isLiteral())
			{
				PredicateFormula temp = ConvertToCNF(orOp.getRightChild(), randomGenerator, variableList);
				if(temp.getClass().equals(AndOperator.class)){
					AndOperator op1 = (AndOperator)temp;
					return ConvertToCNF(new AndOperator(
							new OrOperator(orOp.getLeftChild(), op1.getLeftChild()),
							new OrOperator(orOp.getLeftChild(), op1.getRightChild())), randomGenerator, variableList);
				}
				else
				{
					//return formula; changed in 01/12/2013
					return new OrOperator(orOp.getLeftChild(), temp);
				}
			}
			else if(orOp.getRightChild().isLiteral())
			{
				PredicateFormula temp = ConvertToCNF(orOp.getLeftChild(), randomGenerator, variableList);
				if(temp.getClass().equals(AndOperator.class)){
					AndOperator op1 = (AndOperator)temp;
					return ConvertToCNF(new AndOperator(
							new OrOperator(orOp.getRightChild(), op1.getLeftChild()),
							new OrOperator(orOp.getRightChild(), op1.getRightChild())), randomGenerator, variableList);
				}
				else	
				{
					//return formula; changed in 01/12/20123
					return new OrOperator(orOp.getRightChild(), temp);
				
				}
			}
			else
			{
				String zVariable = "Z" + randomGenerator.nextInt(100000000);
				while(doesVariableExist(zVariable))
				{
					zVariable = "Z" + randomGenerator.nextInt(1100000000);
				}
				zVariable = variableList.getString(zVariable, null, null, null, -1);
				BooleanPredicate zPredicate = new BooleanPredicate(new BooleanVariable(zVariable));
				NotOperator notZOp = new NotOperator(zPredicate);
				OrOperator notZOrP = new OrOperator(notZOp, orOp.getLeftChild());
				OrOperator zOrQ = new OrOperator(zPredicate, orOp.getRightChild());
				AndOperator andOp = new AndOperator(ConvertToCNF(notZOrP, randomGenerator, variableList), ConvertToCNF(zOrQ, randomGenerator, variableList));
				return andOp;
			}
		}
		//If phi has the form ~(...), then:
	    //If phi has the form ~A for some variable A, then return phi.
	    //If phi has the form ~(~P), then return CONVERT(P).           // double negation
	    //If phi has the form ~(P ^ Q), then return CONVERT(~P v ~Q).  // de Morgan's Law
	    //If phi has the form ~(P v Q), then return CONVERT(~P ^ ~Q).  // de Morgan's Law
		else if(formula.getClass().equals(NotOperator.class))//NOT////NOT////NOT////NOT////NOT// //4:4:4:4:4
		{
			NotOperator notOp = (NotOperator) formula;
			if(notOp.getChild().getClass().equals(BooleanPredicate.class))
				return formula;
			else if(notOp.getChild().getClass().equals(NotOperator.class))
			{
				
				NotOperator notOp2 = (NotOperator) notOp.getChild();
				return ConvertToCNF(notOp2.getChild(), randomGenerator, variableList);
			}
			else if(notOp.getChild().getClass().equals(AndOperator.class))
			{
				AndOperator andOp = (AndOperator) notOp.getChild();
				OrOperator orOp = new OrOperator(new NotOperator(andOp.getLeftChild()), new NotOperator(andOp.getRightChild()));
				return ConvertToCNF(orOp, randomGenerator, variableList);
			}
			else if(notOp.getChild().getClass().equals(OrOperator.class))
			{
				OrOperator orOp = (OrOperator) notOp.getChild();
				AndOperator andOp = new AndOperator(new NotOperator(orOp.getLeftChild()), new NotOperator(orOp.getRightChild()));
				return ConvertToCNF(andOp, randomGenerator, variableList);
			}
		}
		
		
		
		return formula;
	}
	
	
	
	/**
	 * model checks this predicate formula using the passed ModelChecker implementation
	 * @param modelChecker the model checker
	 * @param structure the kripke containing the context state for the the evaluation of variables
	 * @param state the current kripke state for the valuation of variables
	 * @return the success of the model checking operation
	 */
	public abstract boolean modelCheck(AbstractModelChecker modelChecker, Kripke structure, KripkeState state) throws Exception;
	
	/**
	 * prints the resulting formulae whose root is this Predicate Formulae
	 */
	public String toString() {
		return toStringValue;
	}
	

	
	@Override
	public int hashCode() {
		int hash = 1;
		if (toStringValue != null) {
			hash = hash * 31 + toStringValue.hashCode();
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredicateFormula other = (PredicateFormula) obj;
		if (toStringValue == null) {
			if (other.toStringValue != null)
				return false;
		} else if (!toStringValue.equals(other.toStringValue))
			return false;
		
		return true;
	}

	@Override  
    public int compareTo(PredicateFormula o) {  
        if(this.toStringValue.equals(o.toStringValue))
        	return 1;
        return 0;
    }
	
	public boolean containsType(Class c)
	{
		List<PredicateFormula> subs = this.getSubFormulea();
		for (PredicateFormula predicateFormula : subs) {
			if(predicateFormula.getClass().equals(c))
				return true;
		}
		return false;
	}
	public List<PredicateFormula> GetPureAndOR(List<PredicateFormula> subs, PredicateFormula formula)
	{
		if(formula.getClass().equals(BooleanPredicate.class))
		{
			subs.add(formula);
			return subs;
		}
		else if(formula.getClass().equals(NotOperator.class))
		{
			NotOperator not = (NotOperator)formula;
			PredicateFormula child = not.getChild();
			if(child.getClass().equals(BooleanPredicate.class))
			{
				subs.add(formula);
				return subs;
			}
			else
			{
				if(child.getClass().equals(AndOperator.class))
				{
					AndOperator and = (AndOperator)child;
					NotOperator not1= new NotOperator(and.getLeftChild());
					NotOperator not2= new NotOperator(and.getRightChild());
					OrOperator or2 = new OrOperator(not1,not2);
					GetPureAndOR(subs, or2);
					return subs;
				}
				else if(child.getClass().equals(OrOperator.class))
				{
					OrOperator or = (OrOperator)child;
					GetPureAndOR(subs, new AndOperator(new NotOperator(or.getLeftChild()), new NotOperator(or.getRightChild())));
					return subs;
				}
				else if(child.getClass().equals(NotOperator.class))
				{
					NotOperator not2 = (NotOperator)child;
					GetPureAndOR(subs,not2.getChild());
					return subs;
				}
			}
			return null;
		}
		else if(formula.getClass().equals(AndOperator.class))
		{
			AndOperator and = (AndOperator)formula;
			PredicateFormula left = and.getLeftChild();
			PredicateFormula right = and.getRightChild();
			boolean leftAtomic = atomicOrNotAtomic(left);
			boolean rightAtomic = atomicOrNotAtomic(right);
			if(leftAtomic && rightAtomic)
			{
				subs.add(formula);
				return subs;
			}
			else if(leftAtomic)
			{
				//subs.addAll(GetPureAndOR(subs, left));
				GetPureAndOR(subs, right);
				subs.add(left);
				return subs;
			}
			else if(rightAtomic)
			{
				//subs.addAll(GetPureAndOR(subs, right));
				GetPureAndOR(subs, left);
				subs.add(right);
				return subs;
			}
			else
			{
				//subs.addAll(GetPureAndOR(subs, left));
				//subs.addAll(GetPureAndOR(subs, left));
				GetPureAndOR(subs, right);
				GetPureAndOR(subs, left);
				return subs;
			}
		}
		else if(formula.getClass().equals(OrOperator.class))
		{
			OrOperator or = (OrOperator)formula;
			PredicateFormula left = or.getLeftChild();
			PredicateFormula right = or.getRightChild();
			boolean leftAtomic = atomicOrNotAtomic(left);
			boolean rightAtomic = atomicOrNotAtomic(right);
			if(leftAtomic && rightAtomic)
			{
				subs.add(formula);
				return subs;
			}
			else if(leftAtomic)
			{
				GetPureAndOR(subs, right);
				subs.add(left);
				return subs;
			}
			else if(rightAtomic)
			{
				GetPureAndOR(subs,left);
				subs.add(right);
				return subs;
			}
			else
			{
				GetPureAndOR(subs, right);
				GetPureAndOR(subs, left);
				return subs;
			}
		}
		else
			return null;
	}
	public boolean atomicOrNotAtomic(PredicateFormula frml)
	{
		if(frml.getClass().equals(BooleanPredicate.class))
			return true;
		else if(frml.getClass().equals(NotOperator.class))
		{
			NotOperator not = (NotOperator)frml;
			PredicateFormula child = not.getChild();
			if(child.getClass().equals(BooleanPredicate.class))
				return true;
			return false;
		}
		return false;
	}
	public boolean containsCTLType()
	{
		if(this instanceof SingleCTLOperator || this instanceof BinaryCTLOperator)
			return true;
		List<PredicateFormula> subs = PredicateFormula.getStrtForwardSubformulae(this);
		for (PredicateFormula frml : subs) {
			if(frml instanceof SingleCTLOperator || frml instanceof BinaryCTLOperator)
				return true;
		}
		return false;
	}
	public static List<PredicateFormula> getStrtForwardSubformulae(PredicateFormula formula)
	{
		List<PredicateFormula> formulas = new ArrayList<PredicateFormula>();
		if(formula instanceof BooleanPredicate)
		{
			formulas.add(formula);
			return formulas;
		}
		if(formula instanceof PropOperator)
		{
			if(formula instanceof NotOperator)
			{
				formulas.add(((NotOperator) formula).getChild());
				formulas.addAll(getStrtForwardSubformulae(((NotOperator) formula).getChild()));
				return formulas;
			}
			else if (formula instanceof AndOperator) 
			{
				formulas.add(((AndOperator) formula).getLeftChild());
				formulas.add(((AndOperator) formula).getRightChild());
				formulas.addAll(getStrtForwardSubformulae(((AndOperator) formula).getLeftChild()));
				formulas.addAll(getStrtForwardSubformulae(((AndOperator) formula).getRightChild()));
				return formulas;
			}
			else if (formula instanceof OrOperator) 
			{
				formulas.add(((OrOperator) formula).getLeftChild());
				formulas.add(((OrOperator) formula).getRightChild());
				formulas.addAll(getStrtForwardSubformulae(((OrOperator) formula).getLeftChild()));
				formulas.addAll(getStrtForwardSubformulae(((OrOperator) formula).getRightChild()));
				return formulas;
			}
		}
		else if(formula instanceof SingleCTLOperator)
		{
			formulas.add(((SingleCTLOperator) formula).getChild());
			formulas.addAll(getStrtForwardSubformulae(((SingleCTLOperator) formula).getChild()));
			return formulas;
		}
		else if(formula instanceof BinaryCTLOperator)
		{
			formulas.add(((BinaryCTLOperator) formula).getLeftChild());
			formulas.add(((BinaryCTLOperator) formula).getRightChild());
			formulas.addAll(getStrtForwardSubformulae(((BinaryCTLOperator) formula).getLeftChild()));
			formulas.addAll(getStrtForwardSubformulae(((BinaryCTLOperator) formula).getRightChild()));
			return formulas;
		}
		return formulas;
	}
	
	public static PredicateFormula SendNegationToInside(PredicateFormula frml)
	{
		if(frml instanceof BooleanPredicate)
			return frml;
		if(frml instanceof NotOperator)
		{
			if(((NotOperator) frml).getChild() instanceof BooleanPredicate)
				return frml;
			return SendNegationToInside(((NotOperator) frml).getChild().getMyNegation());
		}
		if(frml instanceof SingleCTLOperator)
		{
			((SingleCTLOperator) frml).setChild(SendNegationToInside(((SingleCTLOperator) frml).getChild()));
			return frml;
		}
		if(frml instanceof BinaryCTLOperator)
		{
			((BinaryCTLOperator) frml).setChilds(
					SendNegationToInside(((BinaryCTLOperator) frml).getLeftChild()),
					SendNegationToInside(((BinaryCTLOperator) frml).getRightChild()));
			return frml;
		}
		if(frml instanceof AndOperator)
		{
			((AndOperator) frml).setChilds(
					SendNegationToInside(((AndOperator) frml).getLeftChild()),
					SendNegationToInside(((AndOperator) frml).getRightChild()));
			return frml;
		}
		if(frml instanceof OrOperator)
		{
			PredicateFormula f1 = SendNegationToInside(((OrOperator) frml).getLeftChild());
			PredicateFormula f2 = SendNegationToInside(((OrOperator) frml).getRightChild());
			((OrOperator) frml).setChilds(f1,f2);
			return frml;
		}
		if(frml instanceof ImpliesOperator)
		{
			((ImpliesOperator) frml).setChilds(
					SendNegationToInside(((ImpliesOperator) frml).getLeftChild()),
					SendNegationToInside(((ImpliesOperator) frml).getRightChild()));
			return frml;
		}
		
		return null;
	}
	
	public PredicateFormula getMyNegation()
	{
		if(this instanceof NotOperator)
		{
			return ((NotOperator)this).getChild();
		}
		
		if(this instanceof AndOperator)
		{
			NotOperator notLeftChild = new NotOperator(((AndOperator)this).getLeftChild());
			NotOperator notRightChild = new NotOperator(((AndOperator)this).getRightChild());
			OrOperator or = new OrOperator(notLeftChild, notRightChild);
			return or;
		}
		
		if(this instanceof OrOperator)
		{
			NotOperator notLeftChild = new NotOperator(((OrOperator)this).getLeftChild());
			NotOperator notRightChild = new NotOperator(((OrOperator)this).getRightChild());
            AndOperator and = new AndOperator(notLeftChild, notRightChild);
			return and;
		}
		
		if(this instanceof ImpliesOperator)
		{
			
			NotOperator notLeftChild = new NotOperator(((ImpliesOperator)this).getLeftChild());
			OrOperator or = new OrOperator(notLeftChild, ((ImpliesOperator)this).getRightChild());
			return or.getMyNegation();
		}
		
		
		//\neg EF\phi \equiv AG\neg\phi
		if(this instanceof EFOperator)
		{
			PredicateFormula myChild = ((EFOperator)this).getChild();
			NotOperator notMyChild = new NotOperator(myChild); 
			AGOperator ag = new AGOperator(notMyChild);
			return ag;
		}
		//\neg AF\phi \equiv EG\neg\phi
		if(this instanceof AFOperator)
		{
			PredicateFormula myChild = ((AFOperator)this).getChild();
			NotOperator notMyChild = new NotOperator(myChild);
			EGOperator eg = new EGOperator(notMyChild);
			return eg;
		}
		//\neg AG\phi \equiv EF\neg\phi
		if(this instanceof AGOperator)
		{
			PredicateFormula myChild = ((AGOperator)this).getChild();
			NotOperator notMyChild = new NotOperator(myChild);
			EFOperator ef = new EFOperator(notMyChild);
			return ef;				
		}
		//\neg EG\phi \equiv AF\neg\phi
		if(this instanceof EGOperator)
		{
			PredicateFormula myChild = ((AGOperator)this).getChild();
			NotOperator notMyChild = new NotOperator(myChild);
			AFOperator af = new AFOperator(notMyChild);
			return af;	
		}
		//\neg AX\phi \equiv EX\neg\phi
		if(this instanceof AXOperator)
		{
			PredicateFormula myChild = ((AXOperator)this).getChild();
			NotOperator notMyChild = new NotOperator(myChild);
			EXOperator ex = new EXOperator(notMyChild);
			return ex;
		}
		//\neg EX\phi \equiv AX\neg\phi
		if(this instanceof EXOperator)
		{
			PredicateFormula myChild = ((EXOperator)this).getChild();
			NotOperator notMyChild = new NotOperator(myChild);
			AXOperator ax = new AXOperator(notMyChild);
			return ax;
		}
		if(this instanceof AVOperator)
		{
			NotOperator notLeftChild = new NotOperator(((AVOperator)this).getLeftChild());
			NotOperator notRightChild = new NotOperator(((AVOperator)this).getRightChild());
			EUOperator eu = new EUOperator(notLeftChild, notRightChild);
			return eu;
		}
		
		if(this instanceof AUOperator)
		{
			NotOperator notLeftChild = new NotOperator(((AUOperator)this).getLeftChild());
			NotOperator notRightChild = new NotOperator(((AUOperator)this).getRightChild());
			EVOperator ev = new EVOperator(notLeftChild, notRightChild);
			return ev;
		}
		if(this instanceof EVOperator)
		{
			NotOperator notLeftChild = new NotOperator(((EVOperator)this).getLeftChild());
			NotOperator notRightChild = new NotOperator(((EVOperator)this).getRightChild());
			AUOperator au = new AUOperator(notLeftChild, notRightChild);
			return au;
		}
		if(this instanceof EUOperator)
		{
			NotOperator notLeftChild = new NotOperator(((EUOperator)this).getLeftChild());
			NotOperator notRightChild = new NotOperator(((EUOperator)this).getRightChild());
			AVOperator av = new AVOperator(notLeftChild, notRightChild);
			return av;
		}
		
		return null;
	}
	
	public boolean isMyNegation(PredicateFormula f)
	{
		NotOperator notMe = new NotOperator(this);
		if(notMe.equals(f))
			return true;
		if(this instanceof EFOperator)
		{
			if(f instanceof AGOperator)
			{
				PredicateFormula myChild = ((EFOperator)this).getChild();
				PredicateFormula fChild = ((AGOperator)f).getChild();
				return myChild.isMyNegation(fChild);
			}
				
		}
		
		if(this instanceof AFOperator)
		{
			if(f instanceof EGOperator)
			{
				PredicateFormula myChild = ((AFOperator)this).getChild();
				PredicateFormula fChild = ((EGOperator)f).getChild();
				return myChild.isMyNegation(fChild);
			}
		}
		
		if(this instanceof AGOperator)
		{
			if(f instanceof EFOperator)
			{
				PredicateFormula myChild = ((AGOperator)this).getChild();
				PredicateFormula fChild = ((EFOperator)f).getChild();
				return myChild.isMyNegation(fChild);
			}
				
		}
		
		if(this instanceof EGOperator)
		{
			if(f instanceof AFOperator)
			{
				PredicateFormula myChild = ((EGOperator)this).getChild();
				PredicateFormula fChild = ((AFOperator)f).getChild();
				return myChild.isMyNegation(fChild);
			}
		}
		
		if(this instanceof AXOperator)
		{
			if(f instanceof EXOperator)
			{
				PredicateFormula myChild = ((AXOperator)this).getChild();
				PredicateFormula fChild = ((EXOperator)f).getChild();
				return myChild.isMyNegation(fChild);
			}
		}
		if(this instanceof EXOperator)
		{
			if(f instanceof AXOperator)
			{
				PredicateFormula myChild = ((EXOperator)this).getChild();
				PredicateFormula fChild = ((AXOperator)f).getChild();
				return myChild.isMyNegation(fChild);
			}
		}
		
		
		return false;
	}
}
