package com.ymrs.spirit.ffx.exception;

/**
 * 统一DAO层异常类
 * 
 * @author dante
 *
 */
public class SpiritDaoException extends Exception {

	private static final long serialVersionUID = -3180530951608050408L;

	/**
	 * 构造函数
	 */
	public SpiritDaoException() {
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
	public SpiritDaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 * @param cause
	 */
	public SpiritDaoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 构造函数
	 * 
	 * @param message
	 */
	public SpiritDaoException(String message) {
		super(message);
	}

	/**
	 * 构造函数
	 * 
	 * @param cause
	 */
	public SpiritDaoException(Throwable cause) {
		super(cause);
	}

}
