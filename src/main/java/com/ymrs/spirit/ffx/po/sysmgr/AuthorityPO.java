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
 * 权限 PO
 * 
 * @author dante
 *
 */
@Entity
@Table(name = "t_authority")
@Data
public class AuthorityPO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	/**
	 * 权限名称
	 */
	private String name;
	/**
	 * 权限code
	 */
	private String code;
	/**
	 * 权限描述
	 */
	private String authorityDesc;
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
	 * 父节点
	 */
	@ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	private AuthorityPO parentAuthority;

	public AuthorityPO() {
		// 默认构造函数
	}

	public AuthorityPO(Long id) {
		this.id = id;
	}

}
