package eshmun.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexEvaluator implements IRegexEvaluator{
	List<Pattern> patterns;
	
	public RegexEvaluator() {
		super();
		patterns = new ArrayList<Pattern>();
	}

	@Override
	public void addRegexPattern(String regexPattern) {
		if (regexPattern != null && !regexPattern.equals("")) {
			Pattern pattern = Pattern.compile(regexPattern);
			patterns.add(pattern);
		}
	}

	@Override
	public List<IRegexMatches> matchRegex(String variable) {
		List<IRegexMatches> regexMatches = new ArrayList<IRegexMatches>();
		for (Pattern pattern : patterns) {
			Matcher matcher = pattern.matcher(variable);
			while (matcher.find()) {
				RegexMatches regexMatch = new RegexMatches(matcher.start(), matcher.end(), matcher.group(), matcher.pattern().pattern());
				regexMatches.add(regexMatch);
			}
		}
		return regexMatches;
	}

	@Override
	public String replaceIndexes(String variable, List<String> indexNames, List<Integer> indexes) {
		List<IRegexMatches> matches = matchRegex(variable);
		String newVariable = variable;
		for (IRegexMatches match : matches) {
			newVariable = 
				newVariable.substring(0, match.getStartIndex()) +
				replaceIndexesInMatch(match, indexNames, indexes) +
				newVariable.substring(match.getEndIndex());
		}
		
		return newVariable;
	}

	protected String replaceIndexesInMatch(IRegexMatches match, List<String> indexNames, List<Integer> indexes) {
		String replacedMatch = match.getMatch();
		int indexesSize = indexes.size();
		int nameIndex = 0;
		for (String indexName : indexNames) {
			if(nameIndex < indexesSize) { 
				replacedMatch = replacedMatch.replaceAll(indexName, indexes.get(nameIndex).toString());
			}
			nameIndex++;
		}
		return replacedMatch;
	}
	
	public class RegexMatches implements IRegexMatches {
		private int endIndex;
		public RegexMatches(int startIndex, int endIndex, String match, String patternMatched) {
			super();
			this.endIndex = endIndex;
			this.startIndex = startIndex;
			this.match = match;
			this.patternMatched = patternMatched;
		}

		private int startIndex;
		private String match;
		private String patternMatched;
		
		@Override
		public int getEndIndex() {
			return endIndex;
		}

		@Override
		public String getMatch() {
			return match;
		}

		@Override
		public String getPatternMatched() {
			return patternMatched;
		}

		@Override
		public int getStartIndex() {
			return startIndex;
		}
	}

	@Override
	public IRegexEvaluator clone() throws CloneNotSupportedException {
		IRegexEvaluator regexEvaluator = new RegexEvaluator();
		for (Pattern pattern : this.patterns) {
			regexEvaluator.addRegexPattern(pattern.toString());
		}
		return regexEvaluator;
	}
}