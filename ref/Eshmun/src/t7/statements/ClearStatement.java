package t7.statements;
/**
 * Represents a clear statement.
 * @author kinan
 */
public class ClearStatement implements Statement {
	
	/**
	 * @return ClearStatement.
	 */
	@Override
	public Types getType() {
		return Types.ClearStatement;
	}
}
