package eshmun.skeletontextrepresentation;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import eshmun.Eshmun;
import eshmun.structures.AbstractState;
import eshmun.structures.AbstractTransition;
import eshmun.structures.Repairable;
import eshmun.structures.kripke.KripkeState;
import eshmun.structures.kripke.KripkeStructure;
import eshmun.structures.kripke.KripkeTransition;
import eshmun.structures.kripke.MultiKripkeStructure;

/**
 * Class responsible for converting a Kripke Model to a text represented Program
 * 
 * The method used is described in section 2.6 Synthesis of Programs in
 * Synthesis of Concurrent Programs for an Atomic Read/Write Model of
 * Computation TOPLAS01
 * 
 * It works perfectly except for the below complicated scenario with shared
 * variables :
 * 
 * 1. You import a program from text to Concrete Kripke 2. Then you extract out
 * the Program text 3. Then you try to import it as an Abstract Kripke a. It
 * won�t abstract the variables b. It will import it as a Concrete structure.
 * 
 * 
 * @author chukris
 *
 */
public class KripkeToProgramConverter {

	private KripkeStructure kripke;

	private Eshmun eshmun;

	public KripkeToProgramConverter(Repairable structure, Eshmun eshmun) {

		if (structure instanceof KripkeStructure) {
			kripke = (KripkeStructure) structure;
			this.eshmun = eshmun;
		} else {
			throw new IllegalArgumentException("Structure is of unknown type");
		}
	}

	public String convertToProgram() {

		ArrayList<String[]> transitionTable = new ArrayList<String[]>();

		ArrayList<String> intials = new ArrayList<String>();
		;

		HashSet<String> processNumbers = new HashSet<>();

		// Getting the initial labels and var assignments
		for (AbstractState sstate : this.kripke.getStartStates()) {

			KripkeState state = (KripkeState) sstate;
			intials.add(("(" + String.join(" & ", (String[]) state.getStateLabel()) + ")").replace(":=", "="));

		}

		HashSet<String> allAssignments = new HashSet<String>();

		for (AbstractState astate : this.kripke.getStates()) {

			KripkeState state = (KripkeState) astate;

			// System.out.println(state.getStateLabel());

			for (AbstractTransition outTransition : state.getOutTransition()) {
				KripkeTransition outKripke = (KripkeTransition) outTransition;

				String[] row = new String[7];

				String[] startLabels = (String[]) ((KripkeState) (outKripke.getFrom())).getStateLabel();
				String[] finishLabels = (String[]) ((KripkeState) (outKripke.getTo())).getStateLabel();

				for (int i = 0; i < finishLabels.length; i++) {
					String string = finishLabels[i];
					if (!string.contains("="))
						processNumbers.add(string.substring(string.length() - 1));
				}

				for (int i = 0; i < startLabels.length; i++) {
					String string = startLabels[i];
					if (!string.contains("="))
						processNumbers.add(string.substring(string.length() - 1));
				}

				// process number 0
				// T.start 1
				// T.finish 2
				// A 3
				// B 4

				row[1] = outKripke.getProcessNames() != null && outKripke.getProcessNames().length > 0
						? outKripke.getProcessNames()[0]
						: "1";
				int processNumber = Integer.parseInt(row[1]);
				// process number

				// Start and End State
				ArrayList<String> startStates = new ArrayList();

				for (int i = 0; i < startLabels.length; i++) {
					if (startLabels[i].endsWith(Integer.toString(processNumber)) && !startLabels[i].contains("="))
						startStates.add(startLabels[i]);
				}
				row[2] = String.join(",", startStates); // T.start

				ArrayList<String> endStates = new ArrayList();

				for (int i = 0; i < finishLabels.length; i++) {
					if (finishLabels[i].endsWith(Integer.toString(processNumber)) && !finishLabels[i].contains("="))
						endStates.add(finishLabels[i]);
				}
				row[3] = String.join(",", endStates); // T.start

				// Dealing with Assignment
				// Get Assignments
				// 1- IF there is an :=
				// 1.1 - check if there is a the same assignment => skip
				// 1.2 - else Determine the value of the new assignment => Assign it
				// 2- ElseIF Skip

				// Dictionary with all assignments values in start state
				Map<String, String> assignmentsInStartState = new HashMap<String, String>();
				// Dictionary with all assignments values in destination state
				Map<String, String> assignmentsInFinishState = new HashMap<String, String>();

				// F.Assign
				HashSet<String> assignmentsInTransition = new HashSet<String>();

				// Fill in dictionary with all assignments values in start state
				for (String string : startLabels) {
					if (string.contains(":=")) {
						assignmentsInStartState.put(string.split(":=")[0], string.split(":=")[1]);
					}
				}
				// Fill in dictionary with all assignments values in destination state
				for (String string : finishLabels) {
					if (string.contains(":=")) {
						assignmentsInFinishState.put(string.split(":=")[0], string.split(":=")[1]);
					}
				}

				for (Map.Entry<String, String> assignment : assignmentsInStartState.entrySet()) {

					if (assignmentsInFinishState.get(assignment.getKey()) == null) {
						assignmentsInTransition.add(assignment.getKey() + " := " + "null");
					} else {
						//
						// if(assignmentsInFinishState.get(assignment.getKey()).equals(assignment.getValue()))
						// {
						// assignmentsInTransition.add(assignment.getKey() + " := " + "null");
						//
						// }else {
						assignmentsInTransition
								.add(assignment.getKey() + " := " + assignmentsInFinishState.get(assignment.getKey()));
						// }

					}

				}

				for (Map.Entry<String, String> assignment : assignmentsInFinishState.entrySet()) {

					if (assignmentsInStartState.get(assignment.getKey()) == null) { // Not there set it
						assignmentsInTransition.add(assignment.getKey() + " := " + assignment.getValue());
					}

				}

				allAssignments.addAll(assignmentsInTransition);

				TreeSet<String> assignTree = new TreeSet<String>();
				assignTree.addAll(assignmentsInTransition);

				row[4] = assignTree.size() == 0 ? "skip" : assignTree.toString(); // Assignments A

				// Dealing with Guards
				ArrayList<String> guard = new ArrayList<>();
				for (int i = 0; i < startLabels.length; i++) {
					if (!startLabels[i].endsWith(Integer.toString(processNumber)) || startLabels[i].contains(":=")) {
						guard.add("(" + startLabels[i].replace(":=", "=")+")");
					}

				}

				TreeSet<String> myTreeSet = new TreeSet<String>();
				myTreeSet.addAll(guard);

				row[5] = myTreeSet.size() == 1 ? String.join(" & ", myTreeSet)
						: (String.join(" & ", myTreeSet).trim().length() > 0 ? "(" + String.join(" & ", myTreeSet) + ")"
								: "true");

				row[6] = outKripke.getActionName();
		 

				transitionTable.add(row);

			}

		}

		// After generating the transitions table, we group transitions in in families
		// according to the Def 2.3 in
		// in TOPLAS01

		String[][] transitionsArray = transitionTable.toArray(new String[][] {});

		for (int i = 0; i < transitionsArray.length; i++) {
			for (int j = i; j < transitionsArray.length; j++) {
				if ( transitionsArray[i][1].equals(transitionsArray[j][1])
						&& transitionsArray[i][2].equals(transitionsArray[j][2])
						&& transitionsArray[i][3].equals(transitionsArray[j][3])
						&& transitionsArray[i][4].equals(transitionsArray[j][4])) {

					
					transitionsArray[j][0] = i + "";
					transitionsArray[i][0] = i + "";
				}
			}
		}

		HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
		for (int i = 0; i < transitionsArray.length; i++) {

			if (!hashMap.containsKey(transitionsArray[i][0])) {
				hashMap.put(transitionsArray[i][0], transitionsArray[i]);
			} else {
				String[] addedTransition = hashMap.get(transitionsArray[i][0]);
				addedTransition[5] = addedTransition[5] + " | " + transitionsArray[i][5];
				hashMap.put(transitionsArray[i][0], addedTransition);
			}

		}
		ProgramPrinter programPrinter = new ProgramPrinter();
		programPrinter.prog.intial = String.join(" | ", intials);
		programPrinter.prog.addSharedVariables(allAssignments);
		programPrinter.prog.spec = eshmun.getSpecificationFormula();

		for (String[] strings : hashMap.values()) {
			programPrinter.AddTransition(strings[1], strings[2], strings[3], strings[4], strings[5], strings[6]);

			// for (int j = 1; j < strings.length; j++)
			// System.out.print(strings[j] + "\t");
			// System.out.println();

		}

		for (String pNum : processNumbers) {
			programPrinter.AddProcess(pNum);
		}

		// /System.out.println(gg);
		String printed = programPrinter.prog.print();

		if (allAssignments.size() > 0 && printed.contains("skip")) {

			String nullToReplaceSkip = null;
			for (String ass : allAssignments) {
				String key = ass.split(":=")[0];
				if (nullToReplaceSkip == null) {
					nullToReplaceSkip = key;
				} else {
					if (!nullToReplaceSkip.contains(key))
						nullToReplaceSkip = nullToReplaceSkip + ", " + key;
				}

			}

			nullToReplaceSkip = nullToReplaceSkip + " := null";

			printed = printed.replace("skip", nullToReplaceSkip);

		}

		return printed;
	}

	// TODO: Remove
	public void print2D(String mat[][]) {
		// Loop through all rows
		for (int i = 0; i < mat.length; i++) {
			// Loop through all elements of current row
			for (int j = 0; j < mat[i].length; j++)
				System.out.print(mat[i][j] + "\t");
			System.out.println();
		}
	}
}
