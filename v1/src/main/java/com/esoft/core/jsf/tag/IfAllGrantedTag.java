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
 * Taglib to combine the Spring-Security Project with Facelets <br />
 * 
 * This is the Class responsible for making the <br />
 * <code><br />
 *     &lt;sec:ifAllGranted roles=&quot;ROLE_USER,ROLE_EXAMPLE&quot;&gt;<br />
 *         The components you want to show only when the condition holds <br />
 *     lt;/sec:ifAllGranted&gt;<br />
 * </code> work.
 * 
 * 
 * @author Dominik Dorn - http://www.dominikdorn.com/
 * @version %I%, %G%
 * @since 0.1
 */
public class IfAllGrantedTag extends TagHandler {

	private final TagAttribute roles;

	public void apply(FaceletContext faceletContext, UIComponent uiComponent)
			throws IOException, FacesException, FaceletException, ELException {
		if (this.roles == null){
			this.nextHandler.apply(faceletContext, uiComponent);
		}

		String roles = this.roles.getValue(faceletContext);
		if (roles == null || "".equals(roles.trim())){
			this.nextHandler.apply(faceletContext, uiComponent);
		}

		if (SpringSecurityELLibrary.ifAllGranted(roles))
			this.nextHandler.apply(faceletContext, uiComponent);
	}

	public IfAllGrantedTag(ComponentConfig componentConfig) {
		super(componentConfig);
		this.roles = this.getRequiredAttribute("roles");
		if (this.roles == null)
			throw new TagAttributeException(this.roles,
					"The `roles` attribute has to be specified!");
	}
}
