package t7.types;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This is a structure template, used for instantiating concrete structures.
 * @author kinan
 *
 */
public class StructureTemplate implements VariableType {
	
	/**
	 * The left delimiter that indicates a template index.
	 */
	private static final String LEFT_DELIMITER = "[";
	
	/**
	 * The right delimiter that indicates a template index.
	 */
	private static final String RIGHT_DELIMITER = "]";
	
	/**
	 * The template process indices.
	 */
	private String[] indices;
	
	/**
	 * The base name of the template.
	 */
	private String name;
	
	/**
	 * Counts the number of instances, used to generate
	 * unique structure names.
	 */
	private int counter;
	
	/**
	 * The template definition.
	 */
	private String definition;
	
	/**
	 * Flags whether this structure definition is ordered.
	 * An order structure definition means that processes in it are not isomorphic,
	 * in other words, structure&lt;i1, i2, ..&gt; is not equivalent to structure&lt;i2, i1, ..&gt;.
	 * If the structure is not ordered, then the indices will be always re-arranged such that
	 * the smaller index (according to the natural ordering of strings in java) will be first,
	 * Which is useful in order to avoid duplication. If the structure is not ordered, then 
	 * the indices will not be re-arranged, and the same structure with different arrangement of
	 * indices will not be considered as a duplicate.
	 */
	private boolean order;
	
	/**
	 * Creates a new template for a structure.
	 * @param name the base name of the template.
	 * @param indices the symbolic indices the structure takes (in order of replacement).
	 * @param definition the definition of the structure.
	 * @param order if true, then the template represents an a-symmetric structure (i.e. changing the 
	 * process indices order resulting in a different structure), false otherwise.
	 */
	public StructureTemplate(String name, String[] indices, String definition, boolean order) {
		this.name = name.trim();
		this.counter = 0;
		
		this.update(indices, definition, order);
	}
	
	/**
	 * @return the base name of the template.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the indices to be replaced in this template.
	 */
	public String[] getIndices() {
		return indices;
	}
	
	/**
	 * @return the structure definition.
	 */
	public String getDefinition() {
		return definition;
	}
	
	/**
	 * @return the count of instances created from this template. 
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * @return whether this structure definition is ordered or not.
	 */
	public boolean isOrdered() {
		return order;
	}
	
	/**
	 * Updates the template to the given template.
	 * Useful for when the template name is used again with a new define statement.
	 * Preserves the counter to preserve unique names for generated structures.
	 * @param indices the symbolic indices the structure takes (in order of replacement).
	 * @param definition the definition of the structure.
	 * @param order if true, then the template represents an a-symmetric structure (i.e. changing the 
	 * process indices order resulting in a different structure), false otherwise.
	 */
	public void update(String[] indices, String definition, boolean order) {
		this.indices = indices;
		this.definition = definition;
		this.order = order;
		
		for(int i = 0; i < this.indices.length; i++) {
			this.indices[i] = this.indices[i].trim();
		}
	}
	
	/**
	 * Instantiate a new structure from this template. The indices that were provided in the
	 * template definition will be replaced by the given replacements (in order).
	 * @param replacements the indices to use as replacements.
	 * @return the new structure definition, null if two indices (or more) were equal.
	 * @throws IllegalArgumentException if the replacements count does not match the defined indices count.
	 */
	public Structure instantiate(String[] replacements) throws IllegalArgumentException {
		int expected = indices.length;
		int found = replacements.length;
		
		if(replacements.length != indices.length) {
			throw new IllegalArgumentException("Cannot create instance of '"+name+"', expected "+expected+" indices, given "+found);
		}
		
		String[] copyOfReplacements = Arrays.copyOf(replacements, replacements.length);
		Arrays.sort(copyOfReplacements, new Comparator<String>() { //Shortlex ordering.
			@Override
			public int compare(String o1, String o2) {
				if(o1.length() != o2.length()) 
					return o1.length() - o2.length();
				
				return o1.compareTo(o2);
			}
		});
		
		for(int i = 0; i < copyOfReplacements.length - 1; i++) {
			if(copyOfReplacements[i].equals(copyOfReplacements[i+1]))
				return null;
		}
		
		if(order) copyOfReplacements = replacements;
			
		String header = "(";
		String structure = definition;
		for(int i = 0; i < expected; i++) {
			String token = LEFT_DELIMITER + indices[i] + RIGHT_DELIMITER;			
			structure = structure.replace(token, copyOfReplacements[i].trim());
			
			if(i < expected - 1) {
				header += copyOfReplacements[i].trim() + ", ";
			} else {
				header += copyOfReplacements[i].trim() + ")";
			}
		}
		
		counter++;
		String sName = name + header;
		return new Structure(sName, name + counter + header + System.lineSeparator() + structure);
	}

	/**
	 * @return Structure.
	 */
	@Override
	public Types getType() {
		return Types.Structure;
	}
	
	/**
	 * Clones this structure template.
	 * @return a clone of this template.
	 */
	@Override
	public StructureTemplate clone() {
		StructureTemplate clone = new StructureTemplate(name, indices, definition, order);
		clone.counter = counter;
		return clone;
	}
}
