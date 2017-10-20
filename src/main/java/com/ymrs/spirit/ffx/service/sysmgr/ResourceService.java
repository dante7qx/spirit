package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.List;

import com.ymrs.spirit.ffx.dto.req.sysmgr.ResourceReqDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.ResourceRespDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.UserResourceRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.EasyUIDragTreeReq;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;
import com.ymrs.spirit.ffx.vo.sysmgr.ResourceTreeVO;

public interface ResourceService extends SpiritAbstractService<ResourceReqDTO, ResourceRespDTO>{
	
	/**
	 * 获取当前登录用户的所有菜单
	 * 
	 * @param userId
	 * @return
	 * @throws SpiritAPIServiceException
	 */
	public List<UserResourceRespDTO> findUserResourceByUserId(Long userId) throws SpiritServiceException;
	
	public List<ResourceRespDTO> findRootResource() throws SpiritServiceException;

	public List<ResourceRespDTO> findByPid(Long pid) throws SpiritServiceException;
	
	public List<ResourceTreeVO> findResourceTrees() throws SpiritServiceException;
	
	public void updateResourceWhenDrag(EasyUIDragTreeReq dragTreeReq) throws SpiritServiceException;
}
