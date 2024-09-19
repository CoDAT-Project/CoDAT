package eshmun.structures.ioautomata;

import eshmun.structures.AbstractState;
import eshmun.structures.AbstractStructure;
import eshmun.structures.AbstractTransition;

public class IOTransition extends AbstractTransition {
	private ActionSignature action;
	
	public IOTransition(AbstractState from, AbstractState to, ActionSignature action, AbstractStructure parentStructure) {
		super(from, to, parentStructure);
		this.action = action;
	}

	public ActionSignature getAction() {
		return action;
	}

	public void setAction(ActionSignature action) {
		this.action = action;
	}
	
	

}
