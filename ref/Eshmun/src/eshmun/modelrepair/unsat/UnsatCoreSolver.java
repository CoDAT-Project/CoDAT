package eshmun.modelrepair.unsat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import javax.swing.JOptionPane;

import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.visitor.visitors.VariableListerVisitor;
import eshmun.gui.utils.models.vanillakripke.State;
import eshmun.gui.utils.models.vanillakripke.Transition;
import eshmun.modelrepair.sat.AbstractSatSolver;
import eshmun.modelrepair.sat.CNFStream;

/**
 * This is the interface for the sat4j sat solver that is a java implementation
 * for satisfiability problem.
 * 
 * @author Kinan Dak Al Bab
 * @since 0.1
 */
public class UnsatCoreSolver extends AbstractSatSolver {	
	/**
	 * The Path to save the formula to.
	 */
	private static String FORMULA_PATH = "FORMULA.cnf";
	
	/**
	 * The Path to read unsat core from.
	 */
	private static String OUTPUT_PATH = "UNSATCORE";
	
	/**
	 * The name of The UNSAT-CORE Program.
	 */
	private static String MUS_PROGRAM = "run-muser2";
	
	/**
	 * Visitor used to extract problem out of the given expression.
	 */
	private VariableListerVisitor visitor;
	
	/**
	 * Number of clauses in the expression.
	 */
	protected int numberOfClauses;
	
	/**
	 * Number of distinct variables in the expression.
	 */
	protected int numberOfVaraibles;
	
	/**
	 * The path for the Minimal Unsat Core tool.
	 */
	private String musPath;
	
	/**
	 * The current solution.
	 */
	private HashSet<Integer> currentSolution;
	
	/**
	 * CNF Stream to read problem from.
	 */
	private CNFStream cnfStream;
	
	/**
	 * Mapping between Variable IDs and Variable names.
	 */
	private HashMap<Integer, String> idMap;

	/**
	 * Create a new MINIMAL-UNSAT-CORE solver for the given problem.
	 * @param cnfExpression the expression to find UNSAT CORE for.
	 * @param musPath the path for the minimal UNSAT-Core tool.
	 * @throws NotCNFException if the expression is not in CNF format.
	 * @throws IOException if an error while reading problem occurred.
	 */
	public UnsatCoreSolver(AbstractExpression cnfExpression, String musPath) throws NotCNFException, IOException {
		super(cnfExpression);
		
		this.musPath = musPath;
		
		visitor = new VariableListerVisitor();
		visitor.listVariables(cnfExpression); //Looks for contradictions
				
		// CNF Stream to read problem from
		cnfStream = new CNFStream(visitor.getVariableCount(), visitor.getClauseList());
		idMap = visitor.getIDMap();
		
		FileOutputStream fos = new FileOutputStream(new File(musPath+FORMULA_PATH));
		int r = 0;
		while((r = cnfStream.read()) != -1) {
			fos.write(r);
		}
		
		fos.close();
		
		File outputFile = new File(musPath+OUTPUT_PATH+".cnf");
		if(outputFile.exists()) outputFile.delete();
	}

	/**
	 * Determines whether the given expression is satisfiable or not using SAT4J solver.
	 * @return true if it is satisfiable, false otherwise.
	 * @throws IOException if something wrong happened while reading files.
	 */
	@Override
	public boolean isSatisfiable() throws IOException {
		System.out.println(MUS_PROGRAM+" -var -wf "+OUTPUT_PATH+" "+FORMULA_PATH);
		ProcessBuilder builder = new ProcessBuilder("./"+MUS_PROGRAM, "-var", "-wf", OUTPUT_PATH, FORMULA_PATH);
		builder.directory(new File(musPath));			
		Process p = builder.start();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while((line = reader.readLine()) != null)
			System.out.println("MUS: \t"+line);
		
		reader.close();
				
		try {
			p.waitFor();
			
			// Print Errors if any.
			InputStream error = p.getErrorStream();
			String errorMsg = "";
			int b = 0;
			while((b = error.read()) != -1)
				errorMsg += (char) b;
			
			error.close();
			errorMsg = errorMsg.trim();
			if(!errorMsg.isEmpty()) {
				errorMsg = "Running MUSer2 yielded the following error, " +
							"Please make sure you have a compatible version" +
							"of MUSer2 with your OS and dependencies: " + 
							System.lineSeparator() + errorMsg;  
				JOptionPane.showMessageDialog(null, errorMsg, "Error Executing MUSer2", JOptionPane.ERROR_MESSAGE);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * returns the current solution.
	 * @return the current solution.
	 * @throws IOException if file IO throw an exception.
	 */
	@Override
	public int[] getNextSolution() throws IOException {
		currentSolution = new HashSet<Integer>();
		isSatisfiable(); //solve
		
		//When the formula is SAT, The file wont be created.
		File output = new File(musPath+OUTPUT_PATH+".cnf");
		if(!output.exists()) return new int[0];
		
		Scanner scan = new Scanner(output);
		
		scan.nextLine(); //Header line
		scan.nextLine(); //Header line
		while(scan.hasNextInt()) {
			currentSolution.add(Math.abs(scan.nextInt()));
		}
		scan.close();
		
		int counter = 0;
		int[] result = new int[currentSolution.size()];
		for(Integer i : currentSolution) {
			result[counter] = i;
			counter++;
		}
		
		return result;
	}
	
	/**
	 * Gets the Next Solution, as a list of variable names to be deleted.
	 * @throws IOException  if the file IO threw an exception. 
	 */
	@Override
	public Collection<String> getNextRepair() throws IOException {
		int[] solution = getNextSolution();
		
		HashSet<String> unsatCore = new HashSet<String>();
		for(int var : solution) {
			String varName = idMap.get(var);
			if(varName == null) continue;
			if(!(varName.startsWith(State.STATE_PREFIX) || varName.startsWith(Transition.TRANSITION_PREFIX))) 
				continue;
			
			int count = varName.length() - varName.replace(".", "").length();
			if(count > 2) continue;
			
			unsatCore.add(varName);
		}
		
		return unsatCore;
	}
}
