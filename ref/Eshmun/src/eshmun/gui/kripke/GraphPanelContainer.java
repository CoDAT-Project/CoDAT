package eshmun.gui.kripke;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.JComponent;

import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.structures.Repairable;

/**
 * 
 * This class represents a GraphPanelContainer, it could be 
 * anything as long as it provides a method that returns the current GraphPanel instance in use.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public interface GraphPanelContainer {
	/**
	 * Gets the current GraphPanel instance in use.
	 * @return the current GraphPanel in use.
	 */
	public GraphPanel getCurrentGraphPanel();
	
	/**
	 * Gets the component in which the GraphPanel resides.
	 * @return the component in which the GraphPanel resides.
	 */
	public JComponent getGraphPanelComponent();
	
	/**
	 * Constructs an array of saveObject that could be used to save the structure in this Container.
	 * 
	 * <p>If this container contained a single KripkeStructure,then this would be an array of a single element,
	 * if this contained MultipKripkeStrcture then the array would contain the saveObjects related to the nested
	 * Kripke Structure.</p>
	 * 
	 * @return array of saveObject to save the structure in this GraphPanel.
	 */
	public SaveObject[] getSaveObjects();
	
	
	/**
	 * Loads the given save-able object into this GraphPanelContainer.
	 * @param saveable then save-able object to load.
	 */
	public void load(Saveable saveable);
	
	/**
	 * Returns a string that contains the name of the contained structure, if more than one structure is contained
	 * it returns a semi-comma separated string with the structure names and their processes.
	 * @return the string representation.
	 */
	public String stringRepresentation();
	
	/**
	 * Returns the type of the structure contained.
	 * @return the type of the structure.
	 */
	public StructureType getStructureType();
	
	/**
	 * Constructs the string representation / definition of the contained structure.
	 * @return the string representation of the contained structure.
	 */
	public String constructDefinition();
	
	/**
	 * Loads a structure from the given string definition.
	 * @param definition the string definition.
	 * @param structureType the type of structure in the definition.
	 * @return the specifications for this structure.
	 */
	public String loadDefinition(String definition, StructureType structureType);
	
	/**
	 * Add a Structure to the container if legal.
	 * @param name the name of the structure to add.
	 * @throws IllegalArgumentException if it is illegal.
	 */
	public void addStructure(String name);
	
	/**
	 * @return A list of all structures names inside the container. 
	 */
	public List<String> getStructureNames();
	
	/**
	 * @return All the action names referenced inside the container's structure.
	 */
	public HashSet<String> getAllActionNames();
	
	/**
	 * Constructs a Repair-able Structure that reflects the data inside the given container.
	 * @return a repair-able structure representing what is inside the container.
	 */
	public Repairable constructStructure();
	
	/**
	 * Constructs a Repair-able Structure that reflects the data <b>Currently Visible</b> inside Eshmun.
	 * This is equivalent to constructStructure when used with a KripkeGraphPanelContainer, but if it was
	 * used with a composite container, it will return the structure currently in focus inside the composite container.
	 * @return a repair-able structure representing what is inside the container.
	 */
	public Repairable constructCurrentStructure();
	
	/**
	 * Gets the specifications for this structure.
	 * @return the specifications for this structure.
	 */
	public List<String> getSpecifications();
	
	/**
	 * Sets the specifications for the current structure.
	 * @param specs the specifications for the current structure.
	 */
	public void setSpecifications(String specs);
	
	/**
	 * Applies renaming rules to the specifications if such rules were set (due to abstraction
	 * by formula).
	 * @param specifications the specifications to apply renaming to.
	 * @return the specifications renamed.
	 */
	public String applyRenameToSpec(String specifications);
		
	/**
	 * Applies the deletions given (both states and transitions) by name.
	 * @param deletions a list of names of states and transitions to delete/dash.
	 */
	public void applyDeletions(Collection<String> deletions);
	
	/**
	 * Applies the UNSAT-CORE visual effect on the given states and transitions (by var name).
	 * @param unsatCore a list of names of states and transitions to display as unsat core.
	 */
	public void applyUnsatCore(Collection<String> unsatCore);
	
	/**
	 * Applies the state-by-state visual effect on the given states.
	 * @param stateByState a list of names of states to apply the visual effect to.
	 */
	public void applyStateByState(Collection<String> stateByState);
	
	/**
	 * Restores dashed transitions and states.
	 */
	public void restore();
	
	/**
	 * Deletes the dashed transitions and states.
	 */
	public void deleteDashed();
	
	/**
	 * Abstracts the contained model by label.
	 * @param specifications the specifications of this structure to save for restoring.
	 * @param allVars variables (labels) to abstract by.
	 */
	public void abstractByLabel(String specifications, Collection<String> allVars);
	
	/**
	 * Abstracts the contained model by formula.
	 * @param specifications the specifications of this structure.
	 */
	public void abstractByFormula(String specifications);
	
	/**
	 * Abstract the contained model by a set of CTL formulae.
	 * @param specifications array of specifications used to abstract the current model.
	 */
	public void abstractByCTLFormulae(ArrayList<String> specifications);
	
	/**
	 * Concretizes the abstracted model.
	 * @param specifications the specifications of this structure.
	 * @return a collection of names of deleted states and transitions as a result of concretizing, or null if concretizing failed. 
	 */
	public Collection<String> concretize(String specifications);
	
	/**
	 * Rolls back concretization.
	 */
	public void rollback();
	
	/**
	 * Finalize concretization, register it to undo manager and
	 * display it.
	 */
	public void register();

	/**
	 * Rolls back the abstraction without applying the repair.
	 */
	public void resetAbstraction();

	
}
