package eshmun.expression.modalities.binary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.BinaryCTLModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an AW CTL Modality. (Weak until)
 * 
 * <p>An AW Modality has the following form A[leftChild W rightChild], where all children are AbstractExpressions.</p>
 * 
 * <p>It means that the leftChild has to hold until rightChild holds on all the branches. However, rightChild might never hold.</p>
 * 
 * <p>Simplification, negation and equality is based on A(l W r) = A(r V (l OR r) ).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.BinaryCTLModality
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.expression.operators.OrOperator
 * @see eshmun.expression.modalities.binary.AVModality
 */
public class AWModality extends BinaryCTLModality {
	/**
	 * Create a new AWModality with the given child.
	 * @param leftChild The left child expression of this modality.
	 * @param rightChild The right child expression of this modality.
	 */
	public AWModality(AbstractExpression leftChild, AbstractExpression rightChild) {
		super(ExpressionType.AWModality, leftChild, rightChild);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according to !A(l W r) = !A(r V (l OR r) ).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 * @see eshmun.expression.operators.OrOperator
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this.simplify());
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to A(l W r) = A(r V (l OR r) ).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	@Override
	public AbstractExpression simplify() {
		AbstractExpression right = rightChild.simplify();
		AbstractExpression left = leftChild.simplify();
		
		OrOperator orOp = new OrOperator(left, right);
		return new AVModality(right, orOp);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return simplify().toNNF();
	}

	
	/**
	 * Clones this AWModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one AWModality.</p>
	 * 
	 * @return a shallow copy of this AWModality.
	 */
	@Override
	public AWModality clone() {
		return new AWModality(leftChild, rightChild);
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
