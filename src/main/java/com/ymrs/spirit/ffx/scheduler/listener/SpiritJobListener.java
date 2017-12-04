package com.ymrs.spirit.ffx.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.service.sysmgr.ScheduleJobService;
import com.ymrs.spirit.ffx.util.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpiritJobListener implements JobListener {
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	@Override
	public String getName() {
		return "SpiritJobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		String jobId = context.getJobDetail().getKey().getName();
		String failReason = null;
		if(jobException != null) {
			failReason = ExceptionUtils.getStackMsg(jobException);
		}
		try {
			scheduleJobService.updateRuntimeJob(jobId, context.getFireTime(), context.getPreviousFireTime(), context.getNextFireTime(), failReason);
		} catch (SpiritServiceException e) {
			log.error("updateRuntimeJob {} error", jobId, e);
		}

	}

}
