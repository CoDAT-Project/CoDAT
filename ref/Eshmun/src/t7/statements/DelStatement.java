package t7.statements;

import java.util.List;

/**
 * Represents a Del statement, it will remove a structure from the list
 * of instantiated structure. The structure is identified by its name and indices.
 * @author kinan
 */
public class DelStatement implements Statement {
	/**
	 * The name (and indices) of the structures to delete.
	 */
	private List<String> structures;
	
	/**
	 * Creates a new del statement to delete the given structures.
	 * @param structures the name and indices of the structures to delete.
	 */
	public DelStatement(List<String> structures) {
		this.structures = structures;
	}
	
	/**
	 * @return A list of the name and indices of the structure to delete.
	 */
	public List<String> getStructures() {
		System.out.println(structures);
		return structures;
	}
	
	/**
	 * @return Del.
	 */
	@Override
	public Types getType() {
		return Types.Del;
	}
}
