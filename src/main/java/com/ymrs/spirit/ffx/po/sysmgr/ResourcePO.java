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
 * 资源 PO
 * 
 * @author dante
 *
 */
@Entity
@Table(name = "t_resource")
@Data
public class ResourcePO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 资源名称
	 */
	private String name;
	/**
	 * 访问 url
	 */
	private String url;
	/**
	 * 层级Id
	 */
	private String fullId;
	/**
	 * 显示顺序
	 */
	private Integer showOrder;
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
	/**
	 * 服务模块
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "service_module_id")
	private ServiceModulePO serviceModule;

	/**
	 * 父节点
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	private ResourcePO parentResource;

	/**
	 * 权限
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "authority_id")
	private AuthorityPO authority;

	public ResourcePO() {
		// 默认构造函数
	}

	public ResourcePO(Long id) {
		this.id = id;
	}

}
