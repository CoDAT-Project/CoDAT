package eshmun.skeletontextrepresentation.effect;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import eshmun.skeletontextrepresentation.ProgramHelper;
import eshmun.skeletontextrepresentation.State;
import eshmun.skeletontextrepresentation.effect.Effect.EffectsVisitor;

/**
 * Visitor for Effect type to separate transitions generation from the Effect
 * data structure.
 * 
 * Implements Effects visitor which implements visit function for all Effect
 * concrete classes.
 * 
 * @author chukrisoueidi
 *
 */
public class GenerateTransitionsWithNullAsSkipVisitor implements EffectsVisitor {

	/**
	 * When visiting a local effect, which only affects individual process states we
	 * replace each label(state) set to false with all labels set to true.
	 * 
	 * No transitions are generated unless some labels are set to false. To be fixed
	 * later.
	 * 
	 * We assume that in order to have a transition, a state should at least have
	 * one label changed ( set to false)
	 * 
	 * return a set of new states produced
	 */
	public HashSet<State> visit(LocalEffect effect, State state) {

		// If no effects in place
		if (effect.getEffects().isEmpty())
			return new HashSet<State>();

		HashSet<State> newStates = new HashSet<State>();

		State newState = new State(state.getLabels(), state.getVariableAssignments());

		for (Entry<String, String> entry : effect.getEffects().entrySet()) {

			if (effect.getLabelsToRemove().contains(entry.getKey()) && state.stateContainsLabel(entry.getKey())) {
				if (!effect.getLabelsToAdd().isEmpty()) {
					int i = 1;
					for (String toAdd : effect.getLabelsToAdd()) {
						if (i == 1) {
							newState = newState.replaceOrAddAtomicProposition(entry.getKey(), toAdd);
						} else
							newState = newState.addAtomicProposition(toAdd);
						i++;
					}
				} else {
					for (String toAdd : effect.getLabelsToAdd()) {
						newState = newState.addAtomicProposition(toAdd);
					}
				}
			} else if (effect.getLabelsToAdd().contains(entry.getKey()) && !state.stateContainsLabel(entry.getKey())) {

				newState = newState.addAtomicProposition(entry.getKey());
			}

		}

		if(!newState.getLabels().isEmpty()) {
		   newStates.add(newState);
		}

		// // for a removed label
		// for (String toBeRemoved : effect.getLabelsToRemove()) {
		//
		// if (state.stateContainsLabel(toBeRemoved)) {
		// // replace it with all added labels
		// for (String toAd : effect.getLabelsToAdd()) {
		// State newState = state.replaceOrAddAtomicProposition(toBeRemoved, toAd);
		// newStates.add(newState);
		// }
		// }
		//
		// }

		return newStates;
	}

	/**
	 * When visiting a global effect, which only affects shared program variables,
	 * we modify the new states to either add new variable assignments or to already
	 * assigned values.
	 * 
	 * This method will generate final destination states on each action.
	 * 
	 */
	public HashSet<State> visit(GlobalEffect effect, State state) {

		HashSet<State> resultSet = new HashSet<State>();

		// do nothing if the effect is skip or empty, just return a set with the
		// received state as is
		if (effect.getEffects().isEmpty()) {
			resultSet.add(state);
			return resultSet;
		}

		State modifiedState = state;
		
		// loop over all variable assignments
		for (Map.Entry<String, String> assignment : effect.getEffects().entrySet()) {

			// If is abstract and null --> set it to null
			//If not abstract and null --> do not touch
			//Else update
			
			if (ProgramHelper.isAbstractView && assignment.getValue().equals("null")) { // concrete and null
				modifiedState = modifiedState.addOrUpdateVariableAssignment(assignment.getKey(), assignment.getValue());
				continue;
			}
			
			
			if (!ProgramHelper.isAbstractView && assignment.getValue().equals("null")) { // concrete and null
				
				continue;
			}
			

			modifiedState = modifiedState.addOrUpdateVariableAssignment(assignment.getKey(), assignment.getValue());

			// if a state previously contains the variable assignment
//			if (state.stateContainsVariable(assignment.getKey())) {
//
//				if (assignment.getValue().equals("null") && !ProgramHelper.isAbstractView) {
//					resultSet.add(state);
//				} else {
//					resultSet.add(state.addOrUpdateVariableAssignment(assignment.getKey(), assignment.getValue()));
//				}
//
//			} else { // add the new assignments to the state
//
//				if (assignment.getValue().equals("null")) {
//
//					if (ProgramHelper.isAbstractView)
//						resultSet.add(state.addOrUpdateVariableAssignment(assignment.getKey(), assignment.getValue()));
//
//				} else {
//
//					resultSet.add(state.addOrUpdateVariableAssignment(assignment.getKey(), assignment.getValue()));
//				}
//
//			}

		}

		resultSet.add(modifiedState);
		return resultSet;
	}

}
