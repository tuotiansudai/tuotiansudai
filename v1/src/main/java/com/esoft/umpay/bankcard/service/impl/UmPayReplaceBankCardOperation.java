package com.esoft.umpay.bankcard.service.impl;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Service;

import com.esoft.jdp2p.bankcard.model.BankCard;
import com.esoft.jdp2p.trusteeship.exception.TrusteeshipReturnException;
import com.esoft.jdp2p.trusteeship.model.TrusteeshipOperation;
import com.esoft.umpay.trusteeship.service.UmPayOperationServiceAbs;


/**
 * 更换银行卡
 * @author zt
 */
@Service("umPayReplaceBankCardOperation")
public class UmPayReplaceBankCardOperation  extends UmPayOperationServiceAbs<BankCard>{

	
	
	@Override
	public TrusteeshipOperation createOperation(BankCard t,
			FacesContext facesContext) throws IOException {
		
		return null;
	}

	@Override
	public void receiveOperationPostCallback(ServletRequest request)
			throws TrusteeshipReturnException {
		
	}

	@Override
	public void receiveOperationS2SCallback(ServletRequest request,
			ServletResponse response) {
		
	}

}
