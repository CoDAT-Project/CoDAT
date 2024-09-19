package eshmun.lts.kripke;

/**
 * OVERVIEW:an abstract class that generates a kripke structure
 * the way the structure is build is specified in the implementation
 * of generate method
 * 
 * @return
 */

public interface AbstractKripkeGenerator {

	/**
	 * EFFECTS: create the new kripke struture.
	 */
	public Kripke generate() throws Exception;
	
}
