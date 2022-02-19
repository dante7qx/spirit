package com.ymrs.spirit.ffx.service.auth.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ymrs.spirit.ffx.constant.JWTConsts;
import com.ymrs.spirit.ffx.constant.SecurityConsts;
import com.ymrs.spirit.ffx.dao.sysmgr.AuthorityDAO;
import com.ymrs.spirit.ffx.dao.sysmgr.UserDAO;
import com.ymrs.spirit.ffx.dto.auth.req.UserAuthReqDTO;
import com.ymrs.spirit.ffx.dto.auth.resp.UserAuthRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.AuthorityPO;
import com.ymrs.spirit.ffx.po.sysmgr.RolePO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.prop.SpiritProperties;
import com.ymrs.spirit.ffx.security.SpiritJwtTokenUtils;
import com.ymrs.spirit.ffx.service.auth.LdapAuthenticationService;
import com.ymrs.spirit.ffx.service.auth.UserAuthService;
import com.ymrs.spirit.ffx.util.EncryptUtils;

@Service
@Transactional(readOnly = true)
public class UserAuthServiceImpl implements UserAuthService {

	private static final Logger Logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AuthorityDAO authorityDAO;
	@Autowired
	private LdapAuthenticationService ldapAuthenticationService;
	@Autowired
	private SpiritJwtTokenUtils spiritJwtTokenUtils;
	@Autowired
	private SpiritProperties spiritProperties;
	
	@Override
	public UserAuthRespDTO login(UserAuthReqDTO userAuthReq) throws SpiritServiceException {
		UserAuthRespDTO authRespDTO = new UserAuthRespDTO();
		final String account = userAuthReq.getAccount().trim();
		final String password = userAuthReq.getPassword().trim();
		try {
			UserPO userPO = userDAO.findByAccount(account);
			if(userPO == null) {
				throw new SpiritServiceException("用户[" + account + "]不存在。");
			}
			if(SecurityConsts.STATUS_LOCK.equalsIgnoreCase(userPO.getStatus())) {
				throw new SpiritServiceException("用户名[" + userPO + "]被锁定！");
			} else if(SecurityConsts.STATUS_DEL.equalsIgnoreCase(userPO.getStatus())) {
				throw new SpiritServiceException("用户名[" + account + "]已被删除！");
			}
			Boolean ldapUser = userPO.getLdapUser();
			if(ldapUser != null && ldapUser.booleanValue()) {
				if(!ldapAuthenticationService.authenticate(account, password)) {
					throw new SpiritServiceException("用户名或密码错误");
				}
			} else if(!EncryptUtils.match(password, userPO.getPassword())) {
				throw new SpiritServiceException("密码错误");
			}
			
			final String token = spiritJwtTokenUtils.generateToken(userPO.getId(), account);
			BeanUtils.copyProperties(userPO, authRespDTO);
			if(SecurityConsts.SUPER_ADMIN.equalsIgnoreCase(account)) {
				List<AuthorityPO> authoritys = authorityDAO.findAll();
				for (AuthorityPO authorityPO : authoritys) {
					authRespDTO.getAuthoritys().add(authorityPO.getCode());
				}
			} else {
				Set<RolePO> roles = userPO.getRoles();
				if (CollectionUtils.isEmpty(roles)) {
					return authRespDTO;
				}
				for (RolePO rolePo : roles) {
					Set<AuthorityPO> authoritys = rolePo.getAuthoritys();
					if (!CollectionUtils.isEmpty(authoritys)) {
						for (AuthorityPO authorityPO : authoritys) {
							authRespDTO.getAuthoritys().add(authorityPO.getCode());
						}
					}
				}
			}
			authRespDTO.setPassword("****");
			authRespDTO.setAccessToken(token);
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException(e);
		}
		return authRespDTO;
	}

	@Override
	public String refreshToken(String oldToken) throws SpiritServiceException {
		String refreshedToken = null;
		try {
			final String token = oldToken.substring(spiritProperties.getJwt().getTokenHead().length());
			String account = spiritJwtTokenUtils.getUsernameFromToken(token);
			UserPO userPO = userDAO.findById(Long.parseLong(account.split(JWTConsts.TOKEN_SPLIT)[1])).get();
			Date lastPwdUpdateDate = userPO.getLastPwdUpdateDate();
			if (spiritJwtTokenUtils.canTokenBeRefreshed(token, lastPwdUpdateDate)) {
				refreshedToken = spiritJwtTokenUtils.refreshToken(token);
			}
		} catch (Exception e) {
			Logger.error("Token 刷新失败", oldToken, e);
			throw new SpiritServiceException("Token " + oldToken + " 刷新失败", e);
		}
		return refreshedToken;
	}

}
