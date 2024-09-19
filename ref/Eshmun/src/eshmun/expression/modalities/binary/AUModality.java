package eshmun.expression.modalities.binary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.BinaryCTLModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an AU CTL Modality. (Until)
 * 
 * <p>An AU Modality has the following form A[leftChild U rightChild], where all children are AbstractExpressions.</p>
 * 
 * <p>It means that the leftChild has to hold <b>at least</b> until rightChild holds on all the branches.</p>
 *  
 * <p>Simplification, negation and equality is based on A(l U r) = !( E( (!l) V (!r) ) ).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.BinaryCTLModality
 * @see eshmun.expression.modalities.binary.EVModality
 * @see eshmun.expression.AbstractExpression
 */
public class AUModality extends BinaryCTLModality {
	/**
	 * Create a new AUModality with the given child.
	 * @param leftChild The left child expression of this modality.
	 * @param rightChild The right child expression of this modality.
	 */
	public AUModality(AbstractExpression leftChild, AbstractExpression rightChild) {
		super(ExpressionType.AUModality, leftChild, rightChild);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according !A(l U r) = E( (!l) V (!r) ).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.binary.EVModality
	 */
	@Override
	public AbstractExpression negate() {
		return simplify().negate(); //Double negation cancels.
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to A(l U r) = !( E( (!l) V (!r) ) ).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.binary.EVModality
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public AbstractExpression simplify() {
		AbstractExpression ev = new EVModality(leftChild.negate(), rightChild.negate());
		return new NotOperator(ev);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return new AUModality(leftChild.toNNF(), rightChild.toNNF());
	}

	
	/**
	 * Clones this AUModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one AUModality.</p>
	 * 
	 * @return a shallow copy of this AUModality.
	 */
	@Override
	public AUModality clone() {
		return new AUModality(leftChild, rightChild);
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
