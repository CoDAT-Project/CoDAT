package eshmun.structures.skeletons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.guard.AndGuardOperator;
import eshmun.expression.guard.AtomicGuardExpression;
import eshmun.expression.guard.GuardExpression;
import eshmun.expression.guard.GuardExpression.GuardVisitor;
import eshmun.expression.guard.OrGuardOperator;
import eshmun.expression.operators.AndOperator;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;
import eshmun.structures.Repairable;
import eshmun.structures.kripke.KripkeState;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.kripke.KripkeTransition;
import eshmun.structures.kripke.MultiKripkeStructure;

/**
 * Class responsible for converting a structure to a synch. skeleton.
 * 
 * @author Kinan Dak Al Bab
 * @since 2.0
 */
public class ToSkeletonConverter {
	/**
	 * The structures to be converted.
	 */
	private Collection<KripkeStructure> structures;
	
	/**
	 * Multiple nested maps as follows:
	 * processesMap : Process Name -&gt; Process Map (A Map that represents the skeleton of that process).
	 * Process Map : From State (comma separated labels in alphabetical order) -&gt; Out Going transitions Map for that state.
	 * Out Going transitions Map : To State (comma separated labels in order) -&gt; The guard for moving between From State towards To State.  
	 */
	private HashMap<String, HashMap<String, HashMap<String, AndGuardOperator>>> processesMap;
	
	/**
	 * The resulting states will be stored here.
	 * The mapping is from labels to State Object.
	 */
	private HashMap<String, SkeletonState> states;
	
	/**
	 * The resulting transitions will be stored here.
	 */
	private ArrayList<SkeletonTransition> transitions;
		
	/**
	 * Creates a new converter for the given structure.
	 * @param structure the structure to convert.
	 * @throws IllegalArgumentException if the given structure has an invalid type or components.
	 */
	public ToSkeletonConverter(Repairable structure) {
		this.structures = new ArrayList<KripkeStructure>();
		
		if(structure instanceof MultiKripkeStructure) {
			this.structures.addAll(((MultiKripkeStructure) structure).getKripkeList());
		} else if(structure instanceof KripkeStructure) {
			structures.add((KripkeStructure) structure);
		} else {
			throw new IllegalArgumentException("Structure is of unknown type");
		}
		
		for(KripkeStructure kripke : structures) {
			if(kripke.getProcessesIndices().length == 0)
				throw new IllegalArgumentException("Please use a valid multi-process Kripke Structure. Declare the process names in the structure name.");
		}
	}
	
	/**
	 * Gets the Synchronization skeletons states.
	 * @return the converted states.
	 */
	public ArrayList<SkeletonState> getStates() {
		return new ArrayList<>(this.states.values());
	}
	
	/**
	 * Gets the Synchronization skeletons transitions.
	 * @return the converted transitions.
	 */
	public ArrayList<SkeletonTransition> getTransitions() {
		if(this.transitions == null) {
			this.transitions = new ArrayList<>();
			
			// Merge current map with global map.
			for(String pr : processesMap.keySet()) {
				HashMap<String, HashMap<String, AndGuardOperator>> processMap = processesMap.get(pr);
				for(String fromState : processMap.keySet()) {
					SkeletonState fromSkelState = this.states.get(pr + ":" + fromState);
					HashMap<String, AndGuardOperator> outTransitions = processMap.get(fromState);
					
					for(String toState : outTransitions.keySet()) {
						SkeletonState toSkelState = this.states.get(pr + ":" + toState);
						AndGuardOperator transitionGuard = outTransitions.get(toState);
						
						SkeletonTransition transition = new SkeletonTransition(fromSkelState, toSkelState, transitionGuard.simplify());
						this.transitions.add(transition);
						fromSkelState.addTransition(transition);
					}
				}
			}
		}
		
		 return new ArrayList<>(this.transitions);
	}
		
	/**
	 * Resets the converter.
	 */
	public void reset() {
		this.processesMap = new HashMap<>();
		this.states = new HashMap<>();
		this.transitions = null;
	}
	
	/**
	 * Converts the structure to a synchronization skeleton.
	 * @return true if the conversion was successful, false otherwise.
	 */
	public boolean convertToSkeleton() {
		this.reset();		
		for(KripkeStructure st : structures)
			if(!convertToSkeleton(st)) 
				return false;
		
		return true;
	}
	
	/**
	 * Converts a single structure to a sync skeleton.
	 * @param structure the structure to convert.
	 * @return true if the conversion was successful, false otherwise.
	 */
	private boolean convertToSkeleton(KripkeStructure structure) {
		// This map will be used to store the mapping between states and transitions for this skeleton.
		// When this skeleton is finalized, this will be merged into the global map.
		// Notice that guards here are ORs at the top level, while on the skeleton it is ANDs.
		// This is intentional, moving from one state (i.e bunch of labels) to another can be done in multiple ways
		// inside one pair of processes, so it is an OR. However, the actual guard is an AND combination of all
		// the different ways per process to reach the state. 
		HashMap<String, HashMap<String, HashMap<String, OrGuardOperator>>> currentMap = new HashMap<>();
		
		
		// Since we can reach a state (bunch of labels) in a skeleton using different paths and since
		// The state does not store the values of shared variables, it is not easy to compute the shared variables
		// Inside a state (reverse traversing of a graph). Instead, we will store all the possible shared variables 
		// values that can be reached through "SOME" path here. This could include conflicting values from different
		// paths.
		// Process -> Map: State -> assignments.
		HashMap<String, HashMap<String, ArrayList<BooleanVariable>>> stateToAssignments = new HashMap<>();
		
		// Setup
		String[] processes = structure.getProcessesIndices();
		String processIndicesStr = "";
		for(int i = 0; i < processes.length; i++) {
			processIndicesStr += processes[i];
			if(i+1 < processes.length) processIndicesStr += ",";
		}
		
		if(processIndicesStr.length() > 0) processIndicesStr = "{" + processIndicesStr + "}";
		
		Arrays.sort(processes);
		for(String pr : processes) {
			if(this.processesMap.get(pr) == null)
				this.processesMap.put(pr, new HashMap<String, HashMap<String, AndGuardOperator>>());
			
			currentMap.put(pr, new HashMap<String, HashMap<String,OrGuardOperator>>());
			stateToAssignments.put(pr, new HashMap<String, ArrayList<BooleanVariable>>());
		}
		
		// Graph Traversal.
		LinkedList<KripkeState> queue = new LinkedList<>();
		for(AbstractState start : structure.getStartStates()) {
			HashSet<KripkeState> visited = new HashSet<>();
			//BFS.
			queue.add((KripkeState) start);
			while(!queue.isEmpty()) { 
				KripkeState state = queue.poll();
				if(visited.contains(state)) continue;
				
				visited.add(state);
				// Split Kripke State to multiple Skeleton State per process.
				for(String pr : processes) {
					// Split labels into the ones belonging to this process and all other labels.
					HashMap<String, ArrayList<String>> labelMap = state.mapProcessToLabel(processes);
					
					ArrayList<String> thisProcessLabels = labelMap.get(pr);
					String fromStateName = join(thisProcessLabels, ",");
					
					ArrayList<String> otherLabels = new ArrayList<>();
					for(String otherPr : processes) {
						if(pr.equals(otherPr) || otherPr.isEmpty() || !labelMap.containsKey(otherPr)) continue;
						otherLabels.addAll(labelMap.get(otherPr));
					}
					
					otherLabels.addAll(labelMap.get(""));
					
					// Setup maps
					if(stateToAssignments.get(pr).get(fromStateName) == null)
						stateToAssignments.get(pr).put(fromStateName, new ArrayList<BooleanVariable>());
					if(currentMap.get(pr).get(fromStateName) == null)
						currentMap.get(pr).put(fromStateName, new HashMap<String, OrGuardOperator>());
					
					// Save shared variables mapped to the split state.
					for(String sharedVar : labelMap.get("")) 
						stateToAssignments.get(pr).get(fromStateName).add(new BooleanVariable(sharedVar));
					
					// Figure out the guard expression (All labels not belonging to this process).
					AndOperator guard = new AndOperator();
					for(String label : otherLabels) {
						int comEqIndex = label.indexOf(":=");
						int eqIndex = label.indexOf("=");
						
						if(comEqIndex != -1 && !label.contains("{") && processIndicesStr.length() > 0)
							label = label.substring(0, comEqIndex) + processIndicesStr + label.substring(comEqIndex + 1);
						else if(eqIndex != -1 && !label.contains("{") && processIndicesStr.length() > 0)
							label = label.substring(0, eqIndex) + processIndicesStr + label.substring(eqIndex);
						
						guard.and(new BooleanVariable(label));
					}
					
					// Find the different transitions and compute their assignments.
					for(AbstractTransition outTransition : state.getOutTransition()) {
						KripkeTransition outKripke = (KripkeTransition) outTransition;
						KripkeState toState = (KripkeState) outTransition.getTo();
						queue.push(toState);
						
						// Only use Explicit Self-Loops.
						ArrayList<String>nextStateLabels = toState.mapProcessToLabel(processes).get(pr);
						if(thisProcessLabels.containsAll(nextStateLabels) && nextStateLabels.containsAll(thisProcessLabels))
							if(!toState.equals(state)) continue;	
						
						AndOperator copyGuard = guard.clone();
						if(outKripke.isAnAction()) {
							boolean belongsToProcess = false;
							String[] transMakers = outKripke.getProcessNames();
							if(transMakers == null) {
								//processes
								String query = "Cannot identify process making transition from " + outKripke.getFrom().getName();
								query += " to " + outKripke.getTo().getName();
								query += System.lineSeparator() + "The possible processes are : " + Arrays.toString(processes);
								query += System.lineSeparator() + "Please enter the processes responsible (comma separated).";
								String input = JOptionPane.showInputDialog(null, query, "Transition Process(es)", JOptionPane.INFORMATION_MESSAGE);
								
								if(input == null) return false;
								else if(input.trim().isEmpty()) transMakers = new String[0];
								else transMakers = input.replace("\\s", "").split(",");
								
								outKripke.setProcessNames(transMakers);
							}
							
							for(String s : transMakers)
								if(s.trim().equals(pr.trim()))
									{ belongsToProcess = true; break; }
							
							if(belongsToProcess) {
								for(String action : outKripke.getActionName().split(","))
									if(!action.trim().isEmpty()) 
										copyGuard.and(new BooleanVariable("@"+action.trim()));
							}
						}
							
						// Assignments Computation
						ArrayList<BooleanVariable> assignments = new ArrayList<>();
						HashMap<String, ArrayList<String>> toLabelMap = toState.mapProcessToLabel(processes);
						String toStateName = join(toLabelMap.get(pr), ",");
						for(String label : toLabelMap.get("")) {
							int comEqIndex = label.indexOf(":=");
							int eqIndex = label.indexOf("=");
							
							// First try := then =
							if(comEqIndex != -1 && !label.contains("{") && processIndicesStr.length() > 0)
								label = label.substring(0, comEqIndex) + processIndicesStr + label.substring(comEqIndex);
							else if(eqIndex != -1 && !label.contains("{") && processIndicesStr.length() > 0)
								label = label.substring(0, eqIndex) + processIndicesStr + label.substring(eqIndex);
							
							BooleanVariable assignemnt = new BooleanVariable(label);
							if(!copyGuard.containsVariable(assignemnt)) assignments.add(assignemnt);
						}
						
						// Create the guard expression and store it.
						OrGuardOperator savedGuard = currentMap.get(pr).get(fromStateName).get(toStateName);
						if(savedGuard == null) savedGuard = new OrGuardOperator();
						
						savedGuard.orGuard(new AtomicGuardExpression(copyGuard.simplify(), assignments));
						currentMap.get(pr).get(fromStateName).put(toStateName, savedGuard);
					}
					
					// Create a Skeleton State for this state.
					SkeletonState skelState = this.states.get(pr + ":" + fromStateName);
					if(skelState == null) skelState = new SkeletonState(fromStateName);
					if(state.isStartState()) skelState.setStart(true);
					skelState.setProcess(pr);
					this.states.put(pr + ":" + fromStateName, skelState);
				}
			}
		}
		
		// Unset (set to bottom) unused variables.
		for(String pr : stateToAssignments.keySet()) {
			HashMap<String, ArrayList<BooleanVariable>> stateMap = stateToAssignments.get(pr);
			for(String state : stateMap.keySet()) {
				ArrayList<BooleanVariable> assignments = stateMap.get(state);
				HashSet<String> vars = new HashSet<>();
				
				// Remove assignment and keep only variable name.
				for(BooleanVariable assignment : assignments) {
					int index = assignment.getName().indexOf(":=");
					String var = index == -1 ? assignment.getName().trim() : assignment.getName().substring(0, index).trim();
					if(!var.contains("{") && processIndicesStr.length() > 0)
						var += processIndicesStr;
					
					vars.add(var);
				}
				
				UnsetAssignmentVisitor visitor = new UnsetAssignmentVisitor(vars);
				for(GuardExpression guard : currentMap.get(pr).get(state).values())
					guard.accept(visitor); // Unset unused variables.
			}
		}
		
		// Merge current map with global map.
		for(String pr : currentMap.keySet()) {
			HashMap<String, HashMap<String, OrGuardOperator>> processMap = currentMap.get(pr);
			for(String fromState : processMap.keySet()) {
				HashMap<String, OrGuardOperator> outTransitions = processMap.get(fromState);
				for(String toState : outTransitions.keySet()) {
					OrGuardOperator transitionGuard = outTransitions.get(toState);
					
					HashMap<String, AndGuardOperator> stateMap = processesMap.get(pr).get(fromState);
					if(stateMap == null) stateMap = new HashMap<>();
					
					AndGuardOperator and = stateMap.get(toState);
					if(and == null) and = new AndGuardOperator();
					
					and.andGuard(transitionGuard);
					stateMap.put(toState, and);
					processesMap.get(pr).put(fromState, stateMap);
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Helper function to join a list of strings into a string with a delimiter.
	 * @param list the list to join.
	 * @param delimiter the separator between joined elements.
	 * @return the joined string.
	 */
	private String join(ArrayList<String> list, String delimiter) {
		String result = "";
		for(int i = 0; i < list.size(); i++) {
			if(i > 0) result += delimiter;
			result += list.get(i).trim();
		}
		
		return result.trim();
	}
			
	/**
	 * @return the common name between all structures being converted. 
	 * The common name is the longest common prefix of all the structures names.
	 * For example: Mutex1(1, 2), Mutex2(2, 3), Mutex3(5, 6), etc; have a prefix in common
	 * "Mutex" which is the common name.
	 */
	public String getCommonName() {
		String longestPrefix = null;
		
		// Pass over all structure names.
		for(KripkeStructure kripke : structures) {
			String baseName = kripke.getBaseName();
			if(longestPrefix == null) longestPrefix = baseName; // longest prefix initially is the first structure's name.
			
			// Find longest common prefix between the two strings
			for(int i = 0; i < Math.min(longestPrefix.length(), baseName.length()); i++) {
				if(baseName.charAt(i) == longestPrefix.charAt(i)) continue;
				
				longestPrefix = longestPrefix.substring(0, i);
				break;
			}
			
			// If we reached empty string, return a placeholder.
			if(longestPrefix.isEmpty()) return "AutoGen";
		}
		
		return longestPrefix;
	}
	
	/**
	 * A Guard Expression visitor whose duty is to add assignment to unset shared variables
	 * when needed.
	 */
	private static class UnsetAssignmentVisitor implements GuardVisitor {
		/**
		 * The vars to unset (if unused).
		 */
		private HashSet<String> varsToUnset;		
		
		/**
		 * Creates a new unset assignment visitor.
		 * @param varsToUnset the variable to unset (if not used in an assignment).
		 */
		public UnsetAssignmentVisitor(HashSet<String> varsToUnset) {
			this.varsToUnset = varsToUnset;
		}
		
		@Override
		public void visit(AtomicGuardExpression atom) {
			// Remove used.
			for(BooleanVariable assignment : atom.getAssignments()) {
				int index = assignment.getName().indexOf(":=");
				String varName = index != -1 ? assignment.getName().substring(0, index).trim() :  assignment.getName().trim();
				
				this.varsToUnset.remove(varName);
			}
			
			for(String unsetVar : varsToUnset) { // only unused remaining.
				String assignment = unsetVar + ":=" + AtomicGuardExpression.BOTTOM_DELIMITER;
				atom.addAssignment(new BooleanVariable(assignment));
			}
		}
		
		// Unused.
		@Override
		public void visit(AndGuardOperator and) { }
		
		// Unused.
		@Override
		public void visit(OrGuardOperator or) { }
	};
	
}
