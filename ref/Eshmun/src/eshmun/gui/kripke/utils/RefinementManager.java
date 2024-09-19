package eshmun.gui.kripke.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import eshmun.Eshmun;
import eshmun.gui.kripke.StructureType;
import eshmun.gui.synchronizationskeletons.SynchronizationSkeletonFrame;
import eshmun.gui.utils.models.vanillakripke.SaveObject;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;

/**
 * Manages refinements (Meta-tabs)
 * @author kinan
 *
 */
public class RefinementManager extends Saveable {
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = -3417660571008753087L;
	
	/**
	 * Current Refinement
	 */
	public static RefinementManager current = new RefinementManager();

	/**
	 * Returns Structure Type
	 * @return StructureType.Refinement
	 */
	@Override
	public StructureType getStructureType() {
		return StructureType.Refinement;
	}
	
	/**
	 * The structures in the refinement.
	 */
	private ArrayList<Saveable> structures;
	
	/**
	 * The names/labels of the structures in the refinement.
	 */
	private ArrayList<String> names;
	
	/**
	 * Creates a new empty refinement Manager.
	 */
	public RefinementManager() {
		structures = new ArrayList<Saveable>();
		names = new ArrayList<String>();
		
		//Initial Empty Save.
		structures.add(new SaveObject("", new State[0], new Transition[0], "", ""));
		names.add(null);
	}
	
	/**
	 * Opens the structure at index in a new window.
	 * @param index of structure to open.
	 * @param windowSize the dimension of the window size to open.
	 */
	public void goToIndex(int index, Dimension windowSize) {
		if(structures.size() == 0) {
			Eshmun eshmun = new Eshmun(StructureType.Kripke);
			if(windowSize != null) eshmun.setSize(windowSize);
			eshmun.setVisible(true);
			return;
		}
		
		Saveable sav = structures.get(index);
		if(sav == null) {
			return;
		} else if(sav.getStructureType() == StructureType.Kripke || sav.getStructureType() == StructureType.MultiKripke) {
			Eshmun eshmun = new Eshmun(sav.getStructureType());
			eshmun.load(sav);
			if(windowSize != null) eshmun.setSize(windowSize);
			eshmun.setVisible(true);
			eshmun.setRefinementCurrent(index);
		} else if(sav.getStructureType() == StructureType.SynchronizationSkeleton) {
			SynchronizationSkeletonFrame frame = new SynchronizationSkeletonFrame(sav);
			if(windowSize != null) frame.setSize(windowSize);
			frame.setVisible(true);
			frame.setRefinementCurrent(index);
		}
	}
	
	/**
	 * Returns the size.
	 * @return size.
	 */
	public int getSize() {
		return structures.size();
	}
	
	/**
	 * Overwrite an index.
	 * 
	 * @param sav the structure to save.
	 * @param index the index of the structure.
	 * @param name the name of the structure.
	 * @throws IllegalArgumentException if index is out of bounds
	 */
	public void saveToIndex(Saveable sav, String name, int index) throws IllegalArgumentException {
		if(structures.size() == index) {
			structures.add(sav);
			names.add(name);
		} else if(structures.size() > index) {
			structures.set(index, sav);
			names.set(index, name);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Save this object to the given file.
	 * 
	 * @param path the given file to save to.
	 * @throws IOException if an error in writing to file occur.
	 */
	public void save(String path) throws IOException {
		for(int i = 0; i < structures.size(); i++) {
			if(structures.get(i) == null) {
				structures.remove(i);
				i--;
			}
		}
		
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
	 * Creates a new Structure at the given index, shifts current and following structures by one.
	 * @param index the index of the new structure.
	 */
	public void createAtIndex(int index) {
		if(index > structures.size()) index = structures.size();
		
		structures.add(index, null);
		names.add(index, null);
	}
	
	/**
	 * Removes the structure at the given index, shifts consecutive structures.
	 * @param index the index of the structure to be deleted.
	 */
	public void removeAtIndex(int index) {
		if(index >= structures.size()) return;
		
		structures.remove(index);
		names.remove(index);
	}
	
	/**
	 * Returns a copy of the structures in the refinement in order.
	 * @return a copy of the structures. 
	 */
	public Saveable[] getStructures() {
		return structures.toArray(new Saveable[0]);
	}
	
	/**
	 * Returns a copy of the labels of the structures.
	 * @return a copy of the labels (names).
	 */
	public String[] getNames() {
		return names.toArray(new String[0]);
	}
	
	/**
	 * Sets the name/label of the structure at the given index.
	 * @param index the index of the structure which name is to be changed.
	 * @param name the new name.
	 */
	public void setNameAtIndex(int index, String name) {
		names.set(index, name);
	}
	
}
