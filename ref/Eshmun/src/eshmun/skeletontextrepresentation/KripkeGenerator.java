package eshmun.skeletontextrepresentation;

import java.util.ArrayList;
import java.util.HashSet;

import org.stringtemplate.v4.compiler.CodeGenerator.subtemplate_return;

import eshmun.skeletontextrepresentation.guard.EnableStateVisitor;
import eshmun.skeletontextrepresentation.infinitespace.StringHelpers;
import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateActionFactory;
import eshmun.skeletontextrepresentation.infinitespace.structures.InfiniteState;
import eshmun.skeletontextrepresentation.infinitespace.structures.InfiniteStateTransition;


/**
 * This class is needed to generate a Kripke Structure script based on the
 * specification defined in Eshmun.
 * 
 * <pre>
 *  
  NAME < SYMB1, SYMB2, .. > { 
	states:
	NAME:LABELS[:START_STATE[:RETAIN]];
		
	transitions:
	FROM_NAME:TO_NAME[:RETAIN[:ACTION_NAMES(COMMA-SEPARATED)]];
		
	specifications:
		CTL FORMULA
	}
 * </pre>
 * 
 * @author chukrisoueidi
 *
 */
public class KripkeGenerator {
	/**
	 * This method builds a string from received collection of states and
	 * transitions. The function checks all generated transitions. It deletes all
	 * transitions that belong to states that are not considered valid Kripke states
	 * (i.e has no Incoming edge)
	 * 
	 * @param all
	 *            Kripke States
	 * @param all
	 *            Kripke Transitions
	 * @return a StringBuilder with Kripke string
	 */
	public static StringBuilder generateKripkeString(ArrayList<State> validStatesArray,
			HashSet<Transition>  programTransitions) {

		// validTransitions to be filled after the for loop
		HashSet<String> validTransitions = new HashSet<String>();

		for (Transition trans : programTransitions) {

//			// split the transition in source and destination
//			String[] nodes = StringHelpers.removeNullsAndCommas(trans).split("-->");
//
//			String source = nodes[0]; // source state
//			String destination = nodes[1]; // destination state

			// Delete if the states involved in the transition are in valid states array
			if (validStatesArray.contains(trans.getSource()) && validStatesArray.contains(trans.getDestination())) {

				int indexOfS1 = validStatesArray.lastIndexOf(trans.getSource())+1 ;
				int indexOfS2 = validStatesArray.lastIndexOf(trans.getDestination())+1;

				String descriptor = trans.getActionNumber().equals("") ? trans.getEffects():   (trans.getEffects().equals("") ? trans.getActionNumber(): trans.getActionNumber()+"," + trans.getEffects());
				// prepare for a Kripke transition e.g. S1:S2
				String tranisitonName = "S" + indexOfS1 + ":S" + indexOfS2+ ":false:"+ descriptor+ ";";

				validTransitions.add(tranisitonName);
			}

		}

		StringBuilder sb = new StringBuilder();

		String numberOfProcesses = ProgramHelper.numberOfProcesses.toString().replace("[", "")
				.replace("]", "");
		//to generate process indexes string 
		

		sb.append("importedprog(" + numberOfProcesses + ")" + System.lineSeparator());
		sb.append("states:" + System.lineSeparator());

		for (State state : validStatesArray) {

			//Check if state is an initial state by calling the the Guard visitor
			EnableStateVisitor visitor = new EnableStateVisitor();
			 String starter = ProgramHelper.initialGuard.accept(visitor, state) ? "true" : "false";
			//String starter =   "true"  ;
			// states should be defined in the following spec
			// NAME:LABELS[:START_STATE[:RETAIN]];
			 int index= validStatesArray.indexOf(state)+1;
			sb.append("S" + index  + ":" 
						+ (ProgramHelper.isAbstractView ? 
								state.toStringWithoutNull().replace("=", ":="): state.toStringWithoutNull().replace("=", ":="))
						+ ":" + starter + ":false;"	+ System.lineSeparator());

		}

		sb.append("transitions:" + System.lineSeparator());

		for (String trans : validTransitions) {
			// transitions already has the structure S1:S2 constructed above
			// transitions should be defined in the following spec.
			// FROM_NAME:TO_NAME [:RETAIN[:ACTION_NAMES(COMMA-SEPARATED)]] ;
			sb.append(trans  + System.lineSeparator());
		}
		sb.append("specifications:" + System.lineSeparator());
		return sb;
	}


	public static StringBuilder generateInfiniteStateKripkeString(ArrayList<InfiniteState> validStatesArray,
			HashSet<InfiniteStateTransition>  programTransitions) {

		// validTransitions to be filled after the for loop
		HashSet<String> validTransitions = new HashSet<String>();

		for (InfiniteStateTransition trans : programTransitions) {

//			// split the transition in source and destination
//			String[] nodes = StringHelpers.removeNullsAndCommas(trans).split("-->");
//
//			String source = nodes[0]; // source state
//			String destination = nodes[1]; // destination state

			// Delete if the states involved in the transition are in valid states array
			if (validStatesArray.contains(trans.sourceState) && validStatesArray.contains(trans.destinationState)) {

				int indexOfS1 = validStatesArray.lastIndexOf(trans.sourceState)+1 ;
				int indexOfS2 = validStatesArray.lastIndexOf(trans.destinationState)+1;

				String actionName= trans.actionNumber  +"";
				// prepare for a Kripke transition e.g. S1:S2
 				String tranisitonName = "S" + indexOfS1 + ":S" + indexOfS2+ ":false:"
				+ actionName+":"
 						+ trans.getCommandForTextExport()+ ";";
				
				//String tranisitonName = "S" + indexOfS1 + ":S" + indexOfS2+ ":false:a1t3:"+ trans.getCommandForTextExport()+ ";";


				validTransitions.add(tranisitonName);
			}

		}

		StringBuilder sb = new StringBuilder();

		String numberOfProcesses = InfiniteStateActionFactory.processNumbers.toString().replace("[", "")
				.replace("]", "");
		//to generate process indexes string 
		

		sb.append("importedprog(" + numberOfProcesses + ")" + System.lineSeparator());
		sb.append("states:" + System.lineSeparator());

		for (InfiniteState state : validStatesArray) {

			//Check if state is an initial state by calling the the Guard visitor
			EnableStateVisitor visitor = new EnableStateVisitor();
			 String starter = state.isStart? "true" : "false";
					 //ProgramHelper.initialGuard.accept(visitor, state) ? "true" : "false";
			//String starter =   "true"  ;
			// states should be defined in the following spec
			// NAME:LABELS[:START_STATE[:RETAIN]];
			 int index= validStatesArray.indexOf(state)+1;
			sb.append("S" + index  + ":" 
						+  state.getLabels()
						+ ":" + starter + ":false:" 
						+ state.toString()
						//+ StringHelpers.cleanFromBools(state.toString(), InfiniteStateActionFactory.allAPs) 
						+";"	+ System.lineSeparator());

		}

		sb.append("transitions:" + System.lineSeparator());

		for (String trans : validTransitions) {
			// transitions already has the structure S1:S2 constructed above
			// transitions should be defined in the following spec.
			// FROM_NAME:TO_NAME [:RETAIN[:ACTION_NAMES(COMMA-SEPARATED)]] ;
			sb.append(trans  + System.lineSeparator());
		}
		sb.append("specifications:" + System.lineSeparator());
		return sb;
	}
	
	
}
