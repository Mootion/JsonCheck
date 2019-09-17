package com.mootion.json.exception;

/**
 * Json校验异常
 * @author MuCheng Shi
 *
 */
public class JsonCheckException extends Exception{

	private static final long serialVersionUID = 1L;

	public JsonCheckException() {
		super();
	}

	public JsonCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public JsonCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonCheckException(String message) {
		super(message);
	}

	public JsonCheckException(Throwable cause) {
		super(cause);
	}
	
}
