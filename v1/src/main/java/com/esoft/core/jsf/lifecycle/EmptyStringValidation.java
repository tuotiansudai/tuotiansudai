package com.esoft.core.jsf.lifecycle;

import java.util.EnumSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.util.Components;

import com.sun.faces.util.MessageFactory;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-6-24 上午10:49:28
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-6-24 wangzhi 1.0
 */
public class EmptyStringValidation implements PhaseListener {

	private static final Set<VisitHint> VISIT_HINTS = EnumSet
			.of(VisitHint.SKIP_UNRENDERED);

	@Override
	public void afterPhase(PhaseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// 验证提交时候，表单里面的空字符串
		final FacesContext fc = event.getFacesContext();
		UIForm form = Components.getCurrentForm();
		if (form != null) {
			form.visitTree(
					VisitContext.createVisitContext(fc, null, VISIT_HINTS),
					new VisitCallback() {

						@Override
						public VisitResult visit(VisitContext contextVisit,
								UIComponent component) {
							if (component instanceof UIInput) {
								UIInput uiInput = (UIInput) component;
								Object value = ((UIInput) component)
										.getSubmittedValue();
								if (value instanceof String) {
									//如果为空字符串
									if (StringUtils.isEmpty(value.toString()
											.trim())) {
										uiInput.setSubmittedValue("");
									}
								}
							}

							return VisitResult.ACCEPT;
						}
					});
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.PROCESS_VALIDATIONS;
	}

}
