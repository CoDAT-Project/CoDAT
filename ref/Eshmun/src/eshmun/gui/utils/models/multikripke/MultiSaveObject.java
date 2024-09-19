package eshmun.gui.utils.models.multikripke;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import eshmun.gui.kripke.StructureType;
import eshmun.gui.kripke.utils.Saveable;
import eshmun.gui.utils.models.vanillakripke.SaveObject;

/**
 * Represents the minimal information that needs to save and load a single KripkeStructure.
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class MultiSaveObject extends Saveable {
	/**
	 * Auto generated serial UID.
	 */
	private static final long serialVersionUID = -2718076271837136173L;

	/**
	 * The SaveObject for each of the Kripke structures in this MultiKripke structure.
	 */
	private SaveObject[] saveObjects; 
	
	/**
	 * Create a new MultiSaveObject for the given MultiKripkeStructure.
	 * @param saveObjects The SaveObject for each Kripke structures.
	 */
	public MultiSaveObject(SaveObject[] saveObjects) {
		this.saveObjects = saveObjects;
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
	 * Gets the SaveObjects of each Kripke Structure in this MultiKripkeStructure.
	 * @return the SaveObjects.
	 */
	public SaveObject[] getSaveObjects() {
		return saveObjects;
	}
	
	/**
	 * {@inheritDoc}
	 * @return StructureType.Kripke
	 */
	@Override
	public StructureType getStructureType() {
		return StructureType.MultiKripke;
	}
}
