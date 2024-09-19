package eshmun.choreography;

import java.util.ArrayList;

public class StatesAndEnd {
	public ArrayList<ChState> endState = new ArrayList<ChState>();
	public ArrayList<ChState> states = new ArrayList<ChState>();
	
	public StatesAndEnd(ArrayList<ChState> es, ArrayList<ChState> s) {
		endState = es;
		states = s;
	}
}
