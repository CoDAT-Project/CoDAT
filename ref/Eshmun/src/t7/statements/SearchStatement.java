package t7.statements;

/**
 * A search query, searches for already instantiated structures that satisfy the query (REGEX).
 * @author kinan
 *
 */
public class SearchStatement implements Statement {
	/**
	 * The search query.
	 */
	private String query;
	
	/**
	 * Creates a new search statement.
	 * @param query the search query.
	 */
	public SearchStatement(String query) {
		this.query = query;
	}
	
	/**
	 * @return Export.
	 */
	@Override
	public Types getType() {
		return Types.Search;
	}
	
	/**
	 * @return the search query.
	 */
	public String getQuery() {
		return query;
	}
}
