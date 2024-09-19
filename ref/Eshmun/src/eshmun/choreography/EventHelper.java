package eshmun.choreography;

public class EventHelper {
	public BaseEvent parseEvent(String str) throws Exception {
		if(str.contains("while"))
			return new LoopEvent(str.trim());
		if(str.contains(">"))
			return new ProcessEvent(str.trim());
		if(str.contains("+"))
			return new BranchingEvent(str.trim());
		if(str.contains("||"))
			return new ParallelEvent(str.trim());
		return new ChoreographyEvent(str.trim());
		
	}
}
