package com.ymrs.spirit.ffx.controller.sysmgr;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ymrs.spirit.ffx.controller.SpiritController;
import com.ymrs.spirit.ffx.dto.sysmgr.ScheduleJobDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.pub.BaseResp;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.pub.RespCodeEnum;
import com.ymrs.spirit.ffx.service.sysmgr.ScheduleJobService;
import com.ymrs.spirit.ffx.util.LoginUserUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.ScheduleJobVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sysmgr/scheduler")
public class SchedulerJobController extends SpiritController {
	
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	@PreAuthorize("hasAuthority('sysmgr.scheduler.query')")
	@PostMapping(value = "/query_page")
	public PageResult<ScheduleJobDTO> queryScheduleJobPage(PageReq pageReq) {
		PageResult<ScheduleJobDTO> result = null;
		try {
			result = scheduleJobService.findPage(pageReq);
		} catch (SpiritServiceException e) {
			log.error("queryRolePage {} error.", pageReq, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.scheduler.query')")
	@PostMapping("/query_combo")
	public List<ScheduleJobVO> queryCombo() {
		return null;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.scheduler.query')")
	@PostMapping(value = "/query_by_id")
	public BaseResp<ScheduleJobDTO> queryByScheduleId(Long id) {
		BaseResp<ScheduleJobDTO> result = new BaseResp<>();
		try {
			ScheduleJobDTO job = scheduleJobService.findById(id);
			result.setData(job);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			log.error("queryByScheduleId roleId: {} error.", id, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.scheduler.update')")
	@PostMapping(value = "/update_scheduler")
	public BaseResp<ScheduleJobDTO> updateScheduler(ScheduleJobDTO job) {
		BaseResp<ScheduleJobDTO> result = new BaseResp<>();
		try {
			job.setUpdateUser(LoginUserUtils.loginUserId());
			ScheduleJobDTO jobResp = scheduleJobService.persist(job);
			result.setData(jobResp);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			log.error("updateScheduler scheduleJob: {} error.", job, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.scheduler.delete')")
	@PostMapping(value = "/delete_by_id")
	public BaseResp<?> deleteByScheduleId(Long id) {
		BaseResp<?> result = new BaseResp<>();
		try {
			scheduleJobService.deleteById(id);
		} catch (SpiritServiceException e) {
			result.setResultCode(RespCodeEnum.FAILURE.code());
			log.error("deleteByScheduleId id: {} error.", id, e);
		}
		return result;
	}
	
	@PreAuthorize("hasAuthority('sysmgr.scheduler.query')")
	@PostMapping("/check_job_exist")
	public boolean checkJobExist(ScheduleJobDTO schedulerDTO) {
		boolean exist = true;
		Long id = schedulerDTO.getId();
		String jobId = schedulerDTO.getJobId();
		try {
			exist = scheduleJobService.checkExistJob(id, jobId);
		} catch (SpiritServiceException e) {
			log.error("checkJobExist schedulerJob: {} error.", schedulerDTO, e);
		}
		return exist;
	}
	
}
