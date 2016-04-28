package com.esoft.core.jsf.tag;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagHandler;

import com.esoft.core.jsf.el.SpringSecurityELLibrary;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-3-13 下午5:14:30  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-3-13      wangzhi      1.0          
 */
public class IfAnyGrantedTag  extends TagHandler{
	private final TagAttribute roles;

	public void apply(FaceletContext faceletContext, UIComponent uiComponent)
			throws IOException, FacesException, FaceletException, ELException {
		if(this.roles == null){
			this.nextHandler.apply(faceletContext, uiComponent);
		}

		String roles = this.roles.getValue(faceletContext);
		if(roles == null || "".equals(roles.trim())){
			this.nextHandler.apply(faceletContext, uiComponent);
		}

		if(SpringSecurityELLibrary.ifAnyGranted(roles)){
			this.nextHandler.apply(faceletContext, uiComponent);
		}
	}

	public IfAnyGrantedTag(ComponentConfig componentConfig) {
		super(componentConfig);	
		this.roles = this.getRequiredAttribute("roles");
		if(this.roles == null)
			throw new TagAttributeException(this.roles,
					"The `roles` attribute has to be specified!");

	}

}
