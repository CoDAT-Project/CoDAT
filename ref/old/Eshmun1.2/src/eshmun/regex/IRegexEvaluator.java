package eshmun.regex;

import java.util.List;

public interface IRegexEvaluator {
	/** adds a a regex pattern to the list of patterns to be used to match strings */
	public void addRegexPattern(String regexPattern);
	public IRegexEvaluator clone() throws CloneNotSupportedException;
	/** 
	 * searches input variable for a matching pattern, for the matches found,
	 * index names are replaced with the index values 
	 * @param variable
	 * @param indexNames
	 * @param indexes
	 * @return
	 */
	public String replaceIndexes(String variable, List<String> indexNames, List<Integer> indexes);
	
	public List<IRegexMatches> matchRegex(String variable);
	
	public interface IRegexMatches {
		public int getStartIndex();
		public int getEndIndex();
		public String getPatternMatched();
		public String getMatch();
	}
}
