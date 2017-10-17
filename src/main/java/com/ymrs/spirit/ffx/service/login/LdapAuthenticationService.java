package com.ymrs.spirit.ffx.service.login;

public interface LdapAuthenticationService {
	public boolean authenticate(String userName, String password);
}
