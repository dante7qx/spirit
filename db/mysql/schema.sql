CREATE DATABASE IF NOT EXISTS `spirit-ffx` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE `spirit-ffx`;

DROP TABLE IF EXISTS t_user_role;
CREATE TABLE t_user_role (
	id bigint(20) NOT NULL, 
	user_id bigint(20) NOT NULL COMMENT '用户Id',
	role_id bigint(20) NOT NULL COMMENT '角色Id'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci
COMMENT='用户角色表' ;

DROP TABLE IF EXISTS t_role_authority;
CREATE TABLE t_role_authority (
	id bigint(20) NOT NULL, 
	role_id bigint(20) NOT NULL COMMENT '角色Id',
	authority_id bigint(20) NOT NULL COMMENT '权限Id'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8
COLLATE=utf8_general_ci
COMMENT='角色权限表' ;

DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (  
	id bigint(20) NOT NULL,
	account varchar(32) NOT NULL COMMENT '帐号',
	name varchar(64) NOT NULL COMMENT '姓名', 
	password varchar(256) NOT NULL COMMENT '密码', 
	email varchar(128) NOT NULL COMMENT '邮箱', 
	status varchar(6) NOT NULL DEFAULT 'NORMAL' COMMENT 'NORMAL: 正常。LOCK: 锁定。DEL: 删除。',
	last_pwd_update_date datetime NOT NULL COMMENT '密码最后修改时间',
	create_user bigint(20) NOT NULL default 1 COMMENT '创建人',
	create_date datetime NOT NULL default now() COMMENT '创建时间',
	update_user bigint(20) NOT NULL default 1 COMMENT '更新人',
	update_date datetime NOT NULL default now() COMMENT '更新时间',
	CONSTRAINT un_t_user_account UNIQUE KEY (account),
	CONSTRAINT un_t_user_email UNIQUE KEY (email)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COLLATE=utf8_general_ci
COMMENT='用户表' ;

DROP TABLE IF EXISTS t_resource;
CREATE TABLE t_resource (  
	id bigint(20) NOT NULL,  
	name varchar(64) NOT NULL COMMENT '资源名称',
	url varchar(128) NOT NULL COMMENT 'URL',
	pid bigint(20) NULL COMMENT '父节点Id',
	full_id varchar(128) NOT NULL DEFAULT '' COMMENT '层级Id',
	authority_id bigint(20) NOT NULL COMMENT '权限Id', 
	show_order int NOT NULL DEFAULT 1 COMMENT '显示顺序', 
	resource_desc varchar(128) NOT NULL DEFAULT '' COMMENT '资源描述',
	update_user bigint(20) not null DEFAULT 1 COMMENT '更新人',
	update_date datetime not null DEFAULT now() COMMENT '更新时间'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COLLATE=utf8_general_ci
COMMENT='资源表' ;

DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (  
	id bigint(20) NOT NULL,   
	name varchar(64) NOT NULL COMMENT '层级Id',
	role_desc varchar(128) NOT NULL COMMENT '角色描述',
	update_user bigint(20) not null DEFAULT 1 COMMENT '更新人',
	update_date datetime not null DEFAULT now() COMMENT '更新时间'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COLLATE=utf8_general_ci
COMMENT='角色表' ;

DROP TABLE IF EXISTS t_authority;
CREATE TABLE t_authority (  
	id bigint(20) NOT NULL,  
	code varchar(64) NOT NULL COMMENT '权限编码',
	name varchar(64) NOT NULL COMMENT '权限名称',
	authority_desc varchar(100) NOT NULL DEFAULT '' COMMENT '权限描述',
	pid bigint(20) NULL COMMENT '父节点Id',
	show_order int NOT NULL DEFAULT 1 COMMENT '更新时间',
	update_user bigint(20) not null DEFAULT 1 COMMENT '更新人',
	update_date datetime not null DEFAULT now() COMMENT '更新时间'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COLLATE=utf8_general_ci
COMMENT='权限表' ;
