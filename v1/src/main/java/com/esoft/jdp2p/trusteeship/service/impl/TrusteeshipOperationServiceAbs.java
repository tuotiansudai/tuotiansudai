package com.esoft.jdp2p.trusteeship.service.impl;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.esoft.jdp2p.trusteeship.service.TrusteeshipOperationService;

public abstract class TrusteeshipOperationServiceAbs<T> implements
		TrusteeshipOperationService<T> {

	public void sendOperation(String content, String charset,
			FacesContext facesContext) throws IOException {
		ExternalContext ec = facesContext.getExternalContext();
		ec.responseReset();
		ec.setResponseCharacterEncoding(charset);
		ec.setResponseContentType("text/html");
		ec.getResponseOutputWriter().write(content);
		facesContext.responseComplete();
	}
}
