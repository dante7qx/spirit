package com.ymrs.spirit.ffx.controller.sysmgr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.EasyUITreeConsts;
import com.ymrs.spirit.ffx.dto.sysmgr.req.AuthorityReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.AuthorityRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.BaseResp;
import com.ymrs.spirit.ffx.pub.EasyUIDragTreeReq;
import com.ymrs.spirit.ffx.pub.RespCodeEnum;
import com.ymrs.spirit.ffx.service.sysmgr.AuthorityService;
import com.ymrs.spirit.ffx.util.LoginUserUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.AuthorityTreeVO;

@RestController
@RequestMapping("/sysmgr/authority")
public class AuthorityController {
	private static final Logger logger = LoggerFactory.getLogger(AuthorityController.class);
	
	@Autowired
	private AuthorityService authorityService;
	
	@PreAuthorize("hasAuthority('sysmgr.authority.query')")
	@PostMapping("/query_tree")
	public List<AuthorityTreeVO> queryAuthorityTree() {
		List<AuthorityTreeVO> trees = Lists.newArrayList();
		try {
			AuthorityTreeVO root = new AuthorityTreeVO();
			root.setId(-1L);
			root.setText("所有权限");
			root.setState(EasyUITreeConsts.STATE_OPEN);
			List<AuthorityTreeVO> childTrees = authorityService.findAuthorityTrees();
			root.setChildren(childTrees);
			trees.add(root);
		} catch (SpiritServiceException e) {
			logger.error("queryAuthorityTree error.", e);
		}
		return trees;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.authority.query')")
	@PostMapping("/query_combotree")
	public List<AuthorityTreeVO> queryAuthorityComboTree() {
		List<AuthorityTreeVO> trees = Lists.newArrayList();
		try {
			trees = authorityService.findAuthorityTrees();
		} catch (SpiritServiceException e) {
			logger.error("queryAuthorityTree error.", e);
		}
		return trees;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.authority.update')")
	@PostMapping("/update_authority")
	public BaseResp<AuthorityRespDTO> updateAuthority(@RequestBody AuthorityReqDTO authorityVO) {
		BaseResp<AuthorityRespDTO> result = new BaseResp<>();
		try {
			authorityVO.setUpdateUser(LoginUserUtils.loginUserId());
			AuthorityRespDTO authorityResp = authorityService.persist(authorityVO);
			result.setData(authorityResp);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("updateAuthority authorityVO: {} error.", authorityVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.authority.update')")
	@PostMapping("/update_when_drag")
	public BaseResp<?> updateAuthorityWhenDrap(@RequestBody EasyUIDragTreeReq dragTreeReq) {
		BaseResp<?> result = new BaseResp<>();
		try {
			dragTreeReq.setUpdateUser(LoginUserUtils.loginUserId());
			authorityService.updateAuthorityWhenDrag(dragTreeReq);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("updateAuthorityWhenDrap dragTree {} error.", dragTreeReq, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.authority.delete')")
	@PostMapping("/delete_by_id/{id}")
	public BaseResp<?> deleteByAuthorityId(@PathVariable("id") Long id) {
		BaseResp<?> result = new BaseResp<>();
		try {
			authorityService.deleteById(id);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("deleteByAuthorityId id: {} error.", id, e);
		}
		return result;
	}
}
