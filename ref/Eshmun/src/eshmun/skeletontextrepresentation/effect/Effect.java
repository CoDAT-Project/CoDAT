package eshmun.skeletontextrepresentation.effect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import eshmun.skeletontextrepresentation.ProgramPrinter.Program;
import eshmun.skeletontextrepresentation.State;

/**
 * Base class for Local and Global effects of an Action. The class defines
 * common variables needed in all subclasses.
 * 
 * The Effect will help in generating transitions in states.
 * 
 * @author chukrisoueidi
 *
 */
public abstract class Effect {
	
	public static final char BOTTOM = '\u2534';

	/**
	 * List to store all variables
	 */
	public LinkedList<String> variables;

	/**
	 * List to store all values assigned to variable
	 */
	public LinkedList<String> values;

	/**
	 * Initialize the lists in constructor
	 */
	public Effect() {

		variables = new LinkedList<String>();
		values = new LinkedList<String>();

	}

	/**
	 * Pushes a new variable to the variables list
	 * 
	 * We do not check if the variable exists, allowing the possibility of multiple
	 * assignments
	 * 
	 * @param designator
	 */
	public void pushVariable(String designator) {

		variables.add(designator);
	}

	/**
	 * Pushes a value to the values list To be developed later to handle more
	 * complex expressions
	 * 
	 * @param expression
	 */
	public void pushValue(String expression) {
		values.add(expression);

	}

	/**
	 * The method returns a collection of unique assignments ( variable, value) It
	 * allows multiple variable assignments for the same value.
	 * 
	 * e.g. a,b,c := 1 will return (a,1), (b,1), (c,1)
	 * 
	 * @return
	 */
	public HashMap<String, String> getEffects() {

		HashMap<String, String> effects = new HashMap<String, String>();

		// loop through all variable
		int valuesListSize = values.size();
		for (int i = 0; i < variables.size(); i++) {
			// list of values can be smaller that list of variable.
			if (i >= valuesListSize) {
				// if so push last value
				effects.put(variables.get(i), values.getLast());
			} else {
				effects.put(variables.get(i), values.get(i));
			}
		}

		return effects;
	}

	public String print() {
		
		String output = "";
		Map<String, String> effs = getEffects();
		boolean hasbeginning = false;
		for (Map.Entry<String, String> entry : effs.entrySet()) {
			
			if (!entry.getValue().equals("null")) {
				
				output = output + (hasbeginning ? ",":"") + entry.getKey() + ":= " + entry.getValue();
				hasbeginning = true;
			 
			}


		}
		return output;

	}

	/**
	 * Visitor Interface defined to accept a state and generate destination states
	 * based on an Effect
	 * 
	 * @author chukrisoueidi
	 *
	 */
	public static interface EffectsVisitor {

		public HashSet<State> visit(LocalEffect effect, State state);

		public HashSet<State> visit(GlobalEffect effect, State state);

	}

	/**
	 * An abstract class to be implemented by all Effect subclasses.
	 * 
	 * @param visitor
	 * @param state
	 * @return
	 */
	public abstract HashSet<State> accept(EffectsVisitor visitor, State state);

}
