package com.ymrs.spirit.ffx.controller.sysmgr;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.OnlineUserConsts;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.util.JedisUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.OnlineUserVO;

@RestController
@RequestMapping("/sysmgr/online")
public class OnlineUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineUserController.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private JedisUtils JedisUtils;
	@Autowired
    private FindByIndexNameSessionRepository<? extends ExpiringSession> sessions;

	@PreAuthorize("hasAuthority('sysmgr.user.query')")
	@PostMapping(value = "/query_page")
	public List<OnlineUserVO> queryOnlineUser() {
//		List<OnlineUserVO> onlineUsers = onlineUserService.onlineUser();
//		return onlineUsers;
		List<OnlineUserVO> onlineUsers = Lists.newArrayList();
		Map<String, Object> onlineMap = JedisUtils.hGetAll(OnlineUserConsts.ONLINE_KEY);
		try {
			for (String sessionId : onlineMap.keySet()) {  
				OnlineUserVO vo = mapper.readValue(onlineMap.get(sessionId).toString(), OnlineUserVO.class); 
				ExpiringSession expiringSession = sessions.getSession(sessionId);
				if(expiringSession != null) {
					vo.setLastAccessedTime(DateUtils.formatDateTime(new Date(expiringSession.getLastAccessedTime())));
				}
				onlineUsers.add(vo);
			}
		} catch (IOException e) {
			LOGGER.error("onlineUser error.", e);
		}
		return onlineUsers;
	}
	
	
}
