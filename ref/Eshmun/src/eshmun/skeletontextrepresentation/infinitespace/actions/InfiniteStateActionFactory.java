package eshmun.skeletontextrepresentation.infinitespace.actions;

import java.util.ArrayList;
import java.util.HashSet;



import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;

import eshmun.skeletontextrepresentation.infinitespace.InfiniteProgramToKripkeConverter;
import eshmun.skeletontextrepresentation.infinitespace.structures.InfiniteState;

public class InfiniteStateActionFactory {

	public static ArrayList<InfiniteStateAction> actions = new ArrayList<InfiniteStateAction>();

	public static InfiniteStateAction currentAction = new InfiniteStateAction();

	public static HashSet<String> allAPs = new HashSet<String>();

	public static HashSet<String> allIntVariables = new HashSet<String>();

	public static int visitorCounter = 0;

	private static BoolExpr initialGuard;

	public static Context context = new Context();

	public static Solver solver = context.mkSolver();

	public static void setInitialGuard(BoolExpr b) {

		initialGuard = b;
	 
		InfiniteProgramToKripkeConverter.allStates.add(new InfiniteState(b, true ));

	}

	public static HashSet<Integer> processNumbers = new HashSet<>();

	public static void resetFactory() {
		initialGuard = null;
		solver = context.mkSolver();
		context = new Context();
		processNumbers = new HashSet<>();
		allAPs = new HashSet<String>();
		currentAction = new InfiniteStateAction();
		allIntVariables = new HashSet<String>();
		actions = new ArrayList<InfiniteStateAction>();
		visitorCounter = 0;
	}

	public static String getAllZ3Declarations() {

		String result = "";

		// for (String AP : allAPs) {
		//
		// result = result + "(declare-fun " + AP + " () Bool) ";
		// }

		for (String v : allIntVariables) {

			result = result + "(declare-const " + v + " Int) ";
			result = result + "(declare-const " + v + "_ Int) ";
		}
		return result;

	}
	
	

	public static String getZ3SMTDeclarations() {
		
		

		String result = "";

		for (String AP : allAPs) {

			result = result + "(declare-fun " + AP + " () Bool) ";
		}

		for (String v : allIntVariables) {

			result = result + "(declare-const " + v + " Int) ";
			// result = result + "(declare-const " + v + "_ Int) ";
		}

		for (int i = 0; i <= visitorCounter; i++) {
			result = result + "(declare-const x_" + i + " Int) ";

		}
		return result;

	}

}
