package eshmun.expression.visitor.visitors;

import java.util.Collection;

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

public class EvaluaterVisitor extends AbstractStateSpecsVisitor<Boolean, Void> {
	private Collection<String> variables;
	private boolean variableSignificance;
	private String[] suffixes;
	private boolean suffixSignificance;
	
	public EvaluaterVisitor(Collection<String> variables, boolean variableSignificance) {
		this.variables = variables;
		this.variableSignificance = variableSignificance;
	}
	
	public EvaluaterVisitor(Collection<String> variables, boolean variableSignificance, String[] suffixes, boolean suffixSignificance) {
		this.variables = variables;
		this.variableSignificance = variableSignificance;
		this.suffixes = suffixes;
		this.suffixSignificance = suffixSignificance;
	}
	
	@Override
	public Boolean run(AbstractExpression specifications) {
		return visit(specifications, null);
	}

	@Override
	public Boolean visit(BooleanVariable v, Void aVoid) {
		String name = v.getName();
		
		if(suffixes != null) {
			boolean flag = true;
			for(String suffix : suffixes) {
				if(name.endsWith(suffix)) {
					flag = false;
					break;
				}
			}
			
			if(flag)
				return suffixSignificance;
		}
		
		if(variables.contains(name.trim())) {
			return variableSignificance;
		}
		
		return !variableSignificance;
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
		boolean flag = true;
		for(AbstractExpression ex : v.getChildren()) {
			flag = flag && visit(ex, aVoid);
		}
		
		return flag;
	}

	@Override
	public Boolean visit(OrOperator v, Void aVoid) {
		boolean flag = false;
		for(AbstractExpression ex : v.getChildren()) {
			flag = flag || visit(ex, aVoid);
		}
		
		return flag;
	}

	@Override
	public Boolean visit(ImplicationOperator v, Void aVoid) {
		boolean left = visit(v.getLeftChild(), aVoid);
		boolean right = visit(v.getRightChild(), aVoid);
		
		if(left)
			return right;
		
		if(!right)
			return !left;
		
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
	public Boolean visit(AFModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(AGModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(AXModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(EFModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(EGModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(EXModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(AUModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(AWModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(EUModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(EWModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(AVModality v, Void aVoid) {
		return false;
	}

	@Override
	public Boolean visit(EVModality v, Void aVoid) {
		return false;
	}
}
