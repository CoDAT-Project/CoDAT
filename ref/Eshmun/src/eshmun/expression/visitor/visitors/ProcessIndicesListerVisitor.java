package eshmun.expression.visitor.visitors;

import java.util.ArrayList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.visitor.VisitorAdapter;

/**
 * Lists all the process Indices used in a given expression.
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class ProcessIndicesListerVisitor extends VisitorAdapter {
	/**
	 * This list will be filled with the indices.
	 */
	ArrayList<String> indices = new ArrayList<String>();
	
	/**
	 * Reset the visitor, should be used between multiple usages of the same
	 * instance of this visitor.
	 */
	public void reset() {
		indices = new ArrayList<String>();
	}
	
	/**
	 * Does nothing.
	 */
	@Override
	public void childrenSeparator() { }

	/**
	 * Does nothing.
	 */
	@Override
	public void separator() { }

	/**
	 * If visiting AX or EX, stores their process indices.
	 */
	@Override
	protected void visit(AbstractExpression v) {
		if(v.getType() == ExpressionType.AXModality) {
			AXModality ax = (AXModality) v;
			if(ax.isIndexed())
				indices.addAll(ax.getProcessIndices());
		} if(v.getType() == ExpressionType.EXModality) {
			EXModality ex = (EXModality) v;
			if(ex.isIndexed())
				indices.addAll(ex.getProcessIndices());
		}
			
	}
	
	/**
	 * Gets the indices stored between the last reset (or creation) and this call.
	 * @return the indices in a list.
	 */
	public ArrayList<String> getIndices() {
		return indices;
	}
}
