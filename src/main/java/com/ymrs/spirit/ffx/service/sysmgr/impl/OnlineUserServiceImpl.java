package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.constant.OnlineUserConsts;
import com.ymrs.spirit.ffx.service.sysmgr.OnlineUserService;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.util.IPUtils;
import com.ymrs.spirit.ffx.util.JedisUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.OnlineUserVO;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineUserServiceImpl.class);
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private JedisUtils JedisUtils;
	@Autowired
    private FindByIndexNameSessionRepository<? extends ExpiringSession> sessionRepository;
	
	@PostConstruct
	public void init() {
		LOGGER.info("每次重启时，清楚Redis中的在线用户！");
		JedisUtils.delKey(OnlineUserConsts.ONLINE_KEY);
	}

	@Override
	public void addOnlineUser(String account, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		Map<String, Object> onlineMap = JedisUtils.hGetAll(OnlineUserConsts.ONLINE_KEY);
		if(onlineMap.containsKey(sessionId)) {
			JedisUtils.hdel(OnlineUserConsts.ONLINE_KEY, sessionId);
		} else {
			try {
				JedisUtils.hashSet(OnlineUserConsts.ONLINE_KEY, sessionId, new OnlineUserVO(session, account, IPUtils.getIpAddr(request)));
			} catch (JsonProcessingException e) {
				LOGGER.error("addOnlineUser error.", e);
			}
		}
	}

	@Override
	public void removeOnlineUser(HttpSession session) {
		JedisUtils.hdel(OnlineUserConsts.ONLINE_KEY, session.getId());
	}

	@Override
	public List<OnlineUserVO> onlineUser() {
		List<OnlineUserVO> onlineUsers = Lists.newArrayList();
		Map<String, Object> onlineMap = JedisUtils.hGetAll(OnlineUserConsts.ONLINE_KEY);
		try {
			for (String sessionId : onlineMap.keySet()) {  
				OnlineUserVO vo = mapper.readValue(onlineMap.get(sessionId).toString(), OnlineUserVO.class); 
				ExpiringSession expiringSession = sessionRepository.getSession(sessionId);
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
