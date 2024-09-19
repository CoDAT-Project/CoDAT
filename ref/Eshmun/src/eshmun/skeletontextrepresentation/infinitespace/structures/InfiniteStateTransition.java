package eshmun.skeletontextrepresentation.infinitespace.structures;

import java.util.Objects;

 

public class InfiniteStateTransition {

	public InfiniteState sourceState;

	public InfiniteState destinationState;
	
	public int actionNumber;
	
	public int iterationNumber;
	
	public String command;
	
	public InfiniteStateTransition(InfiniteState source, InfiniteState destination, int actionNumber, String command ) {
		this.sourceState = source;
		this.destinationState= destination;
		this.actionNumber = actionNumber;
		this.command = command;
		
		
	 
	}
	
	public String getCommandForTextExport() {
		
		return command.replace(";", "-SEMI-");
	}
	
	@Override
	public String toString() {

		return actionNumber + ": \n"+ sourceState.toString() + "\n------->\n" + destinationState.toString();

	}

	@Override
	public boolean equals(Object o) {

		// If the object is compared with itself then return true
		if (o == this) {
			return true;
		}

		/* Check if o is an instance of State or not */
		if (!(o instanceof InfiniteStateTransition)) {
			return false;
		}

		InfiniteStateTransition s = (InfiniteStateTransition) o;

		// Compare the data members and return accordingly
		return this.sourceState.equals(s.sourceState) && this.destinationState.equals(s.destinationState);

	}

	@Override
	public int hashCode() {

		return Objects.hash(31);
	}

}
