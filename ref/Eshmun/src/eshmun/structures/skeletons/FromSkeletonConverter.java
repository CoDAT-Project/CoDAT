package eshmun.structures.skeletons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

import eshmun.expression.guard.AtomicGuardExpression;
import eshmun.expression.guard.GuardExpression;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

/**
 * Class responsible for converting a synch. skeleton to a Kripke structure (single or multi).
 * 
 * @author Kinan Dak Al Bab
 * @since 2.0
 */
public class FromSkeletonConverter {
	/**
	 * The resulting structure definition.
	 */
	private String definition;
	
	/**
	 * List of the state of the skeletons to convert. Each
	 * state contains its out-going and in-coming edges/transitions.
	 */
	private ArrayList<SkeletonState> states;
	
	/**
	 * The participating processes in the list.
	 */
	private ArrayList<String> processes;
	
	/**
	 * A map from process names to a list of start skeleton states.
	 */
	private HashMap<String, ArrayList<SkeletonState>> process2Start;
	
	/**
	 * Base name for the structure(s).
	 */
	private String baseName;
	
	/**
	 * Construct a new converter for the given skeleton.
	 * @param name the base name of the resulting Kripke structure.
	 * @param states a list of the skeletons' states.
	 */
	public FromSkeletonConverter(String name, ArrayList<SkeletonState> states) {
		this.baseName = name;
		this.definition = null;
		this.states = states;
		this.process2Start = new HashMap<>();
		
		//Map all States to their process
		for(SkeletonState s : this.states) {			
			String p = s.getProcess();
			if(!s.isStart() || p == null || p.isEmpty()) continue;
			
			ArrayList<SkeletonState> tmp = process2Start.get(p);
			if(tmp == null) {
				tmp = new ArrayList<SkeletonState>();
				process2Start.put(p, tmp);
			}
			
			tmp.add(s);
		}
		
		// Get all the processes.
		this.processes = new ArrayList<String>(process2Start.keySet());
		Collections.sort(this.processes);
	}
	
	/**
	 * Convert to Multi-Kripke.
	 * @return true if conversion was successful. false otherwise (error).
	 */
	public boolean convertToMultipKripke() {
		this.definition = "***MULTI***" + System.lineSeparator(); //Start definition
		ArrayList<ArrayList<String>> crossProcesses = new ArrayList<>();
		crossProcesses.add(this.processes);
		crossProcesses.add(this.processes);
		
		int counter = 1;
		// Object[] := String[2]
		ArrayList<String[]> pairs = crossProduct(crossProcesses);
		for(String[] pair : pairs) {
			String p1 = pair[0];
			String p2 = pair[1];
			
			if(p1.compareTo(p2) == 0) continue; // Ignore same process pair.
			if(p1.compareTo(p2) > 0) continue; // Already done the other way.
			
			ArrayList<String> processes = new ArrayList<>();
			processes.add(p1); processes.add(p2);
			this.definition += generateKripkeDefinition(this.baseName + counter, processes) + System.lineSeparator();
			this.definition += "***END***" + System.lineSeparator();
			
			counter++;
		}
		
		return true;
	}
	
	/**
	 * Convert to Single-Kripke.
	 * @return true if conversion was successful. false otherwise (error).
	 */
	public boolean convertToSingleKripke() {
		this.definition = generateKripkeDefinition(this.baseName, this.processes);
		return true;
	}
	
	/**
	 * Generates an equivalent single Kripke Structure for the given States in a Skeleton.
	 * @param name the name of the generated structure.
	 * @param processes a sorted list of processes participating in the resulting structure.
	 * @return the structure definition.
	 */
	private String generateKripkeDefinition(String name, ArrayList<String> processes) {				
		ArrayList<ArrayList<SkeletonState>> process2Start = restrictProcesses(processes);
		ArrayList<SkeletonState[]> startStates = crossProduct(process2Start); // Object[] := SkeletonState[processes.size]
		
		String def = name + "(" + join(processes, ",")  + ")" + System.lineSeparator();
		String sDef = "States:" + System.lineSeparator();
		String tDef = "Transitions:" + System.lineSeparator();
		
		int nameCounter = 0; // For naming states.
		
		HashMap<String, SkeletonState> label2SkelState = new HashMap<>(); //In Skeleton
		for(SkeletonState s : states) label2SkelState.put(createConsistentSkeletonName(s), s);
		
		HashMap<String, String> label2KripkeName = new HashMap<>(); //In Kripke Structure
		HashMap<String, ArrayList<StoredTransition>> state2OutTransitions = new HashMap<>();
		
		// Initialize bfs with visited.
		LinkedList<String> bfs = new LinkedList<>();
		HashSet<String> visited = new HashSet<>();
		HashSet<String> startStatesLabels = new HashSet<>(); // To figure out if a label is a start state.
		for(SkeletonState[] startState : startStates) {
			String startLabel = createConsistentKripkeLabel(startState);
			bfs.push(startLabel);
			startStatesLabels.add(startLabel);
		}
		
		while(!bfs.isEmpty()) {
			String current = bfs.poll();
			
			// Check visited.
			if(visited.contains(current)) continue;
			visited.add(current);
			
			// Get a unique name for this state.
			String sName = label2KripkeName.get(current);
			if(sName == null) {
				sName = "S"+(nameCounter++);
				label2KripkeName.put(current, sName);
			}
			
			// Put the state in the definition.
			String sStart = startStatesLabels.contains(current) + "";
			String sLabel = current.replace(";", ",");
			while(sLabel.contains(",,")) sLabel = sLabel.replace(",,", ",");
			sDef += sName+":"+sLabel+":"+sStart+":false;"+System.lineSeparator();
			
			// Break the state down to Skeleton Labels.
			state2OutTransitions.put(current, new ArrayList<StoredTransition>());
			String[] currentKripkeLabels = current.split(";");
			HashSet<String> stateSet = new HashSet<>(); // Every single label and shared var.
			for(String p : currentKripkeLabels)
				for(String l : p.split(","))
					stateSet.add(l);
			
			// Get the Skeleton States by their label/name.
			SkeletonState[] currentSkeletonStates = new SkeletonState[processes.size()];
			for(int i = 0; i < processes.size(); i++)
				currentSkeletonStates[i] = label2SkelState.get(currentKripkeLabels[i]);
			
			// Make every available move by every process.
			for(int i = 0; i < currentSkeletonStates.length; i++) { // i is the process index.
				SkeletonState s = currentSkeletonStates[i];
				if(s == null) continue;
				
				for(SkeletonTransition t : s.getTransitions()) {
					ArrayList<StoredTransition> resultingTransitions = handleSkelTransition(t,
							currentKripkeLabels, stateSet, processes, i, current);
					
					ArrayList<StoredTransition> previousTransitions = state2OutTransitions.get(current);
					for(StoredTransition result : resultingTransitions) { 
						boolean shouldAdd = true;

						for(StoredTransition compare : previousTransitions) {
							if(compare.equals(result)) { 
								shouldAdd = false; break; 
							} if(compare.shouldMergeSameProcess(result)) {
								shouldAdd = false; compare.mergeSameProcess(result); break;
							} if(compare.shouldCombine2Processes(result)) { 
								shouldAdd = false; compare.combine2Processes(result); break; 
							}
						}
						
						if(shouldAdd) previousTransitions.add(result);
					}
					
					for(StoredTransition trans : resultingTransitions) {
						String nextKripkeLabels = join(trans.toKripkeLabels, ";");
						bfs.push(nextKripkeLabels);
					}					
				}
			}
			
		}
		
		// Write out transitions.
		for(ArrayList<StoredTransition> arr : state2OutTransitions.values()) {
			for(StoredTransition t : arr) {
				tDef += t.toDefinitionString(label2KripkeName) + System.lineSeparator();
			}
		}
		
		return def + sDef + tDef.trim();
	}
	
	//TODO comment
	private ArrayList<StoredTransition> handleSkelTransition(SkeletonTransition transition,
			String[] currentKripkeLabels, HashSet<String> stateSet,
			ArrayList<String> processes, int processIndex, String fromKripkeLabels) {
		ArrayList<StoredTransition> transitions = new ArrayList<>();
		
		GuardExpression guard = transition.getGuard();
		HashSet<HashSet<String>> effects = null;
		try {
			effects = guard.satisfyGuard(stateSet, processes);
		} catch(IllegalStateException ex) { return transitions; } // not satisfied
		
		// The transition changes the processes labels.
		String nextSkeletonLabels = createConsistentSkeletonName(transition.getTo());
		String[] nextKripkeLabels = Arrays.copyOf(currentKripkeLabels, processes.size() + 1); // + 1 for shared vars
		nextKripkeLabels[processIndex] = nextSkeletonLabels;
		
		// Look at each possible effects.
		for(HashSet<String> optionEffect : effects) {						
			TreeSet<String> actions = new TreeSet<>();
			TreeSet<String> newSharedVars = new TreeSet<>();
			HashSet<String> newSharedVarsNames = new HashSet<>();
			HashSet<String> removedSharedVars = new HashSet<>();
			
			// Organize effects.
			for(String e : optionEffect) {
				if(e.isEmpty()) continue;
				else if(e.startsWith("@")) actions.add(e.substring(1));
				else if(e.endsWith(AtomicGuardExpression.BOTTOM_DELIMITER))
					removedSharedVars.add(e.substring(0, e.indexOf("=")));
				else {
					newSharedVars.add(e);
					if(e.contains("=")) e = e.substring(0, e.indexOf("="));
					newSharedVarsNames.add(e);
				}
			}
			
			// Compute the new effects.
			String[] nextState = Arrays.copyOf(nextKripkeLabels, processes.size() + 1); // + 1 for shared vars.
			String oldSharedVars = nextState[processes.size()];
			String[] splitOldVars = oldSharedVars == null ? new String[0] : oldSharedVars.split(",");
			for(String oldVar : splitOldVars) {
				if(oldVar.isEmpty()) continue;
				String oldName = oldVar.contains("=") ? oldVar.substring(0, oldVar.indexOf("=")) : oldVar;
				if(removedSharedVars.contains(oldName) || newSharedVarsNames.contains(oldName))
					continue;
				newSharedVars.add(oldVar);
			}
			
			StoredTransition stored = new StoredTransition(fromKripkeLabels, 
					nextKripkeLabels, processIndex, actions, newSharedVars);
			
			transitions.add(stored);
		}
		
		return transitions;
	}
	
	/**
	 * Restricts the mapping from all process to their skeletons' start state, to
	 * a mapping of the given processes only.
	 * @param processes the processes to restrict the mapping to.
	 * @return a list, each element is a list of start states for the
	 * process that matches its index in the given processes list.
	 */
	private ArrayList<ArrayList<SkeletonState>> restrictProcesses(ArrayList<String> processes) {
		ArrayList<ArrayList<SkeletonState>> result = new ArrayList<>();
		for(String p : processes)
			result.add(this.process2Start.get(p));	
		
		return result;
	}

	/**
	 * Computes the cross product between n collections.
	 * @param <T> the type of elements in the original collections.
	 * @param collections a collection of the collection for which to compute cross product.
	 * @return a list of combination, each combination is an arry of elements; where the
	 * first element belongs to the first collection, the second element belong to the second
	 * collection, and so on.
	 */
	private <T> ArrayList<T[]> crossProduct(Collection<? extends Collection<? extends T>> collections) {
		ArrayList<T[]> result = new ArrayList<>();
		int size = collections.size();
		int index = 0;
		
		for(Collection<? extends T> current : collections) {
			ArrayList<T[]> tmp = new ArrayList<>();
			
			for(T o : current) {
				if(index == 0) { // Initially, result = the first collection.
					@SuppressWarnings("unchecked")
					T[] array = (T[]) Array.newInstance(o.getClass(), size);
					
					array[0] = o;
					tmp.add(array);
					continue;
				}
				
				for(T[] array : result) { // Add the element to all combinations so far.
					array = Arrays.copyOf(array, size);
					array[index] = o;
					tmp.add(array);
				}
			}
			
			result = tmp;
			index++;
		}
		
		return result;
	}
	
	/**
	 * Get a consistent label / name for the given state.
	 * The state could be made from multiple proposition.
	 * The consistent name is their sorted join by ',' .
	 * @param state the state to get the label/name for.
	 * @return the consistent label/name.
	 */
	private String createConsistentSkeletonName(SkeletonState state) {
		String[] labels = state.getLabel().replace(" ", "").split(",");
		Arrays.sort(labels);
		
		String result = "";
		for(int i = 0; i < labels.length; i++) {
			result += labels[i];
			if(i < labels.length - 1) result += ",";
		}
		return result;
	}
	
	/**
	 * Create a label for the given global state. The label will be consistent. 
	 * It is assumed that the given list is sorted by process indices. I.e. the 
	 * smallest process' state comes first.
	 * Note that in the resulting label: a semi-comma separates different processes labels,
	 * while a comma separates different labels for the same process. 
	 * @param states a list of skeleton (local) states sorted by their processes.
	 * @return a consistent label that combines the state of all the processes.
	 */
	private String createConsistentKripkeLabel(SkeletonState[] states) {		
		String label = "";
		for(SkeletonState skelState : states) {
			if(label.length() > 0) label += ";";
			label += createConsistentSkeletonName(skelState);
		}
		
		return label;
	}
	
	/**
	 * Joins a collection of strings into one string.
	 * @param strs the collection.
	 * @param sep the separator.
	 * @return a joined string.
	 */
	private static String join(Collection<String> strs, String sep) {
		String result = "";
		for(String s : strs)
			if(s != null)
				result += s + sep;
		
		while(result.contains(sep+sep)) result = result.replace(sep+sep, sep);
		
		if(result.length() > 0) 
			result = result.substring(0, result.length() - sep.length());
		
		return result;
	}
	
	/**
	 * Joins an array of strings into one string.
	 * @param strs the array.
	 * @param sep the separator.
	 * @return a joined string.
	 */
	private static String join(String[] strs, String sep) {
		String result = "";
		for(String s : strs)
			if(s != null)
				result += s + sep;
		
		while(result.contains(sep+sep)) result = result.replace(sep+sep, sep);
		
		if(result.length() > 0) 
			result = result.substring(0, result.length() - sep.length());
		
		return result;
	}
	
	/**
	 * Resets the converter internally to be ready to convert (the same given skeleton) again.
	 */
	public void reset() {
		this.definition = null;
	}
	
	/**
	 * @return the resulting structure definition from the last conversion attempted,
	 * 		   null if no conversion was attempted since construction or last reset. 
	 */
	public String getStructureDefinition() {
		return definition;
	}
	
	// TODO comment.
	private static class StoredTransition {
		public String fromKripkeLabels;
		public String[] toKripkeLabels;
		public int processIndex;
		public TreeSet<String> actions;
		public TreeSet<String> newSharedVars;
		
		public StoredTransition(String fromKripkeLabels, String[] toKripkeLabels, 
				int processIndex, TreeSet<String> actions, TreeSet<String> newSharedVars) {
			this.fromKripkeLabels = fromKripkeLabels;
			this.toKripkeLabels = toKripkeLabels;
			this.processIndex = processIndex;
			this.actions = actions;
			this.newSharedVars = newSharedVars;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof StoredTransition)) return false;
			
			StoredTransition o = (StoredTransition) obj;
			if(fromKripkeLabels.equals(o.fromKripkeLabels) && processIndex == o.processIndex
					&& actions.equals(o.actions) && newSharedVars.equals(o.newSharedVars)) {
				return Arrays.equals(toKripkeLabels, o.toKripkeLabels);
			}
			
			return false;
		}
		
		public boolean shouldCombine2Processes(StoredTransition other) {
			if(!fromKripkeLabels.equals(other.fromKripkeLabels))
				return false;
			
			if(processIndex == other.processIndex)
				return false;
			
			// Non-empty intersection.
			for(String a : this.actions)
				if(other.actions.contains(a)) return true;
			
			return false;				
		}
		
		public boolean shouldMergeSameProcess(StoredTransition other) {
			if(!fromKripkeLabels.equals(other.fromKripkeLabels))
				return false;
			
			if(!Arrays.equals(toKripkeLabels, other.toKripkeLabels))
				return false;
			
			if(processIndex != other.processIndex)
				return false;
			
			if(!newSharedVars.equals(other.newSharedVars))
				return false;
			
			return true;
		}
		
		public void mergeSameProcess(StoredTransition other) {
			this.actions.addAll(other.actions);
		}
		
		public void combine2Processes(StoredTransition other) {			
			// Merge labels.
			this.toKripkeLabels[other.processIndex] = other.toKripkeLabels[other.processIndex];
			
			// Construct shared variable table.
			HashMap<String, String> namesToVars = new HashMap<>();
			for(String s : newSharedVars) {
				String name = s.contains("=") ? s.substring(0, s.indexOf("=")) : s;
				namesToVars.put(name, s);
			}
			
			// Overwrite shared variables.
			for(String s : other.newSharedVars) {
				String name = s.contains("=") ? s.substring(0, s.indexOf("=")) : s;
				String oldVar = namesToVars.get(name);
				if(oldVar != null) this.newSharedVars.remove(oldVar);
				this.newSharedVars.add(s);
			}
		}
		
		public String toDefinitionString(HashMap<String, String> label2KripkeName) {
			this.toKripkeLabels[toKripkeLabels.length - 1] = join(newSharedVars, ",");
			
			String from = label2KripkeName.get(fromKripkeLabels);
			//String to = label2KripkeName.get(join(toKripkeLabels, ";"));
			String to = label2KripkeName.get(join(Arrays.copyOfRange(toKripkeLabels, 0, 2), ";")); //Fixed by Chukri, after breaking when there is an assignment in to
			String actions = join(this.actions,  ",");
			
			if(actions == null || actions.isEmpty())
				return from + ":" + to + ":" + false + ";";

			return from + ":" + to + ":" + false + ":" + actions + ";";
		}
	}
}
