package eshmun.skeletontextrepresentation.infinitespace.commands;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.infinitespace.visitors.CommandLogicVisitor;

public abstract class Statement {

	  public abstract BoolExpr accept(CommandLogicVisitor v, BoolExpr p, Context ctx, int stateNumber);
	
}
