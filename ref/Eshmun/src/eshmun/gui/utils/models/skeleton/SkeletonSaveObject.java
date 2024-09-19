package eshmun.gui.utils.models.skeleton;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.utils.models.skeleton.SkeletonState;
import eshmun.gui.utils.models.skeleton.SkeletonTransition;

/**
 * Encapsulates the information and content of a synchronization skeleton and saves it to a file.
 *
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class SkeletonSaveObject extends Saveable implements Serializable {

	/**
	 * Auto generated Serial UID
	 */
	private static final long serialVersionUID = 55548787435061395L;
		
	/**
	 * An array of labels.
	 */
	private String[] labels;
	
	/**
	 * An array of labels.
	 */
	private String[] processes;
	
	/**
	 * An array of start flags.
	 */
	private boolean[] starts;
	
	/**
	 * An array of locations.
	 */
	private Point[] locations;
	
	/**
	 * The transitions of the structure in the saveObject.
	 */
	private SkeletonTransition[] transitions;
	
	/**
	 * The structure's basename.
	 */
	private String baseName;
	
	/**
	 * Create a new SaveObject that represents the given parameters.
	 * 
	 * @param states the states in the skeletons.
	 * @param transitions the transitions in the skeletons.
	 * @param baseName the base name of the structure for generation.
	 */
	public SkeletonSaveObject(ArrayList<SkeletonState> states, ArrayList<SkeletonTransition> transitions, String baseName) {
		this.transitions = transitions.toArray(new SkeletonTransition[0]);
		
		labels = new String[states.size()];
		processes = new String[states.size()];
		starts = new boolean[states.size()];
		locations = new Point[states.size()];
		for(int i = 0; i < states.size(); i++) {
			SkeletonState s = states.get(i);
			
			labels[i] = s.getLabel();
			processes[i] = s.getProcess();
			starts[i] = s.isStart();
			locations[i] = s.getLocation();
		}
		
		this.baseName = baseName;
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
	 * Gets a the states, shallow-copied.
	 * @return the states in this save object.
	 */
	public ArrayList<SkeletonState> getStates() {
		ArrayList<SkeletonState> res = new ArrayList<SkeletonState>(labels.length);
		for(int i = 0; i < labels.length; i++) {
			SkeletonState s = new SkeletonState(labels[i], starts[i], locations[i]);
			s.setProcess(processes[i]);
			res.add(s);
		}
		
		return res;
	}
	
	/**
	 * Gets a the transitions, shallow-copied.
	 * @return the transitions in this save object.
	 */
	public ArrayList<SkeletonTransition> getTransitions() {
		ArrayList<SkeletonTransition> res = new ArrayList<SkeletonTransition>(transitions.length);
		for(SkeletonTransition e : transitions) {
			res.add(e);
		}
		
		return res;
	}

	/**
	 * Gets the basename.
	 * @return the basename.
	 */
	public String getBaseName() {
		return baseName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StructureType getStructureType() {
		return StructureType.SynchronizationSkeleton;
	}
}
