package t7.statements;

import java.util.Arrays;

import t7.types.StructureTemplate;

/**
 * Represents a structure definition.
 * @author kinan
 */
public class StructureDefinitionStatement implements Statement {
	/**
	 * The name of the structure.
	 */
	private String name;
	
	/**
	 * The structure defined.
	 */
	private StructureTemplate structure;
	
	/**
	 * Creates a new Structure Definition Statement.
	 * @param name the name of the structure.
	 * @param structure the structure.
	 */
	public StructureDefinitionStatement(String name, StructureTemplate structure) {
		this.name = name;
		this.structure = structure;
	}
	
	/**
	 * @return the structure name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the structure.
	 */
	public StructureTemplate getStructure() {
		return structure;
	}
	
	/**
	 * Hashes the name.
	 * @return hash code for the name.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * @return StructureDefinition.
	 */
	@Override
	public Types getType() {
		return Types.StructureDefinition;
	}
	
	/**
	 * Useful for displaying comments to user.
	 * @return The String representation of this statement.
	 */
	@Override
	public String toString() {
		String definition = structure.getDefinition();
		while(definition.contains(System.lineSeparator() + System.lineSeparator())) {
			definition = definition.replace(System.lineSeparator() + System.lineSeparator(), System.lineSeparator());
		}
		
		String str = name + "\t" + Arrays.toString(structure.getIndices()) + System.lineSeparator();
		str += definition + System.lineSeparator();
		return str;
	}
}
