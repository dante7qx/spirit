package com.ymrs.spirit.ffx.dto.sysmgr;

import java.util.Date;

import lombok.Data;

/**
 * 定时任务管理 DTO
 * 
 * @author dante
 *
 */
@Data
public class ScheduleJobDTO {
	private Long id;
	private String jobId;
	private String jobName;
	private String jobClass;
	private String jobDesc;
	private String cron;
	private Date previousFireTime;
	private Date fireTime;
	private Date nextFireTime;
	private Date startTime;
	private Boolean startJob;
	private String failReason;
	private Long updateUser;
	private String updateUserName;
	private String updateDate;
}
