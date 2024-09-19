package eshmun.skeletontextrepresentation.infinitespace.actions;

import java.util.ArrayList;
import java.util.HashSet;

import org.antlr.v4.parse.GrammarTreeVisitor.tokenSpec_return;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.enumerations.Z3_lbool;

import eshmun.skeletontextrepresentation.infinitespace.commands.AssignmentStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.BlockStatement;
import eshmun.skeletontextrepresentation.infinitespace.commands.Statement;
import eshmun.skeletontextrepresentation.infinitespace.structures.InfiniteStateTransition;
import jdk.nashorn.internal.ir.annotations.Ignore;

public class InfiniteStateAction {

	/**
	 * Local Guard
	 */
	public BoolExpr lGuard;

	/**
	 * Global Guard
	 */
	public BoolExpr gGuard;

	/**
	 * Local Effect
	 */
	public Statement lEffect;

	/**
	 * Global Effect
	 */
	public Statement gEffect;
	
	

	private Statement mergedEffects = null;

	private Statement mergedNonBoolEffects = null;

	/**
	 * Stores the action name
	 */
	public String name;

	/**
	 * Stores the process number
	 */
	public int processNumber;

	public int actionNumber;

	public String toString() {

		return lGuard.toString() + gGuard.toString();
	}

	public Statement getEffects() {

		if (mergedEffects == null) {
			BlockStatement blockStatement = new BlockStatement(this.lEffect, this.gEffect);
			return blockStatement;
		} else {
			return mergedEffects;
		}

	}

	public Statement getNonBoolEffects() {

		if (mergedNonBoolEffects == null) {

			// Guaranteed to have 2 children

			ArrayList<Statement> childrenOf1 = new ArrayList<>(); // l_Effect
			ArrayList<Statement> childrenOf2 = new ArrayList<>(); // g_Effect

			if (this.lEffect instanceof BlockStatement) {
				for (Statement statement : ((BlockStatement) this.lEffect).children) {
					if (!(statement instanceof AssignmentStatement)
							|| !(((AssignmentStatement) statement).right instanceof BoolExpr)) {
						childrenOf1.add(statement);
					}
				}
			} else {
				childrenOf1.add(this.lEffect);
			}

			BlockStatement b1 = new BlockStatement(childrenOf1);
			
			if (this.gEffect instanceof BlockStatement) {
				for (Statement statement : ((BlockStatement) this.gEffect).children) {
					if (!(statement instanceof AssignmentStatement)
							|| !(((AssignmentStatement) statement).right instanceof BoolExpr)) {
						childrenOf2.add(statement);
					}
				}
			} else {
				childrenOf2.add(this.gEffect);
			}

			BlockStatement b2 = new BlockStatement(childrenOf2);

			BlockStatement blockStatement = new BlockStatement(b1, b2);

			return blockStatement;

		} else {
			return mergedNonBoolEffects;
		}

	}

	public HashSet<InfiniteStateTransition> transitions = new HashSet<InfiniteStateTransition>();

	public BoolExpr getCombinedGuards(Context ctx, HashSet<String> allAPs) {
	
		BoolExpr b  = ctx.mkAnd(this.lGuard, this.gGuard);
		
//		Z3_lbool lvalue = this.lGuard.getBoolValue();
//		Z3_lbool gvalue = this.gGuard.getBoolValue();
//		
//		for (String l : allAPs) {
//			if(l.endsWith(processNumber+"")) {
//				
//				if(!b.toString().contains(l) ) {				
//					b = ctx.mkAnd(b, ctx.mkNot(ctx.mkBoolConst(l)));
//				}
//			}else {
//				if(gvalue == Z3_lbool.Z3_L_TRUE) {
//					b = ctx.mkAnd(b, ctx.mkBoolConst(l));
//				}else if ( gvalue == Z3_lbool.Z3_L_UNDEF) {
//					
//					if(!b.toString().contains(l) ) {				
//						b = ctx.mkAnd(b, ctx.mkNot(ctx.mkBoolConst(l)));
//					}
//				}
//			}
//			
//		}
		return b;
	};
}
