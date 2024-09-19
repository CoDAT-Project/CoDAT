package t7.types;

/**
 * An abstract Type, all actual types implement this interface. 
 * @author kinan
 */
public interface VariableType extends Cloneable {
	/**
	 * Gets the actual type.
	 * @return the type.
	 */
	public Types getType();
	

	/**
	 * Contains the types in the data definition language.
	 * @author kinan
	 */
	public static enum Types {
		Range, Integer, Boolean, Structure
	}

	/**
	 * Clones this variable.
	 * @return a clone of this variable.
	 */
	public VariableType clone();
	
}


