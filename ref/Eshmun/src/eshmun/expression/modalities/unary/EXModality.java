package eshmun.expression.modalities.unary;

import java.util.ArrayList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.operators.NotOperator;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an EX CTL Modality. (Next)
 * 
 * <p>An EX Modality has the following form EX_{index}(child), where child is an AbstractExpression, 
 * and index is an optional comma separated list of process and action names.</p>
 * 
 * <p>It means that the formula child has to hold on the next state on some branch.</p>
 *  
 * <p>Notice that this is part of the minimal set of modalities, therefore it cannot be simplified.
 * Simplifying this modality will only result in simplifying its child.</p>
 * 
 * <p>An EX modality can be restricted to a specific process or action, EX_{i,@a}(f) indicates that
 * f should be valid at some next state reached by making a move by process i labeled with action a.</p>
 *  
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.UnaryCTLModality
 * @see eshmun.expression.AbstractExpression
 */
public class EXModality extends UnaryCTLModality {
	/**
	 * The names of processes and/or actions to restrict this modality to,
	 * empty if no such name is provided.
	 */
	private String[] indices;
	
	/**
	 * Create a new EXModality with the given child.
	 * @param child The child expression of this modality.
	 */
	public EXModality(AbstractExpression child) {
		this(child, (String) null);
	}
	
	/**
	 * Create a new EXModality with the given child and process index.
	 * @param child The child expression of this modality.
	 * @param index comma-separated names of the processes and/or actions to restrict this modality to.
	 */
	public EXModality(AbstractExpression child, String index) {
		super(ExpressionType.EXModality, child);
		
		this.indices = index == null ? new String[0] : index.split(",");
		for(int i = 0; i < this.indices.length; i++)
			this.indices[i] = this.indices[i].trim();
	}
	
	/**
	 * Create a new EXModality with the given child and process index.
	 * @param child The child expression of this modality.
	 * @param indices array of names of the processes and/or actions to restrict this modality to.
	 */
	public EXModality(AbstractExpression child, String[] indices) {
		super(ExpressionType.EXModality, child);
		
		this.indices = indices == null ? new String[0] : indices;
	}
	
	/**
	 * Checks whether this modality has a process index associated with it.
	 * @return true if it has a process index, false otherwise.
	 */
	public boolean isIndexed() {
		return indices.length > 0;
	}
	
	/**
	 * Gets all process indices.
	 * @return the process indices. 
	 */
	public ArrayList<String> getProcessIndices() {
		ArrayList<String> processes = new ArrayList<>();
		for(String s : indices)
			if(!s.startsWith("@"))
				processes.add(s);
		
		return processes;
	}
	
	/**
	 * Gets all action indices.
	 * @return the action indices. 
	 */
	public ArrayList<String> getActionIndices() {
		ArrayList<String> actions = new ArrayList<>();
		for(String s : indices)
			if(s.startsWith("@"))
				actions.add(s.substring(1));
		
		return actions;
	}
	
	/**
	 * Gets the indices as a string (comma separated).
	 * @return string representation of the index.
	 */
	public String getIndexAsString() {
		String index = "";
		for(int i = 0; i < indices.length; i++)
			index += (i > 0 ? "," : "") + indices[i];
		
		return index;
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
	 * <p>This is one of the minimal modalities, simplification is simplifying the child.</p>
	 * @return a simplified equivalent expression.
	 */
	@Override
	public AbstractExpression simplify() {
		return new EXModality(child.simplify(), indices);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return new EXModality(child.toNNF(), indices);
	}

	/**
	 * Clones this EXModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one EXModality.</p>
	 * 
	 * @return a shallow copy of this EXModality.
	 */
	@Override
	public EXModality clone() {
		return new EXModality(child, indices);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
		child.accept(visitor);
		visitor.childrenSeparator();
		
		visitor.separator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void acceptChildren(Visitor visitor) {
		child.accept(visitor);
		visitor.childrenSeparator();
		visitor.visit(this);
		
		visitor.separator();
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean directCompare(AbstractExpression exp) {
		EXModality ex = (EXModality) exp;
		
		for(String s1 : indices) {
			boolean flag = false;
			for(String s2 : ex.indices) {
				if(s1.equals(s2)) {
					flag = true;
					break;
				}
			}
			
			if(!flag) return false;
		}
		
		for(String s1 : ex.indices) {
			boolean flag = false;
			for(String s2 : indices) {
				if(s1.equals(s2)) {
					flag = true;
					break;
				}
			}
			
			if(!flag) return false;
		}
		
		return child.equals(ex.child);			
	}
}
