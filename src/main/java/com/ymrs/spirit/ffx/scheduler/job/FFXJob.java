package com.ymrs.spirit.ffx.scheduler.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ymrs.spirit.ffx.scheduler.SpiritJob;
import com.ymrs.spirit.ffx.scheduler.annotation.EnableSpiritSchedule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableSpiritSchedule(jobId = "FFXJob", jobName = "框架演示Job")
public class FFXJob extends SpiritJob {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("FFX - 你好，尤娜！");
	}

}
