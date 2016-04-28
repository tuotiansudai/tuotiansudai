package com.esoft.core.jsf.validator;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.omnifaces.validator.ValueChangeValidator;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.esoft.core.util.SpringBeanUtil;
import com.sun.faces.util.MessageFactory;

@FacesValidator(value = "com.esoft.core.validator.IdCardAlreadyExistValidator")
public class IdCardAlreadyExistValidator extends ValueChangeValidator implements
		PartialStateHolder {

	public static final String VALIDATOR_ID = "com.esoft.core.validator.IdCardAlreadyExistValidator";

	@Override
	public void validateChangedObject(FacesContext context,
			UIComponent component, Object arg2) throws ValidatorException {
		String value = (String) arg2;
		if (StringUtils.isNotEmpty(value)) {
			if (checkIdCardExist(value)) {
				throw new ValidatorException(
						MessageFactory
								.getMessage(
										context,
										"com.esoft.core.validator.AlreadyExistValidator.ALREADY_EXIST",
										MessageFactory.getLabel(context,
												component), value));
			}
		}

	}

	/**
	 * 检查该身份证号是否已存在，并且通过了实名认证
	 */
	private boolean checkIdCardExist(String idCard) {
		String hql = "from User u left join u.roles role where u.idCard=? and role.id=?";
		HibernateTemplate ht = (HibernateTemplate) SpringBeanUtil
				.getBeanByName("ht");
		if (ht.find(hql, idCard, "INVESTOR").size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isTransient() {
		return false;
	}

	@Override
	public void setTransient(boolean arg0) {

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

	@Override
	public Object saveState(FacesContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		// TODO Auto-generated method stub
		
	}

}
