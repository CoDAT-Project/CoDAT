package eshmun.gui.utils.models.vanillakripke;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.utils.Saveable;

/**
 * Represents the minimal information that needs to save and load a single KripkeStructure.
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class SaveObject extends Saveable {
	
	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 55548787435061395L;
	
	/**
	 * Name of the structure in the saveObject.
	 */
	public String structureName;
	
	/**
	 * the states of the structure in the saveObject.
	 */
	private State[] states;
	
	/**
	 * the transitions of the structure in the saveObject.
	 */
	private Transition[] transitions;
	
	/**
	 * the CTL specifications of the structure in the saveObject.
	 */
	private String specifications;
	
	/**
	 * the structural formula in the saveObject.
	 */
	private String structural;
	
	/**
	 * Create a new SaveObject that represents the given parameters.
	 * 
	 * @param structureName the structure name.
	 * @param states the states in the structure.
	 * @param transitions the transitions in the structure.
	 * @param specifications the specifications of the structure.
	 * @param structural the structural formula of the structure.
	 */
	public SaveObject(String structureName, State[] states, Transition[] transitions, String specifications, String structural) {
		this.structureName = structureName;
		this.states = states;
		this.transitions = transitions;
		this.specifications = specifications;
		this.structural = structural;
	}
		
	/**
	 * Save this object to the given file.
	 * 
	 * @param path the given file to save to.
	 * @throws IOException if an error in writing to file occur.
	 */
	public void save(String path) throws IOException {
		File f = new File(path);
		if(f.exists())
			f.delete();
		
		f.createNewFile();
		
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		oos.writeObject(this);
		
		oos.close();
		fos.close();
	}
	
	/**
	 * Gets the structure name.
	 * @return the structure name in this save object.
	 */
	public String getStructureName() {
		return structureName;
	}
	
	/**
	 * Gets a the states, shallow-copied.
	 * @return the states in this save object.
	 */
	public ArrayList<State> getStates() {
		ArrayList<State> res = new ArrayList<State>(states.length);
		for(State s : states) {
			res.add(s);
		}
		
		return res;
	}
	
	/**
	 * Gets a the transitions, shallow-copied.
	 * @return the transitions in this save object.
	 */
	public ArrayList<Transition> getTransitions() {
		ArrayList<Transition> res = new ArrayList<Transition>(transitions.length);
		for(Transition e : transitions) {
			res.add(e);
		}
		
		return res;
	}
	
	/**
	 * Sets the specifications.
	 * @param specifications the specifications of the structure as a string.
	 */
	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	
	/**
	 * Gets the specifications.
	 * @return the specifications of the structure as a string. 
	 */
	public String getSpecifications() {
		if(specifications == null)
			return "";
		
		return specifications;
	}
	
	/**
	 * Gets the structural formula.
	 * @return the structural formula as a string.
	 */
	public String getStructural() {
		return structural;
	}

	/**
	 * {@inheritDoc}
	 * @return StructureType.Kripke
	 */
	@Override
	public StructureType getStructureType() {
		return StructureType.Kripke;
	}
}
