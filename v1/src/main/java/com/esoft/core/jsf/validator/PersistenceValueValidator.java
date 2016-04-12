package com.esoft.core.jsf.validator;

import javax.annotation.Resource;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.service.ValidationService;
import com.sun.faces.util.MessageFactory;

@Component
@FacesValidator(value = "com.esoft.core.validator.PersistenceValueValidator")
public class PersistenceValueValidator extends ValueChangeValidator implements
		PartialStateHolder {

	public static final String VALIDATOR_ID = "com.esoft.core.validator.PersistenceValueValidator";

	// 实体
	private String entityClass;

	// 字段名称
	private String fieldName;

	// 实体id
	private String id;

	// 提示消息
	private String message;

	@Resource
	ValidationService vdtService;

	@Override
	public void validateChangedObject(FacesContext context,
			UIComponent component, Object arg2) throws ValidatorException {
		String value = (String) arg2;
		if (StringUtils.isEmpty(value)) {
			throw new ValidatorException(MessageFactory.getMessage(context,
					"javax.faces.component.UIInput.REQUIRED",
					MessageFactory.getLabel(context, component)));
		}
		try {
			if (!vdtService.equalsPersistenceValue(entityClass, fieldName, id,
					value)) {
				if (StringUtils.isNotEmpty(message)) {
					throw new ValidatorException(Messages.createError(message));
				} else {
					throw new ValidatorException(
							MessageFactory
									.getMessage(
											context,
											"com.esoft.core.validator.AlreadyExistValidator.PERSISTENCE_VALUE",
											MessageFactory.getLabel(context,
													component)));
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new ValidatorException(MessageFactory.getMessage(context,
					"javax.faces.converter.NO_SUCH_FIELD",
					MessageFactory.getLabel(context, component), "get"
							+ StringUtils.capitalize(fieldName)));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ValidatorException(MessageFactory.getMessage(context,
					"javax.faces.converter.CLASS_NOT_FOUND",
					MessageFactory.getLabel(context, component), entityClass));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new ValidatorException(MessageFactory.getMessage(context,
					"javax.faces.converter.NO_SUCH_FIELD",
					MessageFactory.getLabel(context, component), "get"
							+ StringUtils.capitalize(fieldName)));
		}

	}

	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (state != null) {
			Object values[] = (Object[]) state;
			entityClass = (String) values[0];
			fieldName = (String) values[1];
			id = (String) values[2];
			message = (String) values[3];
		}
	}

	@Override
	public Object saveState(FacesContext context) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (!initialStateMarked()) {
			Object values[] = new Object[4];
			values[0] = entityClass;
			values[1] = fieldName;
			values[2] = id;
			values[3] = message;
			return (values);
		}
		return null;
	}

	@Override
	public void setTransient(boolean arg0) {

	}

	public String getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(String entityClass) {
		this.entityClass = entityClass;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	private boolean initialState;

	public void markInitialState() {
		initialState = true;
	}

	public boolean initialStateMarked() {
		return initialState;
	}

	public void clearInitialState() {
		initialState = false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
