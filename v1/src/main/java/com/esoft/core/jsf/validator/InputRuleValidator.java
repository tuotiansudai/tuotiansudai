package com.esoft.core.jsf.validator;

import javax.annotation.Resource;
import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.util.Components;
import org.omnifaces.util.Messages;
import org.omnifaces.validator.ValueChangeValidator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.ValidationService;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.util.SpringBeanUtil;

//@Component
//@Scope(ScopeType.REQUEST)
@FacesValidator(value = "com.esoft.core.validator.InputRuleValidator")
public class InputRuleValidator extends ValueChangeValidator implements
		PartialStateHolder {

	public static final String VALIDATOR_ID = "com.esoft.core.validator.InputRuleValidator";

	// 规则编号
	private String ruleId;

	// 验证消息
	private String message;

	@Resource
	ValidationService vdtService;

	@Override
	public void validateChangedObject(FacesContext context,
			UIComponent component, Object arg2) throws ValidatorException {
		String value = (String) arg2;
		try {
			ValidationService vdtService = (ValidationService) SpringBeanUtil.getBeanByName("validationService");
			vdtService.inputRuleValidation(ruleId, value);
		} catch (NoMatchingObjectsException e) {
			e.printStackTrace();
			throw new ValidatorException(Messages.createError(
					"ruleId:{0} not found!", ruleId));
		} catch (InputRuleMatchingException e) {
			if (StringUtils.isNotEmpty(message)) {
				throw new ValidatorException(Messages.createError(message));
			} else {
				throw new ValidatorException(Messages.createError(
						e.getMessage(), Components.getLabel(component)));
			}
		}
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (state != null) {
			Object values[] = (Object[]) state;
			ruleId = (String) values[0];
			message = (String) values[1];
		}
	}

	@Override
	public Object saveState(FacesContext context) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (!initialStateMarked()) {
			Object values[] = new Object[2];
			values[0] = ruleId;
			values[1] = message;
			return (values);
		}
		return null;
	}

	private boolean transientValue = false;


    public boolean isTransient() {

        return (this.transientValue);

    }


    public void setTransient(boolean transientValue) {

        this.transientValue = transientValue;

    }

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		clearInitialState();
		this.ruleId = ruleId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		clearInitialState();
		this.message = message;
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
	
    public boolean equals(Object otherObj) {

        if (!(otherObj instanceof InputRuleValidator)) {
            return false;
        }
        InputRuleValidator other = (InputRuleValidator) otherObj;
        return StringUtils.equals(this.getRuleId(),other.getRuleId())
                && StringUtils.equals(this.getMessage(),other.getMessage());

    }


    public int hashCode() {

        if (StringUtils.isEmpty(this.getMessage()) && StringUtils.isEmpty(this.getRuleId())) {
			return super.hashCode();
		}
        int hashCode = 0;
        if (StringUtils.isNotEmpty(this.getMessage())) {
			hashCode += this.getMessage().hashCode();
		}
        if (StringUtils.isNotEmpty(this.getRuleId())) {
			hashCode += this.getRuleId().hashCode();
		}
        return (hashCode);

    }


}
