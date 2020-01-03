package exception;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Exceções para tratamento de conexão http
 *
 * @author michael
 */
public class McConnectionException extends IOException {

	/**
	 * Serial version.
	 */
	private static final long serialVersionUID = 7722853285264293417L;

	private static final int SC_INTERNAL_SERVER_ERROR = 500;
	private static final int SC_NOT_FOUND = 404;

	private Integer statusCode;

	public McConnectionException(Integer statusCode) {

		this.setStatusCode(statusCode);
	}

	public McConnectionException(Integer statusCode, String message) {

		super(message);
		this.setStatusCode(statusCode);
	}

	public McConnectionException(Integer statusCode, Throwable cause) {

		super(cause);
		this.setStatusCode(statusCode);
	}

	public McConnectionException(Integer statusCode, String message, Throwable cause) {

		super(message, cause);
		this.setStatusCode(statusCode);
	}

	public McConnectionException(String message, Throwable cause) {

		super(message, cause);
		setStatusCodeByCause(cause);
	}

	public McConnectionException(String message) {

		super(message);
		this.setStatusCode(SC_INTERNAL_SERVER_ERROR);
	}

	public McConnectionException(Throwable cause) {

		super(cause);
		setStatusCodeByCause(cause);
	}

	private void setStatusCodeByCause(Throwable cause) {
		if (cause instanceof McConnectionException) {
			this.setStatusCode(((McConnectionException) cause).getStatusCode());
		} else if(cause instanceof FileNotFoundException) {
			this.setStatusCode(SC_NOT_FOUND);
		} else {
			this.setStatusCode(SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Retorna o campo statusCode
	 *
	 * @return Retorna o campo statusCode
	 */
	public Integer getStatusCode() {

		return statusCode;
	}

	/**
	 * Define o valor para statusCode
	 *
	 * @param statusCode
	 *            o valor a definir em statusCode
	 */
	public void setStatusCode(Integer statusCode) {

		this.statusCode = statusCode;
	}

}
