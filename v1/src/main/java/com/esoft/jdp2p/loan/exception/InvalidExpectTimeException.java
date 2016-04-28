package com.esoft.jdp2p.loan.exception;
/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  项目预计执行时间不合法
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-21 下午3:45:50  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-21      wangzhi      1.0          
 */
public class InvalidExpectTimeException extends Exception{
	public InvalidExpectTimeException(String msg) {
		super(msg);
	}
	public InvalidExpectTimeException() {
	}
}
