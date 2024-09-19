package eshmun.structures;

import java.util.ArrayList;

/**
* <h1>AbstractState</h1>
*AbstractState is an abstract class parent to KripkeState, IOState and BipState
*
* @author  Ali
* @version 1.0
* @since   2015-04-23
*/


public abstract class AbstractState {
	private String name;
	private boolean startState;
	private AbstractStructure parentStructure;
	private ArrayList<AbstractTransition> outTransition;
	private ArrayList<AbstractTransition> inTransition;
	private boolean retain;
	
	public AbstractState(String stateName, AbstractStructure parentStructure) {
		this.name = stateName;
		this.startState = false;
		this.parentStructure = parentStructure;
	}
	
	public AbstractState(String stateName,AbstractStructure parentStructure, boolean isStart) {
		this.name = stateName;
		this.startState = isStart;
		this.parentStructure = parentStructure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStartState() {
		return startState;
	}

	public void setAsStartState(boolean startState) {
		this.startState = startState;
	}

	public AbstractStructure getParentStructure() {
		return parentStructure;
	}

	public void setParentStructure(AbstractStructure parentStructure) {
		this.parentStructure = parentStructure;
	}

	public ArrayList<AbstractTransition> getOutTransition() {
		return outTransition;
	}
	
	public ArrayList<AbstractTransition> getInTransition() {
		return inTransition;
	}

	public boolean isRetain() {
		return retain;
	}

	public void setRetain(boolean retain) {
		this.retain = retain;
	}
	
	public void removeTransition(AbstractState from, AbstractState to) {
		for(AbstractTransition out : outTransition){
			if(out.getFrom().equals(from) && out.getTo().equals(to)){
				outTransition.remove(out);
				to.getInTransition().remove(out);
			}
		}
	}
	
	public void changeStateName(String newName){
		parentStructure.getStatesMap().put(newName, parentStructure.getStatesMap().get(this.getName()));
		parentStructure.getStatesMap().remove(this.getName());
		this.setName(newName);
	}

	@Override
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof AbstractState)) {
			return false;
		}
		return this.name.equals(((AbstractState) o).getName());
	}
}
