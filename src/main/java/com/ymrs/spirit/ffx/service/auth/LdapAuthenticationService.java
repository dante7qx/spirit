package com.ymrs.spirit.ffx.service.auth;

public interface LdapAuthenticationService {
	public boolean authenticate(String userName, String password);
}
