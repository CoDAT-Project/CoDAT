package eshmun.lts.kripke;

/**
 * A class that describes a transition outgoing from a state to another. A
 * transition contains reference to its end state, and a name that could be a
 * process index.
 * 
 * @author Emile
 * 
 */
public class Transition {
	private KripkeState startState;
	private KripkeState endState;
	private String name;
	private String uniqueKey;
	private String taskName;

	/**
	 * Creates a new Transition object
	 * 
	 * @param endState
	 *            : the endState of the transition
	 * @param action
	 *            : the name of the transition
	 */
	public Transition(KripkeState startState, KripkeState endState, String name, String taskName) {
		this.startState = startState;
		this.endState = endState;
		this.name = name;
		this.taskName = taskName;
		computeUniqueKey();
	}

	public String getTaskName() {
		return taskName;
	}

	public KripkeState getStartState() {
		return startState;
	}

	public void setStartState(KripkeState startState) {
		this.startState = startState;
		computeUniqueKey();
	}

	public KripkeState getEndState() {
		return endState;
	}

	public void setEndState(KripkeState endState) {
		this.endState = endState;
		computeUniqueKey();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		computeUniqueKey();
	}

	public String toString() {
		return getName() + "[ " + getTaskName() + " ]->" + getEndState().getName();
	}

	private void computeUniqueKey() {
		uniqueKey = getStartState().getName() + getName() + getEndState().getName();
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endState == null) ? 0 : endState.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((startState == null) ? 0 : startState.hashCode());
		result = prime * result
				+ ((taskName == null) ? 0 : taskName.hashCode());
		result = prime * result
				+ ((uniqueKey == null) ? 0 : uniqueKey.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transition) {
			Transition transition = (Transition) obj;
			boolean sameStartState = this.startState.equals(transition.getStartState());
			boolean sameEndState = this.endState.equals(transition.getEndState());
			boolean sameName = this.name.equals(transition.getName());
			return sameStartState && sameEndState && sameName;
		}
		return super.equals(obj);
	}

	
	
}
