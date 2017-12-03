package com.ymrs.spirit.ffx.controller;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.ymrs.spirit.ffx.pub.SpecialDateEditor;

/**
 * 提供一些公共的处理，有需要时请继承此类
 * 
 * 1. 日期参数解析
 * 
 * @author dante
 *
 */
public abstract class SpiritController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new SpecialDateEditor());
	}
}
