package eshmun.gui.kripke.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import eshmun.gui.kripke.StructureType;

/**
 * Represents a Save-able object, a save-able object needs to specify its type.
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public abstract class Saveable implements Serializable {
	/**
	 * Auto generated Serial UID.
	 */
	private static final long serialVersionUID = 2948770217068411896L;

	/**
	 * Gets the type of the structure stored in this save-able object.
	 * @return the type of structure.
	 */
	public abstract StructureType getStructureType();
	
	/**
	 * Loads a save-able object from a file.
	 * 
	 * @param path the file to load object from.
	 * @return save-able object stored in the given file.
	 * @throws ClassNotFoundException if the file has no valid save-able object.
	 * @throws IOException if an error occurred while reading from file.
	 */
	public static Saveable load(String path) throws ClassNotFoundException, IOException {
		FileInputStream fin = new FileInputStream(path);
		ObjectInputStream ois = new ObjectInputStream(fin);
		
		Saveable sobj = (Saveable) ois.readObject();
		
		ois.close();
		fin.close();
		
		return sobj;
	}
}
