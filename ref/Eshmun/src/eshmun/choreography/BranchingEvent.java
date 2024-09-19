package eshmun.choreography;
import java.util.ArrayList;

public class BranchingEvent extends BaseEvent{
	public Process control;
	public ArrayList<ChoreographyEvent> choreographies;
	
	public BranchingEvent(String data) {
		String[] splitOnPlus = data.trim().split("\\+");
		control = new Process(splitOnPlus[0].trim());
		splitOnPlus[1] = splitOnPlus[1].trim().substring(1);
		splitOnPlus[1] = splitOnPlus[1].substring(0, splitOnPlus[1].length() - 1);
		
		String[] splitOnComma = splitOnPlus[1].split(",");
		choreographies = new ArrayList<ChoreographyEvent>();
		
		for(int i = 0; i < splitOnComma.length; ++i) {
			choreographies.add(new ChoreographyEvent(splitOnComma[i].trim()));
		}
		
		type = "BranchingEvent";
	}
	
	public String print() {
		String toReturn = "\tControl: " + control.name + " " + control.port;
		for(int i = 0; i < choreographies.size(); ++i) {
			toReturn += "\n\tChoreography" + i +": ";
			toReturn += choreographies.get(i).name;
		}
		return toReturn;
	}
}
