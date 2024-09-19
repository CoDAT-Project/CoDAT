package eshmun.skeletontextrepresentation.infinitespace.commands;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.infinitespace.visitors.CommandLogicVisitor;

public class BlockStatement extends Statement {

	public ArrayList<Statement> children;

	public BlockStatement() {
		children = new ArrayList<>();
	}
	
	public BlockStatement(ArrayList<Statement> children) {
		this.children = children;
	}
	
	public BlockStatement(Statement s1, Statement s2 ) {
		
		children = new ArrayList<>();
		children.add(s1);
		children.add(s2);
		
	}
	

	public String toString() {

		String ret = "";
		for (Statement statement : children) {
		 
			ret +=  statement.toString() ;
		}
 
		return ret ;
	}
	
	@Override
	public BoolExpr accept(CommandLogicVisitor v, BoolExpr p ,Context ctx, int stateNumber) {

		return v.visit(this, p, ctx, stateNumber);

	}
}
