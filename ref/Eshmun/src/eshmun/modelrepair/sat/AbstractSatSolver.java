package eshmun.modelrepair.sat;

import java.util.Collection;

import eshmun.expression.AbstractExpression;

/**
 * The abstract implementation for the SAT solver interface. This interface
 * will take an AbstractExpression and solve its satisfiability problem.
 * 
 * @author Emile Chartouni, Kinan Dak Al Bab
 * @since 0.1
 */
public abstract class AbstractSatSolver {
	protected AbstractExpression cnfExpression;
	
	/**
	 * Create a Solver for the given expression.
	 * @param cnfExpression the expression to SAT solve in CNF.
	 */
	public AbstractSatSolver(AbstractExpression cnfExpression) {	
		this.cnfExpression = cnfExpression;
	}
	
	/**
	 * Determines whether the given expression is satisfiable or not.
	 * @return true if it is satisfiable, false otherwise.
	 * @throws Exception general exception.
	 */
	public abstract boolean isSatisfiable() throws Exception;
		
	/**
	 * Gets the next Solution (as a list of variable IDs that are true).
	 * @return the next solution.
	 * @throws Exception general exception.
	 */ 
	public abstract int[] getNextSolution() throws Exception;
	
	
	/**
	 * Gets the Next Repair, as a list of variable names to be deleted.
	 * @return the variables to be deleted.
	 * @throws Exception general exception.
	 */
	public abstract Collection<String> getNextRepair() throws Exception;
}
