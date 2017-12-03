package com.ymrs.spirit.ffx.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import com.ymrs.spirit.ffx.util.ExceptionUtils;

@Component
public class SpiritJobListener implements JobListener {
	
//	@Autowired
//	private SchedulerJobService schedulerJobService;

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
//		schedulerJobService.updateJob(jobId, context.getFireTime(), context.getPreviousFireTime(), context.getNextFireTime(), failReason);

	}

}
