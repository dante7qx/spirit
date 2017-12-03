package com.ymrs.spirit.ffx.vo.sysmgr;

import lombok.Data;

/**
 * 定时任务 VO
 * 
 * @author dante
 *
 */
@Data
public class ScheduleJobVO {
	private String jobId;
	private String jobName;
	private String jobClass;
}
