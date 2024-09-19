package eshmun.skeletontextrepresentation.infinitespace;

import java.util.HashSet;

 

public class StringHelpers {

	public static String cleanFromBools(String s, HashSet<String> labels) {
		
		//return s;
		 
		for (String label : labels) {
			
			s = s.replace(label, "");
			
		}
		
		boolean hasfaults = true;
		
		while(hasfaults) {
			
			if(s.contains("(not )")) {
				hasfaults = true;
				s = s.replace("(not )", " ");
				continue;
			}else if (s.contains("(or  )")) {
				hasfaults = true;
				s = s.replace("(or  )", " ");
				continue;
			}else  if (s.contains("(and  )")) {
				hasfaults = true;
				s = s.replace("(and  )", " ");
				continue;
			} else if (s.contains("(or)")) {
				hasfaults = true;
				s = s.replace("(or)", " ");
				continue;
			}else if (s.contains("(and)")) {
				hasfaults = true;
				s = s.replace("(and)", " ");
				continue;
			}
			else if (s.contains("( )")) {
				hasfaults = true;
				s = s.replace("( )", "");
				continue;
			}
			else if (s.contains("()")) {
				hasfaults = true;
				s = s.replace("()", " ");
				continue;
			}
				
			hasfaults = false;
			 
		}
		
		return s;
	}
}
