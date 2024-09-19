package eshmun.structures.ioautomata;

import eshmun.structures.AbstractStructure;

public class IOAutomata extends AbstractStructure {
	boolean inputEnabled;

	public IOAutomata() {
		super();
		inputEnabled = false;
	}

	public IOAutomata(String name) {
		super(name);
		inputEnabled = false;
	}

	public void addInputTransition(IOState from, IOState to) {
		if (!inputEnabled) {
			inputEnabled = true;
		}
		IOTransition toAdd = new IOTransition(from, to, ActionSignature.IN, this);
		from.getOutTransition().add(toAdd);
		to.getInTransition().add(toAdd);
	}
	
	public void addOutputTransition(IOState in, IOState out) {
		//IOTransition toAdd = new IOTransition()
	}

	public boolean isInputEnabled() {
		return inputEnabled;
	}

}
