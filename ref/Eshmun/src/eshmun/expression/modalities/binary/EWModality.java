package eshmun.expression.modalities.binary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.BinaryCTLModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an EW CTL Modality. (Weak until)
 * 
 * <p>An EW Modality has the following form E[leftChild W rightChild], where all children are AbstractExpressions.</p>
 * 
 * <p>It means that the leftChild has to hold until rightChild holds on some branch. However, rightChild might never hold.</p>
 * 
 * <p>Simplification, negation and equality is based on E(l W r) = E(r V (l OR r) ).</p>
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.BinaryCTLModality
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.expression.operators.OrOperator
 * @see eshmun.expression.modalities.binary.EUModality
 * @see eshmun.expression.modalities.unary.EGModality
 */
public class EWModality extends BinaryCTLModality {
	/**
	 * Create a new EWModality with the given child.
	 * @param leftChild The left child expression of this modality.
	 * @param rightChild The right child expression of this modality.
	 */
	public EWModality(AbstractExpression leftChild, AbstractExpression rightChild) {
		super(ExpressionType.EWModality, leftChild, rightChild);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according to !E(l W r) = !E(r V (l OR r) ).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.operators.OrOperator
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
	 * <p>Simplification is done according to E(l W r) = E(r V (l OR r) ).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	@Override
	public AbstractExpression simplify() {
		AbstractExpression right = rightChild.simplify();
		AbstractExpression left = leftChild.simplify();
		
		OrOperator orOp = new OrOperator(left, right);
		return new EVModality(right, orOp);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return simplify().toNNF();
	}
	
	/**
	 * Clones this EWModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one EWModality.</p>
	 * 
	 * @return a shallow copy of this EWModality.
	 */
	@Override
	public EWModality clone() {
		return new EWModality(leftChild, rightChild);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
		leftChild.accept(visitor);
		visitor.childrenSeparator();
		rightChild.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		leftChild.accept(visitor);
		visitor.childrenSeparator();
		rightChild.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.visit(this);
		visitor.separator();
	}	
}
