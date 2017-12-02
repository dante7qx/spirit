package com.ymrs.spirit.ffx.util;

import java.util.Collection;

/**
 * 集合工具类
 * 
 * @author dante
 *
 */
public abstract class CollectionUtils extends org.springframework.util.CollectionUtils {

	/**
	 * 如果传入集合不为 null 或 empty，返回 true， 否则返回 false
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}
	
}
