package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.dto.sysmgr.req.AuthorityReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.AuthorityRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.EasyUIDragTreeReq;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;
import com.ymrs.spirit.ffx.vo.sysmgr.AuthorityTreeVO;

public interface AuthorityService extends SpiritAbstractService<AuthorityReqDTO, AuthorityRespDTO> {

	public List<AuthorityRespDTO> findRootAuthority() throws SpiritServiceException;

	public List<AuthorityRespDTO> findByPid(Long pid) throws SpiritServiceException;
	
	public List<AuthorityTreeVO> findAuthorityTrees() throws SpiritServiceException;
	
	public void updateAuthorityWhenDrag(EasyUIDragTreeReq dragTreeReq) throws SpiritServiceException;
}
