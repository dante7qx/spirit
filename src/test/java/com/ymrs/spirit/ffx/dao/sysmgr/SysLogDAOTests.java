package com.ymrs.spirit.ffx.dao.sysmgr;

import java.time.Instant;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymrs.spirit.ffx.SpiritApplicationTests;
import com.ymrs.spirit.ffx.po.sysmgr.SysLogPO;

public class SysLogDAOTests extends SpiritApplicationTests {

	@Autowired
	private SysLogDAO sysLogDAO;
	
	private final String id = "d4c7f375-88f7-46cf-bf3d-56e3b667df52";
	
	@Test
	public void save() {
		SysLogPO log = new SysLogPO(id, "test", "10.10.2.21", "post",
				"http://localhost/query/user", "/query/user", "com.ymrs.spirit.ffx.dao.sysmgr.SysLogDAOTests",
				"doQueryUser", Date.from(Instant.now()), 12131313L, "");
		sysLogDAO.save(log);
	}
	
	@Test
	public void delete() {
		sysLogDAO.delete(id);
	}
}
