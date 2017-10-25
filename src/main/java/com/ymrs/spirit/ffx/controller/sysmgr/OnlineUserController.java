package com.ymrs.spirit.ffx.controller.sysmgr;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ymrs.spirit.ffx.service.sysmgr.OnlineUserService;
import com.ymrs.spirit.ffx.vo.sysmgr.OnlineUserVO;

@RestController
@RequestMapping("/sysmgr/online")
public class OnlineUserController {
	
	@Autowired
	private OnlineUserService onlineUserService;

	@PostMapping(value = "/query_page")
	public List<OnlineUserVO> queryRolePage(Principal principal, HttpSession session) {
		List<OnlineUserVO> onlineUsers = onlineUserService.onlineUser();
		return onlineUsers;
	}
	
	
}
