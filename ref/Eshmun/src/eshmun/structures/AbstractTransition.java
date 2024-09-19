package eshmun.structures;

/**
* This class represents an Abstract Transition.
*
* <p>A transition is immutable, nothing can be changed in it after it is created.</p>
*
* @author  Ali Cherri, Kinan Dak Al Bab
* @since 1.0
*/
public abstract class AbstractTransition {
	/**
	 * Name of the related variable.
	 */
	private final String varName;
	
	/**
	 * The state from which this transition starts.
	 */
	private AbstractState from;
	
	/**
	 * The state into which this transition ends.
	 */
	private AbstractState to;

	/**
	 * The structure this state belongs to, gets set automatically when the state is added to some structure.
	 */
	private AbstractStructure parentStructure;
	
	/**
	 * If this state is a retain state.
	 */
	private boolean retain;
	
	/**
	 * Dummy constructor for use in sub classes.
	 * @param from state from which this transition starts.
	 * @param to state into which this transition ends.
	 * @param varName the name of the related variable.
	 */
	protected AbstractTransition(AbstractState from, AbstractState to, String varName) {
		this(from, to, varName, false);
	}
	
	/**
	 * Dummy constructor for use in sub classes.
	 * @param from state from which this transition starts.
	 * @param to state into which this transition ends.
	 * @param varName the name of the related variable.
	 * @param isRetain if this transition is retain or not.
	 */
	protected AbstractTransition(AbstractState from, AbstractState to, String varName, boolean isRetain) {
		this.varName = varName;
		this.from = from;
		this.to = to;
		this.retain = isRetain;
	}
	
	/**
	 * Returns the parentStructure of this transition, null if the transition is not yet added
	 * to a structure.
	 * @return the parentStructure of this transition.
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
	 * given structure does not contain this transition.
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
	 * Gets the name of the related variable.
	 * @return the name of the related variable.
	 */
	public String getVarName() {
		return varName;
	}

	/**
	 * Checks if this transition is a retainTransition.
	 * @return true if it is a retain transition, false otherwise.
	 */
	public boolean isRetain() {
		return retain;
	}
	
	/**
	 * Sets if this is a retain transition.
	 * @param retain if this is a retain transition.
	 */
	public void setRetain(boolean retain) {
		this.retain = retain;
	}

	/**
	 * Gets the state from which this transition starts.
	 * @return the from state.
	 */
	public AbstractState getFrom() {
		return from;
	}
	
	/**
	 * Gets the state into which this transition ends.
	 * @return the to state.
	 */
	public AbstractState getTo() {
		return to;
	}
	
	/**
	 * Removes this transition from its parent, nothing happens if this 
	 * transition has no parent.
	 */
	public void remove() {
		if(parentStructure != null)
			parentStructure.remove(this);
	}
	
	/**
	 * Compares this with a given object.
	 * 
	 * <p>They are judged to be equal if they are both AbstractTransition or 
	 * some of its subclasses, and they have equal from states, and equal to states.</p>
	 * 
	 * @return true if the two objects are equal, false otherwise.
	 * 
	 * @see eshmun.structures.AbstractState
	 */
	@Override
	public boolean equals(Object o){
		if(o instanceof AbstractTransition) {
			AbstractTransition t = (AbstractTransition) o;
			
			return from.equals(t.from) && to.equals(t.to);
		}
		
		return false;
	}
}
