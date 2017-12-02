package com.ymrs.spirit.ffx.mapper.sysmgr;

import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.ymrs.spirit.ffx.SpiritApplicationTests;
import com.ymrs.spirit.ffx.bo.sysmgr.AuthorityRoleBO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@Slf4j
public class AuthorityMapperTests extends SpiritApplicationTests {

	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Test
	public void findAuthorityRoleByRoleId() {
		try {
			List<AuthorityRoleBO> roleAuths = authorityMapper.findAuthorityRoleByRoleId(1L);
			assertNotNull(roleAuths);
		} catch (SpiritDaoException e) {
			log.error("findAuthorityRoleByRoleId 1 error.", e);
		}
	}
	
}
