package eshmun.modelrepair.debug.notcnf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.ImplicationOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.modelrepair.KripkeFormulaGenerator;
import eshmun.modelrepair.KripkeNextReleaseGenerator;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;
import eshmun.structures.kripke.KripkeState;

/**
 * This class is used to generate the repair formula for a single Kripke Structure.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.structures.kripke.KripkeStructure
 */
public class KripkeFormulaGeneratorNotCNF {
	/**
	 * Name of the Structure to generate repair for.
	 */
	protected final String structureName;
	
	/**
	 * Mapping of names to States.
	 */
	protected final HashMap<String, AbstractState> states;
	
	/**
	 * List of Start states.
	 */
	protected final ArrayList<AbstractState> startStates;
	
	/**
	 * List of Retain states.
	 */
	protected final ArrayList<AbstractState> retainStates;
	
	/**
	 * List of all Transitions.
	 */
	protected final ArrayList<AbstractTransition> transitions;
	
	/**
	 * List of Retain transition.
	 */
	protected final ArrayList<AbstractTransition> retainTransitions;
	
	/**
	 * Contains all the labels in all states inside the structure.
	 */
	private final HashSet<String> allLabels;
	
	/**
	 * The specifications of the Kripke Structure, always in the Simplest form.
	 */
	private final AbstractExpression specifications;
	
	/**
	 * The Resulting repair formula.
	 */
	private AndOperator repairFormula;
	
	/**
	 * Flags whether to output the formula in CNF or not.
	 */
	private boolean cnf = false;
	
	/**
	 * The Actual CNF Formula Generator.
	 */
	private KripkeFormulaGenerator form;
	
	/**
 	 * Create a new Formula Generator for the given structure and specifications.
	 * @param structureName the name of the structure to be repaired.
	 * @param states a mapping from names to States.
	 * @param startStates a list of start States.
	 * @param retainStates a list of retain States.
	 * @param transitions a list of Transitions
	 * @param retainTransitions a list of retain Transitions.
	 * @param allLabels all the labels in the structure.
	 * @param specifications The specifications of the Kripke Structure, will be simplified inside.
	 * @param f an equivalent formula generator in CNF.
	 */
	public KripkeFormulaGeneratorNotCNF(String structureName, HashMap<String, AbstractState> states, 
			ArrayList<AbstractState> startStates, ArrayList<AbstractState> retainStates, 
			ArrayList<AbstractTransition> transitions, ArrayList<AbstractTransition> retainTransitions, 
			HashSet<String> allLabels, AbstractExpression specifications, KripkeFormulaGenerator f) {
		
		if(structureName.contains("(")) //Format the structure Name
			structureName.substring(0, structureName.indexOf("("));
		
		structureName = structureName.replaceAll(" ", "_");
		
		form = f;
		this.structureName = structureName;
		this.states = states;
		this.startStates = startStates;
		this.retainStates = retainStates;
		this.transitions = transitions;
		this.retainTransitions = retainTransitions;
		this.allLabels = allLabels;
		
		this.specifications = specifications;
	}
	
	/**
	 * Stores a variable in the required map, the variable is on the form : VAR_{state}_{formula}.
	 * 
	 * @param variable the variable to be stored.
	 * @param stateName the name of the state the variable belongs to.
	 * @param formula the formula that the variable belongs to.
	 */
	public void storeFormulaVariable(BooleanVariable variable, String stateName, AbstractExpression formula) {
		form.storeFormulaVariable(variable, stateName, formula);
	}
	
	/**
	 * Loads a variable from the state-formula map.
	 * 
	 * @param stateName the name of the state the variable belongs to.
	 * @param formula the formula that the variable belongs to.
	 * 
	 * @return the required variable, if it did not exist a new one is 
	 * created and stored then returned.
	 */
	public BooleanVariable loadFormulaVariable(String stateName, AbstractExpression formula) {
		return form.loadFormulaVariable(stateName, formula);
	}
		
	/**
	 * Stores an expression in the required map, the expression is on the form : 
	 * VAR_{state}_{label} or !VAR_{state}_{label}.
	 * 
	 * @param expression the expression to be stored.
	 * @param stateName the name of the state the variable belongs to.
	 * @param label the label that the variable belongs to.
	 */
	public void storeLabelVariable(BooleanVariable expression, String stateName, String label) {
		form.storeLabelVariable(expression, stateName, label);
	}
	
	/**
	 * Loads a variable from the state-label map, either a variable or a NotOperator negating a variable.
	 * 
	 * @param stateName the name of the state the variable belongs to.
	 * @param label the label that the variable belongs to.
	 * 
	 * @return the required variable, if it did not exist a new one is 
	 * created and stored then returned.
	 */
	public BooleanVariable loadLabelVariable(String stateName, String label) {
		return form.loadLabelVariable(stateName, label);
	}
	
	/**
	 * Loads a variable, determines if load should be done from state-label or state-expression map 
	 * depending on type of expression.
	 *  
	 * @param stateName the name of the state the variable belongs to.
	 * @param expression the expression that the variable belongs to.
	 * 
	 * @return the required variable, if it did not exist a new one is 
	 * created and stored then returned.
	 */
	public BooleanVariable loadVariable(String stateName, AbstractExpression expression) {
		return form.loadVariable(stateName, expression);
	}
	
	/**
	 * Loads a variable from the state-formula map with a super-script.
	 * 
	 * @param stateName the name of the state the variable belongs to.
	 * @param formula the formula that the variable belongs to.
	 * @param m the super index, represents the number of propagations
	 * 
	 * @return the required variable, if it did not exist a new one is 
	 * created and stored then returned.
	 */
	public BooleanVariable loadVariable(String stateName, AbstractExpression formula, int m) {		
		return form.loadVariable(stateName, formula, m);
	}
	
	/**
	 * Gets the variable related to the given state or transition.
	 * 
	 * @param name the name of the state or transition.
	 * @return the variable, null if name is not a valid state or transition name.
	 */
	public BooleanVariable getStateOrTransitionVariable(String name) {
		return form.getStateOrTransitionVariable(name);
	}
	
	/**
	 * Generate the repair formula for the given structure.
	 * @return An AndOperator in CNF format that is the repair formula.
	 * @throws NotCNFException if the formula was not successfully converted to CNF internally.
	 */
	public AndOperator generateRepairFormula() throws NotCNFException {
		repairFormula = new AndOperator();
		
		repairFormula.and(labelsPerState()); //Satisfied and Unsatisfied Labels per State
		repairFormula.and(flowConstraints()); //Constraints for respecting totality and flow
		repairFormula.and(retainStartStates()); //Retain start state
		repairFormula.and(retainStatesAndTransitions()); //Retain states & Retain transitions
		
		if(cnf) {
			KripkeNextReleaseGenerator internalGen = new KripkeNextReleaseGenerator(form, startStates, states);
			if(!internalGen.run(specifications))
				throw new NotCNFException();
			
			repairFormula.and(internalGen.getNextReleaseFormula());
		} else {
			KripkeNextReleaseGeneratorNotCNF internalGen = new KripkeNextReleaseGeneratorNotCNF(form, startStates, states);
			if(!internalGen.run(specifications))
				throw new NotCNFException();
			
			repairFormula.and(internalGen.getNextReleaseFormula());
		}
		
		return repairFormula;
	}
	
	/**
	 * Construct a formula that forces the flow of the model (totality, transition needs the start and ends states).
	 * 
	 * <p>These are the 3rd and 4th rule in the paper.</p>   
	 * 
	 * <p>Keeping a transition implies that both its start and end states should be kept,
	 * This result in the following constraint: <br>
	 * &nbsp;&nbsp;&nbsp; E =&gt; S_start &amp; S_end <br></p>
	 * 
	 * <p>Keeping a state is also equivalent to keeping one or more of its transitions (and end start),
	 * This preserves totality, and results in the following constraint: <br>
	 * &nbsp;&nbsp;&nbsp; S &lt;=&gt; (E1 &amp; S_end1) | (E2 &amp; S_end2) | .. <br>
	 * However, due to the first condition, we can reduce this to
	 * &nbsp;&nbsp;&nbsp; S =&gt; E1 | E2 | .. 
	 * </p>
	 * 
	 * <p>Also, if a state is kept it should be reachable, this result in the following constrain: <br>
	 * &nbsp;&nbsp;&nbsp; S =&gt; E`1 | E`2 | .. <br>
	 * Where E`1, E`2, ... are transitions that end in S.
	 * </p>
	 * 
	 * @return an AndOperator that forces the flow constraints described above, in CNF format.
	 * @see eshmun.expression.operators.AndOperator
	 */
	private AndOperator flowConstraints() {
		AndOperator andOp = new AndOperator();
		
		for(AbstractState s : states.values()) {
			OrOperator orOp = new OrOperator();
			BooleanVariable s_start = form.getStateOrTransitionVariable(s.getVarName());
			
			if(s_start == null) {
				s_start = new BooleanVariable(s.getVarName());
				form.storeStateOrTransitionVariable(s.getVarName(), s_start);
			}
			
			for(AbstractTransition t : s.getOutTransition()) { //Totality
				AbstractState s1 = t.getTo();
				
				BooleanVariable e = form.getStateOrTransitionVariable(t.getVarName());
				if(e == null) {
					e = new BooleanVariable(t.getVarName());
					form.storeStateOrTransitionVariable(t.getVarName(), e);
				}
				
				BooleanVariable s_end = form.getStateOrTransitionVariable(s1.getVarName());
				if(s_end == null) {
					s_end = new BooleanVariable(s1.getVarName());
					form.storeStateOrTransitionVariable(s1.getVarName(), s_end);
				}
				
				ImplicationOperator impOp = new ImplicationOperator(e, s_end);
				andOp.and(impOp);
				
				impOp = new ImplicationOperator(e, s_start);
				andOp.and(impOp);
				
				orOp.or(new AndOperator(e, s_end));
			}
			
			EquivalenceOperator totalityEquiv = new EquivalenceOperator(s_start, orOp);
			andOp.and(totalityEquiv);
			
			if(!s.isStartState()) { //if start state, it is always reachable.
				orOp = new OrOperator();
				for(AbstractTransition t : s.getInTransition()) { //Reachability
					if(t.getFrom().equals(t.getTo())) //Ignore self transitions with reachability
						continue;
					
					BooleanVariable e = form.getStateOrTransitionVariable(t.getVarName());
					if(e == null) {
						e = new BooleanVariable(t.getVarName());
						form.storeStateOrTransitionVariable(t.getVarName(), e);
					}
					
					orOp.or(e);
				}
				
				ImplicationOperator reachableImp = new ImplicationOperator(s_start, orOp);
				andOp.and(reachableImp);
			}
		}
		
		return andOp;
	}
	
	/**
	 * Constructs a formula that forces the retain of some start state, Notice that the 
	 * retain of a start state requires that it satisfies the specifications.
	 * 
	 * <p>The retain is done through adding a disjunction to the repair formula, containing
	 * the variables relating to the start states.</p>
	 * 
	 * <p>Also if a state is retained it implies that it satisfies the specification.</p>
	 * 
	 * @return an OrOperator that ands the variables related to the start states, in CNF format.
	 * @see eshmun.expression.operators.OrOperator
	 */
	private AndOperator retainStartStates() {
		AndOperator andOp = new AndOperator(); //For the implications
		OrOperator orOp = new OrOperator(); //For retaining at least one start state
		
		for(AbstractState s : startStates) {
			BooleanVariable var = form.getStateOrTransitionVariable(s.getVarName());
			orOp.or(var); //Retain one at least
			
			BooleanVariable specs = loadVariable(s.getName(), specifications);
			OrOperator impOp = new OrOperator(var.negate(), specs);
			
			andOp.and(impOp); //Retain means it satisfy specifications
		}
		
		andOp.and(orOp);
		return andOp;
	}
	
	/**
	 * Constructs a formula that forces the retain of States and Transitions that were marked as retain.
	 * 
	 * <p>The retain is done through adding a conjecture to the repair formula, containing
	 * the variables relating to the States and Transitions.</p>
	 * 
	 * <p>The retain also requires that the states and transitions are reachable, which is enforced
	 * by requiring their parents to be reachable, and so on..</p>
	 * 
	 * @return an AndOperator that ands the variables related to the retained States 
	 * and Transitions, in CNF format.
	 * @see eshmun.expression.operators.AndOperator
	 */
	private AndOperator retainStatesAndTransitions() {
		int count = states.values().size();
		HashSet<AbstractTransition> retained = new HashSet<AbstractTransition>();
		
		AndOperator andOp = new AndOperator();
		
		for(AbstractState s : retainStates) { //States
			String name = s.getVarName();
			andOp.and(form.getStateOrTransitionVariable(name));
			
			//Reachability
			for(AbstractTransition t : s.getInTransition()) {
				andOp.and(reachableTransition(t, retained, count));
				retained.add(t);
			}
		}
		
		for(AbstractTransition t : retainTransitions) { //Transitions			
			String name = t.getVarName();
			andOp.and(form.getStateOrTransitionVariable(name));
			
			if(retained.contains(t))
				continue;
			
			andOp.and(reachableTransition(t, retained, count)); //Reachability
			retained.add(t);
		}
		
		return andOp;
	}
	
	/**
	 * Forces a transition to be retained by forcing its parents to be retained.
	 * @param transition the transition to retain.
	 * @param retained a set of previously retained transitions, plays the role of 'visited'.
	 * @param count used to count down 
	 * @return an expression in CNF that forces the retainment of the given transition.
	 */
	private AbstractExpression reachableTransition(AbstractTransition transition, HashSet<AbstractTransition> retained, int count) {
		if(count == 0)
			return null;
		
		AndOperator andOp = new AndOperator();
		
		OrOperator orOp = new OrOperator();
		orOp.or(form.getStateOrTransitionVariable(transition.getVarName()).negate());
		
		if(transition.getFrom().isStartState()) {
			orOp.or(form.getStateOrTransitionVariable(transition.getFrom().getVarName()));
			andOp.and(orOp);
			
			return andOp;
		}
		
		boolean flag = false;
		for(AbstractTransition parentTransition : transition.getFrom().getInTransition()) {
			if(retained.contains(parentTransition)) {
				BooleanVariable parentVar = form.getStateOrTransitionVariable(parentTransition.getVarName());
				orOp.or(parentVar);
				
				flag = true;
			} else {
				AbstractExpression result = reachableTransition(parentTransition, retained, count - 1);
				if(result != null) {
					andOp.and(result);
					
					BooleanVariable parentVar = form.getStateOrTransitionVariable(parentTransition.getVarName());
					orOp.or(parentVar);
					
					flag = true;
				}
			}
		}

		if(flag) {
			andOp.and(orOp.simplify());
			return andOp;
		}
		
		return null;
	}
	
	/**
	 * Construct an AndOperator, each variable inside the AndOperator related to a label and a state.
	 * 
	 * <p>If the variable was not negated it means that the state satisfies the label, if it was negated
	 * it means the state doesn't satisfy the label.</p>
	 * 
	 * @return AndOperator for relating states to labels, in CNF format.
	 * @see eshmun.expression.operators.AndOperator
	 */
	private AndOperator labelsPerState() {
		AndOperator andOp = new AndOperator();
		String structureName = this.structureName;
		
		for(AbstractState s : states.values()) {
			Collection<String> labels = ((KripkeState) s).getStateLabelAsCollection();
			for(String l : allLabels) {
				BooleanVariable labelVar = new BooleanVariable("L."+structureName+"."+s.getName()+"."+l);
				if(labels.contains(l)) {
					andOp.and(labelVar);
				} else {
					AbstractExpression exp = labelVar.negate();
					andOp.and(exp);
				}
				
				storeLabelVariable(labelVar, s.getName(), l);
			}
			
			BooleanVariable trueFalseVar = new BooleanVariable("L."+structureName+"."+s.getName()+".true");
			storeLabelVariable(trueFalseVar, s.getName(), "true");
			andOp.and(trueFalseVar);
			
			trueFalseVar = new BooleanVariable("L."+structureName+"."+s.getName()+".false");
			storeLabelVariable(trueFalseVar, s.getName(), "false");
			andOp.and(trueFalseVar.negate());
		}

		return andOp;
	}
}
