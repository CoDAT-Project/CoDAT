package t7.statements;

import java.util.HashSet;

/**
 * Represents an Export statement, it will get the structures from eshmun, and add
 * their definition to the workspace.
 */
public class ExtractStatement implements Statement {
	/**
	 * If true, then the extract should treat the structures as a-symmetric, i.e.
	 * the order of the processes matter, and switching the process index results in
	 * a different structure.
	 */
	private boolean ordered;
	
	/**
	 * The name (and indices) of the structures to delete.
	 */
	private HashSet<String> structures;
	
	/**
	 * Creates a new extract statement to extract the given structures from Eshmun.
	 * @param structures the name and indices of the structures to extract.
	 * @param ordered if the order of process indices matters (structure is not symmetric).
	 */
	public ExtractStatement(HashSet<String> structures, boolean ordered) {
		this.structures = structures;
		this.ordered = ordered;
	}
	
	/**
	 * @return A set of structures to extract.
	 */
	public HashSet<String> getStructures() {
		return structures;
	}
	
	/**
	 * @return true if the statement should treat structures as a-symmetric. 
	 * false otherwise.
	 */
	public boolean isOrdered() {
		return ordered;
	}
	
	/**
	 * @return Extract.
	 */
	@Override
	public Types getType() {
		return Types.Extract;
	}
}
