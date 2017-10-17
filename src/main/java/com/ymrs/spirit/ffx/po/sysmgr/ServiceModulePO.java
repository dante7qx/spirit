package com.ymrs.spirit.ffx.po.sysmgr;

import java.io.Serializable;
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
 * 服务模块 PO
 * 
 * @author dante
 *
 */
@Entity
@Table(name = "t_service_module")
@Data
public class ServiceModulePO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 服务名
	 */
	private String name;
	/**
	 * 服务url
	 */
	private String url;
	/**
	 * 更新人
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "update_user")
	private UserPO updateUser;
	/**
	 * 更新时间
	 */
	private Date updateDate;

	public ServiceModulePO() {
		// 默认构造函数
	}

	public ServiceModulePO(Long id) {
		this.id = id;
	}

}
