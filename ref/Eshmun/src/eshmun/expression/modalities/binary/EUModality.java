package eshmun.expression.modalities.binary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.BinaryCTLModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an EU CTL Modality. (Until)
 * 
 * <p>An EU Modality has the following form E[leftChild U rightChild], where all children are AbstractExpressions.</p>
 * 
 * <p>It means that the leftChild has to hold <b>at least</b> until rightChild holds on some branch.</p>
 * 
 * <p>Simplification, negation and equality is based on E(l U r) = !( A( (!l) V (!r) ) ).</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.BinaryCTLModality
 * @see eshmun.expression.modalities.binary.AVModality
 * @see eshmun.expression.AbstractExpression
 */
public class EUModality extends BinaryCTLModality {
	/**
	 * Create a new EUModality with the given child.
	 * @param leftChild The left child expression of this modality.
	 * @param rightChild The right child expression of this modality.
	 */
	public EUModality(AbstractExpression leftChild, AbstractExpression rightChild) {
		super(ExpressionType.EUModality, leftChild, rightChild);
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>Negation is done according !E(l U r) = A( (!l) V (!r) ).</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.binary.AVModality
	 */
	@Override
	public AbstractExpression negate() {
		return simplify().negate(); //Double negation cancels.
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to E(l U r) = !( A( (!l) V (!r) ) ).</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.binary.AVModality
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public AbstractExpression simplify() {
		AbstractExpression av = new AVModality(leftChild.negate(), rightChild.negate());
		return new NotOperator(av);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return new EUModality(leftChild.toNNF(), rightChild.toNNF());
	}

	/**
	 * Clones this EUModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one EUModality.</p>
	 * 
	 * @return a shallow copy of this EUModality.
	 */
	@Override
	public EUModality clone() {
		return new EUModality(leftChild, rightChild);
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
