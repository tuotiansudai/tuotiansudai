package com.esoft.jdp2p.trusteeship.service;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 资金托管service，负责发起操作请求（充值、投标等等），与接收回调
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-31 下午3:49:10
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-31 wangzhi 1.0
 */
public interface TrusteeshipOperationService<T> {

	/**
	 * 创建操作请求，例如开户、充值等等
	 * 
	 * @return TrusteeshipOperation的编号
	 */
	public TrusteeshipOperation createOperation(T t, FacesContext facesContext)
			throws Exception;

	/**
	 * 接收操作回调(POST方式)，例如开户回调、充值回调
	 * 
	 * @return 需要返回的相应页面
	 */
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException,IOException;

	/**
	 * 接收操作回调(server to server方式)，例如开户回调、充值回调
	 */
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response);
}
