package eshmun.modelrepair.debug.notcnf;

import java.util.ArrayList;
import java.util.HashMap;

import eshmun.expression.AbstractExpression;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.AWModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.binary.EWModality;
import eshmun.expression.modalities.unary.AFModality;
import eshmun.expression.modalities.unary.AGModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EFModality;
import eshmun.expression.modalities.unary.EGModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.ImplicationOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.visitors.AbstractStateSpecsVisitor;
import eshmun.modelrepair.KripkeFormulaGenerator;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;

/**
 * This class traverses the structure and specifications in a technique similar to 
 * model checking, however the goal of this class is to generate the next-time, and 
 * the release formula as discussed in the paper.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */

public class KripkeNextReleaseGeneratorNotCNF extends AbstractStateSpecsVisitor<Boolean, AbstractState> {
	/**
	 * Counts the number of iteration ModelChecking has been doing.
	 */
	protected int counter;
	
	/**
	 * Stores the total number of states in the structure.
	 */
	private int numberOfStates;
	
	/**
	 * Stores the KripkeFormulaGenerator this is running under.
	 */
	private KripkeFormulaGenerator generator;
	
	/**
	 * Maps names to states.
	 */
	protected final HashMap<String, AbstractState> states;
	
	/**
	 * List of Start states.
	 */
	protected final ArrayList<AbstractState> startStates;
	
	/**
	 * Stores the generated formula for the next-time and release 
	 * modalities as well as Propositional consistency.
	 */
	private AndOperator nextReleaseFormula;
	
	/**
	 * Create new Generator for the next-time and release modalities 
	 * as well as Propositional consistency.
	 * @param generator the KripkeFormulaGenerator this is running under.
	 * @param startStates a list of start states.
	 * @param states a mapping of names to states.
	 */
	public KripkeNextReleaseGeneratorNotCNF(KripkeFormulaGenerator generator, ArrayList<AbstractState> startStates, HashMap<String, AbstractState> states) {
		this.generator = generator;
		this.startStates = startStates;
		this.states = states;
		
		this.numberOfStates = states.values().size();
		
		nextReleaseFormula = new AndOperator();
	}
	
	/**
	 * Returns the Formula related to the Propositional consistency, 
	 * next time, and release modalities. 
	 * @return the generated formula.
	 */
	public AndOperator getNextReleaseFormula() {
		return nextReleaseFormula;
	}
	
	/**
	 * Starts the formula generation.
	 * 
	 * @param specifications the specifications.
	 * @return true if everything is valid, false if the specifications was not in CNF format.
	 */
	@Override
	public Boolean run(AbstractExpression specifications) {
		visited = new HashMap<String, HashMap<AbstractExpression, Integer>>();
		for(String s : states.keySet()) {
			visited.put(s, new HashMap<AbstractExpression, Integer>());
		}
		
		for(AbstractState startState : startStates) {
			counter = numberOfStates + 1;
			if(!visit(specifications, startState)) {				
				return false;	
			}
		}
		
		return true;
	}

	/**
	 * Visits a BooleanVariable, Checks if the state has a label that satisfies it.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(BooleanVariable v, AbstractState state) {
		return true;
	}

	/**
	 * Visits a Boolean Literal, returns its value.
	 * @return true if v has value true, false otherwise.
	 */
	@Override
	public Boolean visit(BooleanLiteral v, AbstractState state) {
		return true;
	}

	/**
	 * Visits a NotOperator.
	 * @return true if it was valid CNF, false otherwise.
	 */
	@Override
	public Boolean visit(NotOperator v, AbstractState state) {
		BooleanVariable labelVar = generator.loadVariable(state.getName(), v.getChild());
		BooleanVariable notLabelVar = generator.loadVariable(state.getName(), v);
				
		EquivalenceOperator equi = new EquivalenceOperator(notLabelVar, labelVar.negate());
		nextReleaseFormula.and(equi);
		return visit(v.getChild(), state);
	}

	/**
	 * Visits an AndOperator.
	 * @return true if it was valid CNF, false otherwise.
	 */
	@Override
	public Boolean visit(AndOperator v, AbstractState state) {
		AndOperator andOp = new AndOperator();
		
		for(AbstractExpression child : v.getChildren()) {
			andOp.and(generator.loadVariable(state.getName(), child));
			visit(child, state);
		}
		
		BooleanVariable var = generator.loadVariable(state.getName(), v);
		EquivalenceOperator equiv = new EquivalenceOperator(var, andOp);
		nextReleaseFormula.and(equiv);
		
		return true;
	}

	/**
	 * Visits an OrOperator.
	 * @return true if it was valid CNF, false otherwise.
	 */
	@Override
	public Boolean visit(OrOperator v, AbstractState state) {
		OrOperator orOp = new OrOperator();
		
		for(AbstractExpression child : v.getChildren()) {
			orOp.or(generator.loadVariable(state.getName(), child));
			visit(child, state);
		}
		
		BooleanVariable var = generator.loadVariable(state.getName(), v);
		EquivalenceOperator equiv = new EquivalenceOperator(var, orOp);
		nextReleaseFormula.and(equiv);
		
		return true;
	}

	/**
	 * Visits an ImplicationOperator, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(ImplicationOperator v, AbstractState state) {
		return false;			
	}

	/**
	 * Visits an EquivalenceOperator, all its children must have equal value.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(EquivalenceOperator v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an AFModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(AFModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an AGModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(AGModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an AXModality.
	 * @return true if the child of v is satisfied in all of 
	 * the state's children, false otherwise.
	 */
	@Override
	public Boolean visit(AXModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an EFModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(EFModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an EGModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(EGModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an EXModality.
	 * @return true if the child of v is satisfied in at least one
	 * of the state's children, false otherwise.
	 */
	@Override
	public Boolean visit(EXModality v, AbstractState state) {
		OrOperator orOp = new OrOperator();
		
		for(AbstractTransition child : state.getOutTransition()) {
			BooleanVariable t = generator.getStateOrTransitionVariable(child.getVarName());
			BooleanVariable x = generator.loadVariable(child.getTo().getName(), v.getChild());

			AndOperator andOp = new AndOperator(t, x);
			orOp.or(andOp);
			
			if(!visit(v.getChild(), child.getTo()))
				return false;
		}
		
		EquivalenceOperator equiv = new EquivalenceOperator(generator.loadVariable(state.getName(), v), orOp);
		nextReleaseFormula.and(equiv);
		
		return true;
	}

	/**
	 * Visits an AUModality.
	 * 
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(AUModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an AWModality.
	 * 
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(AWModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an EUModality.
	 * 
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(EUModality v, AbstractState state) {
		return false;
	}

	/**
	 * Visits an EWModality.
	 * 
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(EWModality v, AbstractState state) {
		return false;
	}
	
	private HashMap<String, HashMap<AbstractExpression, Integer>> visited;
	/**
	 * Visits an AVModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(AVModality v, AbstractState state) {
		HashMap<AbstractExpression, Integer> vs = visited.get(state.getName());
		Integer m_prime = vs.get(v);
		if(m_prime != null && m_prime >= counter)
			return true;
		
		AbstractExpression left = v.getLeftChild();
		AbstractExpression right = v.getRightChild();
		
		EquivalenceOperator equi;
		if(counter == numberOfStates + 1) {
			BooleanVariable av = generator.loadVariable(state.getName(), v);
			BooleanVariable av_n = generator.loadVariable(state.getName(), v, numberOfStates);
			
			equi = new EquivalenceOperator(av, av_n);
			nextReleaseFormula.and(equi);

			counter--;
			if(!visit(v, state))
				return false;
			counter++;
			
			vs = visited.get(state.getName());
			m_prime = vs.get(v);
			if(m_prime == null || (m_prime != null && m_prime < counter))
				vs.put(v, counter);
			
		} else if(counter > 0) {
			BooleanVariable thisVar = generator.loadVariable(state.getName(), v, counter);
			
			BooleanVariable x_left = generator.loadVariable(state.getName(), left);
			BooleanVariable x_right = generator.loadVariable(state.getName(), right);
			
			int tmp = counter;
			counter = numberOfStates + 1;
			if(!visit(v.getLeftChild(), state))
				return false;
			
			counter = numberOfStates + 1;
			if(!visit(v.getRightChild(), state))
				return false;
			
			counter = tmp;
			
			AndOperator children = new AndOperator();
			for(AbstractTransition t : state.getOutTransition()) {
				BooleanVariable e = generator.getStateOrTransitionVariable(t.getVarName());
				BooleanVariable child = generator.loadVariable(t.getTo().getName(), v, counter-1);
								
				children.and(new ImplicationOperator(e, child));
				
				counter--;
				if(!visit(v, t.getTo()))
					return false;
				counter++;
			}
						
			OrOperator orOp = new OrOperator(x_left, children);
			AndOperator andOp = new AndOperator(x_right, orOp);
			
			equi = new EquivalenceOperator(thisVar, andOp);
			nextReleaseFormula.and(equi);
			
			vs = visited.get(state.getName());
			m_prime = vs.get(v);
			if(m_prime == null || (m_prime != null && m_prime < counter))
				vs.put(v, counter);
		} else {
			BooleanVariable x_right = generator.loadVariable(state.getName(), right);
			
			equi = new EquivalenceOperator(generator.loadVariable(state.getName(), v, 0), x_right);
			nextReleaseFormula.and(equi);
			
			int tmp = counter;
			if(!visit(v.getRightChild(), state))
				return false;
			counter = tmp;
			
			vs = visited.get(state.getName());
			m_prime = vs.get(v);
			if(m_prime == null || (m_prime != null && m_prime < counter))
				vs.put(v, counter);
		}
		
		return true;
	}
	
	/**
	 * Visits an EVModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public Boolean visit(EVModality v, AbstractState state) {
		HashMap<AbstractExpression, Integer> vs = visited.get(state.getName());
		Integer m_prime = vs.get(v);
		if(m_prime != null && m_prime >= counter)
			return true;
		
		AbstractExpression left = v.getLeftChild();
		AbstractExpression right = v.getRightChild();
		
		EquivalenceOperator equi;
		if(counter == numberOfStates + 1) {
			BooleanVariable av = generator.loadVariable(state.getName(), v);
			BooleanVariable av_n = generator.loadVariable(state.getName(), v, numberOfStates);
			
			equi = new EquivalenceOperator(av, av_n);
			nextReleaseFormula.and(equi);

			counter--;
			if(!visit(v, state))
				return false;
			counter++;
			
			vs = visited.get(state.getName());
			m_prime = vs.get(v);
			if(m_prime == null || (m_prime != null && m_prime < counter))
				vs.put(v, counter);
			
		} else if(counter > 0) {
			BooleanVariable thisVar = generator.loadVariable(state.getName(), v, counter);
			BooleanVariable x_left = generator.loadVariable(state.getName(), left);
			BooleanVariable x_right = generator.loadVariable(state.getName(), right);
			
			int tmp = counter;
			counter = numberOfStates + 1;
			if(!visit(v.getLeftChild(), state))
				return false;
			
			counter = numberOfStates + 1;
			if(!visit(v.getRightChild(), state))
				return false;
			
			counter = tmp;
			
			OrOperator children = new OrOperator();
			for(AbstractTransition t : state.getOutTransition()) {
				BooleanVariable e = generator.getStateOrTransitionVariable(t.getVarName());
				BooleanVariable child = generator.loadVariable(t.getTo().getName(), v, counter-1);
				children.or(new AndOperator(e, child));
				
				counter--;
				if(!visit(v, t.getTo()))
					return false;
				counter++;
			}
						
			children.or(x_left);
			
			AndOperator andOp = new AndOperator(x_right, children);
			
			equi = new EquivalenceOperator(thisVar, andOp);
			nextReleaseFormula.and(equi);
			
			vs = visited.get(state.getName());
			m_prime = vs.get(v);
			if(m_prime == null || (m_prime != null && m_prime < counter))
				vs.put(v, counter);
		} else {
			BooleanVariable x_right = generator.loadVariable(state.getName(), right);
			
			equi = new EquivalenceOperator(generator.loadVariable(state.getName(), v, 0), x_right);
			nextReleaseFormula.and(equi);
			
			int tmp = counter;
			if(!visit(v.getRightChild(), state))
				return false;
			counter = tmp;
			
			vs = visited.get(state.getName());
			m_prime = vs.get(v);
			if(m_prime == null || (m_prime != null && m_prime < counter))
				vs.put(v, counter);
		}
		
		return true;
	}
}
