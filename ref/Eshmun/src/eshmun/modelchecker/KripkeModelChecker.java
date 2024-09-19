package eshmun.modelchecker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import eshmun.structures.kripke.KripkeState;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.kripke.KripkeTransition;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;

/**
 * The class represents a Model Checker, it uses the visitor pattern.
 * 
 * @author Ali Cherri, Kinan Dak Al Bab
 * @since 1.0
 */

public class KripkeModelChecker extends AbstractModelChecker {
	/**
	 * The total size of the graph (number of states).
	 */
	protected int size;
	
	/**
	 * The Kripke Structure to model check.
	 */
	private final KripkeStructure structure;
	
	/**
	 * Indicates whether to throw a NotCNFException or not.
	 */
	private boolean cnf; //check conjunctive normal form
	
	/**
	 * Create a model checker for structure, the model checker could be used multiple
	 * times to check against different specifications.
	 * @param structure the structure to model check.
	 */
	public KripkeModelChecker(KripkeStructure structure) {
		this.structure = structure;
	}
	
	/**
	 * Model checks against the given specifications, the structure to be model checked is provided 
	 * in the constructor and it must be a KripkeStructure.
	 * 
	 * <p>This method should be overridden in the concrete model checker.</p>
	 * 
	 * @param specifications CTL specifications to model-check against, in simplified version.
	 * @return true if the structure satisfies the specifications, false otherwise.
	 */
	@Override
	public boolean run(AbstractExpression specifications) {
		visited = new HashMap<String, HashMap<AbstractExpression, Object[]>>();

		cnf = true;
		specifications = specifications.simplify();
		
		boolean hasStart = false;
		for(AbstractState startState : structure.getStartStates()) {
			hasStart = true;
			size  = structure.getNumberOfStates();
			if(!visit(specifications, startState, size)) {
				if(!cnf)
					System.out.println("NOTCNF");
			
				return false;
			}
		}		
		
		return hasStart;
	}

	/**
	 * Visits a BooleanVariable, Checks if the state has a label that satisfies it.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public boolean visit(BooleanVariable v, AbstractState state, int index) {
		String labels[] = ((KripkeState) state).getStateLabel();
		for(int i = 0; i < labels.length; i++) {
			if(labels[i].equals(v.getName())) {  //v in L(state) so state |= v. v is boolean var, i.e., atomic proposition.
				return true;
			}
		}
		return false;
	}

	/**
	 * Visits a Boolean Literal, returns its value.
	 * @return true if v has value true, false otherwise.
	 */
	@Override
	public boolean visit(BooleanLiteral v, AbstractState state, int index) {
		return v.getValue();
	}

	/**
	 * Visits a NotOperator.
	 * @return true if the child of v is not satisfied in state, false otherwise.
	 */
	@Override
	public boolean visit(NotOperator v, AbstractState state, int index) {
		return !visit(v.getChild(), state, index);
	}

	/**
	 * Visits an AndOperator.
	 * @return true if the state satisfies all of v's children, false otherwise.
	 */
	@Override
	public boolean visit(AndOperator v, AbstractState state, int index) {	
		for(AbstractExpression child : v.getChildren()) {
			if(!visit(child, state, index))
				return false;
		}
		
		return true;
	}

	/**
	 * Visits an OrOperator.
	 * @return true if the state satisfies one of v's children at least, false otherwise.
	 */
	@Override
	public boolean visit(OrOperator v, AbstractState state, int index) {
		for(AbstractExpression child : v.getChildren()) {
			if(visit(child , state, index))
				return true;
		}
		
		return false;
	}

	/**
	 * Visits an ImplicationOperator.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(ImplicationOperator v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an EquivalenceOperator.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(EquivalenceOperator v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an AFModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(AFModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an AGModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(AGModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an AXModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(AXModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an EFModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(EFModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an EGModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(EGModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an EXModality.
	 * @return true if the child of v is satisfied in at least one
	 * of the state's children, false otherwise.
	 */
	@Override
	public boolean visit(EXModality v, AbstractState state, int index) {
		boolean value = false;
		for(AbstractTransition child : state.getOutTransition()) {
			if(v.isIndexed()) { //EX_{i}
				if(!(child instanceof KripkeTransition)) //has no process
					continue;
				
				//TODO: carry changes on ProcessNames of transitions here when done.
				List<String> processes = Arrays.asList(((KripkeTransition) child).getProcessNames()); 
				List<String> actions = Arrays.asList(((KripkeTransition) child).getActionName().split(",")); 

				if(!(processes.containsAll(v.getProcessIndices()) && actions.containsAll(v.getActionIndices())))
					continue; // Not a match with the index.
			}
			
			value = value || visit(v.getChild(), child.getTo(), index);
		}
		return value;
	}

	HashMap<String, HashMap<AbstractExpression, Object[]>> visited;

	/**
	 * Visits an AUModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(AUModality v, AbstractState state, int index) {
		HashMap<AbstractExpression, Object[]> hash = visited.get(state.getName());
		if(hash != null) {
			Object[] objs = hash.get(v);
			if(objs != null) {
				if(((Integer) objs[0]) >= index) {
					
					return (Boolean) objs[1];
				}
			}
		} else {
			visited.put(state.getName(), new HashMap<AbstractExpression, Object[]>());
		}
		
		boolean right = visit(v.getRightChild(), state, size);
		if(right) {
			visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) true });
			return true;
		}
		
		if(index == 0) {
			return false;
		}
		
		if(!visit(v.getLeftChild(), state, size)) {
			visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) false });
			return false;
		}
		
		
		for(AbstractTransition transition : state.getOutTransition()) {
			if(!visit(v, transition.getTo(), index - 1)) {
				visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) false });				
				return false;
			}
		}
		
		visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) true });
		return true;
	}

	/**
	 * Visits an AWModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(AWModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}

	/**
	 * Visits an EUModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(EUModality v, AbstractState state, int index) {
		HashMap<AbstractExpression, Object[]> hash = visited.get(state.getName());
		if(hash != null) {
			Object[] objs = hash.get(v);
			if(objs != null) {
				if(((Integer) objs[0]) >= index) {
					return (Boolean) objs[1];
				}
			}
		} else {
			visited.put(state.getName(), new HashMap<AbstractExpression, Object[]>());
		}
		
		boolean right = visit(v.getRightChild(), state, size);
		if(right) {
			visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) true });
			return true;
		}
		
		if(index == 0) {
			return false;
		}
		
		if(!visit(v.getLeftChild(), state, size)) {
			visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) false });
			return false;
		}
		
		
		for(AbstractTransition transition : state.getOutTransition()) {
			if(visit(v, transition.getTo(), index - 1)) {
				visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) true });
				return true;
			}
		}
		
		visited.get(state.getName()).put(v, new Object[] {(Integer) index, (Boolean) false });
		return false;
	}

	/**
	 * Visits an EWModality.
	 * @return false, and switch cnf flag off, error.
	 */
	@Override
	public boolean visit(EWModality v, AbstractState state, int index) {
		cnf = false;
		return false;
	}
	
	/**
	 * Visits an AVModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public boolean visit(AVModality v, AbstractState state, int index) {		
		AbstractExpression euOperator = v.reduceToEU();
		
		return visit(euOperator, state, index);
	}
	
	/**
	 * Visits an EVModality, simplifies v, then model check.
	 * @return true if the state satisfies v, false otherwise.
	 */
	@Override
	public boolean visit(EVModality v, AbstractState state, int index) {
		AbstractExpression auOperator = v.reduceToAU();

		return visit(auOperator, state, index);
	}
}
