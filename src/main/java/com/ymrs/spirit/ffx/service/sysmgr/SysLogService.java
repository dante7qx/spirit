package com.ymrs.spirit.ffx.service.sysmgr;

import static com.ymrs.spirit.ffx.security.SpiritSysLogAspect.ACCOUNT;
import static com.ymrs.spirit.ffx.security.SpiritSysLogAspect.LOGOUT_URI;
import static com.ymrs.spirit.ffx.security.SpiritSysLogAspect.SHORT_PKG;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ymrs.spirit.ffx.dao.sysmgr.SysLogDAO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.SysLogPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.template.SpiritMongoTemplate;
import com.ymrs.spirit.ffx.util.DateUtils;

@Service
public class SysLogService extends SpiritMongoTemplate<SysLogPO> {

	@Autowired
	private SysLogDAO sysLogDAO;

	public PageResult<SysLogPO> findPage(PageReq pageReq) throws SpiritServiceException {
		return super.findEasyUIPage(pageReq, SysLogPO.class);
	}

	/**
	 * 记录系统日志
	 * 
	 * @param sysLog
	 * @param id
	 * @param spendTime
	 */
	@Async("syslogAsync")
	public void recordLog(SysLogPO sysLog, String id, Long spendTime) {
		if (sysLog != null) {
			if ((SHORT_PKG.concat(".sysmgr.SysLogController")).equals(sysLog.getClazz())
					|| (SHORT_PKG.concat(".SpiritController")).equals(sysLog.getClazz())) {
				return;
			}
			if (ACCOUNT.equals(sysLog.getAccount()) || LOGOUT_URI.equals(sysLog.getUri())) {
				sysLog.setParams("");
			}
			sysLogDAO.save(sysLog);
		} else {
			SysLogPO logPO = sysLogDAO.findById(id).get();
			if (logPO != null) {
				logPO.setSpendTime(spendTime);
				sysLogDAO.save(logPO);
			}
		}

	}

	@Override
	protected Criteria buildCriteria(Map<String, Object> filter) {
		Criteria criteria = new Criteria();
		String account = (String) filter.get("account");
		String ip = (String) filter.get("ip");
		String startDateStr = (String) filter.get("startDate");
		String endDateStr = (String) filter.get("endDate");
		if (StringUtils.hasText(account)) {
			criteria.and("account").regex(".*?" + account + ".*");
		}
		if (StringUtils.hasText(ip)) {
			criteria.and("ip").regex(".*?" + ip + ".*");
		}

		Date startDate = DateUtils.parseDateTime(startDateStr + " 00:00:00");
		Date endDate = DateUtils.parseDateTime(endDateStr + " 23:59:59");
		criteria.and("visitTime").gte(startDate).lte(endDate);
		;

		return criteria;
	}

}
