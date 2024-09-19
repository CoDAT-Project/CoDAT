package eshmun.expression.visitor.visitors;

import eshmun.expression.AbstractExpression;
import eshmun.expression.atomic.BooleanLiteral;
import eshmun.expression.atomic.BooleanVariable;
import eshmun.expression.modalities.binary.AUModality;
import eshmun.expression.modalities.binary.AVModality;
import eshmun.expression.modalities.binary.AWModality;
import eshmun.expression.modalities.binary.EUModality;
import eshmun.expression.modalities.binary.EVModality;
import eshmun.expression.modalities.binary.EWModality;
import eshmun.expression.modalities.unary.AFModality;
import eshmun.expression.modalities.unary.AGModality;
import eshmun.expression.modalities.unary.AXModality;
import eshmun.expression.modalities.unary.EFModality;
import eshmun.expression.modalities.unary.EGModality;
import eshmun.expression.modalities.unary.EXModality;
import eshmun.expression.operators.AndOperator;
import eshmun.expression.operators.EquivalenceOperator;
import eshmun.expression.operators.ImplicationOperator;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.operators.OrOperator;
import eshmun.expression.visitor.Visitor;

/**
 * Visitor for printing an AbstractExpression in a formatted way. (PREFIX)
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 */
public class PreFixStringVisitor implements Visitor {
	/**
	 * The output, The string representing the given expression.
	 */
	public String output;
	
	/**
	 * Creates a new ToStringVisitor, and constructs the prefix string representation of the given expression.
	 * @param exp the expression for which a string representation should be constructed.
	 */
	public PreFixStringVisitor(AbstractExpression exp) {
		output = "";
		
		exp.accept(this);
	}
	
	/**
	 * Returns the string representing the expression of this visitor.
	 * @return String representation of the expression.
	 */
	@Override
	public String toString() {
		return output;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanVariable v) {
		output += " ( ";
		output += v.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(BooleanLiteral v) {
		output += " ( ";
		output += v.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(NotOperator v) {
		output += " ( ";
		output += "!";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AndOperator v) {
		output += " ( ";
		output += "&";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(OrOperator v) {
		output += " ( ";
		output += "|";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(ImplicationOperator v) {
		output += " ( ";
		output += "=>";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EquivalenceOperator v) {
		output += " ( ";
		output += "<=>";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AFModality v) {
		output += " ( ";
		output += "AF";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AGModality v) {
		output += " ( ";
		output += "AG";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AXModality v) {
		output += " ( ";
		output += "AX";
		if(v.isIndexed()) 
			output += "_{"+v.getIndexAsString()+"}";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EFModality v) {
		output += " ( ";
		output += "EF";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EGModality v) {
		output += " ( ";
		output += "EG";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EXModality v) {
		output += " ( ";
		output += "EX";
		if(v.isIndexed()) 
			output += "_{"+v.getIndexAsString()+"}";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AUModality v) {
		output += " ( ";
		output += "AU";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AWModality v) {
		output += " ( ";
		output += "AW";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(AVModality v) {
		output += " ( ";
		output += "AV";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EUModality v) {
		output += " ( ";
		output += "EU";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EWModality v) {
		output += " ( ";
		output += "EW";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(EVModality v) {
		output += " ( ";
		output += "EV";
	}

	/**
	 * Does Nothing. Shown here for completeness.
	 */
	@Override
	public void childrenSeparator() {}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void separator() {
		output += " ) ";
	}
}