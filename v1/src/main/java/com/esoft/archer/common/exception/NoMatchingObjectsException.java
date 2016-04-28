package com.esoft.archer.common.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:找不到对象 exception，即通过id或者其他条件，未查找到任何对象。
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-10 上午11:49:35
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-10 wangzhi 1.0
 */
public class NoMatchingObjectsException extends Exception {
	private Class clazz;

	public NoMatchingObjectsException(Class clazz, String msg, Throwable e) {
		super(msg, e);
		this.clazz = clazz;
	}
	
	public NoMatchingObjectsException(Class clazz, String msg) {
		super(msg);
		this.clazz = clazz;
	}

	public Class getClazz() {
		return clazz;
	}

}
