package eshmun.modelrepair.sat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import eshmun.Eshmun;
import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.visitor.visitors.VariableListerVisitor;

/**
 * This is the interface for the sat4j sat solver that is a java implementation
 * for satisfiability problem.
 * 
 * @author Emile Chartouni, Kinan Dak Al Bab
 * @since 0.1
 */
public class SAT4jSolver extends AbstractSatSolver {
	/**
	 * Time before firing a time out exception (in seconds).
	 */
	private static final int TIMEOUT = 3600; //one hour
	
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
	 * If this expression is initially satisfiable, no contradictions.
	 * If this was true it doesn't mean that the expression is satisfiable.
	 */
	private boolean satisfiable;
	
	/**
	 * The current solution.
	 */
	private int[] currentSolution;
	
	/**
	 * CNF Stream to read problem from.
	 */
	private CNFStream cnfStream;
	
	/**
	 * Solver (SAT4J).
	 */
	private IProblem problem;
	
	/**
	 * Reads the problem from CNF Stream.
	 */
	private Reader reader;
	
	/**
	 * Used to iterate over possible solutions.
	 */
	private ModelIterator mi;

	/**
	 * Create a new SAT4J solver for the given problem.
	 * @param cnfExpression the expression to SAT solve.
	 * @throws NotCNFException if the expression is not in CNF format.
	 * @throws ParseFormatException if the expression is not in CNF format.
	 * @throws IOException if an error while reading problem occurred.
	 */
	public SAT4jSolver(AbstractExpression cnfExpression) throws NotCNFException, ParseFormatException, IOException {
		super(cnfExpression);
		
		visitor = new VariableListerVisitor();
		satisfiable = visitor.listVariables(cnfExpression); //Looks for contradictions
		if(!satisfiable)
			return;
		

		if(Eshmun.DEBUG_FLAG) { //Print the Formula to a file, for Debugging.
			cnfStream = new CNFStream(visitor.getVariableCount(), visitor.getClauseList());
			try {
				FileOutputStream fos = new FileOutputStream(new File("FORMULA.cnf"));
				int r = 0;
				while((r = cnfStream.read()) != -1) fos.write(r);
				fos.close();
		
				HashMap<Integer, String> map = visitor.getIDMap();
				PrintWriter pw = new PrintWriter(new File("VAR_IDS.txt"));
				for(Integer i : map.keySet()) pw.write(i+" : " + map.get(i)+System.lineSeparator());
				pw.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		// CNF Stream to read problem from
		cnfStream = new CNFStream(visitor.getVariableCount(), visitor.getClauseList());
		
		// Solver for the problem
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(TIMEOUT);
		//solver.newVar(visitor.getVariableCount()); FOR MAXSAT
		
		// Iterates over different solutions
		mi = new ModelIterator(solver);
		
		// Reads the problem from cnf stream
		reader = new DimacsReader(mi);
		
		// Read the problem
		try {
			problem = reader.parseInstance(cnfStream);
			
		} catch (ContradictionException e) { //formula has contradiction, unsatisfiable.
			satisfiable = false;
		}
	}

	/**
	 * Determines whether the given expression is satisfiable or not using SAT4J solver.
	 * @return true if it is satisfiable, false otherwise.
	 * @throws TimeoutException if timeout.
	 */
	@Override
	public boolean isSatisfiable() throws TimeoutException {		
		if(satisfiable && problem.isSatisfiable()) {		
			currentSolution = problem.model();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * returns the current solution.
	 * @return the current solution.
	 * @throws TimeoutException if timeout.
	 */
	@Override
	public int[] getNextSolution() throws TimeoutException {
		if(currentSolution != null) {
			int[] tmp = currentSolution;
			currentSolution = null;
		
			return tmp;
		} else {
			if(isSatisfiable()) {
				int[] tmp = currentSolution;
				currentSolution = null;
			
				return tmp;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Gets the Next Solution, as a list of variable names to be deleted.
	 * @throws TimeoutException if a timeout was reached while solving using SAT4j.
	 */
	@Override
	public Collection<String> getNextRepair() throws TimeoutException {
		int[] solution = getNextSolution();
		
		if(solution == null)
			return null;
		
		//Mapping from IDs to Variable Names.
		HashMap<Integer, String> map = visitor.getIDMap();
		
		for(int id : solution) {
			map.remove(id);
		}
				
		return map.values();
	}
	
	/**
	 * @return The number of clauses in the CNF Formula.
	 */
	public int getNumberOfClauses() {
		return cnfStream.getNumberOfClauses();
	}
	
	/**
	 * @return The number of literals in the CNF Formula.
	 */
	public int getNumberOfLiterals() {
		return cnfStream.getNumberOfLiterals();
	}
}
