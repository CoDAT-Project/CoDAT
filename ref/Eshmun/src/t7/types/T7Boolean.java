package t7.types;

/**
 * A Boolean type.
 * @author kinan
 */
public class T7Boolean implements VariableType {
	/**
	 * The value of the boolean
	 */
	private boolean value;
	
	/**
	 * Creates a new Boolean with the given type.
	 * @param value the value.
	 */
	public T7Boolean(boolean value) {
		this.value = value;
	}
	
	/**
	 * @return Boolean.
	 */
	@Override
	public Types getType() {
		return Types.Boolean;
	}
	
	/**
	 * Gets the value of this boolean.
	 * @return the value.
	 */
	public boolean value() {
		return value;
	}
	
	/**
	 * Sets the value of this boolean.
	 * @param value the new value.
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
	
	/**
	 * Clones this variable.
	 * @return a clone of this variable.
	 */
	@Override
	public T7Boolean clone() {
		return new T7Boolean(value);
	}
	

	/**
	 * Useful for displaying comments to user.
	 * @return The String representation of this type.
	 */
	public String toString() {
		return value+"";
	}
}
