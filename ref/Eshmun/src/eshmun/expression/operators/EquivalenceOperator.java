package eshmun.expression.operators;

import java.util.ArrayList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an Equivalence between multiple propositions.
 * An Equivalence is on the form: P &lt;=&gt; Q.
 * Where P, Q are both AbstractExpressions.
 *
 * <p>An Equivalence can be simplified into (P =&gt; Q) &amp; (Q =&gt; P),
 * which is consequently simplified into (!P or Q) &amp; (!Q or P).</p>
 * 
 * <p>The Equivalence can be turned into CNF by simplifying, then turning the resulting AndOperator into CNF.</p>
 * 
 * <p>Notice that Equivalence is commutative.</p>
 * 
 * <p>Note That Equivalence is not necessarily Binary, It is possible to have many children. 
 * E.g. X1 &lt;=&gt; X2 &lt;=&gt; ... &lt;=&gt; Xn </p>
 *  
 * <p> In this case the Equivalence can be simplified into a conjunction of equivalences.
 * (X1 &lt;=&gt; X2) &amp; (X2 &lt;=&gt; X3) &amp; ... &amp; (Xn-1 &lt;=&gt; Xn)   
 * </p>
 * 
 * <p>
 * <i>Invariant</i>: it is guaranteed that no direct child of any EquivalenceOperator is an EquivalenceOperator.
 * </p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.operators.OrOperator
 * @see eshmun.expression.operators.NotOperator
 * @see eshmun.expression.operators.AndOperator
 */
public class EquivalenceOperator extends AbstractExpression {
	public static final char EQU_CHAR = '\u21D4';

	/**
	 * The Identity element for this operator. 
	 */
	public static final BooleanLiteral IDENTITY = new BooleanLiteral(true);
	
	/**
	 * All the children of the Equivalence as a list.
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any EquivalenceOperator is an EquivalenceOperator.
	 * </p>
	 */
	ArrayList<AbstractExpression> children;
	
	/**
	 * Create a new empty EquivalenceOperator.
	 */
	public EquivalenceOperator() {
		super(ExpressionType.EquivalenceOperator);
		
		this.children = new ArrayList<AbstractExpression>();
	}
	
	/**
	 * Create a new EquivalenceOperator that with given children.
	 * 
	 * <p>if any of the children is an EquivalenceOperator its children will be added
	 * For example (X &lt;=&gt; Y) &lt;=&gt; Z will yield (X &lt;=&gt; Y &lt;=&gt; Z). This is to ensure simplicity.
	 * </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any EquivalenceOperator is an EquivalenceOperator.
	 * </p>
	 * 
	 * @param children the children of the EquivalenceOperator.
	 */
	public EquivalenceOperator(AbstractExpression...children) {
		super(ExpressionType.EquivalenceOperator);
		
		this.children = new ArrayList<AbstractExpression>(children.length);
		
		for(AbstractExpression exp : children) {
			if(exp.getType() == ExpressionType.EquivalenceOperator) {
				for(AbstractExpression e : ((EquivalenceOperator) exp).children) {
					this.children.add(e);
				}
			} else {
				this.children.add(exp);
			}
		}
	}
	
	/**
	 * Adds a new clause to the Equivalence statement.
	 * e.g: (X &lt;=&gt; Y).equate(Z) =&gt; X &lt;=&gt; Y &lt;=&gt; Z.
	 * 
	 * <p>if the child is an Equivalence its children will be added
	 * For example Z.equate(X &lt;=&gt; Y) will yield (Z &lt;=&gt; X &lt;=&gt; Y). This is to ensure simplicity.
	 * </p>
	 * 
	 * <p>
	 * <i>Invariant</i>: it is guaranteed that no direct child of any EquivalenceOperator is an EquivalenceOperator.
	 * </p>
	 * 
	 * @param exp the child to be added.
	 */
	public void equate(AbstractExpression exp) {
		if(exp.getType() == ExpressionType.EquivalenceOperator) {
			for(AbstractExpression e : ((EquivalenceOperator) exp).children) {
				this.children.add(e);
			}
		} else {
			this.children.add(exp);
		}
	}
	
	/**
	 * Getter for the children of this Equivalence.
	 * @return the children.
	 */
	public AbstractExpression[] getChildren() {
		return children.toArray(new AbstractExpression[children.size()]);
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
	 * @return true if any of the children contains CTL, false otherwise.
	 */
	@Override
	public boolean containsCTL() {
		for(AbstractExpression child : children) {
			if(child.containsCTL())
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return true if any of the children contains the given variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(BooleanVariable variable) {
		for(AbstractExpression child : children) {
			if(child.containsVariable(variable))
				return true;
		}
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return true if any of the children contains the given variable, false otherwise.
	 */
	@Override
	public boolean containsVariable(String variableName) {
		for(AbstractExpression child : children) {
			if(child.containsVariable(variableName))
				return true;
		}
		
		return false;	}
	

	/**
	 * Negates this expression, the result is guaranteed to be an OrOperator.
	 * 
	 * <p>P &lt;=&gt; Q is equivalent to (!P or Q) and (!Q or P),
	 * By DeMoragn we get that the negation should be (P and !Q) or (Q and !P)
	 * Where P, Q are the children respectively.</p>
	 * 
	 * <p>The same applies to the case with more than one equivalence.</p>
	 * 
	 * @return an OrOperator that is the negation of this Equivalence.
	 * 
	 * @see eshmun.expression.operators.OrOperator
	 */
	@Override
	public AbstractExpression negate() {
		return simplify().negate();
	}

	/**
	 * Simplifies this expression and returns the result, the result is guaranteed to be an AndOperator
	 *  if children count was greater than 1.
	 *  
	 * <p>P &lt;=&gt; Q is equivalent to (!P or Q) and (!Q or P), 
	 * However this in itself is not simplified enough (P, Q can be complex expressions).
	 * Thus the resulting AndOperator is simplified using AndOperator.simplify(),
	 * which guarantees the result's type to be an AndOperator. </p>
	 * 
	 * <p>The same applies to the case with more than one equivalence.</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.operators.AndOperator
	 */
	@Override
	public AbstractExpression simplify() {
		if(children.size() == 1) {
			return children.get(0).simplify();
		} else if(children.size() == 0) {
			return IDENTITY;
		}
		
		AndOperator andOp = new AndOperator();
		
		for(int i = 0; i < children.size() - 1; i++) {
			AbstractExpression first = children.get(i);
			AbstractExpression second = children.get(i+1);
			
			andOp.and(new OrOperator(new NotOperator(first), second));
			andOp.and(new OrOperator(first, new NotOperator(second)));
		}
		
		return andOp.simplify();
	}

	/**
	 * Converts the current expression to CNF. The result type is guaranteed to be an AndOperator. 
	 * <p> P &lt;=&gt; Q is equivalent (!P or Q) and (!Q or P). However, this is not yet guaranteed to be CNF.
	 * Thus the resulting AndOperator is converted to CNF by using AndOperator.toCNF() . </p>
	 * 
	 * @return an equivalent AndOperator in CNF Format.
	 * 
	 * @see eshmun.expression.operators.AndOperator
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
		if(children.size() == 1) {
			return children.get(0).toNNF();
		} else if(children.size() == 0) {
			return IDENTITY;
		}
		
		AndOperator andOp = new AndOperator();
		
		for(int i = 0; i < children.size() - 1; i++) {
			AbstractExpression first = children.get(i);
			AbstractExpression second = children.get(i+1);
			
			andOp.and(new OrOperator(new NotOperator(first), second));
			andOp.and(new OrOperator(first, new NotOperator(second)));
		}
		
		return andOp.toNNF();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		EquivalenceOperator equiv = (EquivalenceOperator) exp;
		return children.containsAll(equiv.children) && equiv.children.containsAll(children);
	}

	/**
	 * Clones this EquivalenceOperator.
	 * 
	 * <p>Please note that while the operator is copied, the children are not.
	 * Changes in a child could affect more than one EquivalenceOperator.</p>
	 * 
	 * @return a shallow copy of this EquivalenceOperator.
	 */
	@Override
	public EquivalenceOperator clone() {
		return new EquivalenceOperator(getChildren());
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
