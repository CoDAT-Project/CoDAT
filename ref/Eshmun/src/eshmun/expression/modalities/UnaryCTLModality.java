package eshmun.expression.modalities;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanVariable;

/**
 * This class is the superclass of all the unary CTL modalities.
 * 
 * <p>Contains methods and stores the child of the modality as an AbstractExpression. </p>
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.AbstractCTLModality
 * @see eshmun.expression.AbstractExpression
 */
public abstract class UnaryCTLModality extends AbstractCTLModality {
	/**
	 * the child of this modality.
	 */
	protected AbstractExpression child;
	
	/**
	 * Default Constructor; Should be used by all subclasses.
	 * @param type The type of the expression.
	 * @param child The child of this modality.
	 */
	public UnaryCTLModality(ExpressionType type, AbstractExpression child) {
		super(type);
		
		this.child = child;
	}
	
	/**
	 * Getter for the child of this modality.
	 * @return child.
	 */
	public AbstractExpression getChild() {
		return child;
	}
	
	/**
	 * {@inheritDoc}
	 * @return true if child contains variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return child.containsVariable(variable);
	}
	
	/**
	 * {@inheritDoc}
	 * @return true if child contains variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		return child.containsVariable(variableName);
	}
	
	/**
	 * returns an equivalent expression.
	 * 
	 * <p>It is impossible to write a CTL modality in CNF. 
	 * Thus this method doesn't return a valid CNF Formula.</p>
	 * 
	 * <p>This method will re-write the child of this modality in CNF.
	 * And return a new instance of this modality with the new child. </p>
	 * 
	 * <p>Notice that even the child is not guaranteed to 
	 * be in CNF if the child contains CTL.</p>
	 * 
	 * @return an equivalent expression with the child in CNF.  
	 */
	@Override
	public AbstractExpression toCNF() {
		AbstractExpression tmp = child;
		
		child = child.toCNF();
		AbstractExpression result = this.clone().simplify();
		
		child = tmp;
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		UnaryCTLModality unary = (UnaryCTLModality) exp;
		return unary.child.equals(this.child);
	}
}
