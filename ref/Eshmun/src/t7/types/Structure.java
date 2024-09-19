package t7.types;

/**
 * Represents a structure that was already instantiated.
 * @author kinan
 *
 */
public class Structure implements Comparable<Structure> {
	/**
	 * The structure's name and indices.
	 */
	private String name;
	
	/**
	 * The structure's definition.
	 */
	private String definition;
	
	/**
	 * Creates a new Structure.
	 * @param name the structure's name.
	 * @param definition the structure's definition.
	 */
	public Structure(String name, String definition) {
		this.name = name;
		this.definition = definition;
	}
	
	/**
	 * @return The structure's name (and indices).
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The structure's definition.
	 */
	public String getDefinition() {
		return definition;
	}
	
	/**
	 * Hashes the structure by hashing its name (and indices).
	 * @return a Hash code representing the structure.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Two structures are equal if they have the same name (and indices).
	 * @param obj an object to check equality with.
	 * @return true if this and obj are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Structure) {
			return name.equals(((Structure) obj).name);
		}
		
		return false;
	}
	
	/**
	 * Compares two structures by comparing their name (shortlex order).
	 * @param o the structure to compare to.
	 * @return 0 if they are equal, a negative value if this is smaller, 
	 * a positive value otherwise.
	 */
	@Override
	public int compareTo(Structure o) {
		String name1 = getName();
		String name2 = o.getName();
		
		//Fetch base names, and compare them lexicographically.
		String base1 = name1.substring(0, name1.indexOf("(")).trim();
		String base2 = name2.substring(0, name2.indexOf("(")).trim();
		
		int comp = base1.compareTo(base2);
		if(comp != 0) return comp;
		
		//If base names are equal, compare the indices.
		name1 = name1.substring(name1.indexOf("(")+1, name1.indexOf(")"));
		name2 = name2.substring(name2.indexOf("(")+1, name2.indexOf(")"));
		
		String[] indices1 = name1.split(",");
		String[] indices2 = name2.split(",");
		
		//if different length of indices, lesser comes first.
		if(indices1.length != indices2.length)
			return indices1.length - indices2.length;
		
		//both indices counts are equal.
		for(int i = 0; i < indices1.length; i++) {
			String i1 = indices1[i].trim();
			String i2 = indices2[i].trim();
			
			//compare each pair of indices in shortlex order.
			if(i1.length() != i2.length()) 
				return i1.length() - i2.length();
			
			comp = i1.compareTo(i2);
			if(comp != 0) return comp;
		}
		
		//if every compare yielded 0, then the two are equal.
		return 0;
	}
}
