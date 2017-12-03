package com.ymrs.spirit.ffx.controller.login;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.ymrs.spirit.ffx.dto.sysmgr.resp.UserResourceRespDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.security.SpiritLoginUser;
import com.ymrs.spirit.ffx.service.sysmgr.ResourceService;
import com.ymrs.spirit.ffx.util.LoginUserUtils;

@Controller
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private ResourceService resourceService;

	@RequestMapping("/")
	public String index(Model model) {
		SpiritLoginUser loginUser = LoginUserUtils.loginUser();
		List<UserResourceRespDTO> menus = Lists.newLinkedList();
		try {
			menus = resourceService.findUserResourceByUserId(loginUser.getId());
		} catch (SpiritServiceException e) {
			LOGGER.error("用户 {} 访问系统错误", loginUser, e);
		}
		model.addAttribute("menus", menus);
		return "index/insdepleft";
	}
}
