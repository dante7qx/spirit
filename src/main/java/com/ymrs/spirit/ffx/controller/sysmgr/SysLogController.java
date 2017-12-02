package com.ymrs.spirit.ffx.controller.sysmgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.SysLogPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.service.sysmgr.SysLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sysmgr/syslog")
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;
	
	@PreAuthorize("hasAuthority('sysmgr.role.query')")
	@PostMapping(value = "/query_page")
	public PageResult<SysLogPO> querySysLogPage(PageReq pageReq) {
		PageResult<SysLogPO> result = null;
		try {
			result = sysLogService.findPage(pageReq);
		} catch (SpiritServiceException e) {
			log.error("querySysLogPage {} error.", pageReq, e);
		}
		return result;
	}
}
