// ______________________________________________________
// Generated by sql2java - http://sql2java.sourceforge.net/
// jdbc driver used at code generation time: org.postgresql.Driver
//
// Please help us improve this tool by reporting:
// - problems and suggestions to
//   http://sourceforge.net/tracker/?group_id=54687
// - feedbacks and ideas on
//   http://sourceforge.net/forum/forum.php?forum_id=182208
// ______________________________________________________

package mirnator.sql2java.exception;

/**
 * @author sql2java
 */
public class DataAccessException extends DAOException {
	private static final long serialVersionUID = 5584785072347143333L;

	/**
	 * contructor
	 */
	public DataAccessException() {
		super();
	}

	/**
	 * contructor
	 */
	public DataAccessException(String message) {
		super(message);
	}

	/**
	 * contructor
	 */
	public DataAccessException(Throwable cause) {
		super(cause);
	}

	/**
	 * contructor
	 */
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
