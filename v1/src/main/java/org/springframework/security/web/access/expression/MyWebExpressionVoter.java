package org.springframework.security.web.access.expression;

import java.util.Collection;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionHandler;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-13 下午6:30:46
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-13 wangzhi 1.0
 */
public class MyWebExpressionVoter implements AccessDecisionVoter {
	private WebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();

	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		assert authentication != null;
		assert object != null;
		assert attributes != null;

		WebExpressionConfigAttribute weca = findConfigAttribute(attributes);

		if (weca == null) {
			return ACCESS_ABSTAIN;
		}

		FilterInvocation fi = (FilterInvocation) object;
		EvaluationContext ctx = expressionHandler.createEvaluationContext(
				authentication, fi);

		return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(),
				ctx) ? ACCESS_GRANTED : ACCESS_DENIED;
	}

	private WebExpressionConfigAttribute findConfigAttribute(
			Collection<ConfigAttribute> attributes) {
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof WebExpressionConfigAttribute) {
				WebExpressionConfigAttribute weca = (WebExpressionConfigAttribute) attribute;
				if (weca.getAuthorizeExpression().getExpressionString()
						.equals("MENU_PERMISSION")
						|| weca.getAuthorizeExpression().getExpressionString()
								.equals("URL_MAPPING_PERMISSION")) {
					continue;
				}
				return weca;
			}
		}
		return null;
	}

	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof WebExpressionConfigAttribute;
	}

	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(FilterInvocation.class);
	}

	public void setExpressionHandler(
			WebSecurityExpressionHandler expressionHandler) {
		this.expressionHandler = expressionHandler;
	}
}
