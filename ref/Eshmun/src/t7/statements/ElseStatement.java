package t7.statements;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Else statement.
 * @author kinan
 *
 */
public class ElseStatement implements Statement {
	
	/**
	 * The statements in the else body.
	 */
	private List<Statement> elseBody;
	
	/**
	 * Creates a new -initially- empty else statement.
	 */
	public ElseStatement() {
		elseBody = new ArrayList<Statement>();
	}
	
	/**
	 * Appends a statement to the end of the body of the else statement.
	 * @param bodyStatement the statement to append.
	 */
	public void appendElseBody(Statement bodyStatement) {
		elseBody.add(bodyStatement);
	}
	
	/**
	 * @return The else's body, could be empty (but not null).
	 */
	public List<Statement> getElseBody() {
		return elseBody;
	}
	
	/**
	 * Else.
	 */
	@Override
	public Types getType() {
		return Types.Else;
	}

}
