package t7.statements;

/**
 * An Export Statement, saves the instantiated structures to a given external file.
 * @author kinan
 *
 */
public class ExportStatement implements Statement {
	/**
	 * The file to save to.
	 */
	private String path;
	
	/**
	 * Creates a new export statement that will save structures
	 * to the given path.
	 * @param path the path of the file to save to.
	 */
	public ExportStatement(String path) {
		this.path = path;
	}
	
	/**
	 * @return Export.
	 */
	@Override
	public Types getType() {
		return Types.Export;
	}
	
	/**
	 * @return the path of the file to save to.
	 */
	public String getPath() {
		return path;
	}
}
