package com.esoft.core.jsf.validator;

import javax.annotation.Resource;
import javax.el.ValueExpression;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.validator.ValueChangeValidator;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.service.ValidationService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.sun.faces.util.MessageFactory;

//@Component
@FacesValidator(value = "com.esoft.core.validator.AlreadyExistValidator")
public class AlreadyExistValidator extends ValueChangeValidator implements
		PartialStateHolder {

	public static final String VALIDATOR_ID = "com.esoft.core.validator.AlreadyExistValidator";

	// 实体
	private String entityClass;

	// 字段名称
	private String fieldName;
	
	//是否验证的不存在
	private boolean isValidateNotExist = false;

	// @Resource
	ValidationService vdtService;

	@Override
	public void validateChangedObject(FacesContext context,
			UIComponent component, Object arg2) throws ValidatorException {
		String value = (String) arg2;
		if (StringUtils.isNotEmpty(value)) {
			try {
				boolean alreadyExist = false;
				vdtService = (ValidationService) SpringBeanUtil
						.getBeanByName("validationService");
				if (StringUtils.isEmpty(entityClass)) {
					ValueExpression ve = component.getValueExpression("value");
					String expr = ve.getExpressionString();
					expr = expr.substring(2, expr.length() - 1);
					String entityExpr = expr
							.substring(0, expr.lastIndexOf("."));
					String filedExpr = expr.substring(
							expr.lastIndexOf(".") + 1, expr.length());
					// Hard Code For referrer to validate
					if (filedExpr.equalsIgnoreCase("referrer")) {
						filedExpr = "username";
					}
					Object entity = FacesUtil.getExpressionValue("#{"
							+ entityExpr + "}");
					Class clazz = entity.getClass();
					alreadyExist = vdtService.isAlreadyExist(clazz, filedExpr,
							value);
				} else {
					alreadyExist = vdtService.isAlreadExist(entityClass,
							fieldName, value);
				}
				if (alreadyExist && !isValidateNotExist) {
					throw new ValidatorException(
							MessageFactory
									.getMessage(
											context,
											"com.esoft.core.validator.AlreadyExistValidator.ALREADY_EXIST",
											MessageFactory.getLabel(context,
													component), value));
				} else if (!alreadyExist && isValidateNotExist) {
					throw new ValidatorException(
							MessageFactory
									.getMessage(
											context,
											"com.esoft.core.validator.AlreadyExistValidator.NOT_EXIST",
											MessageFactory.getLabel(context,
													component), value));
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
						MessageFactory.getLabel(context, component),
						entityClass));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new ValidatorException(MessageFactory.getMessage(context,
						"javax.faces.converter.NO_SUCH_FIELD",
						MessageFactory.getLabel(context, component), "get"
								+ StringUtils.capitalize(fieldName)));
			}
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
			isValidateNotExist = (Boolean) values[3];
		}
	}

	@Override
	public Object saveState(FacesContext context) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (!initialStateMarked()) {
			Object values[] = new Object[3];
			values[0] = entityClass;
			values[1] = fieldName;
			values[2] = isValidateNotExist;			
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

	public boolean getIsValidateNotExist() {
		return isValidateNotExist;
	}

	public void setIsValidateNotExist(boolean isValidateNotExist) {
		this.isValidateNotExist = isValidateNotExist;
	}

}
