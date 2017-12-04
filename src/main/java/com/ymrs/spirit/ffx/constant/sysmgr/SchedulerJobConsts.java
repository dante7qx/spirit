package com.ymrs.spirit.ffx.constant.sysmgr;

/**
 * 系统定时任务常量
 * 
 * @author dante
 *
 */
public final class SchedulerJobConsts {

	private SchedulerJobConsts() {
		throw new IllegalAccessError("SchedulerJobConsts 常量类，不能实例化！");
	}
	
	/**
	 * 定时任务Job所在包
	 */
	public final static String JOB_PKG = "com.ymrs.spirit.ffx.scheduler.job";
	
}
