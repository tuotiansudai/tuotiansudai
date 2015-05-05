package com.esoft.core.jsf.component;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omnifaces.component.script.OnloadScript;
import org.omnifaces.util.Components;
import org.omnifaces.util.State;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-25 下午6:18:50
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-25 wangzhi 1.0
 */
@FacesComponent(ArcherMessages.COMPONENT_TYPE)
@ResourceDependencies({
		// FIXME:缺少一个弹出层的class导入
		@ResourceDependency(library = "javax.faces", name = "jsf.js", target = "head"),
		@ResourceDependency(library = "primefaces", name = "jquery/jquery.js"),
		@ResourceDependency(library = "esoft", name = "style/archerMessage.css"),
		@ResourceDependency(library = "esoft", name = "js/archerMessages.js", target = "head") })
public class ArcherMessages extends OnloadScript {

	// Public constants
	// -----------------------------------------------------------------------------------------------

	/** The standard component type. */
	public static final String COMPONENT_TYPE = "com.esoft.core.component.ArcherMessages";

	// Private constants
	// ----------------------------------------------------------------------------------------------

	private static final Set<VisitHint> VISIT_HINTS = EnumSet
			.of(VisitHint.SKIP_UNRENDERED);
	private static final String DEFAULT_STYLECLASS = "error";
	private static final Boolean DEFAULT_FOCUS = TRUE;
	private static final Boolean DEFAULT_SHOWGLOBALMESSAGES = FALSE;
	private static final String DEFAULT_SEVERITY = "0,1,2,3";
	private static final String SCRIPT = "$(function(){Archer.Messages.showMessages('%s','%s', %s, %s);})";
	private static final String MESSAGE = "{'clientId':'%s','message':{'severity':'%s','summary':'%s','detail':'%s'}}";
	private static final String MESSAGE_NONE = "{'clientId':'%s','message':''}";

	private enum PropertyKeys {
		// Cannot be uppercased. They have to exactly match the attribute names.
		styleClass, focus, showGlobalMessages, severity
	}

	// Variables
	// ------------------------------------------------------------------------------------------------------

	private final State state = new State(getStateHelper());

	// Actions
	// --------------------------------------------------------------------------------------------------------

	/**
	 * Visit all components of the current {@link UIForm}, check if they are an
	 * instance of {@link UIInput} and are not {@link UIInput#isValid()} and
	 * finally append them to an array in JSON format and render the script.
	 * <p>
	 * Note that the {@link FacesContext#getClientIdsWithMessages()} could also
	 * be consulted, but it does not indicate whether the components associated
	 * with those client IDs are actually {@link UIInput} components which are
	 * not {@link UIInput#isValid()}. Also note that the highlighting is been
	 * done by delegating the job to JavaScript instead of directly changing the
	 * component's own <code>styleClass</code> attribute; this is chosen so
	 * because we don't want the changed style class to be saved in the server
	 * side view state as it may result in potential inconsistencies because
	 * it's supposed to be an one-time change.
	 */
	@Override
	public void encodeChildren(final FacesContext context) throws IOException {
//		if (context.isPostback()) {
			UIForm form = Components.getCurrentForm();
			final JSONArray messages = new JSONArray();
			if (form != null) {
				if (context.isValidationFailed()) {
					// FIXME:应该在标签里面能指定，showSummary和showDetail，类似于primefaces的growl
					
					form.visitTree(VisitContext.createVisitContext(context,
							null, VISIT_HINTS), new VisitCallback() {

						@Override
						public VisitResult visit(VisitContext contextVisit,
								UIComponent component) {
							if (component instanceof UIInput) {
								// if (!((UIInput) component).isValid()) {
								String clientId = component
										.getClientId(context);
								List<FacesMessage> msgs = context
										.getMessageList(clientId);
								JSONObject jo = null;
								if (msgs.size() >= 1) {
									try {
										// 验证失败，需要显示消息的组件
										jo = new JSONObject(String.format(
												MESSAGE, clientId, msgs.get(0)
												.getSeverity()
												.getOrdinal(), msgs
												.get(0).getSummary().replace("\'", "\\\'"),
										msgs.get(0).getDetail().replace("\'", "\\\'")));
										messages.put(jo);
									} catch (JSONException e) {
										throw new RuntimeException(
												"jsonString: "
														+ String.format(
																MESSAGE,
																clientId,
																msgs.get(0)
																		.getSeverity()
																		.getOrdinal(),
																msgs.get(0)
																		.getSummary(),
																msgs.get(0)
																		.getDetail()));
									}
								}
								// } else {
								// }
							}

							return VisitResult.ACCEPT;
						}
					});
				}
				if (context.getPartialViewContext().isPartialRequest()) {
					// ajax 请求
					for (String cId : context.getPartialViewContext()
							.getExecuteIds()) {
						if (context.getMessageList(cId).size() == 0) {
							try {
								JSONObject jo = new JSONObject(String.format(
										MESSAGE_NONE, cId));
								messages.put(jo);
							} catch (JSONException e) {
								throw new RuntimeException("jsonString: "
										+ String.format(MESSAGE_NONE, cId));
							}
						}
					}

				}

			}

			Iterator<FacesMessage> msgsWithoutId = context.getMessages(null);
			while (msgsWithoutId.hasNext()) {
				FacesMessage facesMessage = (FacesMessage) msgsWithoutId.next();
				int severityLevel = facesMessage.getSeverity().getOrdinal();
				//消息级别
				if (getSeverity().indexOf(String.valueOf(severityLevel)) == -1) {
					continue;
				}

				if (StringUtils.isEmpty(facesMessage.getSummary())) {
					continue;
				}
				try {
					messages.put(new JSONObject(
							String.format(MESSAGE, "", facesMessage
									.getSeverity().getOrdinal(), facesMessage
									.getSummary(), facesMessage.getDetail())));
				} catch (JSONException e) {
					throw new RuntimeException("jsonString: "
							+ String.format(MESSAGE, "", facesMessage
									.getSeverity().getOrdinal(), facesMessage
									.getSummary(), facesMessage.getDetail()));
				}
			}
			if (messages.length() > 0) {
				context.getResponseWriter().write(
						String.format(SCRIPT,
								messages.toString().replace("\\\"", "\\\\\"").replace("\'", "\\\'"),
								getStyleClass(), isFocus(),
								isShowGlobalMessages()));
			}
		}
//	}

	// Getters/setters
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns the error style class which is to be applied on invalid inputs.
	 * Defaults to <code>error</code>.
	 * 
	 * @return The error style class which is to be applied on invalid inputs.
	 */
	public String getStyleClass() {
		return state.get(PropertyKeys.styleClass, DEFAULT_STYLECLASS);
	}

	/**
	 * Sets the error style class which is to be applied on invalid inputs.
	 * 
	 * @param styleClass
	 *            The error style class which is to be applied on invalid
	 *            inputs.
	 */
	public void setStyleClass(String styleClass) {
		state.put(PropertyKeys.styleClass, styleClass);
	}

	/**
	 * Returns whether the first error element should gain focus. Defaults to
	 * <code>true</code>.
	 * 
	 * @return Whether the first error element should gain focus.
	 */
	public Boolean isFocus() {
		return state.get(PropertyKeys.focus, DEFAULT_FOCUS);
	}

	/**
	 * Sets whether the first error element should gain focus.
	 * 
	 * @param focus
	 *            Whether the first error element should gain focus.
	 */
	public void setFocus(Boolean focus) {
		state.put(PropertyKeys.focus, focus);
	}

	public Boolean isShowGlobalMessages() {
		return state.get(PropertyKeys.showGlobalMessages,
				DEFAULT_SHOWGLOBALMESSAGES);
	}

	public void setShowGlobalMessages(Boolean showGlobalMessages) {
		state.put(PropertyKeys.showGlobalMessages, showGlobalMessages);
	}

	public java.lang.String getSeverity() {
		return (java.lang.String) getStateHelper().eval(PropertyKeys.severity,
				DEFAULT_SEVERITY);
	}

	public void setSeverity(java.lang.String _severity) {
		getStateHelper().put(PropertyKeys.severity, _severity);
	}

}
