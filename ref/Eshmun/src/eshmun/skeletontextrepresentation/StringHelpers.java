package eshmun.skeletontextrepresentation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class has string manipulation methods that might be used by whole package.
 * 
 * @author chukrisoueidi
 *
 */
public class StringHelpers {

	/**
	 * Removes null assignments (x=null) and cleans consecutive commas in states resulting from deletions (N1,N2,,x=1)
	 * 
	 * @param string
	 * @return same string cleaned
	 */
	public static String removeNullsAndCommas(String state) {

		if (!state.contains("null"))
			return state;
		String newstr = state;
		//Regex pattern to identify all null assignments
		Pattern pattern = Pattern.compile("([^,.]+)=null");
		Matcher matcher = pattern.matcher(state);
		while (matcher.find()) {

			newstr = newstr.replace(matcher.group(0), "");

		}

		//clean multiple commas
		newstr = newstr.replace(",,", ",");
		newstr = newstr.replace(",--", "--");
		
		if (newstr.endsWith(","))
			newstr = newstr.substring(0, newstr.length() - 1);

		return newstr;
	}

}
