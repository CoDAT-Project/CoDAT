package eshmun.sat;

import java.io.File;

import eshmun.expression.PredicateFormula;



/**
 * this is the interface for an SAT solver that is not implemented in java.
 * We need to call the SAT solver by spawning a java process that executes 
 * sat solver command. The input to all such SAT solvers is file in DIMACS .cnf
 * file. The .cnf file will contain the propositional formula that we will
 * solve its satisfiability valuation in CNF format. The output is also in .cnf
 * format where the valuation is encoded in the generated text from the executd
 * process.
 * @author Emile
 *
 */
public abstract class AbstractExternalSatSolver extends AbstractSatSolver {
	
	public AbstractExternalSatSolver(PredicateFormula predicate) {
		super(predicate);
	}
	
	@Override
	public boolean isSatisfiable() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * generates the DIMACS .cnf file to be input to the external SAT solver.
	 * @return the .cnf file
	 */
	private File generateCNFFile(){
		return null;
	}
	
	/**
	 * executes the command for the external SAT solver in java process and
	 * retrieves it result.
	 * @return
	 */
	protected abstract String executeSatSolver();
}
