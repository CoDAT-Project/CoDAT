package eshmun.expression.modalities;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;

/**
 * This class consists of abstract methods that help facilities dealing with CTL Modalities.
 * 
 * <p>All CTL Modalities inherit from this class (directly or indirectly). </p>
 * 
 * <p>All CTL Modalities used are simplified into a <b>minimal set of modalities</b>: <br>
 * <center>{AV, EV, EX}</center> <br>
 * This is helpful when simplifying, negating, or checking for equality between modalities. </p>
 * 
 *   
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.AbstractExpression
 * @see <a href="http://en.wikipedia.org/wiki/Computation_tree_logic#Minimal_set_of_operators">Minimal set of modalities</a>
 */
public abstract class AbstractCTLModality extends AbstractExpression {
	/**
	 * Default Constructor; Should be used by all subclasses.
	 * @param type The type of the expression.
	 */
	public AbstractCTLModality(ExpressionType type) {
		super(type);
	}
	
	/**
	 * {@inheritDoc}
	 * @return false
	 */
	@Override
	public boolean isAtomic() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @return false
	 */
	@Override
	public boolean isBooleanVariable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false
	 */
	@Override
	public boolean isCNF() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true
	 */
	@Override
	public boolean isCTL() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @return false
	 */
	@Override
	public boolean isLiteral() {
			return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true
	 */
	@Override
	public boolean containsCTL() {
		return true;
	}
}
