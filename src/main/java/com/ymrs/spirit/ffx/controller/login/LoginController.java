package com.ymrs.spirit.ffx.controller.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ymrs.spirit.ffx.constant.SecurityConsts;
import com.ymrs.spirit.ffx.prop.SpiritProperties;


@Controller
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
//	@Autowired
//	private SysLogService sysLogService;
	@Autowired
	private SpiritProperties spiritProperties;
	
	/**
	 * 登录页
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginpage", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Model model) {
		model.addAttribute("kaptcha", spiritProperties.getKaptcha());
		return "login/spirit";
	}
	
	/**
	 * 登录页2
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginpage2", method = RequestMethod.GET)
	public String login2(HttpServletRequest request, Model model) {
		model.addAttribute("kaptcha", spiritProperties.getKaptcha());
		return "login/cloud";
	}
	
	/**
	 * 用户注销
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syslogout", method = RequestMethod.POST)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();  
	    if (auth != null){    
	    	/*
	    	String loginAccount = auth.getName();
	    	try {
				sysLogService.recordUserVisitLog(SysLogUtils.buildSysLogoutLog(loginAccount, request));
			} catch (SpiritServiceException e) {
				LOGGER.error("系统日志记录失败，{}", loginAccount, e);
			};
			*/
	        new SecurityContextLogoutHandler().logout(request, response, auth);  
	    }  
	    return "redirect:"+request.getContextPath() + SecurityConsts.LOGIN_PAGE+"?logout";
	}
	
}
