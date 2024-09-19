package eshmun.expression.modalities.unary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an AF CTL Modality. (Finally)
 * 
 * <p>An AF Modality has the following form AF(child), where child is an AbstractExpression.</p>
 * 
 * <p>It means that the formula child has to hold eventually on all branches of the computation tree.</p>
 * 
 * <p>Simplification, negation and equality is based on AF(f) = A(true U f).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.UnaryCTLModality
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.expression.modalities.binary.AUModality
 */
public class AFModality extends UnaryCTLModality {
	/**
	 * Create a new AFModality with the given child.
	 * @param child The child expression of this modality.
	 */
	public AFModality(AbstractExpression child) {
		super(ExpressionType.AFModality, child);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according !(AF(f)) = !(A(true U f))</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.binary.AUModality
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this.simplify());
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to AF(f) = A(true U f)</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.binary.AUModality
	 */
	@Override
	public AbstractExpression simplify() {
		BooleanLiteral literal = new BooleanLiteral(true);
		return new AUModality(literal, child).simplify();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return simplify().toNNF();
	}

	/**
	 * Clones this AFModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one AFModality.</p>
	 * 
	 * @return a shallow copy of this AFModality.
	 */
	@Override
	public AFModality clone() {
		return new AFModality(child);
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
