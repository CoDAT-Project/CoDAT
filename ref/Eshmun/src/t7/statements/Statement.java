package t7.statements;

/**
 * Represents an abstract statement.
 * @author kinan
 *
 */
public interface Statement {
	
	/**
	 * Get the type of the statement.
	 * @return the type.
	 */
	public Types getType();
	
	/**
	 * The possible types for statements.
	 * @author kinan
	 *
	 */
	public static enum Types {
		VariableDefinition, StructureDefinition, InstanceStatement,
		For, While, If, Else,
		ClearStatement, Load, Start, Export, Extract,
		List, Del, Search
	}
}
