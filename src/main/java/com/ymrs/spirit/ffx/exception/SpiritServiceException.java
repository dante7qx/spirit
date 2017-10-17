package com.ymrs.spirit.ffx.exception;

/**
 * 统一Rest API异常类
 * 
 * @author dante
 *
 */
public class SpiritServiceException extends Exception {

	private static final long serialVersionUID = -7795831940099270963L;

	/**
	 * 构造函数
	 */
	public SpiritServiceException() {
		super();
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public SpiritServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 */
	public SpiritServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 */
	public SpiritServiceException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 */
	public SpiritServiceException(Throwable cause) {
		super(cause);
	}

}
