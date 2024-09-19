package eshmun.skeletontextrepresentation.guard;

import java.util.ArrayList;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;

import eshmun.skeletontextrepresentation.State;

/**
 * Represents an OR Operator on Guards. Semantically, this means that at least
 * one inner guard expression must be true for this to be true. The resulting
 * assignment is the combined assignment of any inner guard expression that is
 * satisfied.
 * 
 * @author chukris
 *
 */
public class OrGuard extends Guard {

	/**
	 * Holds all nested Guards
	 */
	private ArrayList<Guard> children;

	/**
	 * Initializes the children
	 */
	public OrGuard() {
		this.children = new ArrayList<>();
	}

	/**
	 * Adds a new guard expression to the and statement. If the child is an OrGuard
	 * its children will be added. it is guaranteed that no direct child of any
	 * OrGuard is an OrGuard.
	 * 
	 * @param the
	 *            child to be added.
	 */
	public void orGuard(Guard exp) {
		if (exp instanceof OrGuard) {
			this.children.addAll(((OrGuard) exp).children);
		} else {
			this.children.add(exp);
		}

	}

	/**
	 * Calls the visitor AndGuard implementation
	 */
	@Override
	public boolean accept(GuardVisitor visitor, State state) {
		return visitor.visit(this, state);

	}

	/**
	 * Getter for children
	 * 
	 * @return
	 */
	public ArrayList<Guard> getChildren() {

		return children;
	}

	@Override
	public BoolExpr accept(Z3GuardVisitor visitor,  Context ctx) {
		return visitor.visit(this, ctx);
	}

}
