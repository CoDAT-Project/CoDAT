package eshmun.modelrepair.sat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * A CNF Stream, streams the repair formula to SAT Solver.
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class CNFStream extends InputStream {
	/**
	 * the header of the problem, has the given specific format: <br>
	 * p cnf #of variables #of clauses
	 */
	private byte[] header;
	
	/**
	 * list of clauses, each has the given specific format: <br>
	 * <ul>
	 * 	<li>Each variable is represented by a number (its ID) starting from 1.</li>
	 * 	<li>If a variable is negated, a - sign is put in front of it (no space between).</li>
	 * 	<li>The clause is terminated by a zero.</li>
	 * </ul>
	 */
	private ArrayList<byte[]> clauseList;
	
	/**
	 * the current clause being read from.
	 * -1 represents the header, anything else is in the clause.
	 */
	private int currentClause;
	
	/**
	 * the current character inside a clause.
	 */
	private int currentbyte;
	
	/**
	 * The number of clauses in the formula.
	 */
	private int numberOfClauses;
	
	/**
	 * The number of literals in the formula.
	 */
	private int numberOfLiterals;
	
	/**
	 * Constructs a new CNF Stream, for the given problem.
	 * 
	 * @param numberOfVariable number of distinct variables in the problem.
	 * @param clauseList the clauses of the problem, in format.
	 */
	public CNFStream(int numberOfVariable, ArrayList<String> clauseList) {
		super();
		
		numberOfClauses = clauseList.size();
		numberOfLiterals = 0;
		
		currentClause = -1; //Start from header.
		currentbyte = 0;
		
		String tmp = "p cnf " + numberOfVariable + " " + clauseList.size() + System.lineSeparator();
		header = tmp.getBytes(StandardCharsets.UTF_8);
		
		this.clauseList = new ArrayList<byte[]>();
		for(String s : clauseList) {
			numberOfLiterals += s.length() - s.replace(" ", "").length();
			this.clauseList.add(s.getBytes(StandardCharsets.UTF_8));
		}
	}
	
	/**
	 * Reads the problem byte after byte.
	 * 
	 * @return the byte currently read, -1 if the end was reached.
	 * @throws IOException if something wrong occurs.
	 */
	@Override
	public int read() throws IOException {
		if(currentClause == clauseList.size()) { //end
			return -1;
		}
		
		byte[] clause;
		if(currentClause == -1)
			clause = header;
		else 
			clause = clauseList.get(currentClause);
		
		byte result = clause[currentbyte];
		
		currentbyte++;
		
		if(currentbyte == clause.length) {
			currentbyte = 0;
			currentClause++;
		}
		
		return result;
	}	
	
	/**
	 * @return The number of clauses in the CNF Formula.
	 */
	public int getNumberOfClauses() {
		return numberOfClauses;
	}
	
	/**
	 * @return The number of literals in the CNF Formula.
	 */
	public int getNumberOfLiterals() {
		return numberOfLiterals;
	}
}
