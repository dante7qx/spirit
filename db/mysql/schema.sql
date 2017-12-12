CREATE DATABASE IF NOT EXISTS `spirit_ffx` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE `spirit_ffx`;

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
	update_user bigint(20) NOT NULL default 1 COMMENT '更新人',
	update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
	icon_class varchar(64) NOT NULL DEFAULT 'fa fa-file' COMMENT '资源图标',
	update_user bigint(20) not null DEFAULT 1 COMMENT '更新人',
	update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
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
	update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
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
	authority_desc varchar(128) NOT NULL DEFAULT '' COMMENT '权限描述',
	pid bigint(20) NULL COMMENT '父节点Id',
	show_order int NOT NULL DEFAULT 1 COMMENT '显示顺序',
	update_user bigint(20) not null DEFAULT 1 COMMENT '更新人',
	update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COLLATE=utf8_general_ci
COMMENT='权限表' ;

DROP TABLE IF EXISTS t_schedule_job;
CREATE TABLE t_schedule_job (  
	id bigint(20) NOT NULL,  
	job_id varchar(64) not null COMMENT '任务编号',
	job_name varchar(256) not null COMMENT '任务名称',
	job_class varchar(256) not null COMMENT '任务执行类',
	job_desc varchar(2048) not null COMMENT '任务描述',
	cron varchar(64) not null COMMENT '定时表达式',
	previous_fire_time timestamp COMMENT '上次执行时间',
	fire_time timestamp null COMMENT '触发时间',
	next_fire_time timestamp null COMMENT '下次执行时间',
	start_time timestamp null COMMENT '开始执行时间',
	start_job int not null default 1 COMMENT '是否启动任务',
	fail_reason varchar(8000) COMMENT '任务失败原因',
	update_user bigint(20) not null DEFAULT 1 COMMENT '更新人',
	update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	CONSTRAINT un_t_schedule_job_job_id UNIQUE KEY (job_id)
) 
ENGINE=InnoDB 
DEFAULT CHARSET=utf8 
COLLATE=utf8_general_ci
COMMENT='定时任务表' ;


alter table t_schedule_job add constraint pk_t_schedule_job primary key (id) ;
alter table t_schedule_job modify column id bigint(20) not null auto_increment;