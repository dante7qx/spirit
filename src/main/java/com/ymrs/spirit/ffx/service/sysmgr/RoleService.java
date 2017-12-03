package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.dto.sysmgr.req.RoleReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.RoleRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;
import com.ymrs.spirit.ffx.vo.sysmgr.AuthorityRoleTreeVO;
import com.ymrs.spirit.ffx.vo.sysmgr.RoleTreeVO;

public interface RoleService extends SpiritAbstractService<RoleReqDTO, RoleRespDTO>{
	
	public List<RoleRespDTO> findAllRoles() throws SpiritServiceException;
	
	public List<RoleTreeVO> findRoleTree() throws SpiritServiceException;
	
	public List<AuthorityRoleTreeVO> findAuthoritysByRoleId(Long roleId) throws SpiritServiceException;
	
	
}
