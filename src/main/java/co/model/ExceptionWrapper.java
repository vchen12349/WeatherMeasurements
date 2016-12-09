package co.model;

/**
 * Object that holds the return data to the gui so that we can report
 * errors in a more meaningful way.
 * @author chenvic
 *
 */
public class ExceptionWrapper {
	private String httpStatusCode;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getHttpStatusCode() {
		return httpStatusCode;
	}
	public void setHttpStatusCode(String httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

}
