package eshmun.expression.modalities;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanVariable;

/**
 * This class is the superclass of all the binary CTL modalities.
 * 
 * <p>Contains methods and stores the children of the modality as an AbstractExpression. </p>
 * 
 * <p>A Binary CTL modality is on the form: A|E(leftChild U|W|V rightChild).
 * </p>
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.AbstractCTLModality
 * @see eshmun.expression.AbstractExpression
 */
public abstract class BinaryCTLModality extends AbstractCTLModality {
	/**
	 * the left child of this modality.
	 */
	protected AbstractExpression leftChild;
	
	/**
	 * the right child of this modality.
	 */
	protected AbstractExpression rightChild;
	
	/**
	 * Default Constructor; Should be used by all subclasses.
	 * @param type The type of the expression.
	 * @param leftChild The left child of this modality.
	 * @param rightChild The right child of this modality.
	 */
	public BinaryCTLModality(ExpressionType type, AbstractExpression leftChild, AbstractExpression rightChild) {
		super(type);
		
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}
	
	/**
	 * Getter for the left child of this modality.
	 * @return leftChild.
	 */
	public AbstractExpression getLeftChild() {
		return leftChild;
	}
	
	/**
	 * Getter for the right child of this modality.
	 * @return rightChild.
	 */
	public AbstractExpression getRightChild() {
		return rightChild;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true if children contains variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return leftChild.containsVariable(variable) || rightChild.containsVariable(variable);
	}
	
	/**
	 * {@inheritDoc}
	 * @return true if children contains variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		return leftChild.containsVariable(variableName) || rightChild.containsVariable(variableName);
	}
	
	/**
	 * returns an equivalent expression.
	 * 
	 * <p>It is impossible to write a CTL modality in CNF. 
	 * Thus this method doesn't return a valid CNF Formula.</p>
	 * 
	 * <p>This method will re-write the children of this modality in CNF.
	 * And return a new instance of this modality with the new children. </p>
	 * 
	 * <p>Notice that a child is not guaranteed to 
	 * be in CNF if the child contains CTL.</p>
	 * 
	 * @return an equivalent expression with the children in CNF.  
	 */
	@Override
	public AbstractExpression toCNF() {
		AbstractExpression leftTmp = leftChild;
		AbstractExpression rightTmp = rightChild;
		
		leftChild = leftChild.toCNF();
		rightChild = rightChild.toCNF();
		
		AbstractExpression result = this.clone().simplify();
		
		leftChild = leftTmp;
		rightChild = rightTmp;
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		BinaryCTLModality binary = (BinaryCTLModality) exp;
		
		return binary.leftChild.equals(this.leftChild) && binary.rightChild.equals(this.rightChild);
	}
}
