package eshmun.expression.atomic;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents a BooleanLiteral, True or False.
 * 
 * <p>By definition this type is CNF, not CTL, Literal. </p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class BooleanLiteral extends AbstractExpression {
	private boolean value;
	
	/**
	 * Create a new BooleanLiteral with the given value.
	 * @param value The value of the new BooleanLiteral.
	 */
	public BooleanLiteral(boolean value) {
		super(ExpressionType.BooleanLiteral);
		
		this.value = value;
	}
	
	/**
	 * @return the value of this literal.
	 */
	public boolean getValue() {
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true.
	 */

	@Override
	public boolean isAtomic() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isBooleanVariable() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true.
	 */
	@Override
	public boolean isCNF() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true.
	 */
	@Override
	public boolean isLiteral() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean containsCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression negate() {
		return new BooleanLiteral(!this.value);
	}

	/**
	 * {@inheritDoc}
	 * @return this.
	 */
	@Override
	public AbstractExpression simplify() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 * @return this.
	 */
	@Override
	public AbstractExpression toCNF() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @return this.
	 */
	@Override
	public AbstractExpression toNNF() {
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		BooleanLiteral lit = (BooleanLiteral) exp;
		return lit.value == this.value;
	}
	
	/**
	 * Clones this BooleanLiteral.
	 *  
	 * @return a completely independent copy of this BooleanLiteral.
	 */
	@Override
	public BooleanLiteral clone() {
		return new BooleanLiteral(this.value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		visitor.visit(this);
		visitor.separator();
	}
}
