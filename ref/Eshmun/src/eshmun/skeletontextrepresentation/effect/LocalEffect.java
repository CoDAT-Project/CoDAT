package eshmun.skeletontextrepresentation.effect;

import java.util.HashSet;
import java.util.Map;

import eshmun.skeletontextrepresentation.State;

/**
 * The LocalEffect is an Effect subtype.
 * 
 * Semantically, a local effect is a set of multiple assignments; LHS are props
 * from AP_i , RHS are general boolean expressions. Skip means do nothing
 * 
 * @author chukrisoueidi
 *
 */
public class LocalEffect extends Effect {

	/** The Constant TRUE. */
	public static final String TRUE = "tt";

	/** The Constant FALSE. */
	public static final String FALSE = "ff";

	/**
	 * Instantiates a new local effect.
	 */
	public LocalEffect() {
		super();

	}

	/**
	 * This method gets all labels (individual process states) that are set to
	 * false.
	 *
	 * @return the labels to remove
	 */
	public HashSet<String> getLabelsToRemove() {

		HashSet<String> toRemove = new HashSet<String>();
		Map<String, String> assignments = this.getEffects();

		for (String key : assignments.keySet()) {
			if (assignments.get(key).equals(FALSE)) {
				toRemove.add(key.trim());
			}
		}

		return toRemove;
	}

	/**
	 * This method gets all labels (individual process states) that are set to true.
	 *
	 * @return the labels to add
	 */
	public HashSet<String> getLabelsToAdd() {

		HashSet<String> toAdd = new HashSet<String>();
		Map<String, String> assignments = this.getEffects();

		for (String key : assignments.keySet()) {
			if (assignments.get(key).equals(TRUE)) {
				toAdd.add(key.trim());
			}
		}

		return toAdd;
	}

	/**
	 * Should implement the accept visitor that will call the visitors
	 * implementation.
	 */
	public HashSet<State> accept(EffectsVisitor visitor, State state) {
		return visitor.visit(this, state);
	}
}
