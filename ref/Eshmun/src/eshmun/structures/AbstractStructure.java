package eshmun.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
* This class represents an Abstract Structure.
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
public abstract class AbstractStructure {
	/**
	 * Name of the Structure.
	 */
	protected String structureName;
	
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
	 * Dummy constructor for use in sub classes.
	 * @param structureName the name of the structure. 
	 */
	protected AbstractStructure(String structureName) {
		this.structureName = structureName;
		
		this.states = new HashMap<String, AbstractState>();
		this.startStates = new ArrayList<AbstractState>();
		this.retainStates = new ArrayList<AbstractState>();
		this.transitions = new ArrayList<AbstractTransition>();
		this.retainTransitions = new ArrayList<AbstractTransition>();
	}
	
	/**
	 * gets the structure name.
	 * @return the structure name.
	 */
	public String getStructureName() {
		return structureName;
	}
	
	/**
	 * sets the structure name.
	 * @param structureName the new structure name.
	 */
	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}
	
	/**
	 * @return The base name (without process indices) of the structure.
	 */
	public String getBaseName() {
		int index = structureName.indexOf("(");
		if(index == -1) return structureName;
		
		return structureName.substring(0, index).trim();
	}
	
	/**
	 * @return An Array of process indices.
	 */
	public String[] getProcessesIndices() {
		int start = structureName.indexOf("(");
		int end = structureName.indexOf(")");
		if(start == -1 || end == -1) return new String[0];
		
		String[] processes = structureName.substring(start + 1, end).split(",");
		for(int i = 0; i < processes.length; i++) processes[i] = processes[i].trim();
		return processes;
	}	
	
	/**
	 * adds the given state to this structure.
	 * @param state the state to be added.
	 */
	protected void add(AbstractState state) {		
		states.put(state.getName(), state);
		if(state.isStartState())
			startStates.add(state);
		
		if(state.isRetain())
			retainStates.add(state);
		
		state.setParentStructure(this);
	}
	
	/**
	 * adds the given transition to this structure, and to the appropriate states.
	 * @param transition the transition to be added.
	 */
	protected void add(AbstractTransition transition) {
		transitions.add(transition);
		
		if(transition.isRetain())
			retainTransitions.add(transition);
		
		if(transition.getTo().equals(transition.getFrom())) {
			transition.getTo().addTransition(transition);
		
		} else {
			transition.getTo().addTransition(transition);
			transition.getFrom().addTransition(transition);
		}
		
		transition.setParentStructure(this);
	}
	
	/**
	 * gets a state by name, null if no state with this name exist.
	 * @param name the name of the state to get.
	 * @return the matching state.
	 */
	protected AbstractState getState(String name) {
		return states.get(name);
	}
	
	/**
	 * gets a copy of the states in this structure as a collection.
	 * @return a collection containing the states in this structure.
	 */
	public Collection<AbstractState> getStates() {
		return new HashSet<AbstractState>(states.values());
	}
	
	/**
	 * 
	 * @return a collection containing the states in this structure.
	 */
	public ArrayList<AbstractTransition> getTransitions() {
		return   (ArrayList<AbstractTransition>) transitions.clone()  ;
	}
	
	/**
	 * gets the number of states this structure has.
	 * @return the number of states.
	 */
	public int getNumberOfStates() {
		return states.values().size();
	}
	
	/**
	 * Checks if this structure contains the state.
	 *  
	 * @param state the state to look for.
	 * @return true if this structure contains the state, false otherwise.
	 * 
	 * @see eshmun.structures.AbstractState
	 */
	public boolean contains(AbstractState state) {
		for(AbstractState s : states.values()) {
			if(s.equals(state))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if this structure contains the transition.
	 *  
	 * @param transition the transition to look for.
	 * @return true if this structure contains the transition, false otherwise.
	 * 
	 * @see eshmun.structures.AbstractTransition
	 */
	public boolean contains(AbstractTransition transition) {
		for(AbstractTransition t : transitions) {
			if(t.equals(transition))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Removes the given state (and all its transitions) from the structure.
	 *  
	 * @param state the state to remove.
	 * 
	 * @see eshmun.structures.AbstractState
	 */
	public void remove(AbstractState state) {
		for(AbstractTransition t : state.inTransition) {
			remove(t);
		}
		
		for(AbstractTransition t : state.outTransition) {
			remove(t);
		}
		
		states.remove(state.getName());
		if(state.isStartState()) {
			startStates.remove(state);
		}
		
		if(state.isRetain()) {
			retainStates.remove(state);
		}
	}
	
	public ArrayList<AbstractState> getStartStates() {
		return this.startStates;
	}
	
	/**
	 * Removes the given transition from the structure.
	 *  
	 * @param transition the transition to remove.
	 * 
	 * @see eshmun.structures.AbstractTransition
	 */
	public void remove(AbstractTransition transition) {
		transitions.remove(transition);
		
		if(transition.isRetain()) {
			retainTransitions.remove(transition);
		}
		
		transition.getTo().removeTransition(transition);
		transition.getFrom().removeTransition(transition);
	}
}
