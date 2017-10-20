package com.ymrs.spirit.ffx.controller.sysmgr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dto.req.sysmgr.ResourceReqDTO;
import com.ymrs.spirit.ffx.dto.resp.sysmgr.ResourceRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.BaseResp;
import com.ymrs.spirit.ffx.pub.EasyUIDragTreeReq;
import com.ymrs.spirit.ffx.pub.RespCodeEnum;
import com.ymrs.spirit.ffx.service.sysmgr.ResourceService;
import com.ymrs.spirit.ffx.util.LoginUserUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.ResourceTreeVO;

@RestController
@RequestMapping("/sysmgr/resource")
public class ResourceController {
	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
	@Autowired
	private ResourceService resourceService;
	
	@PreAuthorize("hasAuthority('sysmgr.resource.query')")
	@PostMapping("/query_tree")
	public List<ResourceTreeVO> queryResourceTree() {
		List<ResourceTreeVO> trees = Lists.newArrayList();
		try {
			ResourceTreeVO root = new ResourceTreeVO();
			root.setId(-1L);
			root.setText("所有菜单");
			root.setState(EasyUITreeConsts.STATE_OPEN);
			List<ResourceTreeVO> childTrees = resourceService.findResourceTrees();
			root.setChildren(childTrees);
			trees.add(root);
		} catch (SpiritServiceException e) {
			logger.error("queryResourceTree error.", e);
		}
		return trees;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.resource.update')")
	@PostMapping("/update_resource")
	public BaseResp<ResourceRespDTO> updateResource(ResourceReqDTO resourceVO) {
		BaseResp<ResourceRespDTO> result = new BaseResp<>();
		try {
			resourceVO.setUpdateUser(LoginUserUtils.loginUserId());
			ResourceRespDTO resourceResp = resourceService.persist(resourceVO);
			result.setData(resourceResp);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("updateResource resourceVO: {} error.", resourceVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.resource.update')")
	@PostMapping("/update_when_drag")
	public BaseResp<?> updateResourceWhenDrap(EasyUIDragTreeReq dragTreeReq) {
		BaseResp<?> result = new BaseResp<>();
		try {
			dragTreeReq.setUpdateUser(LoginUserUtils.loginUserId());
			resourceService.updateResourceWhenDrag(dragTreeReq);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("updateResourceWhenDrap dragTree {} error.", dragTreeReq, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.resource.delete')")
	@PostMapping("/delete_by_id")
	public BaseResp<?> deleteByResourceId(Long id) {
		BaseResp<?> result = new BaseResp<>();
		try {
			resourceService.deleteById(id);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("deleteByResourceId id: {} error.", id, e);
		}
		return result;
	}
}
