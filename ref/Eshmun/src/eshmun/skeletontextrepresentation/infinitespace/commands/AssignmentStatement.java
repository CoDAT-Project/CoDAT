package eshmun.skeletontextrepresentation.infinitespace.commands;

 

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import eshmun.skeletontextrepresentation.infinitespace.visitors.CommandLogicVisitor;


public class AssignmentStatement extends Statement {

	public Expr left;
	public Expr right;
	
	public String rightHand;
	
	public AssignmentType assignmentType;
	
	public Boolean isLeftSet() {
		return left != null;
	}
	
	public String toString() {
		
		return left.toString() +  " := " + rightHand + " ; " +System.lineSeparator();
	}
	
	@Override
	public BoolExpr accept(CommandLogicVisitor v, BoolExpr p, Context ctx,int stateNumber) {

		return v.visit(this, p, ctx, stateNumber);

	}
	
	public enum AssignmentType{
		BoolAssignment, IntAssignment
	}
	 
}
