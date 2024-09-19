package eshmun.expression.modalities.unary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an EF CTL Modality. (Finally)
 * 
 * <p>An EF Modality has the following form EF(child), where child is an AbstractExpression.</p>
 * 
 * <p>It means that the formula child has to hold eventually on some branch of the computation tree.</p>
 * 
 * <p>Simplification, negation and equality is based on EF(f) = E(true U f).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.UnaryCTLModality
 * @see eshmun.expression.modalities.binary.EUModality
 * @see eshmun.expression.AbstractExpression
 */
public class EFModality extends UnaryCTLModality {
	/**
	 * Create a new EFModality with the given child.
	 * @param child The child expression of this modality.
	 */
	public EFModality(AbstractExpression child) {
		super(ExpressionType.EFModality, child);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according !EF(f) = !E(true U f).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.binary.EUModality
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this.simplify());
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to EF(f) = E(true U f).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.binary.EUModality
	 */
	@Override
	public AbstractExpression simplify() {
		BooleanLiteral literal = new BooleanLiteral(true);
		return new EUModality(literal, child).simplify();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return simplify().toNNF();
	}

	/**
	 * Clones this EFModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one EFModality.</p>
	 * 
	 * @return a shallow copy of this EFModality.
	 */
	@Override
	public EFModality clone() {
		return new EFModality(child);
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
