package com.ymrs.spirit.ffx.service.sysmgr;

import java.util.Date;
import java.util.List;

import com.ymrs.spirit.ffx.dto.sysmgr.ScheduleJobDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.ScheduleJobPO;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;
import com.ymrs.spirit.ffx.vo.sysmgr.ScheduleJobVO;

/**
 * 定时任务管理 Service
 * 
 * @author dante
 *
 */
public interface ScheduleJobService extends SpiritAbstractService<ScheduleJobDTO, ScheduleJobDTO> {
	
	/**
	 * 根据 jobId 获取定时任务Job
	 * 
	 * @param jobId
	 * @return
	 * @throws SpiritServiceException
	 */
	public ScheduleJobPO findByJobId(String jobId) throws SpiritServiceException;
	
	/**
	 * 检查 jobId 是否已经存在
	 * 
	 * @param id
	 * @param jobId
	 * @return
	 * @throws SpiritServiceException
	 */
	public boolean checkExistJob(Long id, String jobId) throws SpiritServiceException;
	
	/**
	 * 获取系统的Job
	 * 
	 * @return
	 */
	public List<ScheduleJobVO> findScheduleJobCombo();
	
	/**
	 * 更新运行时Job信息
	 * 
	 * @param jobId
	 * @param fireTime
	 * @param previousFireTime
	 * @param nextFireTime
	 * @param failReason
	 * @throws SpiritServiceException
	 */
	public void updateRuntimeJob(String jobId, Date fireTime, Date previousFireTime, Date nextFireTime, String failReason) throws SpiritServiceException;
}
