package eshmun.choreography;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoopEvent extends BaseEvent {
	public Process condition;
	public LinkedList<BaseEvent> events;
	public int conditionStateNumber = -1;
	public LoopEvent(String str) throws Exception {
		Pattern p = Pattern.compile("while\\((.+)\\) \\{(.+)\\}");
		Matcher m = p.matcher(str);
		if(m.matches()) {
		    //System.out.println("The quantity is " + m.group(1));
		    //System.out.println("The time is " + m.group(2));
			condition = new Process(m.group(1));
			String[] eventsData = m.group(2).split("\\^");
			EventHelper eh = new EventHelper();
			events = new LinkedList<BaseEvent>();
			for(int j = 0; j < eventsData.length; ++j) {
				events.add(eh.parseEvent(eventsData[j]));
			}
			
			type = "LoopEvent";
		}
		else {
			throw new Exception("Could not match loop event pattern");
		}
	}
	public String print() {
		String toReturn = "\tCondition: " + condition.name + " " + condition.port;
		for(int i = 0; i < events.size(); ++i) {
			toReturn += "\n\tEvent:" + i + " Type: " + events.get(i).type + "\n\t";
			toReturn += events.get(i).print().replace("\n", "\n\t");
		}
		
		//String[] tmp = toReturn.split("\n");
		//string toReturn
		
		return toReturn;
	}
}
