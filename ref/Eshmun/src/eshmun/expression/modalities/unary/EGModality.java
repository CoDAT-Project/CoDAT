package eshmun.expression.modalities.unary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an EG CTL Modality. (Globally)
 * 
 * <p>An EG Modality has the following form EG(child), where child is an AbstractExpression.</p>
 * 
 * <p>It means that the formula child has to always hold on some branch of the computation tree.</p>
 * 
 * <p>Simplification, negation and equality is based on EG(f) = E(false V f).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.UnaryCTLModality
 * @see eshmun.expression.modalities.binary.EVModality
 * @see eshmun.expression.AbstractExpression
 */
public class EGModality extends UnaryCTLModality {
	/**
	 * Create a new EGModality with the given child.
	 * @param child The child expression of this modality.
	 */
	public EGModality(AbstractExpression child) {
		super(ExpressionType.EGModality, child);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according to !EG(f) = !E(false V f).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.binary.EVModality
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this.simplify());
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to EG(f) = E(false V f).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	@Override
	public AbstractExpression simplify() {
		BooleanLiteral literal = new BooleanLiteral(false);
		return new EVModality(literal, child).simplify();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return simplify().toNNF();
	}

	/**
	 * Clones this EGModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one EGModality.</p>
	 * 
	 * @return a shallow copy of this EGModality.
	 */
	@Override
	public EGModality clone() {
		return new EGModality(child);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
		child.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		child.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.visit(this);
		visitor.separator();
	}	
}
