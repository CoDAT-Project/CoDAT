package eshmun.skeletontextrepresentation.infinitespace.visitors;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.BlockStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.IfStatement;

public interface CommandLogicVisitor {

	   BoolExpr visit(IfStatement statement, BoolExpr p, Context ctx,   int stateNumber );
	   BoolExpr visit(BlockStatement statement, BoolExpr p, Context ctx, int stateNumber);
	   BoolExpr visit(AssignmentStatement statement, BoolExpr p, Context ctx, int stateNumber);
}
