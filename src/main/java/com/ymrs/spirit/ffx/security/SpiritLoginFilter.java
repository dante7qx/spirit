package com.ymrs.spirit.ffx.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ymrs.spirit.ffx.constant.SecurityConsts;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserAuthRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.service.auth.LdapAuthenticationService;
import com.ymrs.spirit.ffx.service.sysmgr.OnlineUserService;
import com.ymrs.spirit.ffx.service.sysmgr.UserService;
import com.ymrs.spirit.ffx.util.EncryptUtils;

/**
 * 自定义登录认证过滤器
 * 
 * @author dante
 *
 */
public class SpiritLoginFilter extends UsernamePasswordAuthenticationFilter {
	
	@Autowired
	private UserService userService;
	@Autowired
	private LdapAuthenticationService ldapAuthenticationService;
	@Autowired
	private OnlineUserService onlineUserService;

	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		String username = obtainUsername(request);
		String password = obtainPassword(request);
		if (username == null) {
			throw new UsernameNotFoundException("用户名不能为空");
		}
		if (password == null) {
			throw new UsernameNotFoundException("密码不能为空");
		}
		username = username.trim();
		UserAuthRespDTO loginUser = null;
		try {
			loginUser = userService.loginAccount(username);
		} catch (SpiritServiceException e) {
			throw new UsernameNotFoundException("系统错误，请稍后重试！", e);
		}
		if(loginUser == null) {
			throw new UsernameNotFoundException("用户名["+username+"]不存在！");
		}
		if(SecurityConsts.STATUS_LOCK.equalsIgnoreCase(loginUser.getStatus())) {
			throw new UsernameNotFoundException("用户名["+username+"]被锁定！");
		} else if(SecurityConsts.STATUS_DEL.equalsIgnoreCase(loginUser.getStatus())) {
			throw new UsernameNotFoundException("用户名["+username+"]已被删除！");
		}
		if(loginUser.getLdapUser() != null && loginUser.getLdapUser().booleanValue()) {
			if(!ldapAuthenticationService.authenticate(username, password)) {
				throw new UsernameNotFoundException("用户名或密码错误");
			}
			password = loginUser.getPassword();
		} else if(!EncryptUtils.match(password, loginUser.getPassword())) {
			throw new UsernameNotFoundException("密码错误");
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		super.setDetails(request, authRequest);
		Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
		if(authentication.isAuthenticated()) {
			onlineUserService.addOnlineUser(username, request);
		}
		
		
		return authentication;
	}
	
	
}
