package eshmun.choreography;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ChState {
	public String choreography;
	public Process control;
	public ArrayList<Process> receiverProcesses;
	public ArrayList<Process> neighborsProcesses;
	public int stateNumber;
	public HashSet<Integer> transitions;
	public ArrayList<ChState> childStates;
	public boolean connectable;
	public String label;
	public static HashMap<String, Integer> allLabels = new HashMap<String, Integer>();
	
	public ChState(String choreography, Process control, ArrayList<Process> receiverProcesses) {
		this.choreography = choreography;
		this.control = control;
		this.receiverProcesses = receiverProcesses;
		this.neighborsProcesses = new ArrayList<Process>();
		this.stateNumber = StateHelper.numberOfStates++;
		transitions = new HashSet<Integer>();
		childStates = new ArrayList<ChState>();
		connectable = true;
		label =  getLabel();
	}
	public ChState(String choreography, String lab) {
		this.choreography = choreography;
		//this.control = control;
		//this.receiverProcesses = receiverProcesses;
		this.neighborsProcesses = new ArrayList<Process>();
		this.stateNumber = StateHelper.numberOfStates++;
		transitions = new HashSet<Integer>();
		childStates = new ArrayList<ChState>();
		connectable = true;
		label =  "S" + stateNumber + ":" + lab;
	}
	
	public void addNeighborProcess(Process neighborProc) {
		neighborsProcesses.add(neighborProc);
	}
	public void addTransition(int id) {
		transitions.add(id);
	}
	public String print() {
		String toReturn = "Choreography: " + choreography + "\n";
		toReturn += "Control: " + control.name + " " + control.port + "\n";
		
		for(int i = 0; i < receiverProcesses.size(); ++i) {
			toReturn += "Receiver" + i + ": " + receiverProcesses.get(i).name + " " + receiverProcesses.get(i).port + "\n";
		}
		
		return toReturn;
	}
	public String toString() {
		return label;
	}
	public String transitionsToString() {
		String toReturn = "";
		for(int id: transitions) {
			toReturn += "S" + stateNumber + ":S" + id + ":false:;\n";
		}
		return toReturn;
	}
	
	public String getLabel() {
		String stateNum = "S" + stateNumber + ":";
		String labelAt =  control.name;
		if(!control.port.equals("NuLL"))
			labelAt += "." + control.port;
		for(int i = 0; i < receiverProcesses.size(); ++i) {
			labelAt += "." + receiverProcesses.get(i).name;
			if(!receiverProcesses.get(i).port.equals("NuLL"))
				labelAt += "." + receiverProcesses.get(i).port;
		}
		
		if(!allLabels.containsKey(labelAt)) {
			allLabels.put(labelAt, 1);
			labelAt = labelAt + "_1_1";
		}
		else {
			allLabels.put(labelAt, allLabels.get(labelAt) + 1);
			labelAt = labelAt + "_" + allLabels.get(labelAt) + "_1";
		}
		return stateNum + labelAt;
	}
}
