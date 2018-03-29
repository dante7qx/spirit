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
import com.ymrs.spirit.ffx.dto.sysmgr.req.UserModifyPasswordReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.req.UserReqDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.BaseResp;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.pub.RespCodeEnum;
import com.ymrs.spirit.ffx.service.sysmgr.UserService;
import com.ymrs.spirit.ffx.util.LoginUserUtils;

@RestController
@RequestMapping("/sysmgr/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('sysmgr.user.query')")
	@PostMapping(value = "/query_page")
	public PageResult<UserRespDTO> queryUserPage(@RequestBody PageReq pageReq) {
		PageResult<UserRespDTO> result = null;
		try {
			result = userService.findPage(pageReq);
		} catch (SpiritServiceException e) {
			logger.error("queryUserPage {} error.", pageReq, e);
		}
		return result;
	}

	@PreAuthorize("hasAuthority('sysmgr.user.query')")
	@PostMapping(value = "/query_by_id/{id}")
	public BaseResp<UserRespDTO> queryByUserId(@PathVariable("id") Long id) {
		BaseResp<UserRespDTO> result = new BaseResp<UserRespDTO>();
		try {
			UserRespDTO userVO = userService.findById(id);
			result.setData(userVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("queryByUserId userId: {} error.", id, e);
		}
		return result;
	}

	@PreAuthorize("hasAuthority('sysmgr.user.query')")
	@PostMapping(value = "/query_by_account/{account}")
	public BaseResp<UserRespDTO> queryByAccount(@PathVariable("account") String account) {
		BaseResp<UserRespDTO> result = new BaseResp<>();
		try {
			UserRespDTO userVO = userService.findByAccount(account);
			result.setData(userVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("queryByAccount account: {} error.", account, e);
		}
		return result;
	}

	@PreAuthorize("hasAuthority('sysmgr.user.query')")
	@PostMapping(value = "/query_by_role_id/{roleId}")
	public List<UserRespDTO> queryByRoleId(@PathVariable("roleId") Long roleId) {
		List<UserRespDTO> result = Lists.newArrayList();
		try {
			result = userService.findByRoleId(roleId);
		} catch (SpiritServiceException e) {
			logger.error("queryByRoleId roleId: {} error.", roleId, e);
		}
		return result;
	}

	@PreAuthorize("hasAuthority('sysmgr.user.update')")
	@PostMapping(value = "/update_user")
	public BaseResp<UserRespDTO> updateUser(@RequestBody UserReqDTO userVO) {
		BaseResp<UserRespDTO> result = new BaseResp<>();
		try {
			userVO.setUpdateUser(LoginUserUtils.loginUserId());
			UserRespDTO userVOResp = userService.persist(userVO);
			result.setData(userVOResp);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("updateUser userVO: {} error.", userVO, e);
		}
		return result;
	}

	@PreAuthorize("hasAuthority('sysmgr.user.delete')")
	@PostMapping(value = "/delete_user")
	public BaseResp<?> deleteUser(@RequestBody UserReqDTO userVO) {
		BaseResp<?> result = new BaseResp<>();
		try {
			userVO.setUpdateUser(LoginUserUtils.loginUserId());
			userService.delete(userVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("deleteByUserId id: {} error.", userVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.user.update')")
	@PostMapping(value = "/lock_user")
	public BaseResp<?> lockUser(@RequestBody UserReqDTO userVO) {
		BaseResp<?> result = new BaseResp<>();
		try {
			userVO.setUpdateUser(LoginUserUtils.loginUserId());
			userService.lockUser(userVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("lockUser {} error.", userVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.user.update')")
	@PostMapping(value = "/release_lock_user")
	public BaseResp<?> releaseLockUser(@RequestBody UserReqDTO userVO) {
		BaseResp<?> result = new BaseResp<>();
		try {
			userVO.setUpdateUser(LoginUserUtils.loginUserId());
			userService.releaseLockUser(userVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("releaseLockUser {} error.", userVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.user.update')")
	@PostMapping(value = "/check_password")
	public BaseResp<Boolean> checkPassword(@RequestBody UserModifyPasswordReqDTO userModifyPasswordVO) {
		BaseResp<Boolean> result = new BaseResp<Boolean>();
		try {
			userModifyPasswordVO.setUpdateUser(LoginUserUtils.loginUserId());
			result.setData(userService.checkPassword(userModifyPasswordVO));
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("checkPassword {} error.", userModifyPasswordVO, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.user.update')")
	@PostMapping(value = "/modify_password")
	public BaseResp<?> modifyPassword(@RequestBody UserModifyPasswordReqDTO userModifyPasswordVO) {
		BaseResp<?> result = new BaseResp<>();
		try {
			userModifyPasswordVO.setUpdateUser(LoginUserUtils.loginUserId());
			userService.modifyPassword(userModifyPasswordVO);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			logger.error("modifyPassword {} error.", userModifyPasswordVO, e);
		}
		return result;
	}
}
