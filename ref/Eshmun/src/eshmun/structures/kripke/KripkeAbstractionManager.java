package eshmun.structures.kripke;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;

/**
 * Manages Concretization of an abstracted Kripke Structure 
 * into the original kripke structure.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class KripkeAbstractionManager {
	/**
	 * The abstracted kripke structure this manager manages.
	 */
	private KripkeStructure abstractKripke;
	
	/**
	 * The old Kripke Structure, in a format that preserves locations and display.
	 */
	private SaveObject oldKripke;
	
	/**
	 * Maps abstracted transitions to old transitions by variable name.
	 */
	private HashMap<String, HashSet<String>> transitionsMap;
			
	/**
	 * Maps abstracted states to old states by variable name.
	 */
	private HashMap<String, HashSet<String>> statesMap;
	
	/**
	 * Create a new Manager for the given abstractKripke.
	 * @param abstractKripke the abstracted Kripke Structure.
	 * @param oldKripke The old Kripke Structure.
	 * @param transitionsMap a Mapping from abstracted transitions to the old transitions by variable name.
	 * @param statesMap a Mapping from abstracted states to the old states by variable name.
	 */
	public KripkeAbstractionManager(KripkeStructure abstractKripke, SaveObject oldKripke, 
			HashMap<String, HashSet<String>> transitionsMap, HashMap<String, HashSet<String>> statesMap) {
		
		this.abstractKripke = abstractKripke;
		this.oldKripke = oldKripke;
		this.transitionsMap = transitionsMap;
		this.statesMap = statesMap;
	}
	
	/**
	 * Gets the abstracted kripke structure.
	 * @return the abstracted Kripke structure.
	 */
	public KripkeStructure getAbstractKripke() {
		return abstractKripke;
	}
	
	/**
	 * Gets the old Kripke structure.
	 * @return the old (original) kripke structure.
	 */
	public SaveObject getOldKripke() {
		return oldKripke;
	}
	
	/**
	 * Gets the equivalent deletions in the original structure.
	 * @param deletions a collection of variables to be deleted in the abstracted structure.
	 * @return an equivalent collection of variable names to be deleted in the original structure.
	 */
	public Collection<String> concretizeDeletions(Collection<String> deletions) {
		LinkedList<String> newDeletions = new LinkedList<String>();
		for(String d : deletions) {			
			if(d.startsWith(State.STATE_PREFIX)) {
				HashSet<String> set = statesMap.get(d);
				if(set != null) {
					newDeletions.addAll(set);
				}
			} else if(d.startsWith(Transition.TRANSITION_PREFIX)) {
				HashSet<String> set = transitionsMap.get(d);
				if(set != null) {
					newDeletions.addAll(set);
				}
			}
		}
		
		return newDeletions;
	}
}
