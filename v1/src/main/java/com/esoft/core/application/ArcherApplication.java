package com.esoft.core.application;

import java.util.HashMap;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;
import javax.faces.convert.Converter;
import javax.faces.validator.Validator;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.esoft.core.util.SpringBeanUtil;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-13 上午9:50:46
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-13 wangzhi 1.0
 */
public class ArcherApplication extends ApplicationWrapper {

	// Variables
	// ------------------------------------------------------------------------------------------------------

	private final Application wrapped;

	private Map<String, Class<? extends Converter>> converterIdMap = new HashMap<String, Class<? extends Converter>>();
	private Map<String, Class<? extends Validator>> validatorMap = new HashMap<String, Class<? extends Validator>>();

	// Constructors
	// ---------------------------------------------------------------------------------------------------

	/**
	 * Construct a new OmniFaces application around the given wrapped
	 * application.
	 * 
	 * @param wrapped
	 *            The wrapped application.
	 */
	public ArcherApplication(Application wrapped) {
		this.wrapped = wrapped;
	}

	// Actions
	// --------------------------------------------------------------------------------------------------------

	/**
	 * If the {@link ConverterProvider} is present and there's a CDI managed
	 * {@link Converter} instance available, then return it, else delegate to
	 * {@link #getWrapped()} which may return the JSF managed {@link Converter}
	 * instance.
	 */
	@Override
	public Converter createConverter(String converterId) {
		Class<? extends Converter> converterClass = converterIdMap
				.get(converterId);
		Converter converter = null;
		if (converterClass == null && !converterIdMap.containsKey(converterId)) {
			Converter converterOri = getWrapped().createConverter(converterId);

			if (converterOri != null) {
				converterClass = converterOri.getClass();
				Converter converterFromSpring = null;
				try {
					// 如果这个对象在spring管理之下，用spring初始化converter
					converterFromSpring = (Converter) SpringBeanUtil
							.getBeanByType(converterClass);
				} catch (NoSuchBeanDefinitionException e) {
				}
				if (converterFromSpring != null) {
					converterOri = converterFromSpring;
					converterIdMap.put(converterId, converterClass);
				}
				converter = converterOri;
			}

		} else {
			converter = (Converter) SpringBeanUtil
					.getBeanByType(converterClass);
		}

		return converter;
	}

	/**
	 * If the {@link ConverterProvider} is present and there's a CDI managed
	 * {@link Converter} instance available, then return it, else delegate to
	 * {@link #getWrapped()} which may return the JSF managed {@link Converter}
	 * instance.
	 */
	@Override
	public Converter createConverter(Class<?> targetClass) {
		Converter converter = null;
		try {
			// 如果这个对象在spring管理之下，用spring初始化converter
			converter = (Converter) SpringBeanUtil.getBeanByType(targetClass);
		} catch (NoSuchBeanDefinitionException e) {
		}

		if (converter == null) {
			converter = getWrapped().createConverter(targetClass);
		}

		return converter;
	}

	/**
	 * If the {@link ValidatorProvider} is present and there's a CDI managed
	 * {@link Validator} instance available, then return it, else delegate to
	 * {@link #getWrapped()} which may return the JSF managed {@link Validator}
	 * instance.
	 */
	@Override
	public Validator createValidator(String validatorId) throws FacesException {
		Class<? extends Validator> validatorClass = validatorMap
				.get(validatorId);
		Validator validator = null;
		if (validatorClass == null && !validatorMap.containsKey(validatorId)) {
			Validator validatorOri = getWrapped().createValidator(validatorId);

			if (validatorOri != null) {
				validatorClass = validatorOri.getClass();
				Validator validatorFromSpring = null;
				try {
					// 如果这个对象在spring管理之下，用spring初始化validator
					validatorFromSpring = (Validator) SpringBeanUtil
							.getBeanByType(validatorClass);
				} catch (NoSuchBeanDefinitionException e) {
				}
				if (validatorFromSpring != null) {
					validatorOri = validatorFromSpring;
					validatorMap.put(validatorId, validatorClass);
				}
				validator = validatorOri;
			}

		} else {
			validator = (Validator) SpringBeanUtil
					.getBeanByType(validatorClass);
		}

		return validator;
	}

	@Override
	public Application getWrapped() {
		return wrapped;
	}

}
