package com.ymrs.spirit.ffx.service.sysmgr;

import com.ymrs.spirit.ffx.dto.sysmgr.ScheduleJobDTO;
import com.ymrs.spirit.ffx.exception.SpiritServiceException;
import com.ymrs.spirit.ffx.po.sysmgr.ScheduleJobPO;
import com.ymrs.spirit.ffx.template.SpiritAbstractService;

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
}
