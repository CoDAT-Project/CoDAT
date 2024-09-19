package eshmun.sat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sat4j.minisat.*;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;
import org.sat4j.tools.SolutionCounter;

import eshmun.expression.PredicateFormula;
import eshmun.modelrepairer.FormulaStringCollection;


/**
 * this is the interface for the sat4j sat solver that is a java inmplementation
 * for satisfiablility problem.
 * @author Emile
 *
 */
public class SAT4jSolver extends AbstractSatSolver{

	private FormulaStringCollection _variableList;
	private String _result;
	private String[] _solution;
	
	public SAT4jSolver(PredicateFormula predicate, FormulaStringCollection variableList) {
		super(predicate);
		this._variableList = variableList;
		_result = "";
	}
	
	
	
	public String[] getSolution() {
		return _solution;
	}

	public String getResult() {
		return _result;
	}
	public long getNbOfSolutions()
	{
	SolutionCounter counter = new SolutionCounter(SolverFactory.newDefault());
	Reader reader = new DimacsReader ( counter );
	 try {
	     return counter.countSolutions();
	  } catch (TimeoutException te) {
	     return counter.lowerBound();
	  }
	}

	@Override
	public boolean isSatisfiable() {
		
		StringBuilder result = new StringBuilder();
		//Create CNF file 
		CNFFile cnfFile = new CNFFile(predicate, _variableList);
		cnfFile.SaveFile();
		ISolver solver = SolverFactory.newDefault ();
		solver . setTimeout (3600); // 1 hour timeout
		Reader reader = new DimacsReader ( solver );
		
		try {
		IProblem problem = reader.parseInstance ("dottool"+ System.getProperty("file.separator") +"CNFFile.cnf");
		if ( problem.isSatisfiable ()) {
			result.append(" Satisfiable  ! \n");
			String solution = reader.decode(problem.model());
			result.append(solution);
			_solution = solution.split(" ");
			_result = result.toString();
			return true;
		} else {
			result.append(" Unsatisfiable  !");
			_result = result.toString();
			return false;
		}
		} catch ( FileNotFoundException e) {
			result.append(e.getMessage());
			_result = result.toString();
			return false;
		} catch ( ParseFormatException e) {
			result.append(e.getMessage());
			_result = result.toString();
			return false;
		} catch ( IOException e) {
			result.append(e.getMessage());
			_result = result.toString();
			return false;
		} catch ( ContradictionException e) {
			result.append(" Unsatisfiable  ( trivial )!");
			_result = result.toString();
			 return false;
		} catch ( TimeoutException e) {
			result.append(" Timeout ,  sorry !");
			_result = result.toString();
			return false;
		}
	}

	public List<String> getAllSolutions() {
		
		List<String> list = new ArrayList<String>();
		StringBuilder result = new StringBuilder();
		//Create CNF file 
		CNFFile cnfFile = new CNFFile(predicate, _variableList);
		cnfFile.SaveFile();
		ISolver solver = SolverFactory.newDefault ();
		ModelIterator mi = new ModelIterator(solver);
		solver . setTimeout (36); // 1 hour timeout
		Reader reader = new InstanceReader(mi);
		int counter = 0;
		try {
		IProblem problem = reader.parseInstance ("dottool"+ System.getProperty("file.separator") +"CNFFile.cnf");
		
		while ( counter < 10) {		
			problem.isSatisfiable ();
			list.add(reader.decode(problem.model()));
			counter ++;
		}
		} catch ( Exception e) {
			list.add(e.getMessage());
		} 
		return list;
	}
	
	
}
