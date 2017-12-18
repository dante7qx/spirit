package com.ymrs.spirit.ffx.security;

import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ymrs.spirit.ffx.dao.sysmgr.SysLogDAO;
import com.ymrs.spirit.ffx.po.sysmgr.SysLogPO;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.util.IPUtils;
import com.ymrs.spirit.ffx.util.LoginUserUtils;

@Aspect
@Order(5)
@Component
public class SpiritSysLogAspect {

	@Autowired
	private SysLogDAO sysLogDAO;

	private static final String ACCOUNT = "匿名用户"; 
	private static final String LOGOUT_URI = "/syslogout";
	private static final String SHORT_PKG = "c.y.s.f.c";
	
	ThreadLocal<Long> startTime = new ThreadLocal<>();
	ThreadLocal<String> logId = new ThreadLocal<>();

	@Pointcut("execution(public * com.ymrs.spirit.ffx.controller..*.*(..))")
	public void spiritSysLog() {
	}

	@Before("spiritSysLog()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		startTime.set(System.currentTimeMillis());
		logId.set(UUID.randomUUID().toString());

		SpiritLoginUser loginUser = LoginUserUtils.loginUser();
		final String method = request.getMethod();
		final String url = request.getRequestURL().toString();
		final String uri = request.getRequestURI();
		final String ip = IPUtils.getIpAddr(request);
		final String account = loginUser != null ? loginUser.getAccount() : "匿名用户";
		final String clazz = joinPoint.getSignature().getDeclaringTypeName().replaceAll("com.ymrs.spirit.ffx.controller", SHORT_PKG);
		final String methodName = joinPoint.getSignature().getName();
		final Object[] args = joinPoint.getArgs();
		final String params = Arrays.toString(args);
		recordLog(new SysLogPO(logId.get(), account, ip, method, url, uri, clazz, methodName, DateUtils.currentDate(),
				0L, params), null, null);
	}

	@AfterReturning(returning = "ret", pointcut = "spiritSysLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		recordLog(null, logId.get(), System.currentTimeMillis() - startTime.get());
	}

	@Async("syslogAsync")
	private void recordLog(SysLogPO sysLog, String id, Long spendTime) {
		if (sysLog != null) {
			if ((SHORT_PKG.concat(".sysmgr.SysLogController")).equals(sysLog.getClazz())
					|| (SHORT_PKG.concat(".SpiritController")).equals(sysLog.getClazz())) {
				return;
			}
			if(ACCOUNT.equals(sysLog.getAccount()) || LOGOUT_URI.equals(sysLog.getUri())) {
				sysLog.setParams("");
			}
			sysLogDAO.save(sysLog);
		} else {
			SysLogPO logPO = sysLogDAO.findOne(id);
			if (logPO != null) {
				logPO.setSpendTime(spendTime);
				sysLogDAO.save(logPO);
			}
		}

	}
}
