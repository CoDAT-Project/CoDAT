package t7.statements;

/**
 * Represents a Start statement, it will start eshmun with the instantiated structures
 * inside.
 * @author kinan
 */
public class StartStatement implements Statement {
	
	/**
	 * @return Start.
	 */
	@Override
	public Types getType() {
		return Types.Start;
	}
}
