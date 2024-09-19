package eshmun.skeletontextrepresentation.guard;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.State;

/**
 * Abstract class to define different Guard types. 
 * Guards are parsed for each process, they can be local or global gaurds.
 * 
 * 
 * @author chukrisoueidi
 *
 */
public abstract class Guard {

	/**
	 * Must be implemented by all subtypes
	 * @param visitor
	 * @param state
	 * @return
	 */
	public abstract boolean accept(GuardVisitor visitor, State state);
	
	public abstract BoolExpr accept(Z3GuardVisitor visitor, Context ctx);
	
	
	public String guardText ;
	
	
	/**
	 * Defines the interface for any visitor operating on Guard types
	 * @author chukrisoueidi
	 *
	 */
	public static interface GuardVisitor {
		/**
		 * Visits an OrGuardOperator.
		 * @param or the visited operator.
		 */
		public boolean visit(OrGuard or, State state);
		
		/**
		 * Visits an AndGuard.
		 * @param or the visited operator.
		 */
		public boolean visit(AndGuard and, State state);
		/**
		 * Visits an NotGuard.
		 * @param or the visited operator.
		 */
		public boolean visit(NotGuard not, State state);
		
		/**
		 * Visits an LabelGuard.
		 * @param or the visited operator.
		 */
		public boolean visit(LabelGuard label, State state);

		/**
		 * Visits an LiteralGuard.
		 * @param or the visited operator.
		 */
		public boolean visit(LiteralGuard literalGuard, State state);
		
		 
	}
	
	public static interface Z3GuardVisitor {
		/**
		 * Visits an OrGuardOperator.
		 * @param or the visited operator.
		 */
		public BoolExpr visit(OrGuard or, Context ctx);
		
		/**
		 * Visits an AndGuard.
		 * @param or the visited operator.
		 */
		public BoolExpr visit(AndGuard and,  Context ctx);
		/**
		 * Visits an NotGuard.
		 * @param or the visited operator.
		 */
		public BoolExpr visit(NotGuard not,  Context ctx);
		
		/**
		 * Visits an LabelGuard.
		 * @param or the visited operator.
		 */
		public BoolExpr visit(LabelGuard label,   Context ctx);

		/**
		 * Visits an LiteralGuard.
		 * @param or the visited operator.
		 */
		public BoolExpr visit(LiteralGuard literalGuard, Context ctx);
		
		 
	}
}
