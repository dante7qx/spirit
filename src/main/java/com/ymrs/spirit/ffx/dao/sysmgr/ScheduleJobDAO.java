package com.ymrs.spirit.ffx.dao.sysmgr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ymrs.spirit.ffx.exception.SpiritDaoException;
import com.ymrs.spirit.ffx.po.sysmgr.ScheduleJobPO;

/**
 * 定时任务管理 DAO
 * 
 * @author dante
 *
 */
public interface ScheduleJobDAO extends JpaRepository<ScheduleJobPO, Long>, JpaSpecificationExecutor<ScheduleJobPO>{
	
	/**
	 * 根据jobId获取定时任务
	 * 
	 * @param jobId
	 * @return
	 * @throws SpiritDaoException
	 */
	public ScheduleJobPO findByJobId(String jobId) throws SpiritDaoException;
	
	/**
	 * 获取所有已启动的定时任务
	 * 
	 * @return
	 * @throws SpiritDaoException
	 */
	@Query("select s from ScheduleJobPO s where coalesce(startJob, 1) = 1 order by startTime asc")
	public List<ScheduleJobPO> findStartedJob() throws SpiritDaoException;
	
}
