package eshmun.skeletontextrepresentation.guard;

 
import eshmun.skeletontextrepresentation.State;

import eshmun.skeletontextrepresentation.guard.Guard.GuardVisitor;
import com.microsoft.z3.*;

/**
 * This visitor operates on a guard to check if a state is enabled or not by it.
 * 
 * @author chukrisoueidi
 *
 */
public class EnableStateVisitor implements GuardVisitor {

	/*
	 * if a state is N1,C1,x=1 this evaluates to 3
	 */
	public static int numberOfLabelsCheckedInGuard = 0;
	
	
	/**
	 * Return true of any child item evaluates to true
	 */
	
	
	@Override
	public boolean visit(OrGuard or, State state) {

		for (Guard gd : or.getChildren()) {
			if (gd.accept(this, state))
				return true;
		}

		return false;
	}

	/**
	 * Return true only if all children evaluates to true
	 */
	@Override
	public boolean visit(AndGuard and, State state) {
		
		for (Guard gd : and.getChildren()) {
			if (!gd.accept(this, state))
				return false;
		}

		return true;
	}

	/**
	 * Negate the evaluation of the child Guard
	 */
	@Override
	public boolean visit(NotGuard not, State state) {

		return ! not.child.accept(this, state);

	}

	/**
	 * If a state contains a label, then the LabelGuard is true
	 */
	@Override
	public boolean visit(LabelGuard label, State state) {
		numberOfLabelsCheckedInGuard++;
		return label.isInState(state);
	}

	/**
	 * Returns true for all states
	 */
	@Override
	public boolean visit(LiteralGuard literalGuard, State state) {
		numberOfLabelsCheckedInGuard++;
		return literalGuard.getValuation();
	}

}
