package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.Date;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.SysLogPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.template.SpiritMongoTemplate;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.util.StringUtils;

@Service
public class SysLogService extends SpiritMongoTemplate<SysLogPO>{

	public PageResult<SysLogPO> findPage(PageReq pageReq) throws SpiritServiceException {
		return super.findEasyUIPage(pageReq, SysLogPO.class);
	}
	
	@Override
	protected Criteria buildCriteria(Map<String, Object> filter) {
		Criteria criteria = new Criteria();  
		String account = (String) filter.get("account");
		String ip = (String) filter.get("ip");
		String startDateStr = (String) filter.get("startDate");
		String endDateStr = (String) filter.get("endDate");
		if(StringUtils.isNotEmpty(account)) {
			criteria.and("account").regex(".*?"+account+".*");
		}
		if(StringUtils.isNotEmpty(ip)) {
			criteria.and("ip").regex(".*?"+ip+".*");
		}
		
			Date startDate = DateUtils.parseDateTime(startDateStr + " 00:00:00");
			Date endDate = DateUtils.parseDateTime(endDateStr + " 23:59:59");
			criteria.and("visitTime").gte(startDate).lte(endDate);;
		
		return criteria;
	}

}
