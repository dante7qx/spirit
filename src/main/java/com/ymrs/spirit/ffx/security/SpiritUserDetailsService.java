package com.ymrs.spirit.ffx.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ymrs.spirit.ffx.dto.resp.sysmgr.UserAuthRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.service.sysmgr.UserService;

@Service
public class SpiritUserDetailsService implements UserDetailsService {
	
	private Logger LOGGER = LoggerFactory.getLogger(SpiritUserDetailsService.class);
	
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
		UserAuthRespDTO loginUserVO = null;
		try {
			loginUserVO = userService.loginAccount(account);
			if(loginUserVO == null) {
				throw new UsernameNotFoundException("用户名["+account+"]不存在！");
			}
		} catch (SpiritServiceException e) {
			LOGGER.error("loadUserByUsername error, Account:{}", account, e);
			throw new UsernameNotFoundException("用户名["+account+"]认证失败！", e);
		}
		return convertToSpiritPrincipal(loginUserVO);
	}

	private SpiritPrincipal convertToSpiritPrincipal(UserAuthRespDTO loginUserVO) {
		SpiritLoginUser spiritLoginUser = new SpiritLoginUser();
		BeanUtils.copyProperties(loginUserVO, spiritLoginUser);
		return new SpiritPrincipal(spiritLoginUser, loginUserVO.getPassword());
	}
}
