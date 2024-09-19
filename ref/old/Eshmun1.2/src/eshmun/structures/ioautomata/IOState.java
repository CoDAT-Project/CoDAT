package eshmun.structures.ioautomata;

import eshmun.structures.AbstractState;
import eshmun.structures.AbstractStructure;

public class IOState extends AbstractState {

	public IOState(String stateName, AbstractStructure parentStructure) {
		super(stateName, parentStructure);
		// TODO Auto-generated constructor stub
	}
	
	public IOState(String stateName, boolean isStart, AbstractStructure parentStructure) {
		super(stateName, parentStructure, isStart);
	}
	

}
