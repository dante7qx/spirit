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
import com.ymrs.spirit.ffx.dto.sysmgr.req.RoleReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.RoleRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.BaseResp;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.pub.RespCodeEnum;
import com.ymrs.spirit.ffx.service.sysmgr.RoleService;
import com.ymrs.spirit.ffx.util.LoginUserUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.AuthorityRoleTreeVO;
import com.ymrs.spirit.ffx.vo.sysmgr.RoleTreeVO;

@RestController
@RequestMapping("/sysmgr/role")
public class RoleController {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@PreAuthorize("hasAuthority('sysmgr.role.query')")
	@PostMapping(value = "/query_page")
	public PageResult<RoleRespDTO> queryRolePage(@RequestBody PageReq pageReq) {
		PageResult<RoleRespDTO> result = null;
		try {
			result = roleService.findPage(pageReq);
		} catch (SpiritServiceException e) {
			logger.error("queryRolePage {} error.", pageReq, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.role.query')")
	@PostMapping(value = "/query_role_tree")
	public List<RoleTreeVO> queryRoleTree() {
		RoleTreeVO root = new RoleTreeVO();
		root.setId(-1L);
		root.setText("所有角色");
		try {
			List<RoleTreeVO> children = roleService.findRoleTree();
			root.setChildren(children);
		} catch (Exception e) {
			logger.error("queryRoleTree error.", e);
		}
		return Lists.newArrayList(root);
	}
	
	@PreAuthorize("hasAuthority('sysmgr.role.query')")
	@PostMapping(value = "/query_by_id/{id}")
	public BaseResp<RoleRespDTO> queryByRoleId(@PathVariable("id") Long id) {
		BaseResp<RoleRespDTO> result = new BaseResp<>();
		try {
			RoleRespDTO roleVO = roleService.findById(id);
			result.setData(roleVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("queryByRoleId roleId: {} error.", id, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.role.update')")
	@PostMapping(value = "/update_role")
	public BaseResp<RoleRespDTO> updateRole(@RequestBody RoleReqDTO roleVO) {
		BaseResp<RoleRespDTO> result = new BaseResp<>();
		try {
			roleVO.setUpdateUser(LoginUserUtils.loginUserId());
			RoleRespDTO roleVOResp = roleService.persist(roleVO);
			result.setData(roleVOResp);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("updateRole roleVO: {} error.", roleVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.role.delete')")
	@PostMapping(value = "/delete_by_id/{id}")
	public BaseResp<?> deleteByRoleId(@PathVariable("id") Long id) {
		BaseResp<?> result = new BaseResp<>();
		try {
			roleService.deleteById(id);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("deleteByRoleId id: {} error.", id, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.role.query')")
	@PostMapping(value = "/query_role_authority/{roleId}")
	public List<AuthorityRoleTreeVO> findAuthorityRoleByRoleId(@PathVariable("roleId") Long roleId) {
		List<AuthorityRoleTreeVO> trees = null;
		try {
			trees = roleService.findAuthoritysByRoleId(roleId);
		} catch (SpiritServiceException e) {
			logger.error("findAuthorityRoleByRoleId roleId: {} error.", roleId, e);
		}
		return trees;
	}
}
