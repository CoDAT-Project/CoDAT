package eshmun.structures.kripke;

import java.util.ArrayList;
import java.util.HashMap;

import eshmun.structures.AbstractTransition;

/**
 * This class represents a Kripke Transition.
 * 
 * @author Ali Cherri, Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.structures.AbstractTransition
 */
public class KripkeTransition extends AbstractTransition {
	/**
	 * The process making this transition.
	 */
	private String[] processNames;
	
	/**
	 * The action name.
	 */
	private String actionName;
	
	/**
	 * Concrete Constructor for a KripkeTransition.
	 * 
	 * @param from the state from which this transition starts.
	 * @param to the state into which this transition ends.
	 * @param varName the name of the related variable.
	 * @param processNames the process(s) making this transition.
	 * @param actionName the name of the action for this transition (empty string or null if no action).
	 */
	public KripkeTransition(KripkeState from, KripkeState to, String varName, String[] processNames, String actionName) {
		this(from, to, varName, processNames, actionName, false);
	}
	
	/**
	 * Concrete Constructor for a KripkeTransition.
	 * 
	 * @param from the state from which this transition starts.
	 * @param to the state into which this transition ends.
	 * @param varName the name of the related variable.
	 * @param processNames the process(s) making this transition.
	 * @param actionName the name of the action for this transition (empty string or null if no action).
	 * @param isRetain if this transition is retain or not.
	 */
	public KripkeTransition(KripkeState from, KripkeState to, String varName, String[] processNames, String actionName, boolean isRetain) {
		super(from, to, varName, isRetain);
		
		this.processNames = processNames == null ? new String[0] : processNames;
		this.actionName = actionName;
		
		if(this.actionName == null) this.actionName = "";
		this.actionName = this.actionName.replace(" ", "");
		
		for(int i = 0; i < this.processNames.length; i++)
			this.processNames[i] = this.processNames[i].trim();
	}
	
	/**
	 * Gets the name of the process(s) making this transition.
	 * @return the name of the process(s) (null if unknown, empty if no process apply).
	 */
	public String[] getProcessNames() {
		return processNames;
	}
	
	/**
	 * Sets the name of the process(s) making this transition.
	 * @param processNames the names of the processes: null if unknown, empty if no process apply.
	 */
	public void setProcessNames(String[] processNames) {
		this.processNames = processNames == null ? new String[0] : processNames;
		for(int i = 0; i < this.processNames.length; i++)
			this.processNames[i] = this.processNames[i].trim();
	}
	
	/**
	 * @return Gets the action name for this transition (or null/empty string if no action).
	 */
	public String getActionName() {
		return actionName;
	}
	
	/**
	 * @return if true then this transition is an action, false otherwise.
	 */
	public boolean isAnAction() {
		return actionName != null && !actionName.isEmpty();
	}
	
	/**
	 * Computes whether this transition causes a change in the labels of the given process.
	 * @param process the process index (empty string for shared variables).
	 * @return true if it changes the process, false otherwise.
	 */
	public boolean changesProcess(String process) {
		KripkeState f = (KripkeState) super.getFrom();
		KripkeState t = (KripkeState) super.getTo();
		
		HashMap<String, ArrayList<String>> fMap = f.mapProcessToLabel(process);
		HashMap<String, ArrayList<String>> tMap = t.mapProcessToLabel(process);
		
		return !fMap.get(process).equals(tMap.get(process));
	}
	
	/**
	 * Computes whether this transition causes a change in the shared variables.
	 * @return true if it changes the shared variables, false otherwise.
	 */
	public boolean changesSharedVariables() {
		return changesProcess("");
	}
}
