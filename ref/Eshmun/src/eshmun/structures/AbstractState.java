package eshmun.structures;

import java.util.ArrayList;

/**
* This class represents an Abstract State.
* 
* <p>A state is immutable, the only modifications possible are adding/removing transitions.</p>
* <p>Removing a transition from this state will only cause the transition to be removed from 
* 	this state not the parent structure, it might cause the invariant to break.</p>
*
* @author Ali Cherri, Kinan Dak Al Bab
* @since 1.0
*/
public abstract class AbstractState {
	/**
	 * Name of the state.
	 */
	private final String name;
	
	/**
	 * Name of the related variable.
	 */
	private final String varName;
	
	/**
	 * The structure this state belongs to, gets set automatically when the state is added to some structure.
	 */
	private AbstractStructure parentStructure;
	
	/**
	 * Transitions coming out of this state, maintained automatically.
	 */
	protected final ArrayList<AbstractTransition> outTransition;
	
	/**
	 * Transitions coming into this state, maintained automatically.
	 */
	protected final ArrayList<AbstractTransition> inTransition;
	
	/**
	 * If this state is a start state.
	 */
	private boolean start;
	
	/**
	 * If this state is a retain state.
	 */
	private boolean retain;
	
	/**
	 * Dummy constructor for use in sub classes.
	 * @param name the state name.
	 * @param varName the name of the related variable.
	 */
	protected AbstractState(String name, String varName) {
		this(name, varName, false, false);
	}
	
	/**
	 * Dummy constructor for use in sub classes.
	 * @param name the state name.
 	 * @param varName the name of the related variable.
	 * @param isStart if this state is a start state.
	 * @param isRetain if this state is a retain state.
	 */
	protected AbstractState(String name, String varName, boolean isStart, boolean isRetain) {
		this.name = name;
		this.varName = varName;
		this.start = isStart;
		this.retain = isRetain;
		
		parentStructure = null;
		
		outTransition = new ArrayList<AbstractTransition>();
		inTransition = new ArrayList<AbstractTransition>();
	}

	/**
	 * Gets the name of the state.
	 * @return the name of the state.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the name of the related variable.
	 * @return the name of the related variable.
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * Checks if this state is a startState.
	 * @return true if it is a start state, false otherwise.
	 */
	public boolean isStartState() {
		return start;
	}
	
	/**
	 * Sets if this is a start state.
	 * @param start if this is a start state.
	 */
	public void setStartState(boolean start) {
		this.start = start;
	}
	
	/**
	 * Checks if this state is a retainState.
	 * @return true if it is a retain state, false otherwise.
	 */
	public boolean isRetain() {
		return retain;
	}

	/**
	 * Sets if this is a retain state.
	 * @param retain if this is a retain state.
	 */
	public void setRetain(boolean retain) {
		this.retain = retain;
	}
	
	/**
	 * Returns the parentStructure of this state, null if the state is not yet added
	 * to a structure.
	 * @return the parentStructure of this state.
	 * 
	 * @see eshmun.structures.AbstractStructure
	 */
	public AbstractStructure getParentStructure() {
		return parentStructure;
	}

	/**
	 * Sets the parentStructure, must never be used directly.
	 * 
	 * @param parentStructure the parentStructure.
	 * @throws IllegalStateException if the parentStructure was already set, or if the
	 * given structure does not contain this state.
	 * @throws NullPointerException if the given structure is null.
	 * 
	 * @see eshmun.structures.AbstractStructure
	 */
	public void setParentStructure(AbstractStructure parentStructure) {
		if(parentStructure == null)
			throw new NullPointerException();
		
		if(this.parentStructure != null || !parentStructure.contains(this))
			throw new IllegalStateException();
		
		this.parentStructure = parentStructure;
	}

	/**
	 * Adds a transition to the state, must never be used directly, instead add the transition
	 * to the appropriate structure, this is then called automatically.
	 * 
	 * @param transition the transition to be added.
	 */
	public void addTransition(AbstractTransition transition) {
		if(transition.getTo().equals(this)) {
			inTransition.add(transition);
		} 
		
		if(transition.getFrom().equals(this)) {
			outTransition.add(transition);
		}
	}
	
	/**
	 * Gets a shallow copy of the transitions coming out of this state.
	 * @return a copy of the out transitions.
	 */
	public ArrayList<AbstractTransition> getOutTransition() {
		return new ArrayList<AbstractTransition>(outTransition);
	}
	
	/**
	 * Gets a shallow copy of the transitions coming into this state.
	 * @return a copy of the in transitions.
	 */
	public ArrayList<AbstractTransition> getInTransition() {
		return new ArrayList<AbstractTransition>(inTransition);
	}

	/**
	 * Removes a transition.
	 * @param transition the transition to be removed.
	 */
	public void removeTransition(AbstractTransition transition) {
		outTransition.remove(transition);
		inTransition.remove(transition);
	}
	
	/**
	 * Removes this state from its parent, nothing happens if this 
	 * state has no parent.
	 */
	public void remove() {
		if(parentStructure != null)
			parentStructure.remove(this);
	}
	
	/**
	 * Compares this with a given object.
	 * 
	 * <p>They are judged to be equal if they are both AbstractState or 
	 * some of its subclasses, and have the same name.</p>
	 * 
	 * @return true if the two objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof AbstractState)
			return name.equals(((AbstractState) o).name);
		
		return false;
	}
	
	/**
	 * Abides by the hashCode/equals contract since equality is determined by the state name.
	 * @return the hashCode of the name.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
