package t7.statements;

/**
 * A Load Statement, loads and execute commands from an external file.
 * @author kinan
 *
 */
public class LoadStatement implements Statement {
	/**
	 * The file to load from.
	 */
	private String path;
	
	/**
	 * Creates a new load statement that will load commands
	 * from the given path.
	 * @param path the path of the file to load from.
	 */
	public LoadStatement(String path) {
		this.path = path;
	}
	
	/**
	 * @return Load.
	 */
	@Override
	public Types getType() {
		return Types.Load;
	}
	
	/**
	 * @return the path of the file to load from.
	 */
	public String getPath() {
		return path;
	}
}
