package com.esoft.core.jsf.validator;

import com.esoft.archer.common.service.CaptchaService;
import com.esoft.core.util.SpringBeanUtil;
import com.sun.faces.util.MessageFactory;
import org.apache.commons.lang.StringUtils;
import org.omnifaces.validator.ValueChangeValidator;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

@FacesValidator(value = "com.esoft.core.validator.ValidateImageCaptchaValidator")
public class ValidateImageCaptchaValidator extends ValueChangeValidator implements
		PartialStateHolder {

	public static final String VALIDATOR_ID = "com.esoft.core.validator.ValidateImageCaptchaValidator";

	CaptchaService captchaService;

	private  static String imageCaptchaStatus = "{0}_image_captcha_status";

	@Override
	public void validateChangedObject(FacesContext context,
			UIComponent component, Object arg2) throws ValidatorException {
		String value = (String) arg2;
		HttpServletRequest request = (HttpServletRequest)context.getCurrentInstance().getExternalContext().getRequest();
		String sessionId = request.getSession().getId();
		captchaService = (CaptchaService) SpringBeanUtil.getBeanByName("captchaService");
		String captchaInRedis = captchaService.getValueInRedisByKey(sessionId);
		if(StringUtils.isEmpty(captchaInRedis)){
			throw new ValidatorException(
					MessageFactory
							.getMessage(
									context,
									"com.esoft.core.validator.ValidateImageCaptchaValidator.CAPTCHA_EXPIRE"));
		}else{
			if( !value.toUpperCase().equals(captchaInRedis)) {
				captchaService.deleteCaptchaFormRedis(sessionId);
				throw new ValidatorException(
						MessageFactory
								.getMessage(
										context,
										"com.esoft.core.validator.ValidateImageCaptchaValidator.CAPTCHA_WRONG"));
			}else{
				captchaService.deleteCaptchaFormRedis(sessionId);
				captchaService.generateCaptchaStatusInRedis(sessionId);
			}

		}


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
