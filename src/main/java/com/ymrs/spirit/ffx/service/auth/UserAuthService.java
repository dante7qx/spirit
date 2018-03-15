package com.ymrs.spirit.ffx.service.auth;

import com.ymrs.spirit.ffx.dto.auth.req.UserAuthReqDTO;
import com.ymrs.spirit.ffx.dto.auth.resp.UserAuthRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;

public interface UserAuthService {

	/**
	 * 用户认证
	 * 
	 * @param userAuthReq
	 * @return
	 * @throws TornadoAPIServiceException
	 */
	public UserAuthRespDTO login(UserAuthReqDTO userAuthReq) throws SpiritServiceException;

	/**
	 * 刷新 JWT token
	 * 
	 * @param oldToken
	 * @return
	 * @throws TornadoAPIServiceException
	 */
	public String refreshToken(String oldToken) throws SpiritServiceException;
}
