package eshmun.skeletontextrepresentation.infinitespace;

import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.antlr.v4.codegen.model.chunk.ThisRulePropertyRef_ctx;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.microsoft.z3.ApplyResult;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Goal;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Tactic;
import com.microsoft.z3.enumerations.Z3_lbool;
import javafx.scene.control.skin.LabeledSkinBase;
import com.sun.org.apache.xpath.internal.operations.Bool;

import eshmun.Eshmun;
import eshmun.skeletontextrepresentation.KripkeGenerator;
import eshmun.skeletontextrepresentation.ProgramParser;

import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateAction;
import eshmun.skeletontextrepresentation.infinitespace.actions.InfiniteStateActionFactory;
import eshmun.skeletontextrepresentation.infinitespace.parser.InfiniteSpaceParser;
import eshmun.skeletontextrepresentation.infinitespace.structures.InfiniteState;
import eshmun.skeletontextrepresentation.infinitespace.structures.InfiniteStateTransition;
import eshmun.skeletontextrepresentation.infinitespace.visitors.StrongestPostConditionVisitor;

// TODO: Auto-generated Javadoc
/**
 * The Class InfiniteProgramToKripkeConverter.
 */
public class InfiniteProgramToKripkeConverter {

	/** The all states. */
	public static LinkedHashSet<InfiniteState> allStates = new LinkedHashSet<InfiniteState>();

	/** The counter. */
	public static int counter = 0;
	
	/** The counter. */
	public static int maxCounter = 15;
	
	public static int maxVariableValue = 0;
	
	public static int maxVariableThreshold = 5;

	/** The eshmun. */
	private static Eshmun eshmun;

	/** The interactive mode. */
	private static boolean interactiveMode = true;

	/** The all transitions. */
	public static HashSet<InfiniteStateTransition> allTransitions = new HashSet<InfiniteStateTransition>();

	/** The previous state equivalence checks. */
	public static HashMap<String, Boolean> previousStateEquivalenceChecks = new HashMap<>();

	/**
	 * Reset.
	 */
	public static void reset() {

		allStates = new LinkedHashSet<InfiniteState>();
		allTransitions = new HashSet<InfiniteStateTransition>();
		counter = 0;
		maxVariableValue = 0;
		previousStateEquivalenceChecks = new HashMap<String, Boolean>();
	}

	/**
	 * Convert.
	 *
	 * @param args
	 *            the args
	 * @param eshmunInstance
	 *            the eshmun instance
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public static String convert(String args, Eshmun eshmunInstance) throws Exception {

		eshmun = eshmunInstance;

		reset();
		InfiniteStateActionFactory.resetFactory();
		parseInput(args);
		Generate();

		return prepareKripke();

	}

	/**
	 * Parses the input.
	 *
	 * @param args
	 *            the args
	 */
	private static void parseInput(String args) {

		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(args));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String content = new String(encoded, Charset.defaultCharset());

		InfiniteSpaceParser.parseProgram(content);
	}

	/**
	 * Generate.
	 */
	public static void Generate() {

		// let K be the currently generated structure, K.states = states currently in K,
		// K.steps = transitions currently in K
		// initially, K consists of a single initial state
		// for s in K.states, let:
		// s.succ: the states in K that are reachable from s by a single transition,
		// i.e., (s,t) in K.steps
		// s.newsucc: the states that will be added as successors in the current
		// iteration of the main loop
		// s.pred be the state predicate of s

		// for each a in Actions
		// B := a.localGuard /\ a.globalGuard
		// for each state s in K.states
		// s.newsucc := emptyset
		// if =| B /\ s.pred // if action guard and state predicate are jointly
		// satisfiable
		// Q := strongestPostcond(B /\ s.pred, a.Action)
		// for each t in K.states
		// if =| Q /\ t.pred then s.newsucc += t
		// if s.newsucc = empty, then s.newsucc := new t where t.pred = Q
		// else if =| Q /\ !(\/ t in s.succ: t.pred) then
		// s.newsucc += new t such that t.pred = Q /\ !(\/ t in s.succ: t.pred)
		// endif
		// for all t in s.newsucc
		// K.states += t
		// K.steps += (s,t)

		// Setup needed for the algorithm
		HashSet<InfiniteState> newStates = new HashSet<InfiniteState>();

		Solver solver = null;
		Status result = null;
		Context ctx = InfiniteStateActionFactory.context;

		int actionNumber = 0;

		for (InfiniteStateAction action : InfiniteStateActionFactory.actions) {

			actionNumber++;
			BoolExpr B = action.getCombinedGuards(ctx, InfiniteStateActionFactory.allAPs);

			// Loop on all states to generate new transitions
			for (InfiniteState s : allStates) {

				HashSet<InfiniteState> newSuccessors = new HashSet<InfiniteState>();
				boolean stateIsEnabledByAction = stateSatisfyGuard(ctx, B, s, true, action);

				if (stateIsEnabledByAction) {
					// s is ENABLED by GUARD B i.e. (B & S) is SAT

					Z3SolverResult Qstate = getStrongestPostCondition(ctx, action, B, s);
					BoolExpr Q = Qstate.predicate;

					// Get valid Transitions
					for (InfiniteState t : allStates) {
						// If already a transition. then skip
						if (s.successors.contains(t)) {
							continue;

						} else if ((!s.notsuccessors.containsKey(Q) // keeping track of states not SAT to skip
								|| (s.notsuccessors.containsKey(Q) && !s.notsuccessors.get(Q).contains(t)))) {
							// Check if Q & t.pred is SAT
							if (stateSatisfyGuard(ctx, Q, t, false, null)) {
								newSuccessors.add(t);
							} else {

								if (s.notsuccessors.get(Q) == null) {
									s.notsuccessors.put(Q, new HashSet<>(Arrays.asList(t)));
								} else {
									// has values
									HashSet<InfiniteState> notSuccessors = s.notsuccessors.get(Q);
									notSuccessors.add(t);
									s.notsuccessors.put(Q, notSuccessors);
								}
							}
						}
					}
					// If no successors were found in K, we will add a new state n with Q
					if (newSuccessors.isEmpty() & s.successors.isEmpty()) {
						// Generate new State
						InfiniteState n = null;

						if (interactiveMode && maxVariableValue >= maxVariableThreshold -1 ) {

							System.out.println(s.toString());
							JTextArea textArea = new JTextArea(Q.toString());
							textArea.setColumns(70);
							textArea.setLineWrap(true);
							textArea.setRows(5);
							textArea.setWrapStyleWord(true);

							textArea.setSize(textArea.getPreferredSize().width, 1);
							
							JScrollPane pJScrollPane = new JScrollPane(textArea);
							
							JPanel panel = new JPanel();
							 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
							 panel.add(new Label("Action " + action.actionNumber + " is adding a new transition"));
							 panel.add(new Label("From state: " + s.toString()));
							 panel.add(new Label("To state: "));
							 panel.add(pJScrollPane);
							 
							 
							JOptionPane.showMessageDialog(null, panel, 
									"You can change the new state predicate",
									JOptionPane.WARNING_MESSAGE);
							
							//JOptionPane.showInputDialog(message, "Adding a new successor state for (" + s.toString() + ") by Action " + action.actionNumber +" :")
							
							
							
							String newQ = textArea.getText();

							
							System.out.println(s.toString());
							System.out.println(Q);
							System.out.println(newQ);
							System.out.println("------------------------------------------");
							
							newQ = InfiniteStateActionFactory.getZ3SMTDeclarations() + "(assert " + newQ + ")";
							BoolExpr newQExp = ctx.mkAnd(ctx.parseSMTLIB2String(newQ, null, null, null, null));
							n = new InfiniteState(newQExp);
							n.setNumber(allStates.size() + 1);
						} else {

							n = new InfiniteState((BoolExpr) Q.simplify(), Qstate.labels);
							n.setNumber(allStates.size() + 1);
						}

						InfiniteState exists = null;
						for (InfiniteState infiniteState : allStates) {
							if (infiniteState.IsEquals(n))
								exists = infiniteState;
						}
						if (exists == null) {
							newStates.add(n);
							newSuccessors.add(n);

						} else {
							newSuccessors.add(exists);
						}
					} else if (!newSuccessors.isEmpty()) {

						// Add those successors to s and transitions, here successors are already in
						// K.States

						for (InfiniteState t : newSuccessors) {
							InfiniteStateTransition r = new InfiniteStateTransition(s, t, actionNumber,
									action.getNonBoolEffects().toString());
							r.iterationNumber = counter + 1;
							s.successors.add(t);
							action.transitions.add(r);
							allTransitions.add(r);
						}

						// reset new successors, since we added them already in section above
						newSuccessors = new HashSet<>();

						// now we check that Q - ! U_t is SAT, if yes this means we need to create a new
						// State
						BoolExpr unionOfSuccessorStates = null;
						// Get Union of All successors
						for (InfiniteState succ : s.successors) {
							if (unionOfSuccessorStates == null) {
								unionOfSuccessorStates = succ.getPredicate();
							} else {
								unionOfSuccessorStates = (BoolExpr) ctx
										.mkOr(unionOfSuccessorStates, succ.getPredicate()).simplify();
							}
						}

						solver = ctx.mkSolver();
						BoolExpr newStateExpr = (BoolExpr) ctx.mkAnd(Q, ctx.mkNot(unionOfSuccessorStates)).simplify();
						solver.add(newStateExpr);
						result = solver.check();
						if (result == Status.SATISFIABLE) {

							// if SAT, add the new state

							Z3SolverResult solverresult = solveAndSimplify(ctx, Q, null);

							InfiniteState newState = new InfiniteState(solverresult.predicate, solverresult.labels);
							newState.setNumber(allStates.size() + 1);
							boolean exists = false;

							for (InfiniteState infiniteState : allStates) {
								if (infiniteState.IsEquals(newState)) {
									newState = infiniteState;
									exists = true;
									break;
								}
							}
							if (exists) {
								newStates.add(newState);

							} else {

							}

							s.successors.add(newState);
							InfiniteStateTransition r = new InfiniteStateTransition(s, newState, actionNumber,
									action.getNonBoolEffects().toString());
							r.iterationNumber = counter + 1;
							action.transitions.add(r);
							allTransitions.add(r);

						}
					}
				}
			}
			if (newStates.size() > 0) {
				allStates.addAll(newStates);

			}
		}

		// printing iteration and counts
		System.out.println(++counter + " runs . State count = " + allStates.size());

		if (newStates.size() > 0 && counter <= maxCounter) {
			
			Generate();
			
		}else {
			JOptionPane.showMessageDialog(eshmun, "The algorithm ran for "+ counter +" iterations.");
		}
	}

	/**
	 * Gets the strongest post condition of a state predicate B and action command.
	 * If Q is not previously generated, this method will invoke the action visitor
	 * to generate it. Else, it will retrieve it from the state dictionary
	 * containing these post conditions.
	 * 
	 *
	 * @param ctx
	 *            the Z3 ctx
	 * @param action
	 *            the action
	 * @param B
	 *            the state predicate
	 * @param s
	 *            the s
	 * @return the strongest post condition
	 */
	private static Z3SolverResult getStrongestPostCondition(Context ctx, InfiniteStateAction action, BoolExpr B,
			InfiniteState s) {

		// generate Q which is SP -
		// if already computed the get it from state
		// else compute it
		
		BoolExpr Q = null;
		if (s.strongestPostConditions.containsKey(action)) {
			Q = s.strongestPostConditions.get(action);
			return new Z3SolverResult(null, Q);
		} else {

			BoolExpr bNa = ctx.mkAnd(B, s.getPredicate());
			StrongestPostConditionVisitor visitor = new StrongestPostConditionVisitor();
			Q = (BoolExpr) action.getEffects().accept(visitor, bNa, ctx, s.stateNumber + 1);

			Z3SolverResult finalValue = solveAndSimplify(ctx, Q, null);
			s.strongestPostConditions.put(action, finalValue.predicate);

			return finalValue;

		}


	}

	/**
	 * Tries to solve the expression and simplifies it to a conjunction of APs.
	 *
	 * @param ctx
	 *            the ctx
	 * @param Q
	 *            the q
	 * @param finalValue
	 *            the final value
	 * @return the bool expr
	 */
	private static Z3SolverResult solveAndSimplify(Context ctx, BoolExpr Q, BoolExpr finalValue) {
		Solver solver = ctx.mkSolver();
		solver.add(Q);
		Status solverresult = solver.check();

		if (solverresult == Status.SATISFIABLE) {

			Model model = solver.getModel();

			HashSet<String> allAPs = InfiniteStateActionFactory.allAPs;
			HashSet<String> allInts = InfiniteStateActionFactory.allIntVariables;

			HashSet<String> labels = new HashSet<>();

			for (String ap : allAPs) {

				boolean isTrue = Boolean.parseBoolean(model.eval(ctx.mkBoolConst(ap), false).toString());

				if (finalValue == null) {
					if (isTrue) {
						finalValue = ctx.mkBoolConst(ap);
						labels.add(ap);
					} else {
						finalValue = ctx.mkNot(ctx.mkBoolConst(ap));
					}

				} else {
					if (isTrue) {
						finalValue = ctx.mkAnd(finalValue, ctx.mkBoolConst(ap));
						labels.add(ap);
					} else {
						finalValue = ctx.mkAnd(finalValue, ctx.mkNot(ctx.mkBoolConst(ap)));
					}

				}

			}

			for (String v : allInts) {
				IntExpr intval =  (IntExpr) model.eval(ctx.mkIntConst(v), false) ;
				try {
					int max = Integer.parseInt(intval.toString());
					if(max > maxVariableValue) {
						maxVariableValue = max;
					}
				} catch (Exception e) {
					maxVariableValue = maxVariableValue;
				} 
				
								
				
				if (finalValue == null) {
				
					finalValue = ctx.mkEq(ctx.mkIntConst(v), intval);

				} else {
					finalValue = ctx.mkAnd(finalValue,
							ctx.mkEq(ctx.mkIntConst(v), intval));
				}
			}

			return new Z3SolverResult(labels, (BoolExpr) finalValue.simplify());

		} else {
			return new Z3SolverResult(null, Q);
		}

	}

	/**
	 * Check if state is enabled by guard.
	 *
	 * @param ctx
	 *            the ctx
	 * @param B
	 *            the b
	 * @param s
	 *            the s
	 * @param isActionSATCheck
	 *            the is action SAT check
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	private static boolean stateSatisfyGuard(Context ctx, BoolExpr B, InfiniteState s, boolean isActionSATCheck,
			InfiniteStateAction action) {

		if (isActionSATCheck) {
			Solver solver;
			Status result;
			boolean stateIsEnabledByAction = false;

			// Is s enabled by Action ?
			// If already checked previously; get it
			// else check and save in state

			if (s.actionsSAT.containsKey(action)) {
				stateIsEnabledByAction = s.actionsSAT.get(action);
			} else {

				solver = ctx.mkSolver();
				solver.add(ctx.mkAnd(B, s.getPredicate()));
				result = solver.check();
				if (result == Status.SATISFIABLE) {
					s.actionsSAT.put(action, true);
					stateIsEnabledByAction = true;
				} else {
					s.actionsSAT.put(action, false);
					stateIsEnabledByAction = false;
				}

			}
			return stateIsEnabledByAction;
		} else {

			Solver solver;
			Status result;

			solver = ctx.mkSolver();
			solver.add(ctx.mkAnd(B, s.getPredicate()));
			result = solver.check();
			if (result == Status.SATISFIABLE) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Parses the program.
	 */
	public static void parseProgram() {

		// InfiniteSpaceParser.parseFormula("forall ?x (forall ?v ( x != ( 2 * ?v ) )
		// )", context);

		// BoolExpr pExpr = InfiniteSpaceParser.parseFormula("N1 & N2 & (n1=0) & (n2=0 )
		// & N1 & (n1=0) & (CH2 | CR2 | N2)", context);

		// Statement statement = InfiniteSpaceParser.parseStatement("{N1 := ff; CH1 :=
		// tt; n1 := n2 + 1;}", context);

		// InfiniteSpaceParser.parseStatement( " if(a<b){a:= 5; a :=6;} else{ x:=3;
		// if(a<b){a:= 5; a :=6;} else{ x:=3;}}" , context);

		// BoolExpr p = InfiniteSpaceParser.parseFormula("(t1 < t2)", context);
		// Statement statement = InfiniteSpaceParser.parseStatement( "if(t1 > t2){\n" +
		// " t1 := 5;\n" +
		// " \n" +
		// " }\n" +
		// "else{\n" +
		// " t1:= 8;\n" +
		// "}" , context);
		// StrongestPostConditionVisitor sPostConditionVisitor = new
		// StrongestPostConditionVisitor();
		// System.out.println(statement.accept(sPostConditionVisitor, pExpr, context));

		InfiniteSpaceParser.parseProgram("program  {\r\n" + "	initial   : One1 & One2 & (x=0)\r\n"
				+ "	process  1 {\r\n" + "		action   {\r\n" + "			l_grd : One1\r\n"
				+ "			g_grd : (x<5) \r\n" + "			l_eff : { One1 :=ff; Two1 :=tt;  }\r\n"
				+ "			g_eff : { }\r\n" + "		 }\r\n" + "\r\n" + "		 action   {\r\n"
				+ "			l_grd : Two1\r\n" + "			g_grd : One2 | Two2 \r\n"
				+ "			l_eff : { One1 :=tt ; Two1 := ff; x := x +1; }\r\n" + "			g_eff : { }\r\n"
				+ "		 }\r\n" + "\r\n" + "		  \r\n" + "		 \r\n" + "	 }\r\n" + "	process  2 {\r\n"
				+ "		 	\r\n" + "		 	action   {\r\n" + "			l_grd : One2\r\n"
				+ "			g_grd : (x>0) \r\n" + "			l_eff : { Two2 := tt ; One1 := ff;  }\r\n"
				+ "			g_eff : { }\r\n" + "		 }\r\n" + "\r\n" + "		 action   {\r\n"
				+ "			l_grd : Two2\r\n" + "			g_grd : One1 | Two1 \r\n"
				+ "			l_eff : {  Two2 := ff ; One1 := tt;  x := x - 1; }\r\n" + "			g_eff : { }\r\n"
				+ "		 }\r\n" + "		 \r\n" + "	 }\r\n" + "	 \r\n" + "}\r\n" + "");

		Generate();

	}

	/**
	 * Prints the report.
	 */
	public static void printReport() {

		// for (InfiniteStateAction action : InfiniteStateActionFactory.actions) {
		// Statement statement = action.getNonBoolEffects();
		// System.out.println(statement.toString().replaceAll("\t", "").replaceAll("\n",
		// "").replaceAll(System.lineSeparator(), ""));
		// }

		System.out.println("------------------------------------------");
		System.out.println("Number of States: " + allStates.size());

		for (InfiniteState s : allStates) {

			for (String label : InfiniteStateActionFactory.allAPs) {

				Context ctx = InfiniteStateActionFactory.context;

				Solver solver = ctx.mkSolver();

				solver.add(ctx.mkAnd(ctx.mkBoolConst(label), s.getPredicate()));

				Status result = solver.check();

				if (result == Status.SATISFIABLE) {
					s.addLabel(label);

				}

			}
		}

		for (InfiniteState s : allStates) {

			System.out.println("------------------------------------------");
			System.out.println(s.getLabels());
		}

		// for (InfiniteState infiniteState : allStates) {
		//
		// System.out.println("---------------------------------STATE------------------------");
		// System.out.println(infiniteState.statePredicate);
		//
		// }

		// // Keep states with incoming edges
		// HashSet<InfiniteState> allStates =
		// ProgramGenerator.deleteStatesWithNoIncomingEdge(ProgramGenerator.allStates,
		// new HashSet<InfiniteState>(), ProgramGenerator.allTransitions);

		//
		// // Keep transitions with live states
		// HashSet<InfiniteStateTransition> remaining = new
		// HashSet<InfiniteStateTransition>();
		// for (InfiniteStateTransition infiniteStateTransition : allTransitions) {
		//
		// if (allStates.contains(infiniteStateTransition.sourceState)
		// && allStates.contains(infiniteStateTransition.sourceState)) {
		// remaining.add(infiniteStateTransition);
		// }
		// }
		//
		// allTransitions = remaining;

		System.out.println("------------------------------------------");
		System.out.println("Number of Trans:  " + allTransitions.size());

		// // Print transitions

		for (InfiniteStateTransition trans : allTransitions) {

			System.out.println("------------------------------------------");
			// System.out.println(trans);
		}

		System.out.println("------------------------------------------");
		System.out.println("------------------------------------------");
		System.out.println("------------------------------------------");
		System.out
				.println(KripkeGenerator.generateInfiniteStateKripkeString(new ArrayList<>(allStates), allTransitions));
	}

	/**
	 * Prepare kripke.
	 *
	 * @return the string
	 */
	public static String prepareKripke() {

		

		addMissingStateLabels();
		
		HashSet<InfiniteState> states =  deleteStatesWithNoIncomingEdge(allStates, new HashSet<>(), allTransitions);

		return KripkeGenerator.generateInfiniteStateKripkeString(new ArrayList<>(states), allTransitions).toString();
	}

	
	private static void addMissingStateLabels() {
	
		for (InfiniteState s : allStates) {

			if (s.getLabels() != null)
				for (String label : InfiniteStateActionFactory.allAPs) {

					if (s.getPredicate().toString().contains(label)) {

						Context ctx = InfiniteStateActionFactory.context;

						Solver solver = ctx.mkSolver();

						solver.add(s.getPredicate());
						solver.add(ctx.mkBoolConst(label));

						Status result = solver.check();

						if (result == Status.SATISFIABLE) {
							s.addLabel(label);

						}
					}
				}
		}
	}

	/**
	 * Delete states with no incoming edge.
	 *
	 * @param states
	 *            the states
	 * @param deleted
	 *            the deleted
	 * @param transitions
	 *            the transitions
	 * @return the hash set
	 */
	public static HashSet<InfiniteState> deleteStatesWithNoIncomingEdge(HashSet<InfiniteState> states,
			HashSet<InfiniteState> deleted, HashSet<InfiniteStateTransition> transitions) {

		HashSet<InfiniteState> validStates = new HashSet<InfiniteState>();

		int deletedCount = 0;
		for (InfiniteState s : states) {

			if (deleted.contains(s)) {

				continue;
			}

			boolean hasIncomingYet = false;
			// boolean hasOutgoingYet = false;

			for (InfiniteStateTransition trans : transitions) {

				if (s.equals(trans.destinationState) && !deleted.contains(trans.sourceState)) {

					// if(s.toString().equals("N1,C2,x=1") || s.toString().equals("C1,C2,x=1") ) {
					// System.out.println("o");
					// }
					hasIncomingYet = true;
				}
			}

			if (hasIncomingYet)

			// || (
			//
			// (ProgramHelper.initialGuard.accept(new EnableStateVisitor(), s)
			//// GuardSolver.checkIfStatePassesGuard(ProgramHelper.initialGuard, s)
			//
			// && !ProgramHelper.isAbstractView ) ))
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

	public static class Z3SolverResult {
		public HashSet<String> labels;
		public BoolExpr predicate;

		public Z3SolverResult(HashSet<String> l, BoolExpr b) {
			labels = l;
			predicate = b;

		}
	}

}
