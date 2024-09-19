package eshmun.structures;

import java.util.List;

import eshmun.expression.AbstractExpression;
import eshmun.expression.NotCNFException;
import eshmun.expression.operators.AndOperator;

/**
 * 
 * This interface represents a Repairable Entity.
 * 
 * <p>Any class that implements this must implement the following methods:
 * <ul>
 * 	<li>boolean modelCheck(Collection&lt;AbstractExpression&gt;): <br>
 * 		model checks the structure against the given specifications.</li>
 * 	<li>AbstractExpression generateRepairFormula(Collection&lt;AbstractExpression&gt;): <br>
 * 		generate an optimized simplified repair formula in CNF form against the given specifications.</li>
 * </ul>
 * </p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
*/
public interface Repairable {
	/**
	 * Sets the CNF flag. determines if the repair formula must be generated in CNF format.
	 * @param cnf if true the repair formula will be generated in CNF (false if for debugging purposes).
	 */
	public abstract void setCNF(boolean cnf);
		
	/**
	 * Model checks this structure against the specifications.
	 * 
	 * <p>If this is a single structure, the specifications are conjuncted into one big specification.</p>
	 * <p>If this contains multiple structures, the specifications are each mapped to an inner structure
	 * then model checked separately.</p>
	 * 
	 * <p>The implementation details are determined in the sub-classes.</p>
	 * 
	 * @param specifications the specifications of this structure.
	 * @return true if the structure abides by the specifications, false otherwise.
	 * 
	 * @see eshmun.expression.AbstractExpression
	 */
	public abstract boolean modelCheck(List<AbstractExpression> specifications);
	
	/**
	 * Generates the repair formula for this structure against the specifications.
	 * 
	 * <p>If this is a single structure, the specifications are conjuncted into one big specification, then 
	 * the repair formula is generated against it.</p>
	 * <p>If this contains multiple structures, the specifications are each mapped to an inner structure, each
	 * structure then generates its own repair formula separately then they are all conjuncted, this helps avoid
	 * Exponential blow-ups.</p>
	 * 
	 * <p>The implementation details are determined in the sub-classes.</p>
	 * 
	 * @param specifications the specifications of this structure.
	 * @param actionSync flags whether or not to sync based on action names.
	 * @param processSync flags whether or not to sync on process labels combined with from and to states labels.
	 * @return the repair formula of this structure against the specifications.
	 * @throws NotCNFException if some error occurred during conversion of specifications to cnf.
	 * @throws IllegalArgumentException if the specifications contained labels not present in the structure.
	 * 
	 * @see eshmun.expression.AbstractExpression
	 * @see eshmun.structures.kripke.MultiKripkeStructure#actionSynchronizationFormula()
	 * @see eshmun.structures.kripke.MultiKripkeStructure#processSynchronizationFormula()
	 */
	public abstract AndOperator generateRepairFormula(List<AbstractExpression> specifications, boolean actionSync, boolean processSync) throws NotCNFException;
}
