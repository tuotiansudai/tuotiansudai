package com.esoft.core.bean.factory.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import com.esoft.core.annotations.Logger;

public class LogBeanPostProcessor implements BeanPostProcessor {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		Class clazz = bean.getClass();
		initializeLog(bean, clazz);

		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		//
		/*
		 * List<Class<?>> clazzes = getAllClasses(bean); for (Class<?> clazz :
		 * clazzes) { // initializeLog(bean, clazz); }
		 */

		return bean;
	}

	private void initializeLog(final Object bean, final Class<? extends Object> clazz) {
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {

			@Override
			public void doWith(final Field field)
					throws IllegalArgumentException, IllegalAccessException {
				// 獲取是否可以方法的属性
				boolean visable = field.isAccessible();
				try {
					// 設置可以属性为可以访问
					field.setAccessible(true);
					if (field.get(bean) == null) {
						field.set(bean, LogFactory.getLog(clazz));
					}
				} catch (Exception e) {
					throw new BeanInitializationException(String.format(
							"初始化logger失败!bean=%s;field=%s", bean, field));
				} finally {
					// 恢复原来的访问修饰
					field.setAccessible(visable);
				}
			}
		}, new FieldFilter() {

			@Override
			public boolean matches(final Field field) {
				if (field.getAnnotation(Logger.class) == null) {
					return false;
				}
				if (!field.getType().isAssignableFrom(Log.class)) {
					return false;
				}
				return true;

			}
		});

	}

	/**
	 * 
	 * 獲取制定 bean 的 class 以及所有父类的列表,该列表中顺序为从父类中当前类
	 * 
	 * @param bean
	 * @return
	 * 
	 *         private List<Class<?>> getAllClasses(Object bean) { Class<?
	 *         extends Object> clazz = bean.getClass(); List<Class<?>> clazzes =
	 *         new ArrayList<Class<?>>(); while (clazz != null) {
	 *         clazzes.add(clazz); clazz = clazz.getSuperclass(); }
	 *         Collections.reverse(clazzes); return clazzes; }
	 */
}
