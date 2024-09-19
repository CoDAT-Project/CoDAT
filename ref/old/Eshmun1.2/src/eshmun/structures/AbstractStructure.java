package eshmun.structures;

import java.util.ArrayList;
import java.util.HashMap;


/**
* AbstractStructure is an abstract class parent to Kripke, IOAutomata and Bip
*
* @author  Ali
* @version 1.0
* @since   2015-04-23
*/


public abstract class AbstractStructure {
	private String structureName;
	private HashMap<String, AbstractState> states;
	private ArrayList<AbstractState> startStates;
	
	public AbstractStructure(){
		states = new HashMap<String,AbstractState>();
		startStates = new ArrayList<AbstractState>();
	}
	
	public AbstractStructure(String name) {
		structureName = name;
		states = new HashMap<String,AbstractState>();
		startStates = new ArrayList<AbstractState>();
	}

	public String getStructureName() {
		return structureName;
	}
	
	/**
    * This method is used to set the name of the structure
   	* @param String structureNamne, the name to set
    */
	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}
	
	public HashMap<String, AbstractState> getStatesMap(){
		return states;
	}

	public ArrayList<AbstractState> getStates() {
		ArrayList<AbstractState> statesList = new ArrayList<AbstractState>();
		for(String key : states.keySet()) {
			statesList.add(states.get(key));
		}
		return statesList;
	}

	public ArrayList<AbstractState> getStartStates() {
		return startStates;
	}
	
	public void changeStateName(String oldName, String newName){
		states.get(oldName).changeStateName(newName);
	}
	
	public void addState(AbstractState toAdd){
		states.put(toAdd.getName(), toAdd);
		if(toAdd.isStartState()) {
			startStates.add(toAdd);
		}
	}
	
	public void removeState(String stateName){
		for(AbstractTransition toRemove : states.get(stateName).getOutTransition()) {
			toRemove.getTo().getInTransition().remove(toRemove);
		}
		for(AbstractTransition toRemove : states.get(stateName).getInTransition()) {
			toRemove.getTo().getOutTransition().remove(toRemove);
		}
		states.remove(stateName);
	}
	
	public void removeTransition(AbstractTransition toRemove){
		toRemove.getFrom().getOutTransition().remove(toRemove);
		toRemove.getTo().getInTransition().remove(toRemove);
	}
		
}
