package com.ymrs.spirit.ffx.controller.sysmgr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize("hasAuthority('sysmgr.user.query')")
	@PostMapping(value = "/query_page")
	public List<OnlineUserVO> queryRolePage() {
		List<OnlineUserVO> onlineUsers = onlineUserService.onlineUser();
		return onlineUsers;
	}
	
	
}
