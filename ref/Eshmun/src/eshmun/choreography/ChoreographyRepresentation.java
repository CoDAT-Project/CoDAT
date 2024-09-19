package eshmun.choreography;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ChoreographyRepresentation {

	public String Convert(String input) throws Exception
	{
		Scanner scan = new Scanner(input.trim());
		HashMap<String, LinkedList<BaseEvent>> globalCh = new HashMap<String, LinkedList<BaseEvent>>();
		int i = 0, j;
		EventHelper eh = new EventHelper();
		StateHelper sh = new StateHelper();
		sh.numberOfStates = 0;
		ChState.allLabels = new HashMap<String, Integer>();
		String initialChoreography = "";
		
		while(scan.hasNextLine()) {
			String[] splitOnEqual = scan.nextLine().split("=");
			String key = splitOnEqual[0].trim();
			String valString = splitOnEqual[1].trim();
			String[] eventsData = valString.split("\\*");
			
			LinkedList<BaseEvent> valEvents = new LinkedList<BaseEvent>();
			for(j = 0; j < eventsData.length; ++j) {
				valEvents.add(eh.parseEvent(eventsData[j]));
			}
			if(i == 0)
				initialChoreography = key;
			globalCh.put(key, valEvents);
			
			i++;
		}
		
		String toText = "";
		StatesAndEnd saeTmp = sh.getState(globalCh, initialChoreography, null);
		ArrayList<ChState> states = saeTmp.states;
		ArrayList<ChState> ChEnd = saeTmp.endState;
		if (ChEnd != null && ChEnd.size() > 0)
			for (ChState ceTmp : ChEnd)
				ceTmp.addTransition(ceTmp.stateNumber);
		//ArrayList<ChState> states = sh.getState(globalCh, initialChoreography, null);
		
		//ArrayList<ChState> childStates = new ArrayList<ChState>();
		//for(ChState cs : states) {
		//	childStates.addAll(cs.childStates);
		//}
		//states.addAll(childStates);
		//System.out.println("importedprog(1,2,3,4)\nstates:");
		toText += "importedprog(1,2,3,4)\nstates:\n";
		for(i = 0; i < states.size(); ++i)
			//System.out.println(states.get(i).toString() + ":false:false;");
			if(i == 0)
				toText += states.get(i).toString() + ":true:false;\n";
			else
				toText += states.get(i).toString() + ":false:false;\n";
				
		//System.out.println("transitions:");
		toText += "transitions:\n";
		
		for(i = 0; i < states.size(); ++i)
			//System.out.print(states.get(i).transitionsToString());
			toText += states.get(i).transitionsToString();
		toText += "specifications:\nCTL Specifications";
		System.out.println(toText);
		return toText;
	}
}
