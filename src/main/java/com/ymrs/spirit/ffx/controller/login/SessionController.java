package com.ymrs.spirit.ffx.controller.login;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ymrs.spirit.ffx.constant.SecurityConsts;
import com.ymrs.spirit.ffx.security.SpiritLoginUser;
import com.ymrs.spirit.ffx.util.LoginUserUtils;

/**
 * 当session过期时，ajax请求跳转到登录页
 * 
 * @author dante
 *
 */
@Controller
public class SessionController {

	/**
	 * 处理session过期
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/session-timeout")
	public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { // ajax 超时处理
			response.getWriter().print("timeout"); // 设置超时标识
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().close();
		} else {
			try {
				SpiritLoginUser loginUser = LoginUserUtils.loginUser();
				if (loginUser != null) {
					response.sendRedirect(request.getContextPath() + SecurityConsts.INDEX_PAGE);
				} else {
					response.sendRedirect(request.getContextPath() + SecurityConsts.LOGIN_PAGE);
				}
			} catch (Exception e) {
				response.sendRedirect(request.getContextPath() + SecurityConsts.LOGIN_PAGE);
			}
		}
	}
}
