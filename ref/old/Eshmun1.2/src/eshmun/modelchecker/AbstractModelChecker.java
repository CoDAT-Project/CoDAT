package eshmun.modelchecker;

import java.util.Collection;
import java.util.List;

import eshmun.expression.PredicateFormula;
import eshmun.expression.visitor.IExpressionVisitor;
import eshmun.lts.kripke.Kripke;



/**
 * this class defines an abstract class representing different model checker
 * algorithm implementations.
 * deciding which model checker to choose should be an option in the config
 * of eshmun
 * @author Emile
 *
 */
public abstract class AbstractModelChecker implements IExpressionVisitor {

	/**
	 * model checks a kripke structure based on a list of CTL specification
	 * formulae. The user may override this class for specific implementation
	 * of the model checker. 
	 * @param kripke the structure to model check
	 * @param specifications a list of CTL specifications
	 * @return true or false if the kripke structure satisfies the spec
	 */
	public abstract boolean modelCheck(Kripke kripke, Collection<PredicateFormula> specifications);
}
