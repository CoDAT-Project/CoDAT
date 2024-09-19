package eshmun.skeletontextrepresentation;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.microsoft.z3.Context;
import com.microsoft.z3.FuncInterp.Entry;
import com.sun.org.apache.xpath.internal.operations.And;

import eshmun.skeletontextrepresentation.guard.AndGuard;
import eshmun.skeletontextrepresentation.guard.EnableStateVisitor;
import eshmun.skeletontextrepresentation.guard.Guard;
import eshmun.skeletontextrepresentation.guard.LabelGuard;
import eshmun.skeletontextrepresentation.guard.Z3EnableStateVisitor;
import eshmun.skeletontextrepresentation.z3.GuardSolver;
 

/**
 * The class helps the Parser to fill in all parsed actions then helps in
 * generating the Kripke Structure. All members are static.
 * 
 * 
 * @author chukris
 *
 */
public class ProgramHelper {

	/**
	 * The number of processes defined in the program
	 */
	public  static HashSet<Integer> numberOfProcesses = new HashSet<Integer>();
	 

	/**
	 * Holds all Labels defined in a program: Atomic propositions like N1,T1 and
	 * variable checks like x=1
	 */
	public static HashSet<String> allProgramLabels = new HashSet<String>();

	public static int numbercheckedLabels = 0;

	/**
	 * A collection of actions that are populated by the ProgramParser
	 */
	public static ArrayList<SingleAction> actions = new ArrayList<SingleAction>();

	/**
	 * A collection of actions that are populated by the ProgramParser
	 */
	private static HashSet<State> programStates = new HashSet<State>();

	/**
	 * Used by parser to populate a single action
	 */
	public static SingleAction currentAction = new SingleAction();;

	/**
	 * Holds guard that defines initial states
	 */
	public static Guard initialGuard;

	/**
	 * Holds all possible states from labels
	 */
	private static HashSet<String> allStates;

	/**
	 * Holds Domain of Shared Variables
	 */

	public static Map<String, HashSet<String>> sharedVariables = new HashMap<String, HashSet<String>>();

	public static String ctlSpec;

	public static boolean isAbstractView = true;

	/**
	 * Getter for allStates
	 * 
	 * @return
	 */
	public static HashSet<State> getAllStates() {

		// if allStates is empty call generateStateSpace() to get the collection
		if (programStates.isEmpty() || programStates == null) {
			programStates = generateStateSpace();
		}
		return programStates;
	};

	/**
	 * Resets all static variables. Should be called before parsing
	 */
	public static void resetFactory() {
		numberOfProcesses = new HashSet<>();
		actions = new ArrayList<SingleAction>();
		currentAction = new SingleAction();
		allProgramLabels = new HashSet<String>();
		allStates = new HashSet<String>();
		sharedVariables = new HashMap<String, HashSet<String>>();
		programStates = new HashSet<State>();
		ctlSpec = null;
	}

	/**
	 * Pushes a new action to the actions collection and resets current action
	 */
	public static void newAction() {

		if (currentAction != null)
			actions.add(currentAction);

		currentAction = new SingleAction();

	}

	/**
	 * This method helps in retrieving all labels belonging to a process by it's
	 * index i.e Individual process states Variable checks are excluded
	 * 
	 * @param index
	 * @return
	 */
	private static HashSet<String> getLabelsOfProcess(Integer index) {

		HashSet<String> labelsByIndex = new HashSet<String>();
		 for (String ap : allProgramLabels) {
		 if (ap.endsWith(index.toString()) && !ap.contains("="))
		 labelsByIndex.add(ap);
		 }
		
		

		for (SingleAction action : actions) {

			if (action.processNumber == index) {
				// Only check Label Guard and And Guards TODO: want to make this more expressive
				
				if (action.lGuard instanceof LabelGuard) {
					
					LabelGuard lGuard = (LabelGuard) action.lGuard;
					labelsByIndex.add(lGuard.label);
					
				} else if (action.lGuard instanceof AndGuard) {

					AndGuard andGuard = (AndGuard) action.lGuard;
					SortedSet<String> result = new TreeSet<String>();
					for (Guard g : andGuard.getChildren()) {
						if (g instanceof LabelGuard) {
							
							result.add(((LabelGuard)g).label);
						}
					}
					labelsByIndex.add(String.join(",", result));
				}

			}
		}

		// Check all local guards for And Guard

		// HashSet<String> combos = new HashSet<>();
		//
		// String arr[] = labelsByIndex.toArray(new String[labelsByIndex.size()]);
		//
		// for (int i = 2; i < labelsByIndex.size(); i++) {
		// int r = i;
		// int n = labelsByIndex.size();
		// combos.addAll(printCombination(arr, n, r));
		// }
		//
		// labelsByIndex.addAll(combos);

		return labelsByIndex;
	}

	/**
	 * Returns a list of all possible variable checks. For each variable check e.g.
	 * x=1, add a new check x=null
	 * 
	 * @return
	 */
	public static Map<String, HashSet<String>> getAllVariableChecks() {

		// if(sharedVariables.size() == 0) {
		//
		// Map<String, HashSet<String>> indexedAPs = new HashMap<String,
		// HashSet<String>>();
		//
		//
		// for (String ap : allProgramLabels) {
		// if (ap.contains("=")) {
		// //indexedAPs.add(ap);
		//
		// indexedAPs
		//
		// // String[] parts = ap.split("=");
		// //indexedAPs.add(parts[0].trim() + "=null");
		// }
		//
		// }
		// return indexedAPs;
		//
		// }else {
		return sharedVariables;
		// }

	}

	/**
	 * We group labels by process # and then generate all possible combinations.
	 * After that for all possible combined states, we create for each variable
	 * assignment a new state by appending the variable check to it.
	 * 
	 * @return
	 */
	private static HashSet<State> generateStateSpace() {

		// Set to be returned
		HashSet<State> resultSet = new HashSet<State>();

		// All possible variable checks in the program
		// HashSet<String> variableChecks = getAllPossibleVariableChecks();
		Map<String, HashSet<String>> variableChecks = getAllVariableChecks();

		// variableChecks.add("null");

		// for each process get unique states and fill them in List of Lists
		List<List<String>> listOfAPsPerProcess = new ArrayList<List<String>>();
		for (Integer ind : numberOfProcesses) {
			listOfAPsPerProcess.add(new ArrayList<String>(getLabelsOfProcess(ind)));
		}
		 

		// Call cartesianProduct to get all possible combinations of states
		List<List<String>> productOfAllStates = cartesianProduct(listOfAPsPerProcess);

		// Now add the set of variables

		List<List<Map.Entry<String, String>>> listOfAllVariableAssignments = new ArrayList<List<Map.Entry<String, String>>>();
		for (Map.Entry<String, HashSet<String>> v : variableChecks.entrySet()) {

			HashSet<Map.Entry<String, String>> varsToAdd = new HashSet<Map.Entry<String, String>>();
			for (String val : v.getValue()) {
				varsToAdd.add(Pair.of(v.getKey(), val));
			}
			listOfAllVariableAssignments.add(new ArrayList<>(varsToAdd));
		}
		List<List<Map.Entry<String, String>>> productOfAllVariableAssignments = cartesianProduct(
				listOfAllVariableAssignments);

		for (List<String> apl : productOfAllStates) {
			
			
			Collections.sort(apl, new Comparator<String>() 
			  {
			    public int compare(String str1, String str2) 
			    {                 
			       String substr1 = str1.substring(str1.length()-2);
			       String substr2 = str2.substring(str2.length()-2);
			                
			       return String.valueOf(substr1).compareTo(String.valueOf(substr2));                           
			    }
			  });
			
			
			Collections.sort(apl, new Comparator<String>() 
			  {
			    public int compare(String str1, String str2) 
			    {                 
			       String substr1 = str1.substring(str1.length()-1);
			       String substr2 = str2.substring(str2.length()-1);
			                
			       return String.valueOf(substr1).compareTo(String.valueOf(substr2));                           
			    }
			  });

			
			
			

			// Convert the list of states to a comma separated String
			String toAdd = String.join(",", apl);

			State newState = new State(toAdd);

			if (productOfAllVariableAssignments.size() == 0) {
				resultSet.add(newState);

			} else {

				for (List<Map.Entry<String, String>> pairsList : productOfAllVariableAssignments) {
					for (Map.Entry<String, String> pair : pairsList) {
						newState = newState.addOrUpdateVariableAssignment(pair.getKey(), pair.getValue());
					}
					resultSet.add(newState);

				}
			}

		}

		return resultSet;

	}

	static SortedSet<String> combinationUtil(String arr[], String data[], int start, int end, int index, int r,
			SortedSet<String> all) {
		// Current combination is ready to be printed, print it
		if (index == r) {
			SortedSet<String> result = new TreeSet<String>();
			for (int j = 0; j < r; j++)
				result.add(data[j]);
			all.add(String.join(",", result));
			return all;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			all.addAll(combinationUtil(arr, data, i + 1, end, index + 1, r, all));
		}

		return all;
	}

	static SortedSet<String> printCombination(String arr[], int n, int r) {
		// A temporary array to store all combination one by one
		String data[] = new String[r];

		// Print all combination using temprary array 'data[]'
		return combinationUtil(arr, data, 0, n - 1, 0, r, new TreeSet<String>());
	}

	/**
	 * The method below creates the Cartesian product of a list of list of strings:
	 * cartesianProduct(Arrays.asList(Arrays.asList("N1", "C1"), Arrays.asList("N2",
	 * "T2", "C2")))
	 * 
	 * would yield this
	 * 
	 * [[N1, N2], [N1, T2], [N1, C2], [C1, N2], [C1, T2], [C1, C2]]
	 * 
	 * @param lists
	 * @return
	 */
	private static <T> List<List<T>> cartesianProduct(List<List<T>> lists) {
		List<List<T>> resultLists = new ArrayList<List<T>>();
		if (lists.size() == 0) {
			resultLists.add(new ArrayList<T>());
			return resultLists;
		} else {
			List<T> firstList = lists.get(0);
			List<List<T>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
			for (T condition : firstList) {
				for (List<T> remainingList : remainingLists) {
					ArrayList<T> resultList = new ArrayList<T>();
					resultList.add(condition);
					resultList.addAll(remainingList);
					resultLists.add(resultList);
				}
			}
		}
		return resultLists;
	}

	/**
	 * This is a recursive function that deletes invalid states i.e with no incoming
	 * edges.
	 * 
	 * On each iteration it removes invalid states which in turn makes other states
	 * invalid.
	 * 
	 * The function keeps recursively calling itself until no states are being
	 * deleted.
	 * 
	 * @param all
	 *            states
	 * @param deleted
	 *            states
	 * @param transitions
	 * @return all valid states
	 */
	public static HashSet<State> deleteStatesWithNoIncomingEdge(HashSet<State> states, HashSet<State> deleted,
			HashSet<Transition> transitions) {

		HashSet<State> validStates = new HashSet<State>();
		// HashSet<State> deletedStates = new HashSet<State>();
		int deletedCount = 0;
		for (State s : states) {

			if (deleted.contains(s)) {

				continue;
			}

			boolean hasIncomingYet = false;
			// boolean hasOutgoingYet = false;

			for (Transition trans : transitions) {

				if (s.equals(trans.getDestination()) && !deleted.contains(trans.getSource())) {

					// if(s.toString().equals("N1,C2,x=1") || s.toString().equals("C1,C2,x=1") ) {
					// System.out.println("o");
					// }
					hasIncomingYet = true;
				}
			}

			if (hasIncomingYet || (ProgramHelper.initialGuard.accept(new EnableStateVisitor(), s) 
					&& s.toString().contains("null") && ProgramHelper.isAbstractView ) ||  (  
					
					(ProgramHelper.initialGuard.accept(new EnableStateVisitor(), s)
//					GuardSolver.checkIfStatePassesGuard(ProgramHelper.initialGuard, s)

					&& !ProgramHelper.isAbstractView ) ))
					{
				// if( s.toString().equals("C1,C2,x=1") ) {
				// System.out.println(s);
				// }
				validStates.add(s);
			}

			else {
				deleted.add(s);
				deletedCount++;
			}

		}

		if (deletedCount == 0)
			return validStates;

		else {
			System.out.println("Deleted States: " + deleted);
			return deleteStatesWithNoIncomingEdge(validStates, deleted, transitions);
		}

	}
}
