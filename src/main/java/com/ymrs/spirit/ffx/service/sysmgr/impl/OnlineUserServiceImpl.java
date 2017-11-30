package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ymrs.spirit.ffx.constant.OnlineUserConsts;
import com.ymrs.spirit.ffx.service.sysmgr.OnlineUserService;
import com.ymrs.spirit.ffx.util.IPUtils;
import com.ymrs.spirit.ffx.util.JedisUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.OnlineUserVO;

@Service
public class OnlineUserServiceImpl implements OnlineUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OnlineUserServiceImpl.class);
	@Autowired
	private JedisUtils JedisUtils;

	@PostConstruct
	public void init() {
		LOGGER.info("每次重启时，清楚Redis中的在线用户！");
//		JedisUtils.delKey(OnlineUserConsts.ONLINE_KEY);
	}

	@Override
	public void addOnlineUser(String account, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		Map<String, Object> onlineMap = JedisUtils.hGetAll(OnlineUserConsts.ONLINE_KEY);
		if (onlineMap.containsKey(sessionId)) {
			JedisUtils.hdel(OnlineUserConsts.ONLINE_KEY, sessionId);
		} else {
			try {
				JedisUtils.hashSet(OnlineUserConsts.ONLINE_KEY, sessionId,
						new OnlineUserVO(session, account, IPUtils.getIpAddr(request)));
			} catch (JsonProcessingException e) {
				LOGGER.error("addOnlineUser error.", e);
			}
		}
	}

	@Override
	public void removeOnlineUser(HttpSession session) {
		JedisUtils.hdel(OnlineUserConsts.ONLINE_KEY, session.getId());
	}

}
