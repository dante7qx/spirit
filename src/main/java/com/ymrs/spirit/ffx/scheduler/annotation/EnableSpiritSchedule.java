package com.ymrs.spirit.ffx.scheduler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface EnableSpiritSchedule {

	/**
	 * 任务编号，全局必须唯一
	 * 
	 * @return
	 */
	String jobId() default "";

	/**
	 * 任务名称
	 * 
	 * @return
	 */
	String jobName() default "";

	/**
	 * 任务是否废弃
	 * 
	 * @return
	 */
	boolean discard() default false;
}
