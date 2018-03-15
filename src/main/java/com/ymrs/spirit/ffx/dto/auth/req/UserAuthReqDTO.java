package com.ymrs.spirit.ffx.dto.auth.req;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户登录请求
 * 
 * @author dante
 *
 */
@Data
public class UserAuthReqDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String account;
	private String password;
	
}
