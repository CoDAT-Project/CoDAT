package eshmun.expression.operators;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an Implication between two propositions.
 * An Implication is on the form: P =&gt; Q.
 * Where P, Q are both AbstractExpressions.
 * 
 * <p>An Implication can be simplified into !P or Q.</p>
 * 
 * <p>The Implication can be turned into CNF by simplifying, 
 * then turning the resulting OrOperator into CNF.</p>
 * 
 * <p>Notice that Implication is not commutative. 
 * To chain Implications you can provide an Implication in the right child.</p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.operators.OrOperator
 * @see eshmun.expression.operators.NotOperator
 */
public class ImplicationOperator extends AbstractExpression {
	public static final char IMP_CHAR = '\u21D2';

	/**
	 * The leftChild in an Implication, The expression that implies the second.
	 */
	private AbstractExpression leftChild;
	
	/**
	 * The rightChild in an Implication, The expression that is being implied by an other.
	 */
	private AbstractExpression rightChild;
	
	/**
	 * Create a new Implication between leftChild to rightChild.
	 * leftChild =&gt; rightChild.
	 * 
	 * @param leftChild the left child of the implication.
	 * @param rightChild the right child of the implication.
	 */
	public ImplicationOperator(AbstractExpression leftChild, AbstractExpression rightChild) {
		super(ExpressionType.ImplicationOperator);
		
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	
	}
	
	/**
	 * Getter for the left child.
	 * @return the left child of the implication.
	 */
	public AbstractExpression getLeftChild() {
		return leftChild;
	}
	
	/**
	 * Getter for the right child.
	 * @return the right child of the implication.
	 */
	public AbstractExpression getRightChild() {
		return rightChild;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return false.
	 */
	@Override
	public boolean isAtomic() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return false.
	 */
	@Override
	public boolean isBooleanVariable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return false.
	 */
	@Override
	public boolean isCNF() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return false.
	 */
	@Override
	public boolean isCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return false.
	 */
	@Override
	public boolean isLiteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return true if leftChild or rightChild contains ctl, false otherwise.
	 */
	@Override
	public boolean containsCTL() {
		return leftChild.containsCTL() || rightChild.containsCTL();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return true if leftChild or rightChild contains the given variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		return leftChild.containsVariable(variable) || rightChild.containsVariable(variable);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return true if leftChild or rightChild contains the given variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		return leftChild.containsVariable(variableName) || rightChild.containsVariable(variableName);
	}
	
	/**
	 * Negates this expression, the result is guaranteed to be an AndOperator.
	 * 
	 * <p>P =&gt; Q is equivalent to !P or Q, By DeMoragn we get that the negation should be P and !Q.
	 * Where P, Q are the left and right child respectively.</p>
	 * 
	 * @return an AndOperator that is the negation of this Implication.
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	@Override
	public AbstractExpression negate() {
		return simplify().negate();	
	}

	/**
	 * Simplifies this expression and returns the result, the result is not guaranteed to be an OrOperator.
	 *  
	 * <p> P =&gt; Q is equivalent to !P or Q. However, this in itself is not simplified enough (P can be an expression).
	 * Thus the resulting OrOperator is simplified using OrOperator.simplify() . </p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 */
	@Override
	public AbstractExpression simplify() {
		NotOperator notOp = new NotOperator(leftChild);
		
		OrOperator orOp = new OrOperator(notOp, rightChild);
		return orOp.simplify();
	}

	/**
	 * Converts the current expression to CNF. The result type is not guaranteed.
	 * 
	 * <p> P =&gt; Q is equivalent to !P or Q. However, this is not yet guaranteed to be CNF.
	 * Thus the resulting OrOperator is converted to CNF by using OrOperator.toCNF() . </p>
	 * 
	 * @return an expression that is the CNF equivalent of this expression.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 * @see <a href="http://en.wikipedia.org/wiki/Conjunctive_normal_form#Conversion_into_CNF">Conversion into CNF</a>
	 */
	@Override
	public AbstractExpression toCNF() {
		return simplify().toCNF();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		NotOperator notOp = new NotOperator(leftChild);
		
		OrOperator orOp = new OrOperator(notOp, rightChild);
		return orOp.toNNF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		ImplicationOperator impOp = (ImplicationOperator) exp;
		return impOp.leftChild.equals(leftChild) && impOp.rightChild.equals(rightChild);
	}

	/**
	 * Clones this ImplicationOperator.
	 * 
	 * <p>Please note that while the operator is copied, the left and right children are not.
	 * Changes in a child could affect more than one ImplicationOperator.</p>
	 * 
	 * @return a shallow copy of this ImplicationOperator.
	 */
	@Override
	public ImplicationOperator clone() {
		ImplicationOperator imOp = new ImplicationOperator(leftChild, rightChild);
		
		return imOp;
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
