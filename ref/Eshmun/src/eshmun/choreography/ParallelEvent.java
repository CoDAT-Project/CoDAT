package eshmun.choreography;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ParallelEvent extends BaseEvent{
	public ArrayList<ChoreographyEvent> choreographies;
	public HashMap<ChoreographyEvent, ArrayList<String>> choreographyLabels = new HashMap<ChoreographyEvent, ArrayList<String>>();
	public HashMap<String, ChState> existingStates = new HashMap<String, ChState>();
	public int eventCount;
	public ChState finalState = null;
	
	public ParallelEvent(String data) {
		String[] splitOnParallel = data.trim().split("\\|\\|");
		
		choreographies = new ArrayList<ChoreographyEvent>();
		
		for(int i = 0; i < splitOnParallel.length; ++i) {
			choreographies.add(new ChoreographyEvent(splitOnParallel[i].trim()));
		}
		eventCount = 0;
		type = "ParallelEvent";
	}
	
	public void parseParallelLabels(HashMap<String, LinkedList<BaseEvent>> globalCh) {
		System.out.println("test1");
		for(int i = 0; i < choreographies.size(); ++i) {
			System.out.println(choreographies.get(i).name);
			ArrayList<String> currLabels = new ArrayList<String>();
			LinkedList<BaseEvent> currChoreo = globalCh.get(choreographies.get(i).name);
			
			eventCount += currChoreo.size();
			
			for(int j = 0; j < currChoreo.size(); ++j) {
				currLabels.add(getLabel((ProcessEvent) currChoreo.get(j)));
			}
			System.out.println("test3");
			
			choreographyLabels.put(choreographies.get(i), currLabels);
			
			
			System.out.println("test4");
		}
	}
	
	public void testLabels() {
		for(int i = 0; i < choreographies.size(); ++i) {
			System.out.println(choreographies.get(i).name);
			ArrayList<String> currLabels = choreographyLabels.get(choreographies.get(i));
			for(int j = 0; j < currLabels.size(); ++j)
				System.out.println(currLabels.get(j));
			System.out.println();
		}
			
	}

	public String getLabel(ProcessEvent pe) {
		Process control = pe.control;
		ArrayList<Process> receiverProcesses = pe.processReceivers;
		String labelAt =  control.name;
		if(!control.port.equals("NuLL"))
			labelAt += "." + control.port;
		for(int i = 0; i < receiverProcesses.size(); ++i) {
			labelAt += "." + receiverProcesses.get(i).name;
			if(!receiverProcesses.get(i).port.equals("NuLL"))
				labelAt += "." + receiverProcesses.get(i).port;
		}
		
		if(!ChState.allLabels.containsKey(labelAt)) {
			ChState.allLabels.put(labelAt, 1);
			labelAt = labelAt + "_1_1";
		}
		else {
			ChState.allLabels.put(labelAt, ChState.allLabels.get(labelAt) + 1);
			labelAt = labelAt + "_" + ChState.allLabels.get(labelAt) + "_1";
		}
		return labelAt;
	}
	
	public ChState existsState(String lab) {
		String[] labSplit = lab.split(",");
		String comp = "";
		Arrays.sort(labSplit);
		for(int i = 0; i < labSplit.length - 1; ++i)
			comp += labSplit[i] + ",";
		comp += labSplit[labSplit.length - 1];
		if(existingStates.containsKey(comp))
			return existingStates.get(comp);
		return null;
	}
	
	public void addState(String lab, ChState s) {
		String[] labSplit = lab.split(",");
		String comp = "";
		Arrays.sort(labSplit);
		for(int i = 0; i < labSplit.length - 1; ++i)
			comp += labSplit[i] + ",";
		comp += labSplit[labSplit.length - 1];
		existingStates.put(comp, s);
		
		if(labSplit.length == eventCount) {
			finalState = s;
		}
	}
}
