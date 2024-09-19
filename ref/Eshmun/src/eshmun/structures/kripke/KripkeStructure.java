package eshmun.structures.kripke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.visitor.visitors.EvaluaterVisitor;
import eshmun.expression.visitor.visitors.GreatestSubFormulaListerVisitor;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.modelchecker.KripkeModelChecker;
import eshmun.modelrepair.KripkeFormulaGenerator;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractStructure;
import eshmun.structures.AbstractTransition;
import eshmun.structures.Repairable;

/**
* This class represents a Kripke Structure.
* 
* <p>All States and transitions in this structure are forced to be of type KripkeState, KripkeTransition respectively.</p>
*  
* <p>The correct usage of this structure and its subclasses dictates the following:
* 	<ul>
* 		<li>Adding States: A state is created through one of its constructors, then added to 
* 			the structure using the add method, parentStructure attribute is handled automatically.</li>
* 		<li>Adding Transitions: A transition is created through one of its constructors then added to
* 			the structure using the add method, parenStructure attribute is handled automatically, also
* 			adding the transition to its states is handled automatically.</li>
* 		<li>Removing States: A state should only be removed using its own remove() method, or through 
* 			structure.remove(state). Both are guaranteed to respect the invariance.</li>
* 		<li>Removing Transitions: A transition should only be removed using its own remove() method, or through 
* 			structure.remove(transition). Both are guaranteed to respect the invariance, removing a transition
* 			from within a state will cause the invariance to break.</li>
* 	</ul>
* </p>
* 
* <p>
* Invariance:
* 	<ul>
* 		<li>A state is always mapped by its name, the state is immutable, thus this always hold.</li>
* 		<li>A state always contains all it valid transitions, given that the transitions has been added
* 			through structure.add() method.</li>
* 		<li>When a State is deleted, all its transitions are deleted with it.</li>
* 		<li>All Start and retain states are always present in their respective lists, the same
* 			holds for retain transitions.</li> 
* 	</ul>
* </p>
* 
* @author Ali Cherri, Kinan Dak Al Bab
* @since 1.0
*/
public class KripkeStructure extends AbstractStructure implements Repairable {
	/**
	 * Flags whether the repair formula must be generated in cnf or not.
	 */
	public boolean cnf = true;
	
	/**
	 * Contains all the labels in all states inside the structure.
	 */
	private HashSet<String> allLabels;
	
	/**
	 * Concrete Constructor for a Kripke Structure with a given name.
	 * @param name the name of the structure.
	 */
	public KripkeStructure(String name) {
		super(name);
		allLabels = new HashSet<String>();
	}
	
	/**
	 * Adds a state to this Kripke Structure, forces it to be a Kripke state.
	 * @param state the state to be added.
	 * @throws IllegalArgumentException if the given state was not an instance of Kripke State.
	 */
	@Override
	public void add(AbstractState state) throws IllegalArgumentException {
		if(!(state instanceof KripkeState))
			throw new IllegalArgumentException();
			
		super.add(state);
		
		for(String label : ((KripkeState) state).getStateLabel()) {
			label = label.trim();
			if(!label.equals(""))
				allLabels.add(label);
		}
	}
	
	/**
	 * Adds a transition to this Kripke Structure, forces it to be a Kripke Transition.
	 * @param transition the transition to be added.
	 * @throws IllegalArgumentException if the given transition was not an instance of Kripke Transition.
	 */
	@Override
	public void add(AbstractTransition transition) throws IllegalArgumentException {
		if(!(transition instanceof KripkeTransition))		
			throw new IllegalArgumentException();
		
		super.add(transition);
	}
	
	/**
	 * Gets a state from this structure by its name, the state is guaranteed to be a KripkeState.
	 * @param name the name of the state to be returned
	 * @return the matching state.
	 */
	@Override
	public KripkeState getState(String name) {
		AbstractState s = super.getState(name);
		boolean flag = (s instanceof KripkeState);
		
		assert flag; //sanity check
		
		return (KripkeState) s;
	}
	
	/**
	 * Returns a mapping between actions and transitions that are named after them.
	 * 
	 * <p>For example: assuming the transition T1 has action name 'A1, A2, A3'. 
	 * In the resulting map, all of "A1", "A2", and "A3" will be mapped to a list 
	 * containing T1. If the transition T2 had action name A2, then "A2" will be mapped
	 * to a list containing both T1 and T2.</p> 
	 *  
	 * @return A Map between actions and transitions.
	 */
	public HashMap<String, ArrayList<KripkeTransition>> mapActionsToTransition() {
		HashMap<String, ArrayList<KripkeTransition>> actionMap = new HashMap<>();
		
		for(AbstractTransition transition : transitions) {
			KripkeTransition kTransition = (KripkeTransition) transition;
			String[] actions = kTransition.getActionName().split(",");
			for(String action : actions) {
				action = action.trim();
				if(!action.isEmpty()) {
					ArrayList<KripkeTransition> transitionList = actionMap.get(action);
					if(transitionList == null) {
						transitionList = new ArrayList<>();
						actionMap.put(action, transitionList);
					}
					
					transitionList.add(kTransition);
				}
			}
		}
		
		return actionMap;
	}
	
	/**
	 * Returns a mapping between the change in labels and transitions.
	 * 
	 * <p>for example: (N1,K1=&gt;C1 : [T1, T2, T3]) means that all of T1, T2, T3 
	 * change the labels N1 and K1 to C1.</p>
	 * 
	 * @return the required mapping.
	 */
	public HashMap<String, ArrayList<KripkeTransition>> mapLabelsToTransitions() {
		HashMap<String, ArrayList<KripkeTransition>> result = new HashMap<String, ArrayList<KripkeTransition>>();
		
		//Get processes in this structure.
		int start = structureName.indexOf("(") + 1; int end = structureName.indexOf(")");
		String[] processes = structureName.substring(start, end).split(",");
		for(int i = 0; i < processes.length; i++) {
			processes[i] = processes[i].trim();
		}
		
		//BFS over states
		HashSet<KripkeState> visited = new HashSet<KripkeState>();  
		LinkedList<KripkeState> queue = new LinkedList<KripkeState>();
		for(AbstractState s : startStates) {
			queue.push((KripkeState) s);
			visited.add((KripkeState) s);
		}
		
		while(!queue.isEmpty()) {
			KripkeState state = queue.pop();
			HashMap<String, ArrayList<String>> originP2L = state.mapProcessToLabel(processes);
			
			//visit all children
			for(AbstractTransition t : state.getOutTransition()) {
				KripkeState next = (KripkeState) t.getTo();
				HashMap<String, ArrayList<String>> endP2L = next.mapProcessToLabel(processes);

				//Compare each process labels between state and next
				for(String p : processes) {
					ArrayList<String> s = new ArrayList<String>();
					ArrayList<String> e = new ArrayList<String>();
					
					for(String l : originP2L.get(p)) {
						if(!endP2L.get(p).contains(l)) {
							s.add(l);
						}
					}
					
					for(String l : endP2L.get(p)) {
						if(!originP2L.get(p).contains(l)) {
							e.add(l);
						}
					}
					
					//Sort alphabetically
					Collections.sort(s);
					Collections.sort(e);
					
					//Join arrays to string with , as delimiter 
					String s1 = "";
					String s2 = "";
					
					int i = 0;
					for(String l : s) {
						s1 += l + (i < s.size() - 1 ? "," : "");
						i++;
					}
					
					i = 0;
					for(String l : e) {
						s2 += l + (i < e.size() - 1 ? "," : "");
						i++;
					}
					
					//Add to result
					String key = s1+"=>"+s2;
					if(key.length() != 2) {
					if(result.get(key) == null) {
						result.put(key, new ArrayList<KripkeTransition>());
					}
					
					result.get(key).add((KripkeTransition) t);
					}
				}
				
				//BFS
				if(!visited.contains(next)) {
					visited.add(next);
					queue.add(next);
				}
			}
			
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean modelCheck(List<AbstractExpression> specifications) {
		AndOperator specs = new AndOperator(specifications.toArray(new AbstractExpression[specifications.size()]));
		KripkeModelChecker modelChecker = new KripkeModelChecker(this);
		
		return modelChecker.run(specs);
	}
	
	/**
	 * Model checks this structure state-by-state.
	 * Runs the model checker on each state (as if that state is the sole start state).
	 * @param specifications the formula to model check against.
	 * @return a list of var names of States that satisfy the formula (everything else does not).
	 */
	public synchronized ArrayList<String> stateByStateModelCheck(AbstractExpression specifications) {
		ArrayList<AbstractState> copyStartStates = new ArrayList<>(super.startStates);
		ArrayList<String> satResult = new ArrayList<>();
		
		//Construct model checker.
		KripkeModelChecker modelChecker = new KripkeModelChecker(this);
		for(AbstractState state : super.states.values()) {
			super.startStates.clear(); //Model Check state-by-state
			super.startStates.add(state);
						
			//If model check return true add state to result.
			if(modelChecker.run(specifications)) satResult.add(state.getVarName());
		}
		
		super.startStates.clear(); //Restore old start state.
		super.startStates.addAll(copyStartStates);
		return satResult;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @param actionSync ignored, useless.
	 * @param processSync ignored, useless.
	 */
	@Override
	public AndOperator generateRepairFormula(List<AbstractExpression> specifications, boolean actionSync, boolean processSync) throws NotCNFException, IllegalArgumentException {
		AbstractExpression specs = new AndOperator(specifications.toArray(new AbstractExpression[specifications.size()]));
		specs = specs.simplify();
		
		if(Eshmun.DEBUG_FLAG)
			System.out.println(specs);
		
		KripkeFormulaGenerator formulaGenerator = new KripkeFormulaGenerator(structureName, states, 
				startStates, retainStates, transitions, retainTransitions, allLabels, specs, cnf);

		return formulaGenerator.generateRepairFormula();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCNF(boolean cnf) {
		this.cnf = cnf;
	}
	
	/**
	 * Abstracts the contained model by label.
	 * @param variables the variables/labels to abstract against.
	 * @param oldKripke the old Kripke structure in a save object.
	 * @return the abstracted structure wrapped in an abstraction manager.
	 */
	public KripkeAbstractionManager abstractByLabel(Collection<String> variables, SaveObject oldKripke) {
		KripkeStructure abstractKripke = new KripkeStructure(getStructureName());
		
		String abstractName = abstractKripke.getStructureName();
		if(abstractName.contains("("))
			abstractName = abstractName.substring(0, abstractName.indexOf("(")).trim();
		
		//Maps the name of an old state to its corresponding new state.
		HashMap<String, KripkeState> oldToNew = new HashMap<String, KripkeState>();
		
		//Maps the name of a new state to the new state. TO AVOID DUPLICATES
		HashMap<String, KripkeState> newStates = new HashMap<String, KripkeState>();
		
		//Maps abstracted transitions to old transitions by variable name.
		HashMap<String, HashSet<String>> transitionsMap = new HashMap<String, HashSet<String>>();
		
		//Maps abstracted states to old states by variable name.
		HashMap<String, HashSet<String>> statesMap = new HashMap<String, HashSet<String>>();
				
		//States
		for(AbstractState state : states.values()) {
			KripkeState kState = (KripkeState) state;
			
			String name = "";
			Collection<String> labels = kState.getStateLabelAsCollection();
			for(String var : variables) {
				if(labels.contains(var)) 
					name += var + "-";
			}
			
			if(name.endsWith("-"))
				name = name.substring(0, name.length() - 1);
			
			if(name.trim().equals(""))
				name = "none";
			
			KripkeState newState = newStates.get(name);
			if(newState == null) {				
				String varName = State.STATE_PREFIX+"."+abstractName+"."+name;
				
				String newLabels = "";
				if(!name.equals("none")) 
					newLabels = name.replaceAll("-", ",");
					
				newState = new KripkeState(name, newLabels,  varName);
				
				newStates.put(name, newState);
				statesMap.put(varName, new HashSet<String>());
				
				abstractKripke.add(newState);
			}

			oldToNew.put(kState.getName(), newState);
			statesMap.get(newState.getVarName()).add(kState.getVarName());
			if(kState.isStartState())
				newState.setStartState(true);
			
			if(kState.isRetain())
				newState.setRetain(true);
		}
		
		//Maps the name of a new transition to the new transition. TO AVOID DUPLICATES
		HashMap<String, KripkeTransition> transitions = new HashMap<String, KripkeTransition>();
		
		//Transitions
		for(AbstractTransition tran : this.transitions) {
			String fromName = tran.getFrom().getName();
			String toName = tran.getTo().getName();
			
			fromName = oldToNew.get(fromName).getName();
			toName = oldToNew.get(toName).getName();
			
			String varName = Transition.TRANSITION_PREFIX+"." + abstractName+"." + fromName + "_" + toName;
			KripkeTransition kTran = transitions.get(varName);
			if(kTran == null) { //TODO: May wanna look at constructor for process indices.
				kTran = new KripkeTransition(abstractKripke.getState(fromName), abstractKripke.getState(toName), varName, null, "");
				
				abstractKripke.add(kTran);
				transitions.put(varName, kTran);
				transitionsMap.put(varName, new HashSet<String>());
			}
			
			transitionsMap.get(varName).add(tran.getVarName());
			if(tran.isRetain())
				kTran.setRetain(true);
		}
		
		return new KripkeAbstractionManager(abstractKripke, oldKripke, transitionsMap, statesMap);
	}
	
	public KripkeAbstractionManager abstractBySubFormula(AbstractExpression specifications, SaveObject oldKripke) {
		KripkeStructure abstractKripke = new KripkeStructure(getStructureName());
		
		String abstractName = abstractKripke.getStructureName();
		if(abstractName.contains("("))
			abstractName = abstractName.substring(0, abstractName.indexOf("(")).trim();
		
		//Maps the name of an old state to its corresponding new state.
		HashMap<String, KripkeState> oldToNew = new HashMap<String, KripkeState>();
		
		//Maps the name of a new state to the new state. TO AVOID DUPLICATES
		HashMap<String, KripkeState> newStates = new HashMap<String, KripkeState>();
		
		//Maps abstracted transitions to old transitions by variable name.
		HashMap<String, HashSet<String>> transitionsMap = new HashMap<String, HashSet<String>>();
		
		//Maps abstracted states to old states by variable name.
		HashMap<String, HashSet<String>> statesMap = new HashMap<String, HashSet<String>>();
		
		GreatestSubFormulaListerVisitor visitor = new GreatestSubFormulaListerVisitor();
		Collection<AbstractExpression> subFormulae = visitor.getSubFormulae(specifications);
		
		int s_index = 1;
		//States
		for(AbstractState state : states.values()) {
			KripkeState kState = (KripkeState) state;
			EvaluaterVisitor ev = new EvaluaterVisitor(kState.getStateLabelAsCollection(), true);
			
			String name = "";
			for(AbstractExpression formula : subFormulae) {
				if(ev.run(formula))
					name += formula.toString(true).trim()+"-";
			}
			
			if(name.endsWith("-"))
				name = name.substring(0, name.length() - 1);
			
			if(name.trim().equals(""))
				name += "none";
		
			
			KripkeState newState = newStates.get(name);
			if(newState == null) {								
				String newLabels = "";
				if(!name.equals("none")) { 
					newLabels = name.replaceAll("-", ",");
				}
				
				String varName = State.STATE_PREFIX+"."+abstractName+".F"+s_index;
				newState = new KripkeState("F"+s_index, newLabels,  varName);
				
				newStates.put(name, newState);
				statesMap.put(varName, new HashSet<String>());
				
				abstractKripke.add(newState);
				s_index++;
			}

			oldToNew.put(kState.getName(), newState);
			statesMap.get(newState.getVarName()).add(kState.getVarName());
			if(kState.isStartState())
				newState.setStartState(true);
			
			if(kState.isRetain())
				newState.setRetain(true);
			
			
		}
		
		//Maps the name of a new transition to the new transition. TO AVOID DUPLICATES
		HashMap<String, KripkeTransition> transitions = new HashMap<String, KripkeTransition>();
		
		//Transitions
		for(AbstractTransition tran : this.transitions) {
			String fromName = tran.getFrom().getName();
			String toName = tran.getTo().getName();
			
			fromName = oldToNew.get(fromName).getName();
			toName = oldToNew.get(toName).getName();
			
			String varName = Transition.TRANSITION_PREFIX+"." + abstractName+"." + fromName + "_" + toName;
			KripkeTransition kTran = transitions.get(varName);
			if(kTran == null) { //TODO: May wanna look at constructor for process indices.
				kTran = new KripkeTransition(abstractKripke.getState(fromName), abstractKripke.getState(toName), varName, null, "");
				
				abstractKripke.add(kTran);
				transitions.put(varName, kTran);
				transitionsMap.put(varName, new HashSet<String>());
			}
			
			transitionsMap.get(varName).add(tran.getVarName());
			if(tran.isRetain())
				kTran.setRetain(true);
		}
		
		return new KripkeAbstractionManager(abstractKripke, oldKripke, transitionsMap, statesMap);
	}
	
	/**
	 * Abstracts the contained model by label.
	 * @param selectedStates the states to be abstracted.
	 * @param oldKripke the old Kripke structure in a save object.
	 * @return the abstracted structure wrapped in an abstraction manager.
	 */
	public KripkeAbstractionManager abstractByManualSelection(Set<State> selectedStates, SaveObject oldKripke) {
        KripkeStructure abstractKripke = new KripkeStructure(getStructureName());

        String abstractName = abstractKripke.getStructureName();
        if(abstractName.contains("("))
            abstractName = abstractName.substring(0, abstractName.indexOf("(")).trim();

        //Maps the name of an old state to its corresponding new state.
        HashMap<String, KripkeState> oldToNew = new HashMap<String, KripkeState>();

        //Maps the name of a new state to the new state. TO AVOID DUPLICATES
        HashMap<String, KripkeState> newStates = new HashMap<String, KripkeState>();

        //Maps abstracted transitions to old transitions by variable name.
        HashMap<String, HashSet<String>> transitionsMap = new HashMap<String, HashSet<String>>();

        //Maps abstracted states to old states by variable name.
        HashMap<String, HashSet<String>> statesMap = new HashMap<String, HashSet<String>>();

        // Stores the names of the selected states, Fixes the renaming problem
        Set<String> selectedStateNames = new HashSet<>();

        // count the abstractions already made
        int number = 1;
        for(AbstractState s : states.values())
        	if(s.getName().startsWith("ABSTRACT-"))
        		number++;

        // save the names of the selected states
        for(State s: selectedStates){
            selectedStateNames.add(s.getName());
        }

        // TODO: Fix the Labels to accept disjunction
        Set<String> abstractLabel = new HashSet<>();

        // constructing new states for abstraction
        for(AbstractState state : states.values()) {
			KripkeState kState = (KripkeState) state;

			String name = "";
			Collection<String> labels = kState.getStateLabelAsCollection();
			// States which where selected will be renamed (ABSTRACT-#)
            if (selectedStateNames.contains(state.getName())) {
				name = "ABSTRACT-"+number;
				abstractLabel.addAll(Arrays.asList(((KripkeState) state).getStateLabel()));
			} else {
            	// otherwise name stays the same
				name = state.getName();
			}
				KripkeState newState = newStates.get(name);

				if (newState == null ) {
					String varName = State.STATE_PREFIX + "." + abstractName + "." + name;

					String newLabels = "";
					for(String l : ((KripkeState) state).getStateLabel())
						newLabels += l + ",";
					newState = new KripkeState(name,
							newLabels.substring(0,newLabels.length()-1), varName);

					newStates.put(name, newState);
					statesMap.put(varName, new HashSet<String>());

					abstractKripke.add(newState);
                }

				oldToNew.put(kState.getName(), newState);
				statesMap.get(newState.getVarName()).add(kState.getVarName());
				if (kState.isStartState())
					newState.setStartState(true);

				if (kState.isRetain())
					newState.setRetain(true);
			}
		KripkeState abstracted = newStates.get("ABSTRACT-"+number);
        // TODO: After disjuction is accepted, should be changed to OR Labels
        for(String l : abstractLabel)
        	abstracted.addLabel(l);
		//Maps the name of a new transition to the new transition. TO AVOID DUPLICATES
		HashMap<String, KripkeTransition> transitions = new HashMap<String, KripkeTransition>();

		//Transitions
		for(AbstractTransition tran : this.transitions) {
			String fromName = tran.getFrom().getName();
			String toName = tran.getTo().getName();

			fromName = oldToNew.get(fromName).getName();
			toName = oldToNew.get(toName).getName();

			String varName = Transition.TRANSITION_PREFIX+"." + abstractName+"." + fromName + "_" + toName;
			KripkeTransition kTran = transitions.get(varName);
			if(kTran == null) { //TODO: May wanna look at constructor for process indices.
				kTran = new KripkeTransition(abstractKripke.getState(fromName), abstractKripke.getState(toName), varName, null, "");

				abstractKripke.add(kTran);
				transitions.put(varName, kTran);
				transitionsMap.put(varName, new HashSet<String>());
			}

			transitionsMap.get(varName).add(tran.getVarName());
			if(tran.isRetain())
				kTran.setRetain(true);
		}

		return new KripkeAbstractionManager(abstractKripke, oldKripke, transitionsMap, statesMap);
	}
}
