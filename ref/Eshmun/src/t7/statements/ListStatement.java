package t7.statements;

/**
 * A List Statement, lists the already instantiated structure (name + indices).
 * @author kinan
 *
 */
public class ListStatement implements Statement {
	/**
	 * @return List.
	 */
	@Override
	public Types getType() {
		return Types.List;
	}
}
