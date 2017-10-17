package com.ymrs.spirit.ffx.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spirit 密码辅助类
 * 
 * @author dante
 *
 */
public class SpiritPasswordEncoder implements PasswordEncoder {
	
	private final static BCryptPasswordEncoder dcryptPasswordEncoder = new BCryptPasswordEncoder();

	@Override
	public String encode(CharSequence rawPassword) {
		return dcryptPasswordEncoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if(rawPassword.equals(encodedPassword)) {
			return true;
		}
		return dcryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}

}
