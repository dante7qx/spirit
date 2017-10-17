package com.ymrs.spirit.ffx.service.login.impl;

import javax.naming.directory.DirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import com.ymrs.spirit.ffx.service.login.LdapAuthenticationService;


@Service
public class LdapAuthenticationServiceImpl implements LdapAuthenticationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LdapAuthenticationServiceImpl.class);

	@Autowired
	private LdapTemplate ldapTemplate;
	
	@Override
	public boolean authenticate(String userName, String password) {
		DirContext ctx = null;
		try {
			ctx = ldapTemplate.getContextSource().getContext(userName + "@hna.net", password); 
			return true;
		} catch (NamingException e) {
			LOGGER.error("Ldap auth [{}] error.", userName, e);
			return false;
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}

}
