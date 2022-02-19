package com.ymrs.spirit.ffx.service.sysmgr.impl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ymrs.spirit.ffx.constant.sysmgr.SchedulerJobConsts;
import com.ymrs.spirit.ffx.dao.sysmgr.ScheduleJobDAO;
import com.ymrs.spirit.ffx.dto.sysmgr.ScheduleJobDTO;
import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.ScheduleJobPO;
import com.ymrs.spirit.ffx.po.sysmgr.UserPO;
import com.ymrs.spirit.ffx.pub.PageReq;
import com.ymrs.spirit.ffx.pub.PageResult;
import com.ymrs.spirit.ffx.scheduler.SpiritScheduler;
import com.ymrs.spirit.ffx.scheduler.annotation.SpiritSchedule;
import com.ymrs.spirit.ffx.service.sysmgr.ScheduleJobService;
import com.ymrs.spirit.ffx.specification.sysmgr.SchedulerJobSpecification;
import com.ymrs.spirit.ffx.template.SpiritServiceTemplate;
import com.ymrs.spirit.ffx.util.AnnotationUtils;
import com.ymrs.spirit.ffx.util.CollectionUtils;
import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.vo.sysmgr.ScheduleJobVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务管理服务实现类
 * 
 * @author dante
 *
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class ScheduleJobServiceImpl extends SpiritServiceTemplate<ScheduleJobDTO, ScheduleJobDTO, ScheduleJobPO> implements ScheduleJobService {

	@Autowired
	private ScheduleJobDAO scheduleJobDAO;
	@Autowired
	private SpiritScheduler spiritScheduler;
	
	@PostConstruct
	public void init() {
		log.info("================= 启动所有定时任务 ================");
		List<ScheduleJobPO> startJobs;
		try {
			startJobs = scheduleJobDAO.findStartedJob();
			startJobs.stream().forEach(j -> {
				spiritScheduler.addJob(j.getJobId(), j.getJobClass(), j.getCron(), j.getStartTime());
			});
			spiritScheduler.startJobs();
		} catch (SpiritDaoException e) {
			log.error("Init start all job error.", e);
		}
	}
		
	@Override
	public PageResult<ScheduleJobDTO> findPage(PageReq pageReq) throws SpiritServiceException {
		return super.findEasyUIPage(pageReq);
	}

	@Override
	@Transactional
	public ScheduleJobDTO persist(ScheduleJobDTO reqDTO) throws SpiritServiceException {
		ScheduleJobPO jobPO = convertReqDtoToPo(reqDTO);
		boolean startJob = jobPO.getStartJob() != null ? jobPO.getStartJob().booleanValue() : false;
		if(jobPO.getId() != null) {
			ScheduleJobPO oldPO = scheduleJobDAO.getById(jobPO.getId());
			jobPO.setStartJob(startJob);
			jobPO.setFireTime(oldPO.getFireTime());
			jobPO.setPreviousFireTime(oldPO.getPreviousFireTime());
			jobPO.setNextFireTime(oldPO.getNextFireTime());
			jobPO.setFailReason(oldPO.getFailReason());
			if(startJob) {
				spiritScheduler.updateJobCron(jobPO.getJobId(), jobPO.getCron(), jobPO.getStartTime());
				spiritScheduler.resumeJob(jobPO.getJobId());
			} else {
				spiritScheduler.pauseJob(jobPO.getJobId());
			}
		} else {
			spiritScheduler.addJob(jobPO.getJobId(), jobPO.getJobClass(), jobPO.getCron(), jobPO.getStartTime(), startJob);
		}
		return convertPoToRespDto(scheduleJobDAO.save(jobPO));
	}

	@Override
	@Transactional
	public void deleteById(Long id) throws SpiritServiceException {
		ScheduleJobPO po = scheduleJobDAO.getById(id);
		if(po != null) {
			scheduleJobDAO.deleteById(id);
			spiritScheduler.removeJob(po.getJobId());
		}
	}

	@Override
	public void delete(ScheduleJobDTO reqDTO) throws SpiritServiceException {
		// 逻辑删除，此功能使用物理删除，故本方法不做实现
	}

	@Override
	public ScheduleJobDTO findById(Long id) throws SpiritServiceException {
		return convertPoToRespDto(scheduleJobDAO.getById(id));
	}
	
	@Override
	public ScheduleJobPO findByJobId(String jobId) throws SpiritServiceException {
		try {
			return scheduleJobDAO.findByJobId(jobId);
		} catch (SpiritDaoException e) {
			throw new SpiritServiceException("findByJobId " + jobId + " error.", e);
		}
	}
	
	@Override
	public boolean checkExistJob(Long id, String jobId) throws SpiritServiceException {
		boolean exists = false;
		ScheduleJobPO schedulerJob = findByJobId(jobId);
		if(schedulerJob != null) {
			if(id == null) {
				exists = true;
			} else if(!schedulerJob.getId().equals(id)) {
				exists = true;
			}
		}
		return exists;
	}
	
	@Override
	public List<ScheduleJobVO> findScheduleJobCombo() {
		List<ScheduleJobVO> vos = new ArrayList<>();
		List<Class<?>> clsList = AnnotationUtils.getClasses(SchedulerJobConsts.JOB_PKG);
		if(CollectionUtils.isNotEmpty(clsList)) {
			clsList.stream().forEach(c -> {
				Annotation[] annotations = c.getAnnotations();
				if (annotations != null && annotations.length > 0) {
					if (annotations[0] instanceof SpiritSchedule) {
						SpiritSchedule annotation = (SpiritSchedule) annotations[0];
						ScheduleJobVO vo = new ScheduleJobVO();
						vo.setJobId(annotation.jobId());
						vo.setJobName(annotation.jobName());
						vo.setJobClass(c.getName());
						vos.add(vo);
					}
				}
			});
		}
		return vos;
	}

	@Override
	@Transactional
	public void updateRuntimeJob(String jobId, Date fireTime, Date previousFireTime, Date nextFireTime,
			String failReason) throws SpiritServiceException {
		ScheduleJobPO job = findByJobId(jobId);
		if(job == null) {
			return;
		}
		job.setFireTime(fireTime);
		if(previousFireTime != null) {
			job.setPreviousFireTime(previousFireTime);
		}
		job.setNextFireTime(nextFireTime);
		job.setFailReason(failReason);
		job.setUpdateDate(DateUtils.currentDate());
		scheduleJobDAO.save(job);
	}

	@Override
	protected ScheduleJobPO convertReqDtoToPo(ScheduleJobDTO reqDTO) {
		ScheduleJobPO po = new ScheduleJobPO();
		BeanUtils.copyProperties(reqDTO, po, "updateUser");
		po.setUpdateDate(DateUtils.currentDate());
		if (reqDTO.getUpdateUser() != null) {
			po.setUpdateUser(new UserPO(reqDTO.getUpdateUser()));
		}
		return po;
	}

	@Override
	protected ScheduleJobDTO convertPoToRespDto(ScheduleJobPO po) {
		ScheduleJobDTO reqDTO = new ScheduleJobDTO();
		if(po != null) {
			BeanUtils.copyProperties(po, reqDTO, "updateUser");
			if (po.getUpdateUser() != null) {
				reqDTO.setUpdateUserName(po.getUpdateUser().getName());
			}
		}
		return reqDTO;
	}

	@Override
	protected Specification<ScheduleJobPO> buildSpecification(Map<String, Object> filter) {
		return SchedulerJobSpecification.querySpecification(filter);
	}

}
