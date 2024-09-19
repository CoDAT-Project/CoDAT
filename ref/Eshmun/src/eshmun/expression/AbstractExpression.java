package eshmun.expression;

import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.visitor.Visitable;
import eshmun.expression.visitor.Visitor;
import eshmun.expression.visitor.visitors.FillDocumentVisitor;
import eshmun.expression.visitor.visitors.PreFixStringVisitor;

/**
 * This class consists of abstract methods that help facilities dealing with boolean expression.
 * 
 * <p>All boolean expressions inherit from this class (directly or indirectly). </p>
 * 
 * <p>Boolean expression has many sub-types: <ul>
 * 		<li>CTL Modalities: AG, AX, ...</li>
 * 		<li>Boolean Logic Operators: And, Or, Not, ...</li>
 * 		<li>Literals: True, False.</li>
 * 		<li>Variables: q, p, x, ...</li>
 * 	</ul>
 * </p>
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.ExpressionType
 */

public abstract class AbstractExpression implements Visitable {
	/**
	 * Stores the current valuation of this expression.
	 */
	protected Boolean valuation;
	
	/**
	 * Stores the type of the expression.
	 * 
	 * @see eshmun.expression.ExpressionType
	 */
	protected final ExpressionType type;
	
	/**
	 * Default Constructor; Should be used by all subclasses.
	 * @param type The type of the expression.
	 */
	protected AbstractExpression(ExpressionType type) {
		valuation = null;
		
		this.type = type;
	}
	
	/**
	 * Gets the type of the expression.
	 * @return the type of the expression.
	 * 
	 * @see eshmun.expression.ExpressionType
	 */
	public ExpressionType getType() {
		return type;
	}
	
	/**
	 * Checks whether the given expression is an atomic proposition.
	 * An atomic proposition is either a literal, variable, or the negation of an atomic variable.
	 * @return true if this is an atomic proposition, false otherwise.
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 * @see eshmun.expression.atomic.BooleanLiteral
	 * @see eshmun.expression.operators.NotOperator
	 */
	public abstract boolean isAtomic();
	
	/**
	 * Checks whether the given expression is a BooleanVariable. 
	 * @return true if this is a BooleanVariable, false otherwise.
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	public abstract boolean isBooleanVariable();
	
	/**
	 * Checks whether the current Expression is CNF.
	 * @return true if this is CNF, false otherwise.
	 * 
	 */
	public abstract boolean isCNF();
	
	/**
	 * Checks whether the current Expression is a CTL Modality.
	 * @return true if this is a CTL Modality, false otherwise.
	 */
	public abstract boolean isCTL();
	
	/**
	 * Checks whether the current expression is a literal (True, False).
	 * @return true if this is a literal, false otherwise.
	 * 
	 * @see eshmun.expression.atomic.BooleanLiteral
	 */
	public abstract boolean isLiteral();
	
	/**
	 * Checks whether the current expression is the negation of the given expression.
	 * @param expression The expression to check negation against.
	 * @return true if this is the negation of expression, false otherwise.
	 * 
	 */
	public boolean isNegationOf(AbstractExpression expression) {
		expression = expression.simplify();
		return expression.equals(this.negate());
	}
	
	/**
	 * Checks whether the current expression contain some CTL Modality.
	 * @return true if this is a CTL Modality, or if some term in it is, false otherwise.
	 */
	
	public abstract boolean containsCTL();
	
	/**
	 * Checks whether the current expression contains a given variable or not.
	 * @param variable The variable to check if is in this expression.
	 * @return true if this contains the given variable, false otherwise.
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	public abstract boolean containsVariable(BooleanVariable variable);
	
	/**
	 * Checks whether the current expression contains a given variable or not.
	 * @param variableName The name of the variable to check if is in this expression.
	 * @return true if this contains the given variable, false otherwise.
	 * 
	 * @see eshmun.expression.atomic.BooleanVariable
	 */
	public abstract boolean containsVariable(String variableName);
	
	/**
	 * Negates this expression, the result is a simplified expression and not always a NotOperater.
	 * The exact type and formulation of the returned expression depends on the class implementing this method. 
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.operators.NotOperator
	 */
	public abstract AbstractExpression negate();
	
	/**
	 * Simplifies this expression and returns the result, this operation doesn't preserve the type of the expression.
	 * The exact type and formulation of the returned expression depends on the class implementing this method. 
	 * @return a simplified equivalent expression.
	 */
	public abstract AbstractExpression simplify();
	
	/**
	 * Converts the current expression to CNF.
	 * 
	 * <p>This method is guaranteed to return an equivalent expression. However if 
	 * the original expression contained CTL Modalities then this method is not guaranteed
	 * to return a valid CNF Formula.</p>
	 * 
	 * @return an expression that is the CNF equivalent of this expression.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Conjunctive_normal_form#Conversion_into_CNF">Conversion into CNF</a>
	 */
	public abstract AbstractExpression toCNF();
	
	
	/**
	 * Converts the current expression to NNF.
	 * 
	 * <p>This method is guaranteed to return an equivalent expression.</p>
	 * 
	 * <p>Negative Normal Form has the negations adjacent to the variables (inside). Implications and Equivalnces are 
	 * simplified into ands, ors, and nots. </p>
	 * 
	 * <p>As for CTL Modalities, This method will reduce everything down to AX, EX, AU, EU.</p>
	 * 
	 * @return an expression that is the CNF equivalent of this expression.
	 */
	public abstract AbstractExpression toNNF();

	/**
	 * Getter for valuation.
	 * @return returns the current valuation of this expression.
	 */
	public Boolean getValuation() {
		return valuation;
	}
	
	/**
	 * Setter for valuation.
	 * @param valuation The new valuation of this expression.
	 */
	public void setValuation(Boolean valuation) {
		this.valuation = valuation;
	}
	
	/**
	 * Equality check.
	 * 
	 * <p>The internals of the implementation of this method depends on the implementing class.</p>
	 * 
	 * <p>Comparison is not done based on pointers rather than based on semantic meaning.
	 * Two Expressions are equal if they consist of the same variables (based on Name), and if 
	 * these variables are connected in the same way.</p>
	 * 
	 * <p>Notice that certain Operators are commutative, this doesn't change the equality of 
	 * two Expression.</p>
	 * 
	 * <p>If the given object is an expression of the same type as this, their contents is directly
	 * compared, if it is of a different expression type, then the given object and this are both simplified,
	 * if their simplifications are of the same type they are compared, otherwise return false.</p>
	 * 
	 * @return true if this and the passed object are semantically equal (as Expressions), false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		
		if(!(obj instanceof AbstractExpression))
			return false;
		
		AbstractExpression exp = (AbstractExpression) obj;
		if(this.getType() == exp.getType()) {
			return directCompare(exp);
		}
		
		exp = exp.simplify();
		AbstractExpression tmp = simplify();
		if(exp.getType() == tmp.getType())
			return tmp.directCompare(exp);
					
		return false;
	}
	
	/**
	 * Equality check, this method is only called if the passed expression has the same type as this.
	 * 
	 * <p>The internals of the implementation of this method depends on the implementing class.</p>
	 * 
	 * <p>Comparison is not done based on pointers rather than based on semantic meaning.
	 * Two Expressions are equal if they consist of the same variables (based on Name), and if 
	 * these variables are connected in the same way.</p>
	 * 
	 * <p>Notice that certain Operators are commutative, this doesn't change the equality of 
	 * two Expression.</p>
	 * 
	 * @param exp the expression to compare with this.
	 * 
	 * @return true if this and the passed object are semantically equal (as Expressions), false otherwise.
	 */
	protected abstract boolean directCompare(AbstractExpression exp); 
	
	/**
	 * Clones an Expression.
	 * 
	 * <p>Gives a shadow copy of this expression, Changes that happen to children or mutable 
	 * attributes can still affect more than one Expression.</p>
	 * 
	 * <p>The copy has the same type as this.</p>
	 * 
	 * @return a shallow copy of this.
	 */
	@Override
	public abstract AbstractExpression clone();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void accept(Visitor visitor);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void acceptChildren(Visitor visitor);
	
	
	/**
	 * Returns a String representation of this expression, the string is equivalent 
	 * to the expression and is in prefix format.
	 * 
	 * @return string representation of this expression in prefix.
	 * 
	 * @see eshmun.expression.visitor.visitors.PreFixStringVisitor
	 */
	@Override
	public String toString() {
		PreFixStringVisitor tsv = new PreFixStringVisitor(this);
		return tsv.toString();
	}
	
	public String toString(boolean infix) {
		if(!infix)
			return toString();
		
		FillDocumentVisitor doc = new FillDocumentVisitor(null, true);
		accept(doc);
		return doc.getString();
	}
}
