package com.ymrs.spirit.ffx.pub;

/**
 * 请求返回状态码
 * 
 * @author dante
 *
 */
public enum RespCodeEnum {
	SUCCESS(1, "响应成功"), 
	LACK_PARAM(10001, "缺少参数"), 
	FORMAT_PARAM(10002, "参数格式错误"),
	REMOTE_FAILURE(90000, "API调用失败"),
	FAILURE(-1, "");

	private int code;
	private String val;

	private RespCodeEnum(int code, String val) {
		this.code = code;
		this.val = val;
	}

	public int code() {
		return code;
	}

	public String Val() {
		return val;
	}

}
