package Exceptions;

public class connectionException extends CouponSystemException {

	/**
	 * connectionException is an exception at the Connection pool level or is
	 * related to other database connection issues.
	 */
	private static final long serialVersionUID = 1L;

	public connectionException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public connectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public connectionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public connectionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public connectionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
