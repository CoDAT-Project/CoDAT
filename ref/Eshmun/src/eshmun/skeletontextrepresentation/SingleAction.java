package eshmun.skeletontextrepresentation;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.microsoft.z3.BoolExpr;

import eshmun.skeletontextrepresentation.effect.Effect;

import eshmun.skeletontextrepresentation.effect.GenerateTransitionsWithNullAsSkipVisitor;
import eshmun.skeletontextrepresentation.guard.Guard;
import eshmun.skeletontextrepresentation.guard.LiteralGuard;
import eshmun.skeletontextrepresentation.guard.Z3EnableStateVisitor;
import eshmun.skeletontextrepresentation.z3.GuardSolver;
import javafx.util.Pair;
import jdk.nashorn.internal.ir.annotations.Ignore;
import eshmun.skeletontextrepresentation.guard.AndGuard;
import eshmun.skeletontextrepresentation.guard.EnableStateVisitor;

/**
 * <p>
 * This class embodies a single action in a Program.
 * 
 * 
 * The Action is defined with a name and 4 properties:
 * </p>
 * <ul>
 * <li>localGuard: Boolean formula over AP_i. No guards means True</li>
 * <li>globalGuard: Boolean formulas over AP - AP_i and shared variables. No
 * guards means True</li>
 * <li>localEffect: multiple assignments; LHS are props from AP_i , RHS are
 * general boolean expressions. Skip means do nothing</li>
 * <li>globalEffect: multiple assignments; LHS is a list of shared variables ,
 * RHS are expressions with values from domain of the shared variables. Skip
 * means do nothing</li>
 * </ul>
 * 
 * @author chukrisoueidi
 *
 */
public class SingleAction {   

	/**
	 * Local Guard
	 */
	public Guard lGuard;

	/**
	 * Global Guard
	 */
	public Guard gGuard;

	/**
	 * Local Effect
	 */
	public Effect lEffect;

	/**
	 * Global Effect
	 */
	public Effect gEffect;

	/**
	 * Stores the action name
	 */
	public String name;

	/**
	 * Stores the action name
	 */
	public int processNumber;

	/**
	 * This method is called on every single action to generate all states enabled
	 * for this action. For all the state space, we iterate to build a set of states
	 * that passes the Local Guard. Then the built set is iterated again to filter
	 * by global guard.
	 * 
	 * 
	 * @param allStates
	 *            in the state space
	 * @return a set of states passing local and global guards of an action
	 */
	public HashSet<State> generateEnabledStatesByGuards(HashSet<State> allStates) {

		  HashSet<State> enabledStatesByGuards = new HashSet<State>();


		// // Visitor for evaluating if a state evaluates to true on a certain guard
		EnableStateVisitor visitor = new EnableStateVisitor();
	
		
		// // Start with Local Guards
		//
		//
		//
		HashSet<State> enabledStatesByLocalGuard = new HashSet<State>();
		for (State s : allStates) {
			
			 
		
			if (this.lGuard.accept(visitor, s)
					 && EnableStateVisitor.numberOfLabelsCheckedInGuard >= s.countOfaProcessLabels(this.processNumber))
				enabledStatesByLocalGuard.add(s);
			
			EnableStateVisitor.numberOfLabelsCheckedInGuard = 0;
		}
		
		
		
		//
		// // Then Global Guards
		//
		
		visitor = new EnableStateVisitor();
		HashSet<State> enabledStatesByGlobalGuard = new HashSet<State>();
		for (State s : enabledStatesByLocalGuard) {
		
			if (this.gGuard.accept(visitor, s))
				enabledStatesByGlobalGuard.add(s);
			// }

		}
		//
		//if (this.name != null) {
//			System.out.println();
//			System.out.println("--------------");
//			System.out.println(this.name);
//
//			System.out.println(enabledStatesByLocalGuard);
//
//			System.out.println();
//
//			System.out.println(enabledStatesByGlobalGuard);

		//}
		
		

		return enabledStatesByGlobalGuard;

	}

	/**
	 * This method is called on the action to generate all transitions allowed based
	 * on the local and global effects. Local effects will change process labels and
	 * global effects will change variable assignments.
	 * 
	 * @param all
	 *            States enabled for this action
	 * @return a collection on transitions
	 */
	public HashSet<Transition> generateTranisitons(HashSet<State> allStates) {

		// Visitor for generating transitions
		GenerateTransitionsWithNullAsSkipVisitor generateTransitionsVisitor = new GenerateTransitionsWithNullAsSkipVisitor();

		// for each state we get all possible transitions and keep it in a HashSet
		Map<State, HashSet<State>> allTransitions = new HashMap<State, HashSet<State>>();

		for (State state : allStates) {
			// local effects check for destination states
			HashSet<State> stateChanges = this.lEffect.accept(generateTransitionsVisitor, state);
			
			 
			for (State newstate : stateChanges) {
				// for each new destination state, we apply global effects
				HashSet<State> variableChanges = this.gEffect.accept(generateTransitionsVisitor, newstate);
				allTransitions.put(state, variableChanges);
			}
		}

		HashSet<Transition> generatedTransitions = new HashSet<Transition>();
		// convert the HashSet to Transitions
		for (State state : allTransitions.keySet()) {
			for (State destination : allTransitions.get(state)) {
				generatedTransitions.add(new Transition(state, destination, this.gEffect.print(), this.name));

			}
		}

		if (this.name != null) {
			//System.out.println();
			for (Transition trans : generatedTransitions) {
			//	System.out.print(trans + " ");
			}

		}

		return generatedTransitions;

	}

}
