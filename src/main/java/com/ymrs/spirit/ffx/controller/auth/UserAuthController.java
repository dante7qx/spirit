package com.ymrs.spirit.ffx.controller.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ymrs.spirit.ffx.dto.auth.req.UserAuthReqDTO;
import com.ymrs.spirit.ffx.dto.auth.resp.UserAuthRespDTO;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserResourceRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.prop.SpiritProperties;
import com.ymrs.spirit.ffx.pub.BaseResp;
import com.ymrs.spirit.ffx.pub.RespCodeEnum;
import com.ymrs.spirit.ffx.service.auth.UserAuthService;
import com.ymrs.spirit.ffx.service.sysmgr.ResourceService;

/**
 * 用户认证 Controller
 * 
 * @author dante
 *
 */
@RestController
public class UserAuthController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthController.class);

	@Autowired
	private UserAuthService userAuthService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private SpiritProperties spiritProperties;

	/**
	 * 用户认证
	 * 
	 * @param userReqDTO
	 * @return
	 */
	@PostMapping("/auth/login")
	public BaseResp<UserAuthRespDTO> login(@RequestBody UserAuthReqDTO userReqDTO) {
		BaseResp<UserAuthRespDTO> result = new BaseResp<>();
		try {
			if (!checkParams(userReqDTO)) {
				throw new SpiritServiceException("请求参数非法，请检查后重试！");
			}
			UserAuthRespDTO authResp = userAuthService.login(userReqDTO);
			result.setData(authResp);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			LOGGER.error("auth account: {} error.", userReqDTO.getAccount(), e);
		}
		return result;
	}

	/**
	 * 刷新JWT Token
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/refresh/token")
	public BaseResp<String> refreshToken(HttpServletRequest request) {
		BaseResp<String> result = new BaseResp<>();
		try {
			String oldToken = request.getHeader(spiritProperties.getJwt().getHeader());
			if (!StringUtils.hasText(oldToken)) {
				throw new SpiritServiceException("未发现Token信息。");
			}
			String refreshToken = userAuthService.refreshToken(oldToken);
			result.setData(refreshToken);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			LOGGER.error("refreshToken error.", e);
		}
		return result;
	}

	/**
	 * 根据 userId 获取用户菜单资源
	 * 
	 * @param userId
	 * @return
	 */
	@PostMapping("/get_user_menu/{userId}")
	public BaseResp<List<UserResourceRespDTO>> getUserMenus(@PathVariable("userId") Long userId) {
		BaseResp<List<UserResourceRespDTO>> result = new BaseResp<>();
		try {
			List<UserResourceRespDTO> resources = resourceService.findUserResourceByUserId(userId);
			result.setData(resources);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			LOGGER.error("refreshToken error.", e);
		}
		return result;
	}

	/**
	 * 用户认证参数校验
	 * 
	 * @param userReqDTO
	 * @return
	 */
	private boolean checkParams(UserAuthReqDTO userReqDTO) {
		boolean valid = true;
		if (!StringUtils.hasText(userReqDTO.getAccount()) || !StringUtils.hasText(userReqDTO.getPassword())) {
			valid = false;
		}
		return valid;
	}

}
