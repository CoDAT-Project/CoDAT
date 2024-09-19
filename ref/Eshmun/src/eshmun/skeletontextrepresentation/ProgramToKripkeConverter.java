package eshmun.skeletontextrepresentation;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import eshmun.Eshmun;
import eshmun.skeletontextrepresentation.grammar.ProgLexer;
import eshmun.skeletontextrepresentation.grammar.ProgParser;
import eshmun.skeletontextrepresentation.guard.EnableStateVisitor;
import eshmun.skeletontextrepresentation.guard.JavaExample;
import javafx.util.Pair;

/**
 * This class is called for converting an imported text represented Program to a
 * KripkeStructure. Used by EshmunMenuBar (form UI: File -> Import -> Program)
 * 
 * 
 * @author chukrisoueidi
 *
 */
public class ProgramToKripkeConverter {

	/**
	 * The converter handles the input by parsing the input based on the defined
	 * Prog.g4 antlr grammar using eshmun.skeletontextrepresentation.ProgramParser.
	 * The parser fills in all actions in ProgramFactory class static objects. In
	 * this class we rely on ProgramFactory to retrieve all actions and generate all
	 * states and transitions in the program.
	 * 
	 * Then after getting the set of transitions and states, we build the Kripke
	 * string and send it back to the caller.
	 * 
	 * @param filepath
	 *            to a text file
	 * @return a string that represents a KripkeStructure
	 * 
	 */
	public static String convert(String args, boolean isAbstractView) throws Exception {
		
		return convert2(args, isAbstractView);

//		// JavaExample.mainJunk();
//		// reset all stored variables
//		ProgramHelper.resetFactory();
//		ProgramHelper.isAbstractView = isAbstractView;
//		// parse input to fill the ProgramFactory
//		parseInput(args);
//
//		System.out.println();
//
//		System.out.println("states:");
//		System.out.println(ProgramHelper.getAllStates());
//
//		// loop through all parsed program actions, generate enable states based on
//		// local and global guards
//		// then generate transition between these states based on the local and global
//		// effects
//
//		HashSet<Transition> programTransitions = new HashSet<Transition>();
//		for (int i = 0; i < ProgramHelper.actions.size(); i++) {
//			HashSet<Transition> actionTransitions = ProgramHelper.actions.get(i).generateTranisitons(
//					ProgramHelper.actions.get(i).generateEnabledStatesByGuards(ProgramHelper.getAllStates()));
//			programTransitions.addAll(actionTransitions);
//		}
//
//		HashSet<State> validStatesSet = ProgramHelper.deleteStatesWithNoIncomingEdge(ProgramHelper.getAllStates(),
//				new HashSet<State>(), programTransitions);
//
//		StringBuilder sb = KripkeGenerator.generateKripkeString(new ArrayList<State>(validStatesSet),
//				programTransitions);
//
//		return sb.toString();

	}

	static HashSet<State> allProgramStates = new HashSet<>();

	public static String convert2(String args, boolean isAbstractView) throws Exception {

		// JavaExample.mainJunk();
		// reset all stored variables
		ProgramHelper.resetFactory();
		ProgramHelper.isAbstractView = isAbstractView;
		allProgramStates = new HashSet<>();

		// parse input to fill the ProgramFactory
		parseInput(args);

	 
		 HashSet<State> initialStates = ProgramHelper.getAllStates(); 
		 EnableStateVisitor visitor = new EnableStateVisitor();
		 for (State state : initialStates) {
			
			if( ProgramHelper.initialGuard.accept(visitor, state))
				allProgramStates.add(state);
					
		}
	
//
//		State state = new State();
//		state =state.addAtomicProposition("N1");
//		state =state.addAtomicProposition("N2");
//		state =state.addOrUpdateVariableAssignment("x", "2");
//		allProgramStates.add(state);
//		
//		state = new State();
//		state =state.addAtomicProposition("N1");
//		state =state.addAtomicProposition("N2");
//		state =state.addOrUpdateVariableAssignment("x", "1");
//	 
//
//		allProgramStates.add(state);

		HashSet<State> newProgramStates = new HashSet<>();

		HashSet<Transition> programTransitions = new HashSet<Transition>();
		boolean repeat = true;
		
		System.out.println();
		System.out.println("ProgramHelper.actions : " + ProgramHelper.actions.size());
		
		
		int z = 0;
		while (repeat) {
			System.out.println();
			System.out.println(++z);
			for (int i = 0; i < ProgramHelper.actions.size(); i++) {

				HashSet<State> enabled = ProgramHelper.actions.get(i).generateEnabledStatesByGuards(allProgramStates);
				if(enabled == null || enabled.isEmpty()) continue;
				
				
				
				HashSet<Transition> actionTransitions = ProgramHelper.actions.get(i).generateTranisitons(enabled);
				
				if(actionTransitions == null || actionTransitions.isEmpty()) continue;
				
				//System.out.print(actionTransitions.size());
				
				programTransitions.addAll(actionTransitions);
				for (Transition transition : actionTransitions) {
					newProgramStates.add(transition.getDestination());
					newProgramStates.add(transition.getSource());
				}
				

			}
			
			repeat = allProgramStates.addAll(newProgramStates);
		}

		System.out.println("ProgramHelper.actions : " + ProgramHelper.actions.size());
		
		
		 
		System.out.println("All States : " + allProgramStates.size());
		System.out.println("Transitions : " + programTransitions.size());
		
		
		HashSet<State> validStatesSet = ProgramHelper.deleteStatesWithNoIncomingEdge(allProgramStates,
				new HashSet<State>(), programTransitions);
		
		 
		 
		System.out.println("Valid States : " + validStatesSet.size());
		

		StringBuilder sb = KripkeGenerator.generateKripkeString(new ArrayList<State>(validStatesSet),
				programTransitions);

		return sb.toString();

	}

	public static String getCtl() {

		return ProgramHelper.ctlSpec;
	}

	/**
	 * Performs common antlr parsing for the input file. Parse results are stored
	 * statically in ProgramFactory
	 * 
	 * @param filepath
	 *            to a text file
	 */
	private static void parseInput(String args) {
		ANTLRInputStream input = null;
		File file = new File(args);

		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(args));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String content = new String(encoded, Charset.defaultCharset());
		content = content.replace(":=", " := ");

		// FileInputStream fileInputStream = null;

		// Open the input file stream
		// fileInputStream = new FileInputStream(file);
		input = new ANTLRInputStream(content);

		ProgLexer lexer = new ProgLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);

		ProgParser parser = new ProgParser(tokens);

		// ProgListener is extended in State Parser and Action Parser
		// Fills Statefactory.APs
		ProgramParser listener = new ProgramParser(parser, lexer); // We defined our own parser listener

		ParseTree tree = parser.prog();
		// walk the tree to fill the AST
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, tree);
	}

}
