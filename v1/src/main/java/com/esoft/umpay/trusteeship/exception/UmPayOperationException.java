package com.esoft.umpay.trusteeship.exception;

/**
 * 
 * Description : 联动优势操作异常
 * 
 * @author zt
 * @data 2015-3-11上午8:58:49
 */
@SuppressWarnings("serial")
public class UmPayOperationException extends RuntimeException {

	public UmPayOperationException(String msg) {
		super(msg);
	}

	public UmPayOperationException(String msg, Throwable t) {
		super(msg, t);
	}

	public UmPayOperationException(Throwable e) {
		super(e);
	}

}
