package com.ymrs.spirit.ffx.mapper.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.bo.sysmgr.AuthorityRoleBO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;

/**
 * 权限 Mapper
 * 
 * @author dante
 *
 */
public interface AuthorityMapper {

	public List<AuthorityRoleBO> findAuthorityRoleByRoleId(Long roleId) throws SpiritDaoException;
	
}
