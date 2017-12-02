package com.ymrs.spirit.ffx.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ymrs.spirit.ffx.prop.SpiritProperties;
import com.ymrs.spirit.ffx.util.IPUtils;
import com.ymrs.spirit.ffx.util.LoginUserUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(6)
@Component
public class SpiritSysSafeCheckAspect {
	
	@Autowired
	private SpiritProperties spiritProperties;

	@Pointcut("execution(public * com.ymrs.spirit.ffx.controller..*.*(..))")
	public void spiritSysSafeCheck() {
	}

	@Before("spiritSysSafeCheck()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		Boolean safeCheck = spiritProperties.getSafeCheck();
		if(!safeCheck) {
			return;
		}
		
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		SpiritLoginUser loginUser = LoginUserUtils.loginUser();
		final String method = request.getMethod();
		final String url = request.getRequestURL().toString();
		final String uri = request.getRequestURI();
		final String ip = IPUtils.getIpAddr(request);
		final String account = loginUser != null ? loginUser.getAccount() : "匿名用户";
		final String clazz = joinPoint.getSignature().getDeclaringTypeName();
		final String methodName = joinPoint.getSignature().getName();
		final Object[] args = joinPoint.getArgs();
		final String params = Arrays.toString(args);

		log.info("TODO: 安全检查待完成------------------------------------------->");

	}


}
