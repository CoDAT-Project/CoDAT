package eshmun.expression.modalities.unary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an AG CTL Modality. (Globally)
 * 
 * <p>An AG Modality has the following form AG(child), where child is an AbstractExpression.</p>
 * 
 * <p>It means that the formula child has to always hold on all branches of the computation tree.</p>
 *  
 * <p>Simplification, negation and equality is based on AG(f) = A(false V f).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.UnaryCTLModality
 * @see eshmun.expression.modalities.binary.AVModality
 * @see eshmun.expression.AbstractExpression
 */
public class AGModality extends UnaryCTLModality {
	/**
	 * Create a new AGModality with the given child.
	 * @param child The child expression of this modality.
	 */
	public AGModality(AbstractExpression child) {
		super(ExpressionType.AGModality, child);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according to !AG(f) = !A(false V f).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.binary.AVModality
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this.simplify());
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to AG(f) = A(false V f).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	@Override
	public AbstractExpression simplify() {
		BooleanLiteral literal = new BooleanLiteral(false);
		return new AVModality(literal, child).simplify();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return simplify().toNNF();
	}
	
	/**
	 * Clones this AGModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one AGModality.</p>
	 * 
	 * @return a shallow copy of this AGModality.
	 */
	@Override
	public AGModality clone() {
		return new AGModality(child);
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
