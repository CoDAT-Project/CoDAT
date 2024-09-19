package eshmun.expression.modalities.binary;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.BinaryCTLModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an AV CTL Modality. (Release)
 * 
 * <p>An AV Modality has the following form A[leftChild V rightChild], where all children are AbstractExpressions.</p>
 * 
 * <p>V is the dual of U, i.e. (leftChild V rightChild) whenever (!leftChild U !rightChild) is false.</p>
 * 
 * <p>Notice that this is part of the minimal set of modalities, therefore it cannot be simplified.
 * Simplifying this modality will only result in simplifying its children.</p> 
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.BinaryCTLModality
 * @see eshmun.expression.modalities.binary.EUModality
 * @see eshmun.expression.AbstractExpression
 */
public class AVModality extends BinaryCTLModality {
	/**
	 * Since AV modality can also be written as AR modality (alias), 
	 * this field is used to tell which alias is used (for display purposes). 
	 */
	private String symbol;
	
	/**
	 * Create a new AVModality with the given child.
	 * @param leftChild The left child expression of this modality.
	 * @param rightChild The right child expression of this modality.
	 */
	public AVModality(AbstractExpression leftChild, AbstractExpression rightChild) {
		this(leftChild, rightChild, "V");
	}
	
	/**
	 * Create a new AVModality with the given child.
	 * @param leftChild The left child expression of this modality.
	 * @param rightChild The right child expression of this modality.
	 * @param symbol which symbol was used (V or R).
	 */
	public AVModality(AbstractExpression leftChild, AbstractExpression rightChild, String symbol) {
		super(ExpressionType.AVModality, leftChild, rightChild);
		this.symbol = symbol;
	}
	
	/**
	 * @return The symbol used when creating this modality (V or R).
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Negates this expression.
	 * 
	 * <p>This is one of minimal modalities, negation is done by passing this to a not operator.</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	@Override
	public AbstractExpression negate() {
		return new NotOperator(this.simplify());
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>This is one of the minimal modalities, simplification is simplifying the children.</p>
	 * @return a simplified equivalent expression.
	 */
	@Override
	public AbstractExpression simplify() {
		return new AVModality(leftChild.simplify(), rightChild.simplify());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return new AVModality(leftChild.toNNF(), rightChild.toNNF());
	}
	
	/**
	 * Reduces this Modality into an EU Modality using A[pVq] == !E[!pU!q].
	 * @return An Equivalent negated EUModality.
	 */
	public NotOperator reduceToEU() {
		return new NotOperator(new EUModality(getLeftChild().negate(), getRightChild().negate()));
	}

	/**
	 * Clones this AVModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one AVModality.</p>
	 * 
	 * @return a shallow copy of this AVModality.
	 */
	@Override
	public AVModality clone() {
		return new AVModality(leftChild, rightChild);
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
