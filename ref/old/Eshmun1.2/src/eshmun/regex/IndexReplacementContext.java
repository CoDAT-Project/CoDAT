package eshmun.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class IndexReplacementContext {
	/** the list of index parameter names */
	private List<String> indexNames;
	/** the list of indexes */
	private List<Integer> indexes;
	/** the regex evaluator to do index replacements */
	private IRegexEvaluator regexEvaluator;
	/** a map to keep the old replacements to avoid extra computation */
	private Map<String, String> replacedVariablesCache;
	/** a map to keep the old appended variables to avoid extra computation */
	private Map<String, String> appendedVariablesCache;
	
	public IndexReplacementContext(List<String> indexNames, List<Integer> indexes, IRegexEvaluator regexEvaluator) {
		super();
		this.indexNames = indexNames;
		this.indexes = indexes;
		this.regexEvaluator = regexEvaluator;
		this.replacedVariablesCache = new HashMap<String, String>();
		this.appendedVariablesCache = new HashMap<String, String>();
	}
	
	/**
	 * replaces the parameterized indexes with the actual values of the indexes
	 * @param variable the parameterized variable
	 * @return the variable name with parameterized indexes repalced
	 */
	public String replaceIndexes (String variable) {
		String replacedVariable = replacedVariablesCache.get(variable);
		if (replacedVariable == null) {
			replacedVariable = regexEvaluator.replaceIndexes(variable, indexNames, indexes);
			replacedVariablesCache.put(variable, replacedVariable);
		} 
		return replacedVariable;
	}
	
	public String appendIndexes (String variable) {
		String appendedVariable = appendedVariablesCache.get(variable);
		if (appendedVariable == null) {
			appendedVariable = variable;
			for (Integer index : indexes) {
				appendedVariable += index;
			}
			appendedVariablesCache.put(variable, appendedVariable);
		}
		return appendedVariable;
	}
	
	public boolean isIndexName(String indexName) {
		return  indexNames != null && indexNames.contains(indexName);
	}
	
	public int getIndexNameValue(String indexName) {
		int index  = -1;
		if (indexNames != null) {
			int indexNameIndex = indexNames.indexOf(indexName);
			if (indexNameIndex != -1) {
				index = indexes.get(indexNameIndex);
			}
		}
		return index;
	}
	
	public List<String> getIndexNames() {
		return indexNames;
	}
	public List<Integer> getIndexes() {
		return indexes;
	}
	public IRegexEvaluator getRegexEvaluator() {
		return regexEvaluator;
	}
	
	@Override
	public IndexReplacementContext clone() throws CloneNotSupportedException {
		List<Integer> cloneIndexes = new ArrayList<Integer>();
		for (Integer index : getIndexes()) {
			cloneIndexes.add(index);
		}
		List<String> cloneIndexNames = new ArrayList<String>();
		for (String indexName : getIndexNames()) {
			cloneIndexNames.add(indexName);
		}
		IRegexEvaluator regexEvaluator = this.getRegexEvaluator().clone();
		IndexReplacementContext clone = new IndexReplacementContext(cloneIndexNames, cloneIndexes, regexEvaluator);
		return clone;
	}
}
