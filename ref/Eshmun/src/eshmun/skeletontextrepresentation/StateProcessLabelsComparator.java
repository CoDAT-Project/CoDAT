package eshmun.skeletontextrepresentation;

import java.util.Comparator;

public class StateProcessLabelsComparator implements Comparator<String>{
	
	@Override

	public int compare(String s1, String s2) {
		
	       String substr1 = s1.substring(0, s1.length()-1);
 	       String substr2 = s2.substring(0, s2.length()-1);
//	                
 	       return String.valueOf(substr1).compareTo(String.valueOf(substr2));    
	}

}
