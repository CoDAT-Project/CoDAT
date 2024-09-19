package eshmun.skeletontextrepresentation.guard;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.State;

/**
 * Represents an AND Operator on Guards. Semantically, this means that all inner
 * guard expression must be true for this to be true, and all their
 * effects/assignments are combined into this assignment.
 * 
 * 
 * @author chukris
 *
 */
public class AndGuard extends Guard {

	/**
	 * Holds all nested Guards
	 */
	private ArrayList<Guard> children;

	/**
	 * Creates a new empty AndGuard.
	 */
	public AndGuard() {
		this.children = new ArrayList<>();
	}

	/**
	 * Adds a new guard expression to the and statement. If the child is an AndGuard its children will be added.	  
	 * it is guaranteed that no direct child of any AndGuardOperator is an AndGuardOperator.
	 * 
	 * @param the child to be added.
	 */
	public void andGuard(Guard exp) {
		if (exp instanceof AndGuard) {
			this.children.addAll(((AndGuard) exp).children);
		} else {
			this.children.add(exp);
		}

	}

 

	/**
	 * Calls the visitor AndGuard implementation 
	 */
	public boolean accept(GuardVisitor visitor, State state) {

		return visitor.visit(this, state);

	}

	/**
	 * Retrieves all Guard children
	 * @return
	 */
	public ArrayList<Guard> getChildren() {

		return children;
	}

	@Override
	public BoolExpr accept(Z3GuardVisitor visitor, Context ctx) {
		// TODO Auto-generated method stub
		return visitor.visit(this,ctx);
	}

	 

}
