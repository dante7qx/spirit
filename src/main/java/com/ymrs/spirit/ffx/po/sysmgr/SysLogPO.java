package com.ymrs.spirit.ffx.po.sysmgr;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection="syslog")
public class SysLogPO {
	
	@Id
	private String id;
	private String account;
	private String ip;
	@Field("requestmethod")
	private String requestMethod;
	private String url;
	private String uri;
	private String clazz;
	@Field("methodname")
	private String methodName;
	@Field("visittime")
	private Date visitTime;
	@Field("spendtime")
	private Long spendTime;
	private String params;
	
}
