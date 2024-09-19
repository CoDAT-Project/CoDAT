package eshmun.skeletontextrepresentation.effect;

import java.util.HashSet;

import eshmun.skeletontextrepresentation.State;


/**
 * The class GlobalEffect is an Efefct subtype.
 * 
 * Semantically, a global effect is a set of multiple assignments; LHS is a list of shared variables ,
 * RHS are expressions with values from domain of the shared variables. Skip means do nothing
 * 
 * @author chukrisoueidi
 *
 */
public class GlobalEffect extends Effect {

	/**
	 * Instantiates a new global effect.
	 */
	public GlobalEffect() {
		super();

	}

	/**
	 * Should implement the accept visitor that will call the visitors implementation.
	 */
	@Override
	public HashSet<State> accept(EffectsVisitor visitor, State state) {
		return visitor.visit(this, state);
	}

}
