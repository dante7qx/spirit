package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.dto.req.sysmgr.AuthorityReqDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.AuthorityRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;

public interface AuthorityService extends SpiritAbstractService<AuthorityReqDTO, AuthorityRespDTO> {

	public List<AuthorityRespDTO> findRootAuthority() throws SpiritServiceException;

	public List<AuthorityRespDTO> findByPid(Long pid) throws SpiritServiceException;
}
