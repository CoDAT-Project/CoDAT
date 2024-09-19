package eshmun.choreography;

public class ChoreographyEvent extends BaseEvent {
	public String name;
	
	public ChoreographyEvent(String name) {
		this.name = name;
		type = "ChoreographyEvent";
	}
	@Override
	public String print() {
		return "\tChoreography: " + name;
	}
}
