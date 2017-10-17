package com.ymrs.spirit.ffx.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常类
 * 
 * @author dante
 *
 */
public class KaptchaException extends AuthenticationException {

	private static final long serialVersionUID = 429705470163016232L;

	public KaptchaException(String msg) {
		super(msg);
	}

	public KaptchaException(String msg, Throwable t) {
		super(msg, t);
	}

}
