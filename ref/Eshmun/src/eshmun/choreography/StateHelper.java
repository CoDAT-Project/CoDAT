package eshmun.choreography;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class StateHelper {
	public static int numberOfStates = 0;

	public StatesAndEnd getState(HashMap<String, LinkedList<BaseEvent>> globalCh, String key,
			ArrayList<ChState> ChEnd) {
		ArrayList<ChState> states = new ArrayList<ChState>();
		ChState s;
		LinkedList<BaseEvent> be;

		be = globalCh.get(key);
		for (int i = 0; i < be.size(); ++i) {
			if (be.get(i).type.equals("ProcessEvent")) { // Process Event
				ProcessEvent pe = (ProcessEvent) be.get(i);
				s = new ChState(key, pe.control, pe.processReceivers);

				if (i != 0 && ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(s.stateNumber);
				states.add(s);
				ChEnd = new ArrayList<ChState>();
				ChEnd.add(s);
			}
			if (be.get(i).type.equals("LoopEvent")) { // Loop Event
				LoopEvent le = (LoopEvent) be.get(i);
				LinkedList<BaseEvent> events = le.events;
				s = new ChState(key, le.condition, new ArrayList<Process>());
				ChState LoopStart = s;
				le.conditionStateNumber = s.stateNumber;

				if (ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(s.stateNumber); // all point to condition
				states.add(s);
				ChEnd = new ArrayList<ChState>();
				ChEnd.add(s);
				for (int j = 0; j < events.size(); ++j) {
					if (events.get(j).type.equals("ProcessEvent")) {
						ProcessEvent pe = (ProcessEvent) events.get(j);
						s = new ChState(key, pe.control, pe.processReceivers);
						s.connectable = false;
						if (ChEnd != null && ChEnd.size() > 0)
							for (ChState ceTmp : ChEnd)
								ceTmp.addTransition(s.stateNumber); // all point to condition
						states.add(s);
						ChEnd = new ArrayList<ChState>();
						ChEnd.add(s);

					} else if (events.get(j).type.equals("ChoreographyEvent")) {
						StatesAndEnd saeTmp = getState(globalCh, ((ChoreographyEvent) events.get(j)).name, null);
						ArrayList<ChState> childStates = saeTmp.states;

						if (ChEnd != null && ChEnd.size() > 0)
							for (ChState ceTmp : ChEnd)
								ceTmp.addTransition(childStates.get(0).stateNumber); // all point to condition

						ChEnd = saeTmp.endState;

						states.addAll(childStates);
					}
				}
				if (ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(LoopStart.stateNumber); // all point to condition
				ChEnd = new ArrayList<ChState>();
				ChEnd.add(LoopStart);
			} else if (be.get(i).type.equals("ChoreographyEvent")) {
				StatesAndEnd saeTmp = getState(globalCh, ((ChoreographyEvent) be.get(i)).name, ChEnd);
				ArrayList<ChState> childStates = saeTmp.states;

				if (ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(childStates.get(0).stateNumber); // all point to condition
				ChEnd = new ArrayList<ChState>();
				ChEnd = saeTmp.endState;

				states.addAll(childStates);
			} else if (be.get(i).type.equals("BranchingEvent")) {
				System.out.println("BRANCHING");
				BranchingEvent beTmp = (BranchingEvent) be.get(i);

				beTmp.control.port = "Branching";
				ChState branchState = new ChState(key, beTmp.control, new ArrayList<Process>());
				
				if (i != 0 && ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(branchState.stateNumber);
				states.add(branchState);
				ChEnd = new ArrayList<ChState>();
				ChEnd.add(branchState);

				boolean hasEbs = false;
				ArrayList<ChState> newEnd = new ArrayList<ChState>();
				for (ChoreographyEvent choreoTmp : beTmp.choreographies) {

					if (choreoTmp.name.equals("E")) {
						hasEbs = true;
						continue;
					}

					StatesAndEnd saeTmp = getState(globalCh, choreoTmp.name, ChEnd);
					ArrayList<ChState> childStates = saeTmp.states;
					if (ChEnd != null && ChEnd.size() > 0)
						for (ChState ceTmp : ChEnd)
							ceTmp.addTransition(childStates.get(0).stateNumber);
					newEnd.addAll(saeTmp.endState);
					states.addAll(childStates);
				}
				if (hasEbs)
					newEnd.addAll(ChEnd);
				ChEnd = newEnd;

			} else if (be.get(i).type.equals("ParallelEvent")) {
				//System.out.println("PARALLEL");
				ParallelEvent parEvent = (ParallelEvent) be.get(i);
				parEvent.parseParallelLabels(globalCh);
				parEvent.testLabels();

				int[] indexes = new int[parEvent.choreographies.size()];
				Arrays.fill(indexes, 0);
				
				
				//s = new ChState(key, key + ".Parallel");
				s = new ChState(key, new Process(key, "Parallel"), new ArrayList<Process>());
				states.add(s);

				if (ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(s.stateNumber);
				ChEnd = new ArrayList<ChState>();
				ChEnd.add(s);
				
				StatesAndEnd saeTmp = getStateParallel(parEvent, "", indexes, ChEnd);
				ChEnd = new ArrayList<ChState>();
				if(parEvent.finalState == null)
					System.out.println("HELLO");
				ChEnd.add(parEvent.finalState);
				states.addAll(saeTmp.states);
				
				
			}

		}
		StatesAndEnd sae = new StatesAndEnd(ChEnd, states);
		return sae;
	}
	public StatesAndEnd getStateParallel(ParallelEvent parEvent, String lab, int[] ind, ArrayList<ChState> ChEnd) {

		ArrayList<ChState> states = new ArrayList<ChState>();
		ArrayList<ChState> tmpEnd;
		ChState s;
		String tmpLabel;
		int[] tmpIndexes;
		for(int i = 0; i < parEvent.choreographies.size(); ++i) {
			ChoreographyEvent currChor = parEvent.choreographies.get(i);
			//ind[i]++;
			tmpIndexes = new int[ind.length];
			for(int z = 0; z < ind.length; ++z){
				tmpIndexes[z] = ind[z];
			}
			if(parEvent.choreographyLabels.get(currChor).size() > tmpIndexes[i]) {
				//System.out.println("test3");
				tmpLabel = parEvent.choreographyLabels.get(currChor).get(tmpIndexes[i]);
				//System.out.println("test4");
				if(!lab.equals(""))
					tmpLabel = lab + "," + tmpLabel;
				s = parEvent.existsState(tmpLabel);
				if(s == null) {
					s = new ChState(currChor.name, tmpLabel);
					states.add(s);
					parEvent.addState(tmpLabel, s);
				}
				if (ChEnd != null && ChEnd.size() > 0)
					for (ChState ceTmp : ChEnd)
						ceTmp.addTransition(s.stateNumber);
				//ArrayList<String> tmp = new ArrayList<>();
				//tmp.addAll(parEvent.choreographyLabels.get(currChor));
				//tmp.remove(0);
				//parEvent.choreographyLabels.get(currChor).remove(0);
				tmpIndexes[i]++;
				tmpEnd = new ArrayList<ChState>();
				tmpEnd.add(s);
				StatesAndEnd sae = getStateParallel(parEvent, tmpLabel, tmpIndexes, tmpEnd);
				states.addAll(sae.states);
			}
		}

		StatesAndEnd sae = new StatesAndEnd(ChEnd, states);
		return sae;
	}
}
