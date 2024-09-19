package eshmun.expression.operators;

import java.util.ArrayList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an And Operator.
 * 
 * <p>The and operator can have many children.
 * e.g.: X and Y and Z and ....</p>
 * 
 * <p>
 * <i>Invariant</i>: it is guaranteed that no direct child of any AndOperator is an AndOperator.
 * </p>
 *
 * <p>Notice that And is commutative.</p>
 *   
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class AndOperator extends AbstractExpression {
	public static final char AND_CHAR = '\u2227';

	/**
	 * The Identity element for this operator. 
	 */
	public static final BooleanLiteral IDENTITY = new BooleanLiteral(true);
	
	/**
	 * All the children of the And as a list.
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndOperator is an AndOperator.
	 * </p>
	 */
	private ArrayList<AbstractExpression> children;
	
	/**
	 * Create a new empty AndOperator.
	 */
	public AndOperator() {
		super(ExpressionType.AndOperator);
		this.children = new ArrayList<AbstractExpression>();
	}
	
	/**
	 * Create a new AndOperator with the given children.
	 * 
	 * <p>if any of the children is an AndOperator its children will be added
	 * For example (X and Y) and Z will yield (X and Y and Z). This is to ensure simplicity.
	 * </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndOperator is an AndOperator.
	 * </p>
	 * 
	 * @param children the children of the AndOperator.
	 */
	public AndOperator(AbstractExpression...children) {
		super(ExpressionType.AndOperator);
				
		this.children = new ArrayList<AbstractExpression>(children.length);
		
		for(AbstractExpression exp : children) {
			if(exp.getType() == ExpressionType.AndOperator) {
				for(AbstractExpression e : ((AndOperator) exp).children) {
					this.children.add(e);
				}
			} else {
				this.children.add(exp);
			}
		}
	}
	
	/**
	 * Adds a new clause to the and statement.
	 * e.g: (X and Y).and(Z) =&gt; X and Y and Z.
	 * 
	 * <p>if the child is an AndOperator its children will be added
	 * For example Z.and(X and Y) will yield (Z and X and Y). This is to ensure simplicity.
	 * </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any AndOperator is an AndOperator.
	 * </p>
	 * 
	 * @param exp the child to be added.
	 */
	public void and(AbstractExpression exp) {
		if(exp.getType() == ExpressionType.AndOperator) {
			for(AbstractExpression e : ((AndOperator) exp).children) {
				this.children.add(e);
			}
		} else {
			this.children.add(exp);
		}
	}

	/**
	 * Getter for the children of this and.
	 * @return the children.
	 */
	public AbstractExpression[] getChildren() {
		return children.toArray(new AbstractExpression[children.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @return false.
	 */

	@Override
	public boolean isAtomic() {
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isBooleanVariable() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if all the children are CNF, false otherwise.
	 */
	@Override
	public boolean isCNF() {
		for(AbstractExpression exp : children) {
			if(!exp.isCNF())
				return false;
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isCTL() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return false.
	 */
	@Override
	public boolean isLiteral() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if one child or more contains a CTL, false otherwise.
	 */
	@Override
	public boolean containsCTL() {	
		for(AbstractExpression exp : children) {
			if(exp.containsCTL())
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if one or more children contains the variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		for(AbstractExpression exp : children) {
			if(exp.containsVariable(variable))
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @return true if one or more children contains the variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		for(AbstractExpression exp : children) {
			if(exp.containsVariable(variableName))
				return true;
		}
		
		return false;
	}

	/**
	 * Applies DeMorgan's law to get the negation, which is guaranteed to be an OrOperator.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression negate() {
		OrOperator orOp = new OrOperator();
		
		for(AbstractExpression exp : children) {
			orOp.or(exp.negate());
		}
		
		return orOp;
	}

	/**
	 * {@inheritDoc}
	 * @return a simplified equivalent expression, guaranteed to be an AndOperator as well if children count was greater than 1.
	 */
	@Override
	public AbstractExpression simplify() {
		if(children.size() == 1) {
			return children.get(0).simplify();
		} else if(children.size() == 0) {
			return IDENTITY;
		}
		
		AndOperator andOp = new AndOperator();
		
		for(AbstractExpression exp : children) {
			andOp.and(exp.simplify());
		}
		
		return andOp;
	}

	/**
	 * {@inheritDoc} 
	 * @return an expression that is the CNF equivalent of this expression, guaranteed to be an AndOperator.
	 */
	@Override
	public AbstractExpression toCNF() {
		AndOperator andOp = new AndOperator();
		
		for(AbstractExpression exp : children) {
			andOp.and(exp.toCNF());
		}
		
		return andOp;
	}
	
	/**
	 * {@inheritDoc}
	 * @return an equivalent NNF expression.
	 */
	@Override
	public AbstractExpression toNNF() {
		if(children.size() == 1) {
			return children.get(0).toNNF();
		} else if(children.size() == 0) { 
			return IDENTITY;
		}
		
		AndOperator andOp = new AndOperator();
		
		for(AbstractExpression exp : children) {
			andOp.and(exp.toNNF());
		}
		
		return andOp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		AndOperator andOp = (AndOperator) exp;
		
		return andOp.children.containsAll(children) && children.containsAll(andOp.children);
	}
	
	/**
	 * Clones this AndOperator.
	 * 
	 * <p>Please note that while the array of children is copied, the children themselves are not.
	 * Changes in one of the children could affect more than one AndOperator.</p>
	 * 
	 * @return a shallow copy of this AndOperator.
	 */
	@Override
	public AndOperator clone() {
		return new AndOperator(getChildren());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
		for(AbstractExpression e : children) {
			e.accept(visitor);
			visitor.childrenSeparator();
		}
		
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		for(AbstractExpression e : children) {
			e.accept(visitor);
			visitor.childrenSeparator();
		}
		
		visitor.visit(this);
		visitor.separator();
	}
}
