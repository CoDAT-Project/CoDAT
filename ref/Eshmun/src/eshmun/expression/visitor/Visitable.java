package eshmun.expression.visitor;

/**
 *
 * Interface to be implemented by all visitable elements.
 * 
 * @see eshmun.expression.AbstractExpression
 * @see eshmun.expression.visitor.Visitor
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public interface Visitable {
	/**
	 * Standard Visitor pattern. 
	 * Visit this first, then pass it to children.
	 * @param visitor the visitor to use.
	 */
	public void accept(Visitor visitor);
	
	/**
	 * Visitor pattern.
	 * Visit the children first, then this
	 * @param visitor the visitor to use.
	 */
	public void acceptChildren(Visitor visitor);

}
