package eshmun.expression.modalities.unary;

import java.util.ArrayList;

import eshmun.expression.AbstractExpression;
import eshmun.expression.ExpressionType;
import eshmun.expression.modalities.UnaryCTLModality;
import eshmun.expression.visitor.Visitor;

/**
 * This class represents an AX CTL Modality. (Next)
 * 
 * <p>An AX Modality has the following form AX{index}(child), where child is an AbstractExpression, 
 * and index is an optional comma separated list of process and action names.</p>
 * 
 * <p>It means that the formula child has to hold on the next state on all the branches.</p>
 *
 * <p>Simplification, negation and equality is based on AX(f) = !EX(!f)</p> 
 * 
 * <p>An AX modality can be restricted to a specific process or action, AX_{i,@a}(f) indicates that
 * f should be valid at all the next states reached by making a move by process i labeled with action a.</p>
 * 
 * @author Kinan Dak Al Bab
 * @since 1.0
 * 
 * @see eshmun.expression.modalities.UnaryCTLModality
 * @see eshmun.expression.modalities.unary.EXModality
 * @see eshmun.expression.AbstractExpression
 */
public class AXModality extends UnaryCTLModality {
	/**
	 * The names of processes and/or actions to restrict this modality to,
	 * empty if no such name is provided.
	 */
	private String[] indices;
	
	/**
	 * Create a new AXModality with the given child.
	 * @param child The child expression of this modality.
	 */
	public AXModality(AbstractExpression child) {
		this(child, (String) null);
	}
	
	/**
	 * Create a new AXModality with the given child and process index.
	 * @param child The child expression of this modality.
	 * @param index comma-separated names of the processes and/or actions to restrict this modality to.
	 */
	public AXModality(AbstractExpression child, String index) {
		super(ExpressionType.AXModality, child);
		
		this.indices = index == null ? new String[0] : index.split(",");
		for(int i = 0; i < this.indices.length; i++)
			this.indices[i] = this.indices[i].trim();
	}
	
	/**
	 * Create a new AXModality with the given child and process index.
	 * @param child The child expression of this modality.
	 * @param indices array of names of the processes and/or actions to restrict this modality to.
	 */
	public AXModality(AbstractExpression child, String[] indices) {
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
	 * <p>Negation is done according to !AX(f) = EX(!f)</p>
	 *  
	 * @return a simplified expression that is the negation of this expression.
	 * 
	 * @see eshmun.expression.modalities.unary.EXModality
	 */
	@Override
	public AbstractExpression negate() {
		AbstractExpression notChild = child.negate();
		return new EXModality(notChild, indices);
	}

	/**
	 * Simplifies this expression.
	 * 
	 * <p>Simplification is done according to AX(f) = !EX(!f)</p>
	 * 
	 * @return a simplified equivalent expression.
	 * 
	 * @see eshmun.expression.modalities.unary.EXModality
	 */
	@Override
	public AbstractExpression simplify() {
		AbstractExpression notChild = child.negate();
		return new EXModality(notChild, indices).negate();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractExpression toNNF() {
		return new AXModality(child.toNNF(), indices);
	}

	/**
	 * Clones this AXModality.
	 * 
	 * <p>Please note that while the operator is copied, the child is not.
	 * Changes in the child could affect more than one AXModality.</p>
	 * 
	 * @return a shallow copy of this AXModality.
	 */
	@Override
	public AXModality clone() {
		return new AXModality(child, indices);
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
		AXModality ax = (AXModality) exp;
		
		for(String s1 : indices) {
			boolean flag = false;
			for(String s2 : ax.indices) {
				if(s1.equals(s2)) {
					flag = true;
					break;
				}
			}
			
			if(!flag) return false;
		}
		
		for(String s1 : ax.indices) {
			boolean flag = false;
			for(String s2 : indices) {
				if(s1.equals(s2)) {
					flag = true;
					break;
				}
			}
			
			if(!flag) return false;
		}
		
		return child.equals(ax.child);			
	}
}
