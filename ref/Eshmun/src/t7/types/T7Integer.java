package t7.types;

/**
 * An Integer Type. 
 * @author kinan
 */
public class T7Integer implements VariableType {
	/**
	 * The value of the integer.
	 */
	private int value;
	
	/**
	 * Creates a new Integer with the given type.
	 * @param value the value.
	 */
	public T7Integer(int value) {
		this.value = value;
	}
	
	/**
	 * @return Integer.
	 */
	@Override
	public Types getType() {
		return Types.Integer;
	}
	
	/**
	 * Gets the value of this integer.
	 * @return the value.
	 */
	public int value() {
		return value;
	}
	
	/**
	 * Sets the value of this integer.
	 * @param value the new value.
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * Clones this variable.
	 * @return a clone of this variable.
	 */
	@Override
	public T7Integer clone() {
		return new T7Integer(value);
	}
	

	/**
	 * Useful for displaying comments to user.
	 * @return The String representation of this type.
	 */
	public String toString() {
		return value+"";
	}
}
