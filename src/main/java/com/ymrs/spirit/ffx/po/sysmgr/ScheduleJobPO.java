package com.ymrs.spirit.ffx.po.sysmgr;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * 定时任务 PO
 * 
 * @author dante
 *
 */
@Data
@Entity
@Table(name = "t_schedule_job")
public class ScheduleJobPO {
	
	@Id
	@GeneratedValue
	private Long id;
	private String jobId;
	private String jobName;
	private String jobClass;
	private String jobDesc;
	private String cron;
	private Date previousFireTime;
	private Date fireTime;
	private Date nextFireTime;
	private Date startTime;
	private Boolean startJob;
	private String failReason;
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user")
	private UserPO updateUser;
	private Date updateDate;
}
