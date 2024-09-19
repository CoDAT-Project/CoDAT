package eshmun.choreography;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessEvent extends BaseEvent {
	public Process control;
	public ArrayList<Process> processReceivers = new ArrayList<Process>();
	
	public ProcessEvent(String data) {
		String[] splitOnArrow = data.split(">");
		control = new Process(splitOnArrow[0].trim());
		if(splitOnArrow[1].trim().charAt(0) == '{') {
			splitOnArrow[1] = splitOnArrow[1].trim().substring(1);
			splitOnArrow[1] = splitOnArrow[1].substring(0, splitOnArrow[1].length() - 1);
			String[] receivers = splitOnArrow[1].split(",");
			
			// trim and sort the receivers
			for(int i = 0; i < receivers.length; ++i)
				receivers[i] = receivers[i].trim();
			Arrays.sort(receivers);
			
			for(int i = 0; i < receivers.length; ++i)
				processReceivers.add(new Process(receivers[i].trim()));
		}
		else
			processReceivers.add(new Process(splitOnArrow[1].trim()));
		type = "ProcessEvent";
	}
	
	@Override
	public String print() {
		String toReturn = "\tControl: " + control.name + " " + control.port;
		for(int i = 0; i < processReceivers.size(); ++i) {
			toReturn += "\n\tReceiver" + i +": ";
			toReturn += processReceivers.get(i).name + " " + processReceivers.get(i).port;
		}
		return toReturn;
	}
}
