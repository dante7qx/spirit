package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.dto.req.sysmgr.RoleReqDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.AuthorityRoleRespDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.RoleRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;

public interface RoleService extends SpiritAbstractService<RoleReqDTO, RoleRespDTO>{
	
	public List<RoleRespDTO> findAllRoles() throws SpiritServiceException;
	
	public List<AuthorityRoleRespDTO> findAuthorityRoleByRoleId(Long roleId) throws SpiritServiceException;
	
}
