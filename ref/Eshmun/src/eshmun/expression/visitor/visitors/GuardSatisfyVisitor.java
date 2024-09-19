package eshmun.expression.visitor.visitors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

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

// TODO comment
public class GuardSatisfyVisitor extends AbstractStateSpecsVisitor<Boolean, Void> {
	private Collection<String> state;
	private TreeSet<String> actions;
	private ArrayList<String> processes;
	
	// TODO ensure actions are only used inside top level ands.
	public GuardSatisfyVisitor(Collection<String> state, ArrayList<String> processes) {
		this.state = state;
		this.actions = new TreeSet<>();
		this.processes = processes;
	}
	
	public TreeSet<String> getActions() {
		return actions;
	}
	
	@Override
	public Boolean run(AbstractExpression specifications) {
		return visit(specifications, null);
	}

	@Override
	public Boolean visit(BooleanVariable v, Void aVoid) {
		String name = v.getName().trim();
		if(name.startsWith("@")) {
			actions.add(name);
			return true;
		}
		
		for(String p : processes) 
			if(name.endsWith(p))
				return state.contains(name);
		
		if(name.contains("{")) { // Maybe shared variable
			String sharedProcesses = name.substring(name.indexOf("{") + 1, name.indexOf("}"));
			for(String p : sharedProcesses.split(",")) {
				if(!processes.contains(p.trim()))
					return false; // Belongs to a different process.
			}
			
			// Belongs to processes all under consideration.
			return state.contains(name);
		}
		
		// Belongs to a different process.
		return true;
	}

	@Override
	public Boolean visit(BooleanLiteral v, Void aVoid) {
		return v.getValue();
	}

	@Override
	public Boolean visit(NotOperator v, Void aVoid) {
		return !visit(v.getChild(), aVoid);
	}
	
	@Override
	public Boolean visit(AndOperator v, Void aVoid) {
		for(AbstractExpression ex : v.getChildren()) {
			if(!visit(ex, aVoid)) return false;
		}
		
		return true;
	}

	@Override
	public Boolean visit(OrOperator v, Void aVoid) {
		for(AbstractExpression ex : v.getChildren()) {
			if(visit(ex, aVoid)) return true;
		}
		
		return false;
	}

	@Override
	public Boolean visit(ImplicationOperator v, Void aVoid) {
		boolean left = visit(v.getLeftChild(), aVoid);
		boolean right = visit(v.getRightChild(), aVoid);
		
		if(left)
			return right;
		
		return true;
	}

	@Override
	public Boolean visit(EquivalenceOperator v, Void aVoid) {
		boolean flag = visit(v.getChildren()[0], aVoid);
		for(AbstractExpression ex : v.getChildren()) {
			if(flag != visit(ex, aVoid))
				return false;
		}
		
		return true;
	}

	@Override
	public Boolean visit(AFModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(AGModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(AXModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(EFModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(EGModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(EXModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(AUModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(AWModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(EUModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(EWModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(AVModality v, Void aVoid) { return null; }

	@Override
	public Boolean visit(EVModality v, Void aVoid) { return null; }
	
}

